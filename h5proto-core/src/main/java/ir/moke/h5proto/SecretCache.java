package ir.moke.h5proto;

import java.util.ArrayList;
import java.util.List;

public class SecretCache {
    public static final SecretCache instance = new SecretCache();
    private static final List<Handshake> CACHE = new ArrayList<>();

    private SecretCache() {

    }

    public void add(int port, byte[] sharedSecret) {
        CACHE.add(new Handshake(port, sharedSecret));
    }

    public Handshake find(int port) {
        return CACHE.stream().filter(item -> item.getPort() == port).findFirst().orElse(null);
    }

    public Handshake find(String cellPhoneNumber) {
        return CACHE.stream().filter(item -> item.getCellPhoneNumber().equals(cellPhoneNumber)).findFirst().orElse(null);
    }

    public void remove(int port) {
        CACHE.remove(find(port));
    }

    public void remove(String cellPhoneNumber) {
        CACHE.remove(find(cellPhoneNumber));
    }

    public void setCellPhoneNumber(int port, String cellPhoneNumber) {
        Handshake handshake = find(port);
        handshake.setCellPhoneNumber(cellPhoneNumber);
    }
}
