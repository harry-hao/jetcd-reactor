package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Maintenance;

import io.etcd.jetcd.maintenance.AlarmResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;

class ReactiveMaintenanceTest {

    private Maintenance maintenance;

    private ReactiveMaintenance reactiveMaintenance;

    @BeforeEach
    void setup() {
        this.maintenance = mock(Maintenance.class);
        this.reactiveMaintenance = new ReactiveMaintenanceImpl(this.maintenance);
    }

    @Test
    void testListAlarms() {
        AlarmResponse response = mock(AlarmResponse.class);
        CompletableFuture<AlarmResponse> future = new CompletableFuture<>();
        Mockito.when(this.maintenance.listAlarms()).thenReturn(future);

        this.reactiveMaintenance.listAlarms()
                .as(StepVerifier::create)
                .expectSubscription()
                .then(() -> future.complete(response))
                .expectNext(response)
                .verifyComplete();
    }
}
