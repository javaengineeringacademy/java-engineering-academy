# Spring Profiles

## Comprehensive Guide to Environment-Specific Configuration

Spring Profiles provide a way to configure different environments. This guide covers profile activation, configuration, and best practices.

---

## Table of Contents

1. [Profile Basics](#profile-basics)
2. [Activation Methods](#activation-methods)
3. [Profile Configuration](#profile-configuration)
4. [Conditional Beans](#conditional-beans)
5. [Best Practices](#best-practices)

---

## Profile Basics

### Profile-Specific Configuration

```yaml
# application.yml (default)
spring:
  profiles:
    active: dev

app:
  name: MyApp

---
# application-dev.yml
spring:
  config:
    activate:
      on-profile: dev

server:
  port: 8080

logging:
  level:
    root: DEBUG

---
# application-prod.yml
spring:
  config:
    activate:
      on-profile: prod

server:
  port: 443

logging:
  level:
    root: WARN
```

### Profile-Specific Classes

```java
@Configuration
@Profile("dev")
public class DevDataSource {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}

@Configuration
@Profile("prod")
public class ProdDataSource {
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty("db.url"));
        return new HikariDataSource(config);
    }
}
```

---

## Activation Methods

### Command Line

```bash
# Active profiles
java -jar app.jar --spring.profiles.active=dev

# Include additional profiles
java -jar app.jar --spring.profiles.include=debug,metrics
```

### Environment Variable

```bash
export SPRING_PROFILES_ACTIVE=prod
java -jar app.jar
```

### Application Properties

```yaml
spring:
  profiles:
    active: dev,test
```

### Programmatic

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setAdditionalProfiles("dev", "custom");
        app.run(args);
    }
}
```

### JVM System Property

```bash
java -Dspring.profiles.active=dev -jar app.jar
```

---

## Profile Configuration

### Profile Groups

```yaml
spring:
  profiles:
    group:
      production:
        - proddb
        - prodmq
        - prod-cache
      development:
        - devdb
        - devmq
        - dev-cache
```

### Profile-Specific Beans

```java
@Service
@Profile("dev")
public class DevEmailService implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {
        System.out.println("DEV Email: " + subject);
    }
}

@Service
@Profile("prod")
public class ProdEmailService implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {
        // Real email sending
    }
}
```

### Profile Expressions

```java
// NOT profile
@Bean
@Profile("!production")
public DataSource devDataSource() { }

// AND profile
@Bean
@Profile("dev & fast")
public DataSource fastDevDataSource() { }

// OR profile
@Bean
@Profile("dev | test")
public DataSource devTestDataSource() { }
```

---

## Conditional Beans

### Conditional on Property

```java
@Configuration
@ConditionalOnProperty(name = "app.feature.cache.enabled",
    havingValue = "true")
public class CacheConfiguration {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
```

### Conditional on Class

```java
@Configuration
@ConditionalOnClass(name = "org.springframework.data.redis.RedisTemplate")
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        return new RedisTemplate<>();
    }
}
```

### Custom Conditions

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnProductionEnvironment.class)
public @interface ProductionOnly {}

public class OnProductionEnvironment implements Condition {
    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata metadata) {
        String env = context.getEnvironment()
            .getProperty("app.environment");
        return "production".equals(env);
    }
}
```

---

## Best Practices

### 1. Use Meaningful Profile Names

```
dev         - Development
test        - Testing
staging     - Staging environment
prod        - Production
```

### 2. Don't Put Secrets in Profiles

```yaml
# application-prod.yml (BAD)
db:
  password: secret123

# application-prod.yml (GOOD)
db:
  password: ${DB_PASSWORD}
```

### 3. Use Profile Groups

```yaml
spring:
  profiles:
    group:
      dev:
        - datasource-dev
        - cache-dev
        - messaging-dev
```

### 4. Test with Specific Profiles

```java
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest { }
```

### 5. Document Profiles

```java
/**
 * Production configuration.
 *
 * Profiles: prod, production
 *
 * Requires:
 * - DB_PASSWORD environment variable
 * - REDIS_URL environment variable
 */
@Configuration
@Profile("prod")
public class ProductionConfig { }
```

---

## Further Reading

- [Spring Profiles](https://docs.spring.io/spring-framework/reference/corebeans/profiles.html)
- [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html)
- [Conditional Beans](https://docs.spring.io/spring-framework/reference/corebeans/conditions.html)
