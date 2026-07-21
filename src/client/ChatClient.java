package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Handles network connection, sending, and receiving messages asynchronously.
 */
public class ChatClient {
    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Consumer<String> messageListener;
    private volatile boolean isConnected = false;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener;
    }

    public boolean connect(String username) {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Handshake protocol
            String serverSignal = reader.readLine();
            if ("SUBMITNAME".equals(serverSignal)) {
                writer.println(username);
                String response = reader.readLine();
                if (response != null && response.startsWith("NAMEACCEPTED")) {
                    isConnected = true;
                    startListening();
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        }
        return false;
    }

    private void startListening() {
        Thread listenerThread = new Thread(() -> {
            try {
                String message;
                while (isConnected && (message = reader.readLine()) != null) {
                    if (messageListener != null) {
                        messageListener.accept(message);
                    }
                }
            } catch (IOException e) {
                if (isConnected) {
                    System.err.println("Connection lost: " + e.getMessage());
                }
            } finally {
                disconnect();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendMessage(String text) {
        if (writer != null && isConnected) {
            writer.println(text);
        }
    }

    public synchronized void disconnect() {
        if (!isConnected) return;
        isConnected = false;
        try {
            if (writer != null) sendMessage("/quit");
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}