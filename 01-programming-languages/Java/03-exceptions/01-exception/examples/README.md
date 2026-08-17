# Examples: Exception in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated. Compile and run each example to verify behavior.

---

## Example 1: Basic Exception Handling

```java
public class BasicException {
    public static void main(String[] args) {
        try {
            int result = divide(10, 0);
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    static int divide(int a, int b) {
        return a / b;
    }
}
```

**Output:**
```
Caught: / by zero
```

**Explanation:** The `divide` method throws `ArithmeticException` when `b` is zero. The `catch` block intercepts it and prevents the program from crashing. The exception carries a message describing the error.

---

## Example 2: Checked vs Unchecked Exception

```java
import java.io.*;

public class CheckedVsUnchecked {
    static void throwChecked() throws IOException {
        throw new IOException("File not found");
    }

    static void throwUnchecked() {
        throw new IllegalArgumentException("Bad argument");
    }

    public static void main(String[] args) {
        // Checked: must catch or declare
        try {
            throwChecked();
        } catch (IOException e) {
            System.out.println("Checked: " + e.getMessage());
        }

        // Unchecked: no declaration required
        try {
            throwUnchecked();
        } catch (IllegalArgumentException e) {
            System.out.println("Unchecked: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Checked: File not found
Unchecked: Bad argument
```

**Explanation:** `IOException` is checked — the compiler enforces that callers handle or declare it. `IllegalArgumentException` is unchecked — no compile-time enforcement. Checked exceptions represent recoverable conditions; unchecked exceptions represent programming errors.

---

## Example 3: Custom Exception with Fields

```java
public class CustomExceptionFields {
    static class OrderException extends Exception {
        private final String orderId;
        private final int errorCode;

        OrderException(String message, String orderId, int errorCode) {
            super(message);
            this.orderId = orderId;
            this.errorCode = errorCode;
        }

        String getOrderId() { return orderId; }
        int getErrorCode() { return errorCode; }
    }

    public static void main(String[] args) {
        try {
            throw new OrderException("Order not found", "ORD-999", 404);
        } catch (OrderException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Order: " + e.getOrderId());
            System.out.println("Code: " + e.getErrorCode());
        }
    }
}
```

**Output:**
```
Error: Order not found
Order: ORD-999
Code: 404
```

**Explanation:** Custom exceptions can carry domain-specific fields. This adds context that generic exception messages cannot provide. The caller can inspect the fields to make decisions (retry, log differently, etc.).

---

## Example 4: Exception Propagation

```java
public class ExceptionPropagation {
    static void methodC() throws Exception {
        throw new Exception("Originated in methodC");
    }

    static void methodB() throws Exception {
        methodC();
    }

    static void methodA() {
        try {
            methodB();
        } catch (Exception e) {
            System.out.println("Caught in methodA: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        methodA();
    }
}
```

**Output:**
```
Caught in methodA: Originated in methodC
```

**Explanation:** Exceptions propagate up the call stack. `methodC` throws, `methodB` declares `throws` so it propagates, and `methodA` catches it. The stack trace shows the full path from origin to catch point.

---

## Example 5: Multi-Catch with Custom Exception

```java
public class MultiCatchCustom {
    static class ValidationException extends Exception {
        ValidationException(String msg) { super(msg); }
    }

    static void validate(String input) throws ValidationException {
        if (input == null) throw new ValidationException("Input is null");
        if (input.isEmpty()) throw new ValidationException("Input is empty");
        if (!input.matches("[a-zA-Z]+")) throw new ValidationException("Invalid characters: " + input);
        System.out.println("Valid: " + input);
    }

    public static void main(String[] args) {
        String[] tests = {"hello", null, "", "hello123"};
        for (String test : tests) {
            try {
                validate(test);
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
```

**Output:**
```
Valid: hello
Error: Input is null
Error: Input is empty
Error: Invalid characters: hello123
```

**Explanation:** The `validate` method throws a custom checked exception for each failure mode. The caller iterates over test inputs and catches the exception for each. This pattern is common in input validation frameworks.
