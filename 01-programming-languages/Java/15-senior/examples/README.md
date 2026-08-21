# Examples Index

## Advanced Concurrency

| # | File | Topics |
|---|------|--------|
| 1 | [AdvancedConcurrencyDemo.java](AdvancedConcurrencyDemo.java) | CompletableFuture, virtual threads, structured concurrency |

## Architecture Decisions

| # | File | Topics |
|---|------|--------|
| 2 | [ArchitectureDecisionDemo.java](ArchitectureDecisionDemo.java) | ADR pattern, trade-off analysis |

## Performance Engineering

| # | File | Topics |
|---|------|--------|
| 3 | [PerformanceEngineeringDemo.java](PerformanceEngineeringDemo.java) | JFR, JMH, profiling |

## Production Patterns

| # | File | Topics |
|---|------|--------|
| 4 | [ProductionPatternsDemo.java](ProductionPatternsDemo.java) | Circuit breaker, rate limiting, health checks |

## Running

```bash
# Compile and run any example
javac AdvancedConcurrencyDemo.java
java AdvancedConcurrencyDemo

# For JFR examples, ensure jdk.jfr module is available
java --add-modules jdk.jfr AdvancedConcurrencyDemo
```
