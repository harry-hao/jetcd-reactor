package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Auth;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.auth.AuthDisableResponse;
import io.etcd.jetcd.auth.AuthEnableResponse;
import io.etcd.jetcd.auth.AuthRoleAddResponse;
import io.etcd.jetcd.auth.AuthRoleDeleteResponse;
import io.etcd.jetcd.auth.AuthRoleGetResponse;
import io.etcd.jetcd.auth.AuthRoleGrantPermissionResponse;
import io.etcd.jetcd.auth.AuthRoleListResponse;
import io.etcd.jetcd.auth.AuthRoleRevokePermissionResponse;
import io.etcd.jetcd.auth.AuthUserAddResponse;
import io.etcd.jetcd.auth.AuthUserChangePasswordResponse;
import io.etcd.jetcd.auth.AuthUserDeleteResponse;
import io.etcd.jetcd.auth.AuthUserGetResponse;
import io.etcd.jetcd.auth.AuthUserGrantRoleResponse;
import io.etcd.jetcd.auth.AuthUserListResponse;
import io.etcd.jetcd.auth.AuthUserRevokeRoleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static io.etcd.jetcd.auth.Permission.Type.READ;
import static io.etcd.jetcd.auth.Permission.Type.READWRITE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReactiveAuthClientTest {
    private Auth auth;

    private ReactiveAuth reactiveAuth;

    @BeforeEach
    void setup() {
        this.auth = mock(Auth.class);
        this.reactiveAuth = new ReactiveAuthImpl(this.auth);
    }

    @Test
    void testAuthEnable() {
        AuthEnableResponse response = mock(AuthEnableResponse.class);
        CompletableFuture<AuthEnableResponse> future = new CompletableFuture<>();
        when(this.auth.authEnable()).thenReturn(future);

        this.reactiveAuth.authEnable()
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testAuthDisable() {
        AuthDisableResponse response = mock(AuthDisableResponse.class);
        CompletableFuture<AuthDisableResponse> future = new CompletableFuture<>();
        when(this.auth.authDisable()).thenReturn(future);

        this.reactiveAuth.authDisable()
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUserAdd() {
        AuthUserAddResponse response = mock(AuthUserAddResponse.class);
        CompletableFuture<AuthUserAddResponse> future = new CompletableFuture<>();
        ByteSequence user = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        ByteSequence password = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        when(this.auth.userAdd(user, password)).thenReturn(future);

        this.reactiveAuth.userAdd(user, password)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUserDelete() {
        AuthUserDeleteResponse response = mock(AuthUserDeleteResponse.class);
        CompletableFuture<AuthUserDeleteResponse> future = new CompletableFuture<>();
        ByteSequence user = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        when(this.auth.userDelete(user)).thenReturn(future);

        this.reactiveAuth.userDelete(user)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUserChangePassword() {
        AuthUserChangePasswordResponse response = mock(AuthUserChangePasswordResponse.class);
        CompletableFuture<AuthUserChangePasswordResponse> future = new CompletableFuture<>();
        ByteSequence user = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        ByteSequence password = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        when(this.auth.userChangePassword(user, password)).thenReturn(future);

        this.reactiveAuth.userChangePassword(user, password)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUserGet() {
        AuthUserGetResponse response = mock(AuthUserGetResponse.class);
        CompletableFuture<AuthUserGetResponse> future = new CompletableFuture<>();
        ByteSequence user = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        when(this.auth.userGet(user)).thenReturn(future);

        this.reactiveAuth.userGet(user)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUserList() {
        AuthUserListResponse response = mock(AuthUserListResponse.class);
        CompletableFuture<AuthUserListResponse> future = new CompletableFuture<>();
        when(this.auth.userList()).thenReturn(future);

        this.reactiveAuth.userList()
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUserGrantRole() {
        AuthUserGrantRoleResponse response = mock(AuthUserGrantRoleResponse.class);
        CompletableFuture<AuthUserGrantRoleResponse> future = new CompletableFuture<>();
        ByteSequence user = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        ByteSequence role = ByteSequence.from("root", UTF_8);
        when(this.auth.userGrantRole(user, role)).thenReturn(future);

        this.reactiveAuth.userGrantRole(user, role)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testUserRevokeRole() {
        AuthUserRevokeRoleResponse response = mock(AuthUserRevokeRoleResponse.class);
        CompletableFuture<AuthUserRevokeRoleResponse> future = new CompletableFuture<>();
        ByteSequence user = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        ByteSequence role = ByteSequence.from("root", UTF_8);
        when(this.auth.userRevokeRole(user, role)).thenReturn(future);

        this.reactiveAuth.userRevokeRole(user, role)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testRoleAdd() {
        AuthRoleAddResponse response = mock(AuthRoleAddResponse.class);
        CompletableFuture<AuthRoleAddResponse> future = new CompletableFuture<>();
        ByteSequence role = ByteSequence.from("root", UTF_8);
        when(this.auth.roleAdd(role)).thenReturn(future);

        this.reactiveAuth.roleAdd(role)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testRoleGrantPermission() {
        AuthRoleGrantPermissionResponse response = mock(AuthRoleGrantPermissionResponse.class);
        CompletableFuture<AuthRoleGrantPermissionResponse> future = new CompletableFuture<>();
        ByteSequence role = ByteSequence.from("root", UTF_8);
        ByteSequence key = ByteSequence.from("key1", UTF_8);
        ByteSequence rangeEnd = ByteSequence.from("key2", UTF_8);
        when(this.auth.roleGrantPermission(role, key, rangeEnd, READ)).thenReturn(future);

        this.reactiveAuth.roleGrantPermission(role, key, rangeEnd, READ)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testRoleGet() {
        AuthRoleGetResponse response = mock(AuthRoleGetResponse.class);
        CompletableFuture<AuthRoleGetResponse> future = new CompletableFuture<>();
        ByteSequence role = ByteSequence.from("root", UTF_8);
        when(this.auth.roleGet(role)).thenReturn(future);

        this.reactiveAuth.roleGet(role)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testRoleList() {
        AuthRoleListResponse response = mock(AuthRoleListResponse.class);
        CompletableFuture<AuthRoleListResponse> future = new CompletableFuture<>();
        when(this.auth.roleList()).thenReturn(future);

        this.reactiveAuth.roleList()
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testRoleRevokePermission() {
        AuthRoleRevokePermissionResponse response = mock(AuthRoleRevokePermissionResponse.class);
        CompletableFuture<AuthRoleRevokePermissionResponse> future = new CompletableFuture<>();
        ByteSequence role = ByteSequence.from("root", UTF_8);
        ByteSequence key = ByteSequence.from("key1", UTF_8);
        ByteSequence rangeEnd = ByteSequence.from("key2", UTF_8);
        when(this.auth.roleRevokePermission(role, key, rangeEnd)).thenReturn(future);

        this.reactiveAuth.roleRevokePermission(role, key, rangeEnd)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testRoleDelete() {
        AuthRoleDeleteResponse response = mock(AuthRoleDeleteResponse.class);
        CompletableFuture<AuthRoleDeleteResponse> future = new CompletableFuture<>();
        ByteSequence role = ByteSequence.from("root", UTF_8);
        when(this.auth.roleDelete(role)).thenReturn(future);

        this.reactiveAuth.roleDelete(role)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }
}

