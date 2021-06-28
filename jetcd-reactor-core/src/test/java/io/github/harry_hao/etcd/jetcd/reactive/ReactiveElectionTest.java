package io.github.harry_hao.etcd.jetcd.reactive;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.Election;
import io.etcd.jetcd.election.CampaignResponse;
import io.etcd.jetcd.election.LeaderKey;
import io.etcd.jetcd.election.LeaderResponse;
import io.etcd.jetcd.election.ProclaimResponse;
import io.etcd.jetcd.election.ResignResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReactiveElectionTest {

    private Election election;

    private ReactiveElection reactiveElection;

    private ByteSequence electionName;

    @BeforeEach
    void setup() {
        Client client = mock(Client.class);
        this.election = mock(Election.class);
        when(client.getElectionClient()).thenReturn(this.election);

        ReactiveClient reactiveClient = ReactiveClient.builder(client).scheduler(Schedulers.single()).build();
        this.reactiveElection = reactiveClient.getElectionClient();

        this.electionName = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
    }

    @Test
    void testElectionCampaign() {
        CampaignResponse response = mock(CampaignResponse.class);
        CompletableFuture<CampaignResponse> future = new CompletableFuture<>();

        long leaseId = 1L;
        ByteSequence proposal = TestUtil.randomByteSequence();
        when(this.election.campaign(this.electionName, leaseId, proposal)).thenReturn(future);

        this.reactiveElection.campaign(this.electionName, leaseId, proposal).as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testElectionProclaim() {
        ProclaimResponse response = mock(ProclaimResponse.class);
        CompletableFuture<ProclaimResponse> future = new CompletableFuture<>();
        ByteSequence proposal = ByteSequence.from(UUID.randomUUID().toString(), UTF_8);
        LeaderKey leaderKey = mock(LeaderKey.class);
        when(this.election.proclaim(leaderKey, proposal)).thenReturn(future);

        this.reactiveElection.proclaim(leaderKey, proposal)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();
    }

    @Test
    void testElectionLeader() {
        LeaderResponse response = mock(LeaderResponse.class);
        CompletableFuture<LeaderResponse> future = new CompletableFuture<>();

        when(this.election.leader(this.electionName)).thenReturn(future);

        this.reactiveElection.leader(this.electionName)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(response))
            .expectNext(response)
            .verifyComplete();

    }

    @Test
    void testElectionObserve() {
        LeaderResponse response1 = mock(LeaderResponse.class);
        LeaderResponse response2 = mock(LeaderResponse.class);

        this.reactiveElection.observe(this.electionName)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> {
                ArgumentCaptor<Election.Listener> listenerCaptor = ArgumentCaptor.forClass(Election.Listener.class);
                verify(this.election).observe(Mockito.any(ByteSequence.class), listenerCaptor.capture());
                Election.Listener listener = listenerCaptor.getValue();
                listener.onNext(response1);
                listener.onNext(response2);
                listener.onCompleted();
            })
            .expectNext(response1)
            .expectNext(response2)
            .verifyComplete();
    }

    @Test
    void testElectionResign() {
        ResignResponse resignResponse = mock(ResignResponse.class);
        CompletableFuture<ResignResponse> future = new CompletableFuture<>();
        LeaderKey leaderKey = mock(LeaderKey.class);
        when(this.election.resign(leaderKey)).thenReturn(future);

        this.reactiveElection.resign(leaderKey)
            .as(StepVerifier::create)
            .expectSubscription()
            .then(() -> future.complete(resignResponse))
            .expectNext(resignResponse)
            .verifyComplete();
    }

}
