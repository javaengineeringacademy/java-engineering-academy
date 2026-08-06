# Mini Project: Exception Handling Framework

## 1. Introduction

This mini project consolidates all exception handling concepts into a comprehensive, production-ready framework. You'll build a complete exception handling system that includes custom exceptions, retry mechanisms, circuit breakers, logging, and monitoring.

## 2. Learning Objectives

By the end of this project, you will be able to:

- Design and implement a complete exception handling framework
- Apply all exception handling concepts in a real-world scenario
- Build reusable exception handling components
- Implement production-ready error handling patterns
- Test exception handling thoroughly
- Document exception handling APIs

## 3. Prerequisites

- All previous exception handling topics
- Understanding of design patterns
- Knowledge of testing frameworks
- Basic logging concepts

## 4. Project Overview

### Goal

Create a comprehensive exception handling framework that can be used in any Java application.

### Components

1. **Exception Hierarchy**: Custom exceptions for different scenarios
2. **Exception Builder**: Fluent API for creating exceptions
3. **Retry Mechanism**: Configurable retry policies
4. **Circuit Breaker**: Prevent cascading failures
5. **Error Reporter**: Logging and monitoring
6. **Recovery Strategies**: Fallback mechanisms

## 5. Problem Statement

### Challenge

Build a framework that:
- Provides consistent exception handling
- Supports different failure scenarios
- Is easy to configure and use
- Includes monitoring and reporting
- Is well-tested and documented

## 6. Theory

### Framework Design Principles

1. **Separation of Concerns**: Each component has a specific responsibility
2. **Configurability**: Components can be customized
3. **Composability**: Components work together seamlessly
4. **Testability**: Easy to test in isolation
5. **Performance**: Minimal overhead

### Architecture

```
ExceptionHandlingFramework
├. ExceptionBuilder
├. RetryMechanism
├. CircuitBreaker
├. ErrorReporter
├. RecoveryStrategy
└. ExceptionRegistry
```

## 7. Implementation

### 7.1 Exception Hierarchy

```java
package academy.javaengineering.exceptionhandling.framework;

import java.time.Instant;
import java.util.Map;

/**
 * Base exception for all framework exceptions.
 */
public abstract class FrameworkException extends Exception {
    private final String errorCode;
    private final Instant timestamp;
    private final Map<String, Object> context;
    
    protected FrameworkException(String message, String errorCode, 
                               Throwable cause, Map<String, Object> context) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.context = context != null ? Map.copyOf(context) : Map.of();
    }
    
    public String getErrorCode() { return errorCode; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, Object> getContext() { return context; }
    
    public abstract String getRecoverySuggestion();
    
    @Override
    public String toString() {
        return String.format("%s{errorCode='%s', message='%s', timestamp=%s, context=%s}",
            getClass().getSimpleName(), errorCode, getMessage(), timestamp, context);
    }
}

/**
 * Validation exception.
 */
public class ValidationException extends FrameworkException {
    private final String fieldName;
    
    public ValidationException(String fieldName, String message, 
                             Throwable cause, Map<String, Object> context) {
        super(message, "VALIDATION_ERROR", cause, 
            context != null ? addFieldToContext(context, fieldName) : 
            Map.of("fieldName", fieldName));
        this.fieldName = fieldName;
    }
    
    public String getFieldName() { return fieldName; }
    
    @Override
    public String getRecoverySuggestion() {
        return String.format("Please check the value for field '%s'", fieldName);
    }
    
    private static Map<String, Object> addFieldToContext(Map<String, Object> context, 
                                                        String fieldName) {
        var newContext = new java.util.HashMap<>(context);
        newContext.put("fieldName", fieldName);
        return Map.copyOf(newContext);
    }
}

/**
 * Resource not found exception.
 */
public class ResourceNotFoundException extends FrameworkException {
    private final String resourceType;
    private final Object resourceId;
    
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s not found with id: %s", resourceType, resourceId),
            "RESOURCE_NOT_FOUND", null, 
            Map.of("resourceType", resourceType, "resourceId", resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public String getResourceType() { return resourceType; }
    public Object getResourceId() { return resourceId; }
    
    @Override
    public String getRecoverySuggestion() {
        return String.format("Verify the %s with id '%s' exists", resourceType, resourceId);
    }
}

/**
 * Operation failed exception.
 */
public class OperationFailedException extends FrameworkException {
    private final String operationName;
    
    public OperationFailedException(String operationName, String message, 
                                  Throwable cause, Map<String, Object> context) {
        super(message, "OPERATION_FAILED", cause, 
            context != null ? addOperationToContext(context, operationName) : 
            Map.of("operation", operationName));
        this.operationName = operationName;
    }
    
    public String getOperationName() { return operationName; }
    
    @Override
    public String getRecoverySuggestion() {
        return "Retry the operation or contact support";
    }
    
    private static Map<String, Object> addOperationToContext(Map<String, Object> context, 
                                                            String operationName) {
        var newContext = new java.util.HashMap<>(context);
        newContext.put("operation", operationName);
        return Map.copyOf(newContext);
    }
}

/**
 * Circuit breaker open exception.
 */
public class CircuitBreakerOpenException extends FrameworkException {
    private final String serviceName;
    
    public CircuitBreakerOpenException(String serviceName) {
        super(String.format("Circuit breaker is open for service: %s", serviceName),
            "CIRCUIT_BREAKER_OPEN", null, Map.of("service", serviceName));
        this.serviceName = serviceName;
    }
    
    public String getServiceName() { return serviceName; }
    
    @Override
    public String getRecoverySuggestion() {
        return "Wait for the circuit breaker to reset or use fallback";
    }
}

/**
 * Retry exhausted exception.
 */
public class RetryExhaustedException extends FrameworkException {
    private final int attempts;
    
    public RetryExhaustedException(int attempts, Throwable cause) {
        super(String.format("All %d retry attempts exhausted", attempts),
            "RETRY_EXHAUSTED", cause, Map.of("attempts", attempts));
        this.attempts = attempts;
    }
    
    public int getAttempts() { return attempts; }
    
    @Override
    public String getRecoverySuggestion() {
        return "Check the underlying cause and try again later";
    }
}
```

### 7.2 Exception Builder

```java
package academy.javaengineering.exceptionhandling.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * Fluent builder for creating exceptions.
 */
public class ExceptionBuilder {
    private String message;
    private Throwable cause;
    private String errorCode;
    private final Map<String, Object> context;
    private String recoverySuggestion;
    
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
    
    public ExceptionBuilder code(String code) {
        this.errorCode = code;
        return this;
    }
    
    public ExceptionBuilder context(String key, Object value) {
        this.context.put(key, value);
        return this;
    }
    
    public ExceptionBuilder context(Map<String, Object> context) {
        this.context.putAll(context);
        return this;
    }
    
    public ExceptionBuilder recovery(String suggestion) {
        this.recoverySuggestion = suggestion;
        return this;
    }
    
    public ValidationException buildValidationException(String fieldName) {
        return new ValidationException(fieldName, message, cause, context);
    }
    
    public ResourceNotFoundException buildResourceNotFoundException(
            String resourceType, Object resourceId) {
        return new ResourceNotFoundException(resourceType, resourceId);
    }
    
    public OperationFailedException buildOperationFailedException(String operationName) {
        return new OperationFailedException(operationName, message, cause, context);
    }
    
    public FrameworkException buildGenericException() {
        return new FrameworkException(message, errorCode, cause, context) {
            @Override
            public String getRecoverySuggestion() {
                return recoverySuggestion;
            }
        };
    }
}
```

### 7.3 Retry Mechanism

```java
package academy.javaengineering.exceptionhandling.framework;

import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Configurable retry mechanism.
 */
public class RetryMechanism {
    private static final Logger logger = Logger.getLogger(RetryMechanism.class.getName());
    private final RetryPolicy policy;
    
    public RetryMechanism(RetryPolicy policy) {
        this.policy = policy;
    }
    
    public <T> T execute(String operationName, Supplier<T> operation) 
            throws RetryExhaustedException {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts <= policy.getMaxRetries()) {
            try {
                logger.fine(String.format("Executing %s (attempt %d)", 
                    operationName, attempts + 1));
                
                T result = operation.get();
                
                logger.fine(String.format("Operation %s succeeded", operationName));
                return result;
                
            } catch (Exception e) {
                attempts++;
                lastException = e;
                
                logger.warning(String.format("Attempt %d failed for %s: %s", 
                    attempts, operationName, e.getMessage()));
                
                if (!policy.shouldRetry(attempts, e)) {
                    logger.info(String.format("Not retrying %s: max retries or non-retryable exception", 
                        operationName));
                    break;
                }
                
                if (attempts <= policy.getMaxRetries()) {
                    try {
                        long delay = policy.getDelay(attempts);
                        logger.fine(String.format("Waiting %d ms before retry", delay));
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RetryExhaustedException(attempts, ie);
                    }
                }
            }
        }
        
        throw new RetryExhaustedException(attempts, lastException);
    }
    
    /**
     * Retry policy configuration.
     */
    public static class RetryPolicy {
        private final int maxRetries;
        private final long baseDelayMs;
        private final double multiplier;
        private final Set<Class<? extends Exception>> retryableExceptions;
        
        private RetryPolicy(Builder builder) {
            this.maxRetries = builder.maxRetries;
            this.baseDelayMs = builder.baseDelayMs;
            this.multiplier = builder.multiplier;
            this.retryableExceptions = builder.retryableExceptions;
        }
        
        public boolean shouldRetry(int attempt, Exception e) {
            if (attempt > maxRetries) return false;
            return retryableExceptions.stream()
                .anyMatch(clazz -> clazz.isInstance(e));
        }
        

## 📑 Continue Reading

**Part 1** of 4 | [Part 2](README-part2.md) | [Part 3](README-part3.md) | [Part 4](README-part4.md)

