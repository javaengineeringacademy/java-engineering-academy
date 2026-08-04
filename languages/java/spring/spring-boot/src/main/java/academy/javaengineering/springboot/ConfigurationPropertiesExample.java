package academy.javaengineering.springboot;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates Spring Boot {@code @ConfigurationProperties} for type-safe externalized configuration.
 *
 * <p>This class showcases:
 * <ul>
 *   <li>{@code @ConfigurationProperties} for binding properties to POJOs</li>
 *   <li>Nested properties and complex types</li>
 *   <li>Property validation with Jakarta Validation annotations</li>
 *   <li>{@code @EnableConfigurationProperties} to activate binding</li>
 *   <li>Default values and relaxed binding</li>
 *   <li>Collection and map binding</li>
 * </ul>
 *
 * <p>Properties are bound from the following sources:
 * <ul>
 *   <li>{@code application.properties} / {@code application.yml}</li>
 *   <li>Command-line arguments</li>
 *   <li>Environment variables</li>
 *   <li>JVM system properties</li>
 * </ul>
 */
@Configuration
@EnableConfigurationProperties({
        ConfigurationPropertiesExample.AppProperties.class,
        ConfigurationPropertiesExample.ServerProperties.class,
        ConfigurationPropertiesExample.MailProperties.class
})
public class ConfigurationPropertiesExample {

    /**
     * Main application configuration properties bound to the "app" prefix.
     *
     * <p>Example configuration:
     * <pre>
     * app.name=My Application
     * app.version=2.0.0
     * app.features.logging.enabled=true
     * </pre>
     */
    @ConfigurationProperties(prefix = "app")
    @Validated
    public static class AppProperties {

        @NotBlank(message = "Application name is required")
        private String name = "Spring Boot Demo";

        private String description = "Demo application";
        private String version = "1.0.0";
        private String author = "Java Engineering Academy";
        private boolean debug = false;
        private Features features = new Features();
        private List<String> tags = new ArrayList<>();
        private Map<String, String> metadata = new HashMap<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public boolean isDebug() {
            return debug;
        }

        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public Features getFeatures() {
            return features;
        }

        public void setFeatures(Features features) {
            this.features = features;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public Map<String, String> getMetadata() {
            return metadata;
        }

        public void setMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
        }

        /**
         * Nested feature toggle configuration.
         */
        public static class Features {

            private Logging logging = new Logging();
            private Metrics metrics = new Metrics();
            private Cache cache = new Cache();

            public Logging getLogging() {
                return logging;
            }

            public void setLogging(Logging logging) {
                this.logging = logging;
            }

            public Metrics getMetrics() {
                return metrics;
            }

            public void setMetrics(Metrics metrics) {
                this.metrics = metrics;
            }

            public Cache getCache() {
                return cache;
            }

            public void setCache(Cache cache) {
                this.cache = cache;
            }

            public static class Logging {
                private boolean enabled = true;

                public boolean isEnabled() {
                    return enabled;
                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                }
            }

            public static class Metrics {
                private boolean enabled = true;

                public boolean isEnabled() {
                    return enabled;
                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                }
            }

            public static class Cache {
                private boolean enabled = false;

                public boolean isEnabled() {
                    return enabled;
                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                }
            }
        }
    }

    /**
     * Server configuration properties bound to the "server" prefix.
     *
     * <p>Example configuration:
     * <pre>
     * server.port=8080
     * server.servlet.context-path=/api
     * </pre>
     */
    @ConfigurationProperties(prefix = "server")
    @Validated
    public static class ServerProperties {

        @Min(value = 1024, message = "Port must be at least 1024")
        @Max(value = 65535, message = "Port must be at most 65535")
        private int port = 8080;

        private String contextPath = "/api";
        private int connectionTimeout = 30000;
        private int readTimeout = 60000;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getContextPath() {
            return contextPath;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }
    }

    /**
     * Mail configuration properties bound to the "app.mail" prefix.
     *
     * <p>Example configuration:
     * <pre>
     * app.mail.host=smtp.example.com
     * app.mail.port=587
     * app.mail.username=user@example.com
     * </pre>
     */
    @ConfigurationProperties(prefix = "app.mail")
    @Validated
    public static class MailProperties {

        @NotBlank(message = "Mail host is required")
        private String host = "smtp.example.com";

        @Min(value = 1, message = "Mail port must be positive")
        @Max(value = 65535, message = "Mail port must be at most 65535")
        private int port = 587;

        @NotBlank(message = "Mail username is required")
        private String username = "user@example.com";

        private String password = "";
        private boolean ssl = true;
        private boolean auth = true;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
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

        public boolean isSsl() {
            return ssl;
        }

        public void setSsl(boolean ssl) {
            this.ssl = ssl;
        }

        public boolean isAuth() {
            return auth;
        }

        public void setAuth(boolean auth) {
            this.auth = auth;
        }
    }

    /**
     * Main method demonstrating ConfigurationProperties concepts.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Configuration Properties Examples ===");
        System.out.println();

        System.out.println("--- App Properties ---");
        AppProperties appProps = new AppProperties();
        appProps.setName("Spring Boot Demo");
        appProps.setVersion("2.0.0");
        appProps.setDebug(true);
        appProps.setTags(List.of("spring", "boot", "demo"));
        appProps.getMetadata().put("build", "Maven");
        appProps.getMetadata().put("java", "21");
        System.out.println("  Name: " + appProps.getName());
        System.out.println("  Version: " + appProps.getVersion());
        System.out.println("  Debug: " + appProps.isDebug());
        System.out.println("  Tags: " + appProps.getTags());
        System.out.println("  Metadata: " + appProps.getMetadata());
        System.out.println("  Logging Enabled: " + appProps.getFeatures().getLogging().isEnabled());
        System.out.println("  Metrics Enabled: " + appProps.getFeatures().getMetrics().isEnabled());
        System.out.println();

        System.out.println("--- Server Properties ---");
        ServerProperties serverProps = new ServerProperties();
        serverProps.setPort(9090);
        serverProps.setContextPath("/api/v2");
        serverProps.setConnectionTimeout(5000);
        System.out.println("  Port: " + serverProps.getPort());
        System.out.println("  Context Path: " + serverProps.getContextPath());
        System.out.println("  Connection Timeout: " + serverProps.getConnectionTimeout() + "ms");
        System.out.println("  Read Timeout: " + serverProps.getReadTimeout() + "ms");
        System.out.println();

        System.out.println("--- Mail Properties ---");
        MailProperties mailProps = new MailProperties();
        mailProps.setHost("smtp.gmail.com");
        mailProps.setPort(587);
        mailProps.setUsername("demo@gmail.com");
        mailProps.setPassword("****");
        System.out.println("  Host: " + mailProps.getHost());
        System.out.println("  Port: " + mailProps.getPort());
        System.out.println("  Username: " + mailProps.getUsername());
        System.out.println("  SSL: " + mailProps.isSsl());
        System.out.println("  Auth: " + mailProps.isAuth());
        System.out.println();

        System.out.println("--- Validation Rules ---");
        System.out.println("  @NotBlank - AppProperties.name");
        System.out.println("  @Min(1024) @Max(65535) - ServerProperties.port");
        System.out.println("  @NotBlank - MailProperties.host");
        System.out.println("  @Min(1) @Max(65535) - MailProperties.port");
        System.out.println();

        System.out.println("--- Property Sources (in priority order) ---");
        System.out.println("  1. Command-line arguments");
        System.out.println("  2. Java System properties");
        System.out.println("  3. OS environment variables");
        System.out.println("  4. application-{profile}.properties");
        System.out.println("  5. application.properties");
        System.out.println("  6. @PropertySource annotations");
    }
}
