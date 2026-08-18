# ExecutorService

## Overview

ExecutorService is the primary interface for managing thread pools. It provides task submission, lifecycle management, and result retrieval through Futures.

## Creating ExecutorService

```java
// Fixed thread pool
ExecutorService fixed = Executors.newFixedThreadPool(4);

// Cached thread pool
ExecutorService cached = Executors.newCachedThreadPool();

// Single thread
ExecutorService single = Executors.newSingleThreadExecutor();

// Custom pool (recommended for production)
ExecutorService custom = new ThreadPoolExecutor(
    4, 8, 60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000),
    new ThreadPoolExecutor.CallerRunsPolicy()
);
```

## Graceful Shutdown

```java
executor.shutdown();           // stop accepting
if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
    executor.shutdownNow();    // force stop
}
```
