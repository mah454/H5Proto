package ir.moke.h5proto.algorithm;

import java.security.MessageDigest;
import java.util.HexFormat;

public class SHA {
    public static String sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(data);
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
