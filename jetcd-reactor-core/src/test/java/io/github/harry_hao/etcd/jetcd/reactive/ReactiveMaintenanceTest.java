package io.github.harry_hao.etcd.jetcd.reactive;

import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import io.etcd.jetcd.Maintenance;
import io.etcd.jetcd.maintenance.AlarmMember;
import io.etcd.jetcd.maintenance.AlarmResponse;
import io.etcd.jetcd.maintenance.DefragmentResponse;
import io.etcd.jetcd.maintenance.HashKVResponse;
import io.etcd.jetcd.maintenance.MoveLeaderResponse;
import io.etcd.jetcd.maintenance.SnapshotResponse;
import io.etcd.jetcd.maintenance.StatusResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReactiveMaintenanceTest {

    private Maintenance maintenance;

    private ReactiveMaintenance reactiveMaintenance;

    @BeforeEach
    void setup() {
        this.maintenance = mock(Maintenance.class);
        this.reactiveMaintenance = new ReactiveMaintenanceImpl(this.maintenance);
    }

    @Test
    void testListAlarms() {
        AlarmResponse response = mock(AlarmResponse.class);
        CompletableFuture<AlarmResponse> future = new CompletableFuture<>();
        when(this.maintenance.listAlarms()).thenReturn(future);

        this.reactiveMaintenance.listAlarms()
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testAlarmDisarm() {
        AlarmResponse response = mock(AlarmResponse.class);
        AlarmMember alarmMember = mock(AlarmMember.class);
        CompletableFuture<AlarmResponse> future = new CompletableFuture<>();
        when(this.maintenance.alarmDisarm(alarmMember)).thenReturn(future);

        this.reactiveMaintenance.alarmDisarm(alarmMember)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testDefragement() {
        DefragmentResponse response = mock(DefragmentResponse.class);
        URI endpoint = URI.create("http://localhost:2379");
        CompletableFuture<DefragmentResponse> future = new CompletableFuture<>();
        when(this.maintenance.defragmentMember(endpoint)).thenReturn(future);

        this.reactiveMaintenance.defragmentMember(endpoint)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testStatusMember() {
        StatusResponse response = mock(StatusResponse.class);
        URI endpoint = URI.create("http://localhost:2379");
        CompletableFuture<StatusResponse> future = new CompletableFuture<>();
        when(this.maintenance.statusMember(endpoint)).thenReturn(future);

        this.reactiveMaintenance.statusMember(endpoint)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testHashKV() {
        HashKVResponse response = mock(HashKVResponse.class);
        URI endpoint = URI.create("http://localhost:2379");
        CompletableFuture<HashKVResponse> future = new CompletableFuture<>();
        when(this.maintenance.hashKV(endpoint, 0L)).thenReturn(future);

        this.reactiveMaintenance.hashKV(endpoint, 0L)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testSnapshotOutputStream() {
        OutputStream out = mock(OutputStream.class);
        CompletableFuture<Long> future = new CompletableFuture<>();
        when(this.maintenance.snapshot(out)).thenReturn(future);

        this.reactiveMaintenance.snapshot(out)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(1L))
            .expectNext(1L)
            .verifyComplete();
    }

    @Test
    void testSnapshot() {
        SnapshotResponse response1 = mock(SnapshotResponse.class);
        SnapshotResponse response2 = mock(SnapshotResponse.class);

        this.reactiveMaintenance.snapshot()
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> {
                ArgumentCaptor<StreamObserver> captor = ArgumentCaptor.forClass(StreamObserver.class);
                verify(this.maintenance).snapshot(captor.capture());
                StreamObserver<SnapshotResponse> observer = captor.getValue();
                observer.onNext(response1);
                observer.onNext(response2);
                observer.onCompleted();
            })
            .expectNext(response1, response2)
            .verifyComplete();
    }

    @Test
    void testMoveLeader() {
        MoveLeaderResponse response = mock(MoveLeaderResponse.class);
        CompletableFuture<MoveLeaderResponse> future = new CompletableFuture<>();
        when(this.maintenance.moveLeader(1L)).thenReturn(future);

        this.reactiveMaintenance.moveLeader(1L)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }
}
