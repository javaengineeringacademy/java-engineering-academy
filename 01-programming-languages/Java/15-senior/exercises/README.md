# Senior Java Exercises

Practice exercises for production-ready Java development patterns.

## Learning Objectives

- Implement production resilience patterns (Circuit Breaker, Rate Limiting, Retry, Bulkhead)
- Build performance-critical data structures and benchmarks
- Apply thread-safe design using concurrent primitives
- Understand trade-offs between performance, safety, and complexity

## Exercises

### 1. PerformanceExercises.java

| Exercise | Description | Concepts |
|----------|-------------|----------|
| **Benchmark Runner** | Implement a mini-JMH benchmark that runs warmup and measurement iterations, calculates avg/min/max | Timing, warmup, JIT optimization |
| **Object Pool** | Thread-safe object pool using `ConcurrentLinkedQueue` and `AtomicInteger` | Pooling, concurrency, resource reuse |
| **LRU Cache** | Least-recently-used eviction cache using `LinkedHashMap` access-order | Caching, data structures, eviction policies |
| **String Concat Benchmark** | Compare `String +`, `StringBuilder`, and `String.join` performance | String internals, GC pressure, benchmarking |
| **Packed Int Array** | Store multiple small ints in a single long using bit manipulation | Memory optimization, bit operations |

### 2. ProductionExercises.java

| Exercise | Description | Concepts |
|----------|-------------|----------|
| **Circuit Breaker** | State machine (CLOSED → OPEN → HALF_OPEN) that stops calls after failure threshold | Fault tolerance, state machines, resilience |
| **Token Bucket Rate Limiter** | Token-bucket algorithm with refill based on elapsed time | Rate limiting, thread safety, concurrency |
| **Retry with Exponential Backoff** | Retry failed operations with increasing delays | Retry patterns, backoff strategies |
| **Bulkhead** | Semaphore-based concurrency limiter that restricts parallel executions | Isolation, resource limiting, backpressure |
| **Timeout Wrapper** | Execute callable with a time limit using `ExecutorService` | Timeouts, cancellation, thread management |

## Instructions

1. Read each TODO comment in the exercise files
2. Implement the missing methods
3. Run the `main` method in each file to verify your solutions
4. All tests must pass for full credit

### Getting Started

```bash
# Compile and run PerformanceExercises
javac PerformanceExercises.java && java academy.javaengineering.exercises.PerformanceExercises

# Compile and run ProductionExercises
javac ProductionExercises.java && java academy.javaengineering.exercises.ProductionExercises
```

### Tips

- **Benchmark Runner**: Use `System.nanoTime()` for timing. Run warmup iterations first, then record measurement iterations.
- **Object Pool**: Use `ConcurrentLinkedQueue` for thread safety. Track active count with `AtomicInteger`.
- **LRU Cache**: `LinkedHashMap` with `accessOrder=true` moves accessed entries to the end. Override `removeEldestEntry()` for eviction.
- **Circuit Breaker**: Track state with `AtomicReference<State>`. Use `volatile` for `lastFailureTime`. Transition: CLOSED → OPEN after threshold, OPEN → HALF_OPEN after timeout, HALF_OPEN → CLOSED on success.
- **Token Bucket**: Refill tokens based on elapsed time since last refill. Use `compareAndSet` or `AtomicInteger` for thread safety.

## Prerequisites

- Java 16+ (for records, pattern matching)
- Concurrency fundamentals (`synchronized`, `AtomicInteger`, `Semaphore`)
- Understanding of thread safety principles
