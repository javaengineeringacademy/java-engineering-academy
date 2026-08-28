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

1. **What is graceful shutdown and why is it critical for production?**
   Graceful shutdown ensures an application finishes processing in-flight requests before terminating, preventing data loss, corrupted state, and failed transactions. Without it, Kubernetes sends SIGTERM, the process dies immediately, and 50-100 in-flight requests fail. With graceful shutdown, requests complete, connections close cleanly, and data consistency is maintained.

2. **How do you implement graceful shutdown in a Spring Boot application?**
   Implement `DisposableBean` or `ApplicationListener<ContextClosedEvent>`. In `destroy()`: (1) stop accepting new requests, (2) set executor.shutdown(), (3) awaitTermination with timeout, (4) close database connections, (5) flush caches. Also register a JVM shutdown hook as a safety net.

3. **What is the Kubernetes shutdown sequence and how do you optimize it?**
   K8s sends SIGTERM → waits terminationGracePeriodSeconds (default 30s) → sends SIGKILL. Optimize: add `preStop` hook with 10s sleep (allows load balancer deregistration), set `terminationGracePeriodSeconds` to 60s, implement health checks that fail during shutdown (prevents new traffic).

4. **What happens if graceful shutdown takes too long?**
   Kubernetes sends SIGKILL after `terminationGracePeriodSeconds`, forcefully killing the process. This defeats graceful shutdown. Solution: set a timeout budget (e.g., 25s for drain, 5s for cleanup), use `shutdownNow()` if timeout exceeded, and log progress for debugging.

5. **How do you test graceful shutdown behavior?**
   (1) Send requests during shutdown, (2) verify in-flight requests complete, (3) verify no new requests accepted, (4) verify connections closed, (5) verify cleanup hooks executed. Use `kubectl delete pod` with `--grace-period=0` to simulate forced shutdown.

## Pitfalls

**Forgetting to register shutdown hooks early:**
```java
// BAD: Registering shutdown hook after server starts
public static void main(String[] args) {
    server.start();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        server.stop(); // Too late — server may already be stopping
    }));
}

// GOOD: Register shutdown hook before starting
public static void main(String[] args) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        server.stop();
    }));
    server.start();
}
```

**No timeout on shutdown:**
```java
// BAD: awaitTermination without timeout — hangs forever
executor.shutdown();
executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

// GOOD: Set reasonable timeout
executor.shutdown();
if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
    log.warn("Shutdown timed out, forcing");
    executor.shutdownNow();
}
```

**Not draining message consumers:**
```java
// BAD: Stopping consumer without finishing messages
kafkaConsumer.unsubscribe(); // In-flight messages lost

// GOOD: Commit offsets and close consumer
kafkaConsumer.commitSync(); // Commit current offsets
kafkaConsumer.unsubscribe();
kafkaConsumer.close();
```

## Performance

**Shutdown Timing:**
```
In-flight request completion: 10-30s (depends on request type)
Connection pool drain: 1-5s
Cache flush: 0.1-1s
Message consumer drain: 5-30s (depends on batch size)
Total graceful shutdown: 15-60s

Kubernetes terminationGracePeriodSeconds: default 30s
Recommended: 60s (if application needs >30s to drain)
```

**Resource Cleanup Order:**
```
1. Stop accepting new requests (<1ms)
2. Complete in-flight requests (10-30s)
3. Close database connections (1-5s)
4. Flush caches (0.1-1s)
5. Commit message offsets (1-5s)
6. Close HTTP server (<1ms)
7. Exit process (<1ms)

Total: 12-40s typical
```

## Internal Working

**JVM Shutdown Hook Execution:**
1. JVM receives SIGTERM (from Kubernetes or OS)
2. JVM invokes all registered shutdown hooks in reverse registration order
3. Hooks run in parallel (not sequentially)
4. JVM waits for all hooks to complete (or timeout)
5. JVM calls System.exit(0)
6. If hooks don't complete in time, OS sends SIGKILL

**Spring Boot Shutdown Sequence:**
1. `ContextClosedEvent` published
2. `@PreDestroy` methods invoked
3. `DisposableBean.destroy()` called
4. `ApplicationContext` closed
5. `JVM shutdown hooks` invoked
6. Process exits

**Kubernetes Pod Termination:**
```
1. kubectl delete pod
2. Pod status → Terminating
3. Endpoints controller removes pod from Service endpoints
4. kubelet sends SIGTERM to container
5. Application performs graceful shutdown
6. If shutdown completes: pod deleted
7. If timeout: kubelet sends SIGKILL
8. Pod removed from etcd
```

## Why This Concept Exists

Graceful shutdown exists because:

1. **Data loss prevention**: In-flight requests may be writing to databases, processing payments, or updating state
2. **Transaction integrity**: Database transactions must commit or rollback cleanly
3. **Resource cleanup**: Database connections, file handles, and network sockets must close properly
4. **Cache consistency**: Dirty caches must be flushed before shutdown
5. **User experience**: Users expect requests to complete, not fail with 503 errors
6. **Kubernetes orchestration**: Pods are ephemeral; graceful shutdown ensures clean transitions during deployments

Without graceful shutdown, rolling deployments cause 1-5% request failure rate, which is unacceptable for production systems.

## Overview

Graceful shutdown ensures an application finishes processing in-flight requests before terminating. It involves four phases: stop accepting new requests, drain in-progress requests, release resources, and terminate. Critical for Kubernetes deployments where pods are frequently created and destroyed. Implementation includes JVM shutdown hooks, Spring Boot DisposableBean, and Kubernetes preStop hooks.

## References

- Kubernetes Termination: https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle/#termination-of-pods
- Spring Boot Graceful Shutdown: https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.server
- JVM Shutdown Hooks: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Runtime.html#addShutdownHook(java.lang.Thread)
- "Release It!" by Michael Nygard — Graceful degradation patterns
- Netty Graceful Shutdown: https://netty.io/wiki/user-guide-for-4.x.html
