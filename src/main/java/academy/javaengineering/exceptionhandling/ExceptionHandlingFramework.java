package academy.javaengineering.exceptionhandling;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Mini Project: Exception Handling Framework
 * 
 * A comprehensive exception handling framework demonstrating all concepts.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ExceptionHandlingFramework {

    private static final Logger logger = Logger.getLogger(ExceptionHandlingFramework.class.getName());
    
    private final RetryMechanism retryMechanism;
    private final CircuitBreaker circuitBreaker;
    private final ErrorReporter errorReporter;
    private final RecoveryStrategy recoveryStrategy;
    
    private ExceptionHandlingFramework(Builder builder) {
        this.retryMechanism = new RetryMechanism(builder.retryPolicy);
        this.circuitBreaker = builder.circuitBreaker;
        this.errorReporter = builder.errorReporter;
        this.recoveryStrategy = builder.recoveryStrategy;
    }
    
    /**
     * Execute operation with exception handling.
     */
    public <T> T execute(String operationName, Supplier<T> operation) {
        return execute(operationName, operation, false, false);
    }
    
    /**
     * Execute operation with retry.
     */
    public <T> T executeWithRetry(String operationName, Supplier<T> operation) {
        return execute(operationName, operation, true, false);
    }
    
    /**
     * Execute operation with circuit breaker.
     */
    public <T> T executeWithCircuitBreaker(String operationName, Supplier<T> operation) {
        return execute(operationName, operation, false, true);
    }
    
    /**
     * Execute operation with retry and circuit breaker.
     */
    public <T> T executeWithResilience(String operationName, Supplier<T> operation) {
        return execute(operationName, operation, true, true);
    }
    
    private <T> T execute(String operationName, Supplier<T> operation, 
                         boolean useRetry, boolean useCircuitBreaker) {
        Supplier<T> wrappedOperation = operation;
        
        if (useCircuitBreaker && circuitBreaker != null) {
            wrappedOperation = () -> circuitBreaker.execute(wrappedOperation);
        }
        
        if (useRetry) {
            wrappedOperation = () -> retryMechanism.execute(operationName, wrappedOperation);
        }
        
        try {
            T result = wrappedOperation.get();
            errorReporter.recordSuccess(operationName);
            return result;
        } catch (Exception e) {
            errorReporter.recordException(e, operationName);
            
            if (recoveryStrategy != null) {
                return recoveryStrategy.recover(e);
            }
            
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Get framework metrics.
     */
    public Map<String, Object> getMetrics() {
        return errorReporter.getReport();
    }
    
    /**
     * Framework builder.
     */
    public static class Builder {
        private RetryPolicy retryPolicy = new RetryPolicy.Builder().build();
        private CircuitBreaker circuitBreaker;
        private ErrorReporter errorReporter = new ErrorReporter();
        private RecoveryStrategy recoveryStrategy;
        
        public Builder withRetryPolicy(RetryPolicy policy) {
            this.retryPolicy = policy;
            return this;
        }
        
        public Builder withCircuitBreaker(CircuitBreaker circuitBreaker) {
            this.circuitBreaker = circuitBreaker;
            return this;
        }
        
        public Builder withErrorReporter(ErrorReporter errorReporter) {
            this.errorReporter = errorReporter;
            return this;
        }
        
        public Builder withRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
            this.recoveryStrategy = recoveryStrategy;
            return this;
        }
        
        public ExceptionHandlingFramework build() {
            return new ExceptionHandlingFramework(this);
        }
    }
    
    // Supporting classes
    
    public static class RetryPolicy {
        private final int maxRetries;
        private final long baseDelayMs;
        private final double multiplier;
        
        private RetryPolicy(Builder builder) {
            this.maxRetries = builder.maxRetries;
            this.baseDelayMs = builder.baseDelayMs;
            this.multiplier = builder.multiplier;
        }
        
        boolean shouldRetry(int attempt, Exception e) {
            return attempt <= maxRetries;
        }
        
        long getDelay(int attempt) {
            return (long) (baseDelayMs * Math.pow(multiplier, attempt - 1));
        }
        
        int getMaxRetries() { return maxRetries; }
        
        public static class Builder {
            private int maxRetries = 3;
            private long baseDelayMs = 1000;
            private double multiplier = 2.0;
            
            public Builder maxRetries(int maxRetries) {
                this.maxRetries = maxRetries;
                return this;
            }
            
            public Builder baseDelayMs(long baseDelayMs) {
                this.baseDelayMs = baseDelayMs;
                return this;
            }
            
            public Builder multiplier(double multiplier) {
                this.multiplier = multiplier;
                return this;
            }
            
            public RetryPolicy build() {
                return new RetryPolicy(this);
            }
        }
    }
    
    public static class RetryMechanism {
        private final RetryPolicy policy;
        
        RetryMechanism(RetryPolicy policy) {
            this.policy = policy;
        }
        
        <T> T execute(String operationName, Supplier<T> operation) throws Exception {
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
            
            throw new RetryExhaustedException(attempts, lastException);
        }
    }
    
    public static class RetryExhaustedException extends Exception {
        RetryExhaustedException(int attempts, Throwable cause) {
            super("All " + attempts + " retry attempts exhausted", cause);
        }
    }
    
    public static class CircuitBreaker {
        private final String name;
        private final int failureThreshold;
        private final long resetTimeoutMs;
        private final java.util.concurrent.atomic.AtomicInteger failureCount = 
            new java.util.concurrent.atomic.AtomicInteger(0);
        private volatile State state = State.CLOSED;
        private long lastFailureTime = 0;
        
        public CircuitBreaker(String name, int failureThreshold, long resetTimeoutMs) {
            this.name = name;
            this.failureThreshold = failureThreshold;
            this.resetTimeoutMs = resetTimeoutMs;
        }
        
        <T> T execute(Supplier<T> operation) throws CircuitBreakerOpenException {
            if (!allowRequest()) {
                throw new CircuitBreakerOpenException("Circuit breaker is open for: " + name);
            }
            
            try {
                T result = operation.get();
                recordSuccess();
                return result;
            } catch (Exception e) {
                recordFailure();
                throw new CircuitBreakerOpenException("Operation failed", e);
            }
        }
        
        private boolean allowRequest() {
            if (state == State.CLOSED) return true;
            
            if (state == State.OPEN) {
                if (System.currentTimeMillis() - lastFailureTime > resetTimeoutMs) {
                    state = State.HALF_OPEN;
                    return true;
                }
                return false;
            }
            
            return true;
        }
        
        private void recordSuccess() {
            failureCount.set(0);
            state = State.CLOSED;
        }
        
        private void recordFailure() {
            failureCount.incrementAndGet();
            lastFailureTime = System.currentTimeMillis();
            
            if (failureCount.get() >= failureThreshold) {
                state = State.OPEN;
            }
        }
        
        public State getState() { return state; }
        
        public enum State { CLOSED, OPEN, HALF_OPEN }
    }
    
    public static class CircuitBreakerOpenException extends Exception {
        CircuitBreakerOpenException(String message) {
            super(message);
        }
        
        CircuitBreakerOpenException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class ErrorReporter {
        private final java.util.concurrent.atomic.AtomicInteger totalExceptions = 
            new java.util.concurrent.atomic.AtomicInteger(0);
        private final ConcurrentHashMap<String, java.util.concurrent.atomic.AtomicInteger> byType = 
            new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, java.util.concurrent.atomic.AtomicInteger> byOperation = 
            new ConcurrentHashMap<>();
        
        public void recordException(Exception e, String operation) {
            totalExceptions.incrementAndGet();
            
            String type = e.getClass().getSimpleName();
            byType.computeIfAbsent(type, k -> new java.util.concurrent.atomic.AtomicInteger(0))
                .incrementAndGet();
            byOperation.computeIfAbsent(operation, k -> new java.util.concurrent.atomic.AtomicInteger(0))
                .incrementAndGet();
        }
        
        public void recordSuccess(String operation) {
            // Record success for metrics
        }
        
        public Map<String, Object> getReport() {
            Map<String, Object> report = new ConcurrentHashMap<>();
            report.put("total", totalExceptions.get());
            report.put("byType", Map.copyOf(byType));
            report.put("byOperation", Map.copyOf(byOperation));
            return report;
        }
    }
    
    public static class RecoveryStrategy {
        private final java.util.Map<Class<?>, Supplier<?>> strategies;
        private final Supplier<?> defaultStrategy;
        
        private RecoveryStrategy(Builder builder) {
            this.strategies = builder.strategies;
            this.defaultStrategy = builder.defaultStrategy;
        }
        
        @SuppressWarnings("unchecked")
        public <T> T recover(Exception e) {
            Supplier<?> strategy = strategies.get(e.getClass());
            
            if (strategy == null) {
                strategy = defaultStrategy;
            }
            
            if (strategy == null) {
                throw new RuntimeException("No recovery strategy for: " + e.getClass(), e);
            }
            
            return (T) strategy.get();
        }
        
        public static class Builder {
            private final java.util.Map<Class<?>, Supplier<?>> strategies = new ConcurrentHashMap<>();
            private Supplier<?> defaultStrategy;
            
            public <E extends Exception> Builder register(Class<E> type, Supplier<?> strategy) {
                strategies.put(type, strategy);
                return this;
            }
            
            public Builder defaultStrategy(Supplier<?> strategy) {
                this.defaultStrategy = strategy;
                return this;
            }
            
            public RecoveryStrategy build() {
                return new RecoveryStrategy(this);
            }
        }
    }
    
    /**
     * Main method to demonstrate the framework.
     */
    public static void main(String[] args) {
        System.out.println("=== Exception Handling Framework Demo ===\n");
        
        // Create framework
        ExceptionHandlingFramework framework = new ExceptionHandlingFramework.Builder()
            .withRetryPolicy(new RetryPolicy.Builder()
                .maxRetries(3)
                .baseDelayMs(100)
                .build())
            .withCircuitBreaker(new CircuitBreaker("demo-service", 3, 5000))
            .withRecoveryStrategy(new RecoveryStrategy.Builder()
                .register(RuntimeException.class, () -> "Fallback value")
                .defaultStrategy(() -> "Default value")
                .build())
            .build();
        
        // Test basic execution
        System.out.println("1. Basic Execution:");
        String result = framework.execute("basicOp", () -> "Success");
        System.out.println("Result: " + result);
        
        // Test with retry
        System.out.println("\n2. With Retry:");
        try {
            String retryResult = framework.executeWithRetry("retryOp", () -> {
                if (Math.random() > 0.7) {
                    throw new RuntimeException("Transient error");
                }
                return "Retry Success";
            });
            System.out.println("Result: " + retryResult);
        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }
        
        // Test with circuit breaker
        System.out.println("\n3. With Circuit Breaker:");
        for (int i = 0; i < 5; i++) {
            try {
                String cbResult = framework.executeWithCircuitBreaker("cbOp", () -> {
                    if (Math.random() > 0.5) {
                        throw new RuntimeException("Service error");
                    }
                    return "CB Success";
                });
                System.out.println("Attempt " + i + ": " + cbResult);
            } catch (Exception e) {
                System.out.println("Attempt " + i + ": " + e.getMessage());
            }
        }
        
        // Get metrics
        System.out.println("\n4. Metrics:");
        Map<String, Object> metrics = framework.getMetrics();
        System.out.println("Total exceptions: " + metrics.get("total"));
        System.out.println("By type: " + metrics.get("byType"));
    }
}
