# Thread Lifecycle - Quiz

## Multiple Choice Questions

**1. Which thread state is entered when a thread calls `Object.wait()`?**
- A) BLOCKED
- B) WAITING
- C) TIMED_WAITING
- D) RUNNABLE

**Answer: B** — `wait()` without a timeout puts the thread in the WAITING state. It releases the monitor and waits until another thread calls `notify()` or `notifyAll()`.

---

**2. What happens when `Thread.sleep(1000)` is called?**
- A) Thread enters WAITING state
- B) Thread enters TIMED_WAITING state
- C) Thread enters BLOCKED state
- D) Thread releases its monitor lock

**Answer: B** — `sleep()` puts the thread in TIMED_WAITING state for the specified duration. Unlike `wait()`, `sleep()` does NOT release any monitor lock.

---

**3. A thread is in BLOCKED state. What can cause this?**
- A) Calling `Thread.sleep()`
- B) Calling `Object.wait()`
- C) Trying to enter a `synchronized` block held by another thread
- D) Calling `Thread.join()`

**Answer: C** — BLOCKED state occurs only when waiting to acquire a monitor lock held by another thread.

---

**4. What is the difference between WAITING and TIMED_WAITING?**
- A) WAITING releases the lock; TIMED_WAITING does not
- B) TIMED_WAITING has an automatic timeout; WAITING does not
- C) WAITING is for daemon threads only
- D) There is no difference

**Answer: B** — TIMED_WAITING automatically transitions back to RUNNABLE after the specified time. WAITING remains until explicitly notified.

---

**5. Which method causes a thread to enter BLOCKED state?**
- A) `Thread.sleep()`
- B) `Object.wait()`
- C) Entering a `synchronized` block when the lock is held
- D) `Thread.join()`

**Answer: C** — Only contention for a monitor lock causes BLOCKED state. The other methods cause WAITING or TIMED_WAITING.

---

## True/False Questions

**6. A thread in TERMINATED state can be restarted by calling `start()` again.**

**Answer: False** — Once a thread reaches TERMINATED state, it cannot be restarted. Calling `start()` again throws `IllegalThreadStateException`.

---

**7. `Thread.yield()` causes the thread to enter WAITING state.**

**Answer: False** — `yield()` is a hint to the scheduler that the current thread is willing to give up its time slice. The thread remains in RUNNABLE state.

---

**8. `Thread.join()` causes the calling thread to enter WAITING state.**

**Answer: True** — `join()` without a timeout puts the calling thread in WAITING state until the target thread terminates. With a timeout, it enters TIMED_WAITING.

---

## Code Output Questions

**9. What is printed?**

```java
public class Quiz9 {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(() -> {
            System.out.println("A: " + Thread.currentThread().getState());
        });
        System.out.println("B: " + t.getState());
        t.start();
        t.join();
        System.out.println("C: " + t.getState());
    }
}
```

**Answer:** `B: NEW`, then `A: RUNNABLE`, then `C: TERMINATED` — A is printed when the thread runs (RUNNABLE), B is printed before start (NEW), C after join (TERMINATED).

---

**10. What happens when this code runs?**

```java
public class Quiz10 {
    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try { lock.wait(); } catch (Exception e) {}
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                try { Thread.sleep(1000); } catch (Exception e) {}
                lock.notify();
            }
        });
        t1.start(); t2.start();
        t1.join(2000); t2.join(2000);
        System.out.println("t1: " + t1.getState());
        System.out.println("t2: " + t2.getState());
    }
}
```

**Answer:** `t1: TERMINATED`, `t2: TERMINATED` — t1 waits, t2 sleeps then notifies. Both complete within the 2-second join timeout.
