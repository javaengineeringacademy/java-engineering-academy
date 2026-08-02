package academy.javaengineering.exceptionhandling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Real-World Exception Handling Examples
 * 
 * Demonstrates enterprise-level exception handling patterns.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class RealWorldExamples {

    private static final Logger logger = Logger.getLogger(RealWorldExamples.class.getName());

    /**
     * Demonstrates retry mechanism.
     */
    public static void retryMechanism() {
        System.out.println("=== Retry Mechanism ===");
        
        RetryPolicy policy = new RetryPolicy.Builder()
            .maxRetries(3)
            .baseDelayMs(100)
            .retryOn(RuntimeException.class)
            .build();
        
        RetryMechanism retry = new RetryMechanism(policy);
        
        try {
            String result = retry.execute("fetchData", () -> {
                // Simulate operation that might fail
                if (Math.random() > 0.7) {
                    throw new RuntimeException("Transient failure");
                }
                return "Success";
            });
            System.out.println("Result: " + result);
        } catch (RetryExhaustedException e) {
            System.out.println("All retries exhausted: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates circuit breaker pattern.
     */
    public static void circuitBreaker() {
        System.out.println("=== Circuit Breaker ===");
        
        CircuitBreaker breaker = new CircuitBreaker("api-service", 3, 5000);
        
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
            } catch (CircuitBreakerOpenException e) {
                System.out.println("Attempt " + i + ": " + e.getMessage());
            }
            
            System.out.println("State: " + breaker.getState());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception metrics.
     */
    public static void exceptionMetrics() {
        System.out.println("=== Exception Metrics ===");
        
        ExceptionMetrics metrics = new ExceptionMetrics();
        
        // Simulate exceptions
        metrics.recordException(new RuntimeException("Error 1"), "service-a");
        metrics.recordException(new IllegalArgumentException("Error 2"), "service-b");
        metrics.recordException(new RuntimeException("Error 3"), "service-a");
        
        Map<String, Object> report = metrics.getReport();
        System.out.println("Total exceptions: " + report.get("total"));
        System.out.println("By type: " + report.get("byType"));
        System.out.println("By service: " + report.get("byService"));
        
        System.out.println();
    }

    /**
     * Demonstrates transaction management.
     */
    public static void transactionManagement() {
        System.out.println("=== Transaction Management ===");
        
        TransactionManager tm = new TransactionManager();
        
        try {
            tm.executeInTransaction(() -> {
                System.out.println("Executing transaction");
                // Simulate transaction
                return "Transaction completed";
            });
        } catch (TransactionException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        retryMechanism();
        circuitBreaker();
        exceptionMetrics();
        transactionManagement();
    }

    // Supporting classes

    static class RetryPolicy {
        private final int maxRetries;
        private final long baseDelayMs;
        private final Class<? extends Exception>[] retryableExceptions;
        
        private RetryPolicy(Builder builder) {
            this.maxRetries = builder.maxRetries;
            this.baseDelayMs = builder.baseDelayMs;
            this.retryableExceptions = builder.retryableExceptions;
        }
        
        boolean shouldRetry(int attempt, Exception e) {
            if (attempt > maxRetries) return false;
            for (Class<? extends Exception> clazz : retryableExceptions) {
                if (clazz.isInstance(e)) return true;
            }
            return false;
        }
        
        long getDelay(int attempt) {
            return baseDelayMs * (long) Math.pow(2, attempt - 1);
        }
        
        int getMaxRetries() { return maxRetries; }
        
        static class Builder {
            int maxRetries = 3;
            long baseDelayMs = 1000;
            @SuppressWarnings("unchecked")
            Class<? extends Exception>[] retryableExceptions = new Class[0];
            
            Builder maxRetries(int maxRetries) {
                this.maxRetries = maxRetries;
                return this;
            }
            
            Builder baseDelayMs(long baseDelayMs) {
                this.baseDelayMs = baseDelayMs;
                return this;
            }
            
            @SafeVarargs
            final Builder retryOn(Class<? extends Exception>... exceptions) {
                this.retryableExceptions = exceptions;
                return this;
            }
            
            RetryPolicy build() {
                return new RetryPolicy(this);
            }
        }
    }

    static class RetryMechanism {
        private final RetryPolicy policy;
        
        RetryMechanism(RetryPolicy policy) {
            this.policy = policy;
        }
        
        <T> T execute(String operationName, Supplier<T> operation) 
                throws RetryExhaustedException {
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
                        try {
                            Thread.sleep(policy.getDelay(attempts));
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new RetryExhaustedException(attempts, ie);
                        }
                    }
                }
            }
            
            throw new RetryExhaustedException(attempts, lastException);
        }
    }

    static class RetryExhaustedException extends Exception {
        RetryExhaustedException(int attempts, Throwable cause) {
            super("All " + attempts + " retry attempts exhausted", cause);
        }
    }

    static class CircuitBreaker {
        private final String name;
        private final int failureThreshold;
        private final long resetTimeoutMs;
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private volatile State state = State.CLOSED;
        private long lastFailureTime = 0;
        
        CircuitBreaker(String name, int failureThreshold, long resetTimeoutMs) {
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
        
        State getState() { return state; }
        
        enum State { CLOSED, OPEN, HALF_OPEN }
    }

    static class CircuitBreakerOpenException extends Exception {
        CircuitBreakerOpenException(String message) {
            super(message);
        }
        
        CircuitBreakerOpenException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class ExceptionMetrics {
        private final AtomicInteger totalExceptions = new AtomicInteger(0);
        private final ConcurrentHashMap<String, AtomicInteger> byType = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, AtomicInteger> byService = new ConcurrentHashMap<>();
        
        void recordException(Exception e, String service) {
            totalExceptions.incrementAndGet();
            
            String type = e.getClass().getSimpleName();
            byType.computeIfAbsent(type, k -> new AtomicInteger(0)).incrementAndGet();
            byService.computeIfAbsent(service, k -> new AtomicInteger(0)).incrementAndGet();
        }
        
        Map<String, Object> getReport() {
            Map<String, Object> report = new ConcurrentHashMap<>();
            report.put("total", totalExceptions.get());
            report.put("byType", Map.copyOf(byType));
            report.put("byService", Map.copyOf(byService));
            return report;
        }
    }

    static class TransactionManager {
        <T> T executeInTransaction(TransactionOperation<T> operation) 
                throws TransactionException {
            try {
                System.out.println("Beginning transaction");
                T result = operation.execute();
                System.out.println("Committing transaction");
                return result;
            } catch (Exception e) {
                System.out.println("Rolling back transaction");
                throw new TransactionException("Transaction failed", e);
            }
        }
        
        interface TransactionOperation<T> {
            T execute() throws Exception;
        }
    }

    static class TransactionException extends Exception {
        TransactionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
