package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.election.CampaignResponse;
import io.etcd.jetcd.election.LeaderKey;
import io.etcd.jetcd.election.LeaderResponse;
import io.etcd.jetcd.election.ProclaimResponse;
import io.etcd.jetcd.election.ResignResponse;
import io.etcd.jetcd.support.CloseableClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface of leader election client talking to etcd.
 */
public interface ReactiveElection extends CloseableClient {
    /**
     * Campaign waits to acquire leadership in an election, returning a leader key
     * representing the leadership if successful. The leader key can then be used
     * to issue new values on the election, transactionally guard API requests on
     * leadership still being held, and resign from the election.
     *
     * @param  electionName election name
     * @param  leaseId      lease identifier
     * @param  proposal     proposal
     * @return              the response
     */
    Mono<CampaignResponse> campaign(ByteSequence electionName, long leaseId, ByteSequence proposal);

    /**
     * Proclaim updates the leader's posted value with a new value. Only active
     * leader can change the value.
     *
     * @param  leaderKey leader key
     * @param  proposal  new proposal
     * @return           the response
     */
    Mono<ProclaimResponse> proclaim(LeaderKey leaderKey, ByteSequence proposal);

    /**
     * Returns the current election proclamation, if any.
     *
     * @param  electionName election name
     * @return              the response
     */
    Mono<LeaderResponse> leader(ByteSequence electionName);

    /**
     * Listens to election proclamations in-order as made by the election's
     * elected leaders.
     *
     * @param electionName election name
     * @param listener     listener
     */
    Flux<LeaderResponse> observe(ByteSequence electionName);

    /**
     * Resign releases election leadership so other campaigners may acquire
     * leadership on the election.
     *
     * @param  leaderKey leader key
     * @return           the response
     */
    Mono<ResignResponse> resign(LeaderKey leaderKey);

}
