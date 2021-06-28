package io.github.harry_hao.etcd.jetcd.reactive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.etcd.jetcd.Lease;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.etcd.jetcd.lease.LeaseRevokeResponse;
import io.etcd.jetcd.lease.LeaseTimeToLiveResponse;
import io.etcd.jetcd.options.LeaseOption;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReactiveLeaseTest {
    private Lease lease;

    private ReactiveLease reactiveLease;

    @BeforeEach
    void setup() {
        this.lease = mock(Lease.class);
        this.reactiveLease = new ReactiveLeaseImpl(this.lease);
    }

    @Test
    void testLeaseGrant() {
        LeaseGrantResponse response = mock(LeaseGrantResponse.class);
        long ttl = 1L;
        CompletableFuture<LeaseGrantResponse> future = new CompletableFuture<>();
        when(this.lease.grant(ttl)).thenReturn(future);

        this.reactiveLease.grant(ttl)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testLeaseGrantTimeout() {
        LeaseGrantResponse response = mock(LeaseGrantResponse.class);
        long ttl = 1L;
        long timeout = 1L;
        CompletableFuture<LeaseGrantResponse> future = new CompletableFuture<>();
        when(this.lease.grant(ttl, timeout, TimeUnit.SECONDS)).thenReturn(future);

        this.reactiveLease.grant(ttl, timeout, TimeUnit.SECONDS)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testLeaseRevoke() {
        LeaseRevokeResponse response = mock(LeaseRevokeResponse.class);
        CompletableFuture<LeaseRevokeResponse> future = new CompletableFuture<>();
        when(this.lease.revoke(1L)).thenReturn(future);

        this.reactiveLease.revoke(1L)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testLeaseKeepAliveOnce() {
        LeaseKeepAliveResponse response = mock(LeaseKeepAliveResponse.class);
        CompletableFuture<LeaseKeepAliveResponse> future = new CompletableFuture<>();
        when(this.lease.keepAliveOnce(1L)).thenReturn(future);

        this.reactiveLease.keepAliveOnce(1L)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testLeaseTimeToLive() {
        LeaseTimeToLiveResponse response = mock(LeaseTimeToLiveResponse.class);
        CompletableFuture<LeaseTimeToLiveResponse> future = new CompletableFuture<>();
        when(this.lease.timeToLive(1L, LeaseOption.DEFAULT)).thenReturn(future);

        this.reactiveLease.timeToLive(1L, LeaseOption.DEFAULT)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testLeaseKeepAlive() {
        LeaseKeepAliveResponse response1 = mock(LeaseKeepAliveResponse.class);
        LeaseKeepAliveResponse response2 = mock(LeaseKeepAliveResponse.class);

        this.reactiveLease.keepAlive(1L)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> {
                ArgumentCaptor<StreamObserver> captor = ArgumentCaptor.forClass(StreamObserver.class);
                verify(this.lease).keepAlive(Mockito.same(1L), captor.capture());
                StreamObserver<LeaseKeepAliveResponse> streamObserver = captor.getValue();
                streamObserver.onNext(response1);
                streamObserver.onNext(response2);
                streamObserver.onCompleted();
            })
            .expectNext(response1)
            .expectNext(response2)
            .verifyComplete();
    }
}
