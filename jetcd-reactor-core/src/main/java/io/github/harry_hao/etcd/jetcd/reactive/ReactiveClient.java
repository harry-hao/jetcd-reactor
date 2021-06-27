package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;

/**
 * Etcd Client.
 *
 * <p>
 * The implementation may throw unchecked ConnectException or AuthFailedException on
 * initialization (or when invoking *Client methods if configured to initialize lazily).
 */
public interface ReactiveClient extends AutoCloseable {

    /**
     * @return the {@link ReactiveAuth} client.
     */
    ReactiveAuth getAuthClient();

    /**
     * @return the {@link ReactiveKV} client.
     */
    ReactiveKV getKVClient();

    /**
     * @return the {@link ReactiveCluster} client.
     */
    ReactiveCluster getClusterClient();

    /**
     * @return the {@link ReactiveMaintenance} client.
     */
    ReactiveMaintenance getMaintenanceClient();

    /**
     * @return the {@link ReactiveLease} client.
     */
    ReactiveLease getLeaseClient();

    /**
     * @return the {@link ReactiveWatch} client.
     */
    ReactiveWatch getWatchClient();

    /**
     * @return the {@link ReactiveLock} client.
     */
    ReactiveLock getLockClient();

    /**
     * @return the {@link ReactiveElection} client.
     */
    ReactiveElection getElectionClient();

    @Override
    void close();

    /**
     * @return a new {@link ClientBuilder}.
     */
    static ReactiveClientBuilder builder(Client client) {
        return new ReactiveClientBuilder(client);
    }
}
