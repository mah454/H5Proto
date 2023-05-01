package ir.moke.h5proto;

public class Handshake {
    private int port ;
    private String cellPhoneNumber ;
    private byte[] sharedSecret ;

    public Handshake() {
    }

    public Handshake(int port, byte[] sharedSecret) {
        this.port = port;
        this.sharedSecret = sharedSecret;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }
}
