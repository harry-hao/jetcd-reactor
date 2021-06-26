package io.github.harry_hao.etcd.jetcd.reactive;

import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;
import io.etcd.jetcd.ByteSequence;

public class Util {

    // ByteSequence.getByteString() is not visiable.
    public static ByteString prefixNamespace(ByteSequence key, ByteSequence namespace) {
        return namespace.isEmpty() ? ByteString.copyFrom(key.getBytes())
            : ByteString.copyFrom(Bytes.concat(key.getBytes(), namespace.getBytes()));
    }

    public static ByteString toByteString(ByteSequence byteSequence) {
        return ByteString.copyFrom(byteSequence.getBytes());
    }
}
