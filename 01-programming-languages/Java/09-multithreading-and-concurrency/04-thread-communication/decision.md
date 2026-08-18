# Thread Communication Decision Guide

## wait/notify vs interrupt vs join

| Method | Use When | Releases Lock | Resumable |
|--------|----------|---------------|-----------|
| `wait()/notify()` | Thread waits for condition | Yes | Yes (by notify) |
| `interrupt()` | Request thread to stop | No | Yes (if handled) |
| `join()` | Wait for thread completion | No | N/A (waits for end) |

## notify vs notifyAll

| Aspect | notify() | notifyAll() |
|--------|----------|-------------|
| Wakes | One thread | All threads |
| Risk | May wake wrong thread | Wakes all (some may not need it) |
| Recommendation | Avoid in most cases | Prefer for correctness |
| Use when | Only one thread can make progress | Multiple conditions or unknown waiters |

## Handling InterruptedException

```java
// CORRECT: Re-interrupt or throw
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // preserve status
    // or throw new InterruptedException(e.getMessage());
}

// WRONG: Swallowing
catch (InterruptedException e) {
    // do nothing — breaks interrupt protocol
}
```
