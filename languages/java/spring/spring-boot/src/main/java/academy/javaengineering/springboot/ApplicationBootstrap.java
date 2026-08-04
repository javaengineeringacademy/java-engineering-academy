package academy.javaengineering.springboot;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates Spring Boot application bootstrap, profiles, runners, and context lifecycle.
 *
 * <p>This class serves as the main entry point for the Spring Boot module and demonstrates:
 * <ul>
 *   <li>{@code @SpringBootApplication} annotation and auto-configuration</li>
 *   <li>{@code SpringApplication.run()} and application context lifecycle</li>
 *   <li>Profile activation via {@code spring.profiles.active}</li>
 *   <li>{@code CommandLineRunner} and {@code ApplicationRunner} for startup logic</li>
 *   <li>Customizing the SpringApplication before running</li>
 * </ul>
 */
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = "academy.javaengineering.springboot")
public class ApplicationBootstrap {

    /**
     * Main entry point for the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApplicationBootstrap.class);

        // Customize the application before running
        app.setBannerMode(org.springframework.boot.Banner.Mode.CONSOLE);
        app.setLogStartupInfo(true);

        // Add custom properties via command-line arguments
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put("app.version", "1.0.0");
        app.setDefaultProperties(defaultProperties);

        // Set active profiles from command-line if provided
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith("--spring.profiles.active=")) {
                    String profile = arg.substring("--spring.profiles.active=".length());
                    app.setAdditionalProfiles(profile);
                }
            }
        }

        ConfigurableApplicationContext context = app.run(args);

        // Log application startup information
        logStartupInfo(context);
    }

    /**
     * Logs useful startup information from the application context.
     *
     * @param context the application context
     */
    private static void logStartupInfo(ConfigurableApplicationContext context) {
        Environment env = context.getEnvironment();
        String appName = env.getProperty("spring.application.name", "Unknown");
        String port = env.getProperty("server.port", "8080");
        String[] activeProfiles = env.getActiveProfiles();

        System.out.println("========================================");
        System.out.println("Application Started Successfully!");
        System.out.println("Name: " + appName);
        System.out.println("Port: " + port);
        System.out.println("Active Profiles: " + Arrays.toString(activeProfiles));
        System.out.println("========================================");
    }

    /**
     * Demonstrates adding property sources programmatically.
     *
     * @param context the application context
     */
    public static void addCustomProperties(ConfigurableApplicationContext context) {
        ConfigurableEnvironment env = context.getEnvironment();
        Map<String, Object> customProps = new HashMap<>();
        customProps.put("custom.dynamic.property", "dynamicValue");
        customProps.put("custom.timestamp", String.valueOf(System.currentTimeMillis()));

        MapPropertySource propertySource = new MapPropertySource("customDynamicProperties", customProps);
        env.getPropertySources().addFirst(propertySource);
    }

    /**
     * Demonstrates retrieving beans from the context after startup.
     *
     * @param context the application context
     * @param clazz   the bean type to retrieve
     * @param <T>     the bean type
     * @return the bean instance
     */
    public static <T> T getBean(ConfigurableApplicationContext context, Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * Demonstrates listing all active profiles.
     *
     * @param context the application context
     * @return array of active profile names
     */
    public static String[] getActiveProfiles(ConfigurableApplicationContext context) {
        return context.getEnvironment().getActiveProfiles();
    }

    /**
     * Demonstrates checking if a specific profile is active.
     *
     * @param context     the application context
     * @param profileName the profile name to check
     * @return true if the profile is active
     */
    public static boolean isProfileActive(ConfigurableApplicationContext context, String profileName) {
        return Arrays.asList(context.getEnvironment().getActiveProfiles()).contains(profileName);
    }

    /**
     * Demonstrates shutting down the application context gracefully.
     *
     * @param context the application context to close
     */
    public static void shutdownGracefully(ConfigurableApplicationContext context) {
        if (context != null && context.isActive()) {
            context.close();
            System.out.println("Application context closed gracefully.");
        }
    }

    /**
     * CommandLineRunner that executes after the application context is loaded.
     * Demonstrates ordered execution with {@code @Order}.
     */
    @Component
    public static class StartupCommandLineRunner implements CommandLineRunner {

        @Override
        public void run(String... args) {
            System.out.println("[CommandLineRunner] Startup runner executed with args: "
                    + Arrays.toString(args));
        }
    }

    /**
     * ApplicationRunner that provides parsed application arguments.
     * Demonstrates the difference between CommandLineRunner and ApplicationRunner.
     */
    @Component
    public static class StartupApplicationRunner implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) {
            System.out.println("[ApplicationRunner] Parsed arguments: " + args.getNonOptionArgs());
            System.out.println("[ApplicationRunner] Option keys: " + args.getOptionNames());
        }
    }
}
