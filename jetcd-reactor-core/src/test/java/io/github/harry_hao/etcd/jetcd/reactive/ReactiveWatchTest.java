package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReactiveWatchTest {

    private Watch watch;

    private ReactiveWatch reactiveWatch;

    @BeforeEach
    void setup() {
        this.watch = mock(Watch.class);
        this.reactiveWatch = new ReactiveWatchImpl(this.watch);
    }

    @Test
    void testWatchOption() {
        WatchResponse response1 = mock(WatchResponse.class);
        WatchResponse response2 = mock(WatchResponse.class);
        ByteSequence key = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        this.reactiveWatch.watch(key, WatchOption.DEFAULT)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> {
                    ArgumentCaptor<Watch.Listener> captor = ArgumentCaptor.forClass(Watch.Listener.class);
                    verify(this.watch).watch(same(key), same(WatchOption.DEFAULT), captor.capture());
                    Watch.Listener listener = captor.getValue();
                    listener.onNext(response1);
                    listener.onNext(response2);
                    listener.onCompleted();
                }).expectNext(response1, response2)
                .verifyComplete();
    }

    @Test
    void testWatch() {
        WatchResponse response1 = mock(WatchResponse.class);
        WatchResponse response2 = mock(WatchResponse.class);
        ByteSequence key = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        this.reactiveWatch.watch(key)
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> {
                    ArgumentCaptor<Watch.Listener> captor = ArgumentCaptor.forClass(Watch.Listener.class);
                    verify(this.watch).watch(same(key), same(WatchOption.DEFAULT), captor.capture());
                    Watch.Listener listener = captor.getValue();
                    listener.onNext(response1);
                    listener.onNext(response2);
                    listener.onCompleted();
                }).expectNext(response1, response2)
                .verifyComplete();
    }
}
