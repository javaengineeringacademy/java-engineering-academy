# Circuit Breaker Pattern

## What Is It?

The circuit breaker pattern prevents an application from repeatedly trying to execute an operation that is likely to fail, allowing it to continue without waiting for fault recovery.

## States

| State | Behavior |
|-------|----------|
| **CLOSED** | Normal operation. Failures are counted. |
| **OPEN** | Calls fail immediately. No actual calls made. |
| **HALF_OPEN** | A trial call is allowed to test recovery. |

## When to Use

- Calling external services (REST, gRPC, databases)
- Network-dependent operations
- Any operation with potential cascading failure risk
- Microservice-to-microservice communication

## Benefits

- Prevents cascading failures across services
- Reduces load on failing downstream services
- Provides fast-fail instead of hanging requests
- Enables automatic recovery detection

## Implementation Options

### Resilience4j (Recommended)

```java
CircuitBreakerConfig config = CircuitBreakerConfig.custom()
    .failureRateThreshold(50)
    .waitDurationInOpenState(Duration.ofSeconds(10))
    .slidingWindowSize(10)
    .build();

CircuitBreaker cb = CircuitBreaker.of("myService", config);

Supplier<String> decorated = CircuitBreaker
    .decorateSupplier(cb, () -> externalService.call());

String result = Try.ofSupplier(decorated)
    .recover(RuntimeException.class, e -> "fallback")
    .get();
```

### Hystrix (Legacy)

Netflix Hystrix popularized this pattern but is now in maintenance mode. Migrate to Resilience4j.

## Configuration Parameters

- **failureRateThreshold**: Percentage of failures before opening (default: 50%)
- **waitDurationInOpenState**: Time before transitioning to half-open
- **slidingWindowSize**: Number of calls to evaluate
- **minimumNumberOfCalls**: Minimum calls before evaluating failure rate
- **permittedNumberOfCallsInHalfOpenState**: Trial calls in half-open state

## Best Practices

1. Always provide a meaningful fallback
2. Monitor circuit breaker state changes
3. Log state transitions for debugging
4. Tune thresholds based on actual failure patterns
5. Combine with retry for transient failures

## See Also
- [Rate Limiting](../rate-limiting/) — Complementary pattern for traffic control
- [Retry](../retry/) — Handling transient failures alongside circuit breakers
- JWT — Auth token handling when services degrade

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Overview

[Brief description of the topic]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Pitfalls

[Common mistakes and anti-patterns]

## References

- [Resilience4j Documentation](https://resilience4j.readme.io/)
- Michael Nygard - *Release It!*
