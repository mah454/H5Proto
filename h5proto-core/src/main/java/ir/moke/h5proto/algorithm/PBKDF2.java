package ir.moke.h5proto.algorithm;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.Key;

public class PBKDF2 {
    public static Key generateStrongPasswordHash(byte[] password, String salt) throws Exception {
        int iterations = 1000;
        String sha256 = SHA.sha256(password);
        PBEKeySpec spec = new PBEKeySpec(sha256.toCharArray(), salt.getBytes(), iterations, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return skf.generateSecret(spec);
    }
}
