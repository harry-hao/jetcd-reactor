package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.auth.AuthDisableResponse;
import io.etcd.jetcd.auth.AuthEnableResponse;
import io.etcd.jetcd.auth.AuthRoleAddResponse;
import io.etcd.jetcd.auth.AuthRoleDeleteResponse;
import io.etcd.jetcd.auth.AuthRoleGetResponse;
import io.etcd.jetcd.auth.AuthRoleGrantPermissionResponse;
import io.etcd.jetcd.auth.AuthRoleListResponse;
import io.etcd.jetcd.auth.AuthRoleRevokePermissionResponse;
import io.etcd.jetcd.auth.AuthUserAddResponse;
import io.etcd.jetcd.auth.AuthUserChangePasswordResponse;
import io.etcd.jetcd.auth.AuthUserDeleteResponse;
import io.etcd.jetcd.auth.AuthUserGetResponse;
import io.etcd.jetcd.auth.AuthUserGrantRoleResponse;
import io.etcd.jetcd.auth.AuthUserListResponse;
import io.etcd.jetcd.auth.AuthUserRevokeRoleResponse;
import io.etcd.jetcd.auth.Permission;
import io.etcd.jetcd.support.CloseableClient;
import reactor.core.publisher.Mono;

/**
 * Interface of auth talking to etcd.
 */
public interface ReactiveAuth extends CloseableClient {

    /**
     * enables auth of an etcd cluster.
     *
     * @return the response.
     */
    Mono<AuthEnableResponse> authEnable();

    /**
     * disables auth of an etcd cluster.
     *
     * @return the response.
     */
    Mono<AuthDisableResponse> authDisable();

    /**
     * adds a new user to an etcd cluster.
     *
     * @param  user     the user
     * @param  password the password
     * @return          the response.
     */
    Mono<AuthUserAddResponse> userAdd(ByteSequence user, ByteSequence password);

    /**
     * deletes a user from an etcd cluster.
     *
     * @param  user the user
     * @return      the response.
     */
    Mono<AuthUserDeleteResponse> userDelete(ByteSequence user);

    /**
     * changes a password of a user.
     *
     * @param  user     the user
     * @param  password the password
     * @return          the response.
     */
    Mono<AuthUserChangePasswordResponse> userChangePassword(ByteSequence user, ByteSequence password);

    /**
     * gets a detailed information of a user.
     *
     * @param  user the user
     * @return      the response.
     */
    Mono<AuthUserGetResponse> userGet(ByteSequence user);

    /**
     * gets a list of all users.
     *
     * @return the response.
     */
    Mono<AuthUserListResponse> userList();

    /**
     * grants a role to a user.
     *
     * @param  user the user
     * @param  role the role to grant
     * @return      the response.
     */
    Mono<AuthUserGrantRoleResponse> userGrantRole(ByteSequence user, ByteSequence role);

    /**
     * revokes a role of a user.
     *
     * @param  user the user
     * @param  role the role to revoke
     * @return      the response.
     */
    Mono<AuthUserRevokeRoleResponse> userRevokeRole(ByteSequence user, ByteSequence role);

    /**
     * adds a new role to an etcd cluster.
     *
     * @param  role the role to add
     * @return      the response.
     */
    Mono<AuthRoleAddResponse> roleAdd(ByteSequence role);

    /**
     * grants a permission to a role.
     *
     * @param  role     the role
     * @param  key      the key
     * @param  rangeEnd the range end
     * @param  permType the type
     * @return          the response.
     */
    Mono<AuthRoleGrantPermissionResponse> roleGrantPermission(ByteSequence role, ByteSequence key,
        ByteSequence rangeEnd, Permission.Type permType);

    /**
     * gets a detailed information of a role.
     *
     * @param  role the role to get
     * @return      the response.
     */
    Mono<AuthRoleGetResponse> roleGet(ByteSequence role);

    /**
     * gets a list of all roles.
     *
     * @return the response.
     */
    Mono<AuthRoleListResponse> roleList();

    /**
     * revokes a permission from a role.
     *
     * @param  role     the role
     * @param  key      the key
     * @param  rangeEnd the range end
     * @return          the response.
     */
    Mono<AuthRoleRevokePermissionResponse> roleRevokePermission(ByteSequence role, ByteSequence key,
        ByteSequence rangeEnd);

    /**
     * RoleDelete deletes a role.
     *
     * @param  role the role to delete.
     * @return      the response.
     */
    Mono<AuthRoleDeleteResponse> roleDelete(ByteSequence role);

}
