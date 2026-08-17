# Examples: Stack Trace in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Reading Stack Trace

```java
public class ReadingStackTrace {
    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        StackTraceElement[] trace = ex.getStackTrace();

        System.out.println("Stack depth: " + trace.length);
        for (int i = 0; i < Math.min(trace.length, 3); i++) {
            System.out.println("  at " + trace[i]);
        }
    }
}
```

**Output:**
```
Stack depth: 1
  at ReadingStackTrace.main(ReadingStackTrace.java:4)
```

**Explanation:** `getStackTrace()` returns an array of `StackTraceElement` objects. Each element represents one method call. The most recent call is at index 0. The element includes class name, method name, file name, and line number.

---

## Example 2: Stack Trace Elements

```java
public class StackTraceElements {
    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        StackTraceElement[] trace = ex.getStackTrace();

        for (StackTraceElement frame : trace) {
            System.out.println("Class: " + frame.getClassName());
            System.out.println("Method: " + frame.getMethodName());
            System.out.println("File: " + frame.getFileName());
            System.out.println("Line: " + frame.getLineNumber());
            System.out.println();
        }
    }
}
```

**Output:**
```
Class: StackTraceElements
Method: main
File: StackTraceElements.java
Line: 4
```

**Explanation:** Each `StackTraceElement` provides detailed location information. `getClassName()` returns the fully qualified class name. `getMethodName()` returns the method name. `getFileName()` returns the source file. `getLineNumber()` returns the line number (or -1 if unknown).

---

## Example 3: Custom Stack Trace

```java
public class CustomStackTrace {
    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("checkpoint");

        StackTraceElement[] original = ex.getStackTrace();
        System.out.println("Original frames: " + original.length);

        StackTraceElement[] custom = new StackTraceElement[]{
            new StackTraceElement("CustomClass", "customMethod", "Custom.java", 42)
        };
        ex.setStackTrace(custom);

        System.out.println("Modified frames: " + ex.getStackTrace().length);
        System.out.println("Top frame: " + ex.getStackTrace()[0]);
    }
}
```

**Output:**
```
Original frames: 1
Modified frames: 1
Top frame: CustomClass.customMethod(Custom.java:42)
```

**Explanation:** `setStackTrace()` replaces the stack trace. This is useful for cleaning up internal frames before exposing exceptions to callers, or for creating exceptions in test scenarios with specific stack traces.

---

## Example 4: printStackTrace

```java
public class PrintStackTrace {
    public static void main(String[] args) {
        try {
            methodA();
        } catch (Exception e) {
            System.err.println("=== Caught ===");
            e.printStackTrace(System.err);
        }
    }

    static void methodA() { methodB(); }
    static void methodB() { methodC(); }
    static void methodC() { throw new RuntimeException("deep error"); }
}
```

**Output:**
```
=== Caught ===
java.lang.RuntimeException: deep error
    at PrintStackTrace.methodC(PrintStackTrace.java:12)
    at PrintStackTrace.methodB(PrintStackTrace.java:11)
    at PrintStackTrace.methodA(PrintStackTrace.java:10)
    at PrintStackTrace.main(PrintStackTrace.java:5)
```

**Explanation:** `printStackTrace()` prints the full stack trace to the specified stream. The trace shows the complete call chain from the point of the exception back to `main`. This is essential for debugging production issues.
