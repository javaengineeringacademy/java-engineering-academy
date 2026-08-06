# Exception Handling Quiz

## Question 1 (MCQ)
Which of the following is a checked exception in Java?
- A) NullPointerException
- B) ArrayIndexOutOfBoundsException
- C) IOException
- D) ClassCastException

**Answer: C**
**Explanation:** Checked exceptions (like IOException, SQLException) must be declared in the method signature or caught. Unchecked exceptions (RuntimeException subclasses) don't require explicit handling.

---

## Question 2 (MCQ)
What is the correct order of blocks in a try-catch-finally statement?
- A) try → finally → catch
- B) catch → try → finally
- C) try → catch → finally
- D) finally → try → catch

**Answer: C**
**Explanation:** The order is always: `try` block first, then one or more `catch` blocks, and optionally `finally` at the end. The `finally` block always executes regardless of whether an exception is thrown.

---

## Question 3 (MCQ)
What happens if an exception is thrown inside a finally block?
- A) It is ignored
- B) It replaces any exception from the try block
- C) The program terminates immediately
- D) It is caught by the catch block

**Answer: B**
**Explanation:** If a finally block throws an exception, it masks any exception from the try/catch block. This is why code inside finally should be kept simple and not throw exceptions.

---

## Question 4 (MCQ)
What is the purpose of try-with-resources?
- A) To manually close resources
- B) To automatically close resources implementing AutoCloseable
- C) To catch exceptions automatically
- D) To prevent exceptions from occurring

**Answer: B**
**Explanation:** try-with-resources automatically calls `close()` on resources declared in the parentheses after the try block exits, even if an exception occurs. This prevents resource leaks.

---

## Question 5 (Code Output)
What does this code print?

```java
public class Main {
    public static void main(String[] args) {
        try {
            System.out.print("A ");
            int result = 10 / 0;
            System.out.print("B ");
        } catch (ArithmeticException e) {
            System.out.print("C ");
        } finally {
            System.out.print("D ");
        }
        System.out.print("E ");
    }
}
```

**Answer:** A C D E
**Explanation:** "A" prints before the division. Division by zero throws ArithmeticException, so "B" is skipped. The catch block prints "C". The finally block always runs, printing "D". Execution continues after the try-catch-finally, printing "E".

---

## Question 6 (Code Output)
What does this code print?

```java
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("try");
            return;
        } finally {
            System.out.println("finally");
        }
    }
}
```

**Answer:** try finally
**Explanation:** The `finally` block executes even when a `return` statement is encountered in the try block. The method returns after the finally block completes.

---

## Question 7 (Bug Finding)
Find the bug:

```java
public void readFile(String path) {
    FileInputStream fis = new FileInputStream(path);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line = br.readLine();
    System.out.println(line);
    fis.close();
}
```

**Bug:** Resources are not properly managed. If an exception occurs before `fis.close()`, the file handle leaks. Also, there's no try-catch to handle IOException.
**Fix:** Use try-with-resources:
```java
public void readFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        String line = br.readLine();
        System.out.println(line);
    }
}
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
public void process() {
    try {
        riskyOperation();
    } catch (Exception e) {
        // handle silently
    } finally {
        cleanup();
    }
}
```

**Bug:** The catch block swallows the exception silently without logging or rethrowing. This makes debugging nearly impossible because failures are hidden from the caller and logs.
**Fix:** At minimum, log the exception or rethrow it:
```java
} catch (Exception e) {
    logger.error("Error during processing", e);
    throw new ServiceException("Processing failed", e);
}
```

---

## Question 9 (Scenario-based)
You are writing a method that reads a file and parses JSON. The file might not exist, and the JSON might be malformed. How should you handle exceptions?

- A) Catch Exception and return null
- B) Catch FileNotFoundException and JsonParseException separately, and wrap JsonParseException in a custom exception
- C) Declare throws Exception on the method signature
- D) Use a global try-catch in main()

**Answer: B**
**Explanation:** Catching specific exceptions allows differentiated handling (e.g., file not found → use default, malformed JSON → report parsing error). Wrapping in a custom exception provides meaningful context while preserving the original cause.

---

## Question 10 (Architecture Decision)
You are building a microservice that calls 3 external APIs. Each API can fail independently. How should you design the exception handling?

- A) Let all exceptions propagate to the caller
- B) Create a custom exception hierarchy with specific exceptions for each API, implement retry logic with circuit breaker pattern
- C) Catch all exceptions and return null
- D) Use a single global catch-all exception handler

**Answer: B**
**Explanation:** A custom exception hierarchy allows targeted recovery strategies per API. Retry logic handles transient failures, and the circuit breaker pattern prevents cascading failures when an API is consistently unavailable. This is the industry standard for resilient microservices.
