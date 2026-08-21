# References: Logback

## Official Documentation

- [Logback Manual](https://logback.qos.ch/manual/) - Complete reference
- [Logback Appenders](https://logback.qos.ch/manual/appenders.html) - All appender types
- [Logback Configuration](https://logback.qos.ch/manual/configuration.html) - XML configuration
- [Logback Filters](https://logback.qos.ch/manual/filters.html) - Filter configuration

## Spring Boot Logging

- [Spring Boot Logging](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-logging)
- [Spring Boot Logback Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-logback-configuration)

## Key Classes

| Class | Module | Purpose |
|-------|--------|---------|
| `LoggerContext` | logback-classic | Manages loggers and configuration |
| `ChrootLogger` | logback-classic | Logger with changed root |
| `RollingFileAppender` | logback-core | File rolling output |
| `AsyncAppender` | logback-core | Async wrapper for appenders |
| `PatternLayoutEncoder` | logback-core | Pattern-based encoding |

## Maven/Gradle

```xml
<!-- Logback Classic (includes SLF4J API) -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

## Related Topics

- [Log4j 2](../04-log4j2/README.md)
- [Performance](../07-performance/README.md)
- [Best Practices](../08-best-practices/README.md)
