package ir.moke.client;

import ir.moke.SharedConcept;
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
import java.util.Scanner;

public class ClientRunner {
    private static final String prompt = "[CLI Messenger]> ";
    private static PrintWriter writer;
    private static final ECDH ecdh = new ECDH();
    private static final String salt = StringUtils.generateSalt();
    private static byte[] sharedSecret;

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 9091)) {
            InputStream is = socket.getInputStream();
            writer = new PrintWriter(socket.getOutputStream(), true);

            /* Send public key to server */
            PublicKey publicKey = ecdh.getPublicKey();
            String message = SharedConcept.encapsulateMessage("pub", publicKey.getEncoded(), null);
            send(message);


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            System.out.println("Wait to receive ECDH public key");
            while ((line = bufferedReader.readLine()) != null) {
                String cmd = line.split(":")[0];
                String payload_b64 = line.split(":")[1];
                byte[] payload = Base64Utils.decodeToBytes(payload_b64);
                if (cmd.equals("pub")) {
                    System.out.println("Received server public key: " + payload_b64);
                    PublicKey serverPublicKey = ecdh.convertByteArrayToPublicKey(payload);

                    System.out.println("Initialize ECDH");
                    ecdh.initializeSharedSecret(serverPublicKey);
                    sharedSecret = ecdh.getSharedSecret();
                    break;
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print(prompt);
            while (scanner.hasNext()) {
                if (socket.isClosed()) break;

                String text = scanner.nextLine();

                SecretKey key = AES.getKeyFromPassword(sharedSecret, salt);
                byte[] iv = AES.generateIv();
                byte[] payload = AES.encrypt(text.getBytes(), key, iv);
                System.out.println("IV: " + Base64Utils.encodeToString(iv));
                System.out.println("Payload: " + Base64Utils.encodeToString(payload));
                message = SharedConcept.encapsulateMessage("msg", payload, iv);
                send(message);
                System.out.print(prompt);
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
