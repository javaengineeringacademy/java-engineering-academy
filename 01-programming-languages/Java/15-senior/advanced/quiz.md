# Advanced Java Quiz

## Question 1 (Code Output)
What does this CompletableFuture chain print?

```java
CompletableFuture.supplyAsync(() -> "hello")
    .thenApply(s -> s.toUpperCase())
    .thenCombine(
        CompletableFuture.supplyAsync(() -> "world"),
        (a, b) -> a + " " + b
    )
    .thenAccept(System.out::println);
```

A) HELLO world
B) hello WORLD
C) HELLO WORLD
D) Compilation error

**Answer: C**
**Explanation:** `thenApply` transforms "hello" to "HELLO". `thenCombine` joins with "world" (no transformation). Result: "HELLO world". Actually, `thenCombine` uses the second future's raw value "world", so the result is "HELLO world". Wait — let me reconsider. `supplyAsync(() -> "world")` returns "world" (lowercase). `thenCombine` concatenates "HELLO" + " " + "world" = "HELLO world". The answer should be A: HELLO world.

**Corrected Answer: A**
**Explanation:** `thenApply(s -> s.toUpperCase())` converts "hello" to "HELLO". `thenCombine` combines with the second future's result "world" (lowercase, no transformation applied). Final output: "HELLO world".

---

## Question 2 (Bug Finding)
This virtual thread code can cause a thread leak. Find the bug.

```java
public class LeakyServer {
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public static void handleRequest() {
        executor.submit(() -> {
            Thread t = Thread.currentThread();
            System.out.println("Handling: " + t.getName());
            // Simulate long-running work
            try {
                while (true) {
                    Thread.sleep(1000);
                    processChunk();
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        });
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) handleRequest();
    }
}
```

A) No bug — virtual threads are lightweight and unlimited
B) The executor is never shut down, preventing clean JVM exit
C) Virtual threads block on sleep() causing carrier thread starvation
D) The infinite while loop creates unbounded virtual threads

**Answer: D**
**Explanation:** Each call to `handleRequest` creates a new virtual thread that runs forever (infinite `while(true)` loop). With 100 calls, 100 virtual threads are permanently alive. While virtual threads are cheap, they still consume memory for their stack. The real issue is unbounded thread creation — the method has no mechanism to stop or limit concurrent tasks.

---

## Question 3 (Code Output)
What is the output of this lock-free counter?

```java
public class Counter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();
    }
    
    public int get() {
        return count.get();
    }
}

// Client:
Counter counter = new Counter();
IntStream.range(0, 1000).parallel().forEach(i -> counter.increment());
System.out.println(counter.get());
```

A) 1000 (always)
B) Less than 1000 (race condition)
C) 0 (never incremented)
D) Throws ConcurrentModificationException

**Answer: A**
**Explanation:** `AtomicInteger.incrementAndGet()` is atomic and thread-safe. It uses CAS (Compare-And-Swap) internally, ensuring each increment is applied exactly once regardless of concurrency. Parallel streams with 1000 tasks will always produce 1000.

---

## Question 4 (Architecture)
You need to implement a distributed lock for a payment service. Which approach is most appropriate?

A) `synchronized` keyword on the payment method
B) Redis-based lock with TTL using Redlock algorithm
C) Database row-level lock with SELECT FOR UPDATE
D) File-based lock using `java.nio.channels.FileLock`

**Answer: B**
**Explanation:** Distributed locks require coordination across multiple JVM instances. `synchronized` only works within a single JVM. Database locks add latency and don't work across services. File locks don't work in containerized environments. Redlock provides a Redis-based distributed lock with TTL (automatic expiration), handling network partitions and process failures.

---

## Question 5 (Code Output)
What does this structured concurrency code print?

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> a = scope.fork(() -> { Thread.sleep(100); return "A"; });
    Subtask<String> b = scope.fork(() -> { Thread.sleep(50); return "B"; });
    
    scope.join();
    
    System.out.println(a.get() + " " + b.get());
}
```

A) A B (after ~100ms)
B) B A (order may vary)
C) Compilation error
D) Throws exception from shorter task

**Answer: A**
**Explanation:** `ShutdownOnFailure` waits for all subtasks. Both complete successfully. `a` finishes after 100ms, `b` after 50ms. After `join()`, both results are available. The output is "A B" (both results printed).

---

## Question 6 (Bug Finding)
Find the deadlock in this producer-consumer code.

```java
public class BuggyQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final int capacity;
    
    public BuggyQueue(int capacity) {
        this.capacity = capacity;
    }
    
    public synchronized void produce(String item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(item);
    }
    
    public synchronized String consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }
}
```

A) No deadlock — the code is correct
B) Both methods are synchronized on the same object, so `wait()` in `produce` blocks `consume` from running
C) `notifyAll()` is missing, so waiting threads never wake up
D) LinkedList is not thread-safe

**Answer: C**
**Explanation:** When `produce` calls `wait()`, it releases the monitor. However, after adding an item, `produce` exits without calling `notifyAll()`. Similarly, `consume` never notifies. Threads waiting in `wait()` will never be woken up. The fix is to call `notifyAll()` after each modification to the queue.

---

## Question 7 (Scenario)
Your Spring Boot application uses CompletableFuture with a ForkJoinPool. Under load, response times degrade significantly. What is the most likely cause?

A) ForkJoinPool has a fixed thread pool that's too small
B) CompletableFuture defaults to ForkJoinPool.commonPool(), which is shared across the JVM and can be saturated
C) CompletableFuture doesn't support async execution
D) Spring Boot doesn't integrate with ForkJoinPool

**Answer: B**
**Explanation:** `CompletableFuture.supplyAsync()` without an explicit executor uses `ForkJoinPool.commonPool()`. This pool is shared across the entire JVM (all CompletableFuture, parallel streams, etc.). Under heavy load, the common pool becomes a bottleneck. Solution: provide a dedicated ExecutorService with appropriate sizing.

---

## Question 8 (Code Output)
What is the output?

```java
var lock = new ReentrantReadWriteLock();
var writeLock = lock.writeLock();
var readLock = lock.readLock();

readLock.lock();
try {
    System.out.print("R1 ");
    readLock.lock();
    try {
        System.out.print("R2 ");
    } finally {
        readLock.unlock();
    }
} finally {
    readLock.unlock();
}
System.out.print("R3 ");

writeLock.lock();
try {
    System.out.print("W1");
} finally {
    writeLock.unlock();
}
```

A) R1 R2 R3 W1
B) R1 R2 W1
C) R1 W1 (deadlock after R1)
D) R1 R2 R3 (deadlock before W1)

**Answer: A**
**Explanation:** Read locks are reentrant — a thread holding a read lock can acquire another read lock. So R1 and R2 print. After releasing both read locks, R3 prints. Then the write lock is acquired (no readers), and W1 prints.

---

## Question 9 (Bug Finding)
This scoped value code has a subtle bug. Find it.

```java
public class UserContext {
    private static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();
    
    public static void runAsUser(String user, Runnable action) {
        ScopedValue.where(CURRENT_USER, user).run(action);
    }
    
    public static String getCurrentUser() {
        return CURRENT_USER.get();
    }
    
    public static void main(String[] args) {
        runAsUser("Alice", () -> {
            System.out.println(getCurrentUser());
            CompletableFuture.runAsync(() -> {
                System.out.println(getCurrentUser());
            });
        });
    }
}
```

A) Prints "Alice" twice
B) Prints "Alice" then throws exception — ScopedValue doesn't cross async boundaries
C) Prints "null" twice
D) Compilation error

**Answer: B**
**Explanation:** ScopedValue is bound to the current thread's scope. When `CompletableFuture.runAsync` executes on a different thread, the scoped value is not inherited. `getCurrentUser()` inside the async task throws `ScopedValue.ScopedValueAccessException` because the scope is not bound on that thread.

---

## Question 10 (Architecture)
You are designing a system that must process 50,000 concurrent WebSocket connections. Which thread model is most appropriate?

A) One thread per connection (traditional Java NIO)
B) Virtual threads with blocking I/O
C) Event loop with Netty (single-threaded reactor)
D) Fixed thread pool with CompletableFuture

**Answer: B**
**Explanation:** Virtual threads handle blocking I/O efficiently without the overhead of platform thread per connection. With 50K connections, platform threads would exhaust memory. Event loops (Netty) work but require non-blocking callback-based code. Virtual threads allow straightforward blocking code with the scalability of async I/O — the best of both worlds for this use case.
