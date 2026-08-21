# References: Log4j 2

## Official Documentation

- [Log4j 2 Manual](https://logging.apache.org/log4j/2.x/manual/) - Complete reference
- [Log4j 2 Configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html) - Configuration guide
- [Log4j 2 Appenders](https://logging.apache.org/log4j/2.x/manual/appenders.html) - All appender types
- [Log4j 2 Layouts](https://logging.apache.org/log4j/2.x/manual/layouts.html) - Layout reference
- [Log4j 2 Filters](https://logging.apache.org/log4j/2.x/manual/filters.html) - Filter reference
- [Log4j 2 Async](https://logging.apache.org/log4j/2.x/manual/async.html) - Async logging guide

## Security

- [Log4j 2 Security](https://logging.apache.org/log4j/2.x/security.html) - Security considerations
- [JNDI Lookup](https://logging.apache.org/log4j/2.x/manual/jndi.html) - JNDI configuration

## Key Artifacts

| Artifact | Purpose |
|----------|---------|
| `log4j-api` | Public API |
| `log4j-core` | Implementation |
| `log4j-slf4j2-impl` | SLF4J 2.x binding |
| `log4j-1.2-api` | Log4j 1.x bridge |
| `log4j-jul` | java.util.logging adapter |
| `log4j-jcl` | Jakarta Commons Logging adapter |

## Maven

```xml
<!-- Log4j 2 Core -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.21.1</version>
</dependency>

<!-- SLF4J Binding -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j2-impl</artifactId>
    <version>2.21.1</version>
</dependency>

<!-- Async (LMAX Disruptor) -->
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>3.4.4</version>
</dependency>
```

## Related Topics

- [Logback](../03-logback/README.md)
- [Performance](../07-performance/README.md)
- [Best Practices](../08-best-practices/README.md)
