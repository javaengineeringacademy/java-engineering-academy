# Examples: finally Block in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Basic finally Execution

```java
public class BasicFinally {
    public static void main(String[] args) {
        try {
            System.out.println("try block");
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("catch block: " + e.getMessage());
        } finally {
            System.out.println("finally block");
        }
    }
}
```

**Output:**
```
try block
catch block: / by zero
finally block
```

**Explanation:** The `finally` block executes after both the try and catch blocks. It runs regardless of whether an exception was thrown. This guarantees cleanup code executes even when exceptions occur.

---

## Example 2: finally with Return

```java
public class FinallyWithReturn {
    static int testReturn() {
        try {
            return 1;
        } finally {
            System.out.println("finally executed");
        }
    }

    public static void main(String[] args) {
        System.out.println("Returned: " + testReturn());
    }
}
```

**Output:**
```
finally executed
Returned: 1
```

**Explanation:** The `finally` block executes before the method returns. The return value is set at the `return` statement, but `finally` runs before the value is actually returned to the caller. Do not rely on `finally` to modify return values.

---

## Example 3: finally Does Not Override Return

```java
public class FinallyNoOverride {
    static int tricky() {
        int value = 10;
        try {
            return value;
        } finally {
            value = 20;
            System.out.println("finally: value = " + value);
        }
    }

    public static void main(String[] args) {
        System.out.println("Returned: " + tricky());
    }
}
```

**Output:**
```
finally: value = 20
Returned: 10
```

**Explanation:** The return value of 10 is committed before `finally` runs. Changing `value` to 20 in `finally` does not affect the returned value. This is a common source of bugs. Avoid modifying variables that affect return values in `finally`.

---

## Example 4: finally Masks Exception

```java
public class FinallyMasksException {
    static int dangerous() {
        try {
            throw new RuntimeException("original");
        } finally {
            System.out.println("finally: returning 42");
            return 42;
        }
    }

    public static void main(String[] args) {
        System.out.println("Returned: " + dangerous());
    }
}
```

**Output:**
```
finally: returning 42
Returned: 42
```

**Explanation:** The `return` in `finally` suppresses the exception. The caller never sees the `RuntimeException`. This is dangerous — the exception is silently swallowed. Never return from a `finally` block that is used for cleanup.

---

## Example 5: finally for Resource Cleanup

```java
public class FinallyCleanup {
    static class Resource implements AutoCloseable {
        String name;
        Resource(String name) { this.name = name; }
        void use() { System.out.println("Using " + name); }
        @Override public void close() { System.out.println("Closed " + name); }
    }

    public static void main(String[] args) {
        Resource r1 = new Resource("database");
        Resource r2 = new Resource("file");
        try {
            r1.use();
            r2.use();
        } finally {
            r1.close();
            r2.close();
        }
        System.out.println("All resources closed");
    }
}
```

**Output:**
```
Using database
Using file
Closed database
Closed file
All resources closed
```

**Explanation:** The `finally` block ensures both resources are closed even if an exception occurs during `use()`. In real code, prefer try-with-resources for `AutoCloseable` resources — it handles this pattern automatically and correctly.
