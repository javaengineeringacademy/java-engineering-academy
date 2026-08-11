# Solutions — Try-Catch Exception Handling

---

## Exercise 1: Basic Try-Catch

```java
public static int divide(int a, int b) {
    try {
        return a / b;
    } catch (ArithmeticException e) {
        System.out.println("Cannot divide by zero");
        return -1;
    }
}

// Tests
System.out.println(divide(10, 2));   // 5
System.out.println(divide(10, 0));   // "Cannot divide by zero" → -1
System.out.println(divide(0, 5));    // 0
```

---

## Exercise 2: Single Catch with String Parsing

```java
public static int parseAge(String input) {
    if (input == null) {
        return -1;
    }
    try {
        return Integer.parseInt(input);
    } catch (NumberFormatException e) {
        return -1;
    }
}

// Tests
System.out.println(parseAge("25"));     // 25
System.out.println(parseAge("abc"));    // -1
System.out.println(parseAge(null));     // -1
System.out.println(parseAge("25.5"));   // -1
```

---

## Exercise 3: Multiple Catch Blocks

```java
public static String getArrayElement(String[] arr, String indexStr) {
    try {
        int index = Integer.parseInt(indexStr);
        return arr[index];
    } catch (NumberFormatException e) {
        return null;
    } catch (ArrayIndexOutOfBoundsException e) {
        return null;
    } catch (NullPointerException e) {
        return null;
    }
}

// Tests
System.out.println(getArrayElement(new String[]{"a","b","c"}, "1"));  // "b"
System.out.println(getArrayElement(new String[]{"a","b","c"}, "abc")); // null
System.out.println(getArrayElement(new String[]{"a","b","c"}, "10")); // null
System.out.println(getArrayElement(null, "0"));                       // null
```

---

## Exercise 4: Multi-catch (Java 7+)

```java
public static int processInput(String input) {
    try {
        if (input == null) {
            throw new NullPointerException();
        }
        if (input.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return Integer.parseInt(input);
    } catch (NullPointerException | IllegalArgumentException e) {
        return -1;
    } catch (NumberFormatException e) {
        return -2;
    }
}

// Tests
System.out.println(processInput("42"));  // 42
System.out.println(processInput(""));    // -1
System.out.println(processInput(null));  // -1
System.out.println(processInput("abc")); // -2
```

---

## Exercise 5: Nested try-catch

```java
public static int safeDivision(String numeratorStr, String denominatorStr) {
    try {
        int numerator = Integer.parseInt(numeratorStr);
        int denominator = Integer.parseInt(denominatorStr);
        try {
            return numerator / denominator;
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero");
            return -1;
        }
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format");
        return -1;
    }
}

// Tests
System.out.println(safeDivision("10", "3"));   // 3
System.out.println(safeDivision("10", "0"));   // "Cannot divide by zero" → -1
System.out.println(safeDivision("abc", "5"));  // "Invalid number format" → -1
```

---

## Exercise 6: Rethrowing Exceptions

```java
public static class ValidationException extends Exception {
    private final String originalInput;

    public ValidationException(String message, String originalInput) {
        super(message);
        this.originalInput = originalInput;
    }

    public String getOriginalInput() {
        return originalInput;
    }
}

public static int validateAndProcess(String input) throws ValidationException {
    try {
        int value = Integer.parseInt(input);
        if (value < 0) {
            throw new IllegalArgumentException("Negative number not allowed");
        }
        return value;
    } catch (NumberFormatException e) {
        throw new ValidationException("Invalid integer format", input);
    } catch (IllegalArgumentException e) {
        throw new ValidationException(e.getMessage(), input);
    }
}

// Tests
try {
    System.out.println(validateAndProcess("42"));   // 42
    System.out.println(validateAndProcess("-5"));    // throws ValidationException
    System.out.println(validateAndProcess("abc"));   // throws ValidationException
} catch (ValidationException e) {
    System.out.println(e.getMessage() + " | input: " + e.getOriginalInput());
}
```

---

## Exercise 7: Exception Chaining

```java
public static class DataLoadException extends Exception {
    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}

public static List<Integer> loadAndParseData(String data) throws DataLoadException {
    try {
        String[] parts = data.split(",");
        List<Integer> values = new ArrayList<>();
        for (String part : parts) {
            values.add(Integer.parseInt(part.trim()));
        }
        return values;
    } catch (NullPointerException e) {
        throw new DataLoadException("Input data is null", e);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
        throw new DataLoadException("Failed to parse data: " + data, e);
    }
}

// Tests
try {
    System.out.println(loadAndParseData("1,2,3"));      // [1, 2, 3]
    System.out.println(loadAndParseData("1,abc,3"));    // throws DataLoadException
    System.out.println(loadAndParseData(null));          // throws DataLoadException
} catch (DataLoadException e) {
    System.out.println(e.getMessage());
    System.out.println("Root cause: " + e.getCause().getClass().getSimpleName());
}
```

---

## Exercise 8: Retry Pattern

```java
@FunctionalInterface
interface ThrowingSupplier<T> {
    T get() throws Exception;
}

public static <T> T retryOperation(ThrowingSupplier<T> operation, int maxRetries) throws Exception {
    Exception lastException = null;

    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            return operation.get();
        } catch (Exception e) {
            lastException = e;
            System.out.println("Attempt " + attempt + "/" + maxRetries + " failed: " + e.getMessage());
        }
    }

    throw new Exception("Operation failed after " + maxRetries + " retries", lastException);
}

// Test
try {
    int result = retryOperation(() -> {
        if (Math.random() < 0.8) {
            throw new RuntimeException("Simulated failure");
        }
        return 42;
    }, 5);
    System.out.println("Result: " + result);
} catch (Exception e) {
    System.out.println("All retries failed: " + e.getMessage());
}
```

---

## Exercise 9: File Processing Pipeline

```java
public static class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

public static List<Person> processLines(String[] lines) {
    List<Person> people = new ArrayList<>();

    for (int i = 0; i < lines.length; i++) {
        try {
            String[] parts = lines[i].split("\\|");
            String name = parts[0];
            int age = Integer.parseInt(parts[1]);
            people.add(new Person(name, age));
        } catch (Exception e) {
            System.out.println("Skipping line " + (i + 1) + ": " + e.getMessage());
        }
    }

    return people;
}

// Test
String[] data = {"Alice|25", "Bob|abc", "Charlie|30", "|", "Diana|22"};
List<Person> people = processLines(data);
System.out.println("Parsed: " + people);
// Output: [Alice(25), Charlie(30), Diana(22)]
```

---

## Exercise 10: Production-Style Error Handler

```java
public class ErrorHandler {

    public void handle(Exception e, String context) {
        if (e instanceof IOException) {
            System.out.println("IO error in " + context + ": " + e.getMessage());
        } catch (NullPointerException | IllegalArgumentException ex) {
            System.out.println(ex.getClass().getSimpleName() + " in " + context + ": " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error in " + context + ": " + ex.getMessage());
        }
    }
}

// Wait — the instanceof approach won't work with multi-catch for different types.
// Let's fix this with a proper implementation:

public class ErrorHandler {

    public void handle(Exception e, String context) {
        try {
            throw e;
        } catch (IOException ex) {
            System.out.println("IO error in " + context + ": " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("Null pointer in " + context);
        } catch (IllegalArgumentException ex) {
            System.out.println("Bad argument in " + context + ": " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error in " + context + ": " + ex.getMessage());
        }
    }
}

// Test
ErrorHandler handler = new ErrorHandler();
handler.handle(new IOException("File not found"), "file loading");
handler.handle(new NullPointerException(), "user lookup");
handler.handle(new IllegalArgumentException("invalid email"), "validation");
handler.handle(new RuntimeException("unknown"), "processing");
```

**Output:**
```
IO error in file loading: File not found
Null pointer in user lookup
Bad argument in validation: invalid email
Unexpected error in processing: unknown
```
