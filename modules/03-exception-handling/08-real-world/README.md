# Real-World Exception Handling

## Introduction

Apply exception handling concepts to real-world scenarios including layered architectures, web applications, and enterprise systems.

## Learning Objectives

- Implement exception handling in multi-layered applications
- Design custom exception hierarchies for enterprises
- Handle exceptions in REST APIs
- Implement retry and recovery mechanisms

## Prerequisites

- All previous exception handling topics
- Basic understanding of layered architecture
- REST API concepts

## Why This Matters

Real-world applications require sophisticated exception handling strategies that span multiple layers and components.

## Syntax/Patterns

```java
// Global exception handler (Spring)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
    }
}

// Result pattern (no exceptions for business logic)
public class Result<T> {
    private T data;
    private Error error;

    public static <T> Result<T> success(T data) { ... }
    public static <T> Result<T> failure(Error error) { ... }
}
```

## Examples

```java
// Example 1: Layered exception handling
// DAO Layer
public class UserDao {
    public User findById(Long id) throws DataAccessException {
        try {
            return jdbcTemplate.queryForObject(SQL, new UserRowMapper(), id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find user: " + id, e);
        }
    }
}

// Service Layer
public class UserService {
    public User getUser(Long id) throws ServiceException {
        try {
            return userDao.findById(id);
        } catch (DataAccessException e) {
            throw new ServiceException("User service failed", e);
        }
    }
}

// Controller Layer
@RestController
public class UserController {
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (ServiceException e) {
            return ResponseEntity.status(500).body(new ErrorResponse(e.getMessage()));
        }
    }
}

// Example 2: Retry mechanism
public class RetryableOperation {
    public <T> T executeWithRetry(Supplier<T> operation, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return operation.get();
            } catch (TransientException e) {
                attempts++;
                if (attempts == maxRetries) {
                    throw new OperationFailedException("Max retries exceeded", e);
                }
                wait(attempts * 1000);  // Exponential backoff
            }
        }
        throw new OperationFailedException("Operation failed");
    }
}

// Example 3: Circuit breaker pattern
public class CircuitBreaker {
    private int failureCount = 0;
    private final int threshold;
    private CircuitState state = CircuitState.CLOSED;

    public <T> T execute(Supplier<T> operation) {
        if (state == CircuitState.OPEN) {
            throw new CircuitOpenException("Circuit is open");
        }
        try {
            T result = operation.get();
            reset();
            return result;
        } catch (Exception e) {
            recordFailure();
            throw e;
        }
    }
}
```

## Exercises

1. Design an exception hierarchy for an e-commerce application with at least 5 custom exceptions.
2. Implement a global exception handler for a REST API.
3. Create a retry mechanism that handles transient failures.

## Interview Questions

- How do you handle exceptions across microservices?
- What is the Circuit Breaker pattern and when would you use it?
- How do you test exception handling code?

## Common Pitfalls

- Not considering the entire call stack when handling exceptions
- Losing exception context in layered applications
- Not implementing proper error responses in APIs

## Best Practices

1. Create a clear exception hierarchy
2. Use global exception handlers for APIs
3. Implement retry for transient failures
4. Log exceptions with context
5. Return meaningful error responses
6. Consider idempotency in retry logic
7. Monitor exception rates

## Real World Applications

- Spring @ControllerAdvice for global handling
- Netflix Hystrix for circuit breaking
- Resilience4j for fault tolerance
- Distributed tracing with exception context

## References

- [Spring Exception Handling](https://spring.io/guides/gs/rest-service/)
- [Microservices Patterns](https://www.oreilly.com/library/view/microservices-patterns/9781617294549/)
- [Resilience4j Documentation](https://resilience4j.readme.io/)

## Summary

In this topic, you learned how to apply exception handling in real-world scenarios, including layered architectures, APIs, and fault-tolerant systems. Practice with the exercises before building the mini-project.
