package academy.javaengineering.exceptions.examples;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Production retry pattern example with exponential backoff.
 * Demonstrates robust retry mechanisms for transient failures in production systems.
 */
public class ProductionRetryExample {

    // Custom exception for service failures
    static class ServiceUnavailableException extends Exception {
        public ServiceUnavailableException(String message) {
            super(message);
        }
        
        public ServiceUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    // Configuration for retry behavior
    static class RetryConfig {
        final int maxRetries;
        final long initialDelayMs;
        final double backoffMultiplier;
        final long maxDelayMs;
        
        RetryConfig(int maxRetries, long initialDelayMs, 
                   double backoffMultiplier, long maxDelayMs) {
            this.maxRetries = maxRetries;
            this.initialDelayMs = initialDelayMs;
            this.backoffMultiplier = backoffMultiplier;
            this.maxDelayMs = maxDelayMs;
        }
        
        // Default production config
        static RetryConfig defaultConfig() {
            return new RetryConfig(3, 1000, 2.0, 30000);
        }
        
        // Aggressive config for critical services
        static RetryConfig aggressiveConfig() {
            return new RetryConfig(5, 500, 1.5, 60000);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Production Retry Example ===");
        
        // Example 1: Simple retry with fixed delay
        testSimpleRetry();
        
        // Example 2: Exponential backoff retry
        testExponentialBackoff();
        
        // Example 3: Production-grade retry with config
        testProductionRetry();
        
        // Example 4: Retry with different exception types
        testRetryWithDifferentExceptions();
        
        System.out.println("=== End of Production Retry Example ===");
    }
    
    private static void testSimpleRetry() {
        System.out.println("\n--- Simple Retry ---");
        
        int maxRetries = 3;
        int attempt = 1;
        
        while (attempt <= maxRetries) {
            try {
                System.out.println("Attempt " + attempt + "...");
                callExternalService(); // Might fail
                System.out.println("Success on attempt " + attempt);
                break; // Success, exit retry loop
                
            } catch (ServiceUnavailableException e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
                attempt++;
                
                if (attempt > maxRetries) {
                    System.out.println("All attempts failed");
                    break;
                }
                
                // Simple fixed delay
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    
    private static void testExponentialBackoff() {
        System.out.println("\n--- Exponential Backoff ---");
        
        int maxRetries = 4;
        long delayMs = 1000;
        double multiplier = 2.0;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("Attempt " + attempt + " with delay " + delayMs + "ms");
                callExternalService();
                System.out.println("Success on attempt " + attempt);
                break;
                
            } catch (ServiceUnavailableException e) {
                System.out.println("Failed: " + e.getMessage());
                
                if (attempt < maxRetries) {
                    System.out.println("Waiting " + delayMs + "ms before retry...");
                    
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    
                    // Exponential backoff
                    delayMs = (long) (delayMs * multiplier);
                }
            }
        }
    }
    
    private static void testProductionRetry() {
        System.out.println("\n--- Production Retry ---");
        
        RetryConfig config = RetryConfig.defaultConfig();
        
        try {
            String result = retryWithConfig(() -> callExternalServiceWithResult(), config);
            System.out.println("Production retry succeeded: " + result);
            
        } catch (ServiceUnavailableException e) {
            System.out.println("Production retry failed after " + 
                config.maxRetries + " attempts");
        }
    }
    
    private static void testRetryWithDifferentExceptions() {
        System.out.println("\n--- Retry with Different Exceptions ---");
        
        int maxRetries = 3;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("Attempt " + attempt);
                callServiceWithDifferentExceptions();
                System.out.println("Success");
                break;
                
            } catch (ServiceUnavailableException e) {
                // Retry on service unavailable
                System.out.println("Retrying on ServiceUnavailableException");
                
            } catch (RuntimeException e) {
                // Don't retry on programming errors
                System.out.println("Not retrying on RuntimeException: " + e.getMessage());
                break;
            }
        }
    }
    
    private static void callExternalService() throws ServiceUnavailableException {
        // Simulate external service that fails 70% of the time
        if (ThreadLocalRandom.current().nextDouble() < 0.7) {
            throw new ServiceUnavailableException("Service temporarily unavailable");
        }
    }
    
    private static String callExternalServiceWithResult() throws ServiceUnavailableException {
        if (ThreadLocalRandom.current().nextDouble() < 0.6) {
            throw new ServiceUnavailableException("Service unavailable");
        }
        return "success_data";
    }
    
    private static void callServiceWithDifferentExceptions() throws ServiceUnavailableException {
        double random = ThreadLocalRandom.current().nextDouble();
        
        if (random < 0.5) {
            throw new ServiceUnavailableException("Service unavailable");
        } else if (random < 0.8) {
            throw new RuntimeException("Programming error");
        }
        // Success case
    }
    
    // Generic retry method with configuration
    private static <T> T retryWithConfig(RetryOperation<T> operation, 
                                        RetryConfig config) throws ServiceUnavailableException {
        long delayMs = config.initialDelayMs;
        
        for (int attempt = 1; attempt <= config.maxRetries; attempt++) {
            try {
                return operation.execute();
                
            } catch (ServiceUnavailableException e) {
                if (attempt == config.maxRetries) {
                    throw e;
                }
                
                System.out.println("Attempt " + attempt + " failed, retrying in " + 
                    delayMs + "ms");
                
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ServiceUnavailableException("Retry interrupted", ie);
                }
                
                delayMs = Math.min((long) (delayMs * config.backoffMultiplier), 
                                  config.maxDelayMs);
            }
        }
        
        throw new ServiceUnavailableException("Max retries exceeded");
    }
    
    // Functional interface for retry operations
    @FunctionalInterface
    interface RetryOperation<T> {
        T execute() throws ServiceUnavailableException;
    }
}