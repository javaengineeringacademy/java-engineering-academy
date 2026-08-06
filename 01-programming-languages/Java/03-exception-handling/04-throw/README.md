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

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

```
