package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Txn;
import io.etcd.jetcd.kv.TxnResponse;
import io.etcd.jetcd.op.Cmp;
import io.etcd.jetcd.op.Op;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class ReactiveTxnImpl implements ReactiveTxn {

    private Txn txn;

    private Scheduler scheduler;

    ReactiveTxnImpl(Txn txn, Scheduler scheduler) {
        this.txn = txn;
        this.scheduler = scheduler;
    }

    @Override
    public Txn If(Cmp... cmps) {
        return this.txn.If(cmps);
    }

    @Override
    public Txn Then(Op... ops) {
        return this.txn.Then(ops);
    }

    @Override
    public Txn Else(Op... ops) {
        return this.txn.Else(ops);
    }

    @Override
    public Mono<TxnResponse> commit() {
        return Mono.fromFuture(this.txn.commit()).subscribeOn(this.scheduler);
    }
}
