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
import reactor.core.scheduler.Scheduler;

public class ReactiveKVImpl implements ReactiveKV {

    private KV kv;

    private Scheduler scheduler;

    ReactiveKVImpl(KV kv, Scheduler scheduler) {
        this.kv = kv;
        this.scheduler = scheduler;
    }

    @Override
    public Mono<PutResponse> put(ByteSequence key, ByteSequence value) {
        return Mono.fromFuture(this.kv.put(key, value)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<PutResponse> put(ByteSequence key, ByteSequence value, PutOption option) {
        return Mono.fromFuture(this.kv.put(key, value, option)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<GetResponse> get(ByteSequence key) {
        return Mono.fromFuture(this.kv.get(key)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<GetResponse> get(ByteSequence key, GetOption option) {
        return Mono.fromFuture(this.kv.get(key, option)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<DeleteResponse> delete(ByteSequence key) {
        return Mono.fromFuture(this.kv.delete(key)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<DeleteResponse> delete(ByteSequence key, DeleteOption option) {
        return Mono.fromFuture(this.kv.delete(key, option)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<CompactResponse> compact(long rev) {
        return Mono.fromFuture(this.kv.compact(rev)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<CompactResponse> compact(long rev, CompactOption option) {
        return Mono.fromFuture(this.kv.compact(rev, option)).subscribeOn(this.scheduler);
    }

    @Override
    public ReactiveTxn txn() {
        return new ReactiveTxnImpl(this.kv.txn(), this.scheduler);
    }

    @Override
    public void close() {
        this.kv.close();
    }
}
