# Exercises: Checked Exceptions in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Try-Catch for Checked Exception

### Problem

Write a method `safeRead(String path)` that reads the first line of a file. If the file doesn't exist, return `"File not found"`.

### Starter Code

```java
import java.io.*;

public class Exercise1 {
    static String safeRead(String path) {
        // TODO: Read first line, catch IOException
    }

    public static void main(String[] args) {
        System.out.println(safeRead("test.txt"));
    }
}
```

### Expected Output

```
File not found
```

### Hints

1. Use `new BufferedReader(new FileReader(path))`.
2. Call `reader.readLine()` to get the first line.
3. Catch `IOException` and return the error message.
4. Close the reader in a finally block or use TWR.

---

## Exercise 2: Declare Throws

### Problem

Write a method `readLines(String path)` that reads all lines from a file and returns them as a `List<String>`. Declare `throws IOException`.

### Starter Code

```java
import java.io.*;
import java.util.*;

public class Exercise2 {
    static List<String> readLines(String path) throws IOException {
        // TODO: Read all lines and return as list
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readLines("test.txt"));
    }
}
```

### Expected Output

```
[line1, line2, line3]
```

### Hints

1. Use `Files.readAllLines(Path.of(path))`.
2. Return the list directly.
3. Let the IOException propagate to main.

---

## Exercise 3: Custom Checked Exception

### Problem

Create a custom checked exception `ValidationException` with a `field` and `message` field. Write a `validateEmail(String email)` method that throws it if the email doesn't contain `@`.

### Starter Code

```java
public class Exercise3 {
    // TODO: Create ValidationException extending Exception
    // Fields: String field, String message
    // Constructor: (String field, String message)

    static void validateEmail(String email) throws ValidationException {
        // TODO: Validate email, throw ValidationException if invalid
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

### Expected Output

```
Valid email
Validation error: email - Invalid email format
```

### Hints

1. Extend `Exception` for checked behavior.
2. Store `field` and `message` as final fields.
3. Create getter methods for both fields.
4. Check for `@` in the email string.

---

## Exercise 4: Exception Propagation

### Problem

Write three methods that form a call chain: `methodA` calls `methodB` which calls `methodC`. `methodC` throws `IOException`. `methodA` catches it. Use the appropriate approach (declare or catch) at each level.

### Starter Code

```java
import java.io.*;

public class Exercise4 {
    static void methodC() throws IOException {
        throw new IOException("Disk full");
    }

    static void methodB() throws IOException {
        // TODO: Call methodC, declare throws
    }

    static void methodA() {
        // TODO: Call methodB, catch IOException
    }

    public static void main(String[] args) {
        methodA();
    }
}
```

### Expected Output

```
Caught: Disk full
```

### Hints

1. `methodC` already throws and declares.
2. `methodB` should call `methodC()` and declare `throws IOException`.
3. `methodA` should call `methodB()` inside try-catch.
4. Print the exception message in the catch block.
