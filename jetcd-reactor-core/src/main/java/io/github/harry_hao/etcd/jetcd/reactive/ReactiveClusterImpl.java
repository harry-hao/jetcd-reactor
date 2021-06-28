package io.github.harry_hao.etcd.jetcd.reactive;

import java.net.URI;
import java.util.List;

import io.etcd.jetcd.Cluster;
import io.etcd.jetcd.cluster.MemberAddResponse;
import io.etcd.jetcd.cluster.MemberListResponse;
import io.etcd.jetcd.cluster.MemberRemoveResponse;
import io.etcd.jetcd.cluster.MemberUpdateResponse;
import reactor.core.publisher.Mono;

public class ReactiveClusterImpl implements ReactiveCluster {

    private Cluster cluster;

    ReactiveClusterImpl(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public Mono<MemberListResponse> listMember() {
        return Mono.fromFuture(this.cluster.listMember());
    }

    @Override
    public Mono<MemberAddResponse> addMember(List<URI> peerAddrs) {
        return Mono.fromFuture(this.cluster.addMember(peerAddrs));
    }

    @Override
    public Mono<MemberRemoveResponse> removeMember(long memberID) {
        return Mono.fromFuture(this.cluster.removeMember(memberID));
    }

    @Override
    public Mono<MemberUpdateResponse> updateMember(long memberID, List<URI> peerAddrs) {
        return Mono.fromFuture(this.cluster.updateMember(memberID, peerAddrs));
    }

    @Override
    public void close() {
        this.cluster.close();
    }
}
