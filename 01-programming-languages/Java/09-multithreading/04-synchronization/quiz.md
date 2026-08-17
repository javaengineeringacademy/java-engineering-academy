# Synchronization - Quiz

## Multiple Choice Questions

**1. What problem does `volatile` solve?**
- A) Atomicity of compound operations
- B) Memory visibility across threads
- C) Mutual exclusion
- D) Thread scheduling

**Answer: B** — `volatile` ensures that writes to a variable are immediately visible to all threads. It does NOT provide atomicity for compound operations like `i++`.

---

**2. Which is true about `synchronized` blocks?**
- A) They allow multiple threads to enter simultaneously
- B) They use reentrant (recursive) locking
- C) They do not release locks on exceptions
- D) They are faster than volatile

**Answer: B** — Java's `synchronized` uses reentrant locks. A thread that holds a lock can re-enter the same synchronized block without deadlocking itself.

---

**3. What is a race condition?**
- A) When threads run too fast
- B) When the program outcome depends on unpredictable thread scheduling
- C) When a thread is garbage collected
- D) When threads use too much memory

**Answer: B** — A race condition occurs when the correctness of a program depends on the relative timing of multiple threads.

---

**4. What does `AtomicInteger.incrementAndGet()` guarantee?**
- A) Only visibility
- B) Atomicity and visibility
- C) Mutual exclusion
- D) Thread scheduling

**Answer: B** — `AtomicInteger` uses hardware-level CAS (Compare-And-Swap) operations to provide both atomicity and visibility without locking.

---

**5. Why use a private lock object instead of `this`?**
- A) Private locks are faster
- B) Prevents external code from acquiring your lock
- C) Private locks use less memory
- D) Private locks are reentrant

**Answer: B** — Using `this` as a lock allows external code to synchronize on the same object, potentially causing deadlocks. Private locks keep synchronization internal.

---

## True/False Questions

**6. A `volatile` variable can be used to implement a thread-safe counter.**

**Answer: False** — `volatile` only ensures visibility, not atomicity. `count++` is a read-modify-write operation that requires `synchronized` or `AtomicInteger`.

---

**7. `synchronized` methods lock on the object instance (`this`).**

**Answer: True** — A `synchronized` instance method locks on `this`. A `synchronized` static method locks on the `Class` object.

---

**8. `AtomicInteger` uses locks internally.**

**Answer: False** — `AtomicInteger` uses hardware-level CAS (Compare-And-Swap) operations, not locks. This makes it lock-free and generally faster than `synchronized`.

---

## Code Output Questions

**9. What is the output?**

```java
public class Quiz9 {
    private static int count = 0;

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) count++;
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println(count);
    }
}
```

**Answer:** Less than 10000 — `count++` is not atomic, so multiple threads incrementing simultaneously causes lost updates (race condition).

---

**10. What is the output?**

```java
public class Quiz10 {
    private static volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(() -> {
            while (running) { /* spin */ }
            System.out.println("Stopped");
        });
        t.start();
        Thread.sleep(100);
        running = false;
        t.join();
        System.out.println("Done");
    }
}
```

**Answer:** `Stopped` then `Done` — `volatile` ensures the worker thread sees the `running = false` write, so it exits the loop and prints "Stopped".
