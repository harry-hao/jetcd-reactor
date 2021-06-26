package io.github.harry_hao.etcd.jetcd.reactive;

import java.net.URI;
import java.util.List;

import io.etcd.jetcd.cluster.MemberAddResponse;
import io.etcd.jetcd.cluster.MemberListResponse;
import io.etcd.jetcd.cluster.MemberRemoveResponse;
import io.etcd.jetcd.cluster.MemberUpdateResponse;
import io.etcd.jetcd.support.CloseableClient;
import reactor.core.publisher.Mono;

/**
 * Interface of cluster client talking to etcd.
 */
public interface ReactiveCluster extends CloseableClient {

    /**
     * lists the current cluster membership.
     *
     * @return the response
     */
    Mono<MemberListResponse> listMember();

    /**
     * add a new member into the cluster.
     *
     * @param  peerAddrs the peer addresses of the new member
     * @return           the response
     */
    Mono<MemberAddResponse> addMember(List<URI> peerAddrs);

    /**
     * removes an existing member from the cluster.
     *
     * @param  memberID the member to remove.
     * @return          the response
     */
    Mono<MemberRemoveResponse> removeMember(long memberID);

    /**
     * update peer addresses of the member.
     *
     * @param  memberID  the member id.
     * @param  peerAddrs the addresses.
     * @return           the response
     */
    Mono<MemberUpdateResponse> updateMember(long memberID, List<URI> peerAddrs);

}
