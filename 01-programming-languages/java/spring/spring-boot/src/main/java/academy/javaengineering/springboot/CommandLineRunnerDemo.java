package academy.javaengineering.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates multiple {@code CommandLineRunner} beans with ordered execution and conditional startup.
 *
 * <p>This class showcases:
 * <ul>
 *   <li>Multiple {@code CommandLineRunner} implementations</li>
 *   <li>Ordered execution using {@code @Order} annotation</li>
 *   <li>Conditional startup logic based on arguments</li>
 *   <li>Initialization and cleanup patterns</li>
 *   <li>Differences between {@code CommandLineRunner} and {@code ApplicationRunner}</li>
 * </ul>
 *
 * <p>Execution order follows the {@code @Order} annotation value:
 * <ol>
 *   <li>{@code DatabaseInitializer} (order = 1) - Database schema setup</li>
 *   <li>{@code CacheWarmer} (order = 2) - Cache warming</li>
 *   <li>{@code HealthChecker} (order = 3) - Health verification</li>
 *   <li>{@code StartupReporter} (order = 4) - Final report</li>
 * </ol>
 */
@Configuration
public class CommandLineRunnerDemo {

    /**
     * Tracks the order of runner execution for verification.
     */
    private static final List<String> EXECUTION_LOG = new ArrayList<>();

    /**
     * Gets the execution log showing runner execution order.
     *
     * @return unmodifiable list of execution entries
     */
    public static List<String> getExecutionLog() {
        return List.copyOf(EXECUTION_LOG);
    }

    /**
     * Clears the execution log (for testing).
     */
    public static void clearExecutionLog() {
        EXECUTION_LOG.clear();
    }

    /**
     * First runner to execute: Database initialization.
     * Sets up database schema and seed data.
     */
    @Component
    @Order(1)
    public static class DatabaseInitializer implements CommandLineRunner {

        @Override
        public void run(String... args) {
            EXECUTION_LOG.add("DatabaseInitializer:run");
            System.out.println("[Order 1] Initializing database schema...");
            System.out.println("[Order 1] Creating tables: users, roles, permissions");
            System.out.println("[Order 1] Seeding default data...");
            System.out.println("[Order 1] Database initialization complete.");
        }
    }

    /**
     * Second runner to execute: Cache warming.
     * Pre-loads frequently accessed data into cache.
     */
    @Component
    @Order(2)
    public static class CacheWarmer implements CommandLineRunner {

        @Override
        public void run(String... args) {
            EXECUTION_LOG.add("CacheWarmer:run");
            System.out.println("[Order 2] Warming application cache...");
            System.out.println("[Order 2] Loading user sessions...");
            System.out.println("[Order 2] Loading configuration data...");
            System.out.println("[Order 2] Cache warming complete.");
        }
    }

    /**
     * Third runner to execute: Health verification.
     * Verifies all dependent services are available.
     */
    @Component
    @Order(3)
    public static class HealthChecker implements CommandLineRunner {

        @Override
        public void run(String... args) {
            EXECUTION_LOG.add("HealthChecker:run");
            System.out.println("[Order 3] Running health checks...");
            System.out.println("[Order 3] Checking database connection... OK");
            System.out.println("[Order 3] Checking cache connection... OK");
            System.out.println("[Order 3] Checking external services... OK");
            System.out.println("[Order 3] All health checks passed.");
        }
    }

    /**
     * Final runner to execute: Startup report.
     * Generates and displays the startup summary.
     */
    @Component
    @Order(4)
    public static class StartupReporter implements CommandLineRunner {

        @Override
        public void run(String... args) {
            EXECUTION_LOG.add("StartupReporter:run");
            System.out.println("[Order 4] === Startup Report ===");
            System.out.println("[Order 4] Application started successfully!");
            System.out.println("[Order 4] Arguments received: " + Arrays.toString(args));
            System.out.println("[Order 4] Execution order:");
            EXECUTION_LOG.forEach(entry -> System.out.println("[Order 4]   - " + entry));
            System.out.println("[Order 4] ========================");
        }
    }

    /**
     * Conditional runner: Only executes when --enable-monitoring is provided.
     */
    @Component
    @Order(5)
    public static class MonitoringStartupRunner implements CommandLineRunner {

        @Override
        public void run(String... args) {
            boolean monitoringEnabled = Arrays.asList(args).contains("--enable-monitoring");
            if (monitoringEnabled) {
                EXECUTION_LOG.add("MonitoringStartupRunner:run");
                System.out.println("[Order 5] Starting monitoring agent...");
                System.out.println("[Order 5] Registering JMX beans...");
                System.out.println("[Order 5] Monitoring agent started.");
            }
        }
    }

    /**
     * Demonstrates conditional runner that checks for a specific profile.
     */
    @Component
    @Order(6)
    public static class DevToolsInitializer implements CommandLineRunner {

        @Override
        public void run(String... args) {
            boolean devMode = Arrays.asList(args).contains("--dev-mode");
            if (devMode) {
                EXECUTION_LOG.add("DevToolsInitializer:run");
                System.out.println("[Order 6] Dev mode detected - enabling hot reload...");
                System.out.println("[Order 6] Setting up live reload...");
                System.out.println("[Order 6] Dev tools initialized.");
            }
        }
    }

    /**
     * Demonstrates an ordered application runner (alternative to CommandLineRunner).
     */
    @Component
    @Order(7)
    public static class ApplicationStartupRunner
            implements org.springframework.boot.ApplicationRunner {

        @Override
        public void run(org.springframework.boot.ApplicationArguments args) {
            EXECUTION_LOG.add("ApplicationStartupRunner:run");
            System.out.println("[Order 7] ApplicationRunner executed.");
            System.out.println("[Order 7] Non-option args: " + args.getNonOptionArgs());
            System.out.println("[Order 7] Option values for 'config': "
                    + args.getOptionValues("config"));
        }
    }

    /**
     * Main method demonstrating CommandLineRunner concepts.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== CommandLineRunner Demo ===");
        System.out.println();
        System.out.println("This demo shows multiple CommandLineRunner beans executing in order.");
        System.out.println();
        System.out.println("Key concepts:");
        System.out.println("  1. @Order annotation controls execution sequence");
        System.out.println("  2. Multiple runners can be defined as @Component or @Bean");
        System.out.println("  3. Runners execute after the application context is loaded");
        System.out.println("  4. ApplicationRunner provides parsed arguments");
        System.out.println();
        System.out.println("Execution order:");
        System.out.println("  1. DatabaseInitializer - schema setup");
        System.out.println("  2. CacheWarmer - pre-load cache");
        System.out.println("  3. HealthChecker - verify dependencies");
        System.out.println("  4. StartupReporter - summary report");
        System.out.println("  5. MonitoringStartupRunner - conditional (needs --enable-monitoring)");
        System.out.println("  6. DevToolsInitializer - conditional (needs --dev-mode)");
        System.out.println("  7. ApplicationStartupRunner - ApplicationRunner example");
    }
}
