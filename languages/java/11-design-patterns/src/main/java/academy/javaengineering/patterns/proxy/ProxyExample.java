package academy.javaengineering.patterns.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates all 5 flavors of the Proxy design pattern.
 *
 * <h3>Proxy Flavors:</h3>
 * <ol>
 *   <li>Virtual Proxy (Lazy Loading)</li>
 *   <li>Protection Proxy (Access Control)</li>
 *   <li>Caching Proxy</li>
 *   <li>Logging Proxy</li>
 *   <li>Remote Proxy (Simulated)</li>
 * </ol>
 */
public class ProxyExample {

    // ========================================
    // Shared Interface
    // ========================================
    public interface Database {
        String query(String sql);
        void connect();
        void disconnect();
    }

    // ========================================
    // Real Subject
    // ========================================
    public static class RealDatabase implements Database {
        private final String name;
        private boolean connected = false;
        
        public RealDatabase(String name) {
            this.name = name;
            System.out.println("RealDatabase: Creating expensive connection to " + name);
            // Simulate expensive initialization
            try { Thread.sleep(100); } catch (InterruptedException e) { }
        }
        
        @Override
        public String query(String sql) {
            System.out.println("RealDatabase: Executing query on " + name);
            return "Result for: " + sql;
        }
        
        @Override
        public void connect() {
            connected = true;
            System.out.println("RealDatabase: Connected to " + name);
        }
        
        @Override
        public void disconnect() {
            connected = false;
            System.out.println("RealDatabase: Disconnected from " + name);
        }
        
        public boolean isConnected() { return connected; }
    }

    // ========================================
    // Flavor 1: Virtual Proxy (Lazy Loading)
    // ========================================
    static class VirtualProxy implements Database {
        private RealDatabase realDatabase;
        private final String name;
        
        public VirtualProxy(String name) {
            this.name = name;
            System.out.println("VirtualProxy: Created proxy for " + name + " (not connected yet)");
        }
        
        private void initialize() {
            if (realDatabase == null) {
                System.out.println("VirtualProxy: Lazy initializing real database...");
                realDatabase = new RealDatabase(name);
            }
        }
        
        @Override
        public String query(String sql) {
            initialize();
            return realDatabase.query(sql);
        }
        
        @Override
        public void connect() {
            initialize();
            realDatabase.connect();
        }
        
        @Override
        public void disconnect() {
            if (realDatabase != null) {
                realDatabase.disconnect();
            }
        }
    }

    // ========================================
    // Flavor 2: Protection Proxy (Access Control)
    // ========================================
    static class ProtectionProxy implements Database {
        private final RealDatabase realDatabase;
        private final String userRole;
        
        public ProtectionProxy(RealDatabase realDatabase, String userRole) {
            this.realDatabase = realDatabase;
            this.userRole = userRole;
        }
        
        private boolean hasPermission(String operation) {
            if (operation.equals("DROP") || operation.equals("DELETE")) {
                return "admin".equals(userRole);
            }
            return true;
        }
        
        @Override
        public String query(String sql) {
            String operation = sql.trim().split("\\s+")[0].toUpperCase();
            if (!hasPermission(operation)) {
                System.out.println("ProtectionProxy: ACCESS DENIED for " + userRole + " - " + operation);
                return "ACCESS DENIED";
            }
            return realDatabase.query(sql);
        }
        
        @Override
        public void connect() {
            realDatabase.connect();
        }
        
        @Override
        public void disconnect() {
            realDatabase.disconnect();
        }
    }

    // ========================================
    // Flavor 3: Caching Proxy
    // ========================================
    static class CachingProxy implements Database {
        private final RealDatabase realDatabase;
        private final Map<String, String> cache = new HashMap<>();
        
        public CachingProxy(RealDatabase realDatabase) {
            this.realDatabase = realDatabase;
        }
        
        @Override
        public String query(String sql) {
            if (cache.containsKey(sql)) {
                System.out.println("CachingProxy: Cache HIT for: " + sql);
                return cache.get(sql);
            }
            
            System.out.println("CachingProxy: Cache MISS for: " + sql);
            String result = realDatabase.query(sql);
            cache.put(sql, result);
            return result;
        }
        
        @Override
        public void connect() { realDatabase.connect(); }
        
        @Override
        public void disconnect() { realDatabase.disconnect(); }
    }

    // ========================================
    // Flavor 4: Logging Proxy
    // ========================================
    static class LoggingProxy implements Database {
        private final RealDatabase realDatabase;
        
        public LoggingProxy(RealDatabase realDatabase) {
            this.realDatabase = realDatabase;
        }
        
        private void log(String message) {
            System.out.println("[LOG " + java.time.LocalTime.now() + "] " + message);
        }
        
        @Override
        public String query(String sql) {
            log("QUERY START: " + sql);
            long start = System.currentTimeMillis();
            
            String result = realDatabase.query(sql);
            
            long duration = System.currentTimeMillis() - start;
            log("QUERY END: " + sql + " (" + duration + "ms)");
            return result;
        }
        
        @Override
        public void connect() {
            log("CONNECT");
            realDatabase.connect();
            log("CONNECTED");
        }
        
        @Override
        public void disconnect() {
            log("DISCONNECT");
            realDatabase.disconnect();
            log("DISCONNECTED");
        }
    }

    // ========================================
    // Flavor 5: Remote Proxy (Simulated)
    // ========================================
    static class RemoteProxy implements Database {
        private final String remoteHost;
        private final int remotePort;
        
        public RemoteProxy(String host, int port) {
            this.remoteHost = host;
            this.remotePort = port;
            System.out.println("RemoteProxy: Created for " + host + ":" + port);
        }
        
        @Override
        public String query(String sql) {
            System.out.println("RemoteProxy: Sending query to " + remoteHost + ":" + remotePort);
            // Simulate network call
            try { Thread.sleep(50); } catch (InterruptedException e) { }
            return "Remote result from " + remoteHost + " for: " + sql;
        }
        
        @Override
        public void connect() {
            System.out.println("RemoteProxy: Connecting to " + remoteHost + ":" + remotePort);
        }
        
        @Override
        public void disconnect() {
            System.out.println("RemoteProxy: Disconnecting from " + remoteHost);
        }
    }

    // ========================================
    // Main Method
    // ========================================
    public static void main(String[] args) {
        System.out.println("=== Proxy Pattern - All 5 Flavors ===\n");
        
        // Flavor 1: Virtual Proxy
        System.out.println("--- 1. Virtual Proxy (Lazy Loading) ---");
        Database virtualDb = new VirtualProxy("ProductionDB");
        System.out.println("Virtual proxy created (no connection yet)");
        System.out.println("First query triggers connection:");
        virtualDb.query("SELECT * FROM users");
        System.out.println("Second query reuses connection:");
        virtualDb.query("SELECT * FROM orders");
        
        // Flavor 2: Protection Proxy
        System.out.println("\n--- 2. Protection Proxy (Access Control) ---");
        RealDatabase realDb = new RealDatabase("SecureDB");
        Database userProxy = new ProtectionProxy(realDb, "user");
        Database adminProxy = new ProtectionProxy(realDb, "admin");
        
        System.out.println("User tries SELECT:");
        userProxy.query("SELECT * FROM users");
        System.out.println("User tries DELETE:");
        userProxy.query("DELETE FROM users WHERE id=1");
        System.out.println("Admin tries DELETE:");
        adminProxy.query("DELETE FROM users WHERE id=1");
        
        // Flavor 3: Caching Proxy
        System.out.println("\n--- 3. Caching Proxy ---");
        RealDatabase cacheDb = new RealDatabase("CacheDB");
        Database cachingProxy = new CachingProxy(cacheDb);
        
        System.out.println("First query (cache miss):");
        cachingProxy.query("SELECT * FROM products");
        System.out.println("Second query (cache hit):");
        cachingProxy.query("SELECT * FROM products");
        System.out.println("Different query (cache miss):");
        cachingProxy.query("SELECT * FROM orders");
        
        // Flavor 4: Logging Proxy
        System.out.println("\n--- 4. Logging Proxy ---");
        RealDatabase logDb = new RealDatabase("LogDB");
        Database loggingProxy = new LoggingProxy(logDb);
        
        loggingProxy.query("SELECT * FROM users");
        loggingProxy.query("UPDATE users SET name='test'");
        
        // Flavor 5: Remote Proxy
        System.out.println("\n--- 5. Remote Proxy (Simulated) ---");
        Database remoteProxy = new RemoteProxy("api.example.com", 8080);
        remoteProxy.connect();
        remoteProxy.query("GET /api/users");
        remoteProxy.disconnect();
        
        System.out.println("\n=== Summary ===");
        System.out.println("Virtual Proxy:    Lazy initialization of expensive objects");
        System.out.println("Protection Proxy: Access control and permissions");
        System.out.println("Caching Proxy:    Caches results for performance");
        System.out.println("Logging Proxy:    Logs all operations for auditing");
        System.out.println("Remote Proxy:     Represents remote object locally");
    }
}
