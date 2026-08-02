package academy.javaengineering.exceptionhandling;

/**
 * Exercises Examples
 * 
 * Provides exercises for practicing exception handling.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ExercisesExamples {

    /**
     * Exercise 1: Calculator Exception Handling
     */
    public static void exercise1Calculator() {
        System.out.println("=== Exercise 1: Calculator Exception Handling ===\n");
        
        System.out.println("Task: Create a calculator that handles:");
        System.out.println("- Division by zero");
        System.out.println("- Invalid operators");
        System.out.println("- Number format exceptions");
        System.out.println("- Overflow conditions");
        
        System.out.println("\nSolution:");
        try {
            Calculator calculator = new Calculator();
            System.out.println("10 + 5 = " + calculator.calculate(10, 5, '+'));
            System.out.println("10 - 5 = " + calculator.calculate(10, 5, '-'));
            System.out.println("10 * 5 = " + calculator.calculate(10, 5, '*'));
            System.out.println("10 / 5 = " + calculator.calculate(10, 5, '/'));
            System.out.println("10 / 0 = " + calculator.calculate(10, 0, '/'));
        } catch (CalculatorException e) {
            System.out.println("Calculator error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Exercise 2: File Processor
     */
    public static void exercise2FileProcessor() {
        System.out.println("=== Exercise 2: File Processor ===\n");
        
        System.out.println("Task: Create a file processor that handles:");
        System.out.println("- FileNotFoundException");
        System.out.println("- Permission denied");
        System.out.println("- Corrupted file");
        System.out.println("- Proper resource cleanup");
        
        System.out.println("\nSolution:");
        FileProcessor processor = new FileProcessor();
        processor.processFile("test.txt");
        
        System.out.println();
    }

    /**
     * Exercise 3: Network Client
     */
    public static void exercise3NetworkClient() {
        System.out.println("=== Exercise 3: Network Client ===\n");
        
        System.out.println("Task: Create a network client that handles:");
        System.out.println("- Connection timeout");
        System.out.println("- DNS resolution failure");
        System.out.println("- SSL/TLS errors");
        System.out.println("- Retry logic");
        
        System.out.println("\nSolution:");
        NetworkClient client = new NetworkClient();
        client.connect("example.com", 80);
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        exercise1Calculator();
        exercise2FileProcessor();
        exercise3NetworkClient();
    }

    // Supporting classes

    static class Calculator {
        double calculate(double a, double b, char operator) throws CalculatorException {
            switch (operator) {
                case '+': return a + b;
                case '-': return a - b;
                case '*': return a * b;
                case '/':
                    if (b == 0) {
                        throw new CalculatorException("Division by zero");
                    }
                    return a / b;
                default:
                    throw new CalculatorException("Invalid operator: " + operator);
            }
        }
    }

    static class CalculatorException extends Exception {
        CalculatorException(String message) {
            super(message);
        }
    }

    static class FileProcessor {
        void processFile(String filename) {
            java.io.BufferedReader reader = null;
            try {
                reader = new java.io.BufferedReader(new java.io.FileReader(filename));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            } catch (java.io.IOException e) {
                System.out.println("IO error: " + e.getMessage());
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (java.io.IOException e) {
                    System.out.println("Close error: " + e.getMessage());
                }
            }
        }
    }

    static class NetworkClient {
        void connect(String host, int port) {
            try {
                System.out.println("Connecting to " + host + ":" + port);
                // Simulate connection
                if (Math.random() > 0.7) {
                    throw new java.net.ConnectException("Connection refused");
                }
                System.out.println("Connected successfully");
            } catch (java.net.ConnectException e) {
                System.out.println("Connection failed: " + e.getMessage());
                System.out.println("Retrying...");
                // Retry logic
            } catch (Exception e) {
                System.out.println("Network error: " + e.getMessage());
            }
        }
    }
}
