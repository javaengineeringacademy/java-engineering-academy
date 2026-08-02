package academy.javaengineering.springboot;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot Fundamentals - Auto-configuration, Starters.
 */
public class SpringBootFundamentalsExample {

    public static class AutoConfiguration {
        private final Map<String, Object> properties = new HashMap<>();

        public void configure(String key, Object value) { properties.put(key, value); }

        @SuppressWarnings("unchecked")
        public <T> T getProperty(String key, Class<T> type) {
            Object value = properties.get(key);
            return type.isInstance(value) ? (T) value : null;
        }
    }

    public static class Starter {
        private final String name;
        private final AutoConfiguration config;

        public Starter(String name, AutoConfiguration config) {
            this.name = name;
            this.config = config;
        }

        public void initialize() {
            config.configure("starter." + name, true);
        }

        public boolean isInitialized() {
            return Boolean.TRUE.equals(config.getProperty("starter." + name, Boolean.class));
        }
    }

    public static void main(String[] args) {
        AutoConfiguration config = new AutoConfiguration();
        Starter webStarter = new Starter("web", config);
        webStarter.initialize();
        System.out.println("Web starter initialized: " + webStarter.isInitialized());
    }
}
