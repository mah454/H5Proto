package ir.moke.h5proto;

import ir.moke.h5proto.utils.Base64Utils;

public class SharedConcept {
    public static String encapsulateMessage(RPC rpc, byte[] payload, byte[] iv) {
        if (payload == null) return String.valueOf(rpc.getCode());
        String payload_b64 = Base64Utils.encodeToString(payload);
        if (iv != null) {
            String iv_b64 = Base64Utils.encodeToString(iv);
            return rpc.getCode() + ":" + payload_b64 + ":" + iv_b64;
        }
        return rpc.getCode() + ":" + payload_b64;
    }
}
