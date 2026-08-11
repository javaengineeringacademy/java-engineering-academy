# Solutions: try-catch in Java

These are complete solutions for all six exercises. Review your own implementation before reading these. Compare your approach; there is often more than one correct solution.

---

## Solution 1: Basic try-catch

```java
public class Exercise1 {
    public static int safeDivide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println("10 / 2 = " + safeDivide(10, 2));
        System.out.println("10 / 0 = " + safeDivide(10, 0));
        System.out.println("-5 / 3 = " + safeDivide(-5, 3));
    }
}
```

**Output:**
```
10 / 2 = 5
10 / 0 = -1
-5 / 3 = -1
```

**Key points:**
- The try-catch wraps only the operation that can throw.
- The catch block returns a sentinel value (`-1`).
- The main method demonstrates both normal and exceptional paths.

---

## Solution 2: Multiple Catch with Different Handling

```java
public class Exercise2 {
    public static void processInput(String input) {
        try {
            int value = Integer.parseInt(input);
            int result = 100 / value;
            System.out.println("Result: " + result);
        } catch (NumberFormatException e) {
            System.out.println("Invalid format: " + input);
        } catch (ArithmeticException e) {
            System.out.println("Division by zero");
        }
    }

    public static void main(String[] args) {
        processInput("25");
        processInput("abc");
        processInput("0");
        processInput("null");
    }
}
```

**Output:**
```
Result: 4
Invalid format: abc
Division by zero
Invalid format: null
```

**Key points:**
- `NumberFormatException` is caught before `ArithmeticException` because it is the more likely failure for this method.
- Each catch block handles a single exception type with a distinct message.
- `Integer.parseInt(null)` throws `NumberFormatException`, so the third catch block is never reached. This is correct; the code handles it appropriately regardless.

---

## Solution 3: Multi-catch

```java
public class Exercise3 {
    public static void processInput(String input) {
        try {
            int value = Integer.parseInt(input);
            int result = 100 / value;
            System.out.println("Result: " + result);
        } catch (NumberFormatException | ArithmeticException e) {
            System.out.println("Error for input \"" + input + "\": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        processInput("25");
        processInput("abc");
        processInput("0");
        processInput("null");
    }
}
```

**Output:**
```
Result: 4
Error for input "abc": For input string: "abc"
Error for input "0": / by zero
Error for input "null": For input string: "null"
```

**Key points:**
- The multi-catch syntax reduces two catch blocks to one.
- `NumberFormatException` and `ArithmeticException` are not in an inheritance relationship, so the compiler permits this combination.
- The error message includes both the input and the exception message for diagnostic clarity.

---

## Solution 4: Nested try-catch

```java
public class Exercise4 {
    public static void processArray(String[] inputs) {
        for (String input : inputs) {
            try {
                int value = Integer.parseInt(input);
                int result = 100 / value;
                System.out.println("100 / " + value + " = " + result);
            } catch (NumberFormatException e) {
                System.out.println("Skipping: " + input);
            } catch (ArithmeticException e) {
                System.out.println("Cannot divide by: " + input);
            }
        }
        System.out.println("Done");
    }

    public static void main(String[] args) {
        processArray(new String[]{"20", "abc", "0", "25", "xyz"});
    }
}
```

**Output:**
```
100 / 20 = 5
Skipping: abc
Cannot divide by: 0
100 / 25 = 4
Skipping: xyz
Done
```

**Key points:**
- The try-catch is inside the loop, so each element is processed independently.
- A failure on element `i` does not prevent processing of element `i+1`.
- The `"Done"` message prints after the loop completes, outside the try-catch scope.

---

## Solution 5: Exception Translation

```java
class ValidationException extends Exception {
    ValidationException(String message) {
        super(message);
    }

    ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class Exercise5 {
    public static int parseAndValidate(String input) throws ValidationException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid input: " + input, e);
        }
    }

    public static void loadAndProcess(String input) {
        try {
            int value = parseAndValidate(input);
            System.out.println("Valid: " + value);
        } catch (ValidationException e) {
            System.out.println("Load failed: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getClass().getName());
        }
    }

    public static void main(String[] args) {
        loadAndValidate("42");
        loadAndValidate("abc");
        loadAndValidate("0");
    }
}
```

**Output:**
```
Valid: 42
Load failed: Invalid input: abc
Root cause: java.lang.NumberFormatException
Valid: 0
```

**Key points:**
- `ValidationException` is a checked exception, enforcing that callers handle or declare it.
- The `(String, Throwable)` constructor preserves the causal chain.
- `e.getCause()` returns the original `NumberFormatException`, allowing the caller to access root cause details.
- `getClass().getName()` returns the fully qualified class name of the cause.

---

## Solution 6: Finally Block Behavior

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

**Output:**
```
Finally executed
Code 10: try
Finally executed
Code -1: catch
```

**Key points:**
- The `finally` block executes in both the normal path and the exceptional path.
- The return value is determined at the `return` statement. The `finally` block runs after the return value is set but does not override it (in this specific pattern).
- `status = "finally"` in the `finally` block modifies the local variable but does not change the return value that was already committed.
- This behavior is a known source of bugs. The general recommendation is to avoid `finally` blocks that modify return values or variables that affect the return value.
