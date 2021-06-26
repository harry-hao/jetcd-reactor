package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Lock;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.lock.UnlockResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class ReactiveLockImpl implements ReactiveLock {

    private Lock lock;

    private Scheduler scheduler;

    ReactiveLockImpl(Lock lock, Scheduler scheduler) {
        this.lock = lock;
        this.scheduler = scheduler;
    }

    @Override
    public Mono<LockResponse> lock(ByteSequence name, long leaseId) {
        return Mono.fromFuture(this.lock.lock(name, leaseId)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<UnlockResponse> unlock(ByteSequence lockKey) {
        return Mono.fromFuture(this.lock.unlock(lockKey)).subscribeOn(this.scheduler);
    }

    @Override
    public void close() {
        this.lock.close();
    }
}
