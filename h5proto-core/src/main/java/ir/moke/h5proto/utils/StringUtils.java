package ir.moke.h5proto.utils;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public interface StringUtils {

    static String generateSalt() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100_000, 900_000));
    }

    static byte[] generateRandomBytes(int length) {
        byte[] iv = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    static long nextSequence() {
        return System.nanoTime();
    }

    static boolean isUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
