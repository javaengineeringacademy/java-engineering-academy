# Solutions: Suppressed Exceptions in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Add Suppressed Exceptions

```java
public class Exercise1 {
    static RuntimeException createWithSuppressed(String primaryMsg, String... suppressedMsgs) {
        RuntimeException ex = new RuntimeException(primaryMsg);
        for (String msg : suppressedMsgs) {
            ex.addSuppressed(new RuntimeException(msg));
        }
        return ex;
    }

    public static void main(String[] args) {
        RuntimeException ex = createWithSuppressed("Primary", "Sup1", "Sup2");
        System.out.println("Primary: " + ex.getMessage());
        System.out.println("Suppressed: " + ex.getSuppressed().length);
        for (Throwable t : ex.getSuppressed()) {
            System.out.println("  " + t.getMessage());
        }
    }
}
```

**Output:**
```
Primary: Primary
Suppressed: 2
  Sup1
  Sup2
```

**Key points:**
- `addSuppressed()` appends to the suppressed array.
- Each suppressed exception is independent.
- The primary exception remains the main exception.

---

## Solution 2: TWR Suppressed

```java
public class Exercise2 {
    static class Resource implements AutoCloseable {
        private final String name;

        Resource(String name) { this.name = name; }

        void use() { System.out.println("Using " + name); }

        @Override
        public void close() {
            System.out.println("Closing " + name);
            if (name.contains("bad")) {
                throw new RuntimeException("Close failed for " + name);
            }
        }
    }

    public static void main(String[] args) {
        try (var good = new Resource("good");
             var bad = new Resource("bad")) {
            good.use();
            bad.use();
            throw new RuntimeException("Primary error");
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable t : e.getSuppressed()) {
                System.out.println("Suppressed: " + t.getMessage());
            }
        }
    }
}
```

**Output:**
```
Using good
Using bad
Closing bad
Closing good
Primary: Primary error
Suppressed: Close failed for bad
```

**Key points:**
- TWR automatically manages suppressed exceptions.
- `bad.close()` throws, which is added as suppressed.
- Resources close in reverse declaration order.

---

## Solution 3: Count Suppressed

```java
public class Exercise3 {
    static int totalExceptions(Throwable t) {
        return 1 + t.getSuppressed().length;
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("Primary");
        ex.addSuppressed(new RuntimeException("S1"));
        ex.addSuppressed(new RuntimeException("S2"));
        System.out.println("Total: " + totalExceptions(ex));
    }
}
```

**Output:**
```
Total: 3
```

**Key points:**
- Start with 1 for the primary exception.
- `getSuppressed().length` gives the count of suppressed exceptions.
- The total includes all exceptions in the chain.
