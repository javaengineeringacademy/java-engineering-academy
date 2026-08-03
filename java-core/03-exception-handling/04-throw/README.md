# Throw Keyword

## 1. Introduction

The `throw` keyword in Java is used to explicitly抛出 an exception. It allows you to create and throw custom exceptions or predefined exceptions when a specific error condition is detected. This lesson covers the proper use of throw, exception creation, and when to use throw versus letting exceptions propagate naturally.

## 2. Learning Objectives

By the end of this lesson, you will be able to:

- Use the throw keyword correctly
- Create and throw exception objects
- Understand when to throw exceptions
- Differentiate between throw and throws
- Implement proper exception propagation
- Apply throw in method chaining
- Follow throw best practices

## 3. Prerequisites

- Understanding of try-catch blocks
- Knowledge of exception hierarchy
- Familiarity with method signatures
- Basic OOP concepts

## 4. Why This Concept Exists

### The Problem

Sometimes you need to signal an error condition explicitly:

```java
public void setAge(int age) {
    // How do we signal that age is invalid?
    this.age = age; // Bad - allows invalid state
}
```

### The Solution

Throw an exception to signal the error:

```java
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age: " + age);
    }
    this.age = age;
}
```

## 5. Problem Statement

### Challenge 1: Explicit Error Signaling

How do you signal that a method received invalid input or encountered an error condition?

### Challenge 2: Exception Creation

How do you create meaningful exception objects with useful information?

### Challenge 3: Exception Propagation

How do you throw exceptions from nested method calls?

### Challenge 4: Exception Chaining

How do you preserve the original cause when wrapping exceptions?

## 6. Theory

### Throw Syntax

```java
throw new ExceptionType("Message", cause);
```

### Throw Behavior

1. Creates a new exception object
2. Immediately terminates normal execution
3. Passes the exception to the JVM for handling
4. Begins stack unwinding

### Throw vs Throws

- `throw`: Used in method body to抛出 an exception
- `throws`: Used in method signature to declare exceptions

### When to Throw

- Input validation failures
- Business rule violations
- State inconsistency detection
- Resource unavailability
- Configuration errors

## 7. Internal Working

### Bytecode Implementation

The `throw` keyword compiles to the `athrow` instruction:

```
aload_1           // Load exception object
athrow            // Throw the exception
```

### JVM Behavior

1. The `athrow` instruction pops the exception from the operand stack
2. The JVM searches for an exception handler
3. If found, control transfers to the handler
4. If not found, the exception propagates to the calling method

## 8. JVM Perspective

### Stack Frame Management

When throw is executed:
1. The current stack frame is examined
2. The exception table is consulted
3. If a handler exists, the operand stack is cleared and the exception is pushed
4. If no handler, the frame is popped and the process repeats

### Exception Object Creation

The JVM creates the exception object with:
- Message string
- Stack trace (filled lazily)
- Cause (if provided)
- Suppressed exceptions list

## 9. Memory Representation

### Exception Object Lifecycle

```
Creation:
1. allocate() → Exception object on heap
2. init() → Set message, cause, stack trace
3. athrow → Push to operand stack

Propagation:
1. Stack frame examination
2. Exception table lookup
3. Stack unwinding
4. Handler execution
```

### Memory Layout

```
Exception Object
├── Object Header
├── message (String)
├── cause (Throwable)
├── stackTrace (StackTraceElement[])
├── suppressedExceptions (Throwable[])
└── backtrace (Object - JVM internal)
```

## 10. Syntax

### Basic Throw

```java
throw new Exception("Something went wrong");
```

### Throw with Cause

```java
throw new Exception("Something went wrong", originalException);
```

### Throw Custom Exception

```java
throw new CustomException("Custom error", errorCode, originalException);
```

### Throw in Method Chain

```java
public void method() throws CustomException {
    try {
        riskyOperation();
    } catch (Exception e) {
        throw new CustomException("Method failed", e);
    }
}
```

## 11. Easy Example

### Basic Throw Usage

```java
public class BasicThrow {
    public static void main(String[] args) {
        try {
            int age = -5;
            validateAge(age);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
    }
    
    static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        System.out.println("Valid age: " + age);
    }
}
```

### Throw Different Exception Types

```java
public class ThrowDifferentTypes {
    public static void main(String[] args) {
        // Throw RuntimeException
        try {
            throw new RuntimeException("Runtime error");
        } catch (RuntimeException e) {
            System.out.println("Runtime: " + e.getMessage());
        }
        
        // Throw checked exception
        try {
            throw new Exception("Checked error");
        } catch (Exception e) {
            System.out.println("Checked: " + e.getMessage());
        }
        
        // Throw Error (not recommended)
        try {
            throw new Error("Error");
        } catch (Error e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

## 12. Medium Example

### Input Validation with Throw

```java
public class InputValidator {
    private final String name;
    private final int age;
    private final String email;
    
    public InputValidator(String name, int age, String email) {
        this.name = validateName(name);
        this.age = validateAge(age);
        this.email = validateEmail(email);
    }
    
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new IllegalArgumentException(
                "Name must be between 2 and 50 characters: " + name);
        }
        if (!name.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException(
                "Name must contain only letters and spaces: " + name);
        }
        return name.trim();
    }
    
    private int validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        return age;
    }
    
    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return email.trim().toLowerCase();
    }
    
    public static void main(String[] args) {
        try {
            InputValidator validator = new InputValidator("John Doe", 30, "john@example.com");
            System.out.println("Valid input: " + validator.name);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
    }
}
```

### Throw with Exception Chaining

```java
public class ExceptionChainingExample {
    public static void main(String[] args) {
        try {
            processData();
        } catch (DataProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
            System.out.println("Root cause: " + 
                (e.getCause().getCause() != null ? 
                 e.getCause().getCause().getMessage() : "None"));
        }
    }
    
    static void processData() throws DataProcessingException {
        try {
            parseData();
        } catch (ParseException e) {
            // Wrap and rethrow with context
            throw new DataProcessingException("Failed to process data", e);
        }
    }
    
    static void parseData() throws ParseException {
        try {
            Integer.parseInt("invalid");
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid data format", 0);
        }
    }
    
    static class DataProcessingException extends Exception {
        DataProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    static class ParseException extends Exception {
        private final int errorOffset;
        
        ParseException(String message, int errorOffset) {
            super(message);
            this.errorOffset = errorOffset;
        }
        
        int getErrorOffset() { return errorOffset; }
    }
}
```

## 13. Hard Example

### Custom Exception Builder Pattern

```java
public class ExceptionBuilder {
    private String message;
    private Throwable cause;
    private ErrorCode code;
    private Map<String, Object> context;
    
    private ExceptionBuilder() {
        this.context = new HashMap<>();
    }
    
    public static ExceptionBuilder create() {
        return new ExceptionBuilder();
    }
    
    public ExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }
    
    public ExceptionBuilder cause(Throwable cause) {
        this.cause = cause;
        return this;
    }
    
    public ExceptionBuilder code(ErrorCode code) {
        this.code = code;
        return this;
    }
    
    public ExceptionBuilder context(String key, Object value) {
        this.context.put(key, value);
        return this;
    }
    
    public BusinessException build() {
        return new BusinessException(message, code, cause, context);
    }
    
    public void throwIt() throws BusinessException {
        throw build();
    }
    
    public enum ErrorCode {
        VALIDATION_ERROR,
        NOT_FOUND,
        ALREADY_EXISTS,
        INTERNAL_ERROR
    }
    
    static class BusinessException extends Exception {
        private final ErrorCode code;
        private final Map<String, Object> context;
        
        BusinessException(String message, ErrorCode code, 
                         Throwable cause, Map<String, Object> context) {
            super(message, cause);
            this.code = code;
            this.context = new HashMap<>(context);
        }
        
        public ErrorCode getCode() { return code; }
        public Map<String, Object> getContext() { return context; }
        
        @Override
        public String toString() {
            return String.format("BusinessException{code=%s, message='%s', context=%s}", 
                code, getMessage(), context);
        }
    }
    
    // Usage
    public static void main(String[] args) {
        try {
            String input = "invalid";
            
            if (!isValid(input)) {
                ExceptionBuilder.create()
                    .message("Invalid input")
                    .code(ErrorCode.VALIDATION_ERROR)
                    .context("input", input)
                    .context("field", "username")
                    .throwIt();
            }
        } catch (BusinessException e) {
            System.out.println(e);
            System.out.println("Error code: " + e.getCode());
            System.out.println("Context: " + e.getContext());
        }
    }
    
    static boolean isValid(String input) {
        return input != null && input.matches("[a-zA-Z0-9]+");
    }
}
```

### Exception Registry Pattern

```java
import java.util.*;
import java.util.function.Supplier;

public class ExceptionRegistry {
    private final Map<String, Supplier<? extends Exception>> registry;
    private final Map<String, ExceptionFactory<?>> factories;
    
    public ExceptionRegistry() {
        this.registry = new ConcurrentHashMap<>();
        this.factories = new ConcurrentHashMap<>();
        registerDefaults();
    }
    
    private void registerDefaults() {
        register("VALIDATION", IllegalArgumentException::new);
        register("NOT_FOUND", msg -> new NoSuchElementException(msg));
        register("AUTH", msg -> new SecurityException(msg));
        register("IO", msg -> new java.io.IOException(msg));
    }
    
    public void register(String code, Supplier<? extends Exception> factory) {
        registry.put(code, factory);
    }
    
    public <T extends Exception> void registerFactory(String code, 
            ExceptionFactory<T> factory) {
        factories.put(code, factory);
    }
    
    public Exception create(String code, String message) {
        Supplier<? extends Exception> supplier = registry.get(code);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown exception code: " + code);
        }
        
        Exception e = supplier.get();
        // Set message via reflection or use factory pattern
        return e;
    }
    
    public <T extends Exception> T create(String code, String message, Object... args) {
        ExceptionFactory<T> factory = (ExceptionFactory<T>) factories.get(code);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown exception code: " + code);
        }
        return factory.create(message, args);
    }
    
    public void throwException(String code, String message) throws Exception {
        throw create(code, message);
    }
    
    interface ExceptionFactory<T extends Exception> {
        T create(String message, Object... args);
    }
    
    // Usage
    public static void main(String[] args) {
        ExceptionRegistry registry = new ExceptionRegistry();
        
        try {
            registry.throwException("VALIDATION", "Invalid email");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
```

## 14. Performance

### Throw Performance

**Cost of throw:**
- Exception object creation: ~1-5 microseconds
- Stack trace filling: ~10-100 microseconds
- Stack unwinding: ~1-2 microseconds per frame

### Best Practices

1. **Pre-validate to avoid throws**
```java
// Bad
public void process(String input) {
    if (input == null) throw new IllegalArgumentException("null");
    // Process
}

// Good
public void process(String input) {
    Objects.requireNonNull(input, "input");
    // Process
}
```

2. **Cache exception messages**
```java
// Bad
throw new IllegalArgumentException("User not found: " + userId);

// Good
throw new IllegalArgumentException(
    String.format("User not found: %d", userId));
```

3. **Use static factory methods**
```java
// Bad
throw new UserNotFoundException("User not found: " + id);

// Good
throw UserNotFoundException.notFound(id);
```

## 15. Best Practices

### Throw Guidelines

1. **Be Specific**
```java
// Bad
throw new Exception("Error");

// Good
throw new IllegalArgumentException("Age cannot be negative: " + age);
```

2. **Include Context**
```java
// Bad
throw new ValidationException("Invalid input");

// Good
throw new ValidationException("Invalid email format: " + email);
```

3. **Preserve Cause**
```java
try {
    riskyOperation();
} catch (Exception e) {
    throw new ApplicationException("Operation failed", e);
}
```

4. **Don't Throw Generic Exceptions**
```java
// Bad
throw new Exception("Error");

// Good
throw new BusinessException("Error", ErrorCode.INVALID_INPUT);
```

5. **Document Throw**
```java
/**
 * Validates user input.
 * @param input the input to validate
 * @throws IllegalArgumentException if input is null or empty
 * @throws ValidationException if input doesn't match pattern
 */
public void validate(String input) throws IllegalArgumentException, ValidationException {
    // Implementation
}
```

## 16. Common Mistakes

### Mistake 1: Throwing Exceptions You Can't Catch

```java
// Bad - can't catch Errors
throw new OutOfMemoryError();

// Good - throw catchable exceptions
throw new ResourceExhaustedException("Out of memory");
```

### Mistake 2: Throwing in Constructor Without Cleanup

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

### Mistake 3: Throwing Checked Exceptions from Lambdas

```java
// Bad - can't throw checked exceptions from lambdas
List<String> list = Arrays.asList("1", "2", "3");
list.stream()
    .map(s -> Integer.parseInt(s)) // Can't throw checked exception
    .collect(Collectors.toList());

// Good - wrap in unchecked exception
list.stream()
    .map(s -> {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    })
    .collect(Collectors.toList());
```

## 17. Pitfalls

### Pitfall 1: Throwing Exception in Finally

```java
// Bad - loses original exception
try {
    throw new RuntimeException("Original");
} finally {
    throw new RuntimeException("Finally"); // Original lost!
}
```

### Pitfall 2: Throwing from Finally Overwrites Return

```java
// Bad
public int test() {
    try {
        return 1;
    } finally {
        throw new RuntimeException("Finally"); // Return lost!
    }
}
```

### Pitfall 3: Throwing Null

```java
// Bad - throws NullPointerException
throw null;

// Good
throw new NullPointerException("Object cannot be null");
```

## 18. Debugging Tips

### Debugging Throw Issues

1. **Check Stack Trace**
```java
try {
    throw new Exception("Test");
} catch (Exception e) {
    e.printStackTrace();
    // Analyze stack trace
}
```

2. **Add Logging Before Throw**
```java
if (invalid) {
    logger.warn("Invalid state detected: {}", state);
    throw new ValidationException("Invalid state: " + state);
}
```

3. **Use Exception Breakpoints**
- Set breakpoint on exception type
- Inspect state when exception is thrown
- Check call stack

## 19. Comparison Table

### Throw vs Throws

| Aspect | throw | throws |
|--------|-------|--------|
| Location | Method body | Method signature |
| Purpose |抛出 an exception | Declare exceptions |
| Quantity | One at a time | Multiple declarations |
| Inheritance | Not inherited | Inherited by subclasses |
| Override | Not required | Must be compatible |

### Exception Creation Patterns

| Pattern | Use Case | Example |
|---------|----------|---------|
| New exception | Simple errors | `throw new Exception(msg)` |
| Chained exception | Wrap causes | `throw new Exception(msg, cause)` |
| Factory method | Common exceptions | `throw Exception.notFound(id)` |
| Builder pattern | Complex exceptions | `ExceptionBuilder.create()...throwIt()` |

## 20. Decision Tree

### When to Throw

```
Should you throw an exception?
├── Is it an error condition?
│   ├── Yes
│   │   ├── Is it recoverable?
│   │   │   ├── Yes → Throw checked exception
│   │   │   └── No → Throw unchecked exception
│   │   └── Is it a programming error?
│   │       ├── Yes → Throw unchecked exception
│   │       └── No → Throw checked exception
│   └── No → Don't throw
└── Is it input validation?
    ├── Yes → Throw IllegalArgumentException
    └── No → Is it state validation?
        ├── Yes → Throw IllegalStateException
        └── No → Throw appropriate exception
```

## 21. Interview Questions

### Q1: What is the difference between throw and throws?

**Answer:**
- `throw` is used in the method body to actually抛出 an exception
- `throws` is used in the method signature to declare that a method might抛出 exceptions

### Q2: Can you throw a checked exception without declaring it?

**Answer:**
No. Checked exceptions must be either caught or declared in the method signature using throws.

### Q3: What happens when you throw an exception?

**Answer:**
1. Exception object is created
2. Normal flow stops
3. JVM searches for exception handler
4. If found, execution continues there
5. If not, exception propagates up call stack

### Q4: Can you throw null?

**Answer:**
Technically yes, but it throws NullPointerException. Always throw actual exception objects.

### Q5: When should you create custom exceptions?

**Answer:**
When:
- You need to distinguish between different error types
- You want to add domain-specific information
- You need custom behavior (logging, recovery)
- Standard exceptions don't fit your domain

## 22. Exercises

### Exercise 1: Input Validator

Create a validator that throws appropriate exceptions for:
- Null inputs
- Empty strings
- Invalid numbers
- Invalid email formats

### Exercise 2: Calculator

Build a calculator that throws exceptions for:
- Division by zero
- Invalid operators
- Number overflow

### Exercise 3: File Processor

Create a file processor that throws exceptions for:
- Missing files
- Permission denied
- Invalid format

## 23. Assignments

### Assignment 1: API Validator

Create an API request validator that throws specific exceptions for each validation failure.

### Assignment 2: Configuration Loader

Build a configuration loader that throws exceptions for missing files, invalid formats, and missing required values.

### Assignment 3: Transaction Manager

Create a transaction manager that throws exceptions for invalid states and failed operations.

## 24. Mini Project

### Exception Throwing Framework

Create a framework that:
1. Provides exception builders for common scenarios
2. Supports exception chaining
3. Includes exception factories
4. Handles exception propagation
5. Logs exceptions with context

## 25. Summary

### Key Takeaways

- Use throw to explicitly signal error conditions
- Create meaningful exception messages with context
- Preserve the original cause when wrapping exceptions
- Don't throw generic exceptions - be specific
- Document thrown exceptions in Javadoc
- Pre-validate to avoid unnecessary throws
- Use static factory methods for common exceptions
- Don't throw from finally blocks (or handle carefully)

## 26. References

### Official Documentation
- [Java SE Throw](https://docs.oracle.com/en/java/javase/21/essential/exceptions/throwing.html)
- [Java SE Exception Chaining](https://docs.oracle.com/en/java/javase/21/essential/exceptions/chaining.html)

### Books
- "Effective Java" by Joshua Bloch
- "Java Concurrency in Practice" by Brian Goetz

### Online Resources
- [Baeldung - Java Throw](https://www.baeldung.com/java-exceptions)
- [Baeldung - Exception Chaining](https://www.baeldung.com/java-exception-chaining)

## 27. Next Steps

Now that you understand the throw keyword, proceed to:
- **05-throws**: Learn about declaring exceptions in method signatures
