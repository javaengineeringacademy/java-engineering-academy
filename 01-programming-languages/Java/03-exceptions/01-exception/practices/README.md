# Exercises: Exception in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic. Do not look at the solutions until you have attempted each exercise.

---

## Exercise 1: Create a Checked Exception

### Problem

Create a custom checked exception `InsufficientFundsException` with a `balance` and `amount` field. The message should be auto-generated from these fields.

### Starter Code

```java
public class Exercise1 {
    // TODO: Create InsufficientFundsException extending Exception
    // Fields: double balance, double amount
    // Constructor: (double balance, double amount)
    // Auto-generate message: "Insufficient funds: balance=XX, attempted=XX"

    static void withdraw(double balance, double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(balance, amount);
        }
        System.out.println("Withdrawn: " + amount + ", Remaining: " + (balance - amount));
    }

    public static void main(String[] args) {
        try {
            withdraw(100.0, 50.0);
            withdraw(100.0, 150.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

### Expected Output

```
Withdrawn: 50.0, Remaining: 50.0
Error: Insufficient funds: balance=100.0, attempted=150.0
```

### Hints

1. Extend `Exception` (not `RuntimeException`) for a checked exception.
2. Store `balance` and `amount` as fields.
3. Generate the message in the constructor using `super("Insufficient funds: balance=" + balance + ", attempted=" + amount)`.

---

## Exercise 2: Declare vs Catch

### Problem

Write two versions of a method that reads a file: one that declares `throws IOException` and one that catches it and returns null.

### Starter Code

```java
import java.io.*;

public class Exercise2 {
    static String readAndDeclare(String path) throws IOException {
        // TODO: Read the file and return its content, declaring throws
    }

    static String readAndCatch(String path) {
        // TODO: Read the file, catch IOException, return null on failure
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Declare: " + readAndDeclare("test.txt"));
        System.out.println("Catch: " + readAndCatch("test.txt"));
    }
}
```

### Expected Output

```
Declare: null
Catch: null
```

### Hints

1. Use `new String(java.nio.file.Files.readAllBytes(Path.of(path)))` for reading.
2. For `readAndDeclare`, let the IOException propagate.
3. For `readAndCatch`, wrap in try-catch and return null.
4. Both will return null when the file doesn't exist (the declare version throws to main).

---

## Exercise 3: Exception Chaining

### Problem

Write a method `loadData(String path)` that reads a file, parses each line as an integer, and returns the sum. Wrap any exception in a custom `DataLoadException` that preserves the cause.

### Starter Code

```java
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Exercise3 {
    // TODO: Create DataLoadException extending Exception
    // Include constructors for message and message+cause

    static int loadData(String path) throws DataLoadException {
        // TODO: Read file, parse integers, sum them
        // Wrap NumberFormatException or IOException in DataLoadException
    }

    public static void main(String[] args) {
        try {
            System.out.println("Sum: " + loadData("numbers.txt"));
        } catch (DataLoadException e) {
            System.out.println("Load failed: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Caused by: " + e.getCause().getClass().getSimpleName());
            }
        }
    }
}
```

### Expected Output

```
Load failed: Failed to load data from numbers.txt
Caused by: IOException
```

### Hints

1. Read all lines with `Files.readAllLines(Path.of(path))`.
2. Parse each line with `Integer.parseInt()`.
3. Catch `IOException` and `NumberFormatException` separately.
4. Wrap each in `DataLoadException` with the original cause.

---

## Exercise 4: Multiple Exception Types

### Problem

Write a method `process(String input)` that throws different exception types based on the input. Use multi-catch to handle them with a single catch block.

### Starter Code

```java
public class Exercise4 {
    static void process(String input) {
        if (input == null) throw new NullPointerException("Input is null");
        if (input.isEmpty()) throw new IllegalArgumentException("Input is empty");
        if (input.length() > 10) throw new StringIndexOutOfBoundsException("Too long: " + input.length());
        System.out.println("Processed: " + input);
    }

    public static void main(String[] args) {
        String[] tests = {"hello", null, "", "this is way too long"};
        for (String test : tests) {
            try {
                process(test);
            } catch (NullPointerException | IllegalArgumentException | StringIndexOutOfBoundsException e) {
                // TODO: Print the exception class simple name and message
            }
        }
    }
}
```

### Expected Output

```
Processed: hello
NullPointerException: Input is null
IllegalArgumentException: Input is empty
StringIndexOutOfBoundsException: Too long: 13
```

### Hints

1. Use `e.getClass().getSimpleName()` to get the exception type name.
2. Use `e.getMessage()` for the message.
3. The multi-catch syntax is `catch (A | B | C e)`.

---

## Exercise 5: Finally Block

### Problem

Write a method `readFile(String path)` that:
1. Opens a resource (simulated with a print statement)
2. Reads the file
3. Closes the resource in a `finally` block (simulated with a print statement)
4. Returns the content or null on failure

### Starter Code

```java
import java.io.*;

public class Exercise5 {
    static String readFile(String path) {
        System.out.println("Opening resource");
        try {
            // TODO: Read file content
            // Return content string
        } catch (Exception e) {
            // TODO: Print error, return null
        } finally {
            // TODO: Print "Closing resource"
        }
        return null;
    }

    public static void main(String[] args) {
        String result = readFile("test.txt");
        System.out.println("Result: " + result);
    }
}
```

### Expected Output

```
Opening resource
Closing resource
Result: null
```

### Hints

1. The `finally` block always executes, whether or not an exception occurs.
2. Place the `finally` after the `catch` block.
3. The `finally` block should print "Closing resource".
