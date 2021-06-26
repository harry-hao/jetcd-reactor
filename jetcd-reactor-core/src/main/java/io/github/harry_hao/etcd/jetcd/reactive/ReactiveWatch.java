package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.common.exception.ClosedClientException;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.support.CloseableClient;
import io.etcd.jetcd.watch.WatchResponse;
import reactor.core.publisher.Flux;

/**
 * Interface of the watch client.
 */
public interface ReactiveWatch extends CloseableClient {

    /**
     * watch on a key with option.
     *
     * @param  key                   key to be watched on.
     * @param  option                see {@link io.etcd.jetcd.options.WatchOption}.
     * @return                       this watcher
     * @throws ClosedClientException if watch client has been closed.
     */
    Flux<WatchResponse> watch(ByteSequence key, WatchOption option);

    /**
     * watch on a key.
     *
     * @param  key                   key to be watched on.
     * @param  listener              the event consumer
     * @return                       this watcher
     * @throws ClosedClientException if watch client has been closed.
     **/
    default Flux<WatchResponse> watch(ByteSequence key, Watch.Listener listener) {
        return watch(key, WatchOption.DEFAULT);
    }

}
