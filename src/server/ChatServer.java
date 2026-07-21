package server;
import common.ChatLogger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main Chat Server entry point.
 * Listens for incoming client connections and dispatches them to a thread pool.
 */
public class ChatServer {
    private static final int DEFAULT_PORT = 12345;
    private static final int MAX_THREADS = 50;

    // Thread-safe set of connected active client handlers
    private final Set<ClientHandler> activeClients = Collections.synchronizedSet(new HashSet<>());
    // Thread pool to manage concurrent client handler threads efficiently
    private final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);
    
    private final int port;
    private volatile boolean isRunning = true;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        ChatLogger.info("Starting Chat Server on port " + port + "...");
        
        // Add shutdown hook to handle graceful termination (e.g., Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ChatLogger.info("Server bound successfully. Waiting for clients...");

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ChatLogger.info("New connection attempt from: " + clientSocket.getRemoteSocketAddress());

                    // Create client handler instance
                    ClientHandler handler = new ClientHandler(clientSocket, this);
                    threadPool.execute(handler);
                } catch (IOException e) {
                    if (!isRunning) {
                        ChatLogger.info("Server socket closed via shutdown hook.");
                        break;
                    }
                    ChatLogger.error("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            ChatLogger.error("Could not start server on port " + port + ": " + e.getMessage());
        } finally {
            stop();
        }
    }

    /**
     * Broadcasts a text message to all currently connected clients.
     *
     * @param message Message to deliver.
     * @param sender  Client handler originating the message (can be null for system messages).
     */
    public void broadcast(String message, ClientHandler sender) {
        synchronized (activeClients) {
            for (ClientHandler client : activeClients) {
                // Optionally send to all including sender, or exclude sender if needed
                client.sendMessage(message);
            }
        }
    }

    /**
     * Registers a authenticated/named client to the active list.
     */
    public void registerClient(ClientHandler handler) {
        activeClients.add(handler);
        ChatLogger.info("Registered client: " + handler.getUsername() + " | Active clients: " + activeClients.size());
        broadcast("[SYSTEM] " + handler.getUsername() + " joined the chat.", null);
    }

    /**
     * Unregisters a client when disconnected.
     */
    public void unregisterClient(ClientHandler handler) {
        if (activeClients.remove(handler)) {
            ChatLogger.info("Unregistered client: " + handler.getUsername() + " | Active clients: " + activeClients.size());
            if (handler.getUsername() != null) {
                broadcast("[SYSTEM] " + handler.getUsername() + " left the chat.", null);
            }
        }
    }

    /**
     * Stops the server and frees up thread pool resource locks.
     */
    public synchronized void stop() {
        if (!isRunning) return;
        isRunning = false;
        ChatLogger.info("Shutting down Chat Server...");

        // Disconnect all clients safely
        synchronized (activeClients) {
            for (ClientHandler client : activeClients) {
                client.closeConnection();
            }
            activeClients.clear();
        }

        threadPool.shutdownNow();
        ChatLogger.info("Chat Server terminated cleanly.");
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number provided. Defaulting to " + DEFAULT_PORT);
            }
        }

        ChatServer server = new ChatServer(port);
        server.start();
    }
}