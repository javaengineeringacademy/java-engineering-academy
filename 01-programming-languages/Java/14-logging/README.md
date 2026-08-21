# Logging

> Comprehensive guide to Java logging — SLF4J, Log4j2, Logback, structured logging, MDC, and best practices.

## Why Logging?

Logging is essential for:
- **Debugging** — trace application behavior
- **Monitoring** — track production health
- **Auditing** — record user actions
- **Security** — detect suspicious activity
- **Performance** — measure response times

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | [Logging Basics](01-logging-basics/) | Core logging concepts |
| 02 | [SLF4J](02-slf4j/) | Facade API for logging |
| 03 | [Logback](03-logback/) | Native SLF4J implementation |
| 04 | [Log4j2](04-log4j2/) | High-performance logging |
| 05 | [MDC](05-mdc/) | Mapped Diagnostic Context |
| 06 | [Structured Logging](06-structured-logging/) | JSON and structured logs |
| 07 | [Performance](07-performance/) | Logging performance optimization |
| 08 | [Best Practices](08-best-practices/) | Logging patterns and conventions |

## Logging Architecture

```
┌─────────────────────────────────────┐
│       Java Logging Architecture     │
├─────────────────────────────────────┤
│  Application Code                   │
│  ┌─────────────────────────────┐    │
│  │ logger.info("message")      │    │
│  └─────────────────────────────┘    │
│           ↓                         │
│  Logging Facade (SLF4J)            │
│  ┌─────────────────────────────┐    │
│  │ Unified API                 │    │
│  └─────────────────────────────┘    │
│           ↓                         │
│  Implementation                     │
│  ┌─────────────────────────────┐    │
│  │ Logback │ Log4j2 │ JUL     │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

## Log Levels

| Level | Purpose |
|-------|---------|
| ERROR | Unexpected errors |
| WARN | Potential issues |
| INFO | Business events |
| DEBUG | Development details |
| TRACE | Fine-grained details |

## Resources

- [SLF4J Manual](http://www.slf4j.org/manual.html)
- [Logback Manual](https://logback.qos.ch/manual/)
- [Log4j2 Manual](https://logging.apache.org/log4j/2.x/manual/)
