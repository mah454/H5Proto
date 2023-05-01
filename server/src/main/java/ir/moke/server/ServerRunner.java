package ir.moke.server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunner {
    public static void main(String[] args) throws Exception {
        System.out.println("Start server on port 9091");
        try (ServerSocket server = new ServerSocket(9091)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                new SocketThread(socket).start();
            }
        }
    }
}
