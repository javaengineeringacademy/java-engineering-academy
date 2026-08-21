# Decision: Logging

## When to Use Different Log Levels

| Level | Use Case |
|-------|----------|
| ERROR | System errors, exceptions, failures |
| WARN | Degraded performance, fallback used |
| INFO | Business events, startup/shutdown |
| DEBUG | Method entry/exit, variable values |
| TRACE | Loop iterations, detailed flow |

## Framework Selection

| Framework | Use Case |
|-----------|----------|
| SLF4J + Logback | Default for Spring Boot |
| SLF4J + Log4j2 | High-performance applications |
| JUL | Legacy applications |
| Log4j | Legacy systems |

## Best Practices

1. Use SLF4J facade, not implementations directly
2. Use parameterized messages: `logger.info("User {} logged in", userId)`
3. Don't concatenate strings: `logger.info("User " + userId)` is bad
4. Use MDC for context: request ID, user ID
5. Avoid expensive operations in log messages

## Further Reading

- [SLF4J Manual](http://www.slf4j.org/manual.html)
- [Logback Manual](https://logback.qos.ch/manual/)
