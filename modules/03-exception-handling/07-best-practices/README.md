# Exception Handling Best Practices

## Introduction

Following best practices ensures your exception handling is effective, maintainable, and doesn't introduce new problems.

## Learning Objectives

- Apply exception handling best practices
- Avoid common anti-patterns
- Design exception-safe code
- Create robust error handling strategies

## Prerequisites

- All previous exception handling topics
- Understanding of clean code principles

## Why This Matters

Poor exception handling can make debugging difficult, hide bugs, and even cause security vulnerabilities. Best practices ensure your error handling helps rather than hinders.

## Syntax/Principles

```java
// Principle 1: Be specific
catch (FileNotFoundException e) { }  // Good
catch (Exception e) { }             // Bad (too broad)

// Principle 2: Don't ignore exceptions
catch (Exception e) {
    // Do something!
    logger.error("Error occurred", e);
}

// Principle 3: Use try-with-resources
try (InputStream is = new FileInputStream("file.txt")) {
    // Auto-closed
}
```

## Examples

```java
// Example 1: Exception wrapping
public class ExceptionWrapping {
    public ServiceResult processOrder(Order order) {
        try {
            // Process order
            return ServiceResult.success();
        } catch (DatabaseException e) {
            throw new OrderProcessingException("Failed to process order", e);
        }
    }
}

// Example 2: Recovery patterns
public class RecoveryPattern {
    public User getUser(String id) {
        try {
            return database.findUser(id);
        } catch (DatabaseException e) {
            logger.warn("Database unavailable, using cache", e);
            return cache.getUser(id);
        }
    }
}

// Example 3: Exception-safe resource management
public class ResourceManagement {
    public void processData() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Process data
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to process data", e);
        }
    }
}
```

## Exercises

1. Review the following code and identify exception handling issues:
   ```java
   try {
       readFile();
   } catch (Exception e) {
       // Do nothing
   }
   ```
2. Refactor code that catches generic Exception to catch specific exceptions.
3. Create a method that demonstrates proper exception chaining.

## Interview Questions

- Why is catching `Exception` generally bad practice?
- What is the exception translation pattern?
- How do you handle exceptions in lambdas and streams?

## Common Pitfalls

- Catching too broad exceptions
- Empty catch blocks
- Using exceptions for flow control
- Logging and rethrowing (doubles the stack trace)
- Not preserving the original exception

## Best Practices

1. Catch specific exceptions
2. Don't swollow exceptions silently
3. Use try-with-resources for AutoCloseable
4. Document exceptions in Javadoc
5. Create meaningful exception messages
6. Use exception chaining to preserve context
7. Consider performance implications
8. Validate inputs early (fail-fast)
9. Clean up resources in finally
10. Create a global exception handler

## Real World Applications

- Centralized error handling in web applications
- Retry mechanisms for transient failures
- Circuit breaker patterns
- Graceful degradation strategies

## References

- [Effective Java - Exceptions](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Exception Best Practices](https://www.oracle.com/technical-resources/articles/java/exception-handling.html)
- [Google Java Style Guide - Exceptions](https://google.github.io/styleguide/javaguide.html#s6.2-cautious-approach)

## Summary

In this topic, you learned the best practices for exception handling that will help you write robust, maintainable, and debuggable code. Apply these principles in your real-world projects.
