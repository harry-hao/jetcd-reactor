package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.lock.UnlockResponse;
import io.etcd.jetcd.support.CloseableClient;
import reactor.core.publisher.Mono;

/**
 * Interface of Lock talking to etcd.
 */
public interface ReactiveLock extends CloseableClient {

    /**
     * Acquire a lock with the given name.
     *
     * @param  name
     *                 the identifier for the distributed shared lock to be acquired.
     * @param  leaseId
     *                 the ID of the lease that will be attached to ownership of the
     *                 lock. If the lease expires or is revoked and currently holds the
     *                 lock, the lock is automatically released. Calls to Lock with the
     *                 same lease will be treated as a single acquistion; locking twice
     *                 with the same lease is a no-op.
     * @return         the lock response
     */
    Mono<LockResponse> lock(ByteSequence name, long leaseId);

    /**
     * Release the lock identified by the given key.
     *
     * @param  lockKey
     *                 key is the lock ownership key granted by Lock.
     * @return         the unlock response
     */
    Mono<UnlockResponse> unlock(ByteSequence lockKey);

}
