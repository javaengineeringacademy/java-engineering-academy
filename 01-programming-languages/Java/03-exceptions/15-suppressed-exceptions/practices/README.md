# Exercises: Suppressed Exceptions in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Add Suppressed Exceptions

### Problem

Write a method `createWithSuppressed(String primaryMsg, String... suppressedMsgs)` that creates a `RuntimeException` with the primary message and adds each suppressed message as a suppressed exception.

### Starter Code

```java
public class Exercise1 {
    static RuntimeException createWithSuppressed(String primaryMsg, String... suppressedMsgs) {
        // TODO: Create primary exception, add suppressed exceptions
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

### Expected Output

```
Primary: Primary
Suppressed: 2
  Sup1
  Sup2
```

### Hints

1. Create the primary exception first.
2. Loop over `suppressedMsgs` and call `addSuppressed()`.
3. Return the primary exception.

---

## Exercise 2: TWR Suppressed

### Problem

Create a `Resource` class implementing `AutoCloseable` that throws `RuntimeException` on `close()` if the resource name contains `"bad"`. Use TWR to demonstrate suppressed exceptions.

### Starter Code

```java
public class Exercise2 {
    // TODO: Create Resource implementing AutoCloseable
    // Constructor takes a name
    // use() prints "Using <name>"
    // close() throws RuntimeException if name contains "bad"

    public static void main(String[] args) {
        // TODO: Use TWR with Resource("good") and Resource("bad")
        // Throw primary exception in try body
        // Catch and print primary + suppressed exceptions
    }
}
```

### Expected Output

```
Using good
Using bad
Closing bad
Closing good
Primary: Primary error
Suppressed: Close failed for bad
```

### Hints

1. `close()` checks `name.contains("bad")`.
2. If true, throw `new RuntimeException("Close failed for " + name)`.
3. In main, use TWR with both resources.
4. Throw a primary exception in the try body.

---

## Exercise 3: Count Suppressed

### Problem

Write a method `totalExceptions(Throwable t)` that returns the total number of exceptions: 1 for the primary plus all suppressed exceptions.

### Starter Code

```java
public class Exercise3 {
    static int totalExceptions(Throwable t) {
        // TODO: Return 1 + number of suppressed exceptions
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("Primary");
        ex.addSuppressed(new RuntimeException("S1"));
        ex.addSuppressed(new RuntimeException("S2"));
        System.out.println("Total: " + totalExceptions(ex));
    }
}
```

### Expected Output

```
Total: 3
```

### Hints

1. Start with count = 1.
2. Add `t.getSuppressed().length`.
3. Return the total.
