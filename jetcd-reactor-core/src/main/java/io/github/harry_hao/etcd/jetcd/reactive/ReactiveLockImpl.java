package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Lock;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.lock.UnlockResponse;
import reactor.core.publisher.Mono;

public class ReactiveLockImpl implements ReactiveLock {

    private Lock lock;

    ReactiveLockImpl(Lock lock) {
        this.lock = lock;
    }

    @Override
    public Mono<LockResponse> lock(ByteSequence name, long leaseId) {
        return Mono.fromFuture(this.lock.lock(name, leaseId));
    }

    @Override
    public Mono<UnlockResponse> unlock(ByteSequence lockKey) {
        return Mono.fromFuture(this.lock.unlock(lockKey));
    }

    @Override
    public void close() {
        this.lock.close();
    }
}
