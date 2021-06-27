package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Client;
import io.etcd.jetcd.Txn;
import io.etcd.jetcd.api.TxnRequest;
import io.etcd.jetcd.kv.TxnResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReactiveTxnTest {

    @Test
    void testTxnCommit() {
        Txn txn = mock(Txn.class);
        ReactiveTxn reactiveTxn = new ReactiveTxnImpl(txn);

        TxnResponse txnResponse = mock(TxnResponse.class);
        CompletableFuture<TxnResponse> future = new CompletableFuture<>();
        when(txn.commit()).thenReturn(future);

        reactiveTxn.commit().as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(txnResponse))
                .expectNext(txnResponse)
                .verifyComplete();
    }
}
