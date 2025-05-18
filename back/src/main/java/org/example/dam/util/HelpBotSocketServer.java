package org.example.dam.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HelpBotSocketServer implements Runnable {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("[HelpBot] Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("[HelpBot] Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println("👋 Welcome to the HelpBot! Ask your question:");
                String message;

                while ((message = in.readLine()) != null) {
                    System.out.println("[HelpBot] User asked: " + message);
                    out.println(botReply(message));
                }
            } catch (IOException e) {
                System.out.println("[HelpBot] Disconnected.");
            }
        }

        private String botReply(String input) {
            input = input.toLowerCase();
            if (input.contains("price")) return "💬 Flights start at 49.99€.";
            if (input.contains("cancel")) return "💬 Cancel in the 'Your Bookings' section.";
            if (input.contains("refund")) return "💬 Refunds available up to 24h before flight.";
            return "🤖 Try asking about price, cancel, or refund.";
        }
    }
}
