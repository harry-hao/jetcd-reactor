package io.github.harry_hao.etcd.jetcd.reactive;

import com.google.common.base.Preconditions;
import io.etcd.jetcd.Client;
import reactor.core.scheduler.Scheduler;

public class ReactiveClientBuilder {
    private Client client;

    private Scheduler scheduler;

    public ReactiveClientBuilder(Client client) {
        Preconditions.checkNotNull(client, "client must not be null");
        this.client = client;
    }

    public Client client() {
        return this.client;
    }

    public Scheduler scheduler() {
        return this.scheduler;
    }

    public ReactiveClientBuilder scheduler(Scheduler scheduler) {
        Preconditions.checkNotNull(scheduler, "scheduler must not be null");
        this.scheduler = scheduler;
        return this;
    }

    public ReactiveClient build() {
        return new ReactiveClientImpl(this);
    }

}
