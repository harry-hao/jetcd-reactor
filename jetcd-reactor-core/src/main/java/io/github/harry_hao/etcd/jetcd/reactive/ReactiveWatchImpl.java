package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchResponse;
import reactor.core.publisher.Flux;

public class ReactiveWatchImpl implements ReactiveWatch {

    private Watch watch;

    ReactiveWatchImpl(Watch watch) {
        this.watch = watch;
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
        });
    }

    @Override
    public void close() {
        ReactiveWatch.super.close();
    }
}
