package io.github.harry_hao.etcd.jetcd.reactive;

import java.util.concurrent.CompletableFuture;

import io.etcd.jetcd.Txn;
import io.etcd.jetcd.kv.TxnResponse;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReactiveTxnTest {

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
