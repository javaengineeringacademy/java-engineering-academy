package academy.javaengineering.springboot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates Spring Boot auto-configuration patterns and conditional bean registration.
 *
 * <p>This class showcases:
 * <ul>
 *   <li>{@code @Configuration} and {@code @ConfigurationProperties} for type-safe binding</li>
 *   <li>{@code @ConditionalOnClass} to register beans only when a class is on the classpath</li>
 *   <li>{@code @ConditionalOnProperty} to control bean creation via properties</li>
 *   <li>{@code @ConditionalOnMissingBean} to provide default implementations</li>
 *   <li>Custom auto-configuration patterns</li>
 * </ul>
 */
@Configuration
@EnableConfigurationProperties(AutoConfigExample.AutoProperties.class)
public class AutoConfigExample {

    /**
     * Creates a default notification service when no other is defined.
     *
     * @return the default notification service
     */
    @Bean
    @ConditionalOnMissingBean(NotificationService.class)
    public NotificationService defaultNotificationService() {
        return new DefaultNotificationService();
    }

    /**
     * Creates an enhanced notification service when the SMTP class is available.
     *
     * @return the SMTP notification service
     */
    @Bean
    @ConditionalOnClass(name = "com.sun.mail.smtp.SMTPTransport")
    @ConditionalOnProperty(prefix = "app.notification", name = "smtp-enabled", havingValue = "true")
    public NotificationService smtpNotificationService() {
        return new SmtpNotificationService();
    }

    /**
     * Creates a metrics tracker bean when the feature is enabled.
     *
     * @param autoProperties the auto-configuration properties
     * @return the metrics tracker
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.features.metrics", name = "enabled", havingValue = "true",
            matchIfMissing = false)
    public MetricsTracker metricsTracker(AutoProperties autoProperties) {
        return new MetricsTracker(autoProperties.getFeatures().getMetrics().isEnabled());
    }

    /**
     * Configuration properties bound to the "app" prefix.
     */
    @ConfigurationProperties(prefix = "app")
    public static class AutoProperties {

        private String name = "Spring Boot Demo";
        private String description = "Demo application";
        private String version = "1.0.0";
        private String author = "Java Engineering Academy";
        private boolean debug = false;
        private Features features = new Features();

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

        /**
         * Nested properties for feature toggles.
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

    // ========== Service Interfaces and Implementations ==========

    /**
     * Interface for notification services.
     */
    public interface NotificationService {
        String sendNotification(String message);
        String getServiceName();
    }

    /**
     * Default notification service implementation.
     */
    public static class DefaultNotificationService implements NotificationService {

        @Override
        public String sendNotification(String message) {
            return "[DefaultNotification] " + message;
        }

        @Override
        public String getServiceName() {
            return "DefaultNotificationService";
        }
    }

    /**
     * SMTP notification service implementation (conditional on class availability).
     */
    public static class SmtpNotificationService implements NotificationService {

        @Override
        public String sendNotification(String message) {
            return "[SMTPNotification] " + message;
        }

        @Override
        public String getServiceName() {
            return "SmtpNotificationService";
        }
    }

    /**
     * Metrics tracker that tracks application metrics.
     */
    public static class MetricsTracker {

        private final boolean enabled;
        private final Map<String, Long> metrics = new HashMap<>();
        private final List<String> eventLog = new ArrayList<>();

        public MetricsTracker(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void recordMetric(String name, long value) {
            if (enabled) {
                metrics.put(name, value);
                eventLog.add("Recorded " + name + "=" + value);
            }
        }

        public Long getMetric(String name) {
            return metrics.get(name);
        }

        public Map<String, Long> getAllMetrics() {
            return new HashMap<>(metrics);
        }

        public List<String> getEventLog() {
            return new ArrayList<>(eventLog);
        }

        public void reset() {
            metrics.clear();
            eventLog.clear();
        }
    }

    /**
     * Main method demonstrating auto-configuration concepts.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Auto-Configuration Examples ===");
        System.out.println("Key concepts demonstrated:");
        System.out.println("  1. @ConditionalOnClass - beans created only when class is on classpath");
        System.out.println("  2. @ConditionalOnProperty - beans created based on property values");
        System.out.println("  3. @ConditionalOnMissingBean - default beans when none defined");
        System.out.println("  4. @ConfigurationProperties - type-safe property binding");
        System.out.println();

        System.out.println("Notification Service:");
        NotificationService service = new DefaultNotificationService();
        System.out.println("  Service: " + service.getServiceName());
        System.out.println("  Message: " + service.sendNotification("Hello, World!"));
        System.out.println();

        System.out.println("Metrics Tracker:");
        MetricsTracker tracker = new MetricsTracker(true);
        tracker.recordMetric("requests.total", 100);
        tracker.recordMetric("errors.count", 5);
        System.out.println("  Enabled: " + tracker.isEnabled());
        System.out.println("  All Metrics: " + tracker.getAllMetrics());
        System.out.println("  Event Log: " + tracker.getEventLog());
        System.out.println();

        System.out.println("Auto Properties:");
        AutoProperties props = new AutoProperties();
        System.out.println("  Name: " + props.getName());
        System.out.println("  Version: " + props.getVersion());
        System.out.println("  Debug: " + props.isDebug());
        System.out.println("  Logging Enabled: " + props.getFeatures().getLogging().isEnabled());
        System.out.println("  Metrics Enabled: " + props.getFeatures().getMetrics().isEnabled());
        System.out.println("  Cache Enabled: " + props.getFeatures().getCache().isEnabled());
    }
}
