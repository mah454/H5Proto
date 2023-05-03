package ir.moke.client;

import ir.moke.h5proto.RPC;
import ir.moke.h5proto.SharedConcept;
import ir.moke.h5proto.algorithm.AES;
import ir.moke.h5proto.algorithm.ECDH;
import ir.moke.h5proto.utils.Base64Utils;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;

import static ir.moke.h5proto.RPC.AUTH_CELLPHONE_NUMBER;
import static ir.moke.h5proto.RPC.MESSAGE_TEXT;

public class ClientRunner {
    private static final String prompt = "[CLI Messenger]> ";
    private static PrintWriter writer;
    private static final ECDH ecdh = new ECDH();
    private static byte[] sharedSecret;
    private static SecretKey key;

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 9091)) {
            InputStream is = socket.getInputStream();
            writer = new PrintWriter(socket.getOutputStream(), true);

            /* Send public key to server */
            PublicKey publicKey = ecdh.getPublicKey();
            String message = SharedConcept.encapsulateMessage(RPC.PUBLIC_KEY, publicKey.getEncoded(), null);
            send(message);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            System.out.println("Wait to receive ECDH public key");
            Scanner scanner = new Scanner(System.in);
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(":");
                String rpcCode = split[0];
                String payload_b64 = split.length > 1 ? split[1] : null;
                byte[] payload = payload_b64 != null ? Base64Utils.decodeToBytes(payload_b64) : null;
                RPC rpc = RPC.getValue(rpcCode);

                switch (rpc) {
                    case PUBLIC_KEY -> {
                        System.out.println("Received server public key: " + payload_b64);
                        PublicKey serverPublicKey = ecdh.convertByteArrayToPublicKey(payload);
                        System.out.println("Initialize ECDH");
                        ecdh.initializeSharedSecret(serverPublicKey);
                        sharedSecret = ecdh.getSharedSecret();
                    }
                    case AUTH_CELLPHONE_NUMBER -> {
                        System.out.print("Enter mobile number: ");
                        String text = scanner.nextLine();
                        message = SharedConcept.encapsulateMessage(AUTH_CELLPHONE_NUMBER, text.getBytes(), null);
                        send(message);
                    }
                    case ACTIVATION_KEY -> {
                        System.out.print("Enter activation key: ");
                        String text = scanner.nextLine();
                        key = AES.getKeyFromPassword(sharedSecret, text);
                        byte[] iv = AES.generateIv();
                        byte[] nextMessage = AES.encrypt(text.getBytes(), key, iv);
                        System.out.println("IV: " + Base64Utils.encodeToString(iv));
                        System.out.println("Payload: " + Base64Utils.encodeToString(nextMessage));
                        message = SharedConcept.encapsulateMessage(MESSAGE_TEXT, nextMessage, iv);
                        send(message);
                    }
                }
                System.out.println("--------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void send(String message) {
        try {
            writer.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
