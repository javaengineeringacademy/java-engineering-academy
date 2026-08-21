# Decision Framework: Logback Configuration

## When to Choose Logback

### Use Logback When:

- **Default choice for SLF4J** - Native implementation, no binding needed
- **Spring Boot project** - Pre-configured with sensible defaults
- **Need mature, stable logging** - Battle-tested since 2006
- **Simple configuration requirements** - XML/Groovy configuration
- **Need rolling file policies** - Time, size, and combined rolling

### Consider Alternatives When:

- **Extreme throughput required** - Log4j 2's async is faster
- **Need fancy JSON layouts** - Log4j 2 has more layout options
- **Java EE/Jakarta EE apps** - Log4j 2 has better integration
- **Need garbage-free logging** - Log4j 2 is optimized for this

## Logback vs Log4j 2

| Aspect | Logback | Log4j 2 |
|--------|---------|---------|
| SLF4J integration | Native | Binding required |
| Configuration | XML/Groovy | XML/JSON/YAML/Properties |
| Async performance | Good | Better (LMAX Disruptor) |
| Garbage generation | Some | Garbage-free mode |
| Rolling policies | Time, Size, TimeAndSize | All Logback + more |
| JSON support | Via encoder | Native Layouts |
| Plugin system | Limited | Extensive |
| MDC | Yes | Yes (ThreadContext) |
| Java version | 8+ | 8+ |
| License | EPL 1.0 / LGPL 2.1 | Apache 2.0 |

## Configuration Format Decision

| Format | Pros | Cons |
|--------|------|------|
| XML | Most documented, standard | Verbose, error-prone |
| Groovy | More readable, programmatic | Requires groovy dependency |
| Programmatic | Full control, testable | More code, harder to change |

## Rolling Policy Selection

| Scenario | Policy | Example |
|----------|--------|---------|
| Daily rotation | `TimeBasedRollingPolicy` | `app.%d{yyyy-MM-dd}.log` |
| Size limit per file | `SizeAndTimeBasedRollingPolicy` | `app.%d{yyyy-MM-dd}.%i.log` |
| Fixed window | `FixedWindowRollingPolicy` | `app.%i.log` (legacy) |

## Spring Boot vs Raw Logback

| Aspect | Spring Boot | Raw Logback |
|--------|------------|-------------|
| Configuration | `application.yml` | `logback.xml` |
| Defaults | Sensible out of box | Must configure everything |
| Profile support | Native | Manual |
| Externalized config | Via Spring properties | Manual |
| Complexity | Lower | Higher |
