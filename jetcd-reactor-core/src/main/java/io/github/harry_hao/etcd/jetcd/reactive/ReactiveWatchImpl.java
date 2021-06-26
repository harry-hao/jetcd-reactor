package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchResponse;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class ReactiveWatchImpl implements ReactiveWatch {

    private Watch watch;

    private Scheduler scheduler;

    ReactiveWatchImpl(Watch watch, Scheduler scheduler) {
        this.watch = watch;
        this.scheduler = scheduler;
    }

    @Override
    public Flux<WatchResponse> watch(ByteSequence key, WatchOption option) {
        return Flux.<WatchResponse> create(sink -> {
            Watch.Watcher watcher = this.watch.watch(key, option, new Watch.Listener() {
                @Override
                public void onNext(WatchResponse watchResponse) {
                    sink.next(watchResponse);
                }

                @Override
                public void onError(Throwable throwable) {
                    sink.error(throwable);
                }

                @Override
                public void onCompleted() {
                    sink.complete();
                }
            });
            sink.onCancel(() -> watcher.close());
        }).subscribeOn(this.scheduler);
    }

    @Override
    public void close() {
        ReactiveWatch.super.close();
    }
}
