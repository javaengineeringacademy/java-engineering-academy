# Exercises: try-catch in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic. Do not look at the solutions until you have attempted each exercise.

---

## Exercise 1: Basic try-catch

### Problem

Write a method `safeDivide(int a, int b)` that returns `a / b` as an integer. If `b` is zero, the method should return `-1` instead of throwing an exception. Demonstrate the method with three test cases: `(10, 2)`, `(10, 0)`, and `(-5, 3)`.

### Starter Code

```java
public class Exercise1 {
    public static int safeDivide(int a, int b) {
        // TODO: Use try-catch to handle ArithmeticException
        // Return -1 if division by zero occurs
    }

    public static void main(String[] args) {
        System.out.println("10 / 2 = " + safeDivide(10, 2));
        System.out.println("10 / 0 = " + safeDivide(10, 0));
        System.out.println("-5 / 3 = " + safeDivide(-5, 3));
    }
}
```

### Expected Output

```
10 / 2 = 5
10 / 0 = -1
-5 / 3 = -1
```

### Hints

1. The exception thrown by integer division by zero is `ArithmeticException`.
2. A try-catch block around the division operation is sufficient.
3. The catch block should return `-1`.
4. Verify that non-exceptional paths return the correct quotient.

---

## Exercise 2: Multiple Catch with Different Handling

### Problem

Write a method `processInput(String input)` that attempts to parse the input as an integer and then compute `100 / parsedValue`. Handle two distinct failure modes:

- If the input is not a valid integer, print `"Invalid format: <input>"`.
- If the parsed value is zero, print `"Division by zero"`.

If both operations succeed, print `"Result: <value>"`.

Test with inputs: `"25"`, `"abc"`, `"0"`, `"null"`.

### Starter Code

```java
public class Exercise2 {
    public static void processInput(String input) {
        // TODO: Try to parse and divide
        // TODO: Catch NumberFormatException separately
        // TODO: Catch ArithmeticException separately
    }

    public static void main(String[] args) {
        processInput("25");
        processInput("abc");
        processInput("0");
        processInput("null");
    }
}
```

### Expected Output

```
Result: 4
Invalid format: abc
Division by zero
Invalid format: null
```

### Hints

1. `Integer.parseInt(null)` throws `NumberFormatException`, not `NullPointerException`.
2. Order the catch blocks from most specific to most general.
3. Each catch block should print a distinct message.
4. Do not use a single catch block for both exceptions.

---

## Exercise 3: Multi-catch

### Problem

Rewrite Exercise 2 using multi-catch syntax. Both `NumberFormatException` and `ArithmeticException` should be caught in a single catch block. The message format is: `"Error for input \"<input>\": <exception message>"`.

### Starter Code

```java
public class Exercise3 {
    public static void processInput(String input) {
        // TODO: Use multi-catch to handle both exception types
    }

    public static void main(String[] args) {
        processInput("25");
        processInput("abc");
        processInput("0");
        processInput("null");
    }
}
```

### Expected Output

```
Result: 4
Error for input "abc": For input string: "abc"
Error for input "0": / by zero
Error for input "null": For input string: "null"
```

### Hints

1. The multi-catch syntax is `catch (A | B e)`.
2. The exceptions must not share an inheritance relationship.
3. Access the exception message using `e.getMessage()`.
4. This exercise should produce functionally identical output to Exercise 2, with a unified error message format.

---

## Exercise 4: Nested try-catch

### Problem

Write a method `processArray(String[] inputs)` that iterates over a string array. For each element:

1. Parse it as an integer.
2. Compute `100 / parsedValue`.

If parsing fails for an element, print `"Skipping: <element>"` and continue to the next element. If division fails, print `"Cannot divide by: <element>"` and continue.

After processing all elements, print `"Done"`.

### Starter Code

```java
public class Exercise4 {
    public static void processArray(String[] inputs) {
        // TODO: Outer loop over inputs
        // TODO: Inner try-catch for parse and divide
        // TODO: Two catch blocks inside the loop
    }

    public static void main(String[] args) {
        processArray(new String[]{"20", "abc", "0", "25", "xyz"});
    }
}
```

### Expected Output

```
100 / 20 = 5
Skipping: abc
Cannot divide by: 0
100 / 25 = 4
Skipping: xyz
Done
```

### Hints

1. Place the try-catch inside the loop so each element is handled independently.
2. Use two catch blocks: one for `NumberFormatException`, one for `ArithmeticException`.
3. The `"Done"` message should print after the loop completes, outside the try-catch.
4. Verify that a failure on one element does not skip subsequent elements.

---

## Exercise 5: Exception Translation (Wrap and Rethrow)

### Problem

Implement two methods:

1. `parseAndValidate(String input)` that parses the input as an integer. If parsing fails, throw a custom `ValidationException` that wraps the original `NumberFormatException`.

2. `loadAndProcess(String input)` that calls `parseAndValidate`. If a `ValidationException` is thrown, catch it and print `"Load failed: <validation message>"` and `"Root cause: <root cause class name>"`.

Define `ValidationException` as a checked exception with a `String` message constructor and a `(String, Throwable)` constructor.

### Starter Code

```java
class ValidationException extends Exception {
    // TODO: Add constructors
}

public class Exercise5 {
    public static int parseAndValidate(String input) throws ValidationException {
        // TODO: Parse input, wrap NumberFormatException in ValidationException
    }

    public static void loadAndProcess(String input) {
        // TODO: Call parseAndValidate, catch ValidationException
    }

    public static void main(String[] args) {
        loadAndProcess("42");
        loadAndProcess("abc");
        loadAndProcess("0");
    }
}
```

### Expected Output

```
Valid: 42
Load failed: Invalid input: abc
Root cause: java.lang.NumberFormatException
Valid: 0
```

### Hints

1. `ValidationException` needs a `super(message)` constructor and a `super(message, cause)` constructor.
2. In `parseAndValidate`, catch `NumberFormatException` and throw `new ValidationException("Invalid input: " + input, e)`.
3. In `loadAndProcess`, use `e.getCause()` to get the root cause.
4. Call `e.getCause().getClass().getName()` to print the root cause class name.
5. The `"Valid: <value>"` message should be printed when no exception is thrown.

---

## Exercise 6: Finally Block Behavior

### Problem

Write a method `testFinally(int code)` that demonstrates `finally` block behavior:

- In the `try` block, set a local variable `status = "try"` and return it.
- In a `catch` block (for `code < 0`), set `status = "catch"` and return it.
- In the `finally` block, set `status = "finally"` and print `"Finally executed"`.

Observe and document what value is actually returned in each case.

### Starter Code

```java
public class Exercise6 {
    public static String testFinally(int code) {
        String status = "initial";
        try {
            if (code < 0) {
                throw new RuntimeException("Negative code");
            }
            status = "try";
            return status;
        } catch (RuntimeException e) {
            status = "catch";
            return status;
        } finally {
            status = "finally";
            System.out.println("Finally executed");
        }
    }

    public static void main(String[] args) {
        System.out.println("Code 10: " + testFinally(10));
        System.out.println("Code -1: " + testFinally(-1));
    }
}
```

### Expected Output

```
Finally executed
Code 10: try
Finally executed
Code -1: catch
```

### Hints

1. The `finally` block always executes, regardless of whether an exception was thrown.
2. The return value is determined at the point the `return` statement executes, before `finally` runs.
3. Assigning `status = "finally"` in the `finally` block does not change the already-committed return value.
4. Run the code and verify the output matches the expected values.
5. Consider: what would happen if `finally` also contained a `return` statement? (Do not test this in production code.)
