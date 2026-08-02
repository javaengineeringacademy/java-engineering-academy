# Exception Handling Best Practices

## 1. Introduction

Exception handling best practices are guidelines and patterns that help you write robust, maintainable, and efficient error handling code. Following these practices ensures your applications are resilient, debuggable, and user-friendly. This lesson covers the essential best practices for Java exception handling.

## 2. Learning Objectives

By the end of this lesson, you will be able to:

- Apply exception handling best practices
- Avoid common exception handling anti-patterns
- Implement proper logging and error reporting
- Design exception-safe APIs
- Use exception handling for performance optimization
- Debug exception-related issues effectively
- Review code for exception handling quality

## 3. Prerequisites

- Understanding of try-catch-finally
- Knowledge of exception hierarchy
- Familiarity with custom exceptions
- Basic logging concepts

## 4. Why This Concept Exists

### The Problem

Poor exception handling leads to:
- Silent failures
- Resource leaks
- Hard-to-debug issues
- Poor user experience
- Security vulnerabilities

### The Solution

Best practices provide:
- Consistent error handling
- Proper resource cleanup
- Meaningful error messages
- Better debugging information
- Improved application stability

## 5. Problem Statement

### Challenge 1: Error Visibility

How do you ensure errors are properly logged and visible?

### Challenge 2: Resource Safety

How do you guarantee resources are always cleaned up?

### Challenge 3: API Design

How do you design APIs that handle errors gracefully?

### Challenge 4: Performance

How do you avoid performance penalties from exception handling?

## 6. Theory

### Core Principles

1. **Fail Fast**: Detect errors early
2. **Fail Safe**: Handle errors gracefully
3. **Be Specific**: Catch specific exceptions
4. **Preserve Context**: Include relevant information
5. **Document Behavior**: Declare and document exceptions

### Exception Handling Patterns

- **Catch and Rethrow**: Wrap and propagate
- **Catch and Recover**: Handle and continue
- **Catch and Convert**: Transform exception types
- **Retry**: Attempt operation multiple times
- **Fallback**: Use alternative approach

## 7. Internal Working

### How JVM Handles Exceptions

1. Exception object creation
2. Stack trace filling
3. Exception table lookup
4. Stack unwinding
5. Handler execution
6. Resource cleanup

### Performance Implications

- Exception creation: ~1-5 microseconds
- Stack trace: ~10-100 microseconds
- Propagation: ~1-2 microseconds per frame

## 8. JVM Perspective

### Exception Table

Each method has an exception table:
```
Exception Table:
from    to  target  type
  0    12    15   Class java/io/IOException
  0    12    28   Class java/lang/Exception
```

### Stack Frame Management

During exception handling:
- Stack frames are popped
- Local variables are released
- Exception object is pushed

## 9. Memory Representation

### Exception Object Lifecycle

```
Creation → Propagation → Handling → Cleanup
    ↓           ↓            ↓          ↓
 allocate    stack trace   catch     finally
   heap        fill        block     block
```

### Resource Management

```
Resource Acquisition → Usage → Release
        ↓                ↓         ↓
     try block      business   finally
                              or try-
                             with-
                             resources
```

## 10. Syntax

### Basic Best Practice Patterns

```java
// 1. Use try-with-resources
try (var resource = acquire()) {
    use(resource);
}

// 2. Catch specific exceptions
try {
    riskyOperation();
} catch (SpecificException e) {
    handleSpecific(e);
} catch (Exception e) {
    handleGeneral(e);
}

// 3. Preserve cause
try {
    riskyOperation();
} catch (Exception e) {
    throw new ApplicationException("Failed", e);
}

// 4. Log and rethrow
try {
    riskyOperation();
} catch (Exception e) {
    logger.error("Operation failed", e);
    throw e;
}
```

## 11. Easy Example

### Basic Best Practices

```java
import java.util.Objects;

public class BasicBestPractices {
    
    // Practice 1: Validate inputs early
    public void process(String data) {
        Objects.requireNonNull(data, "Data cannot be null");
        // Process data
    }
    
    // Practice 2: Use specific exceptions
    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
    
    // Practice 3: Clean resource management
    public String readFile(String filename) throws IOException {
        try (var reader = new BufferedReader(new FileReader(filename))) {
            return reader.readLine();
        }
    }
    
    // Practice 4: Preserve exception cause
    public void transferData(String source, String dest) throws DataException {
        try {
            copy(source, dest);
        } catch (IOException e) {
            throw new DataException("Transfer failed", e);
        }
    }
    
    public static void main(String[] args) {
        BasicBestPractices example = new BasicBestPractices();
        
        // Example 1: Null check
        try {
            example.process(null);
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        
        // Example 2: Division by zero
        try {
            example.divide(10, 0);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
```

### Logging Exceptions

```java
import java.util.logging.Logger;

public class ExceptionLogging {
    private static final Logger logger = Logger.getLogger(ExceptionLogging.class.getName());
    
    public void riskyOperation() {
        try {
            // Some risky code
            throw new RuntimeException("Something went wrong");
        } catch (Exception e) {
            // Practice: Log with context
            logger.severe("Risky operation failed: " + e.getMessage());
            logger.throwing(getClass().getName(), "riskyOperation", e);
            
            // Don't swallow - rethrow or handle
            throw e;
        }
    }
    
    public static void main(String[] args) {
        ExceptionLogging example = new ExceptionLogging();
        try {
            example.riskyOperation();
        } catch (Exception e) {
            // Already logged
        }
    }
}
```

## 12. Medium Example

### Comprehensive Exception Handler

```java
import java.util.logging.*;
import java.time.Instant;
import java.util.*;

public class ExceptionHandler {
    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());
    private final ErrorReporter reporter;
    private final RecoveryStrategy defaultStrategy;
    private final Map<Class<?>, RecoveryStrategy> strategies;
    
    public ExceptionHandler(ErrorReporter reporter, RecoveryStrategy defaultStrategy) {
        this.reporter = reporter;
        this.defaultStrategy = defaultStrategy;
        this.strategies = new ConcurrentHashMap<>();
    }
    
    public <T> T execute(Operation<T> operation, String context) {
        try {
            return operation.execute();
        } catch (Exception e) {
            return handleError(e, context);
        }
    }
    
    private <T> T handleError(Exception e, String context) {
        // 1. Log the exception
        logger.log(Level.SEVERE, String.format("Error in context '%s': %s", 
            context, e.getMessage()), e);
        
        // 2. Report to monitoring
        reporter.report(e, context);
        
        // 3. Find recovery strategy
        RecoveryStrategy strategy = findStrategy(e.getClass());
        
        // 4. Attempt recovery
        return strategy.recover(e, context);
    }
    
    private RecoveryStrategy findStrategy(Class<?> exceptionClass) {
        // Check for exact match
        RecoveryStrategy strategy = strategies.get(exceptionClass);
        if (strategy != null) return strategy;
        
        // Check for superclass match
        for (Map.Entry<Class<?>, RecoveryStrategy> entry : strategies.entrySet()) {
            if (entry.getKey().isAssignableFrom(exceptionClass)) {
                return entry.getValue();
            }
        }
        
        return defaultStrategy;
    }
    
    public void registerStrategy(Class<?> exceptionClass, RecoveryStrategy strategy) {
        strategies.put(exceptionClass, strategy);
    }
    
    @FunctionalInterface
    interface Operation<T> {
        T execute() throws Exception;
    }
    
    interface RecoveryStrategy {
        <T> T recover(Exception e, String context);
    }
    
    interface ErrorReporter {
        void report(Exception e, String context);
    }
    
    // Usage
    public static void main(String[] args) {
        ExceptionHandler handler = new ExceptionHandler(
            (e, ctx) -> System.out.println("Reported: " + e.getMessage()),
            (e, ctx) -> {
                System.out.println("Default recovery for: " + ctx);
                return null;
            }
        );
        
        handler.registerStrategy(IllegalArgumentException.class, (e, ctx) -> {
            System.out.println("Illegal argument recovery");
            return "default";
        });
        
        String result = handler.execute(() -> {
            throw new IllegalArgumentException("Invalid input");
        }, "userInput");
        
        System.out.println("Result: " + result);
    }
}
```

### API Exception Handling

```java
import java.util.*;
import java.util.logging.*;

public class ApiService {
    private static final Logger logger = Logger.getLogger(ApiService.class.getName());
    
    public ApiResponse<User> getUser(Long id) {
        try {
            // Validate input
            if (id == null || id <= 0) {
                return ApiResponse.badRequest("Invalid user ID: " + id);
            }
            
            // Attempt to find user
            User user = repository.findById(id);
            if (user == null) {
                return ApiResponse.notFound("User not found with ID: " + id);
            }
            
            return ApiResponse.success(user);
            
        } catch (RepositoryException e) {
            logger.log(Level.SEVERE, "Repository error for user ID: " + id, e);
            return ApiResponse.serverError("Database error occurred");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error for user ID: " + id, e);
            return ApiResponse.serverError("Internal server error");
        }
    }
    
    public ApiResponse<User> createUser(CreateUserRequest request) {
        try {
            // Validate request
            validateRequest(request);
            
            // Create user
            User user = new User(request.getName(), request.getEmail());
            User created = repository.save(user);
            
            return ApiResponse.created(created);
            
        } catch (ValidationException e) {
            return ApiResponse.badRequest(e.getMessage());
            
        } catch (DuplicateException e) {
            return ApiResponse.conflict("User with this email already exists");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating user", e);
            return ApiResponse.serverError("Failed to create user");
        }
    }
    
    private void validateRequest(CreateUserRequest request) throws ValidationException {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new ValidationException("Valid email is required");
        }
    }
    
    // Supporting classes
    static class ApiResponse<T> {
        private final int status;
        private final String message;
        private final T data;
        
        private ApiResponse(int status, String message, T data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }
        
        static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(200, "Success", data);
        }
        
        static <T> ApiResponse<T> created(T data) {
            return new ApiResponse<>(201, "Created", data);
        }
        
        static <T> ApiResponse<T> badRequest(String message) {
            return new ApiResponse<>(400, message, null);
        }
        
        static <T> ApiResponse<T> notFound(String message) {
            return new ApiResponse<>(404, message, null);
        }
        
        static <T> ApiResponse<T> conflict(String message) {
            return new ApiResponse<>(409, message, null);
        }
        
        static <T> ApiResponse<T> serverError(String message) {
            return new ApiResponse<>(500, message, null);
        }
    }
    
    static class ValidationException extends Exception {
        ValidationException(String message) { super(message); }
    }
}
```

## 13. Hard Example

### Exception Handling Framework

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.logging.*;

public class ExceptionHandlingFramework {
    private static final Logger logger = Logger.getLogger(ExceptionHandlingFramework.class.getName());
    private final RetryPolicy defaultRetryPolicy;
    private final CircuitBreaker circuitBreaker;
    private final ErrorReporter reporter;
    private final Map<Class<? extends Exception>, ExceptionHandler<?>> handlers;
    
    public ExceptionHandlingFramework(Builder builder) {
        this.defaultRetryPolicy = builder.defaultRetryPolicy;
        this.circuitBreaker = builder.circuitBreaker;
        this.reporter = builder.reporter;
        this.handlers = new ConcurrentHashMap<>(builder.handlers);
    }
    
    public <T> T execute(String operationName, Callable<T> operation) throws Exception {
        return execute(operationName, operation, defaultRetryPolicy);
    }
    
    public <T> T execute(String operationName, Callable<T> operation, 
                        RetryPolicy retryPolicy) throws Exception {
        // Check circuit breaker
        if (circuitBreaker != null && !circuitBreaker.allowRequest()) {
            throw new CircuitBreakerOpenException("Circuit breaker is open for: " + operationName);
        }
        
        Exception lastException = null;
        int attempts = 0;
        
        while (attempts <= retryPolicy.getMaxRetries()) {
            try {
                T result = operation.call();
                
                // Success - reset circuit breaker
                if (circuitBreaker != null) {
                    circuitBreaker.recordSuccess();
                }
                
                return result;
                
            } catch (Exception e) {
                attempts++;
                lastException = e;
                
                logger.warning(String.format("Attempt %d failed for '%s': %s", 
                    attempts, operationName, e.getMessage()));
                
                // Report exception
                reporter.report(e, operationName);
                
                // Check if we should retry
                if (!retryPolicy.shouldRetry(attempts, e)) {
                    break;
                }
                
                // Wait before retry
                if (attempts <= retryPolicy.getMaxRetries()) {
                    try {
                        Thread.sleep(retryPolicy.getDelayMs(attempts));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedException("Retry interrupted");
                    }
                }
            }
        }
        
        // All retries exhausted
        if (circuitBreaker != null) {
            circuitBreaker.recordFailure();
        }
        
        throw new OperationFailedException(
            String.format("Operation '%s' failed after %d attempts", operationName, attempts),
            lastException);
    }
    
    public <T, E extends Exception> void registerHandler(
            Class<E> exceptionClass, ExceptionHandler<E> handler) {
        handlers.put(exceptionClass, handler);
    }
    
    @SuppressWarnings("unchecked")
    public <T> ExceptionHandler<T> findHandler(Class<? extends Exception> exceptionClass) {
        for (Map.Entry<Class<? extends Exception>, ExceptionHandler<?>> entry : handlers.entrySet()) {
            if (entry.getKey().isAssignableFrom(exceptionClass)) {
                return (ExceptionHandler<T>) entry.getValue();
            }
        }
        return null;
    }
    
    // Builder pattern
    public static class Builder {
        private RetryPolicy defaultRetryPolicy = RetryPolicy.noRetry();
        private CircuitBreaker circuitBreaker;
        private ErrorReporter reporter = (e, ctx) -> {};
        private Map<Class<? extends Exception>, ExceptionHandler<?>> handlers = new HashMap<>();
        
        public Builder withRetryPolicy(RetryPolicy policy) {
            this.defaultRetryPolicy = policy;
            return this;
        }
        
        public Builder withCircuitBreaker(CircuitBreaker breaker) {
            this.circuitBreaker = breaker;
            return this;
        }
        
        public Builder withReporter(ErrorReporter reporter) {
            this.reporter = reporter;
            return this;
        }
        
        public <E extends Exception> Builder withHandler(
                Class<E> type, ExceptionHandler<E> handler) {
            handlers.put(type, handler);
            return this;
        }
        
        public ExceptionHandlingFramework build() {
            return new ExceptionHandlingFramework(this);
        }
    }
    
    // Interfaces
    @FunctionalInterface
    public interface ExceptionHandler<T extends Exception> {
        void handle(T exception, String context);
    }
    
    @FunctionalInterface
    public interface ErrorReporter {
        void report(Exception e, String context);
    }
    
    // Retry policy
    public static class RetryPolicy {
        private final int maxRetries;
        private final long baseDelayMs;
        private final double multiplier;
        private final Set<Class<? extends Exception>> retryableExceptions;
        
        private RetryPolicy(int maxRetries, long baseDelayMs, double multiplier,
                          Set<Class<? extends Exception>> retryableExceptions) {
            this.maxRetries = maxRetries;
            this.baseDelayMs = baseDelayMs;
            this.multiplier = multiplier;
            this.retryableExceptions = retryableExceptions;
        }
        
        public static RetryPolicy noRetry() {
            return new RetryPolicy(0, 0, 1, Collections.emptySet());
        }
        
        public static RetryPolicy of(int maxRetries, long baseDelayMs) {
            return new RetryPolicy(maxRetries, baseDelayMs, 2, 
                Set.of(IOException.class, TimeoutException.class));
        }
        
        public boolean shouldRetry(int attempt, Exception e) {
            return attempt <= maxRetries && retryableExceptions.stream()
                .anyMatch(clazz -> clazz.isInstance(e));
        }
        
        public long getDelayMs(int attempt) {
            return (long) (baseDelayMs * Math.pow(multiplier, attempt - 1));
        }
        
        public int getMaxRetries() { return maxRetries; }
        public long getBaseDelayMs() { return baseDelayMs; }
    }
    
    // Circuit breaker
    public static class CircuitBreaker {
        private final int failureThreshold;
        private final long resetTimeoutMs;
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private final AtomicLong lastFailureTime = new AtomicLong(0);
        private volatile State state = State.CLOSED;
        
        public CircuitBreaker(int failureThreshold, long resetTimeoutMs) {
            this.failureThreshold = failureThreshold;
            this.resetTimeoutMs = resetTimeoutMs;
        }
        
        public boolean allowRequest() {
            if (state == State.CLOSED) {
                return true;
            }
            
            if (state == State.OPEN) {
                if (System.currentTimeMillis() - lastFailureTime.get() > resetTimeoutMs) {
                    state = State.HALF_OPEN;
                    return true;
                }
                return false;
            }
            
            // HALF_OPEN - allow one request
            return true;
        }
        
        public void recordSuccess() {
            failureCount.set(0);
            state = State.CLOSED;
        }
        
        public void recordFailure() {
            failureCount.incrementAndGet();
            lastFailureTime.set(System.currentTimeMillis());
            
            if (failureCount.get() >= failureThreshold) {
                state = State.OPEN;
            }
        }
        
        enum State { CLOSED, OPEN, HALF_OPEN }
    }
    
    // Custom exceptions
    public static class OperationFailedException extends Exception {
        public OperationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class CircuitBreakerOpenException extends Exception {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }
    
    // Usage
    public static void main(String[] args) throws Exception {
        ExceptionHandlingFramework framework = new ExceptionHandlingFramework.Builder()
            .withRetryPolicy(RetryPolicy.of(3, 1000))
            .withCircuitBreaker(new ExceptionHandlingFramework.CircuitBreaker(5, 30000))
            .withReporter((e, ctx) -> System.out.println("Reported: " + e.getMessage()))
            .build();
        
        String result = framework.execute("fetchData", () -> {
            // Simulate operation
            if (Math.random() > 0.7) {
                throw new IOException("Simulated failure");
            }
            return "Success";
        });
        
        System.out.println("Result: " + result);
    }
}
```

## 14. Performance

### Performance Best Practices

1. **Avoid exception-based control flow**
```java
// Bad
public boolean isInteger(String str) {
    try {
        Integer.parseInt(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}

// Good
public boolean isInteger(String str) {
    return str != null && str.matches("-?\\d+");
}
```

2. **Pre-validate to avoid exceptions**
```java
// Bad
public void process(List<String> list) {
    try {
        String first = list.get(0);
    } catch (IndexOutOfBoundsException e) {
        // Handle
    }
}

// Good
public void process(List<String> list) {
    if (list.isEmpty()) {
        return;
    }
    String first = list.get(0);
}
```

3. **Cache exception messages**
```java
// Bad
throw new IllegalArgumentException("User not found: " + userId);

// Good
throw new IllegalArgumentException(
    String.format("User not found: %d", userId));
```

4. **Use static factory methods**
```java
// Bad
throw new UserNotFoundException("User not found: " + id);

// Good
throw UserNotFoundException.notFound(id);
```

### Benchmark Example

```java
public class ExceptionBenchmark {
    private static final int ITERATIONS = 1_000_000;
    
    public static void main(String[] args) {
        // Benchmark 1: Normal operation
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            normalOperation(i);
        }
        long normalTime = System.nanoTime() - start;
        
        // Benchmark 2: Exception-based
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                exceptionOperation(i);
            } catch (Exception e) {
                // Expected
            }
        }
        long exceptionTime = System.nanoTime() - start;
        
        // Benchmark 3: Pre-validation
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            validatedOperation(i);
        }
        long validatedTime = System.nanoTime() - start;
        
        System.out.printf("Normal: %d ms%n", normalTime / 1_000_000);
        System.out.printf("Exception: %d ms%n", exceptionTime / 1_000_000);
        System.out.printf("Validated: %d ms%n", validatedTime / 1_000_000);
        System.out.printf("Exception/Normal ratio: %.2f%n", 
            (double) exceptionTime / normalTime);
    }
    
    static void normalOperation(int i) {
        if (i % 100 == 0) {
            // Special case
        }
    }
    
    static void exceptionOperation(int i) throws Exception {
        if (i % 100 == 0) {
            throw new Exception("Special case");
        }
    }
    
    static void validatedOperation(int i) {
        if (i % 100 == 0) {
            // Special case
        }
    }
}
```

## 15. Best Practices

### Exception Handling Guidelines

1. **Be Specific**
```java
// Bad
try {
    riskyOperation();
} catch (Exception e) {
    // Too broad
}

// Good
try {
    riskyOperation();
} catch (FileNotFoundException e) {
    handleFileNotFound(e);
} catch (IOException e) {
    handleIOError(e);
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

3. **Use Try-With-Resources**
```java
// Bad
FileInputStream fis = null;
try {
    fis = new FileInputStream(file);
    // Use fis
} finally {
    if (fis != null) fis.close();
}

// Good
try (var fis = new FileInputStream(file)) {
    // Use fis
}
```

4. **Preserve the Cause**
```java
// Bad
try {
    riskyOperation();
} catch (Exception e) {
    throw new ApplicationException("Failed"); // Cause lost
}

// Good
try {
    riskyOperation();
} catch (Exception e) {
    throw new ApplicationException("Failed", e); // Cause preserved
}
```

5. **Document Exceptions**
```java
/**
 * Reads data from file.
 * 
 * @param filename the file to read
 * @return the file contents
 * @throws FileNotFoundException if file doesn't exist
 * @throws IOException if read fails
 */
public String readData(String filename) throws FileNotFoundException, IOException {
    // Implementation
}
```

### API Design Guidelines

1. **Use Unchecked Exceptions for Programming Errors**
```java
// Bad - checked exception for programming error
public void process(String input) throws InvalidInputException {
    if (input == null) throw new InvalidInputException("null");
}

// Good - unchecked for programming error
public void process(String input) {
    Objects.requireNonNull(input, "input");
}
```

2. **Use Checked Exceptions for Recoverable Conditions**
```java
// Good - checked for recoverable
public void readFile(String filename) throws IOException {
    // File might not exist - caller can handle
}
```

3. **Provide Meaningful Error Messages**
```java
// Bad
throw new Exception("Error");

// Good
throw new IllegalArgumentException(
    "Age must be between 0 and 150, got: " + age);
```

## 16. Common Mistakes

### Mistake 1: Catching Generic Exception

```java
// Bad
try {
    riskyOperation();
} catch (Exception e) {
    // Catches everything, including programming errors
}

// Good
try {
    riskyOperation();
} catch (SpecificException e) {
    // Only catches specific exception
}
```

### Mistake 2: Empty Catch Block

```java
// Bad
try {
    riskyOperation();
} catch (Exception e) {
    // Silent failure - bugs hidden
}

// Good
try {
    riskyOperation();
} catch (Exception e) {
    logger.error("Operation failed", e);
    throw new ApplicationException("Operation failed", e);
}
```

### Mistake 3: Exception in Finally

```java
// Bad - loses original exception
try {
    throw new RuntimeException("Original");
} finally {
    throw new RuntimeException("Finally"); // Original lost
}

// Good - handle finally exceptions
try {
    throw new RuntimeException("Original");
} finally {
    try {
        cleanup();
    } catch (Exception e) {
        logger.error("Cleanup failed", e);
    }
}
```

## 17. Pitfalls

### Pitfall 1: Resource Leak

```java
// Bad
Connection conn = dataSource.getConnection();
try {
    // Use connection
} finally {
    // What if exception here?
    conn.close(); // Might not execute
}

// Good
try (Connection conn = dataSource.getConnection()) {
    // Use connection
} // Automatically closed
```

### Pitfall 2: Exception in Constructor

```java
// Bad - object state inconsistent
public class Resource {
    public Resource() throws Exception {
        initialize();
        throw new Exception("Failed"); // Object created but failed
    }
}

// Good - use static factory
public class Resource {
    private Resource() {}
    
    public static Resource create() throws Exception {
        Resource r = new Resource();
        r.initialize();
        return r;
    }
}
```

### Pitfall 3: Thread Interruption

```java
// Bad - doesn't preserve interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Interrupt status lost
}

// Good - preserves interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore status
    throw e; // Or handle appropriately
}
```

## 18. Debugging Tips

### Debugging Exception Issues

1. **Add Contextual Logging**
```java
try {
    riskyOperation();
} catch (Exception e) {
    logger.error("Operation failed for user {} with input {}", 
        userId, input, e);
    throw e;
}
```

2. **Use Exception Filters**
```java
// Set breakpoint on catch line
// Add condition: e instanceof SpecificException
```

3. **Check Stack Trace**
```
java.lang.NullPointerException
    at com.example.MyClass.process(MyClass.java:42) ← Your code
    at com.example.MyClass.main(MyClass.java:10) ← Caller
```

4. **Inspect Exception Object**
- Message
- Cause chain
- Stack trace
- Suppressed exceptions

### Debugging Checklist

- [ ] Read the full stack trace
- [ ] Find your code in the trace
- [ ] Check the exception message
- [ ] Follow the cause chain
- [ ] Check for suppressed exceptions
- [ ] Verify resource cleanup
- [ ] Check thread state

## 19. Comparison Table

### Exception Handling Approaches

| Approach | Pros | Cons | When to Use |
|----------|------|------|-------------|
| Try-catch | Explicit handling | Verbose | General use |
| Try-with-resources | Automatic cleanup | Only for AutoCloseable | Resource management |
| Multi-catch | Less code | Same handling | Identical recovery |
| Catch-rethrow | Preserves exception | Still need handler | Delegation |

### Exception Types

| Type | Declaration | Handling | Use Case |
|------|-------------|----------|----------|
| Checked | Required | Required | Recoverable |
| Unchecked | Optional | Optional | Programming errors |
| Error | Optional | Not recommended | System errors |

## 20. Decision Tree

### Exception Handling Decision

```
Should you catch or declare?
├── Is it recoverable?
│   ├── Yes → Catch and handle
│   └── No → Declare (checked) or throw (unchecked)
├── Is it programming error?
│   ├── Yes → Fix the code, don't catch
│   └── No → Handle appropriately
└── Should you retry?
    ├── Yes → Implement retry logic
    └── No → Handle once
```

### Resource Management Decision

```
Do you have resources to manage?
├── Yes
│   ├── AutoCloseable?
│   │   ├── Yes → Try-with-resources
│   │   └── No → Try-finally
│   └. Need complex cleanup?
│       ├── Yes → Try-finally with helpers
│       └. No → Try-with-resources
└. No → No resource management needed
```

## 21. Interview Questions

### Q1: What are the best practices for exception handling?

**Answer:**
1. Catch specific exceptions
2. Don't swallow exceptions
3. Use try-with-resources
4. Preserve the cause
5. Document exceptions
6. Use appropriate exception types
7. Log exceptions properly
8. Handle exceptions at appropriate level

### Q2: When should you use checked vs unchecked exceptions?

**Answer:**
- Checked: Recoverable conditions, caller must handle
- Unchecked: Programming errors, optional handling

### Q3: How do you handle exceptions in lambdas?

**Answer:**
Wrap checked exceptions in RuntimeException:
```java
list.forEach(item -> {
    try {
        process(item);
    } catch (CheckedException e) {
        throw new RuntimeException(e);
    }
});
```

### Q4: What is exception chaining and why is it important?

**Answer:**
Exception chaining preserves the original cause when wrapping exceptions. It's important for debugging as it maintains the full context of what went wrong.

### Q5: How do you test exception handling code?

**Answer:**
- Use try-catch in tests
- Verify exception type and message
- Check cause chain
- Test recovery logic
- Use @Test(expected) or assertThrows

## 22. Exercises

### Exercise 1: Exception Review

Review the following code and identify exception handling issues:

```java
public void process(String filename) {
    try {
        FileReader reader = new FileReader(filename);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int value = Integer.parseInt(line);
        System.out.println(100 / value);
    } catch (Exception e) {
    }
}
```

### Exercise 2: Best Practices Implementation

Refactor the following code to follow best practices:

```java
public User getUser(String id) throws Exception {
    try {
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id=" + id);
        if (rs.next()) {
            return new User(rs.getString("name"), rs.getString("email"));
        }
        throw new Exception("Not found");
    } catch (Exception e) {
        return null;
    }
}
```

### Exercise 3: Exception Handler Design

Design an exception handler that:
- Logs exceptions with context
- Implements retry logic
- Handles different exception types differently
- Preserves the original exception

## 23. Assignments

### Assignment 1: API Error Handler

Create an API error handler that:
- Catches all exceptions
- Returns appropriate HTTP status codes
- Logs errors with context
- Preserves exception cause

### Assignment 2: Resource Manager

Build a resource manager that:
- Manages multiple resources
- Ensures cleanup in all cases
- Handles cleanup failures
- Supports nested resources

### Assignment 3: Exception Logger

Create an exception logger that:
- Logs to different outputs
- Formats stack traces
- Includes context information
- Supports different log levels

## 24. Mini Project

### Exception Handling Library

Create a comprehensive exception handling library with:
1. Exception hierarchy for different domains
2. Retry mechanisms with configurable policies
3. Circuit breaker pattern
4. Error aggregation and reporting
5. Resource management utilities
6. Exception logging utilities

## 25. Summary

### Key Takeaways

- Be specific in exception handling
- Don't swallow exceptions
- Use try-with-resources for resource management
- Preserve exception causes
- Log exceptions with context
- Document exceptions in APIs
- Use appropriate exception types
- Test exception handling code
- Follow performance best practices

### Best Practices Checklist

- [ ] Catch specific exceptions
- [ ] Don't swallow exceptions
- [ ] Use try-with-resources
- [ ] Preserve exception causes
- [ ] Log exceptions properly
- [ ] Document exceptions
- [ ] Use appropriate exception types
- [ ] Validate inputs early
- [ ] Don't use exceptions for control flow
- [ ] Test exception paths

## 26. References

### Official Documentation
- [Java SE Exception Handling](https://docs.oracle.com/javase/tutorial/essential/exceptions/)
- [Java SE Try-With-Resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)

### Books
- "Effective Java" by Joshua Bloch
- "Clean Code" by Robert Martin
- "Java Concurrency in Practice" by Brian Goetz

### Online Resources
- [Baeldung - Java Exception Handling](https://www.baeldung.com/java-exceptions)
- [Baeldung - Java Best Practices](https://www.baeldung.com/java-best-practices)

## 27. Next Steps

Now that you understand best practices, proceed to:
- **08-real-world**: Learn about real-world exception handling scenarios
