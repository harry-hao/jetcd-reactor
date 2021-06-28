package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
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
     * @param  key    key to be watched on.
     * @param  option see {@link io.etcd.jetcd.options.WatchOption}.
     * @return        {@code Flux<WatchResponse>}
     */
    Flux<WatchResponse> watch(ByteSequence key, WatchOption option);

    /**
     * watch on a key.
     *
     * @param  key key to be watched on.
     * @return     {@code Flux<WatchResponse>}
     **/
    default Flux<WatchResponse> watch(ByteSequence key) {
        return watch(key, WatchOption.DEFAULT);
    }

}
