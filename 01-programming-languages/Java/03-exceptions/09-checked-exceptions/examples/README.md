# Examples: Checked Exceptions in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: Basic Checked Exception

```java
import java.io.*;

public class BasicChecked {
    static void readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        System.out.println("Line: " + line);
        reader.close();
    }

    public static void main(String[] args) {
        try {
            readFile("test.txt");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Error: test.txt (No such file or directory)
```

**Explanation:** `IOException` is checked — the compiler forces the method to declare `throws IOException`. The caller must catch it or propagate it. This ensures file errors are handled explicitly.

---

## Example 2: Declare and Propagate

```java
import java.io.*;

public class DeclarePropagate {
    static void step1() throws IOException {
        step2();
    }

    static void step2() throws IOException {
        throw new IOException("Disk failure");
    }

    public static void main(String[] args) {
        try {
            step1();
        } catch (IOException e) {
            System.out.println("Caught in main: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Caught in main: Disk failure
```

**Explanation:** Checked exceptions propagate up the call stack when methods declare `throws`. `step2` throws, `step1` declares and propagates, `main` catches. Each method in the chain must declare the exception.

---

## Example 3: Custom Checked Exception

```java
public class CustomChecked {
    static class ParseException extends Exception {
        private final String input;

        ParseException(String input, String message) {
            super(message);
            this.input = input;
        }

        String getInput() { return input; }
    }

    static int parseRecord(String input) throws ParseException {
        if (input == null || input.isEmpty()) {
            throw new ParseException(input, "Empty record");
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ParseException(input, "Not a valid number: " + input);
        }
    }

    public static void main(String[] args) {
        String[] records = {"42", "", "abc", null};
        for (String record : records) {
            try {
                int value = parseRecord(record);
                System.out.println("Parsed: " + value);
            } catch (ParseException e) {
                System.out.println("Parse error: " + e.getMessage() + " (input: " + e.getInput() + ")");
            }
        }
    }
}
```

**Output:**
```
Parsed: 42
Parse error: Empty record (input: )
Parse error: Not a valid number: abc (input: abc)
Parse error: Empty record (input: null)
```

**Explanation:** Custom checked exceptions carry domain-specific data. The caller can inspect the fields for programmatic handling. Checked exceptions enforce that callers deal with expected failure modes.

---

## Example 4: Multi-catch with Checked Exception

```java
import java.io.*;
import java.sql.*;

public class MultiCatchChecked {
    static void riskyOperation() throws IOException, SQLException {
        if (Math.random() > 0.5) {
            throw new IOException("File error");
        } else {
            throw new SQLException("DB error");
        }
    }

    public static void main(String[] args) {
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Operation failed: File error
```

**Explanation:** Multi-catch handles multiple checked exception types in one block. Both `IOException` and `SQLException` are caught together when the handling logic is identical. The exceptions must not be related by inheritance.
