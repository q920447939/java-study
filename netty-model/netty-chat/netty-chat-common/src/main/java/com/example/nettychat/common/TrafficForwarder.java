package com.example.nettychat.common;

import java.io.*;
import java.net.*;

public class TrafficForwarder {
    private static final String REMOTE_HOST = "172.18.200.172";
    private static final int REMOTE_PORT = 7892;
    private static final int LOCAL_PORT = 6666;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(LOCAL_PORT);
        System.out.println("Local server started on port " + LOCAL_PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted incoming TCP connection from " + clientSocket.getInetAddress().getHostAddress());

            new Thread(() -> {
                try {
                    Socket remoteSocket = new Socket(REMOTE_HOST, REMOTE_PORT);
                    System.out.println("Connected to remote server at " + remoteSocket.getInetAddress().getHostAddress());

                    // start forwarding data
                    forwardData(clientSocket.getInputStream(), remoteSocket.getOutputStream(), clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " => " + REMOTE_HOST + ":" + REMOTE_PORT);
                    forwardData(remoteSocket.getInputStream(), clientSocket.getOutputStream(), REMOTE_HOST + ":" + REMOTE_PORT + " => " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                    // close sockets when done
                    clientSocket.close();
                    remoteSocket.close();
                    System.out.println("Connection closed");
                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }).start();
        }
    }

    private static void forwardData(InputStream input, OutputStream output, String connectionName) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            System.out.println(connectionName + " - " + bytesRead + " bytes forwarded");
        }
    }
}
