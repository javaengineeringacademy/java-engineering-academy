# Multithreading Quiz

## Question 1 (MCQ)
What is the difference between start() and run() methods?
- A) start() creates a new thread and calls run(); run() executes in the current thread
- B) start() calls run() directly; run() creates a new thread
- C) Both create new threads
- D) Both execute in the current thread

**Answer: A**
**Explanation:** `start()` creates a new OS thread and invokes `run()` in that thread. `run()` is just a regular method call in the current thread — it does not create any new thread.

---

## Question 2 (MCQ)
What does the volatile keyword guarantee?
- A) Atomicity of compound operations
- B) Visibility of writes to all threads and prevents instruction reordering
- C) Mutual exclusion for synchronized blocks
- D) Thread-safe iteration over collections

**Answer: B**
**Explanation:** `volatile` ensures that reads and writes to a variable go directly to main memory, not CPU caches. It prevents instruction reordering but does NOT provide atomicity for compound operations like `count++`.

---

## Question 3 (MCQ)
What is the difference between wait() and sleep()?
- A) wait() releases the object lock; sleep() does not
- B) wait() does not release the lock; sleep() does
- C) Both release the lock
- D) Neither releases the lock

**Answer: A**
**Explanation:** `wait()` releases the monitor lock on the object and waits until notified. `sleep()` pauses the thread for a specified time but does NOT release any locks it holds.

---

## Question 4 (MCQ)
What is a deadlock?
- A) A thread that runs too slowly
- B) Two or more threads waiting for each other's locks indefinitely
- C) A thread that throws an exception
- D) A thread that is interrupted

**Answer: B**
**Explanation:** Deadlock occurs when Thread A holds Lock1 and waits for Lock2, while Thread B holds Lock2 and waits for Lock1. Both threads are stuck forever in a circular dependency.

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
