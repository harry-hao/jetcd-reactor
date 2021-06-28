package io.github.harry_hao.etcd.jetcd.reactive;

import java.io.OutputStream;
import java.net.URI;

import io.etcd.jetcd.Maintenance;
import io.etcd.jetcd.maintenance.AlarmMember;
import io.etcd.jetcd.maintenance.AlarmResponse;
import io.etcd.jetcd.maintenance.DefragmentResponse;
import io.etcd.jetcd.maintenance.HashKVResponse;
import io.etcd.jetcd.maintenance.MoveLeaderResponse;
import io.etcd.jetcd.maintenance.SnapshotResponse;
import io.etcd.jetcd.maintenance.StatusResponse;
import io.grpc.stub.StreamObserver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveMaintenanceImpl implements ReactiveMaintenance {

    private Maintenance maintenance;

    public ReactiveMaintenanceImpl(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    @Override
    public Mono<AlarmResponse> listAlarms() {
        return Mono.fromFuture(this.maintenance.listAlarms());
    }

    @Override
    public Mono<AlarmResponse> alarmDisarm(AlarmMember member) {
        return Mono.fromFuture(this.maintenance.alarmDisarm(member));
    }

    @Override
    public Mono<DefragmentResponse> defragmentMember(URI endpoint) {
        return Mono.fromFuture(this.maintenance.defragmentMember(endpoint));
    }

    @Override
    public Mono<StatusResponse> statusMember(URI endpoint) {
        return Mono.fromFuture(this.maintenance.statusMember(endpoint));
    }

    @Override
    public Mono<HashKVResponse> hashKV(URI endpoint, long rev) {
        return Mono.fromFuture(this.maintenance.hashKV(endpoint, rev));
    }

    @Override
    public Mono<Long> snapshot(OutputStream output) {
        return Mono.fromFuture(this.maintenance.snapshot(output));
    }

    @Override
    public Flux<SnapshotResponse> snapshot() {
        return Flux.<SnapshotResponse> create(sink -> this.maintenance.snapshot(new StreamObserver<SnapshotResponse>() {
            @Override
            public void onNext(SnapshotResponse value) {
                sink.next(value);
            }

            @Override
            public void onError(Throwable t) {
                sink.error(t);
            }

            @Override
            public void onCompleted() {
                sink.complete();
            }
        }));
    }

    @Override
    public Mono<MoveLeaderResponse> moveLeader(long transfereeID) {
        return Mono.fromFuture(this.maintenance.moveLeader(transfereeID));
    }

    @Override
    public void close() {
        this.maintenance.close();
    }
}
