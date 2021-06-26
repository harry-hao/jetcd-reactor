package io.github.harry_hao.etcd.jetcd.reactive;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import io.etcd.jetcd.auth.AuthRoleGetResponse;
import io.etcd.jetcd.auth.AuthRoleListResponse;
import io.etcd.jetcd.auth.Permission;
import io.etcd.jetcd.test.EtcdClusterExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static io.github.harry_hao.etcd.jetcd.reactive.TestUtil.bytesOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReactiveAuthClientTest {

    @RegisterExtension
    public static EtcdClusterExtension cluster = new EtcdClusterExtension("auth-etcd", 1, false);

    private final ByteSequence rootRoleKey = bytesOf("root");
    private final ByteSequence rootRoleValue = bytesOf("b");
    private final ByteSequence rootRoleKeyRangeBegin = bytesOf("root");
    private final ByteSequence rootRoleKeyRangeEnd = bytesOf("root1");

    private final ByteSequence userRoleKey = bytesOf("foo");
    private final ByteSequence userRoleValue = bytesOf("bar");
    private final ByteSequence userRoleKeyRangeBegin = bytesOf("foo");
    private final ByteSequence userRoleKeyRangeEnd = bytesOf("foo1");

    private final String rootString = "root";
    private final ByteSequence root = bytesOf(rootString);
    private final ByteSequence rootPass = bytesOf("123");
    private final String rootRoleString = "root";
    private final ByteSequence rootRole = bytesOf(rootRoleString);

    private final String userString = "user";
    private final ByteSequence user = bytesOf(userString);
    private final ByteSequence userPass = bytesOf("userPass");
    private final ByteSequence userNewPass = bytesOf("newUserPass");
    private final String userRoleString = "userRole";
    private final ByteSequence userRole = bytesOf(userRoleString);

    private static ReactiveAuth authDisabledAuthClient;
    private static ReactiveKV authDisabledKVClient;

    private static List<URI> endpoints;
    private static Duration timeout = Duration.ofSeconds(1);

    /**
     * Build etcd client to create role, permission.
     */
    @BeforeAll
    public static void setupEnv() {
        ReactiveAuthClientTest.endpoints = ReactiveAuthClientTest.cluster.getClientEndpoints();
        ClientBuilder clientBuilder = Client.builder().endpoints(endpoints);
        ReactiveClient client = ReactiveClient.builder(clientBuilder).build();

        authDisabledKVClient = client.getKVClient();
        authDisabledAuthClient = client.getAuthClient();
    }

    @Test
    public void testAuth() throws Exception {
        authDisabledAuthClient.roleAdd(rootRole).block(timeout);
        authDisabledAuthClient.roleAdd(userRole).block(timeout);

        final AuthRoleListResponse response = authDisabledAuthClient.roleList().block(timeout);

        assertThat(response.getRoles()).containsOnly(rootRoleString, userRoleString);

        authDisabledAuthClient
            .roleGrantPermission(rootRole, rootRoleKeyRangeBegin, rootRoleKeyRangeEnd, Permission.Type.READWRITE)
            .block(timeout);
        authDisabledAuthClient
            .roleGrantPermission(userRole, userRoleKeyRangeBegin, userRoleKeyRangeEnd, Permission.Type.READWRITE)
            .block(timeout);

        authDisabledAuthClient.userAdd(root, rootPass).block(timeout);
        authDisabledAuthClient.userAdd(user, userPass).block(timeout);

        authDisabledAuthClient.userChangePassword(user, userNewPass).block(timeout);

        List<String> users = authDisabledAuthClient.userList().block(timeout).getUsers();
        assertThat(users).containsOnly(rootString, userString);

        authDisabledAuthClient.userGrantRole(root, rootRole).block(timeout);
        authDisabledAuthClient.userGrantRole(user, rootRole).block(timeout);
        authDisabledAuthClient.userGrantRole(user, userRole).block(timeout);

        assertThat(authDisabledAuthClient.userGet(root).block(timeout).getRoles()).containsOnly(rootRoleString);
        assertThat(authDisabledAuthClient.userGet(user).block(timeout).getRoles()).containsOnly(rootRoleString, userRoleString);

        authDisabledAuthClient.authEnable().block(timeout);

        final ClientBuilder userClientBuilder = Client.builder().endpoints(endpoints).user(user).password(userNewPass);
        final ClientBuilder rootClientBuilder = Client.builder().endpoints(endpoints).user(root).password(rootPass);

        final ReactiveClient userClient = ReactiveClient.builder(userClientBuilder).build();
        final ReactiveClient rootClient = ReactiveClient.builder(rootClientBuilder).build();

        userClient.getKVClient().put(rootRoleKey, rootRoleValue).block(timeout);
        userClient.getKVClient().put(userRoleKey, userRoleValue).block(timeout);
        userClient.getKVClient().get(rootRoleKey).block(timeout);
        userClient.getKVClient().get(userRoleKey).block(timeout);

        assertThatThrownBy(() -> authDisabledKVClient.put(rootRoleKey, rootRoleValue).block(timeout))
            .hasMessageContaining("etcdserver: user name is empty");
        assertThatThrownBy(() -> authDisabledKVClient.put(userRoleKey, rootRoleValue).block(timeout))
            .hasMessageContaining("etcdserver: user name is empty");
        assertThatThrownBy(() -> authDisabledKVClient.get(rootRoleKey).block(timeout))
            .hasMessageContaining("etcdserver: user name is empty");
        assertThatThrownBy(() -> authDisabledKVClient.get(userRoleKey).block(timeout))
            .hasMessageContaining("etcdserver: user name is empty");

        AuthRoleGetResponse roleGetResponse = userClient.getAuthClient().roleGet(rootRole).block(timeout);
        assertThat(roleGetResponse.getPermissions().size()).isNotEqualTo(0);

        roleGetResponse = userClient.getAuthClient().roleGet(userRole).block(timeout);
        assertThat(roleGetResponse.getPermissions().size()).isNotEqualTo(0);

        rootClient.getAuthClient().userRevokeRole(user, rootRole).block(timeout);

        final ReactiveKV kvClient = userClient.getKVClient();
        // verify the access to root role is revoked for user.
        assertThatThrownBy(() -> kvClient.get(rootRoleKey).block(timeout)).isNotNull();
        // verify userRole is still valid.
        assertThat(kvClient.get(userRoleKey).block(timeout).getCount()).isNotEqualTo(0);

        rootClient.getAuthClient().roleRevokePermission(userRole, userRoleKeyRangeBegin, userRoleKeyRangeEnd).block(timeout);

        // verify the access to foo is revoked for user.
        assertThatThrownBy(() -> userClient.getKVClient().get(userRoleKey).block(timeout)).isNotNull();

        rootClient.getAuthClient().authDisable().block(timeout);

        authDisabledAuthClient.userDelete(root).block(timeout);
        authDisabledAuthClient.userDelete(user).block(timeout);

        authDisabledAuthClient.roleDelete(rootRole).block(timeout);
        authDisabledAuthClient.roleDelete(userRole).block(timeout);
    }
}
