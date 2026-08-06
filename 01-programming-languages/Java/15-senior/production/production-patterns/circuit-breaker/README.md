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

1. **What are the three states of a circuit breaker?** — CLOSED (normal), OPEN (failing, calls rejected), HALF_OPEN (trial calls allowed to test recovery).

2. **When does a circuit breaker open?** — When the failure rate exceeds the threshold (default 50%) within the sliding window.

3. **How does a circuit breaker recover?** — After `waitDurationInOpenState` expires, it transitions to HALF_OPEN. If trial calls succeed, it closes; if they fail, it reopens.

4. **What is the difference between circuit breaker and retry?** — Circuit breaker prevents calls when a service is failing; retry attempts to recover from transient failures. They are complementary.

5. **How do you test a circuit breaker?** — Unit test state transitions with mocked failures. Use Resilience4j's `CircuitBreakerTestUtils` for state manipulation.

6. **What happens if no fallback is configured?** — The exception propagates to the caller. Always provide a fallback for graceful degradation.

## Performance

Circuit breaker adds ~10-50ns overhead per call (state check + metrics update). The benefit is avoiding cascading failures that cause seconds of latency. When open, calls fail immediately (~1µs) instead of timing out (~30s). Metrics use lock-free counters for high throughput.

## Engineering Decision Framework

### ✅ Use Circuit Breaker when:
- Calling external services that may fail or timeout
- Cascading failures across microservices are possible
- Graceful degradation is required during outages
- Load shedding is needed for failing dependencies
- Rapid failure detection improves user experience

### ❌ Avoid Circuit Breaker when:
- Simple retry with backoff handles transient failures
- No external service dependencies exist
- Operations are purely in-process
- Failure rate is too low to justify overhead

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| Retry with backoff | Transient, recoverable failures |
| Rate limiting | Preventing overload on healthy services |
| Bulkhead pattern | Isolating failure domains |
| Timeout + fallback | Simple degradation without state machine |

### Production Examples
- Payment gateway integration
- Third-party API calls (REST, gRPC)
- Database connection failure handling
- Message queue producer/consumer resilience
- Inter-service communication in microservices

### Common Production Mistakes
- Not providing meaningful fallback responses
- Setting failure thresholds too sensitively (opens too often)
- Ignoring circuit breaker state transitions in monitoring
- Not combining with retry for transient failures
- Using circuit breaker for non-idempotent operations without careful design

## Examples

```java
// Circuit breaker with fallback
CircuitBreakerConfig config = CircuitBreakerConfig.custom()
    .failureRateThreshold(50)
    .waitDurationInOpenState(Duration.ofSeconds(30))
    .slidingWindowSize(10)
    .minimumNumberOfCalls(5)
    .build();

CircuitBreaker cb = CircuitBreaker.of("paymentService", config);

// With fallback
Supplier<String> decorated = CircuitBreaker
    .decorateSupplier(cb, () -> paymentService.processPayment(order));

String result = Try.ofSupplier(decorated)
    .recover(CallNotPermittedException.class, e -> {
        System.out.println("Circuit open, using fallback");
        return "fallback: payment queued";
    })
    .recover(TimeoutException.class, e -> "fallback: timeout, retry later")
    .get();
```

## Internal Working

The circuit breaker tracks failures using a sliding window (count-based or time-based). Each call increments the failure counter. When the failure rate exceeds the threshold, the state transitions to OPEN. After the wait duration, it transitions to HALF_OPEN and allows trial calls. Success closes the circuit; failure reopens it. State transitions are atomic and thread-safe.

## Why This Concept Exists

When a downstream service fails, callers keep sending requests, overwhelming the failing service and consuming resources (threads, connections). Circuit breaker stops this by failing fast when failures exceed a threshold. This gives the failing service time to recover and prevents cascading failures across the system. It is essential for microservice resilience.

## Pitfalls

1. **No fallback**: Without a fallback, circuit breaker just changes the exception type — not helpful
2. **Wrong thresholds**: Too sensitive (opens too often) or not sensitive enough (lets failures through)
3. **Ignoring state changes**: Not monitoring when circuit opens/closes misses recovery opportunities
4. **Not combining with retry**: Transient failures need retry; persistent failures need circuit breaker
5. **Testing gaps**: Circuit breaker behavior must be tested with fault injection

## References

- [Resilience4j CircuitBreaker](https://resilience4j.readme.io/docs/circuitbreaker)
- [Michael Nygard - Release It!](https://pragprog.com/titles/mnee2/release-it-second-edition/)
- [Netflix Hystrix (Legacy)](https://github.com/Netflix/Hystrix)
