# Synchronization (Part 2)

[📖 Back to Part 1](README.md)

---

## Advanced Concepts

### Double-Checked Locking (DCL)

Lazy initialization with minimal synchronization overhead:

```java
public class Singleton {
    private static volatile Singleton instance; // volatile is essential!

    public static Singleton getInstance() {
        if (instance == null) { // First check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) { // Second check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

Without `volatile`, the JIT compiler may reorder instructions, allowing another thread to see a partially constructed object.

### Immutable Objects for Thread Safety

The simplest way to achieve thread safety is to use immutable objects:

```java
public final class Money {
    private final int amount;
    private final String currency;

    public Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public int getAmount() { return amount; }
    public String getCurrency() { return currency; }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException("Different currencies");
        return new Money(this.amount + other.amount, this.currency);
    }
}
```

### CAS (Compare-And-Swap) Algorithms

Atomic classes use CAS for lock-free operations:

```
CAS Loop (simplified):
1. Read current value (old)
2. Compute new value
3. Attempt: CAS(expected=old, new=computed)
4. If success: done
5. If fail (value changed): retry from step 1

This is called a CAS loop or spin-lock pattern.
```

### False Sharing Solutions

```java
// Solution 1: @Contended annotation (Java 8+)
@sun.misc.Contended
class PaddedAtomicLong {
    private AtomicLong value = new AtomicLong(0);
}

// Solution 2: Manual padding
class PaddedLong {
    long p1, p2, p3, p4, p5, p6, p7;
    volatile long value;
    long p8, p9, p10, p11, p12, p13, p14;
}
```

### ReadWriteLock Fairness

`ReentrantReadWriteLock` supports fairness:

```java
// Fair lock: longest-waiting thread gets the lock
ReentrantReadWriteLock fairLock = new ReentrantReadWriteLock(true);

// Unfair lock (default): throughput optimized
ReentrantReadWriteLock unfairLock = new ReentrantReadWriteLock(false);
```

Fair locks prevent starvation but have lower throughput.

---

[📖 Back to Part 1](README.md)
