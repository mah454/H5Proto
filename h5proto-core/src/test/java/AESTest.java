import ir.moke.h5proto.algorithm.AES;
import ir.moke.h5proto.utils.Base64Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class AESTest {
    private static final String message = "Hello Mahdi";
    private static final String password = "password";
    private static final String salt = "2264646";
    private static SecretKey secretKey;
    private static byte[] iv;

    @BeforeAll
    public static void init() throws Exception {
        secretKey = AES.getKeyFromPassword(password.getBytes(), salt);
        iv = AES.generateIv();
    }

    @Test
    public void checkEncryption() throws Exception {
        byte[] enc_bytes = AES.encrypt(message.getBytes(), secretKey, iv);
        String payload = Base64Utils.encodeToString(enc_bytes);
        System.out.println(payload);
        Assertions.assertNotNull(payload);


        String clearText = AES.decrypt(enc_bytes, secretKey, iv);
        System.out.println(clearText);
        Assertions.assertEquals(message, clearText);
    }
}
