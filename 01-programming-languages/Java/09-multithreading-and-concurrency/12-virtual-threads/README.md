# 12 - Virtual Threads

## Overview

Virtual threads (Project Loom, Java 21) solve the scalability problem of platform threads. They are lightweight threads scheduled by the JVM, enabling millions of concurrent tasks with minimal memory overhead.

## Key Differences

| Aspect | Platform Thread | Virtual Thread |
|--------|----------------|----------------|
| Stack | 1MB reserved | 1KB (grows/shrinks) |
| Scheduling | OS scheduler | JVM scheduler |
| Blocking | Ties up OS thread | Frees carrier thread |
| Max count | ~10,000 | ~millions |
| Pool sizing | Complex formulas | One per task |

## Creating Virtual Threads

```java
// Virtual thread
Thread.startVirtualThread(() -> System.out.println("Virtual"));

// Builder
Thread vt = Thread.ofVirtual().name("vt-1").start(() -> {});

// ExecutorService
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> System.out.println("Task"));
}
```

## Structured Concurrency (Preview)

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user = scope.fork(() -> fetchUser());
    Future<Order> order = scope.fork(() -> fetchOrder());
    scope.join();
    return new Response(user.get(), order.get());
}
```
