package io.github.harry_hao.etcd.jetcd.reactive;

import java.net.URI;
import java.util.List;

import io.etcd.jetcd.Cluster;
import io.etcd.jetcd.cluster.MemberAddResponse;
import io.etcd.jetcd.cluster.MemberListResponse;
import io.etcd.jetcd.cluster.MemberRemoveResponse;
import io.etcd.jetcd.cluster.MemberUpdateResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class ReactiveClusterImpl implements ReactiveCluster {

    private Cluster cluster;

    private Scheduler scheduler;

    ReactiveClusterImpl(Cluster cluster, Scheduler scheduler) {
        this.cluster = cluster;
        this.scheduler = scheduler;
    }

    @Override
    public Mono<MemberListResponse> listMember() {
        return Mono.fromFuture(this.cluster.listMember()).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<MemberAddResponse> addMember(List<URI> peerAddrs) {
        return Mono.fromFuture(this.cluster.addMember(peerAddrs)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<MemberRemoveResponse> removeMember(long memberID) {
        return Mono.fromFuture(this.cluster.removeMember(memberID)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<MemberUpdateResponse> updateMember(long memberID, List<URI> peerAddrs) {
        return Mono.fromFuture(this.cluster.updateMember(memberID, peerAddrs)).subscribeOn(this.scheduler);
    }

    @Override
    public void close() {
        this.cluster.close();
    }
}
