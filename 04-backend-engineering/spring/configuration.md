# Spring Configuration

## application.properties

### Basic Configuration

```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.file.name=app.log
```

### Profile-Specific Properties

```
application.properties          # Common properties
application-dev.properties      # Development
application-prod.properties     # Production
application-test.properties     # Testing
```

```properties
# application-dev.properties
spring.datasource.url=jdbc:h2:mem:devdb
spring.jpa.show-sql=true
logging.level.com.example=DEBUG
```

## YAML Configuration

```yaml
# application.yml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

logging:
  level:
    root: INFO
    com.example: DEBUG
```

## @Configuration

```java
@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        config.setUsername("root");
        config.setPassword("password");
        return new HikariDataSource(config);
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

## @Value

```java
@Component
public class AppConfig {
    @Value("${app.name}")
    private String appName;
    
    @Value("${app.port:8080}")
    private int port;
    
    @Value("${app.features:feature1,feature2}")
    private List<String> features;
    
    @Value("${app.enabled:true}")
    private boolean enabled;
}
```

## @ConfigurationProperties

```java
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private int port = 8080;
    private boolean enabled = true;
    private List<String> features = new ArrayList<>();
    private Database database = new Database();
    
    @Data
    public static class Database {
        private String url;
        private String username;
        private String password;
    }
}
```

```properties
# application.properties
app.name=MyApp
app.port=8080
app.enabled=true
app.features=feature1,feature2
app.database.url=jdbc:mysql://localhost:3306/mydb
app.database.username=root
app.database.password=password
```

## @PropertySource

```java
@Configuration
@PropertySource("classpath:custom.properties")
@PropertySource("classpath:override.properties")
public class CustomPropertyConfig {
    @Value("${custom.property}")
    private String customProperty;
}
```

## @EnableConfigurationProperties

```java
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig {
    @Bean
    public AppService appService(AppProperties properties) {
        return new AppService(properties);
    }
}
```

## Nested Configuration

```java
@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private Cache cache = new Cache();
    private Security security = new Security();
    
    @Data
    public static class Cache {
        private int ttl = 3600;
        private int maxSize = 1000;
    }
    
    @Data
    public static class Security {
        private boolean enabled = true;
        private String secret = "default-secret";
    }
}
```

```properties
app.cache.ttl=7200
app.cache.max-size=5000
app.security.enabled=true
app.security.secret=my-secret-key
```

## Custom Converters

```java
@Component
public class DurationConverter implements Converter<String, Duration> {
    @Override
    public Duration convert(String source) {
        return Duration.parse(source);
    }
}

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration timeout = Duration.ofMinutes(30);
}
```

## Random Values

```properties
# Random int
app.port=${random.int}

# Random long
app.id=${random.long}

# Random UUID
app.uuid=${random.uuid}

# Random value in range
app.port=${random.int[1024,65536]}

# Random string
app.secret=${random.value}
```

## Placeholder Resolution

```properties
# Default values
app.port=${MY_PORT:8080}
app.url=${APP_URL:http://localhost}

# Reference other properties
app.name=MyApp
app.description=${app.name} Application
```

## Multi-Document YAML

```yaml
---
spring:
  config:
    activate:
      on-profile: dev
server:
  port: 8081

---
spring:
  config:
    activate:
      on-profile: prod
server:
  port: 80
```

## Configuration Metadata

```json
// META-INF/spring-configuration-metadata.json
{
  "properties": [
    {
      "name": "app.name",
      "type": "java.lang.String",
      "description": "Application name"
    },
    {
      "name": "app.port",
      "type": "java.lang.Integer",
      "defaultValue": 8080,
      "description": "Server port"
    }
  ]
}
```

## Configuration Validation

```java
@Data
@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    @NotNull
    private String name;
    
    @Min(1024)
    @Max(65535)
    private int port = 8080;
    
    @NotBlank
    private String secret;
}
```

## Environment Variables

```properties
# Reference environment variables
app.db.password=${DB_PASSWORD}
app.api.key=${API_KEY}

# With defaults
app.port=${SERVER_PORT:8080}
```

## Command Line Arguments

```bash
# Override properties via CLI
java -jar app.jar --server.port=9090 --app.name=MyApp

# Multiple arguments
java -jar app.jar --spring.profiles.active=prod --server.port=80
```

## Configuration Best Practices

1. Use `@ConfigurationProperties` for type-safe configuration
2. Use profile-specific properties for environment differences
3. Validate configuration with `@Validated`
4. Use nested properties for complex configuration
5. Provide sensible defaults
6. Use environment variables for secrets
7. Document properties with metadata
8. Use `@ConditionalOnProperty` for feature toggles
9. Avoid hardcoding values
10. Use configuration groups for related properties
