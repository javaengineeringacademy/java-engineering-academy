package academy.javaengineering.patterns.creational;

import java.util.ArrayList;
import java.util.List;

public class SingletonExample {

    private static class DatabaseConnectionPool {
        private static DatabaseConnectionPool instance;
        private final List<String> connections;

        private DatabaseConnectionPool() {
            connections = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                connections.add("Connection-" + (i + 1));
            }
        }

        public static DatabaseConnectionPool getInstance() {
            if (instance == null) {
                synchronized (DatabaseConnectionPool.class) {
                    if (instance == null) {
                        instance = new DatabaseConnectionPool();
                    }
                }
            }
            return instance;
        }

        public String acquireConnection() {
            if (connections.isEmpty()) {
                throw new RuntimeException("No connections available");
            }
            return connections.remove(0);
        }

        public void releaseConnection(String conn) {
            connections.add(conn);
        }

        public int availableConnections() {
            return connections.size();
        }
    }

    private static class ConfigurationManager {
        private static ConfigurationManager instance;
        private final java.util.Map<String, String> properties;

        private ConfigurationManager() {
            properties = new java.util.HashMap<>();
            properties.put("db.url", "jdbc:mysql://localhost:3306/mydb");
            properties.put("db.username", "admin");
            properties.put("app.name", "MyApplication");
        }

        public static ConfigurationManager getInstance() {
            if (instance == null) {
                synchronized (ConfigurationManager.class) {
                    if (instance == null) {
                        instance = new ConfigurationManager();
                    }
                }
            }
            return instance;
        }

        public String getProperty(String key) {
            return properties.getOrDefault(key, "");
        }

        public void setProperty(String key, String value) {
            properties.put(key, value);
        }
    }

    public static void main(String[] args) {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        String conn = pool.acquireConnection();
        System.out.println("Acquired: " + conn);
        System.out.println("Available: " + pool.availableConnections());

        pool.releaseConnection(conn);
        System.out.println("After release: " + pool.availableConnections());

        ConfigurationManager config = ConfigurationManager.getInstance();
        System.out.println("DB URL: " + config.getProperty("db.url"));
        System.out.println("App Name: " + config.getProperty("app.name"));
    }
}
