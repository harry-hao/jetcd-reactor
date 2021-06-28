package io.github.harry_hao.etcd.jetcd.reactive;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Lock;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.lock.UnlockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReactiveLockTest {
    private Lock lock;

    private ReactiveLock reactiveLock;

    @BeforeEach
    void setup() {
        this.lock = mock(Lock.class);
        this.reactiveLock = new ReactiveLockImpl(this.lock);
    }

    @Test
    void testLockLock() {
        LockResponse response = mock(LockResponse.class);
        CompletableFuture<LockResponse> future = new CompletableFuture<>();
        ByteSequence name = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        when(this.lock.lock(name, 1L)).thenReturn(future);

        this.reactiveLock.lock(name, 1L)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testLockUnlock() {
        UnlockResponse response = mock(UnlockResponse.class);
        CompletableFuture<UnlockResponse> future = new CompletableFuture<>();
        ByteSequence lockKey = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        when(this.lock.unlock(lockKey)).thenReturn(future);

        this.reactiveLock.unlock(lockKey)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }
}
