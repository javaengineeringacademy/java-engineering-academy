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

1. **When should you retry and when should you NOT retry?**
   Retry: network timeouts, 5xx server errors, rate limiting (429), deadlocks, connection refused. Do NOT retry: 4xx client errors (invalid input, auth failure), idempotency violations (non-idempotent POST), business rule failures (validation errors), permanent failures (configuration errors). Retrying non-idempotent operations causes duplicate side effects.

2. **What is the thundering herd problem and how does jitter prevent it?**
   Thundering herd: when many clients retry simultaneously after a service recovers, overwhelming it again. Jitter adds randomness to retry delays, spreading retries across time. Without jitter: 10K clients retry at t+1s simultaneously. With jitter: 10K clients retry between t+0.5s and t+1.5s, reducing peak load by 10x.

3. **How do you make operations idempotent for safe retries?**
   Use idempotency keys: client generates unique key per request, server stores key + result, duplicate requests return cached result. For database operations: use upsert (INSERT ... ON CONFLICT). For message queues: use deduplication (message ID). For REST APIs: prefer PUT over POST (PUT is idempotent by definition).

4. **What is the difference between retry and circuit breaker?**
   Retry handles transient failures (temporary network glitch, brief service unavailability). Circuit breaker handles persistent failures (service is down, database overloaded). Retry: "Try again in case it was a fluke." Circuit breaker: "Stop trying, the service is broken." They work together: retry for transient failures, circuit breaker to stop retry storms.

5. **How do you configure retry with exponential backoff properly?**
   Max attempts: 3 (beyond that, it's likely not transient). Initial delay: 500ms. Multiplier: 2x (500ms → 1s → 2s). Jitter: random 0-50% of delay. Max delay: 30 seconds. Retry only on specific exceptions (IOException, TimeoutException). Ignore business exceptions. Log each retry attempt with context.

## Pitfalls

**Retrying non-idempotent operations:**
```java
// BAD: Retrying a payment charge (non-idempotent)
// Retry causes double charge
for (int i = 0; i < 3; i++) {
    try {
        return paymentService.charge(orderId, amount); // Doubles charge on retry
    } catch (Exception e) {
        Thread.sleep(1000);
    }
}

// GOOD: Use idempotency key
String idempotencyKey = UUID.randomUUID().toString();
for (int i = 0; i < 3; i++) {
    try {
        return paymentService.charge(orderId, amount, idempotencyKey);
        // Server returns same result for same idempotency key
    } catch (Exception e) {
        Thread.sleep(1000);
    }
}
```

**No maximum retry count:**
```java
// BAD: Infinite retries
while (true) {
    try {
        return callService();
    } catch (Exception e) {
        Thread.sleep(1000);
    }
}
// If service is permanently down, this loop runs forever

// GOOD: Maximum 3 retries
for (int i = 0; i < 3; i++) {
    try {
        return callService();
    } catch (Exception e) {
        if (i == 2) throw e; // Give up after 3 attempts
        Thread.sleep(1000 * (i + 1));
    }
}
```

**Retrying on all exceptions:**
```java
// BAD: Retry on business exceptions
try {
    return orderService.createOrder(order);
} catch (ValidationException e) {
    // Retry won't fix invalid input!
    Thread.sleep(1000);
}

// GOOD: Retry only on transient exceptions
try {
    return orderService.createOrder(order);
} catch (IOException | TimeoutException e) {
    // Retry on network/timeout issues
    Thread.sleep(1000);
} catch (ValidationException e) {
    // Don't retry on business logic failures
    throw e;
}
```

## Performance

**Retry Overhead:**
```
No retry: 1 call, 10ms
1 retry: 2 calls, 20ms (if first fails)
2 retries: 3 calls, 30ms (if first two fail)
3 retries: 4 calls, 40ms (if first three fail)

With exponential backoff (500ms initial):
No retry: 10ms
1 retry: 510ms (500ms delay + 10ms call)
2 retries: 1520ms (500ms + 1000ms + 20ms)
3 retries: 3530ms (500ms + 1000ms + 2000ms + 30ms)
```

**Exponential Backoff Comparison:**
```
Fixed delay (1s):
- Attempt 1: 1s
- Attempt 2: 2s
- Attempt 3: 3s
- Total: 6s

Exponential backoff (1s initial):
- Attempt 1: 1s
- Attempt 2: 2s
- Attempt 3: 4s
- Total: 7s

Exponential backoff with jitter (1s initial):
- Attempt 1: 0.5-1.5s (random)
- Attempt 2: 1-3s (random)
- Attempt 3: 2-6s (random)
- Total: 3.5-10.5s (spread across time)
```

## Internal Working

**Exponential Backoff Algorithm:**
```
delay = initial_delay × multiplier^attempt
actual_delay = delay + random(0, jitter × delay)

Example (initial=500ms, multiplier=2, jitter=0.5):
Attempt 0: 500ms + random(0, 250ms) = 500-750ms
Attempt 1: 1000ms + random(0, 500ms) = 1000-1500ms
Attempt 2: 2000ms + random(0, 1000ms) = 2000-3000ms
```

**Resilience4j Retry State Machine:**
```
1. Attempt call
2. If success: return result
3. If failure: check exception type
4. If retryable: check attempt count
5. If attempt < max: calculate delay, sleep, retry
6. If attempt >= max: throw last exception
7. If not retryable: throw immediately
```

## Why This Concept Exists

Retry exists because:

1. **Networks are unreliable**: Temporary failures happen constantly (packet loss, DNS timeouts, TCP retransmissions)
2. **Services are flaky**: Brief outages, garbage collection pauses, and resource contention cause transient failures
3. **User experience**: Users expect requests to succeed even during brief hiccups
4. **System resilience**: Transient failures shouldn't cause permanent failures
5. **Cost of failure**: One failed request can lose revenue, data, or user trust

However, retries must be bounded and idempotent to avoid causing more harm than good (retry storms, duplicate operations, resource exhaustion).

## Overview

The retry pattern automatically re-executes failed operations to handle transient failures. Strategies include fixed delay, exponential backoff, and exponential backoff with jitter. Critical for resilience: network timeouts, 5xx errors, rate limiting. Must be combined with idempotency to prevent duplicate side effects and circuit breakers to prevent retry storms.

## References

- Resilience4j Retry: https://resilience4j.readme.io/docs/retry
- "Release It!" by Michael Nygard — Retry patterns
- "Building Microservices" by Sam Newman — Resilience patterns
- AWS Retry Best Practices: https://docs.aws.amazon.com/general/latest/gr/api-retries.html
- Google Cloud Retry Guidelines: https://cloud.google.com/vertex-ai/docs/general/retry-guidelines
