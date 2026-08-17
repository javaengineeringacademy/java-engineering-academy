# Examples: RuntimeException in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: NullPointerException

```java
public class NullPointerDemo {
    public static void main(String[] args) {
        String text = null;
        try {
            text.length();
        } catch (NullPointerException e) {
            System.out.println("Cannot call method on null");
            System.out.println("Stack trace top: " + e.getStackTrace()[0].getMethodName());
        }
    }
}
```

**Output:**
```
Cannot call method on null
Stack trace top: main
```

**Explanation:** `NullPointerException` is thrown when calling a method on a null reference. It has no message by default but includes a stack trace. Always check for null before dereferencing.

---

## Example 2: IllegalArgumentException

```java
public class IllegalArgumentDemo {
    static void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        System.out.println("Age set to: " + age);
    }

    public static void main(String[] args) {
        setAge(25);
        setAge(-5);
        setAge(200);
    }
}
```

**Output:**
```
Age set to: 25
Exception in thread "main" java.lang.IllegalArgumentException: Invalid age: -5
```

**Explanation:** `IllegalArgumentException` signals that a method was passed an inappropriate argument. Include the actual value in the message for debugging. This is the standard exception for parameter validation.

---

## Example 3: ClassCastException

```java
public class ClassCastDemo {
    public static void main(String[] args) {
        Object obj = "hello";
        try {
            Integer num = (Integer) obj;
        } catch (ClassCastException e) {
            System.out.println("Cannot cast: " + e.getMessage());
            System.out.println("From: " + e.getSourceClass());
            System.out.println("To: " + e.getTargetClass());
        }
    }
}
```

**Output:**
```
Cannot cast: class java.lang.String cannot be cast to class java.lang.Integer
```

**Explanation:** `ClassCastException` occurs when attempting to cast an object to a type it is not. Use `instanceof` before casting to prevent this exception.

---

## Example 4: NumberFormatException

```java
public class NumberFormatDemo {
    static int parseOrDefault(String input, int defaultValue) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void main(String[] args) {
        System.out.println("42 -> " + parseOrDefault("42", 0));
        System.out.println("abc -> " + parseOrDefault("abc", 0));
        System.out.println("null -> " + parseOrDefault(null, 0));
    }
}
```

**Output:**
```
42 -> 42
abc -> 0
null -> 0
```

**Explanation:** `NumberFormatException` is thrown when parsing a string that is not a valid number. The catch-and-return-default pattern is common for user input processing.

---

## Exception Chaining with RuntimeException

```java
public class RuntimeExceptionChaining {
    static class ServiceException extends RuntimeException {
        ServiceException(String msg, Throwable cause) { super(msg, cause); }
    }

    static void processData(String input) {
        try {
            int value = Integer.parseInt(input);
            int result = 100 / value;
            System.out.println("Result: " + result);
        } catch (NumberFormatException | ArithmeticException e) {
            throw new ServiceException("Processing failed for: " + input, e);
        }
    }

    public static void main(String[] args) {
        try {
            processData("abc");
        } catch (ServiceException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
```

**Output:**
```
Error: Processing failed for: abc
Cause: NumberFormatException
```

**Explanation:** Low-level exceptions are wrapped in domain-specific exceptions. The original exception is preserved as the cause. This pattern provides meaningful exception types while retaining debugging information.
