package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import io.etcd.jetcd.Cluster;
import io.etcd.jetcd.cluster.Member;
import io.etcd.jetcd.cluster.MemberAddResponse;
import io.etcd.jetcd.cluster.MemberListResponse;
import io.etcd.jetcd.test.EtcdClusterExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveClusterClientTest {

    @RegisterExtension
    public static EtcdClusterExtension cluster = new EtcdClusterExtension("cluster-client", 3, false);

    private static Duration timeout = Duration.ofSeconds(1);
    private static List<URI> endpoints;
    private static List<URI> peerUrls;


    @BeforeAll
    public static void setUp() throws InterruptedException {
        endpoints = cluster.getClientEndpoints();
        peerUrls = cluster.getPeerEndpoints();
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testListCluster() throws ExecutionException, InterruptedException {
        final ClientBuilder clientBuilder = Client.builder().endpoints(endpoints);
        final ReactiveClient client = ReactiveClient.builder(clientBuilder).build();
        final ReactiveCluster clusterClient = client.getClusterClient();
        MemberListResponse response = clusterClient.listMember().block();
        assertThat(response.getMembers()).hasSize(3);
    }

    @Test
    public void testMemberManagement() throws ExecutionException, InterruptedException, TimeoutException {
        final ClientBuilder clientBuilder = Client.builder().endpoints(endpoints.subList(0, 2));
        final ReactiveClient client = ReactiveClient.builder(clientBuilder).build();
        final ReactiveCluster clusterClient = client.getClusterClient();

        MemberListResponse response = clusterClient.listMember().block(timeout);
        assertThat(response.getMembers()).hasSize(3);

        final MemberAddResponse addResponse = clusterClient.addMember(peerUrls.subList(2, 3)).block(Duration.ofSeconds(5));
        assertThat(addResponse.getMember()).isNotNull();
        assertThat(clusterClient.listMember().block(timeout).getMembers()).hasSize(4);

        // Test update peer url for member
        response = clusterClient.listMember().block(timeout);

        List<URI> newPeerUrls = peerUrls.subList(0, 1);
        clusterClient.updateMember(response.getMembers().get(0).getId(), newPeerUrls).block(timeout);

        response = clusterClient.listMember().block(timeout);
        assertThat(response.getMembers().get(0).getPeerURIs()).containsOnly(newPeerUrls.toArray(new URI[0]));

        // Test remove member from cluster, the member is added by testAddMember
        clusterClient.removeMember(addResponse.getMember().getId()).block(timeout);
        assertThat(clusterClient.listMember().block(timeout).getMembers()).hasSize(3);
    }

}