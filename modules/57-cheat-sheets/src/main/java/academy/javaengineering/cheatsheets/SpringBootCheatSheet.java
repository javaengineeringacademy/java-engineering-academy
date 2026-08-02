package academy.javaengineering.cheatsheets;

/**
 * Demonstrates Spring Boot cheat sheets.
 */
public class SpringBootCheatSheet {

    public static java.util.Map<String, String> getAnnotationsQuickReference() {
        return java.util.Map.of(
            "@SpringBootApplication", "Main application class",
            "@RestController", "REST API controller",
            "@Service", "Service layer component",
            "@Repository", "Data access layer",
            "@Configuration", "Configuration class",
            "@Bean", "Bean definition",
            "@Autowired", "Dependency injection",
            "@Value", "Property injection"
        );
    }

    public static java.util.Map<String, String> getPropertiesQuickReference() {
        return java.util.Map.of(
            "server.port", "HTTP port",
            "spring.datasource.url", "Database URL",
            "spring.jpa.hibernate.ddl-auto", "DDL strategy",
            "logging.level.root", "Logging level",
            "spring.profiles.active", "Active profiles"
        );
    }
}
