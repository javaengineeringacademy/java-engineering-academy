# References: Logging Basics

## Official Documentation

- [SLF4J Manual](http://www.slf4j.org/manual.html) - Official SLF4J documentation
- [Logback Manual](https://logback.qos.ch/manual/) - Logback configuration reference
- [Log4j 2 Manual](https://logging.apache.org/log4j/2.x/manual/) - Log4j 2 documentation
- [java.util.logging (JUL)](https://docs.oracle.com/javase/8/docs/api/java/util/logging/package-summary.html) - JDK logging API

## Books

- "Java Application Logging Handbook" by Raldon Sousa
- "Pro Java Logging" by Dhruv Chopra (Apress)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Facade Pattern | SLF4J separates API from implementation |
| Appender | Where log output goes (console, file, network) |
| Layout/Encoder | How log messages are formatted |
| Threshold | Minimum log level for an appender to output |
| Mapped Diagnostic Context (MDC) | Thread-local key-value pairs added to log output |
| Bridge | Redirects one logging API to another implementation |

## Common Artifacts (Maven)

```xml
<!-- SLF4J API -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>

<!-- Logback Classic (SLF4J implementation) -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>

<!-- Log4j2 Core -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.21.1</version>
</dependency>
```

## Related Topics

- [Structured Logging](../06-structured-logging/README.md)
- [MDC](../05-mdc/README.md)
- [Performance](../07-performance/README.md)
- [Best Practices](../08-best-practices/README.md)
