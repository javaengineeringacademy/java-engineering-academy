# Exception Handling Quiz

## Question 1 (Production Scenario)
Your microservice calls an external payment API that occasionally times out. During peak hours, 30% of requests fail with `SocketTimeoutException`. The service must remain available. How should you handle this?

- A) Let the exception propagate to the caller
- B) Catch the specific exception, implement exponential backoff retry, and fall back to a cached response
- C) Catch `Exception` and return null
- D) Increase the connection timeout to 60 seconds

**Answer: B**
**Explanation:** Retry with exponential backoff handles transient failures without overwhelming the external service. Falling back to cached data maintains availability. Catching specific exceptions (not generic `Exception`) allows targeted handling. This pattern (retry + fallback) is standard for resilient microservices.

---

## Question 2 (Production Scenario)
A developer writes a method that reads a file and parses JSON. They wrap everything in `catch (Exception e)` and return null. In production, the system silently produces incorrect results. What went wrong?

- A) The method should catch `RuntimeException` instead
- B) Catching `Exception` and returning null masks all errors, making failures invisible to callers
- C) JSON parsing doesn't throw exceptions
- D) File reading always succeeds

**Answer: B**
**Explanation:** Catching `Exception` and returning null is an anti-pattern. Callers cannot distinguish between "no data" and "error occurred." This leads to silent failures and incorrect business logic. The fix: catch specific exceptions (FileNotFoundException, JsonParseException), log them, and either rethrow as a custom exception or return a meaningful error result.

---

## Question 3 (Debugging)
A production application throws `NullPointerException` intermittently. The stack trace shows:

```java
public void processOrder(Order order) {
    try {
        validate(order);
        save(order);
        sendNotification(order);
    } catch (Exception e) {
        // handle
    } finally {
        order.setStatus("PROCESSED");
    }
}
```

The `order` parameter is sometimes null. What is the bug?

- A) The try-catch block should be removed
- B) The finally block executes even when order is null, causing NPE on `order.setStatus()`
- C) The `save()` method should be called before `validate()`
- D) Exception handling is not needed for NPE

**Answer: B**
**Explanation:** If `order` is null, the `finally` block still executes and calls `order.setStatus("PROCESSED")`, throwing NPE. The fix: null-check `order` before entering the try block, or add a null check in the finally block: `if (order != null) order.setStatus("PROCESSED");`.

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

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        try {
            System.out.print("A ");
            try {
                System.out.print("B ");
                int x = 1 / 0;
                System.out.print("C ");
            } catch (ArithmeticException e) {
                System.out.print("D ");
            } finally {
                System.out.print("E ");
            }
            System.out.print("F ");
        } catch (Exception e) {
            System.out.print("G ");
        }
        System.out.print("H ");
    }
}
```

A) A B D E F H
B) A B D E G H
C) A B C D E F H
D) A B D E F

**Answer: A**
**Explanation:** Outer try prints "A ". Inner try prints "B ". Division by zero throws ArithmeticException, so "C" is skipped. Inner catch prints "D ". Inner finally prints "E ". After inner try-catch-finally completes, outer try continues and prints "F ". No outer exception occurs, so "G" is skipped. Finally "H" prints. Output: `A B D E F H`.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
class CustomException extends RuntimeException {
    CustomException(String msg) { super(msg); }
}

public class Main {
    static void methodA() {
        try {
            methodB();
        } catch (CustomException e) {
            System.out.print("Caught: " + e.getMessage() + " ");
        }
    }

    static void methodB() {
        try {
            throw new CustomException("Error1");
        } finally {
            System.out.print("Finally ");
        }
    }

    public static void main(String[] args) {
        methodA();
        System.out.print("Done ");
    }
}
```

A) Finally Caught: Error1 Done
B) Caught: Error1 Finally Done
C) Finally Done
D) Uncaught CustomException

**Answer: A**
**Explanation:** `methodB()` throws CustomException, but the finally block executes before the exception propagates, printing "Finally ". The exception propagates to `methodA()` where it's caught, printing "Caught: Error1 ". Then "Done " prints. Output: `Finally Caught: Error1 Done`.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
class Resource implements AutoCloseable {
    Resource() { System.out.print("Open "); }
    void use() { System.out.print("Use "); }
    @Override public void close() { System.out.print("Close "); }
}

public class Main {
    public static void main(String[] args) {
        try (Resource r = new Resource()) {
            r.use();
            throw new RuntimeException("Error");
        } catch (Exception e) {
            System.out.print("Caught ");
        }
        System.out.print("End");
    }
}
```

A) Open Use Close Caught End
B) Open Use Caught Close End
C) Open Use Close End
D) Open Close Caught End

**Answer: A**
**Explanation:** try-with-resources opens the Resource (prints "Open "), calls use() (prints "Use "), then the RuntimeException is thrown. The close() method is called automatically (prints "Close "), then the exception is caught (prints "Caught "), and "End" prints. Output: `Open Use Close Caught End`.

