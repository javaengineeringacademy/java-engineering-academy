package academy.javaengineering.exceptions.examples;

import java.util.HashMap;
import java.util.Map;

/**
 * RuntimeException recovery example demonstrating graceful handling of runtime exceptions.
 * Shows techniques for recovering from unexpected runtime failures.
 */
public class RuntimeExceptionRecoveryExample {

    public static void main(String[] args) {
        System.out.println("=== RuntimeException Recovery Example ===");
        
        // Example 1: Null check recovery
        testNullCheckRecovery();
        
        // Example 2: Default value recovery
        testDefaultValueRecovery();
        
        // Example 3: Retry with fallback
        testRetryWithFallback();
        
        // Example 4: Graceful degradation
        testGracefulDegradation();
        
        System.out.println("=== End of RuntimeException Recovery Example ===");
    }
    
    private static void testNullCheckRecovery() {
        System.out.println("\n--- Null Check Recovery ---");
        
        String input = null;
        
        try {
            // Attempt operation that might throw NullPointerException
            int length = input.length();
            System.out.println("Length: " + length);
            
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException, using default");
            // Recovery: provide default value
            input = "default";
            int length = input.length();
            System.out.println("Length after recovery: " + length);
        }
    }
    
    private static void testDefaultValueRecovery() {
        System.out.println("\n--- Default Value Recovery ---");
        
        Map<String, String> config = new HashMap<>();
        config.put("host", "localhost");
        // "port" is missing
        
        try {
            // Attempt to get configuration value
            String port = config.get("port");
            int portNumber = Integer.parseInt(port);
            System.out.println("Port: " + portNumber);
            
        } catch (NullPointerException e) {
            System.out.println("Config value missing, using default");
            // Recovery: use default configuration
            int portNumber = 8080;
            System.out.println("Port after recovery: " + portNumber);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid config format, using default");
            int portNumber = 8080;
            System.out.println("Port after recovery: " + portNumber);
        }
    }
    
    private static void testRetryWithFallback() {
        System.out.println("\n--- Retry with Fallback ---");
        
        String result = null;
        
        // Try primary approach
        try {
            result = riskyOperation();
            System.out.println("Primary operation succeeded: " + result);
            
        } catch (RuntimeException e) {
            System.out.println("Primary operation failed: " + e.getMessage());
            
            // Try fallback approach
            try {
                result = fallbackOperation();
                System.out.println("Fallback operation succeeded: " + result);
                
            } catch (RuntimeException fallbackException) {
                System.out.println("Fallback also failed: " + fallbackException.getMessage());
                
                // Final recovery: use default
                result = "default_value";
                System.out.println("Using default value: " + result);
            }
        }
    }
    
    private static void testGracefulDegradation() {
        System.out.println("\n--- Graceful Degradation ---");
        
        try {
            // Attempt complex operation
            String complexResult = complexOperation();
            System.out.println("Complex operation result: " + complexResult);
            
        } catch (RuntimeException e) {
            System.out.println("Complex operation failed, degrading gracefully");
            
            // Provide simplified result
            String simplifiedResult = simplifiedOperation();
            System.out.println("Simplified result: " + simplifiedResult);
        }
    }
    
    private static String riskyOperation() {
        // Simulate operation that might fail
        if (Math.random() > 0.5) {
            throw new RuntimeException("Operation failed randomly");
        }
        return "success";
    }
    
    private static String fallbackOperation() {
        // Simulate fallback that might also fail
        if (Math.random() > 0.3) {
            throw new RuntimeException("Fallback failed");
        }
        return "fallback_success";
    }
    
    private static String complexOperation() {
        // Simulate complex operation that fails
        throw new RuntimeException("Complex operation not available");
    }
    
    private static String simplifiedOperation() {
        return "simplified_result";
    }
}