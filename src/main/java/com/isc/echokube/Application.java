package com.isc.echokube;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Application {
    public static void main(String[] args) throws Exception {
        var server = new ServerSocket(8080, 50, InetAddress.getByName("0.0.0.0"));
        var handlerPool = Executors.newCachedThreadPool();
        try {
            while (true) {
                var socket = server.accept();
                handlerPool.submit(new HttpConnectionHandler(socket));
            }
        } catch (IOException exception) {
            server.close();
            exception.printStackTrace();
        }
    }
}


class HttpConnectionHandler implements Runnable {
    private Socket socket;

    HttpConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            handle();
        } catch (Exception ignored) {
        }

    }

    private void handle() throws Exception {
        var requestReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        var sb = new StringBuilder();
        String current;
        do {
            sb.append(current = requestReader.readLine());
        } while (!current.isEmpty());

        var response = sb.toString();
        var responseWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        responseWriter.write("HTTP/1.1 200 OK\n");
        responseWriter.write("Content-Type: text/html\n");
        responseWriter.write(String.format("Content-Length: %d\n", response.length()));
        responseWriter.write("\n");
        responseWriter.write(response);
        responseWriter.flush();
        socket.close();
    }
}