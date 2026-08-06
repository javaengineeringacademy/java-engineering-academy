# Senior Java Engineer Quiz

## Question 1 (Code Output)
What is the output of this JMH benchmark snippet?

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class MyBenchmark {
    @Benchmark
    public int measureSum() {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }
}
```

A) Compiles and runs, result reported in ns/op
B) Compilation error — @BenchmarkMode is misplaced
C) Compilation error — @Fork value must be 2 or more
D) Runs but throws RuntimeException at startup

**Answer: A**
**Explanation:** All annotations are correctly placed at the class level. @BenchmarkMode, @OutputTimeUnit, @Warmup, @Measurement, and @Fork are all valid class-level JMH annotations. The benchmark compiles and produces timing results in nanoseconds per operation.

---

## Question 2 (Code Output)
What does the following virtual threads code print?

```java
public class Main {
    public static void main(String[] args) throws Exception {
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 3; i++) {
            int id = i;
            threads.add(Thread.ofVirtual().start(() -> {
                System.out.println("Task " + id + " on " + Thread.currentThread().getThreadClass().getSimpleName());
            }));
        }
        for (var t : threads) t.join();
        System.out.println("All done");
    }
}
```

A) Prints "Task 0/1/2 on VirtualThread" then "All done"
B) Prints "Task 0/1/2 on PlatformThread" then "All done"
C) Compilation error — getThreadClass() does not exist
D) Throws IllegalThreadStateException

**Answer: A**
**Explanation:** `Thread.ofVirtual().start()` creates virtual threads. `getThreadClass()` returns `VirtualThread.class`, whose simple name is "VirtualThread". All three tasks execute and print before "All done" because join() waits for completion.

---

## Question 3 (Bug Finding)
A developer uses a circuit breaker to protect a remote API call. The circuit opens after 3 failures. What is the bug in this code?

```java
public class CircuitBreaker {
    private int failureCount = 0;
    private State state = State.CLOSED;
    
    public String call(Supplier<String> apiCall) {
        if (state == State.OPEN) {
            throw new RuntimeException("Circuit is open");
        }
        try {
            String result = apiCall.get();
            failureCount = 0;
            return result;
        } catch (Exception e) {
            failureCount++;
            if (failureCount >= 3) {
                state = State.OPEN;
            }
            throw e;
        }
    }
    
    enum State { CLOSED, OPEN }
}
```

A) The circuit never opens because failureCount is reset on success
B) There is no timeout on the API call, so it can hang forever
C) The circuit never transitions back from OPEN to CLOSED (half-open state missing)
D) failureCount should be AtomicInteger for thread safety

**Answer: C**
**Explanation:** The circuit correctly opens after 3 consecutive failures, but there is no mechanism to transition back to CLOSED or HALF_OPEN. Once OPEN, it stays OPEN permanently. A real circuit breaker needs a timeout to allow trial requests after a cooldown period.

---

## Question 4 (Code Output)
What is the output of this rate limiter using a sliding window?

```java
public class RateLimiter {
    private final int maxRequests;
    private final long windowMillis;
    private final Deque<Long> timestamps = new ArrayDeque<>();

    public RateLimiter(int maxRequests, long windowMillis) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
    }

    public synchronized boolean allow() {
        long now = System.currentTimeMillis();
        while (!timestamps.isEmpty() && timestamps.peekFirst() < now - windowMillis) {
            timestamps.pollFirst();
        }
        if (timestamps.size() < maxRequests) {
            timestamps.addLast(now);
            return true;
        }
        return false;
    }
}

// Client code:
var limiter = new RateLimiter(2, 1000);
System.out.print(limiter.allow() + " ");
System.out.print(limiter.allow() + " ");
System.out.print(limiter.allow() + " ");
```

A) true true true
B) true true false
C) false false false
D) true false false

**Answer: B**
**Explanation:** The first call succeeds (1 of 2 slots used). The second succeeds (2 of 2 slots used). The third fails because the window hasn't expired and both slots are occupied. The window is 1 second, so calls within that window are counted.

---

## Question 5 (Scenario)
You are migrating a monolithic Spring Boot app to microservices. The order service calls inventory, payment, and notification services. During a payment service outage, orders fail completely. Which pattern addresses this?

A) API Gateway — route requests more efficiently
B) Saga Pattern — manage distributed transactions with compensating actions
C) Circuit Breaker — prevent cascading failures but orders still can't complete
D) CQRS — separate read/write models

**Answer: B**
**Explanation:** The Saga Pattern manages distributed transactions by defining compensating actions for each step. If payment fails, the saga can roll back the inventory reservation and still record the order as pending. This ensures eventual consistency without requiring all services to be available simultaneously.

---

## Question 6 (Code Output)
What is the output of this code using the Java Flight Recorder API?

```java
public class Main {
    public static void main(String[] args) throws Exception {
        Recording recording = new Recording();
        recording.setDuration(Duration.ofSeconds(2));
        recording.enable("jdk.GarbageCollection");
        recording.start();
        
        // Simulate GC pressure
        for (int i = 0; i < 100_000; i++) {
            byte[] arr = new byte[1024];
        }
        
        recording.stop();
        recording.dump(Path.of("/tmp/jfr_dump.jfr"));
        System.out.println("Recording dumped successfully");
    }
}
```

A) Prints "Recording dumped successfully" and creates the JFR file
B) Compilation error — Recording class not in java.lang
C) Throws SecurityException because JFR requires elevated privileges
D) Prints "Recording dumped successfully" but the file is empty

**Answer: A**
**Explanation:** `jdk.jfr.Recording` is available in the `jdk.jfr` module (included in standard JDK). The code enables GC event recording, generates garbage collection pressure, stops the recording, and dumps it to disk. The file contains the recorded GC events.

---

## Question 7 (Bug Finding)
This code uses CompletableFuture but can deadlock. Find the bug.

```java
public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return fetchData();
        }, pool);
        
        String result = future.thenApply(data -> {
            return data.toUpperCase();
        }).join();  // main thread blocks
        
        System.out.println(result);
        pool.shutdown();
    }
    
    static String fetchData() {
        return "hello";
    }
}
```

A) No deadlock — it works correctly
B) Deadlock because the single-thread pool is consumed by supplyAsync, and thenApply cannot execute
C) thenApply throws CompletionException because it runs on the main thread
D) join() throws CancellationException

**Answer: B**
**Explanation:** With a single-thread pool, `supplyAsync` occupies the only thread. `thenApply` needs to run on the same thread pool to complete the chain, but the thread is blocked waiting for `supplyAsync` to finish. Since both stages run on the same pool and there's only 1 thread, `thenApply` is queued but never runs — classic thread pool deadlock.

---

## Question 8 (Architecture)
You need to design a system that processes 1 million events per second with exactly-once semantics. Which architecture is most appropriate?

A) Synchronous REST API with a message queue
B) Event-driven microservices with Kafka using consumer groups and idempotent processing
C) Shared-nothing architecture with in-memory caches
D) Request-reply pattern with database polling

**Answer: B**
**Explanation:** Kafka provides partitioned, ordered logs with consumer groups for horizontal scaling. Exactly-once semantics are achieved through idempotent producers (deduplication), transactional messaging, and consumer-side idempotency (e.g., database upserts with unique keys). This architecture scales to millions of events/sec across partitions.

---

## Question 9 (Scenario)
A production JVM is experiencing intermittent pauses of 10-30ms under load. You suspect GC. Which combination of JFR events and JDK tools would you use to diagnose?

A) jstat -gcutil <pid> and VisualVM heap dump
B) JFR recording with jdk.GarbageCollection, jdk.GCHeapSummary events + GC logs with -Xlog:gc*
C) jmap -heap <pid> and thread dump via jstack
D) JFR recording with only jdk.Executor events

**Answer: B**
**Explanation:** JFR captures low-overhead, production-safe GC data: `jdk.GarbageCollection` (pause times, causes), `jdk.GCHeapSummary` (heap before/after). Combined with `-Xlog:gc*` for detailed GC logs, this provides complete visibility into pause causes (young GC, full GC, mixed GC), heap occupancy, and allocation rates — without the stop-the-world overhead of heap dumps.

---

## Question 10 (Code Output)
What is the output of this virtual thread code with structured concurrency?

```java
import jdk.incubator.concurrent.StructuredTaskScope;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> user = scope.fork(() -> fetchUser());
            Subtask<Integer> order = scope.fork(() -> fetchOrder());
            
            scope.join();
            
            System.out.println(user.get() + " " + order.get());
        }
    }
    
    static String fetchUser() { return "Alice"; }
    static int fetchOrder() { return 42; }
}
```

A) Prints "Alice 42"
B) Compilation error — Subtask is not a public API (preview)
C) Throws IllegalStructuredStateException
D) Prints "null null"

**Answer: B**
**Explanation:** `StructuredTaskScope` and `Subtask` are part of the Structured Concurrency API, which was incubating/preview in JDK 21 and not yet a final API. The code requires `--add-modules jdk.incubator.concurrent` and may not compile on standard JDK without enabling preview features. The design is correct but uses a preview API.
