# Solutions: Exception in Java

These are complete solutions for all five exercises. Review your own implementation before reading these. Compare your approach; there is often more than one correct solution.

---

## Solution 1: Create a Checked Exception

```java
public class Exercise1 {
    static class InsufficientFundsException extends Exception {
        private final double balance;
        private final double amount;

        InsufficientFundsException(double balance, double amount) {
            super("Insufficient funds: balance=" + balance + ", attempted=" + amount);
            this.balance = balance;
            this.amount = amount;
        }

        double getBalance() { return balance; }
        double getAmount() { return amount; }
    }

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

**Output:**
```
Withdrawn: 50.0, Remaining: 50.0
Error: Insufficient funds: balance=100.0, attempted=150.0
```

**Key points:**
- Extends `Exception` for checked exception behavior.
- Fields store domain data for programmatic access.
- Message is auto-generated in the constructor.

---

## Solution 2: Declare vs Catch

```java
import java.io.*;

public class Exercise2 {
    static String readAndDeclare(String path) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Path.of(path)));
    }

    static String readAndCatch(String path) {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Path.of(path)));
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Declare: " + readAndDeclare("test.txt"));
        System.out.println("Catch: " + readAndCatch("test.txt"));
    }
}
```

**Output:**
```
Declare: null
Catch: null
```

**Key points:**
- `readAndDeclare` lets the exception propagate to `main`.
- `readAndCatch` handles it internally and returns null.
- When the file doesn't exist, `Files.readAllBytes` throws `NoSuchFileException` (subclass of `IOException`).
- The declare approach shifts responsibility to the caller; the catch approach absorbs the failure.

---

## Solution 3: Exception Chaining

```java
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Exercise3 {
    static class DataLoadException extends Exception {
        DataLoadException(String message) { super(message); }
        DataLoadException(String message, Throwable cause) { super(message, cause); }
    }

    static int loadData(String path) throws DataLoadException {
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new DataLoadException("Failed to load data from " + path, e);
        }

        int sum = 0;
        for (String line : lines) {
            try {
                sum += Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                throw new DataLoadException("Invalid integer on line: " + line, e);
            }
        }
        return sum;
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

**Output:**
```
Load failed: Failed to load data from numbers.txt
Caused by: NoSuchFileException
```

**Key points:**
- `DataLoadException` has both message-only and message+cause constructors.
- `IOException` from file reading is wrapped with context.
- `NumberFormatException` from parsing is wrapped separately.
- The caller sees a single exception type but can access the root cause.

---

## Solution 4: Multiple Exception Types

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
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}
```

**Output:**
```
Processed: hello
NullPointerException: Input is null
IllegalArgumentException: Input is empty
StringIndexOutOfBoundsException: Too long: 13
```

**Key points:**
- Multi-catch handles three unrelated exception types in one block.
- `e.getClass().getSimpleName()` identifies the exception type.
- This is cleaner than three separate catch blocks with identical logic.

---

## Solution 5: Finally Block

```java
import java.io.*;

public class Exercise5 {
    static String readFile(String path) {
        System.out.println("Opening resource");
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Path.of(path)));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } finally {
            System.out.println("Closing resource");
        }
    }

    public static void main(String[] args) {
        String result = readFile("test.txt");
        System.out.println("Result: " + result);
    }
}
```

**Output:**
```
Opening resource
Error: test.txt (No such file or directory)
Closing resource
Result: null
```

**Key points:**
- The `finally` block runs regardless of whether an exception was thrown.
- This guarantees resource cleanup even when exceptions occur.
- In real code, use try-with-resources instead of manual finally blocks for `AutoCloseable` resources.
