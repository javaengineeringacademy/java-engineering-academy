package academy.javaengineering.springboot;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration - application.properties, @ConfigurationProperties.
 */
public class ConfigurationExample {

    public static class AppConfig {
        private final Map<String, String> properties = new HashMap<>();

        public void loadProperties(Map<String, String> props) { properties.putAll(props); }

        public String get(String key) { return properties.get(key); }

        public String get(String key, String defaultValue) {
            return properties.getOrDefault(key, defaultValue);
        }
    }

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        config.loadProperties(Map.of(
                "server.port", "8080",
                "spring.datasource.url", "jdbc:h2:mem:test"
        ));
        System.out.println("Port: " + config.get("server.port"));
        System.out.println("URL: " + config.get("spring.datasource.url"));
    }
}
