package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.CompactResponse;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.CompactOption;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import reactor.core.publisher.Mono;

public class ReactiveKVImpl implements ReactiveKV {

    private KV kv;

    ReactiveKVImpl(KV kv) {
        this.kv = kv;
    }

    @Override
    public Mono<PutResponse> put(ByteSequence key, ByteSequence value) {
        return Mono.fromFuture(this.kv.put(key, value));
    }

    @Override
    public Mono<PutResponse> put(ByteSequence key, ByteSequence value, PutOption option) {
        return Mono.fromFuture(this.kv.put(key, value, option));
    }

    @Override
    public Mono<GetResponse> get(ByteSequence key) {
        return Mono.fromFuture(this.kv.get(key));
    }

    @Override
    public Mono<GetResponse> get(ByteSequence key, GetOption option) {
        return Mono.fromFuture(this.kv.get(key, option));
    }

    @Override
    public Mono<DeleteResponse> delete(ByteSequence key) {
        return Mono.fromFuture(this.kv.delete(key));
    }

    @Override
    public Mono<DeleteResponse> delete(ByteSequence key, DeleteOption option) {
        return Mono.fromFuture(this.kv.delete(key, option));
    }

    @Override
    public Mono<CompactResponse> compact(long rev) {
        return Mono.fromFuture(this.kv.compact(rev));
    }

    @Override
    public Mono<CompactResponse> compact(long rev, CompactOption option) {
        return Mono.fromFuture(this.kv.compact(rev, option));
    }

    @Override
    public ReactiveTxn txn() {
        return new ReactiveTxnImpl(this.kv.txn());
    }

    @Override
    public void close() {
        this.kv.close();
    }
}
