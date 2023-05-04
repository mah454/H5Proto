package ir.moke.server;

import ir.moke.h5proto.RPC;
import ir.moke.h5proto.SharedConcept;
import ir.moke.h5proto.algorithm.AES;
import ir.moke.h5proto.algorithm.ECDH;
import ir.moke.h5proto.utils.Base64Utils;
import ir.moke.h5proto.utils.StringUtils;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;

public class SocketThread extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private InputStream is;
    private final String salt = StringUtils.generateSalt();
    private final ECDH ecdh = new ECDH();
    private byte[] sharedSecret;

    public SocketThread(Socket socket) {
        try {
            int port = socket.getPort();
            System.out.println("Client connected on port: " + port);

            this.socket = socket;
            this.is = socket.getInputStream();
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            System.out.println("Wait to receive ECDH public key");
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("quit")) break;

                String rpcCode = line.split(":")[0];
                String payload_b64 = line.split(":")[1];
                byte[] payload = Base64Utils.decodeToBytes(payload_b64);
                RPC rpc = RPC.getValue(rpcCode);
                switch (rpc) {
                    case PUBLIC_KEY -> {
                        System.out.println("Received client public key: " + payload_b64);
                        PublicKey clientPublicKey = ecdh.convertByteArrayToPublicKey(payload);

                        System.out.println("Initialize ECDH");
                        ecdh.initializeSharedSecret(clientPublicKey);
                        PublicKey serverPublicKey = ecdh.getPublicKey();

                        /* Server send public key to client */
                        String message = SharedConcept.encapsulateMessage(RPC.PUBLIC_KEY, serverPublicKey.getEncoded(), null);
                        send(message);

                        /* generated shared secret */
                        this.sharedSecret = ecdh.getSharedSecret();

                        /* tell client for send cell phone number (ACKNOWLEDGE) */
                        message = SharedConcept.encapsulateMessage(RPC.AUTH_CELLPHONE_NUMBER, null, null);
                        send(message);
                    }
                    case AUTH_CELLPHONE_NUMBER -> {
                        /* mobile phone number */
                        String cellPhoneNumber = new String(payload);

                        /* Send salt [with sms] to client */
                        System.out.printf("Send Activation key %s to %s%n", salt, cellPhoneNumber);

                        /* Send cell phone number (ACKNOWLEDGE) */
                        String message = SharedConcept.encapsulateMessage(RPC.ACTIVATION_KEY, null, null);
                        send(message);
                    }
                    case MESSAGE_TEXT -> {
                        System.out.println("IV: " + line.split(":")[2]);
                        System.out.println("Payload: " + payload_b64);
                        byte[] iv_bytes = Base64Utils.decodeToBytes(line.split(":")[2]);

                        /* Generate AES key */
                        SecretKey key = AES.getKeyFromPassword(sharedSecret, salt);

                        /* Decrypt message */
                        String message = AES.decrypt(payload, key, iv_bytes);
                        System.out.println("Decrypted message: " + message);
                    }
                }
            }
            bufferedReader.close();
            is.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {
            writer.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
