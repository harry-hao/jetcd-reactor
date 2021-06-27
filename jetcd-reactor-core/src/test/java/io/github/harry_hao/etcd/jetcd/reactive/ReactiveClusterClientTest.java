package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Cluster;
import io.etcd.jetcd.cluster.MemberAddResponse;
import io.etcd.jetcd.cluster.MemberListResponse;
import io.etcd.jetcd.cluster.MemberRemoveResponse;
import io.etcd.jetcd.cluster.MemberUpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReactiveClusterClientTest {

    private Cluster cluster;
    
    private ReactiveCluster reactiveCluster;


    @BeforeEach
    void setUp() throws InterruptedException {
        this.cluster = mock(Cluster.class);
        this.reactiveCluster = new ReactiveClusterImpl(this.cluster);
    }

    @Test
    void testClusterListMember() throws ExecutionException, InterruptedException {
        MemberListResponse response = mock(MemberListResponse.class);
        CompletableFuture<MemberListResponse> future = new CompletableFuture<>();
        when(this.cluster.listMember()).thenReturn(future);

        this.reactiveCluster.listMember()
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testClusterAddMember() {
        MemberAddResponse response = mock(MemberAddResponse.class);
        CompletableFuture<MemberAddResponse> future = new CompletableFuture<>();
        List<URI> endpoints = mock(List.class);
        when(this.cluster.addMember(endpoints)).thenReturn(future);

        this.reactiveCluster.addMember(endpoints)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testClusterRemoveMember() {
        MemberRemoveResponse response = mock(MemberRemoveResponse.class);
        CompletableFuture<MemberRemoveResponse> future = new CompletableFuture<>();
        when(this.cluster.removeMember(1L)).thenReturn(future);

        this.reactiveCluster.removeMember(1L)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void testClusterUpdateMember() {
        MemberUpdateResponse response = mock(MemberUpdateResponse.class);
        CompletableFuture<MemberUpdateResponse> future = new CompletableFuture<>();
        List<URI> endpoints = mock(List.class);
        when(this.cluster.updateMember(1L, endpoints)).thenReturn(future);

        this.reactiveCluster.updateMember(1L, endpoints)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }
}