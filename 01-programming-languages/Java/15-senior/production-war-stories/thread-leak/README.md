# War Story: Thread Leak Crashed Production

## What Happened

Our notification service experienced a slow death over 3 days. Thread count grew from 200 to 10,000+, eventually causing `OutOfMemoryError: unable to create new native thread`. The service became unresponsive and had to be force-killed and restarted. This pattern repeated 3 times before we identified the root cause.

## Timeline

| Time | Event |
|------|-------|
| Day 1, 00:00 | Service deployed with new notification batching feature |
| Day 1, 12:00 | Thread count: 250 (normal) |
| Day 2, 00:00 | Thread count: 1,200 (elevated but not alerting) |
| Day 2, 12:00 | Thread count: 3,800 |
| Day 3, 00:00 | Thread count: 7,200 |
| Day 3, 08:00 | Service crashes with OutOfMemoryError |
| Day 3, 08:15 | Service restarted, thread count resets to 200 |
| Day 3, 20:00 | Thread count: 2,100 (growing again) |
| Day 4, 06:00 | Root cause identified in thread dump |
| Day 4, 10:00 | Hotfix deployed |

## Root Cause

A developer added a notification batching feature that created a new `ExecutorService` per HTTP request to batch database writes:

```java
// Leaked code — new ExecutorService per request
@PostMapping("/notifications")
public ResponseEntity<Void> processNotifications(@RequestBody List<Notification> notifications) {
    ExecutorService executor = Executors.newFixedThreadPool(5); // LEAKED!
    List<Future<Void>> futures = new ArrayList<>();

    for (Notification notification : notifications) {
        futures.add(executor.submit(() -> {
            notificationRepository.save(notification);
            return null;
        }));
    }

    // Wait for all tasks to complete
    for (Future<Void> future : futures) {
        future.get();
    }

    // BUG: executor.shutdown() never called!
    return ResponseEntity.ok().build();
}
```

Each request created 5 threads. At 100 requests/minute, the service leaked 500 threads/minute. The threads were never shut down because `executor.shutdown()` was missing.

## Detection

### Thread Dump Analysis
```
"pool-1-thread-1" #50 prio=5 os_prio=0 tid=0x00007f4b3c0a8000
   java.lang.Thread.State: TIMED_WAITING (parking)
    at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:226)

"pool-2-thread-1" #51 prio=5 os_prio=0 tid=0x00007f4b3c0ac000
   java.lang.Thread.State: WAITING (parking)

"pool-3-thread-1" #52 prio=5 os_prio=0 tid=0x00007f4b3c0b0000
   java.lang.Thread.State: WAITING (parking)
```
Thread names showed incrementing pool numbers (pool-1, pool-2, pool-3...), confirming new ExecutorService instances per request.

### Metrics
- Thread count: `jvm_threads_current` grew linearly
- No alerting on thread count (was only monitoring heap memory)
- OutOfMemoryError appeared in application logs

### What We Missed
- No thread count alerting
- No resource leak detection in code review
- No integration test for thread lifecycle

## Fix

### Immediate (Hotfix)
1. Added `executor.shutdown()` in finally block
2. Replaced per-request ExecutorService with shared application-level pool

```java
// Fixed code — shared thread pool
@Service
public class NotificationService {
    private final ExecutorService executor =
        Executors.newFixedThreadPool(10);

    public void processNotifications(List<Notification> notifications) {
        List<Future<Void>> futures = new ArrayList<>();
        for (Notification notification : notifications) {
            futures.add(executor.submit(() -> {
                notificationRepository.save(notification);
                return null;
            }));
        }
        for (Future<Void> future : futures) {
            try {
                future.get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Notification processing failed", e);
            }
        }
    }
}
```

### Short-Term (Within 1 Week)
1. Added thread count alerting: `jvm_threads_current > 500`
2. Ran SpotBugs scan for resource leak patterns across codebase
3. Added thread pool metrics to Grafana (active threads, pool size, queue size)

### Long-Term (Within 1 Month)
1. Implemented code review checklist for resource management
2. Added LeakCanary-style runtime leak detection
3. Created shared thread pool utility with monitoring
4. Added integration test for thread lifecycle validation

## Prevention

### Code Standards
- ExecutorService must be created at class level, not per-request
- All resources must be closed in finally blocks or try-with-resources
- Use `@PreDestroy` for cleanup of application-level resources
- Thread pool configuration must be externalized (not hardcoded)

### Monitoring
- Alert on thread count > 500 per service
- Alert on thread count growth rate > 50/minute
- Monitor thread pool utilization for shared pools
- Dashboard for JVM thread metrics

### Code Review
- Flag any `Executors.new*()` calls inside request handlers
- Verify resource cleanup in all code paths
- Use SpotBugs/FindBugs for leak detection
- Add ArchUnit rule: no ExecutorService creation in controller layer
