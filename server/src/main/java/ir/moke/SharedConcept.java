package ir.moke;

import ir.moke.h5proto.utils.Base64Utils;

public class SharedConcept {
    public static String encapsulateMessage(String cmd, byte[] payload, byte[] iv) {
        String payload_b64 = Base64Utils.encodeToString(payload);
        if (iv != null) {
            String iv_b64 = Base64Utils.encodeToString(iv);
            return cmd + ":" + payload_b64 + ":" + iv_b64;
        }
        return cmd + ":" + payload_b64;
    }
}
