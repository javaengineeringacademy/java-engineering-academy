# Graceful Shutdown

## What Is It?

Graceful shutdown ensures an application finishes processing in-flight requests before terminating, preventing data loss and corrupted state.

## Shutdown Phases

1. **Stop accepting new requests** — Reject incoming traffic
2. **Drain in-progress requests** — Complete pending work
3. **Release resources** — Close connections, flush caches
4. **Terminate** — Exit the process

## Implementation

### Shutdown Hooks

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    System.out.println("Shutdown signal received");
    initiateGracefulShutdown();
}));
```

### ExecutorService Shutdown

```java
executor.shutdown(); // Stop accepting new tasks
if (!executor.awaitTermination(timeout, TimeUnit.SECONDS)) {
    executor.shutdownNow(); // Force shutdown
}
```

## Kubernetes Considerations

- **preStop hook**: Add delay before SIGTERM (e.g., 10s)
- **terminationGracePeriodSeconds**: K8s default is 30s
- **SIGTERM vs SIGKILL**: K8s sends SIGTERM first, then SIGKILL after grace period
- **Load balancer deregistration**: Remove from service before shutdown

```yaml
lifecycle:
  preStop:
    exec:
      command: ["/bin/sh", "-c", "sleep 10"]
```

## Best Practices

1. Always register shutdown hooks early in application startup
2. Set reasonable timeouts for drain operations
3. Log shutdown progress for debugging
4. Implement health checks that reflect shutdown state
5. Test shutdown behavior regularly
6. Handle both SIGTERM and SIGINT signals

## Common Pitfalls

- Forgetting to close database connections
- Not draining message queue consumers
- Skipping cache flush operations
- Ignoring in-flight HTTP responses
- No timeout on shutdown — process hangs forever

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

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Examples

[Code examples demonstrating the concept]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
