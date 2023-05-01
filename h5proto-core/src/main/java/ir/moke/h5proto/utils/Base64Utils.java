package ir.moke.h5proto.utils;

import java.util.Base64;

public interface Base64Utils {
    static String encodeToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    static byte[] decodeToBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    static String decodeToString(String base64) {
        return new String(decodeToBytes(base64));
    }
}
