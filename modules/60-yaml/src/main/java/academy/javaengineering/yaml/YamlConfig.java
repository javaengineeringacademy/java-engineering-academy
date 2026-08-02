package academy.javaengineering.yaml;

/**
 * Demonstrates YAML configuration examples.
 */
public class YamlConfig {

    public record ServerConfig(
        int port,
        String contextPath,
        int maxThreads
    ) {}

    public record DatabaseConfig(
        String url,
        String username,
        String password,
        int poolSize
    ) {}

    public record AppConfig(
        ServerConfig server,
        DatabaseConfig database,
        java.util.Map<String, String> properties
    ) {}

    public static String generateYaml() {
        return """
            server:
              port: 8080
              context-path: /api
              max-threads: 200
            database:
              url: jdbc:postgresql://localhost:5432/mydb
              username: admin
              password: secret
              pool-size: 10
            properties:
              app.name: MyApplication
              app.version: 1.0.0
            """;
    }
}
