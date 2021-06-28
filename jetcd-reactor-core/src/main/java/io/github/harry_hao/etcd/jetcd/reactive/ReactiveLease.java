package io.github.harry_hao.etcd.jetcd.reactive;

import java.util.concurrent.TimeUnit;

import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.etcd.jetcd.lease.LeaseRevokeResponse;
import io.etcd.jetcd.lease.LeaseTimeToLiveResponse;
import io.etcd.jetcd.options.LeaseOption;
import io.etcd.jetcd.support.CloseableClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveLease extends CloseableClient {
    /**
     * New a lease with ttl value.
     *
     * @param  ttl ttl value, unit seconds
     * @return     the grant response
     */
    Mono<LeaseGrantResponse> grant(long ttl);

    /**
     * New a lease with ttl value.Waits if necessary for at most the given time
     * if etcd server is available.
     *
     * @param  ttl     ttl value, unit seconds
     * @param  timeout the maximum time to waits
     * @param  unit    the time unit of the timeout argument
     * @return         The grant response
     */
    Mono<LeaseGrantResponse> grant(long ttl, long timeout, TimeUnit unit);

    /**
     * revoke one lease and the key bind to this lease will be removed.
     *
     * @param  leaseId id of the lease to revoke
     * @return         the revoke response
     */
    Mono<LeaseRevokeResponse> revoke(long leaseId);

    /**
     * keep alive one lease only once.
     *
     * @param  leaseId id of lease to keep alive once
     * @return         The keep alive response
     */
    Mono<LeaseKeepAliveResponse> keepAliveOnce(long leaseId);

    /**
     * retrieves the lease information of the given lease ID.
     *
     * @param  leaseId     id of lease
     * @param  leaseOption LeaseOption
     * @return             LeaseTimeToLiveResponse wrapped in CompletableFuture
     */
    Mono<LeaseTimeToLiveResponse> timeToLive(long leaseId, LeaseOption leaseOption);

    /**
     * keep the given lease alive forever.
     *
     * @param  leaseId  lease to be keep alive forever.
     * @param  observer the observer
     * @return          a KeepAliveListener that listens for KeepAlive responses.
     */
    Flux<LeaseKeepAliveResponse> keepAlive(long leaseId);
}
