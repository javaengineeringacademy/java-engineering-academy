# Examples: try-catch in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated. Compile and run each example to verify behavior.

---

## Example 1: Basic try-catch

```java
public class BasicTryCatch {
    public static void main(String[] args) {
        int numerator = 10;
        int denominator = 0;

        try {
            int result = numerator / denominator;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero.");
        }

        System.out.println("Program continues after catch.");
    }
}
```

**Output:**
```
Cannot divide by zero.
Program continues after catch.
```

**Explanation:** The division `10 / 0` throws an `ArithmeticException`. The `catch` block intercepts it. Execution resumes after the catch block. Without the try-catch, the program would terminate with an unhandled exception stack trace.

---

## Example 2: Multiple Catch Blocks

```java
public class MultipleCatch {
    public static void main(String[] args) {
        String[] inputs = {"42", "abc", null};

        for (String input : inputs) {
            try {
                int value = Integer.parseInt(input);
                int result = 100 / value;
                System.out.println("100 / " + value + " = " + result);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number: \"" + input + "\"");
            } catch (ArithmeticException e) {
                System.out.println("Arithmetic error for input: " + input);
            }
        }
    }
}
```

**Output:**
```
100 / 42 = 2
Invalid number: "abc"
Invalid number: "null"
```

**Explanation:** Each catch block handles a distinct exception type. `NumberFormatException` is thrown by `Integer.parseInt` when the string is not a valid integer. `ArithmeticException` would be thrown if parsing succeeded but division produced an error. The JVM checks catch blocks in order and executes the first matching one.

Note: `Integer.parseInt(null)` throws `NumberFormatException` (not `NullPointerException`), so the second catch block is never reached in this example. It would only execute if a valid parse produced a zero divisor.

---

## Example 3: Multi-catch (Java 7+)

```java
public class MultiCatch {
    public static void main(String[] args) {
        String[] inputs = {"42", "abc", "0"};

        for (String input : inputs) {
            try {
                int value = Integer.parseInt(input);
                int result = 100 / value;
                System.out.println("100 / " + value + " = " + result);
            } catch (NumberFormatException | ArithmeticException e) {
                System.out.println("Error processing \"" + input + "\": " + e.getMessage());
            }
        }
    }
}
```

**Output:**
```
100 / 42 = 2
Error processing "abc": For input string: "abc"
Error processing "0": / by zero
```

**Explanation:** The multi-catch syntax `catch (A | B e)` consolidates two catch blocks into one. Both exception types are handled by the same code. This reduces duplication when the handling logic is identical. The exceptions listed in a multi-catch must not be related by inheritance; the compiler enforces this.

---

## Example 4: Nested try-catch

```java
public class NestedTryCatch {
    public static void main(String[] args) {
        try {
            System.out.println("Outer try block");

            String[] data = {"A", "B", null};
            int sum = 0;

            for (String s : data) {
                try {
                    sum += Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid element: \"" + s + "\"");
                }
            }

            System.out.println("Partial sum: " + sum);

            int[] divisors = {2, 0, 5};
            for (int d : divisors) {
                try {
                    System.out.println("Sum / " + d + " = " + (sum / d));
                } catch (ArithmeticException e) {
                    System.out.println("Cannot divide " + sum + " by " + d);
                }
            }

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }

        System.out.println("Outer try block complete.");
    }
}
```

**Output:**
```
Outer try block
Skipping invalid element: "null"
Partial sum: 0
Sum / 2 = 0
Cannot divide 0 by 0
Sum / 5 = 0
Outer try block complete.
```

**Explanation:** The inner try-catch blocks handle exceptions independently within the loop. Each failure is isolated; processing continues for remaining elements. The outer try-catch serves as a safety net for any unexpected exception not handled by the inner blocks. This pattern is appropriate when each iteration of a loop has independent failure modes.

---

## Example 5: Rethrowing Exceptions

```java
public class RethrowExample {

    static class DataLoadException extends Exception {
        DataLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static int readRecord(String input) throws DataLoadException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new DataLoadException("Invalid record: " + input, e);
        }
    }

    public static void main(String[] args) {
        String[] records = {"101", "abc", "303"};

        for (String record : records) {
            try {
                int id = readRecord(record);
                System.out.println("Loaded record ID: " + id);
            } catch (DataLoadException e) {
                System.out.println("Load failed: " + e.getMessage());
                System.out.println("Caused by: " + e.getCause());
            }
        }
    }
}
```

**Output:**
```
Loaded record ID: 101
Load failed: Invalid record: abc
Caused by: java.lang.NumberFormatException: For input string: "abc"
Loaded record ID: 303
```

**Explanation:** The `readRecord` method catches the low-level `NumberFormatException` and wraps it in a domain-specific `DataLoadException`. The original exception is preserved as the cause. The caller receives a meaningful exception type and can still access the root cause for diagnostics. This is the standard pattern for exception translation.

---

## Example 6: Finally Block and Return Interaction

```java
public class FinallyExample {

    static int withFinally() {
        int value = 0;
        try {
            value = 10;
            return value;
        } finally {
            value = 20;
            System.out.println("Finally: value = " + value);
        }
    }

    static int withFinallyAndCatch() {
        int value = 0;
        try {
            value = 10;
            int result = 100 / 0;
            return result;
        } catch (ArithmeticException e) {
            value = 30;
            return value;
        } finally {
            value = 40;
            System.out.println("Finally: value = " + value);
        }
    }

    public static void main(String[] args) {
        System.out.println("withFinally returned: " + withFinally());
        System.out.println("withFinallyAndCatch returned: " + withFinallyAndCatch());
    }
}
```

**Output:**
```
Finally: value = 20
withFinally returned: 10
Finally: value = 40
withFinallyAndCatch returned: 30
```

**Explanation:** The `finally` block executes in both paths. However, the return value is determined at the point the `return` statement executes. Assigning `value = 20` in `finally` does not change the returned value of `10` because the return value was already set to `10` before `finally` ran. This demonstrates a subtle and dangerous interaction: `finally` can execute but cannot always modify the return value in the way a developer might expect. The safe rule is to never assign return values in `finally`.

In the catch path, the same logic applies. The `catch` block sets the return value to `30`. The `finally` block sets `value = 40`, but the returned value remains `30`.

**Key takeaway:** `finally` runs, but its assignments to the return variable do not override a return statement unless the return was not yet committed. This behavior is implementation-dependent in edge cases and should be avoided entirely.
