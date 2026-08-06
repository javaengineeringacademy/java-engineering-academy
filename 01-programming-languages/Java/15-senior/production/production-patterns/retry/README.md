# Retry Pattern

## What Is It?

The retry pattern automatically re-executes a failed operation. It handles transient failures like network timeouts, temporary service unavailability, and race conditions.

## Strategies

### Fixed Delay

- Constant wait time between retries
- Simple and predictable
- **Best for**: Stable services with consistent recovery times

### Exponential Backoff

- Delay doubles with each retry
- Reduces load on recovering services
- **Best for**: Cascading failure prevention

### Exponential Backoff with Jitter

- Adds randomness to exponential backoff
- Prevents thundering herd problem
- **Best for**: Distributed systems with many clients

## When to Retry

- **Network timeouts**: Transient connectivity issues
- **5xx errors**: Server-side temporary failures
- **Rate limiting (429)**: After appropriate wait time
- **Deadlocks**: Database lock contention
- **Connection refused**: Service starting up

## When NOT to Retry

- **4xx errors**: Client errors (invalid input, auth failure)
- **Idempotency violations**: Non-idempotent operations
- **Business rule failures**: Validation errors
- **Permanent failures**: Configuration errors

## Idempotency Requirements

Retries require idempotent operations to avoid duplicate side effects:

- Use idempotency keys for payment processing
- Database operations should use upsert patterns
- Message queues should use deduplication
- REST APIs should be designed idempotently (PUT over POST)

## Configuration

```java
RetryConfig config = RetryConfig.custom()
    .maxAttempts(3)
    .waitDuration(Duration.ofMillis(500))
    .retryExceptions(RuntimeException.class)
    .ignoreExceptions(BusinessException.class)
    .build();
```

## Best Practices

1. Always set a maximum retry count
2. Use jitter to prevent thundering herd
3. Log retry attempts with context
4. Make operations idempotent
5. Combine with circuit breaker for resilience
6. Set appropriate timeouts per attempt

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

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
