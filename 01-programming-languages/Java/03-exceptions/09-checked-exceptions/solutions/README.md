# Solutions: Checked Exceptions in Java

These are complete solutions for all four exercises. Review your own implementation before reading these.

---

## Solution 1: Try-Catch for Checked Exception

```java
import java.io.*;

public class Exercise1 {
    static String safeRead(String path) {
        try (var reader = new BufferedReader(new FileReader(path))) {
            return reader.readLine();
        } catch (IOException e) {
            return "File not found";
        }
    }

    public static void main(String[] args) {
        System.out.println(safeRead("test.txt"));
    }
}
```

**Output:**
```
File not found
```

**Key points:**
- TWR handles resource closure automatically.
- `IOException` is caught and a default value is returned.
- The caller doesn't need to handle the exception.

---

## Solution 2: Declare Throws

```java
import java.io.*;
import java.util.*;

public class Exercise2 {
    static List<String> readLines(String path) throws IOException {
        return Files.readAllLines(Path.of(path));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readLines("test.txt"));
    }
}
```

**Output:**
```
[line1, line2, line3]
```

**Key points:**
- `Files.readAllLines` returns a `List<String>`.
- The `IOException` propagates to `main`.
- `main` declares `throws` so the exception prints a stack trace.

---

## Solution 3: Custom Checked Exception

```java
public class Exercise3 {
    static class ValidationException extends Exception {
        private final String field;
        private final String detailMessage;

        ValidationException(String field, String message) {
            super(message);
            this.field = field;
            this.detailMessage = message;
        }

        String getField() { return field; }
        String getDetailMessage() { return detailMessage; }
    }

    static void validateEmail(String email) throws ValidationException {
        if (email == null || !email.contains("@")) {
            throw new ValidationException("email", "Invalid email format");
        }
    }

    public static void main(String[] args) {
        try {
            validateEmail("alice@example.com");
            System.out.println("Valid email");
            validateEmail("invalid-email");
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getField() + " - " + e.getDetailMessage());
        }
    }
}
```

**Output:**
```
Valid email
Validation error: email - Invalid email format
```

**Key points:**
- Custom checked exception carries domain data.
- The `field` identifies which input failed.
- Callers must handle or declare the exception.

---

## Solution 4: Exception Propagation

```java
import java.io.*;

public class Exercise4 {
    static void methodC() throws IOException {
        throw new IOException("Disk full");
    }

    static void methodB() throws IOException {
        methodC();
    }

    static void methodA() {
        try {
            methodB();
        } catch (IOException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        methodA();
    }
}
```

**Output:**
```
Caught: Disk full
```

**Key points:**
- `methodC` throws the exception.
- `methodB` propagates by declaring `throws`.
- `methodA` catches and handles it.
- This is the standard pattern for checked exception propagation.
