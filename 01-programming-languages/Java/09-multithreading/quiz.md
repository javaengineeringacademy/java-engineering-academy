# Multithreading Quiz

## Question 1 (Production Scenario)
Your web server needs to handle 10,000 concurrent connections. Each connection requires minimal processing (read request, send response). The current implementation creates a new thread per connection and crashes under load. How should you redesign the threading model?

- A) Create a new thread for each connection (current approach)
- B) Use a fixed thread pool with a size equal to the number of CPU cores
- C) Use a cached thread pool or virtual threads (Java 21+) to handle concurrent I/O efficiently
- D) Use a single thread for all connections

**Answer: C**
**Explanation:** I/O-bound operations (like network I/O) benefit from many threads. A cached thread pool creates threads as needed, and virtual threads (Java 21+) handle millions of concurrent connections with minimal overhead. Fixed thread pools are better for CPU-bound work, not I/O-bound servers.

---

## Question 2 (Production Scenario)
A financial trading system processes transactions. Each transaction updates multiple shared counters atomically. The current implementation uses `synchronized` blocks, causing contention under high load. Which alternative reduces contention?

- A) Use `volatile` variables instead of `synchronized`
- B) Use `AtomicLong` with `compareAndSet()` for lock-free updates
- C) Use `Thread.sleep()` between updates
- D) Use `wait()` and `notify()` for coordination

**Answer: B**
**Explanation:** `AtomicLong` with CAS (compare-and-swap) operations provides lock-free thread safety. Unlike `synchronized`, it doesn't block threads — it retries on contention. `volatile` only ensures visibility, not atomicity for compound operations. For high-contention counters, atomic classes outperform synchronized blocks.

---

## Question 3 (Debugging)
A production application throws `ConcurrentModificationException` intermittently. The code:

```java
List<String> items = new ArrayList<>();
// Thread 1: items.add("A");
// Thread 2: for (String s : items) { System.out.println(s); }
```

What is the bug?

- A) `ArrayList` is not thread-safe — use `CopyOnWriteArrayList` for concurrent read/write
- B) The for-each loop should use an index instead
- C) Add `synchronized` around both operations
- D) Use `Vector` instead of `ArrayList`

**Answer: A**
**Explanation:** `ArrayList` is not thread-safe. Concurrent modification during iteration throws `ConcurrentModificationException`. `CopyOnWriteArrayList` creates a new copy of the array on each write, allowing safe iteration without locking. For read-heavy scenarios, this provides excellent concurrency.

---

## Question 4 (Production Scenario)
You are building a producer-consumer system where producers add items to a buffer and consumers process them. The buffer has a fixed capacity of 1000 items. Producers can produce faster than consumers can consume. Which concurrent collection is best?

- A) `ArrayList` with synchronized methods
- B) `BlockingQueue` (e.g., `ArrayBlockingQueue`)
- C) `HashMap` with locks
- D) `LinkedList` with manual synchronization

**Answer: B**
**Explanation:** `BlockingQueue` provides built-in thread safety with `put()` blocking when full and `take()` blocking when empty. This eliminates the need for manual wait/notify coordination, reducing complexity and potential bugs. It's specifically designed for producer-consumer patterns.

---

## Question 5 (Code Output)
What does this code print?

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.print("Worker ");
            }
        });

        t.start();
        t.join();

        for (int i = 0; i < 3; i++) {
            System.out.print("Main ");
        }
    }
}
```

**Answer:** Worker Worker Worker Main Main Main
**Explanation:** `t.join()` causes the main thread to wait until thread `t` completes. So all "Worker" prints happen first, followed by all "Main" prints.

---

## Question 6 (Code Output)
What does this code print?

```java
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> future1 = executor.submit(() -> 10 + 20);
        Future<Integer> future2 = executor.submit(() -> 30 + 40);

        System.out.println(future1.get() + future2.get());

        executor.shutdown();
    }
}
```

**Answer:** 100
**Explanation:** future1 returns 30 (10+20), future2 returns 70 (30+40). `get()` retrieves the results, so 30 + 70 = 100.

---

## Question 7 (Bug Finding)
Find the bug:

```java
public class Counter {
    private int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Thread[] threads = new Thread[100];

        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println(counter.getCount()); // Expected: 100000
    }
}
```

**Bug:** Race condition — `count++` is not atomic. Multiple threads can read the same value, increment it, and write back simultaneously, causing lost updates.
**Fix:** Use synchronization or AtomicInteger:
```java
private final AtomicInteger count = new AtomicInteger(0);

public void increment() {
    count.incrementAndGet();
}
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            System.out.println("Task 1");
            throw new RuntimeException("Error");
        });

        executor.submit(() -> {
            System.out.println("Task 2");
        });

        executor.shutdown();
    }
}
```

**Bug:** When a task throws an uncaught exception in a thread pool, the thread dies silently. The second task may or may not execute depending on timing. The exception is swallowed without logging.
**Fix:** Add exception handling:
```java
executor.submit(() -> {
    try {
        System.out.println("Task 1");
        throw new RuntimeException("Error");
    } catch (Exception e) {
        e.printStackTrace();
    }
});
```

---

## Question 9 (Scenario-based)
You need to implement a producer-consumer pattern where producers add items to a buffer and consumers process them. The buffer has a fixed capacity. Which concurrent collection is best?

- A) ArrayList with synchronized methods
- B) BlockingQueue (e.g., ArrayBlockingQueue)
- C) HashMap with locks
- D) LinkedList with manual synchronization

**Answer: B**
**Explanation:** BlockingQueue provides built-in thread safety with `put()` blocking when full and `take()` blocking when empty. This eliminates the need for manual wait/notify coordination, reducing complexity and potential bugs.

---

## Question 10 (Architecture Decision)
You are building a web server that needs to handle 10,000 concurrent connections. Each connection requires minimal processing (read request, send response). How should you design the threading model?

- A) Create a new thread for each connection
- B) Use a fixed thread pool with a size equal to the number of CPU cores
- C) Use a cached thread pool or virtual threads (Java 21+) to handle concurrent I/O efficiently
- D) Use a single thread for all connections

**Answer: C**
**Explanation:** I/O-bound operations (like network I/O) benefit from many threads. A cached thread pool creates threads as needed, and virtual threads (Java 21+) handle millions of concurrent connections with minimal overhead. Fixed thread pools are better for CPU-bound work.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.print("Running ");
        };

        Thread t = new Thread(task);
        t.start();
        t.run();

        System.out.print("Done");
    }
}
```

A) Running Running Done
B) Running Done Running
C) Done Running Running
D) Running Running

**Answer: A**
**Explanation:** `t.start()` creates a new thread and calls run() in that thread — prints "Running ". `t.run()` is a direct method call in the main thread (not creating a new thread) — prints "Running ". Then "Done" prints. Note: the first "Running" might print before or after "Done" due to thread scheduling, but typically start() creates a thread that runs concurrently. The most likely output is `Running Running Done`.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Object lock = new Object();
        synchronized (lock) {
            System.out.print("A ");
            lock.wait(100);
            System.out.print("B ");
        }
        System.out.print("C");
    }
}
```

A) A B C
B) A C
C) A (then blocks indefinitely)
D) Compilation error

**Answer: A**
**Explanation:** `lock.wait(100)` releases the lock and waits for up to 100 milliseconds. After 100ms (or when notified), it reacquires the lock and continues. Since no other thread notifies, it waits 100ms then resumes, printing "B ". Then "C" prints. Output: `A B C` (with a ~100ms delay between A and B).

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(() -> System.out.print("A "));
        executor.execute(() -> System.out.print("B "));

        Future<String> future = executor.submit(() -> {
            Thread.sleep(50);
            return "C";
        });

        System.out.println(future.get() + " Done");

        executor.shutdown();
    }
}
```

A) A B C Done
B) A B Done C
C) A B (then blocks until C is ready)
D) A B C Done (with A and B before C)

**Answer: D**
**Explanation:** `execute()` submits two Runnable tasks that print "A " and "B " (order may vary). `submit()` returns a Future for a task that sleeps 50ms then returns "C". `future.get()` blocks until "C" is ready. So A and B print first (concurrent), then "C Done" prints. Output: `A B C Done` (A and B order may swap).

