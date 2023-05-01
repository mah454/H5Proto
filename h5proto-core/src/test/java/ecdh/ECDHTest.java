package ecdh;

import ir.moke.h5proto.utils.Base64Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ECDHTest {

    @Test
    public void checkSharedSecret() {
        Node server = new Node();
        Node client = new Node();

        server.initializeSharedSecret(client.getPublicKey());
        client.initializeSharedSecret(server.getPublicKey());

        System.out.println(Base64Utils.encodeToString(client.getSharedSecret()));
        System.out.println(Base64Utils.encodeToString(server.getSharedSecret()));

        Assertions.assertArrayEquals(client.getSharedSecret(), server.getSharedSecret());
    }
}
