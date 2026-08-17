# Thread Creation - Quiz

## Multiple Choice Questions

**1. Which is the preferred way to create a thread in Java?**
- A) Extending `Thread` class
- B) Implementing `Runnable` interface
- C) Calling `Thread.destroy()`
- D) Using `System.runFinalization()`

**Answer: B** — Implementing `Runnable` is preferred because it doesn't require extending a class, allows implementing interfaces, and separates the task from the thread.

---

**2. What does `Callable` provide that `Runnable` does not?**
- A) Ability to run in a thread
- B) A return value and checked exception propagation
- C) Thread priority support
- D) Daemon thread capability

**Answer: B** — `Callable<V>` has a `call()` method that returns a value of type `V` and can throw checked exceptions, unlike `Runnable.run()`.

---

**3. When must `setDaemon(true)` be called on a thread?**
- A) After `start()` is called
- B) After the thread begins executing
- C) Before `start()` is called
- D) At any time during thread execution

**Answer: C** — The daemon flag must be set before `start()`. Calling `setDaemon(true)` on a started thread throws `IllegalThreadStateException`.

---

**4. What is the output of `new Thread(() -> {}).start()` called twice on the same Thread object?**
- A) Both calls succeed
- B) Second call throws `IllegalThreadStateException`
- C) Second call is ignored
- D) Second call creates a new OS thread

**Answer: B** — A Thread object can only be started once. The second `start()` call throws `IllegalThreadStateException`.

---

**5. Which executor creates new threads as needed and reuses idle ones?**
- A) `Executors.newFixedThreadPool(n)`
- B) `Executors.newSingleThreadExecutor()`
- C) `Executors.newCachedThreadPool()`
- D) `Executors.newScheduledThreadPool(n)`

**Answer: C** — `newCachedThreadPool()` creates threads on demand and reuses idle ones. Threads that are idle for 60 seconds are terminated.

---

## True/False Questions

**6. Virtual threads (Java 21+) are managed by the OS kernel.**

**Answer: False** — Virtual threads are scheduled by the JVM onto carrier (platform) threads. The OS only sees the carrier threads.

---

**7. A `Runnable` lambda can throw checked exceptions.**

**Answer: False** — `Runnable.run()` does not declare checked exceptions. To handle checked exceptions, you must catch them inside the lambda or use `Callable`.

---

**8. `CompletableFuture.supplyAsync()` runs the task on a common fork-join pool by default.**

**Answer: True** — By default, `supplyAsync()` uses `ForkJoinPool.commonPool()`. You can specify a custom executor as a second parameter.

---

## Code Output Questions

**9. What is printed?**

```java
public class Quiz9 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            System.out.print("Hello ");
        });
        t.start();
        System.out.print("World");
    }
}
```

**Answer:** Either `Hello World` or `World Hello` — thread scheduling is non-deterministic, so the order depends on the scheduler.

---

**10. What is printed?**

```java
public class Quiz10 {
    public static void main(String[] args) throws Exception {
        Callable<Integer> c = () -> 42;
        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<Integer> f = ex.submit(c);
        System.out.println(f.get());
        ex.shutdown();
    }
}
```

**Answer:** `42` — The `Callable` returns 42, and `future.get()` blocks until the result is available, then prints it.
