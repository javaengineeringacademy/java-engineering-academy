package academy.javaengineering.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Demonstrates Spring profile-specific bean registration and configuration.
 *
 * <p>This class showcases:
 * <ul>
 *   <li>{@code @Profile} annotation for conditional bean creation based on active profiles</li>
 *   <li>Profile-specific bean definitions (dev, prod, test)</li>
 *   <li>Profile-specific configuration properties</li>
 *   <li>How to activate profiles via {@code spring.profiles.active}</li>
 * </ul>
 *
 * <p>Profiles can be activated in multiple ways:
 * <ul>
 *   <li>Command line: {@code --spring.profiles.active=dev}</li>
 *   <li>Properties file: {@code spring.profiles.active=dev}</li>
 *   <li>Programmatically: {@code SpringApplication.setAdditionalProfiles("dev")}</li>
 *   <li>Test annotation: {@code @ActiveProfiles("dev")}</li>
 * </ul>
 */
@Configuration
public class ProfileExample {

    // ========== Database Configuration ==========

    /**
     * In-memory database configuration for development profile.
     *
     * @return database configuration map
     */
    @Bean
    @Profile("dev")
    public DatabaseConfig devDatabaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        config.setType("H2");
        config.setUrl("jdbc:h2:mem:devdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setPooling(true);
        config.setMaxPoolSize(5);
        return config;
    }

    /**
     * PostgreSQL configuration for production profile.
     *
     * @return database configuration map
     */
    @Bean
    @Profile("prod")
    public DatabaseConfig prodDatabaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        config.setType("PostgreSQL");
        config.setUrl("jdbc:postgresql://localhost:5432/proddb");
        config.setUsername("prod_user");
        config.setPassword("${DB_PASSWORD}");
        config.setPooling(true);
        config.setMaxPoolSize(20);
        return config;
    }

    /**
     * H2 database configuration for test profile.
     *
     * @return database configuration map
     */
    @Bean
    @Profile("test")
    public DatabaseConfig testDatabaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        config.setType("H2");
        config.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        config.setPooling(false);
        config.setMaxPoolSize(1);
        return config;
    }

    // ========== Service Configuration ==========

    /**
     * Cache service for development (no caching).
     *
     * @return cache service
     */
    @Bean
    @Profile("dev")
    public CacheService devCacheService() {
        return new NoOpCacheService();
    }

    /**
     * Cache service for production (Redis-backed).
     *
     * @return cache service
     */
    @Bean
    @Profile("prod")
    public CacheService prodCacheService() {
        return new RedisCacheService();
    }

    /**
     * Email service for development (console logging).
     *
     * @return email service
     */
    @Bean
    @Profile("dev")
    public EmailService devEmailService() {
        return new ConsoleEmailService();
    }

    /**
     * Email service for production (SMTP).
     *
     * @return email service
     */
    @Bean
    @Profile("prod")
    public EmailService prodEmailService() {
        return new SmtpEmailService();
    }

    // ========== Inner Classes ==========

    /**
     * Database configuration holder.
     */
    public static class DatabaseConfig {
        private String type;
        private String url;
        private String username;
        private String password;
        private boolean pooling;
        private int maxPoolSize;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isPooling() {
            return pooling;
        }

        public void setPooling(boolean pooling) {
            this.pooling = pooling;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", type);
            map.put("url", url);
            map.put("username", username);
            map.put("pooling", pooling);
            map.put("maxPoolSize", maxPoolSize);
            return map;
        }
    }

    /**
     * Cache service interface.
     */
    public interface CacheService {
        String get(String key);
        void put(String key, String value);
        String getCacheType();
    }

    /**
     * No-operation cache service for development.
     */
    public static class NoOpCacheService implements CacheService {

        private final Map<String, String> store = new LinkedHashMap<>();

        @Override
        public String get(String key) {
            return store.get(key);
        }

        @Override
        public void put(String key, String value) {
            store.put(key, value);
        }

        @Override
        public String getCacheType() {
            return "NoOp (Dev)";
        }
    }

    /**
     * Redis-backed cache service for production.
     */
    public static class RedisCacheService implements CacheService {

        private final Map<String, String> store = new LinkedHashMap<>();

        @Override
        public String get(String key) {
            return store.get(key);
        }

        @Override
        public void put(String key, String value) {
            store.put(key, value);
        }

        @Override
        public String getCacheType() {
            return "Redis (Prod)";
        }
    }

    /**
     * Email service interface.
     */
    public interface EmailService {
        String sendEmail(String to, String subject, String body);
        String getServiceType();
    }

    /**
     * Console-based email service for development.
     */
    public static class ConsoleEmailService implements EmailService {

        @Override
        public String sendEmail(String to, String subject, String body) {
            return "[ConsoleEmail] To: " + to + ", Subject: " + subject;
        }

        @Override
        public String getServiceType() {
            return "Console (Dev)";
        }
    }

    /**
     * SMTP-based email service for production.
     */
    public static class SmtpEmailService implements EmailService {

        @Override
        public String sendEmail(String to, String subject, String body) {
            return "[SmtpEmail] To: " + to + ", Subject: " + subject;
        }

        @Override
        public String getServiceType() {
            return "SMTP (Prod)";
        }
    }

    /**
     * Main method demonstrating profile-specific configuration.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Profile Configuration Examples ===");
        System.out.println("Key concepts demonstrated:");
        System.out.println("  1. @Profile(\"dev\") - beans only active in dev profile");
        System.out.println("  2. @Profile(\"prod\") - beans only active in prod profile");
        System.out.println("  3. @Profile(\"test\") - beans only active in test profile");
        System.out.println();

        System.out.println("Available profiles: dev, prod, test");
        System.out.println("Activate via: --spring.profiles.active=dev");
        System.out.println();

        System.out.println("Dev Database Config:");
        DatabaseConfig devConfig = new DatabaseConfig();
        devConfig.setType("H2");
        devConfig.setUrl("jdbc:h2:mem:devdb");
        devConfig.setUsername("sa");
        devConfig.setPooling(true);
        devConfig.setMaxPoolSize(5);
        System.out.println("  " + devConfig.toMap());
        System.out.println();

        System.out.println("Prod Database Config:");
        DatabaseConfig prodConfig = new DatabaseConfig();
        prodConfig.setType("PostgreSQL");
        prodConfig.setUrl("jdbc:postgresql://localhost:5432/proddb");
        prodConfig.setUsername("prod_user");
        prodConfig.setPooling(true);
        prodConfig.setMaxPoolSize(20);
        System.out.println("  " + prodConfig.toMap());
        System.out.println();

        System.out.println("Cache Services:");
        CacheService devCache = new NoOpCacheService();
        CacheService prodCache = new RedisCacheService();
        devCache.put("key1", "value1");
        prodCache.put("key1", "value1");
        System.out.println("  Dev Cache [" + devCache.getCacheType() + "]: " + devCache.get("key1"));
        System.out.println("  Prod Cache [" + prodCache.getCacheType() + "]: " + prodCache.get("key1"));
        System.out.println();

        System.out.println("Email Services:");
        EmailService devEmail = new ConsoleEmailService();
        EmailService prodEmail = new SmtpEmailService();
        System.out.println("  Dev Email [" + devEmail.getServiceType() + "]: "
                + devEmail.sendEmail("user@test.com", "Hello", "Body"));
        System.out.println("  Prod Email [" + prodEmail.getServiceType() + "]: "
                + prodEmail.sendEmail("user@test.com", "Hello", "Body"));
    }
}
