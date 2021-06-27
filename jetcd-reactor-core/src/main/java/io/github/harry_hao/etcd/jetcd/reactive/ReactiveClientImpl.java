package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Client;
import io.etcd.jetcd.support.MemoizingClientSupplier;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ReactiveClientImpl implements ReactiveClient {

    private final Scheduler scheduler;
    private final MemoizingClientSupplier<ReactiveKV> kvClient;
    private final MemoizingClientSupplier<ReactiveAuth> authClient;
    private final MemoizingClientSupplier<ReactiveMaintenance> maintenanceClient;
    private final MemoizingClientSupplier<ReactiveCluster> clusterClient;
    private final MemoizingClientSupplier<ReactiveLease> leaseClient;
    private final MemoizingClientSupplier<ReactiveWatch> watchClient;
    private final MemoizingClientSupplier<ReactiveLock> lockClient;
    private final MemoizingClientSupplier<ReactiveElection> electionClient;

    public ReactiveClientImpl(ReactiveClientBuilder builder) {

        this.scheduler = builder.scheduler() == null ? Schedulers.newSingle("jetcd-reactor") : builder.scheduler();

        Client client = builder.client();

        this.kvClient = new MemoizingClientSupplier<>(() -> new ReactiveKVImpl(client.getKVClient()));
        this.authClient = new MemoizingClientSupplier<>(() -> new ReactiveAuthImpl(client.getAuthClient()));
        this.maintenanceClient = new MemoizingClientSupplier<>(() -> new ReactiveMaintenanceImpl(client.getMaintenanceClient()));
        this.clusterClient = new MemoizingClientSupplier<>(() -> new ReactiveClusterImpl(client.getClusterClient()));
        this.leaseClient = new MemoizingClientSupplier<>(() -> new ReactiveLeaseImpl(client.getLeaseClient()));
        this.watchClient = new MemoizingClientSupplier<>(() -> new ReactiveWatchImpl(client.getWatchClient()));
        this.lockClient = new MemoizingClientSupplier<>(() -> new ReactiveLockImpl(client.getLockClient()));
        this.electionClient = new MemoizingClientSupplier<>(() -> new ReactiveElectionImpl(client.getElectionClient()));
    }

    @Override
    public ReactiveAuth getAuthClient() {
        return this.authClient.get();
    }

    @Override
    public ReactiveKV getKVClient() {
        return this.kvClient.get();
    }

    @Override
    public ReactiveCluster getClusterClient() {
        return this.clusterClient.get();
    }

    @Override
    public ReactiveMaintenance getMaintenanceClient() {
        return this.maintenanceClient.get();
    }

    @Override
    public ReactiveLease getLeaseClient() {
        return this.leaseClient.get();
    }

    @Override
    public ReactiveWatch getWatchClient() {
        return this.watchClient.get();
    }

    @Override
    public ReactiveLock getLockClient() {
        return this.lockClient.get();
    }

    @Override
    public ReactiveElection getElectionClient() {
        return this.electionClient.get();
    }

    @Override
    public void close() {
        this.kvClient.close();
        this.authClient.close();
        this.maintenanceClient.close();
        this.clusterClient.close();
        this.leaseClient.close();
        this.watchClient.close();
        this.lockClient.close();
        this.electionClient.close();

        this.scheduler.dispose();
    }
}
