package academy.javaengineering.properties;

import java.util.Map;
import java.util.HashMap;

/**
 * Demonstrates environment-based configuration.
 */
public class EnvironmentConfig {

    private final Map<String, String> config = new HashMap<>();

    public EnvironmentConfig() {
        loadFromEnvironment();
    }

    private void loadFromEnvironment() {
        config.put("database.url", System.getenv("DATABASE_URL"));
        config.put("api.key", System.getenv("API_KEY"));
        config.put("server.port", System.getenv("SERVER_PORT"));
    }

    public String get(String key) {
        return config.get(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }

    public Map<String, String> getAll() {
        return new HashMap<>(config);
    }
}
