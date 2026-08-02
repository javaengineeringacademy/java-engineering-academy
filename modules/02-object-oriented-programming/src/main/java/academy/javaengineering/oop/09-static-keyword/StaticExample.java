package academy.javaengineering.oop.`09-static-keyword`;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates static methods, static blocks, and static nested classes.
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Static methods: belong to the class, not instances</li>
 *   <li>Static blocks: one-time initialization code</li>
 *   <li>Static variables: shared across all instances</li>
 *   <li>Static nested classes: don't need enclosing instance</li>
 *   <li>Static import and utility classes</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class StaticExample {

    // ==================== Static Variables ====================

    /**
     * Connection pool manager demonstrating static shared state.
     */
    public static class ConnectionPool {
        private static ConnectionPool instance;
        private static int totalInstances = 0;

        private final Map<String, Connection> connections;
        private final int maxSize;

        /** Static initializer block - runs once when class is loaded. */
        static {
            System.out.println("[STATIC] ConnectionPool class loaded");
            totalInstances = 0;
        }

        private ConnectionPool(int maxSize) {
            this.maxSize = maxSize;
            this.connections = new HashMap<>();
            totalInstances++;
        }

        /** Thread-safe singleton using static method. */
        public static synchronized ConnectionPool getInstance(int maxSize) {
            if (instance == null) {
                instance = new ConnectionPool(maxSize);
            }
            return instance;
        }

        public static int getTotalInstances() { return totalInstances; }

        public Connection acquire(String name) {
            if (connections.size() >= maxSize) {
                throw new RuntimeException("Connection pool exhausted");
            }
            Connection conn = new Connection(name);
            connections.put(name, conn);
            return conn;
        }

        public void release(String name) {
            connections.remove(name);
        }

        public int getActiveCount() { return connections.size(); }
    }

    /**
     * Simple connection representation.
     */
    public static class Connection {
        private final String name;

        public Connection(String name) {
            this.name = name;
        }

        public String getName() { return name; }

        @Override
        public String toString() {
            return "Connection{'%s'}".formatted(name);
        }
    }

    // ==================== Static Methods ====================

    /**
     * Utility class with static methods only.
     */
    public static final class StringUtils {
        private StringUtils() {} // Prevent instantiation

        public static boolean isBlank(String s) {
            return s == null || s.isBlank();
        }

        public static String capitalize(String s) {
            if (isBlank(s)) return s;
            return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
        }

        public static String repeat(String s, int count) {
            if (count <= 0) return "";
            return s.repeat(count);
        }

        public static String truncate(String s, int maxLength) {
            if (s == null) return null;
            return s.length() > maxLength ? s.substring(0, maxLength) + "..." : s;
        }
    }

    // ==================== Static Nested Classes ====================

    /**
     * Static nested class - doesn't need enclosing instance.
     */
    public static class ConfigParser {
        private final Map<String, String> properties;

        public ConfigParser() {
            this.properties = new HashMap<>();
        }

        public void parse(String configLine) {
            String[] parts = configLine.split("=", 2);
            if (parts.length == 2) {
                properties.put(parts[0].trim(), parts[1].trim());
            }
        }

        public String get(String key) { return properties.get(key); }
        public int size() { return properties.size(); }

        @Override
        public String toString() {
            return "ConfigParser{properties=" + properties + "}";
        }
    }

    /**
     * Enterprise config with static factory and nested parser.
     */
    public static class AppConfig {
        private final String host;
        private final int port;
        private final boolean ssl;

        private AppConfig(String host, int port, boolean ssl) {
            this.host = host;
            this.port = port;
            this.ssl = ssl;
        }

        /** Static factory method - preferred over constructor for clarity. */
        public static AppConfig create(String host, int port) {
            return new AppConfig(host, port, false);
        }

        /** Static factory with SSL. */
        public static AppConfig createSecure(String host, int port) {
            return new AppConfig(host, port, true);
        }

        /** Static factory from config string. */
        public static AppConfig fromString(String config) {
            String[] parts = config.split(":");
            String host = parts[0];
            int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 8080;
            boolean ssl = config.startsWith("https");
            return new AppConfig(host, port, ssl);
        }

        public String getHost() { return host; }
        public int getPort() { return port; }
        public boolean isSsl() { return ssl; }

        @Override
        public String toString() {
            String protocol = ssl ? "https" : "http";
            return "%s://%s:%d".formatted(protocol, host, port);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Static Keyword Demo ===\n");

        // Static methods - no instance needed
        System.out.println("--- Static Methods (StringUtils) ---");
        System.out.println("isBlank(null):    " + StringUtils.isBlank(null));
        System.out.println("isBlank(''):      " + StringUtils.isBlank(""));
        System.out.println("isBlank('hello'): " + StringUtils.isBlank("hello"));
        System.out.println("capitalize:       " + StringUtils.capitalize("hello world"));
        System.out.println("repeat:           " + StringUtils.repeat("ab", 3));
        System.out.println("truncate:         " + StringUtils.truncate("Very long text here", 10));

        // Static block - class loading
        System.out.println("\n--- Static Blocks ---");
        System.out.println("Loading class...");
        @SuppressWarnings("unused")
        int _ = ConnectionPool.getTotalInstances(); // Triggers class loading

        // Static singleton
        System.out.println("\n--- Singleton with Static ---");
        ConnectionPool pool = ConnectionPool.getInstance(5);
        ConnectionPool pool2 = ConnectionPool.getInstance(5);
        System.out.println("Same instance: " + (pool == pool2));
        System.out.println("Total instances: " + ConnectionPool.getTotalInstances());

        Connection c1 = pool.acquire("db-primary");
        Connection c2 = pool.acquire("db-replica");
        System.out.println("Acquired: " + c1 + ", " + c2);
        System.out.println("Active: " + pool.getActiveCount());

        pool.release("db-replica");
        System.out.println("After release: " + pool.getActiveCount());

        // Static factory methods
        System.out.println("\n--- Static Factory Methods ---");
        AppConfig http = AppConfig.create("api.example.com", 8080);
        AppConfig https = AppConfig.createSecure("api.example.com", 443);
        AppConfig fromStr = AppConfig.fromString("https://db.example.com:5432");

        System.out.println("HTTP:  " + http);
        System.out.println("HTTPS: " + https);
        System.out.println("Parsed: " + fromStr);

        // Static nested class
        System.out.println("\n--- Static Nested Class ---");
        ConfigParser parser = new ConfigParser();
        parser.parse("db.host=localhost");
        parser.parse("db.port=5432");
        parser.parse("app.name=MyService");
        System.out.println(parser);
        System.out.println("db.host = " + parser.get("db.host"));
    }
}
