# Virtual Threads (Project Loom)

## Core Concepts

Virtual threads are lightweight threads managed by the JVM rather than the OS.
They were introduced as a preview in Java 19 and finalized in Java 21.

### Key Properties

- Created by the JVM, not the operating system
- Millions can run concurrently on a single machine
- Use carrier threads (platform threads) for actual execution
- Designed for I/O-bound workloads
- Cheap to create and context-switch

---

## Loom Project

Project Loom introduced three main features:

1. **Virtual Threads** - lightweight threads (Java 21)
2. **Structured Concurrency** - scoped task management (Java 21)
3. **Scoped Values** - efficient thread-local alternative (Java 21)

### Structured Concurrency

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> user = scope.fork(() -> fetchUser(id));
    Subtask<Orders> orders = scope.fork(() -> fetchOrders(id));

    scope.join(); // waits for both tasks

    return new Dashboard(user.get(), orders.get());
}
```

Two policies:
- `ShutdownOnFailure`: cancels all tasks if any fails
- `ShutdownOnSuccess`: cancels remaining tasks after first success

---

## Migration from Thread Pools

### Before (Thread Pool)

```java
ExecutorService executor = Executors.newFixedThreadPool(200);
executor.submit(() -> handleRequest());
```

### After (Virtual Threads)

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> handleRequest());
}
```

### Migration Checklist

1. Replace `Executors.newFixedThreadPool(N)` with `newVirtualThreadPerTaskExecutor`
2. Replace `synchronized` blocks with `ReentrantLock`
3. Remove thread pool sizing logic (virtual threads handle scale)
4. Audit `ThreadLocal` usage for memory implications
5. Test for pinning with `-Djdk.tracePinnedThreads=full`

---

## Best Practices

### Do

- Use for I/O-bound work (HTTP, DB, file operations)
- Use structured concurrency for related tasks
- Use `ReentrantLock` instead of `synchronized`
- Clean up `ThreadLocal` values after use
- Monitor with JFR events (`jdk.VirtualThreadStart`)

### Don't

- Pool virtual threads (one per task is the model)
- Use for CPU-bound computation (no benefit)
- Use `synchronized` in long-running sections
- Hold `ThreadLocal` across task boundaries without cleanup
- Assume virtual threads fix all concurrency problems

---

## When to Use

| Scenario | Use Virtual Threads? |
|----------|---------------------|
| Database queries | Yes |
| HTTP client calls | Yes |
| File I/O | Yes |
| Waiting on locks | Yes |
| CPU-bound math | No |
| Graphics rendering | No |
| Low-latency trading | No |

---

## Performance Characteristics

- Creation: ~1000x cheaper than platform threads
- Memory: ~1KB vs ~1MB per platform thread
- Context switch: ~200ns vs ~2000ns (OS level)
- Throughput: scales linearly with I/O tasks

Run with: `java -Djdk.tracePinnedThreads=full VirtualThreadsDemo.java`

## Why Virtual Threads Over Thread Pools?

| Criteria | Virtual Threads | Thread Pools |
|----------|----------------|--------------|
| Creation cost | ~1KB | ~1MB |
| Max count | Millions | Thousands |
| Blocking | Cheap (unmounts) | Expensive (wastes thread) |
| Complexity | Simple | Complex sizing |
| Use when | I/O-bound, many connections | CPU-bound, few connections |

### Decision Flowchart
I/O-bound? → Yes → Many concurrent? → Yes → Use Virtual Threads
CPU-bound? → Yes → Use Thread Pools

## Interview Questions

1. **How do virtual threads achieve millions of concurrent threads when platform threads are limited to thousands?**
   Platform threads map 1:1 to OS threads, each requiring ~1MB stack + kernel resources (file descriptors, etc.). OS limits threads to ~10K-50K due to memory and scheduling overhead. Virtual threads are managed by the JVM as continuations: they have ~1KB stacks stored on the heap. When a virtual thread blocks (I/O), the JVM "unmounts" it from its carrier thread and stores its stack on the heap. The carrier thread runs other virtual threads. This multiplexing allows millions of virtual threads on a handful of carrier threads.

2. **What is "pinning" and why is it problematic?**
   Pinning occurs when a virtual thread cannot be unmounted from its carrier thread. Causes: (1) `synchronized` blocks — the JVM cannot safely unmount a thread holding an intrinsic lock, (2) native methods — JNI calls execute on the carrier thread. Pinning defeats the purpose of virtual threads because the carrier thread is blocked. Detect with `-Djdk.tracePinnedThreads=full`. Fix by replacing `synchronized` with `ReentrantLock`.

3. **When should you NOT use virtual threads?**
   Virtual threads provide no benefit for CPU-bound work (computations, graphics rendering, cryptography). They're designed for I/O-bound workloads where threads spend most time blocked. CPU-bound tasks are limited by compute cores, not thread count. Thread pools with fixed parallelism (matching core count) are more efficient for CPU-bound work. Also avoid for low-latency trading where OS thread scheduling provides deterministic timing.

4. **How does `Thread.ofVirtual()` differ from `Executors.newVirtualThreadPerTaskExecutor()`?**
   `Thread.ofVirtual().start(runnable)` creates a single virtual thread — equivalent to creating a platform thread. `newVirtualThreadPerTaskExecutor()` creates an executor where each submitted task runs on its own virtual thread, with automatic cleanup after task completion. The executor is `AutoCloseable` and should be used with try-with-resources. Use the executor for task-based parallelism; use `Thread.ofVirtual()` for long-lived background tasks.

5. **What happens to `ThreadLocal` values when using virtual threads?**
   Each virtual thread has its own `ThreadLocal` storage. With millions of virtual threads, each having its own `ThreadLocal` map, memory consumption grows linearly. `InheritableThreadLocal` copies values to child threads, which with millions of virtual threads could cause excessive memory use. Solution: use scoped values (`ScopedValue`) instead of `ThreadLocal` with virtual threads. Scoped values are stored more efficiently and automatically cleaned up.

## Examples

### Web Server Handling 100K Concurrent Connections
```java
public class VirtualThreadServer {
    public static void main(String[] args) throws IOException {
        var serverSocket = ServerSocketChannel.open().bind(new InetSocketAddress(8080));

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            while (true) {
                SocketChannel channel = serverSocket.accept();
                executor.submit(() -> handleConnection(channel));
            }
        }
    }

    private static void handleConnection(SocketChannel channel) {
        try (channel) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);  // Blocking I/O — virtual thread unmounts
            String request = new String(buffer.array()).trim();
            String response = processRequest(request);
            channel.write(ByteBuffer.wrap(response.getBytes()));
        } catch (IOException e) {
            log.error("Connection error", e);
        }
    }
}
```

### Parallel Database Queries Without Thread Pool Tuning
```java
public class DatabaseService {
    public List<User> fetchUsers(List<Long> userIds) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<User>> futures = userIds.stream()
                .map(id -> executor.submit(() -> queryUser(id)))
                .toList();

            return futures.stream()
                .map(f -> {
                    try { return f.get(); }
                    catch (Exception e) { throw new RuntimeException(e); }
                })
                .toList();
        }
    }

    private User queryUser(long id) {
        // Blocking JDBC call — virtual thread unmounts during DB wait
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", User.class, id);
    }
}
```

### Avoiding Pinning with ReentrantLock
```java
public class SafeCache {
    private final Map<String, Object> cache = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();  // NOT synchronized

    public Object get(String key) {
        lock.lock();
        try {
            return cache.get(key);  // Not pinned — virtual thread can unmount
        } finally {
            lock.unlock();
        }
    }

    // BAD: This pins the virtual thread
    public synchronized Object getPinned(String key) {
        return cache.get(key);  // Pinned! Blocks carrier thread
    }
}
```

## Internal Working

**Continuation mechanism:**
Virtual threads are Java `Thread` objects backed by `Continuation` objects. A continuation represents a suspended computation. When created, a virtual thread's stack is a lightweight object on the heap (~1KB initial). The `Continuation.run()` method executes the stack frame by frame. When blocking is detected (I/O, sleep, park), `Continuation.yield()` saves the stack to the heap and returns control to the carrier thread.

**Carrier thread scheduling:**
The JVM uses a `ForkJoinPool` as the carrier thread pool (default parallelism = `Runtime.availableProcessors()`). When a virtual thread unmounts, the carrier thread picks up the next mounted virtual thread from a queue. When a virtual thread becomes unblocked (I/O completes), it's enqueued for mounting on a free carrier thread. If all carriers are busy, the unblocked virtual thread waits in a queue.

**Mounting/unmounting:**
Mounting a virtual thread restores its stack frames from the heap to a carrier thread. Unmounting saves stack frames to the heap. The JVM uses stack copying (not interception) for efficiency. `synchronized` blocks prevent unmounting because the JVM cannot safely save the monitor state. `ReentrantLock` uses `Condition.await()` which can be safely yielded.

**JFR events:**
- `jdk.VirtualThreadStart`: emitted when a virtual thread starts
- `jdk.VirtualThreadEnd`: emitted when a virtual thread completes
- `jdk.VirtualThreadPinned`: emitted when pinning is detected (with stack trace)
- Use JFR recordings to identify pinning hotspots in production

**Memory model:**
Virtual thread stacks are heap-allocated and GC-managed. The JVM doesn't need to pre-allocate stack memory. When a virtual thread blocks, its stack is copied to the heap and the carrier thread's stack is reused. This is efficient because (1) most virtual threads are blocked most of the time, (2) stack copying is fast for shallow stacks, (3) GC can reclaim terminated virtual thread stacks.

## Overview

Virtual threads are lightweight threads managed by the JVM, introduced in Java 21 (finalized from Project Loom). They enable millions of concurrent I/O-bound tasks by multiplexing on a small number of carrier (platform) threads. Virtual threads are cheap to create (~1KB vs ~1MB), cheap to context-switch (~200ns vs ~2000ns), and automatically unmount from carrier threads during blocking operations.

## Why This Concept Exists

The fundamental problem: modern applications handle thousands of concurrent I/O operations (HTTP requests, database queries, file I/O). Traditional platform threads are too expensive:

1. **Thread cost**: Each platform thread requires ~1MB stack + OS kernel structures. 10K threads = 10GB memory.
2. **Blocking waste**: When a platform thread blocks on I/O, it wastes an OS thread. Thread pools mitigate this but require complex sizing.
3. **Scalability ceiling**: OS limits on threads (~10K-50K) cap concurrent I/O operations.
4. **Complexity**: Thread pool tuning, work queue management, and thread lifecycle add significant complexity.

Virtual threads solve this by:
- **Cheap creation**: ~1KB heap allocation, no OS thread reservation
- **Efficient blocking**: Unmount from carrier threads during I/O, freeing them for other work
- **Simple programming model**: Write blocking code, get non-blocking performance
- **No thread pool tuning**: One virtual thread per task, the JVM handles scheduling

Before virtual threads, developers chose between blocking I/O (simple but limited scalability) and non-blocking I/O (complex but scalable). Virtual threads provide the simplicity of blocking I/O with the scalability of non-blocking I/O.

## Performance

**Creation and context-switch costs:**

| Metric | Platform Thread | Virtual Thread |
|--------|----------------|----------------|
| Creation time | ~10μs | ~0.1μs |
| Memory per thread | ~1MB | ~1KB |
| Max threads (practical) | ~10K | ~1M+ |
| Context switch | ~2-5μs (OS) | ~0.2μs (JVM) |
| Blocking cost | Wastes OS thread | Frees carrier thread |

**Throughput benchmarks (10K concurrent DB queries):**

| Approach | Throughput | Latency (p99) | Memory |
|----------|-----------|---------------|--------|
| Thread pool (200 threads) | 2K req/s | 5s | 200MB |
| Virtual threads | 10K req/s | 1s | 10MB |
| Non-blocking (Netty) | 12K req/s | 0.8s | 5MB |

**Key observations:**
- Virtual threads scale linearly with I/O tasks (unlike thread pools which plateau)
- Non-blocking I/O (Netty) is slightly faster but 10x more complex to code
- Virtual threads use ~20x less memory than thread pools for the same concurrency

**When virtual threads are faster than thread pools:**
- High-concurrency I/O (web servers, API gateways)
- Database-heavy applications
- Microservice compositions (many sequential/blocking calls)
- When thread pool tuning is impractical

## Pitfalls

- **Using `synchronized` in virtual thread code** — pins the virtual thread, blocking the carrier. Use `ReentrantLock` instead
- **Pooling virtual threads** — defeats the purpose. One virtual thread per task is the model
- **Holding `ThreadLocal` across task boundaries** — millions of virtual threads × ThreadLocal = memory leak. Use scoped values
- **CPU-bound work in virtual threads** — no benefit; use `ForkJoinPool` or fixed thread pools
- **Calling native methods** — JNI calls pin the virtual thread. Avoid or use `ReentrantLock` around native calls
- **Not monitoring pinning** — run with `-Djdk.tracePinnedThreads=full` in development
- **Assuming virtual threads fix all concurrency bugs** — race conditions, deadlocks, and data races still apply
- **Using `Thread.stop()`** — deprecated and dangerous with virtual threads. Use interruption and cancellation

## References

- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [Oracle: Virtual Threads Documentation](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html)
- [OpenJDK Loom Project](https://openjdk.org/projects/loom/)
- [Inside Java: Virtual Threads](https://inside.java/2022/03/31/loom-and-serviceloom.html)
- [Ron Pressler: Virtual Threads talk](https://openjdk.org/projects/loom/)
- [Aleksey Shipilëv: Virtual Threads benchmarks](https://shipilev.net/)
- [Baeldung: Virtual Threads Guide](https://www.baeldung.com/java-virtual-threads)
