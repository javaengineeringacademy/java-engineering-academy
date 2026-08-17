# Solutions: Exception Hierarchy in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Catch Hierarchy

```java
public class Exercise1 {
    static void throwNull() {
        String s = null;
        s.length();
    }

    public static void main(String[] args) {
        try {
            throwNull();
        } catch (RuntimeException e) {
            System.out.println("Caught RuntimeException");
        } catch (Exception e) {
            System.out.println("Caught Exception");
        }
    }
}
```

**Output:**
```
Caught RuntimeException
```

**Key points:**
- `NullPointerException` extends `RuntimeException`.
- The first matching catch block executes.
- Catching `RuntimeException` first prevents it from being caught by `Exception`.

---

## Solution 2: Custom Exception Base

```java
public class Exercise2 {
    static class AppException extends Exception {
        AppException(String msg) { super(msg); }
    }

    static class ValidationError extends AppException {
        ValidationError(String msg) { super(msg); }
    }

    static class NotFoundError extends AppException {
        NotFoundError(String msg) { super(msg); }
    }

    static void process(String input) throws AppException {
        if ("invalid".equals(input)) {
            throw new ValidationError("Invalid input");
        }
        if ("missing".equals(input)) {
            throw new NotFoundError("Resource missing");
        }
        System.out.println("OK: " + input);
    }

    public static void main(String[] args) {
        String[] tests = {"valid", "invalid", "missing"};
        for (String test : tests) {
            try {
                process(test);
            } catch (ValidationError e) {
                System.out.println("Validation: " + e.getMessage());
            } catch (NotFoundError e) {
                System.out.println("Not found: " + e.getMessage());
            }
        }
    }
}
```

**Output:**
```
OK: valid
Validation: Invalid input
Not found: Resource missing
```

**Key points:**
- The hierarchy allows catching at specific or general levels.
- Each subtype carries its own semantics.
- Callers can catch `AppException` for a general handler.

---

## Solution 3: Polymorphic Handling

```java
public class Exercise3 {
    static void handle(Throwable t) {
        if (t instanceof Error) {
            System.out.println("Error: " + t.getMessage());
        } else if (t instanceof RuntimeException) {
            System.out.println("Unchecked: " + t.getMessage());
        } else if (t instanceof Exception) {
            System.out.println("Checked: " + t.getMessage());
        }
    }

    public static void main(String[] args) {
        handle(new OutOfMemoryError("heap"));
        handle(new IllegalArgumentException("bad arg"));
        handle(new java.io.IOException("disk"));
    }
}
```

**Output:**
```
Error: heap
Unchecked: bad arg
Checked: disk
```

**Key points:**
- `instanceof` checks navigate the type hierarchy.
- Order matters: `Error` before `RuntimeException` before `Exception`.
- This pattern is useful for logging and monitoring systems.
