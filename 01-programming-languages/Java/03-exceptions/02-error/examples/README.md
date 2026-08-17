# Examples: Error in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Error Hierarchy

```java
public class ErrorHierarchy {
    public static void main(String[] args) {
        System.out.println("Throwable > Error > LinkageError > NoClassDefFoundError");
        System.out.println("Throwable > Error > VirtualMachineError > OutOfMemoryError");
        System.out.println("Throwable > Error > VirtualMachineError > StackOverflowError");
        System.out.println("Throwable > Error > AssertionError");
        System.out.println();
        System.out.println("OutOfMemoryError is Error: " + (new OutOfMemoryError() instanceof Error));
        System.out.println("OutOfMemoryError is Throwable: " + (new OutOfMemoryError() instanceof Throwable));
    }
}
```

**Output:**
```
Throwable > Error > LinkageError > NoClassDefFoundError
Throwable > Error > VirtualMachineError > OutOfMemoryError
Throwable > Error > VirtualMachineError > StackOverflowError
Throwable > Error > AssertionError

OutOfMemoryError is Error: true
OutOfMemoryError is Throwable: true
```

**Explanation:** All error types extend `Error`, which extends `Throwable`. Errors indicate serious problems that a reasonable application should not catch. The hierarchy mirrors the exception hierarchy but signals unrecoverable conditions.

---

## Example 2: StackOverflowError

```java
public class StackOverflowDemo {
    static int count = 0;

    static void infinite() {
        count++;
        infinite();
    }

    public static void main(String[] args) {
        try {
            infinite();
        } catch (StackOverflowError e) {
            System.out.println("Stack overflowed at depth: " + count);
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Stack overflowed at depth: ~10000-15000
Error: null
```

**Explanation:** Each method call adds a frame to the call stack. When the stack is full, the JVM throws `StackOverflowError`. The exact depth depends on stack size and frame size. This error should generally not be caught — fix the recursion instead.

---

## Example 3: AssertionError

```java
public class AssertionErrorDemo {
    static void assertPositive(int value) {
        assert value > 0 : "Expected positive, got " + value;
        System.out.println("Value is positive: " + value);
    }

    public static void main(String[] args) {
        // Enable assertions with -ea flag
        assertPositive(5);
        assertPositive(-1);
    }
}
```

**Output (with -ea flag):**
```
Value is positive: 5
Exception in thread "main" java.lang.AssertionError: Expected positive, got -1
```

**Explanation:** `assert` throws `AssertionError` when the condition is false. Assertions are disabled by default (no performance cost in production). Enable with `-ea` JVM flag for development/testing. Never catch `AssertionError` — it indicates a bug.

---

## Example 4: NoClassDefFoundError

```java
public class NoClassDefFoundDemo {
    public static void main(String[] args) {
        try {
            // Simulate: Class.forName("com.missing.NonExistentClass");
            System.out.println("Would load missing class here");
        } catch (NoClassDefFoundError e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Would load missing class here
```

**Explanation:** `NoClassDefFoundError` occurs when the JVM or classloader cannot find a class at runtime. This is different from `ClassNotFoundException` (checked). The error signals a deployment or configuration problem, not a code bug.

---

## Example 5: VirtualMachineError

```java
public class VirtualMachineErrorDemo {
    public static void main(String[] args) {
        System.out.println("VirtualMachineError subtypes:");
        System.out.println("  OutOfMemoryError - heap exhausted");
        System.out.println("  StackOverflowError - call stack full");
        System.out.println("  InternalError - JVM internal failure");
        System.out.println("  UnknownError - unknown JVM failure");
        System.out.println();
        System.out.println("These should NEVER be caught in application code.");
        System.out.println("They indicate JVM-level failures beyond application control.");
    }
}
```

**Output:**
```
VirtualMachineError subtypes:
  OutOfMemoryError - heap exhausted
  StackOverflowError - call stack full
  InternalError - JVM internal failure
  UnknownError - unknown JVM failure

These should NEVER be caught in application code.
They indicate JVM-level failures beyond application control.
```

**Explanation:** `VirtualMachineError` and its subtypes indicate the JVM itself has encountered a fatal condition. Catching these is almost always wrong — the JVM may be in an inconsistent state. Let them propagate and restart the process.
