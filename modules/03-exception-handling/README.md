# Module 03: Exception Handling

## Description

Master Java's exception handling mechanism to build robust, fault-tolerant applications. Learn to anticipate, catch, and gracefully handle runtime errors while maintaining program stability. This comprehensive module covers everything from basic try-catch blocks to advanced enterprise patterns like circuit breakers and retry mechanisms.

## Learning Objectives

By the end of this module, you will be able to:

- Understand the exception hierarchy and types in Java
- Implement proper try-catch-finally blocks
- Use throw and throws keywords effectively
- Create and use custom exceptions
- Apply best practices for exception handling
- Design fault-tolerant real-world applications
- Implement retry and circuit breaker patterns
- Build comprehensive exception handling frameworks

## Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Basic understanding of program flow and control structures

## Topics

| # | Topic | Duration | Difficulty | Description |
|---|-------|----------|------------|-------------|
| 01 | [Introduction](01-introduction/) | 30 min | Beginner | Exception handling fundamentals, hierarchy, and rationale |
| 02 | [Try-Catch](02-try-catch/) | 45 min | Beginner | Try-catch blocks, multiple catch, multi-catch |
| 03 | [Finally](03-finally/) | 30 min | Beginner | Finally block, resource cleanup, try-with-resources |
| 04 | [Throw](04-throw/) | 30 min | Intermediate | Explicitly throwing exceptions, exception creation |
| 05 | [Throws](05-throws/) | 30 min | Intermediate | Declaring exceptions, method signatures, overriding |
| 06 | [Custom Exceptions](06-custom-exceptions/) | 45 min | Intermediate | Creating custom exception classes, hierarchy design |
| 07 | [Best Practices](07-best-practices/) | 45 min | Intermediate | Exception handling guidelines and patterns |
| 08 | [Real World](08-real-world/) | 60 min | Advanced | Enterprise patterns, retry, circuit breaker |
| 09 | [Mini Project](09-mini-project/) | 90 min | Advanced | Complete exception handling framework |

**Total Estimated Time: 7-8 hours**

## Learning Path

```
Introduction → Try-Catch → Finally → Throw → Throws → Custom Exceptions
                                                              ↓
                              Mini Project ← Real World ← Best Practices
```

## Difficulty Progression

- **Beginner** (Topics 01-03): Core concepts and basic syntax
- **Intermediate** (Topics 04-07): Advanced features and patterns
- **Advanced** (Topics 08-09): Real-world applications and projects

## Key Concepts Covered

### Exception Hierarchy
```
Throwable
├── Error (serious problems - should not be caught)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── VirtualMachineError
└── Exception (conditions that application can catch and handle)
    ├── IOException (checked)
    ├── RuntimeException (unchecked)
    │   ├── NullPointerException
    │   ├── ArrayIndexOutOfBoundsException
    │   └── IllegalArgumentException
    └── Custom Exceptions
```

### Core Syntax

```java
// Basic try-catch
try {
    riskyOperation();
} catch (SpecificException e) {
    handleException(e);
} finally {
    cleanup();
}

// Throw exception
throw new CustomException("Error message", cause);

// Declare exceptions
public void method() throws IOException, SQLException;

// Try-with-resources
try (Resource resource = new Resource()) {
    use(resource);
}
```

### Enterprise Patterns

```java
// Retry mechanism
String result = retryMechanism.execute("operation", () -> {
    return riskyOperation();
});

// Circuit breaker
String result = circuitBreaker.execute(() -> {
    return remoteService.call();
});

// Recovery strategy
String result = recoveryStrategy.recover(exception);
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                   Exception Flow                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐        │
│  │  Try      │────▶│  Catch   │────▶│ Finally  │        │
│  │  Block    │     │  Block   │     │  Block   │        │
│  └──────────┘     └──────────┘     └──────────┘        │
│       │               │                 │               │
│       ▼               ▼                 ▼               │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐        │
│  │  Success │     │ Exception│     │ Resource │        │
│  │  Path    │     │ Handling │     │ Cleanup  │        │
│  └──────────┘     └──────────┘     └──────────┘        │
│                                                         │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                 Exception Hierarchy                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│                      Throwable                          │
│                         │                              │
│           ┌─────────────┴─────────────┐                │
│           │                           │                │
│         Error                     Exception            │
│           │                           │                │
│  ┌────────┴────────┐      ┌───────────┴──────────┐    │
│  │                 │      │                       │    │
│  │  ┌──────┐ ┌─────┐    │  ┌──────────┐ ┌───────┐  │
│  │  │ OOM  │ │ SOF │    │  │ Checked  │ │Uncheck│  │
│  │  └──────┘ └─────┘    │  │ Exception│ │  ed   │  │
│  │                      │  └──────────┘ └───────┘  │
│  └──────────────────────┘                          │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## Best Practices Checklist

- [ ] Catch specific exceptions, not generic Exception
- [ ] Don't swallow exceptions silently
- [ ] Use try-with-resources for automatic resource management
- [ ] Preserve exception causes when wrapping
- [ ] Log exceptions with context
- [ ] Document exceptions in Javadoc
- [ ] Use appropriate exception types (checked vs unchecked)
- [ ] Validate inputs early to avoid exceptions
- [ ] Don't use exceptions for normal control flow
- [ ] Test exception paths thoroughly

## Module Resources

- [Java Exception Handling Official Docs](https://docs.oracle.com/en/java/javase/21/essential/exceptions/)
- [Java SE Throwable Class API](https://docs.oracle.com/javase/21/docs/api/java.base/java/lang/Throwable.html)
- [Effective Java - Item 69: Use exceptions only for exceptional conditions](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Baeldung - Java Exceptions](https://www.baeldung.com/java-exceptions)

## Performance Comparison

| Operation | Time Complexity | Space | Notes |
|-----------|----------------|-------|-------|
| try-catch | O(1) | Minimal | No overhead when no exception |
| Exception creation | O(1) | Stack trace | Expensive for deep stacks |
| Checked exceptions | O(1) | Minimal | Compile-time only |
| Unchecked exceptions | O(1) | Minimal | Runtime overhead |
| Retry mechanism | O(n) | Low | Depends on retry count |
| Circuit breaker | O(1) | Low | State machine |

## Exception Handling Patterns

### 1. Guard Clause Pattern
```java
public void processOrder(Order order) {
    if (order == null) {
        throw new IllegalArgumentException("Order cannot be null");
    }
    if (order.getItems().isEmpty()) {
        throw new EmptyOrderException("Order has no items");
    }
    // Process order
}
```

### 2. Exception Translation Pattern
```java
public UserDTO getUser(Long id) {
    try {
        User user = userRepository.findById(id);
        return UserMapper.toDTO(user);
    } catch (DataAccessException e) {
        throw new ServiceException("Failed to fetch user", e);
    }
}
```

### 3. Recovery Pattern
```java
public String readFileWithRecovery(String path) {
    try {
        return Files.readString(Path.of(path));
    } catch (FileNotFoundException e) {
        logger.warn("File not found, using default: {}", path);
        return getDefaultContent(path);
    } catch (IOException e) {
        throw new ServiceException("Failed to read file", e);
    }
}
```

## Common Pitfalls

| Pitfall | Description | Solution |
|---------|-------------|----------|
| Swallowing exceptions | `catch (Exception e) {}` | Always log or rethrow |
| Catching too broad | `catch (Exception e)` | Catch specific exceptions |
| Ignoring finally | Not cleaning up resources | Use try-with-resources |
| Exception in finally | Exception masking | Be careful with finally blocks |
| Empty catch blocks | Silent failure | Log or handle properly |

## Interview Questions

### Q1: What is the difference between checked and unchecked exceptions?
**Answer:** Checked exceptions must be declared or caught (IOException). Unchecked exceptions don't require handling (RuntimeException).

### Q2: When should you use custom exceptions?
**Answer:** When you need domain-specific exception types with meaningful names and additional context.

### Q3: What is exception chaining?
**Answer:** Preserving the original exception as the cause when wrapping in a new exception.

### Q4: How do you handle exceptions in streams?
**Answer:** Use try-catch inside map/flatMap, or create custom stream operations.

### Q5: What is the difference between throw and throws?
**Answer:** `throw` creates and throws an exception. `throws` declares exceptions a method can throw.

## Assessment

After completing this module, you should be able to:

1. **Explain** the exception hierarchy and different exception types
2. **Implement** proper try-catch-finally blocks
3. **Create** custom exceptions with meaningful information
4. **Apply** best practices for exception handling
5. **Design** fault-tolerant applications with retry and circuit breaker patterns
6. **Debug** exception-related issues effectively
7. **Build** comprehensive exception handling frameworks

## Next Steps

After completing this module, proceed to:
- Module 04: Collections Framework
- Module 05: Generics
- Module 06: I/O and NIO

---

**Note:** This module contains comprehensive documentation with 27 sections per topic, including theory, examples, best practices, interview questions, exercises, and assignments. Each topic README is 400+ lines for in-depth learning.
