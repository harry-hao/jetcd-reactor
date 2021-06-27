package io.github.harry_hao.etcd.jetcd.reactive;

import io.etcd.jetcd.Auth;
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
import reactor.core.publisher.Mono;

public class ReactiveAuthImpl implements ReactiveAuth {

    private Auth auth;

    ReactiveAuthImpl(Auth auth) {
        this.auth = auth;
    }

    @Override
    public Mono<AuthEnableResponse> authEnable() {
        return Mono.fromFuture(this.auth.authEnable());
    }

    @Override
    public Mono<AuthDisableResponse> authDisable() {
        return Mono.fromFuture(this.auth.authDisable());
    }

    @Override
    public Mono<AuthUserAddResponse> userAdd(ByteSequence user, ByteSequence password) {
        return Mono.fromFuture(this.auth.userAdd(user, password));
    }

    @Override
    public Mono<AuthUserDeleteResponse> userDelete(ByteSequence user) {
        return Mono.fromFuture(this.auth.userDelete(user));
    }

    @Override
    public Mono<AuthUserChangePasswordResponse> userChangePassword(ByteSequence user, ByteSequence password) {
        return Mono.fromFuture(this.auth.userChangePassword(user, password));
    }

    @Override
    public Mono<AuthUserGetResponse> userGet(ByteSequence user) {
        return Mono.fromFuture(this.auth.userGet(user));
    }

    @Override
    public Mono<AuthUserListResponse> userList() {
        return Mono.fromFuture(this.auth.userList());
    }

    @Override
    public Mono<AuthUserGrantRoleResponse> userGrantRole(ByteSequence user, ByteSequence role) {
        return Mono.fromFuture(this.auth.userGrantRole(user, role));
    }

    @Override
    public Mono<AuthUserRevokeRoleResponse> userRevokeRole(ByteSequence user, ByteSequence role) {
        return Mono.fromFuture(this.auth.userRevokeRole(user, role));
    }

    @Override
    public Mono<AuthRoleAddResponse> roleAdd(ByteSequence role) {
        return Mono.fromFuture(this.auth.roleAdd(role));
    }

    @Override
    public Mono<AuthRoleGrantPermissionResponse> roleGrantPermission(ByteSequence role,
        ByteSequence key,
        ByteSequence rangeEnd,
        Permission.Type permType) {
        return Mono.fromFuture(this.auth.roleGrantPermission(role, key, rangeEnd, permType));
    }

    @Override
    public Mono<AuthRoleGetResponse> roleGet(ByteSequence role) {
        return Mono.fromFuture(this.auth.roleGet(role));
    }

    @Override
    public Mono<AuthRoleListResponse> roleList() {
        return Mono.fromFuture(this.auth.roleList());
    }

    @Override
    public Mono<AuthRoleRevokePermissionResponse> roleRevokePermission(ByteSequence role,
        ByteSequence key,
        ByteSequence rangeEnd) {
        return Mono.fromFuture(this.auth.roleRevokePermission(role, key, rangeEnd));
    }

    @Override
    public Mono<AuthRoleDeleteResponse> roleDelete(ByteSequence role) {
        return Mono.fromFuture(this.auth.roleDelete(role));
    }

    @Override
    public void close() {
        this.auth.close();
    }
}
