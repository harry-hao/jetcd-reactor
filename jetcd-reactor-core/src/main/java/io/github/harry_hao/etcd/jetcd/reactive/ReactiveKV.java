package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.kv.CompactResponse;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.CompactOption;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.support.CloseableClient;
import reactor.core.publisher.Mono;

/**
 * Interface of kv client talking to etcd.
 */
public interface ReactiveKV extends CloseableClient {

    /**
     * put a key-value pair into etcd.
     *
     * @param  key   key in ByteSequence
     * @param  value value in ByteSequence
     * @return       PutResponse
     */
    Mono<PutResponse> put(ByteSequence key, ByteSequence value);

    /**
     * put a key-value pair into etcd with option.
     *
     * @param  key    key in ByteSequence
     * @param  value  value in ByteSequence
     * @param  option PutOption
     * @return        PutResponse
     */
    Mono<PutResponse> put(ByteSequence key, ByteSequence value, PutOption option);

    /**
     * retrieve value for the given key.
     *
     * @param  key key in ByteSequence
     * @return     GetResponse
     */
    Mono<GetResponse> get(ByteSequence key);

    /**
     * retrieve keys with GetOption.
     *
     * @param  key    key in ByteSequence
     * @param  option GetOption
     * @return        GetResponse
     */
    Mono<GetResponse> get(ByteSequence key, GetOption option);

    /**
     * delete a key.
     *
     * @param  key key in ByteSequence
     * @return     DeleteResponse
     */
    Mono<DeleteResponse> delete(ByteSequence key);

    /**
     * delete a key or range with option.
     *
     * @param  key    key in ByteSequence
     * @param  option DeleteOption
     * @return        DeleteResponse
     */
    Mono<DeleteResponse> delete(ByteSequence key, DeleteOption option);

    /**
     * compact etcd KV history before the given rev.
     *
     * <p>
     * All superseded keys with a revision less than the compaction revision will be removed.
     *
     * @param  rev the revision to compact.
     * @return     CompactResponse
     */
    Mono<CompactResponse> compact(long rev);

    /**
     * compact etcd KV history before the given rev with option.
     *
     * <p>
     * All superseded keys with a revision less than the compaction revision will be removed.
     *
     * @param  rev    etcd revision
     * @param  option CompactOption
     * @return        CompactResponse
     */
    Mono<CompactResponse> compact(long rev, CompactOption option);

    /**
     * creates a transaction.
     *
     * @return a Txn
     */
    ReactiveTxn txn();
}
