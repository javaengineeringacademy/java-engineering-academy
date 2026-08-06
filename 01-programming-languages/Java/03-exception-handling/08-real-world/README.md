# Real-World Exception Handling

## 1. Introduction

Real-world exception handling goes beyond basic try-catch blocks. It involves designing resilient systems that can handle failures gracefully, recover from errors, and provide meaningful feedback to users. This lesson covers practical exception handling scenarios you'll encounter in production applications.

## 2. Learning Objectives

By the end of this lesson, you will be able to:

- Handle exceptions in enterprise applications
- Implement retry and circuit breaker patterns
- Handle distributed system failures
- Implement proper logging and monitoring
- Handle transaction management
- Design fault-tolerant systems
- Debug production issues

## 3. Prerequisites

- Understanding of exception hierarchy
- Knowledge of try-catch-finally
- Familiarity with custom exceptions
- Basic understanding of enterprise patterns

## 4. Why This Concept Exists

### The Problem

Real applications face complex failure scenarios:

```java
// Multiple failure points
public void processOrder(Order order) {
    validateOrder(order);        // Validation can fail
    checkInventory(order);       // Network can fail
    processPayment(order);       // Payment can fail
    updateInventory(order);      // Database can fail
    sendConfirmation(order);     // Email can fail
    // What happens when any of these fail?
}
```

### The Solution

Real-world exception handling addresses:

1. **Partial Failures**: Some operations succeed, others fail
2. **Transient Failures**: Temporary issues that can be retried
3. **Permanent Failures**: Issues that won't resolve
4. **Cascading Failures**: One failure causes others
5. **Resource Exhaustion**: System runs out of resources

## 5. Problem Statement

### Challenge 1: Distributed Failures

How do you handle failures across multiple services?

### Challenge 2: Transaction Management

How do you ensure data consistency when operations fail?

### Challenge 3: Retry Logic

How do you implement retry mechanisms without overwhelming the system?

### Challenge 4: Monitoring and Alerting

How do you detect and respond to production issues?

## 6. Theory

### Failure Categories

**Transient Failures:**
- Network timeouts
- Database locks
- Service unavailability
- Solution: Retry with backoff

**Permanent Failures:**
- Invalid data
- Permission denied
- Resource not found
- Solution: Handle immediately

**Partial Failures:**
- Some operations succeed
- Others fail
- Solution: Transaction management, compensation

### Resilience Patterns

- **Retry**: Attempt operation multiple times
- **Circuit Breaker**: Stop calling failing service
- **Fallback**: Use alternative approach
- **Timeout**: Limit wait time
- **Bulkhead**: Isolate failures

## 7. Internal Working

### Exception Propagation in Distributed Systems

```
Service A → Service B → Service C
    ↓           ↓           ↓
 Exception   Exception   Exception
    ↓           ↓           ↓
 Wrap &      Wrap &      Throw
 Propagate   Propagate
```

### Transaction Rollback

```
Begin Transaction
├── Operation 1 (Success)
├── Operation 2 (Failure)
├── Operation 3 (Not executed)
└── Rollback all operations
```

## 8. JVM Perspective

### Stack Trace in Distributed Systems

When exceptions occur across services:
1. Each service adds its context
2. Stack trace includes remote calls
3. Correlation IDs help trace requests
4. Log aggregation enables analysis

### Resource Management

JVM manages:
- Database connections
- Thread pools
- Memory
- File handles

## 9. Memory Representation

### Exception in Distributed System

```
Exception Object
├── Local Stack Trace
├── Remote Stack Traces
├. Correlation ID
├. Service Context
├ Timestamps
└. Error Codes
```

### Transaction Context

```
Transaction
├. XID (Transaction ID)
├. Branch IDs
├. Status
├. Participants
└. Log Records
```

## 10. Syntax

### Basic Enterprise Exception Handling

```java
// 1. Service layer exception handling
@Transactional
public void processOrder(Order order) throws OrderException {
    try {
        validateOrder(order);
        saveOrder(order);
        notifyWarehouse(order);
    } catch (ValidationException e) {
        throw new OrderException("Invalid order", e);
    } catch (RuntimeException e) {
        transactionManager.rollback();
        throw new OrderException("Processing failed", e);
    }
}

// 2. Controller layer exception handling
@RestController
public class OrderController {
    
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }
}
```

### Retry Pattern

```java
public <T> T executeWithRetry(Callable<T> operation, int maxRetries) 
        throws Exception {
    int attempts = 0;
    while (attempts < maxRetries) {
        try {
            return operation.call();
        } catch (Exception e) {
            attempts++;
            if (attempts == maxRetries) {
                throw e;
            }
            Thread.sleep((long) Math.pow(2, attempts) * 1000);
        }
    }
    throw new RuntimeException("Should not reach here");
}
```

## 11. Easy Example

### Basic Enterprise Exception Handling

```java
import java.sql.*;
import java.io.*;

public class BasicEnterpriseExample {
    
    // Database operation with exception handling
    public User findUser(Long id) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapUser(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find user: " + id, e);
            
        } finally {
            closeQuietly(rs);
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }
    
    // File operation with exception handling
    public Config loadConfig(String filename) throws ConfigException {
        try (var reader = new BufferedReader(new FileReader(filename))) {
            // Parse config
            return parseConfig(reader);
            
        } catch (FileNotFoundException e) {
            throw new ConfigException("Config file not found: " + filename, e);
            
        } catch (IOException e) {
            throw new ConfigException("Error reading config: " + filename, e);
            
        } catch (ParseException e) {
            throw new ConfigException("Invalid config format: " + filename, e);
        }
    }
    
    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Log but don't throw
            }
        }
    }
    
    // Custom exceptions
    static class DatabaseException extends Exception {
        DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    static class ConfigException extends Exception {
        ConfigException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
```

### REST API Exception Handling

```java
import java.util.logging.Logger;

public class UserApi {
    private static final Logger logger = Logger.getLogger(UserApi.class.getName());
    
    public ApiResponse<User> getUser(String userId) {
        try {
            // Validate input
            if (userId == null || userId.trim().isEmpty()) {
                return ApiResponse.badRequest("User ID is required");
            }
            
            // Fetch user
            User user = userService.findById(userId);
            if (user == null) {
                return ApiResponse.notFound("User not found: " + userId);
            }
            
            return ApiResponse.success(user);
            
        } catch (Exception e) {
            logger.severe("Error fetching user: " + userId + " - " + e.getMessage());
            return ApiResponse.serverError("Internal server error");
        }
    }
    
    public ApiResponse<User> createUser(CreateUserRequest request) {
        try {
            // Validate
            validateRequest(request);
            
            // Create user
            User user = userService.create(request);
            return ApiResponse.created(user);
            
        } catch (ValidationException e) {
            return ApiResponse.badRequest(e.getMessage());
            
        } catch (DuplicateException e) {
            return ApiResponse.conflict("User already exists");
            
        } catch (Exception e) {
            logger.severe("Error creating user: " + e.getMessage());
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
    
    // Response class
    static class ApiResponse<T> {
        private final int status;
        private final String message;
        private final T data;
        
        ApiResponse(int status, String message, T data) {
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


## 📑 Continue Reading

**Part 1** of 4 | Part 2 | Part 3 | Part 4

