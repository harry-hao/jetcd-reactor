package io.github.harry_hao.etcd.jetcd.reactive;

import java.util.concurrent.CompletableFuture;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.CompactResponse;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.CompactOption;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReactiveKVTest {

    private KV kv;

    private ReactiveKV reactiveKv;

    private ByteSequence key;

    private ByteSequence value;

    @BeforeEach
    void setup() {
        Client client = mock(Client.class);
        this.kv = mock(KV.class);
        when(client.getKVClient()).thenReturn(this.kv);
        this.reactiveKv = ReactiveClient.builder(client).build().getKVClient();
        this.key = ByteSequence.from("key", UTF_8);
        this.value = ByteSequence.from("value", UTF_8);
    }

    @Test
    void testKVPut() {
        PutResponse putResponse = mock(PutResponse.class);
        CompletableFuture<PutResponse> future = new CompletableFuture<>();

        when(this.kv.put(this.key, this.value)).thenReturn(future);

        this.reactiveKv.put(this.key, this.value).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(putResponse))
            .expectNext(putResponse)
            .verifyComplete();
    }

    @Test
    void testKVPutOption() {
        PutResponse putResponse = mock(PutResponse.class);
        CompletableFuture<PutResponse> future = new CompletableFuture<>();

        when(this.kv.put(this.key, this.value, PutOption.DEFAULT)).thenReturn(future);

        this.reactiveKv.put(this.key, this.value, PutOption.DEFAULT).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(putResponse))
            .expectNext(putResponse)
            .verifyComplete();
    }

    @Test
    void testKVGet() {
        GetResponse getResponse = mock(GetResponse.class);
        CompletableFuture<GetResponse> future = new CompletableFuture<>();
        when(kv.get(key)).thenReturn(future);

        reactiveKv.get(this.key).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(getResponse))
            .expectNext(getResponse)
            .expectComplete()
            .verify();
    }

    @Test
    void testKVGetOption() {
        GetResponse getResponse = mock(GetResponse.class);
        CompletableFuture<GetResponse> future = new CompletableFuture<>();

        when(kv.get(this.key, GetOption.DEFAULT)).thenReturn(future);

        this.reactiveKv.get(this.key, GetOption.DEFAULT).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(getResponse))
            .expectNext(getResponse)
            .verifyComplete();
    }

    @Test
    void testKVDelete() {
        DeleteResponse deleteResponse = mock(DeleteResponse.class);
        CompletableFuture<DeleteResponse> future = new CompletableFuture<>();
        when(kv.delete(this.key)).thenReturn(future);

        this.reactiveKv.delete(this.key).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(deleteResponse))
            .expectNext(deleteResponse)
            .verifyComplete();
    }

    void testKVDeleteOption() {
        DeleteResponse deleteResponse = mock(DeleteResponse.class);
        CompletableFuture<DeleteResponse> future = new CompletableFuture<>();
        when(kv.delete(this.key, DeleteOption.DEFAULT)).thenReturn(future);

        this.reactiveKv.delete(this.key, DeleteOption.DEFAULT).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(deleteResponse))
            .expectNext(deleteResponse)
            .verifyComplete();
    }

    @Test
    void testCompact() {
        CompactResponse compactResponse = mock(CompactResponse.class);
        CompletableFuture<CompactResponse> future = new CompletableFuture<>();
        int rev = 1;
        when(kv.compact(rev)).thenReturn(future);

        this.reactiveKv.compact(rev).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(compactResponse))
            .expectNext(compactResponse)
            .verifyComplete();
    }

    @Test
    void testCompactOption() {
        CompactResponse compactResponse = mock(CompactResponse.class);
        CompletableFuture<CompactResponse> future = new CompletableFuture<>();
        int rev = 1;
        when(kv.compact(rev, CompactOption.DEFAULT)).thenReturn(future);

        this.reactiveKv.compact(rev, CompactOption.DEFAULT)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(compactResponse))
            .expectNext(compactResponse)
            .verifyComplete();
    }

}
