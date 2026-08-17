# Exercises: Exception Chaining in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Basic Chaining

### Problem

Write a method `wrapAndThrow()` that catches a `NumberFormatException` and wraps it in a `RuntimeException` with the message `"Parse error"`.

### Starter Code

```java
public class Exercise1 {
    static void wrapAndThrow(String input) {
        // TODO: Parse input, catch NumberFormatException, wrap in RuntimeException
    }

    public static void main(String[] args) {
        try {
            wrapAndThrow("abc");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }
}
```

### Expected Output

```
Error: Parse error
Cause: For input string: "abc"
```

### Hints

1. Use `Integer.parseInt(input)` in a try block.
2. Catch `NumberFormatException`.
3. Throw `new RuntimeException("Parse error", e)`.

---

## Exercise 2: Walk the Chain

### Problem

Write a method `chainDepth(Throwable t)` that returns the number of exceptions in the cause chain.

### Starter Code

```java
public class Exercise2 {
    static int chainDepth(Throwable t) {
        // TODO: Walk the cause chain and count
    }

    public static void main(String[] args) {
        RuntimeException ex1 = new RuntimeException("1");
        RuntimeException ex2 = new RuntimeException("2", ex1);
        RuntimeException ex3 = new RuntimeException("3", ex2);

        System.out.println("Depth 1: " + chainDepth(ex1));
        System.out.println("Depth 3: " + chainDepth(ex3));
    }
}
```

### Expected Output

```
Depth 1: 1
Depth 3: 3
```

### Hints

1. Start with count = 1.
2. Use `t.getCause()` in a loop until null.
3. Increment count for each cause.

---

## Exercise 3: Exception Translation

### Problem

Write a method `loadConfig(String path)` that reads a file, catches `IOException`, and throws a custom `ConfigException` with the original cause.

### Starter Code

```java
import java.io.*;

public class Exercise3 {
    // TODO: Create ConfigException extending RuntimeException
    // Include constructor for message and message+cause

    static String loadConfig(String path) {
        // TODO: Read file, catch IOException, throw ConfigException
    }

    public static void main(String[] args) {
        try {
            loadConfig("app.properties");
        } catch (ConfigException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

### Expected Output

```
Error: Failed to load config: app.properties
Cause: FileNotFoundException
```

### Hints

1. Create `ConfigException` extending `RuntimeException`.
2. Use `Files.readString(Path.of(path))` to read the file.
3. Catch `IOException` and throw `ConfigException` with the cause.
