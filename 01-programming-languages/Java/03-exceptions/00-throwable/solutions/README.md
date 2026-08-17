# Solutions: Throwable in Java

These are complete solutions for all five exercises. Review your own implementation before reading these. Compare your approach; there is often more than one correct solution.

---

## Solution 1: Create a Throwable with a Message

```java
public class Exercise1 {
    public static RuntimeException createException(String message) {
        return new RuntimeException(message);
    }

    public static void main(String[] args) {
        RuntimeException ex = createException("Disk quota exceeded");
        System.out.println("Message: " + ex.getMessage());
    }
}
```

**Output:**
```
Message: Disk quota exceeded
```

**Key points:**
- The constructor stores the message.
- `getMessage()` retrieves it.
- No additional logic needed — the framework handles message storage.

---

## Solution 2: Chain Exceptions

```java
public class Exercise2 {
    public static int wrapException(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Parse failed", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Parsed: " + wrapException("42"));
        try {
            wrapException("abc");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }
}
```

**Output:**
```
Parsed: 42
Error: Parse failed
Cause: For input string: "abc"
```

**Key points:**
- `new RuntimeException(message, cause)` preserves the causal chain.
- `getCause()` retrieves the original `NumberFormatException`.
- The caller gets a meaningful wrapper message and can still access the root cause.

---

## Solution 3: Walk the Cause Chain

```java
public class Exercise3 {
    public static int countCauses(Throwable t) {
        int count = 0;
        Throwable current = t;
        while (current != null) {
            count++;
            current = current.getCause();
        }
        return count;
    }

    public static void main(String[] args) {
        RuntimeException level3 = new RuntimeException("Level 3");
        RuntimeException level2 = new RuntimeException("Level 2", level3);
        RuntimeException level1 = new RuntimeException("Level 1", level2);

        System.out.println("Chain depth: " + countCauses(level1));
        System.out.println("Single: " + countCauses(new RuntimeException("Solo")));
    }
}
```

**Output:**
```
Chain depth: 3
Single: 1
```

**Key points:**
- Start counting from 1 (the exception itself).
- `getCause()` returns null at the root of the chain.
- The loop terminates when there are no more causes.

---

## Solution 4: Suppressed Exceptions

```java
public class Exercise4 {
    public static RuntimeException createWithSuppressed(String primary, String... suppressed) {
        RuntimeException ex = new RuntimeException(primary);
        for (String msg : suppressed) {
            ex.addSuppressed(new RuntimeException(msg));
        }
        return ex;
    }

    public static void main(String[] args) {
        RuntimeException ex = createWithSuppressed("Primary", "Sup1", "Sup2", "Sup3");
        System.out.println("Primary: " + ex.getMessage());
        System.out.println("Suppressed count: " + ex.getSuppressed().length);
        for (Throwable t : ex.getSuppressed()) {
            System.out.println("  " + t.getMessage());
        }
    }
}
```

**Output:**
```
Primary: Primary
Suppressed count: 3
  Sup1
  Sup2
  Sup3
```

**Key points:**
- `addSuppressed()` attaches exceptions without replacing the primary.
- The suppressed array is growable — each call appends.
- This mirrors what try-with-resources does internally.

---

## Solution 5: Inspect Stack Trace

```java
public class Exercise5 {
    public static String topFrameName(Throwable t) {
        StackTraceElement[] trace = t.getStackTrace();
        if (trace.length == 0) {
            return "(empty stack trace)";
        }
        return trace[0].getMethodName();
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Top frame: " + topFrameName(ex));
    }
}
```

**Output:**
```
Top frame: main
```

**Key points:**
- `getStackTrace()` returns an array where index 0 is the most recent call.
- Defensive check for empty stack traces prevents `ArrayIndexOutOfBoundsException`.
- `StackTraceElement.getMethodName()` returns the simple method name.
