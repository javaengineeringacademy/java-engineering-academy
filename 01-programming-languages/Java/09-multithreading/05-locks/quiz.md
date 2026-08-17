# Locks - Quiz

## Multiple Choice Questions

**1. What is the main advantage of `ReentrantLock` over `synchronized`?**
- A) ReentrantLock is faster
- B) ReentrantLock supports tryLock(), interruptibility, and timed locking
- C) ReentrantLock uses less memory
- D) ReentrantLock is reentrant while synchronized is not

**Answer: B** — `ReentrantLock` provides `tryLock()`, `lockInterruptibly()`, and timed lock acquisition, which `synchronized` does not support.

---

**2. What does `ReentrantLock.tryLock()` return if the lock is not available?**
- A) Blocks until the lock is available
- B) Throws `InterruptedException`
- C) Returns `false` immediately
- D) Returns `true`

**Answer: C** — `tryLock()` is non-blocking. It returns `true` if the lock was acquired, `false` if it's not available.

---

**3. In `ReadWriteLock`, when can a read lock be acquired?**
- A) Only when no locks are held
- B) When no write lock is held (multiple readers allowed)
- C) Only when no other read lock is held
- D) Always, regardless of write lock

**Answer: B** — Multiple threads can hold read locks simultaneously, but only when no write lock is held. A write lock requires no other locks.

---

**4. What is optimistic reading in `StampedLock`?**
- A) Reading without acquiring any lock
- B) Reading with a stamp, then validating the stamp after reading
- C) Always acquiring a read lock
- D) Reading cached values

**Answer: B** — Optimistic reads get a stamp, read the data, then validate the stamp. If valid, no lock was needed. If invalid, fall back to a pessimistic read lock.

---

**5. What happens if you forget to call `unlock()` in a `finally` block?**
- A) The lock is automatically released
- B) The lock may never be released, causing deadlock
- C) A `LockException` is thrown
- D) Nothing happens

**Answer: B** — Unlike `synchronized`, `ReentrantLock` must be manually unlocked. If `unlock()` is not called (especially in `finally`), the lock may never be released.

---

## True/False Questions

**6. `ReentrantLock` is always faster than `synchronized`.**

**Answer: False** — For simple synchronization without advanced features, `synchronized` is often faster due to JVM optimizations (biased locking, lock elision). `ReentrantLock` is preferred when advanced features are needed.

---

**7. `StampedLock` supports reentrant read locks.**

**Answer: False** — `StampedLock` does not support reentrant read locks. If a thread holding a read lock tries to acquire another read lock, it may deadlock.

---

**8. `Condition` objects are created from `Lock` instances.**

**Answer: True** — `Condition` is created via `lock.newCondition()` and is bound to that specific lock instance.

---

## Code Output Questions

**9. What is the output?**

```java
public class Quiz9 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        System.out.println("Hold count: " + lock.getHoldCount());
        lock.lock();
        System.out.println("Hold count: " + lock.getHoldCount());
        lock.lock();
        System.out.println("Hold count: " + lock.getHoldCount());
        lock.unlock();
        System.out.println("Hold count: " + lock.getHoldCount());
        lock.unlock();
        System.out.println("Hold count: " + lock.getHoldCount());
    }
}
```

**Answer:** `0, 1, 2, 1, 0` — Each `lock()` increments the hold count, each `unlock()` decrements it. The lock is only fully released when count reaches 0.

---

**10. What is the output?**

```java
public class Quiz10 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        boolean acquired = lock.tryLock();
        System.out.println("First tryLock: " + acquired);
        lock.unlock();
        acquired = lock.tryLock();
        System.out.println("Second tryLock: " + acquired);
        lock.unlock();
    }
}
```

**Answer:** `First tryLock: true, Second tryLock: true` — Both `tryLock()` calls succeed because the lock is free and we unlock after each acquire.
