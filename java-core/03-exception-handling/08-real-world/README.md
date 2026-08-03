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

## 12. Medium Example

### Transaction Management

```java
import java.sql.*;
import java.util.logging.Logger;

public class TransactionManager {
    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());
    private final DataSource dataSource;
    
    public TransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public <T> T executeInTransaction(TransactionOperation<T> operation) 
            throws TransactionException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            
            T result = operation.execute(conn);
            
            conn.commit();
            return result;
            
        } catch (Exception e) {
            rollbackQuietly(conn);
            throw new TransactionException("Transaction failed", e);
            
        } finally {
            closeQuietly(conn);
        }
    }
    
    public void executeBatchInTransaction(BatchOperation operation) 
            throws TransactionException {
        Connection conn = null;
        Savepoint savepoint = null;
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            
            operation.execute(conn);
            
            conn.commit();
            
        } catch (Exception e) {
            rollbackToSavepointQuietly(conn, savepoint);
            throw new TransactionException("Batch operation failed", e);
            
        } finally {
            closeQuietly(conn);
        }
    }
    
    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.warning("Rollback failed: " + e.getMessage());
            }
        }
    }
    
    private void rollbackToSavepointQuietly(Connection conn, Savepoint savepoint) {
        if (conn != null && savepoint != null) {
            try {
                conn.rollback(savepoint);
            } catch (SQLException e) {
                logger.warning("Rollback to savepoint failed: " + e.getMessage());
            }
        }
    }
    
    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warning("Connection close failed: " + e.getMessage());
            }
        }
    }
    
    @FunctionalInterface
    interface TransactionOperation<T> {
        T execute(Connection conn) throws Exception;
    }
    
    @FunctionalInterface
    interface BatchOperation {
        void execute(Connection conn) throws Exception;
    }
    
    static class TransactionException extends Exception {
        TransactionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    // Usage
    public static void main(String[] args) throws Exception {
        TransactionManager tm = new TransactionManager(dataSource);
        
        // Transfer money between accounts
        User result = tm.executeInTransaction(conn -> {
            // Debit from account A
            try (PreparedStatement debit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE id = ?")) {
                debit.setDouble(1, 100.0);
                debit.setLong(2, 1L);
                debit.executeUpdate();
            }
            
            // Credit to account B
            try (PreparedStatement credit = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE id = ?")) {
                credit.setDouble(1, 100.0);
                credit.setLong(2, 2L);
                credit.executeUpdate();
            }
            
            return null;
        });
    }
}
```

### Retry Mechanism

```java
import java.util.logging.Logger;
import java.util.function.Supplier;

public class RetryMechanism {
    private static final Logger logger = Logger.getLogger(RetryMechanism.class.getName());
    
    public static <T> T executeWithRetry(Supplier<T> operation, RetryPolicy policy) 
            throws RetryExhaustedException {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts <= policy.getMaxRetries()) {
            try {
                return operation.get();
                
            } catch (Exception e) {
                attempts++;
                lastException = e;
                
                logger.warning(String.format("Attempt %d failed: %s", 
                    attempts, e.getMessage()));
                
                if (!policy.shouldRetry(attempts, e)) {
                    break;
                }
                
                if (attempts <= policy.getMaxRetries()) {
                    try {
                        long delay = policy.getDelay(attempts);
                        logger.info(String.format("Retrying in %d ms", delay));
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RetryExhaustedException("Retry interrupted", ie);
                    }
                }
            }
        }
        
        throw new RetryExhaustedException(
            String.format("Operation failed after %d attempts", attempts), 
            lastException);
    }
    
    // Retry policy
    public static class RetryPolicy {
        private final int maxRetries;
        private final long baseDelay;
        private final double multiplier;
        private final java.util.Set<Class<? extends Exception>> retryableExceptions;
        
        private RetryPolicy(Builder builder) {
            this.maxRetries = builder.maxRetries;
            this.baseDelay = builder.baseDelay;
            this.multiplier = builder.multiplier;
            this.retryableExceptions = builder.retryableExceptions;
        }
        
        public boolean shouldRetry(int attempt, Exception e) {
            if (attempt > maxRetries) return false;
            return retryableExceptions.stream()
                .anyMatch(clazz -> clazz.isInstance(e));
        }
        
        public long getDelay(int attempt) {
            return (long) (baseDelay * Math.pow(multiplier, attempt - 1));
        }
        
        public int getMaxRetries() { return maxRetries; }
        
        public static class Builder {
            private int maxRetries = 3;
            private long baseDelay = 1000;
            private double multiplier = 2.0;
            private java.util.Set<Class<? extends Exception>> retryableExceptions = 
                new java.util.HashSet<>();
            
            public Builder maxRetries(int maxRetries) {
                this.maxRetries = maxRetries;
                return this;
            }
            
            public Builder baseDelay(long baseDelay) {
                this.baseDelay = baseDelay;
                return this;
            }
            
            public Builder multiplier(double multiplier) {
                this.multiplier = multiplier;
                return this;
            }
            
            public Builder retryOn(Class<? extends Exception> exceptionClass) {
                retryableExceptions.add(exceptionClass);
                return this;
            }
            
            public RetryPolicy build() {
                return new RetryPolicy(this);
            }
        }
    }
    
    static class RetryExhaustedException extends Exception {
        RetryExhaustedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    // Usage
    public static void main(String[] args) throws RetryExhaustedException {
        RetryPolicy policy = new RetryPolicy.Builder()
            .maxRetries(3)
            .baseDelay(1000)
            .multiplier(2.0)
            .retryOn(RuntimeException.class)
            .build();
        
        String result = executeWithRetry(() -> {
            // Simulate operation that might fail
            if (Math.random() > 0.7) {
                throw new RuntimeException("Transient failure");
            }
            return "Success";
        }, policy);
        
        System.out.println("Result: " + result);
    }
}
```

## 13. Hard Example

### Circuit Breaker Pattern

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class CircuitBreaker {
    private static final Logger logger = Logger.getLogger(CircuitBreaker.class.getName());
    
    private final int failureThreshold;
    private final long resetTimeout;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicLong lastFailureTime = new AtomicLong(0);
    private volatile State state = State.CLOSED;
    
    public CircuitBreaker(int failureThreshold, long resetTimeout) {
        this.failureThreshold = failureThreshold;
        this.resetTimeout = resetTimeout;
    }
    
    public <T> T execute(Supplier<T> operation) throws CircuitBreakerException {
        if (!allowRequest()) {
            throw new CircuitBreakerException("Circuit breaker is OPEN");
        }
        
        try {
            T result = operation.get();
            recordSuccess();
            return result;
            
        } catch (Exception e) {
            recordFailure();
            throw new CircuitBreakerException("Operation failed", e);
        }
    }
    
    private boolean allowRequest() {
        if (state == State.CLOSED) {
            return true;
        }
        
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastFailureTime.get() > resetTimeout) {
                state = State.HALF_OPEN;
                logger.info("Circuit breaker transitioning to HALF_OPEN");
                return true;
            }
            return false;
        }
        
        // HALF_OPEN - allow one request
        return true;
    }
    
    private void recordSuccess() {
        failureCount.set(0);
        if (state == State.HALF_OPEN) {
            state = State.CLOSED;
            logger.info("Circuit breaker CLOSED - service recovered");
        }
    }
    
    private void recordFailure() {
        int failures = failureCount.incrementAndGet();
        lastFailureTime.set(System.currentTimeMillis());
        
        if (failures >= failureThreshold && state != State.OPEN) {
            state = State.OPEN;
            logger.warning("Circuit breaker OPEN - too many failures");
        }
    }
    
    public State getState() { return state; }
    
    public enum State { CLOSED, OPEN, HALF_OPEN }
    
    static class CircuitBreakerException extends Exception {
        CircuitBreakerException(String message) {
            super(message);
        }
        
        CircuitBreakerException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    // Usage
    public static void main(String[] args) {
        CircuitBreaker breaker = new CircuitBreaker(3, 5000);
        
        for (int i = 0; i < 10; i++) {
            try {
                String result = breaker.execute(() -> {
                    // Simulate failing service
                    if (Math.random() > 0.5) {
                        throw new RuntimeException("Service unavailable");
                    }
                    return "Success";
                });
                System.out.println("Attempt " + i + ": " + result);
                
            } catch (CircuitBreakerException e) {
                System.out.println("Attempt " + i + ": " + e.getMessage());
            }
            
            System.out.println("State: " + breaker.getState());
        }
    }
}
```

### Distributed Exception Handling

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class DistributedExceptionHandler {
    private static final Logger logger = Logger.getLogger(DistributedExceptionHandler.class.getName());
    private final Map<String, ServiceClient> services;
    private final CircuitBreakerRegistry circuitBreakers;
    private final RetryPolicy defaultRetryPolicy;
    
    public DistributedExceptionHandler() {
        this.services = new ConcurrentHashMap<>();
        this.circuitBreakers = new CircuitBreakerRegistry();
        this.defaultRetryPolicy = RetryPolicy.of(3, 1000, 2.0);
    }
    
    public <T> T executeDistributed(String serviceName, Supplier<T> operation) 
            throws DistributedException {
        
        CircuitBreaker breaker = circuitBreakers.getOrCreate(serviceName);
        
        if (!breaker.allowRequest()) {
            throw new DistributedException(
                serviceName, "Circuit breaker is open", null);
        }
        
        try {
            T result = executeWithRetry(operation, defaultRetryPolicy);
            breaker.recordSuccess();
            return result;
            
        } catch (Exception e) {
            breaker.recordFailure();
            throw new DistributedException(
                serviceName, "Operation failed", e);
        }
    }
    
    private <T> T executeWithRetry(Supplier<T> operation, RetryPolicy policy) 
            throws Exception {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts <= policy.getMaxRetries()) {
            try {
                return operation.get();
                
            } catch (Exception e) {
                attempts++;
                lastException = e;
                
                if (!policy.shouldRetry(attempts, e)) {
                    break;
                }
                
                if (attempts <= policy.getMaxRetries()) {
                    Thread.sleep(policy.getDelay(attempts));
                }
            }
        }
        
        throw lastException;
    }
    
    // Service client with exception handling
    static class ServiceClient {
        private final String name;
        private final CircuitBreaker circuitBreaker;
        
        ServiceClient(String name) {
            this.name = name;
            this.circuitBreaker = new CircuitBreaker(3, 30000);
        }
        
        public <T> T execute(Supplier<T> operation) throws ServiceClientException {
            if (!circuitBreaker.allowRequest()) {
                throw new ServiceClientException("Circuit breaker open for: " + name);
            }
            
            try {
                T result = operation.get();
                circuitBreaker.recordSuccess();
                return result;
                
            } catch (Exception e) {
                circuitBreaker.recordFailure();
                throw new ServiceClientException("Service call failed", e);
            }
        }
    }
    
    static class ServiceClientException extends Exception {
        ServiceClientException(String message) { super(message); }
        ServiceClientException(String message, Throwable cause) { super(message, cause); }
    }
    
    // Circuit breaker registry
    static class CircuitBreakerRegistry {
        private final Map<String, CircuitBreaker> breakers = new ConcurrentHashMap<>();
        
        CircuitBreaker getOrCreate(String name) {
            return breakers.computeIfAbsent(name, 
                k -> new CircuitBreaker(3, 30000));
        }
    }
    
    // Retry policy
    static class RetryPolicy {
        private final int maxRetries;
        private final long baseDelay;
        private final double multiplier;
        private final Set<Class<? extends Exception>> retryableExceptions;
        
        private RetryPolicy(int maxRetries, long baseDelay, double multiplier,
                          Set<Class<? extends Exception>> retryableExceptions) {
            this.maxRetries = maxRetries;
            this.baseDelay = baseDelay;
            this.multiplier = multiplier;
            this.retryableExceptions = retryableExceptions;
        }
        
        static RetryPolicy of(int maxRetries, long baseDelay, double multiplier) {
            return new RetryPolicy(maxRetries, baseDelay, multiplier,
                Set.of(RuntimeException.class));
        }
        
        boolean shouldRetry(int attempt, Exception e) {
            if (attempt > maxRetries) return false;
            return retryableExceptions.stream()
                .anyMatch(clazz -> clazz.isInstance(e));
        }
        
        long getDelay(int attempt) {
            return (long) (baseDelay * Math.pow(multiplier, attempt - 1));
        }
        
        int getMaxRetries() { return maxRetries; }
    }
    
    // Distributed exception
    static class DistributedException extends Exception {
        private final String serviceName;
        
        DistributedException(String serviceName, String message, Throwable cause) {
            super(String.format("[%s] %s", serviceName, message), cause);
            this.serviceName = serviceName;
        }
        
        String getServiceName() { return serviceName; }
    }
    
    // Usage
    public static void main(String[] args) throws DistributedException {
        DistributedExceptionHandler handler = new DistributedExceptionHandler();
        
        String result = handler.executeDistributed("user-service", () -> {
            // Call to user service
            return "User data";
        });
        
        System.out.println("Result: " + result);
    }
}
```

## 14. Performance

### Production Considerations

1. **Exception Logging Performance**
```java
// Bad - expensive string concatenation
logger.severe("Error: " + e.getMessage() + " for user: " + userId);

// Good - lazy evaluation
logger.log(Level.SEVERE, "Error: {0} for user: {1}", 
    new Object[]{e.getMessage(), userId});
```

2. **Stack Trace Performance**
```java
// Bad - always fills stack trace
throw new Exception("Error");

// Good - cache for frequently thrown exceptions
private static final Exception COMMON_ERROR = new Exception("Common error");
throw COMMON_ERROR;
```

3. **Retry Backoff**
```java
// Bad - fixed delay causes thundering herd
Thread.sleep(1000);

// Good - exponential backoff with jitter
long delay = baseDelay * (long) Math.pow(2, attempt);
delay += random.nextLong(delay / 2); // Add jitter
Thread.sleep(delay);
```

### Monitoring Metrics

```java
public class ExceptionMetrics {
    private final AtomicInteger totalExceptions = new AtomicInteger(0);
    private final Map<String, AtomicInteger> exceptionsByType = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> exceptionsByService = new ConcurrentHashMap<>();
    
    public void recordException(Exception e, String service) {
        totalExceptions.incrementAndGet();
        
        exceptionsByType.computeIfAbsent(e.getClass().getSimpleName(), 
            k -> new AtomicInteger(0)).incrementAndGet();
        
        exceptionsByService.computeIfAbsent(service, 
            k -> new AtomicInteger(0)).incrementAndGet();
    }
    
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("total", totalExceptions.get());
        metrics.put("byType", new HashMap<>(exceptionsByType));
        metrics.put("byService", new HashMap<>(exceptionsByService));
        return metrics;
    }
}
```

## 15. Best Practices

### Enterprise Exception Handling

1. **Layered Exception Handling**
```java
// DAO layer - wrap SQL exceptions
public User findById(Long id) throws DataAccessException {
    try {
        // JDBC code
    } catch (SQLException e) {
        throw new DataAccessException("Failed to find user", e);
    }
}

// Service layer - handle business exceptions
public User getUser(Long id) throws ServiceException {
    try {
        return userDao.findById(id);
    } catch (DataAccessException e) {
        throw new ServiceException("User retrieval failed", e);
    }
}

// Controller layer - handle HTTP exceptions
@GetMapping("/users/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {
    try {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    } catch (ServiceException e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
```

2. **Exception Translation**
```java
public class ExceptionTranslator {
    public static ServiceException translate(Exception e) {
        if (e instanceof DataAccessException) {
            return new ServiceException("Database error", e);
        }
        if (e instanceof IOException) {
            return new ServiceException("IO error", e);
        }
        return new ServiceException("Unexpected error", e);
    }
}
```

3. **Circuit Breaker Integration**
```java
public class ResilientService {
    private final CircuitBreaker circuitBreaker;
    private final ServiceClient client;
    
    public ResilientService(ServiceClient client) {
        this.client = client;
        this.circuitBreaker = new CircuitBreaker(5, 30000);
    }
    
    public <T> T execute(Supplier<T> operation) throws ServiceException {
        if (!circuitBreaker.allowRequest()) {
            throw new ServiceException("Service unavailable");
        }
        
        try {
            T result = operation.get();
            circuitBreaker.recordSuccess();
            return result;
        } catch (Exception e) {
            circuitBreaker.recordFailure();
            throw new ServiceException("Operation failed", e);
        }
    }
}
```

## 16. Common Mistakes

### Mistake 1: Not Handling Partial Failures

```java
// Bad - what if email fails?
public void processOrder(Order order) {
    saveOrder(order);
    sendEmail(order); // If this fails, order is saved but email not sent
}

// Good - handle partial failures
public void processOrder(Order order) {
    try {
        saveOrder(order);
    } catch (Exception e) {
        throw new OrderException("Failed to save order", e);
    }
    
    try {
        sendEmail(order);
    } catch (Exception e) {
        // Log but don't fail order
        logger.warning("Email failed for order: " + order.getId());
    }
}
```

### Mistake 2: Swallowing Exceptions in Production

```java
// Bad - silent failure in production
try {
    criticalOperation();
} catch (Exception e) {
    // Logged nowhere, no alert
}

// Good - proper handling
try {
    criticalOperation();
} catch (Exception e) {
    logger.log(Level.SEVERE, "Critical operation failed", e);
    alertingService.alert("Critical failure", e);
    throw new ServiceException("Critical failure", e);
}
```

### Mistake 3: Not Cleaning Up Resources

```java
// Bad - resource leak
Connection conn = dataSource.getConnection();
try {
    // Use connection
} catch (Exception e) {
    // Handle
}
// Connection never closed!

// Good - always cleanup
try (Connection conn = dataSource.getConnection()) {
    // Use connection
} catch (Exception e) {
    // Handle
}
```

## 17. Pitfalls

### Pitfall 1: Exception in Exception Handler

```java
// Bad - handler throws exception
@ExceptionHandler(Exception.class)
public ResponseEntity<?> handle(Exception e) {
    String message = formatMessage(e); // This might throw!
    return ResponseEntity.status(500).body(message);
}

// Good - handle handler exceptions
@ExceptionHandler(Exception.class)
public ResponseEntity<?> handle(Exception e) {
    try {
        String message = formatMessage(e);
        return ResponseEntity.status(500).body(message);
    } catch (Exception handlerException) {
        logger.error("Handler failed", handlerException);
        return ResponseEntity.status(500).body("Internal error");
    }
}
```

### Pitfall 2: Exception in Finally Block

```java
// Bad - finally exception loses original
try {
    throw new RuntimeException("Original");
} finally {
    throw new RuntimeException("Finally"); // Original lost!
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

### Pitfall 3: Not Preserving Context

```java
// Bad - loses context
try {
    remoteService.call();
} catch (Exception e) {
    throw new RuntimeException("Failed"); // No context
}

// Good - preserves context
try {
    remoteService.call();
} catch (Exception e) {
    throw new RuntimeException("Failed to call remote service for user: " + userId, e);
}
```

## 18. Debugging Tips

### Production Debugging

1. **Correlation IDs**
```java
public class CorrelationIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        String correlationId = request.getHeader("X-Correlation-ID");
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }
        
        MDC.put("correlationId", correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId");
        }
    }
}
```

2. **Structured Logging**
```java
logger.error(Map.of(
    "service", "user-service",
    "operation", "getUser",
    "userId", userId,
    "error", e.getMessage(),
    "stackTrace", getStackTrace(e)
).toString());
```

3. **Exception Aggregation**
```java
public class ExceptionAggregator {
    private final Map<String, AtomicInteger> exceptionCounts = new ConcurrentHashMap<>();
    
    public void record(Exception e) {
        String key = e.getClass().getSimpleName();
        exceptionCounts.computeIfAbsent(key, k -> new AtomicInteger(0))
            .incrementAndGet();
    }
    
    public Map<String, Integer> getReport() {
        Map<String, Integer> report = new HashMap<>();
        exceptionCounts.forEach((k, v) -> report.put(k, v.get()));
        return report;
    }
}
```

## 19. Comparison Table

### Exception Handling Patterns

| Pattern | Purpose | Use Case | Complexity |
|---------|---------|----------|------------|
| Retry | Handle transient failures | Network calls | Low |
| Circuit Breaker | Prevent cascading failures | Remote services | Medium |
| Fallback | Provide alternatives | Critical operations | Medium |
| Bulkhead | Isolate failures | Multi-service | High |

### Failure Types

| Type | Characteristics | Strategy | Example |
|------|-----------------|----------|---------|
| Transient | Temporary, self-resolving | Retry | Network timeout |
| Permanent | Won't resolve | Handle immediately | Invalid input |
| Partial | Some operations fail | Transaction | Batch processing |
| Cascading | One causes others | Circuit breaker | Service dependency |

## 20. Decision Tree

### Exception Handling Decision

```
What type of failure?
├── Transient
│   ├── Is it safe to retry?
│   │   ├── Yes → Implement retry with backoff
│   │   └── No → Fail immediately
│   └── Is it idempotent?
│       ├── Yes → Retry
│       └── No → Manual intervention
├── Permanent
│   ├── Is it recoverable?
│   │   ├── Yes → Handle and continue
│   │   └── No → Fail fast
│   └── Is it user error?
│       ├── Yes → Return user-friendly message
│       └── No → Log and alert
└── Partial
    ├── Is it a transaction?
    │   ├── Yes → Rollback
    │   └── No → Handle individually
    └── Is compensation possible?
        ├── Yes → Implement saga pattern
        └── No → Manual intervention
```

## 21. Interview Questions

### Q1: How do you handle distributed system failures?

**Answer:**
1. Implement circuit breakers
2. Use retry with exponential backoff
3. Add correlation IDs for tracing
4. Implement fallback mechanisms
5. Monitor and alert on failures

### Q2: What is the circuit breaker pattern?

**Answer:**
A pattern that prevents cascading failures by stopping calls to a failing service. It has three states: CLOSED (normal), OPEN (failing), HALF_OPEN (testing recovery).

### Q3: How do you handle partial failures?

**Answer:**
Use transactions for atomic operations, implement compensation logic (saga pattern), and handle each failure independently when possible.

### Q4: How do you monitor exceptions in production?

**Answer:**
Use structured logging, correlation IDs, exception aggregation, metrics collection, and alerting on critical failures.

### Q5: What are best practices for retry logic?

**Answer:**
1. Use exponential backoff
2. Add jitter to prevent thundering herd
3. Limit max retries
4. Only retry on transient failures
5. Ensure idempotency

## 22. Exercises

### Exercise 1: Transaction Manager

Create a transaction manager that:
- Supports nested transactions
- Implements rollback on failure
- Handles partial failures
- Logs transaction events

### Exercise 2: Circuit Breaker

Implement a circuit breaker that:
- Tracks failure counts
- Transitions between states
- Provides metrics
- Integrates with retry logic

### Exercise 3: Distributed Exception Handler

Build a distributed exception handler that:
- Handles cross-service failures
- Implements correlation IDs
- Provides fallback mechanisms
- Aggregates exception metrics

## 23. Assignments

### Assignment 1: Microservice Error Handling

Create a microservice with:
- Global exception handler
- Circuit breaker integration
- Retry mechanisms
- Fallback responses

### Assignment 2: Batch Processing System

Build a batch processor that:
- Handles partial failures
- Implements transaction management
- Provides detailed error reporting
- Supports recovery from failures

### Assignment 3: API Gateway Exception Handler

Create an API gateway that:
- Handles all exception types
- Provides consistent error responses
- Implements rate limiting
- Logs and monitors errors

## 24. Mini Project

### Resilient Application Framework

Create a framework that:
1. Provides circuit breaker implementation
2. Implements retry mechanisms
3. Handles distributed transactions
4. Monitors and reports exceptions
5. Supports fallback mechanisms

## 25. Summary

### Key Takeaways

- Handle exceptions at appropriate layers
- Implement retry for transient failures
- Use circuit breakers to prevent cascading failures
- Ensure proper resource cleanup
- Log exceptions with context
- Monitor and alert on production issues
- Implement fallback mechanisms
- Test exception paths thoroughly

### Production Checklist

- [ ] Global exception handler
- [ ] Circuit breaker implementation
- [ ] Retry mechanisms
- [ ] Fallback responses
- [ ] Structured logging
- [ ] Correlation IDs
- [ ] Metrics collection
- [ ] Alerting setup
- [ ] Resource cleanup
- [ ] Transaction management

## 26. References

### Official Documentation
- [Java SE Exception Handling](https://docs.oracle.com/en/java/javase/21/essential/exceptions/)
- [Spring Exception Handling](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-exceptionhandlers)

### Books
- "Release It!" by Michael Nygard
- "Designing Data-Intensive Applications" by Martin Kleppmann
- "Java Concurrency in Practice" by Brian Goetz

### Online Resources
- [Baeldung - Spring Exception Handling](https://www.baeldung.com/exception-handling-in-spring)
- [Baeldung - Circuit Breaker](https://www.baeldung.com/circuit-breaker-pattern-java)
- [Netflix Hystrix](https://github.com/Netflix/Hystrix)

## 27. Next Steps

Now that you understand real-world exception handling, proceed to:
- **09-mini-project**: Build a comprehensive exception handling project
