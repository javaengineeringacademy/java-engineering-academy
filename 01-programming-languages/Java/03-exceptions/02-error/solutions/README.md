# Solutions: Error in Java

These are complete solutions for all four exercises. Review your own implementation before reading these.

---

## Solution 1: Error vs Exception

```java
public class Exercise1 {
    static String classify(Throwable t) {
        if (t instanceof Error) {
            return "Error";
        }
        return "Exception";
    }

    public static void main(String[] args) {
        System.out.println(classify(new RuntimeException("test")));
        System.out.println(classify(new StackOverflowError()));
        System.out.println(classify(new Exception("checked")));
    }
}
```

**Output:**
```
Exception
Error
Exception
```

**Key points:**
- `instanceof` checks the type hierarchy.
- `StackOverflowError` extends `Error`, so it returns `"Error"`.
- `RuntimeException` and `Exception` are not `Error` instances.

---

## Solution 2: Catch Specific Error

```java
public class Exercise2 {
    static int safeDivide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            return -1;
        } catch (Error e) {
            System.out.println("JVM error");
            return -2;
        }
    }

    public static void main(String[] args) {
        System.out.println("10/2 = " + safeDivide(10, 2));
        System.out.println("10/0 = " + safeDivide(10, 0));
    }
}
```

**Output:**
```
10/2 = 5
10/0 = -1
```

**Key points:**
- `ArithmeticException` is caught first (more specific).
- `Error` catch is a safety net — in practice, you should not catch `Error`.
- The sentinel values differentiate normal, exceptional, and error results.

---

## Solution 3: Stack Trace Depth

```java
public class Exercise3 {
    static int depth = 0;

    static int measureStackDepth(int maxDepth) {
        depth = 0;
        try {
            recurse(maxDepth);
        } catch (StackOverflowError e) {
            return depth;
        }
        return depth;
    }

    static void recurse(int remaining) {
        if (remaining <= 0) return;
        depth++;
        recurse(remaining - 1);
    }

    public static void main(String[] args) {
        System.out.println("Requested depth 10: " + measureStackDepth(10));
        System.out.println("Requested depth 100000: " + measureStackDepth(100000));
    }
}
```

**Output:**
```
Requested depth 10: 10
Requested depth 100000: ~10000
```

**Key points:**
- `StackOverflowError` is caught when the stack limit is reached.
- The `depth` variable captures how far recursion progressed.
- Actual stack depth depends on JVM configuration and frame size.

---

## Solution 4: Assertion Practice

```java
public class Exercise4 {
    static void validateRange(int value, int min, int max) {
        assert min <= max : "Invalid range: min=" + min + " > max=" + max;
        assert value >= min : "Value " + value + " below minimum " + min;
        assert value <= max : "Value " + value + " above maximum " + max;
    }

    public static void main(String[] args) {
        validateRange(5, 1, 10);
        System.out.println("5 is in range [1, 10]");
        validateRange(5, 10, 1);
    }
}
```

**Output (with -ea):**
```
5 is in range [1, 10]
Exception in thread "main" java.lang.AssertionError: Invalid range: min=10 > max=1
```

**Key points:**
- Assertions validate invariants that should never occur.
- Descriptive messages in assertions help debug the root cause.
- Assertions are for development, not production validation.
- Use `IllegalArgumentException` for production input validation.
