package ir.moke.h5proto.algorithm;

import ir.moke.h5proto.utils.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AES {
    private static final String KEY_ALGORITHM = "AES";
    private static final String ENC_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 16;

    public static SecretKey getKeyFromPassword(byte[] password, String salt) throws Exception {
        Key key = PBKDF2.generateStrongPasswordHash(password, salt);
        return new SecretKeySpec(key.getEncoded(), KEY_ALGORITHM);
    }

    public static byte[] generateIv() {
        return StringUtils.generateRandomBytes(GCM_IV_LENGTH);
    }

    public static byte[] encrypt(byte[] payload, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ENC_ALGORITHM);
        var gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        return cipher.doFinal(payload);
    }

    public static String decrypt(byte[] payload, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ENC_ALGORITHM);
        var gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] plainText = cipher.doFinal(payload);
        return new String(plainText);
    }
}
