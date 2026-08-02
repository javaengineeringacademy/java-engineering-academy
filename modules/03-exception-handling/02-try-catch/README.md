# Try-Catch Blocks

## 1. Introduction

The try-catch block is the fundamental construct for handling exceptions in Java. It allows you to enclose code that might throw an exception and define how to handle it when it occurs. This lesson covers all aspects of try-catch blocks, from basic usage to advanced patterns.

## 2. Learning Objectives

By the end of this lesson, you will be able to:

- Implement try-catch blocks correctly
- Use multiple catch blocks effectively
- Understand catch block ordering and specificity
- Implement multi-catch blocks (Java 7+)
- Handle nested try-catch scenarios
- Understand the flow of control in try-catch
- Apply best practices for exception handling

## 3. Prerequisites

- Basic Java syntax
- Understanding of exception hierarchy
- Knowledge of checked vs unchecked exceptions
- Familiarity with basic OOP concepts

## 4. Why This Concept Exists

### The Problem

Without try-catch, a single exception can crash your entire application:

```java
public class fragileApplication {
    public static void main(String[] args) {
        // If this line throws an exception, everything below never executes
        processData();
        saveResults();
        sendNotification();
    }
}
```

### The Solution

Try-catch provides a structured way to handle exceptional conditions:

```java
public class robustApplication {
    public static void main(String[] args) {
        try {
            processData();
            saveResults();
            sendNotification();
        } catch (DataException e) {
            logger.error("Data processing failed", e);
            notifyAdmin(e);
        }
    }
}
```

## 5. Problem Statement

### Challenge 1: Specific Exception Handling

Different exceptions require different handling strategies. How do you handle multiple exception types appropriately?

### Challenge 2: Resource Cleanup

When exceptions occur, resources need to be cleaned up properly. How do you ensure cleanup happens regardless of success or failure?

### Challenge 3: Exception Information

How do you access detailed information about what went wrong and use it for recovery or logging?

### Challenge 4: Code Organization

How do you structure exception handling code to maintain readability and avoid code duplication?

## 6. Theory

### Try Block

The try block encloses code that might throw an exception. If an exception occurs within the try block, the normal flow of execution is interrupted.

### Catch Block

The catch block follows a try block and contains code to handle a specific type of exception. You can have multiple catch blocks for a single try block.

### Multi-Catch Block (Java 7+)

A multi-catch block allows you to catch multiple exception types in a single catch block using the pipe (|) operator.

### Exception Propagation

When an exception is thrown in a try block, Java searches for the appropriate catch block in the following order:
1. Exact match in current method
2. Parent class match in current method
3. Propagate to calling method
4. Continue up the call stack

## 7. Internal Working

### Bytecode Implementation

At the bytecode level, try-catch is implemented using:

1. **Exception Table**: Maps ranges of bytecode to exception handlers
2. **Handler Entry Points**: Points to the first instruction of each catch block
3. **Exception Type Filters**: Specifies which exception types each handler catches

### Exception Table Example

```
Exception Table:
from    to  target  type
  0    12    15   Class java/io/IOException
  0    12    28   Class java/lang/Exception
 15    22    28   Class java/lang/Exception
```

## 8. JVM Perspective

### Stack Frame Management

When an exception occurs:

1. The JVM creates an exception object
2. The current stack frame is examined
3. The exception table is consulted
4. If a handler is found, the operand stack is cleared and the exception is pushed
5. Execution jumps to the handler
6. If no handler, the stack frame is popped and the process repeats

### JVM Instructions

- `athrow`: Throws an exception
- `jsr`/`ret`: Used for finally block implementation (legacy)

## 9. Memory Representation

### Exception Object in Memory

```
Exception Object
├── Object Header (mark word + klass pointer)
├── message (String reference)
├── cause (Throwable reference)
├── stackTrace (StackTraceElement[])
├── suppressedExceptions (Throwable[])
└── backtrace (Object - JVM internal)
```

### Stack Frame During Exception

```
Stack Frame
├── Local Variables
├── Operand Stack
│   └── [Exception Object] ← Top of stack
├── Dynamic Linking
├── Return Address
└── Reference to Exception Handler
```

## 10. Syntax

### Basic Try-Catch

```java
try {
    // Code that might throw an exception
    riskyOperation();
} catch (ExceptionType e) {
    // Handle the exception
    handleException(e);
}
```

### Multiple Catch Blocks

```java
try {
    riskyOperation();
} catch (SpecificException1 e) {
    handleSpecific1(e);
} catch (SpecificException2 e) {
    handleSpecific2(e);
} catch (Exception e) {
    handleGeneral(e);
}
```

### Multi-Catch Block

```java
try {
    riskyOperation();
} catch (SpecificException1 | SpecificException2 e) {
    handleBoth(e);
}
```

### Nested Try-Catch

```java
try {
    outerOperation();
    try {
        innerOperation();
    } catch (InnerException e) {
        handleInner(e);
    }
} catch (OuterException e) {
    handleOuter(e);
}
```

## 11. Easy Example

### Basic Try-Catch

```java
public class BasicTryCatch {
    public static void main(String[] args) {
        try {
            int[] numbers = {1, 2, 3};
            System.out.println(numbers[5]); // ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Array index out of bounds");
            System.out.println("Index tried: " + e.getMessage());
        }
        System.out.println("Program continues normally");
    }
}
```

### String to Integer Conversion

```java
public class StringConversion {
    public static void main(String[] args) {
        String input = "not_a_number";
        
        try {
            int number = Integer.parseInt(input);
            System.out.println("Number: " + number);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + input);
        }
    }
}
```

## 12. Medium Example

### Multiple Catch Blocks

```java
import java.io.*;
import java.util.*;

public class MultipleCatchExample {
    public static void main(String[] args) {
        try {
            // Attempt various operations
            String data = readFile("config.txt");
            int value = Integer.parseInt(data.trim());
            int result = 100 / value;
            System.out.println("Result: " + result);
            
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found: " + e.getMessage());
            useDefaultConfig();
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number in config: " + e.getMessage());
            useDefaultValue();
            
        } catch (ArithmeticException e) {
            System.out.println("Math error: " + e.getMessage());
            useDefaultValue();
            
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
            handleIOError(e);
        }
    }
    
    static String readFile(String filename) throws IOException {
        // Implementation
        return "10";
    }
    
    static void useDefaultConfig() {}
    static void useDefaultValue() {}
    static void handleIOError(IOException e) {}
}
```

### Multi-Catch Block

```java
import java.util.*;

public class MultiCatchExample {
    public static void processInput(String input) {
        try {
            // Validate input
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be null or empty");
            }
            
            // Parse and process
            int number = Integer.parseInt(input);
            int[] array = new int[10];
            array[number] = 100; // Might throw ArrayIndexOutOfBoundsException
            
            System.out.println("Processed successfully");
            
        } catch (IllegalArgumentException | NumberFormatException 
                 | ArrayIndexOutOfBoundsException e) {
            System.out.println("Input validation failed: " + e.getMessage());
            logError(e);
        }
    }
    
    static void logError(Exception e) {
        // Log the error
    }
    
    public static void main(String[] args) {
        processInput(null);
        processInput("abc");
        processInput("100");
    }
}
```

## 13. Hard Example

### Exception Handler with Recovery

```java
import java.util.*;
import java.io.*;

public class ResilientDataProcessor {
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;
    
    public ProcessingResult processWithRetry(String dataId) {
        int attempt = 0;
        Exception lastException = null;
        
        while (attempt < MAX_RETRIES) {
            try {
                return processData(dataId);
                
            } catch (TransientException e) {
                attempt++;
                lastException = e;
                System.out.printf("Attempt %d failed (transient): %s%n", 
                    attempt, e.getMessage());
                
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new ProcessingException("Retry interrupted", ie);
                    }
                }
                
            } catch (PermanentException e) {
                System.out.println("Permanent failure: " + e.getMessage());
                return ProcessingResult.failure(e.getMessage());
                
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                throw new ProcessingException("Unexpected error during processing", e);
            }
        }
        
        // All retries exhausted
        return ProcessingResult.failure(
            "Failed after " + MAX_RETRIES + " attempts: " + lastException.getMessage());
    }
    
    private ProcessingResult processData(String dataId) throws TransientException, PermanentException {
        // Simulate processing
        Random random = new Random();
        int outcome = random.nextInt(10);
        
        if (outcome < 3) {
            throw new TransientException("Temporary network error");
        } else if (outcome < 5) {
            throw new PermanentException("Invalid data format");
        }
        
        return ProcessingResult.success("Data processed: " + dataId);
    }
    
    static class ProcessingResult {
        private final boolean success;
        private final String message;
        
        private ProcessingResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        static ProcessingResult success(String message) {
            return new ProcessingResult(true, message);
        }
        
        static ProcessingResult failure(String message) {
            return new ProcessingResult(false, message);
        }
    }
    
    static class TransientException extends Exception {
        TransientException(String message) { super(message); }
    }
    
    static class PermanentException extends Exception {
        PermanentException(String message) { super(message); }
    }
    
    static class ProcessingException extends RuntimeException {
        ProcessingException(String message, Throwable cause) { super(message, cause); }
    }
}
```

### Comprehensive Exception Handler

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class ExceptionHandler {
    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());
    private final Map<Class<? extends Exception>, ExceptionStrategy> strategies;
    
    public ExceptionHandler() {
        this.strategies = new ConcurrentHashMap<>();
        registerDefaultStrategies();
    }
    
    private void registerDefaultStrategies() {
        strategies.put(IllegalArgumentException.class, 
            new LoggingStrategy(Level.WARNING));
        strategies.put(IOException.class, 
            new RetryStrategy(3, 1000));
        strategies.put(SecurityException.class, 
            new AlertStrategy());
    }
    
    public <T> T execute(Callable<T> operation, String operationName) {
        try {
            return operation.call();
            
        } catch (Exception e) {
            ExceptionStrategy strategy = findStrategy(e);
            return strategy.handle(e, operationName, () -> {
                try {
                    return operation.call();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
    
    private ExceptionStrategy findStrategy(Exception e) {
        // Find most specific strategy
        for (Map.Entry<Class<? extends Exception>, ExceptionStrategy> entry : strategies.entrySet()) {
            if (entry.getKey().isInstance(e)) {
                return entry.getValue();
            }
        }
        return new RethrowStrategy();
    }
    
    public void registerStrategy(Class<? extends Exception> exceptionType, ExceptionStrategy strategy) {
        strategies.put(exceptionType, strategy);
    }
    
    interface ExceptionStrategy {
        <T> T handle(Exception e, String operationName, Supplier<T> retryOperation);
    }
    
    static class LoggingStrategy implements ExceptionStrategy {
        private final Level level;
        
        LoggingStrategy(Level level) {
            this.level = level;
        }
        
        @Override
        public <T> T handle(Exception e, String operationName, Supplier<T> retryOperation) {
            logger.log(level, String.format("Operation '%s' failed: %s", 
                operationName, e.getMessage()), e);
            throw new RuntimeException("Operation failed: " + operationName, e);
        }
    }
    
    static class RetryStrategy implements ExceptionStrategy {
        private final int maxRetries;
        private final long delayMs;
        
        RetryStrategy(int maxRetries, long delayMs) {
            this.maxRetries = maxRetries;
            this.delayMs = delayMs;
        }
        
        @Override
        public <T> T handle(Exception e, String operationName, Supplier<T> retryOperation) {
            for (int i = 0; i < maxRetries; i++) {
                try {
                    Thread.sleep(delayMs * (i + 1));
                    return retryOperation.get();
                } catch (Exception ex) {
                    logger.warning(String.format("Retry %d for '%s' failed", i + 1, operationName));
                }
            }
            throw new RuntimeException("All retries exhausted for: " + operationName, e);
        }
    }
    
    static class AlertStrategy implements ExceptionStrategy {
        @Override
        public <T> T handle(Exception e, String operationName, Supplier<T> retryOperation) {
            logger.severe(String.format("SECURITY ALERT in '%s': %s", 
                operationName, e.getMessage()));
            // Send alert to security team
            throw new RuntimeException("Security alert triggered", e);
        }
    }
    
    static class RethrowStrategy implements ExceptionStrategy {
        @Override
        public <T> T handle(Exception e, String operationName, Supplier<T> retryOperation) {
            throw new RuntimeException("Unhandled exception in: " + operationName, e);
        }
    }
}
```

## 14. Performance

### Try-Catch Performance Impact

**No Exception Thrown:**
- Minimal overhead (~2-5 nanoseconds)
- Only involves checking the exception table

**Exception Thrown:**
- Significant overhead (~1-100 microseconds)
- Depends on stack depth and exception type

### Best Practices

```java
// Bad: Exception-based control flow
public boolean isInteger(String str) {
    try {
        Integer.parseInt(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}

// Good: Pre-validation
public boolean isInteger(String str) {
    if (str == null || str.isEmpty()) return false;
    for (char c : str.toCharArray()) {
        if (!Character.isDigit(c) && c != '-' && c != '+') {
            return false;
        }
    }
    return true;
}
```

## 15. Best Practices

### Exception Handling Guidelines

1. **Catch Specific Exceptions First**
```java
// Good
try {
    riskyOperation();
} catch (FileNotFoundException e) {
    // Handle specific case
} catch (IOException e) {
    // Handle more general case
}
```

2. **Don't Swallow Exceptions**
```java
// Bad
try {
    riskyOperation();
} catch (Exception e) {
    // Silent failure
}

// Good
try {
    riskyOperation();
} catch (Exception e) {
    logger.error("Operation failed", e);
    throw new ApplicationException("Operation failed", e);
}
```

3. **Use Multi-Catch for Same Handling**
```java
// Good when handling is identical
try {
    parseInput(input);
} catch (NumberFormatException | IllegalArgumentException e) {
    handleInvalidInput(e);
}
```

4. **Keep Try Blocks Focused**
```java
// Bad
try {
    // Too much code - hard to identify what failed
    a();
    b();
    c();
} catch (Exception e) {
    // Which operation failed?
}

// Good
try {
    a();
} catch (ExceptionA e) {
    handleA(e);
}

try {
    b();
} catch (ExceptionB e) {
    handleB(e);
}
```

## 16. Common Mistakes

### Mistake 1: Catching Generic Exception

```java
// Bad
try {
    riskyOperation();
} catch (Exception e) {
    // Too broad - catches everything
}

// Good
try {
    riskyOperation();
} catch (SpecificException e) {
    // Handle specific case
}
```

### Mistake 2: Wrong Order of Catch Blocks

```java
// Bad - more general first
try {
    riskyOperation();
} catch (Exception e) {
    // Catches everything - specific blocks never reached
} catch (FileNotFoundException e) {
    // Never reached!
}

// Good - more specific first
try {
    riskyOperation();
} catch (FileNotFoundException e) {
    // Handle specific case
} catch (Exception e) {
    // Handle general case
}
```

### Mistake 3: Exception in Catch Block

```java
// Bad - exception in catch not handled
try {
    riskyOperation();
} catch (Exception e) {
    logToFile(e.getMessage()); // This might throw!
}

// Good - handle exceptions in catch
try {
    riskyOperation();
} catch (Exception e) {
    try {
        logToFile(e.getMessage());
    } catch (IOException logException) {
        // Handle logging failure
        System.err.println("Logging failed: " + logException.getMessage());
    }
}
```

## 17. Pitfalls

### Pitfall 1: Catching Throwable

```java
// Bad - catches Errors too
try {
    riskyOperation();
} catch (Throwable t) {
    // Catches OutOfMemoryError, StackOverflowError, etc.
}

// Good - catch Exception, not Throwable
try {
    riskyOperation();
} catch (Exception e) {
    // Only catches Exceptions, not Errors
}
```

### Pitfall 2: Exception Variable Scope

```java
// Bad - exception variable not accessible outside catch
try {
    riskyOperation();
} catch (Exception e) {
    // e accessible here
}
// e not accessible here - can't log or rethrow

// Good - use finally or rethrow
Exception caughtException = null;
try {
    riskyOperation();
} catch (Exception e) {
    caughtException = e;
}
if (caughtException != null) {
    logger.error("Operation failed", caughtException);
}
```

### Pitfall 3: Overly Broad Multi-Catch

```java
// Bad - too many exceptions in one catch
try {
    riskyOperation();
} catch (IOException | SQLException | ClassNotFoundException 
         | InterruptedException | ExecutionException e) {
    // Which exception was it? Hard to handle differently
}

// Good - separate handling for different exceptions
try {
    riskyOperation();
} catch (IOException e) {
    handleIO(e);
} catch (SQLException e) {
    handleDB(e);
} catch (ClassNotFoundException | InterruptedException | ExecutionException e) {
    handleAsync(e);
}
```

## 18. Debugging Tips

### Debugging Try-Catch Issues

1. **Add Logging Before and After**
```java
logger.debug("About to execute risky operation");
try {
    riskyOperation();
    logger.debug("Operation completed successfully");
} catch (Exception e) {
    logger.error("Operation failed", e);
}
```

2. **Use Exception Filters in Debugger**
- Set breakpoint on catch line
- Add condition to filter by exception type

3. **Check Stack Trace Carefully**
```
java.lang.NullPointerException
    at com.example.MyClass.process(MyClass.java:42) ← Your code
    at com.example.MyClass.main(MyClass.java:10) ← Caller
```

4. **Inspect Exception Object**
- Message
- Cause chain
- Stack trace elements
- Suppressed exceptions

## 19. Comparison Table

### Catch Block Variations

| Variation | Syntax | Use Case | Java Version |
|-----------|--------|----------|--------------|
| Single catch | `catch (Exception e)` | One exception type | All |
| Multiple blocks | Multiple `catch` | Different handling | All |
| Multi-catch | `catch (A \| B e)` | Same handling | 7+ |
| Final catch | `catch (Exception e)` last | Fallback | All |

### Exception Handling Approaches

| Approach | Pros | Cons | When to Use |
|----------|------|------|-------------|
| Specific catches | Precise handling | More code | Different recovery needed |
| Multi-catch | Less code | Same handling | Identical recovery |
| Generic catch | Simple | Misses details | Fallback only |
| Rethrowing | Preserves info | Still need handler | Delegation |

## 20. Decision Tree

### When to Use Each Pattern

```
Do you need to handle different exceptions differently?
├── Yes → Use multiple catch blocks
│   ├── Is handling identical for some?
│   │   ├── Yes → Use multi-catch for those
│   │   └── No → Separate catch blocks
│   └── Order by specificity
└── No → Use single catch block
    ├── Use most specific exception type
    └── Consider multi-catch if multiple types possible

Do you need to retry on failure?
├── Yes → Implement retry logic in catch
│   ├── Transient failure? → Retry with backoff
│   └── Permanent failure? → Log and abort
└── No → Handle and continue or rethrow
```

## 21. Interview Questions

### Q1: What is the difference between `catch (Exception e)` and `catch (Throwable t)`?

**Answer:**
- `catch (Exception e)` catches all Exception subclasses but not Error subclasses
- `catch (Throwable t)` catches both Exception and Error subclasses
- Generally, you should catch Exception, not Throwable, as Errors represent serious JVM problems

### Q2: Can we have a try block without catch?

**Answer:**
Yes, if there's a finally block:
```java
try {
    riskyOperation();
} finally {
    cleanup();
}
```
However, this is rare - usually you want to handle the exception.

### Q3: What is multi-catch and when should you use it?

**Answer:**
Multi-catch (Java 7+) allows catching multiple exception types in one block:
```java
catch (IOException | SQLException e)
```
Use it when the handling logic is identical for multiple exception types.

### Q4: What happens if an exception occurs in a catch block?

**Answer:**
The new exception propagates up the call stack. The original exception is lost unless explicitly chained.

### Q5: Can catch blocks be empty?

**Answer:**
Technically yes, but it's bad practice. Empty catch blocks hide exceptions and make debugging difficult. Always log or rethrow.

## 22. Exercises

### Exercise 1: Calculator Exception Handling

Create a calculator that handles:
- Division by zero
- Invalid operators
- Number format exceptions
- Overflow conditions

### Exercise 2: File Processor

Build a file processor with:
- FileNotFoundException handling
- Permission denied handling
- Corrupted file handling
- Proper resource cleanup

### Exercise 3: Network Client

Create a network client with:
- Connection timeout handling
- DNS resolution failure
- SSL/TLS errors
- Retry logic

## 23. Assignments

### Assignment 1: Robust Parser

Create a parser that handles:
- Invalid input formats
- Null inputs
- Empty inputs
- Boundary conditions

### Assignment 2: Transaction Manager

Build a transaction manager with:
- Rollback on failure
- Partial commit handling
- Deadlock detection
- Timeout handling

### Assignment 3: API Gateway

Create an API gateway with:
- Rate limiting exceptions
- Authentication failures
- Request validation errors
- Response formatting

## 24. Mini Project

### Error Handling Library

Create a comprehensive error handling library with:
1. Exception hierarchy for different domains
2. Retry mechanisms with configurable strategies
3. Circuit breaker pattern
4. Error aggregation and reporting
5. Fallback mechanisms

## 25. Summary

### Key Takeaways

- Try-catch is the fundamental exception handling mechanism
- Order catch blocks from most specific to most general
- Use multi-catch for identical handling of multiple exceptions
- Keep try blocks focused on specific operations
- Always handle or rethrow exceptions
- Don't catch Throwable unless absolutely necessary
- Add meaningful logging in catch blocks
- Consider retry logic for transient failures

### Best Practices Checklist

- [ ] Catch specific exceptions, not generic Exception
- [ ] Order catch blocks correctly
- [ ] Don't swallow exceptions silently
- [ ] Log exceptions with context
- [ ] Use multi-catch when appropriate
- [ ] Keep try blocks small and focused
- [ ] Handle exceptions in catch blocks too
- [ ] Test exception paths

## 26. References

### Official Documentation
- [Java SE Try-Catch](https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html)
- [Multi-Catch Blocks](https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html#multiple_catch)

### Books
- "Effective Java" by Joshua Bloch
- "Java Concurrency in Practice" by Brian Goetz

### Online Resources
- [Baeldung - Java Exceptions](https://www.baeldung.com/java-exceptions)
- [Baeldung - Multi-Catch](https://www.baeldung.com/java-multi-catch)

## 27. Next Steps

Now that you understand try-catch blocks, proceed to:
- **03-finally**: Learn about the finally block and resource cleanup
