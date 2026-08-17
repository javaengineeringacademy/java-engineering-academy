# Exercises: Throwable in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic. Do not look at the solutions until you have attempted each exercise.

---

## Exercise 1: Create a Throwable with a Message

### Problem

Write a method `createException(String message)` that returns a new `RuntimeException` with the given message.

### Starter Code

```java
public class Exercise1 {
    public static RuntimeException createException(String message) {
        // TODO: Create and return a RuntimeException with the message
    }

    public static void main(String[] args) {
        RuntimeException ex = createException("Disk quota exceeded");
        System.out.println("Message: " + ex.getMessage());
    }
}
```

### Expected Output

```
Message: Disk quota exceeded
```

### Hints

1. Use `new RuntimeException(message)`.
2. Return the exception object.
3. The message is stored and retrievable via `getMessage()`.

---

## Exercise 2: Chain Exceptions

### Problem

Write a method `wrapException(String input)` that:
1. Tries to parse `input` as an integer
2. If parsing fails, catches `NumberFormatException`
3. Throws a new `RuntimeException("Parse failed", caughtException)`
4. Returns the parsed integer on success

### Starter Code

```java
public class Exercise2 {
    public static int wrapException(String input) {
        // TODO: Parse input, catch NumberFormatException, wrap and rethrow
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

### Expected Output

```
Parsed: 42
Error: Parse failed
Cause: For input string: "abc"
```

### Hints

1. Use `Integer.parseInt(input)` inside a try block.
2. Catch `NumberFormatException` and throw a new exception with the cause as the second argument.
3. `getCause()` returns the original exception.

---

## Exercise 3: Walk the Cause Chain

### Problem

Write a method `countCauses(Throwable t)` that returns the number of exceptions in the cause chain (including the original exception).

### Starter Code

```java
public class Exercise3 {
    public static int countCauses(Throwable t) {
        // TODO: Walk the cause chain and count exceptions
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

### Expected Output

```
Chain depth: 3
Single: 1
```

### Hints

1. Start with count = 1.
2. Use `t.getCause()` in a loop until it returns null.
3. Increment the count for each cause in the chain.

---

## Exercise 4: Suppressed Exceptions

### Problem

Write a method `createWithSuppressed(String primary, String... suppressed)` that creates a `RuntimeException` with the given primary message and adds each suppressed message as a separate suppressed exception.

### Starter Code

```java
public class Exercise4 {
    public static RuntimeException createWithSuppressed(String primary, String... suppressed) {
        // TODO: Create RuntimeException, add suppressed exceptions
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

### Expected Output

```
Primary: Primary
Suppressed count: 3
  Sup1
  Sup2
  Sup3
```

### Hints

1. Create the primary exception first.
2. Loop over the `suppressed` array.
3. Call `addSuppressed(new RuntimeException(message))` for each.
4. Return the primary exception.

---

## Exercise 5: Inspect Stack Trace

### Problem

Write a method `topFrameName(Throwable t)` that returns the method name of the topmost stack frame.

### Starter Code

```java
public class Exercise5 {
    public static String topFrameName(Throwable t) {
        // TODO: Get the stack trace and return the top frame's method name
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Top frame: " + topFrameName(ex));
    }
}
```

### Expected Output

```
Top frame: main
```

### Hints

1. Call `t.getStackTrace()` to get the array.
2. Return `t.getStackTrace()[0].getMethodName()`.
3. The top frame is the most recent method call.
