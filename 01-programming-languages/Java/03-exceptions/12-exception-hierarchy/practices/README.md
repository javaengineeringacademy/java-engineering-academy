# Exercises: Exception Hierarchy in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Catch Hierarchy

### Problem

Write a method that throws a `NullPointerException`. Catch it first with `RuntimeException`, then with `Exception`. Verify which catch block executes.

### Starter Code

```java
public class Exercise1 {
    static void throwNull() {
        String s = null;
        s.length();
    }

    public static void main(String[] args) {
        // TODO: Call throwNull in try-catch
        // Catch RuntimeException first, then Exception
    }
}
```

### Expected Output

```
Caught RuntimeException
```

### Hints

1. Call `throwNull()` in a try block.
2. Catch `RuntimeException` first with a print statement.
3. Catch `Exception` second with a print statement.
4. Only the first matching catch executes.

---

## Exercise 2: Custom Exception Base

### Problem

Create a base exception `AppException` and two subtypes: `ValidationError` and `NotFoundError`. Write a method that throws each based on input.

### Starter Code

```java
public class Exercise2 {
    // TODO: Create AppException extending Exception
    // TODO: Create ValidationError extending AppException
    // TODO: Create NotFoundError extending AppException

    static void process(String input) throws AppException {
        // TODO: If input is "invalid", throw ValidationError
        // TODO: If input is "missing", throw NotFoundError
        // TODO: Otherwise print "OK: " + input
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

### Expected Output

```
OK: valid
Validation: Invalid input
Not found: Resource missing
```

### Hints

1. `AppException` extends `Exception`.
2. `ValidationError` and `NotFoundError` extend `AppException`.
3. Each subtype should have a constructor that accepts a message.

---

## Exercise 3: Polymorphic Handling

### Problem

Write a method `handle(Throwable t)` that prints different messages based on the exception type: `"Error"` for `Error`, `"Unchecked"` for `RuntimeException`, `"Checked"` for other `Exception`.

### Starter Code

```java
public class Exercise3 {
    static void handle(Throwable t) {
        // TODO: Use instanceof to check type and print appropriate message
    }

    public static void main(String[] args) {
        handle(new OutOfMemoryError("heap"));
        handle(new IllegalArgumentException("bad arg"));
        handle(new java.io.IOException("disk"));
    }
}
```

### Expected Output

```
Error: heap
Unchecked: bad arg
Checked: disk
```

### Hints

1. Check `instanceof Error` first.
2. Check `instanceof RuntimeException` second.
3. Check `instanceof Exception` third.
4. Print the message from each exception.
