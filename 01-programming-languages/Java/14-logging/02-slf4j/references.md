# References: SLF4J

## Official Documentation

- [SLF4J Manual](http://www.slf4j.org/manual.html) - Complete SLF4J guide
- [SLF4J Migration Guide](http://www.slf4j.org/migrator.html) - Moving from other frameworks
- [SLF4J 2.0 Release Notes](http://www.slf4j.org/news.html) - Fluent API and changes

## Key SLF4J Artifacts

| Artifact | Purpose |
|----------|---------|
| `slf4j-api` | Core API - include in all projects |
| `logback-classic` | Logback implementation (SLF4J native) |
| `log4j-slf4j2-impl` | Log4j 2 SLF4J binding |
| `slf4j-simple` | Simple console output |
| `slf4j-jdk14` | java.util.logging adapter |
| `slf4j-nop` | No operation (disables logging) |
| `jcl-over-slf4j` | Redirects JCL to SLF4J |
| `log4j-over-slf4j` | Redirects Log4j 1.x to SLF4J |
| `jul-to-slf4j` | Redirects JUL to SLF4J |

## Maven Dependencies

```xml
<!-- Core API only (for libraries) -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>

<!-- With Logback implementation -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

## Related Topics

- [Logging Basics](../01-logging-basics/README.md)
- [Logback](../03-logback/README.md)
- [MDC](../05-mdc/README.md)
