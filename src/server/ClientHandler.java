package server;
import common.ChatLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles communication between the server and a single connected client.
 * Runs on its own thread managed by the ChatServer thread pool.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    private volatile boolean isConnected = true;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Setup input and output streams
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Request username from client upon connection
            writer.println("SUBMITNAME");
            String requestedName = reader.readLine();

            if (requestedName == null || requestedName.trim().isEmpty()) {
                closeConnection();
                return;
            }

            this.username = requestedName.trim();
            writer.println("NAMEACCEPTED " + this.username);
            
            // Register client with server and notify others
            server.registerClient(this);

            // Main message processing loop
            String incomingMessage;
            while (isConnected && (incomingMessage = reader.readLine()) != null) {
                if (incomingMessage.equalsIgnoreCase("/quit")) {
                    break;
                }
                
                if (!incomingMessage.trim().isEmpty()) {
                    String formattedMessage = this.username + ": " + incomingMessage;
                    ChatLogger.info(formattedMessage);
                    server.broadcast(formattedMessage, this);
                }
            }

        } catch (IOException e) {
            ChatLogger.error("Connection error for user (" + username + "): " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Sends a raw message text line to this connected client.
     */
    public void sendMessage(String message) {
        if (writer != null && !writer.checkError()) {
            writer.println(message);
        }
    }

    /**
     * Safely closes client socket connection and releases stream resources.
     */
    public synchronized void closeConnection() {
        if (!isConnected) return;
        isConnected = false;

        server.unregisterClient(this);

        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            ChatLogger.error("Error closing resources for " + username + ": " + e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }
}