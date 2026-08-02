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

## Source Code

Java source files are located in:
```
src/main/java/academy/javaengineering/exceptionhandling/
├── ExceptionHandlingIntro.java
├── TryCatchExamples.java
├── FinallyExamples.java
├── ThrowExamples.java
├── ThrowsExamples.java
├── CustomExceptionsExamples.java
├── BestPracticesExamples.java
├── RealWorldExamples.java
├── ExceptionHandlingFramework.java
├── ExceptionHierarchyDemo.java
├── PerformanceExamples.java
├── DebuggingExamples.java
├── CommonMistakesExamples.java
├── InterviewQuestionsExamples.java
├── ExercisesExamples.java
├── AssignmentsExamples.java
└── SummaryExamples.java
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
