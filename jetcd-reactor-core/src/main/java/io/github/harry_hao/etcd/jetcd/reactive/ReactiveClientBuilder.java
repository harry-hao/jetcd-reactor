package io.github.harry_hao.etcd.jetcd.reactive;

import com.google.common.base.Preconditions;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import reactor.core.scheduler.Scheduler;

public class ReactiveClientBuilder {
    private ClientBuilder clientBuilder;

    private Scheduler scheduler;

    public ReactiveClientBuilder(ClientBuilder clientBuilder) {
        Preconditions.checkNotNull(clientBuilder, "clientBuilder must not be null");
        this.clientBuilder = clientBuilder;
    }

    public ClientBuilder clientBuilder() {
        return this.clientBuilder;
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
        Client client = this.clientBuilder.build();
        return new ReactiveClientImpl(this);
    }

}
