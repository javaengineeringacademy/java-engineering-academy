# Solutions: finally Block in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Basic finally

```java
public class Exercise1 {
    static int safeDivide(int a, int b) {
        System.out.println("Dividing");
        try {
            return a / b;
        } catch (ArithmeticException e) {
            return -1;
        } finally {
            System.out.println("Done");
        }
    }

    public static void main(String[] args) {
        System.out.println("Result: " + safeDivide(10, 2));
        System.out.println("Result: " + safeDivide(10, 0));
    }
}
```

**Output:**
```
Dividing
Done
Result: 5
Dividing
Done
Result: -1
```

**Key points:**
- `System.out.println("Dividing")` executes before try.
- `finally` runs after both the normal path and the catch path.
- The return value is committed before `finally` executes.

---

## Solution 2: finally Always Runs

```java
public class Exercise2 {
    static String testFinally(String action) {
        System.out.println("Start");
        try {
            if ("return".equals(action)) return "normal";
            if ("exception".equals(action)) throw new RuntimeException("boom");
            if ("error".equals(action)) throw new StackOverflowError();
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("End");
        }
        return "fell through";
    }

    public static void main(String[] args) {
        System.out.println("Result: " + testFinally("return"));
        System.out.println("---");
        System.out.println("Result: " + testFinally("exception"));
        System.out.println("---");
        System.out.println("Result: " + testFinally("other"));
    }
}
```

**Output:**
```
Start
End
Result: normal
---
Start
Caught: boom
End
Result: fell through
---
Start
End
Result: fell through
```

**Key points:**
- "End" prints for all three execution paths.
- The `return` action returns "normal" but finally still prints "End".
- The caught exception continues execution to the return after finally.
- Uncaught errors (like `StackOverflowError`) are not caught by `catch (RuntimeException)`.

---

## Solution 3: Cleanup Pattern

```java
public class Exercise3 {
    static void processWithCleanup(String data) {
        try {
            if (data == null) {
                throw new RuntimeException("null data");
            }
            System.out.println("Processing: " + data);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Cleanup complete");
        }
    }

    public static void main(String[] args) {
        processWithCleanup("valid");
        processWithCleanup(null);
    }
}
```

**Output:**
```
Processing: valid
Cleanup complete
Processing: null
Error: null data
Cleanup complete
```

**Key points:**
- The try block processes valid data or throws on null.
- The catch block handles the exception gracefully.
- The finally block guarantees "Cleanup complete" prints regardless.
- This pattern is the basis for try-with-resources.
