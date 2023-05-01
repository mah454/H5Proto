package ir.moke.h5proto.algorithm;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;

public class ECDH {
    private PublicKey publicKey;
    private KeyAgreement keyAgreement;
    private byte[] sharedSecret;

    public ECDH() {
        makeKeyExchangeParams();
    }

    private void makeKeyExchangeParams() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec secp256r1 = new ECGenParameterSpec("secp256r1");
            kpg.initialize(secp256r1);
            KeyPair kp = kpg.generateKeyPair();
            publicKey = kp.getPublic();
            keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(kp.getPrivate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeSharedSecret(PublicKey publickey) {
        try {
            keyAgreement.doPhase(publickey, true);
            sharedSecret = keyAgreement.generateSecret();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public PublicKey convertByteArrayToPublicKey(byte[] bytes) {
        try {
            KeyFactory kf = KeyFactory.getInstance("EC");
            X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(bytes);
            return kf.generatePublic(pkSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }
}
