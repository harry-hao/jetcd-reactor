package io.github.harry_hao.etcd.jetcd.reactive;

import java.util.concurrent.TimeUnit;

import io.etcd.jetcd.Lease;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.etcd.jetcd.lease.LeaseRevokeResponse;
import io.etcd.jetcd.lease.LeaseTimeToLiveResponse;
import io.etcd.jetcd.options.LeaseOption;
import io.grpc.stub.StreamObserver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class ReactiveLeaseImpl implements ReactiveLease {

    private Lease lease;

    private Scheduler scheduler;

    ReactiveLeaseImpl(Lease lease, Scheduler scheduler) {
        this.lease = lease;
        this.scheduler = scheduler;
    }

    @Override
    public Mono<LeaseGrantResponse> grant(long ttl) {
        return Mono.fromFuture(this.lease.grant(ttl)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<LeaseGrantResponse> grant(long ttl, long timeout, TimeUnit unit) {
        return Mono.fromFuture(this.lease.grant(ttl, timeout, unit)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<LeaseRevokeResponse> revoke(long leaseId) {
        return Mono.fromFuture(this.lease.revoke(leaseId)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<LeaseKeepAliveResponse> keepAliveOnce(long leaseId) {
        return Mono.fromFuture(this.lease.keepAliveOnce(leaseId)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<LeaseTimeToLiveResponse> timeToLive(long leaseId, LeaseOption leaseOption) {
        return Mono.fromFuture(this.lease.timeToLive(leaseId, leaseOption)).subscribeOn(this.scheduler);
    }

    @Override
    public Flux<LeaseKeepAliveResponse> keepAlive(long leaseId) {
        return Flux.create(sink -> this.lease.keepAlive(leaseId, new StreamObserver<LeaseKeepAliveResponse>() {
            @Override
            public void onNext(LeaseKeepAliveResponse value) {
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
    public void close() {
        this.lease.close();
    }
}
