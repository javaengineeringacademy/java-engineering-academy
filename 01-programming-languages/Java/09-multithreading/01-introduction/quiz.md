# Introduction to Multithreading - Quiz

## Multiple Choice Questions

**1. What is the primary benefit of multithreading over multiprocessing?**
- A) Lower memory usage per task
- B) Faster context switching
- C) Better isolation between tasks
- D) Simpler debugging

**Answer: B** — Threads share memory space, so context switching between threads is faster (1-10μs) than between processes (10-100μs).

---

**2. Which statement about Java threads is correct?**
- A) Java threads are always mapped 1:1 to OS threads
- B) Java threads are always green threads
- C) Virtual threads are multiplexed onto carrier threads
- D) Daemon threads always run at higher priority

**Answer: C** — Virtual threads (Java 21+) are scheduled onto carrier (platform) threads by the JVM, allowing millions of virtual threads on few carriers.

---

**3. What happens when all non-daemon threads finish in a Java application?**
- A) The JVM continues running daemon threads
- B) The JVM waits for all threads including daemon threads
- C) The JVM terminates
- D) The JVM calls System.exit(0)

**Answer: C** — The JVM shuts down when only daemon threads remain. Daemon threads are background services (like GC) that don't prevent JVM exit.

---

**4. What is the difference between concurrency and parallelism?**
- A) Concurrency requires multiple cores; parallelism does not
- B) Concurrency is about structure; parallelism is about execution
- C) They are the same thing
- D) Parallelism is about structure; concurrency is about execution

**Answer: B** — Concurrency deals with handling multiple tasks at once (structure), while parallelism is literally executing multiple tasks simultaneously (requires multiple cores).

---

**5. Which is NOT a reason to use multithreading?**
- A) Improving I/O-bound application throughput
- B) Making CPU-bound code run faster on single-core machines
- C) Maintaining UI responsiveness during background processing
- D) Utilizing multiple CPU cores

**Answer: B** — On a single-core machine, multithreading doesn't make CPU-bound code faster—it adds overhead from context switching.

---

## True/False Questions

**6. Each Java thread has its own heap memory.**

**Answer: False** — All threads in a JVM share the same heap memory. Each thread has its own stack, program counter, and thread-local storage.

---

**7. Calling `Thread.start()` more than once on the same Thread object throws `IllegalThreadStateException`.**

**Answer: True** — A thread can only be started once. Calling `start()` on a thread that has already been started throws `IllegalThreadStateException`.

---

**8. The main thread in a Java application has the highest priority by default.**

**Answer: False** — The main thread has a default priority of 5 (`Thread.NORM_PRIORITY`), same as all newly created threads.

---

## Code Output Questions

**9. What is the output of the following code?**

```java
public class Quiz9 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            System.out.print("A");
        });
        t.start();
        System.out.print("B");
    }
}
```

**Answer:** The output could be either `AB` or `BA`. Thread scheduling is non-deterministic—the main thread and the new thread may execute in either order.

---

**10. What is the output of the following code?**

```java
public class Quiz10 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.print("X");
        });
        t.start();
        t.join();
        System.out.print("Y");
    }
}
```

**Answer:** The output is always `XY`. `t.join()` blocks the main thread until thread `t` completes, so `X` is always printed before `Y`.
