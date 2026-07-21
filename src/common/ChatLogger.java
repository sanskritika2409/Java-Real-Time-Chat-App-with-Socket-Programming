package common;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread-safe logging utility for server activities.
 */
public class ChatLogger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static synchronized void info(String message) {
        log("INFO", message);
    }

    public static synchronized void error(String message) {
        System.err.printf("[%s] [ERROR] %s%n", getTimestamp(), message);
    }

    private static void log(String level, String message) {
        System.out.printf("[%s] [%s] %s%n", getTimestamp(), level, message);
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}