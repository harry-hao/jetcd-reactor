package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Election;
import io.etcd.jetcd.election.CampaignResponse;
import io.etcd.jetcd.election.LeaderKey;
import io.etcd.jetcd.election.LeaderResponse;
import io.etcd.jetcd.election.ProclaimResponse;
import io.etcd.jetcd.election.ResignResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public class ReactiveElectionImpl implements ReactiveElection {

    private Election election;

    private Scheduler scheduler;

    ReactiveElectionImpl(Election election, Scheduler scheduler) {
        this.election = election;
        this.scheduler = scheduler;
    }

    @Override
    public Mono<CampaignResponse> campaign(ByteSequence electionName, long leaseId, ByteSequence proposal) {
        return Mono.fromFuture(this.election.campaign(electionName, leaseId, proposal)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<ProclaimResponse> proclaim(LeaderKey leaderKey, ByteSequence proposal) {
        return Mono.fromFuture(this.election.proclaim(leaderKey, proposal)).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<LeaderResponse> leader(ByteSequence electionName) {
        return Mono.fromFuture(this.election.leader(electionName)).subscribeOn(this.scheduler);
    }

    @Override
    public Flux<LeaderResponse> observe(ByteSequence electionName) {
        return Flux.<LeaderResponse> create(sink -> this.election.observe(electionName, new Election.Listener() {
            @Override
            public void onNext(LeaderResponse leaderResponse) {
                sink.next(leaderResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                sink.error(throwable);
            }

            @Override
            public void onCompleted() {
                sink.complete();
            }
        })).subscribeOn(this.scheduler);
    }

    @Override
    public Mono<ResignResponse> resign(LeaderKey leaderKey) {
        return Mono.fromFuture(this.election.resign(leaderKey)).subscribeOn(this.scheduler);
    }

    @Override
    public void close() {
        this.election.close();
    }
}
