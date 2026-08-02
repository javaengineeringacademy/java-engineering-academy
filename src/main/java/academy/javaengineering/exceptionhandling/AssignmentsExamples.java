package academy.javaengineering.exceptionhandling;

/**
 * Assignments Examples
 * 
 * Provides assignment solutions for exception handling.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class AssignmentsExamples {

    /**
     * Assignment 1: Calculator with Exception Handling
     */
    public static void assignment1Calculator() {
        System.out.println("=== Assignment 1: Calculator with Exception Handling ===\n");
        
        RobustCalculator calculator = new RobustCalculator();
        
        try {
            System.out.println("10 + 5 = " + calculator.add(10, 5));
            System.out.println("10 - 5 = " + calculator.subtract(10, 5));
            System.out.println("10 * 5 = " + calculator.multiply(10, 5));
            System.out.println("10 / 5 = " + calculator.divide(10, 5));
            System.out.println("10 / 0 = " + calculator.divide(10, 0));
        } catch (ArithmeticException e) {
            System.out.println("Math error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Assignment 2: File Processor
     */
    public static void assignment2FileProcessor() {
        System.out.println("=== Assignment 2: File Processor ===\n");
        
        RobustFileProcessor processor = new RobustFileProcessor();
        processor.processFile("test.txt");
        
        System.out.println();
    }

    /**
     * Assignment 3: Transaction Manager
     */
    public static void assignment3TransactionManager() {
        System.out.println("=== Assignment 3: Transaction Manager ===\n");
        
        TransactionManager manager = new TransactionManager();
        
        try {
            manager.beginTransaction();
            System.out.println("Performing operation 1");
            System.out.println("Performing operation 2");
            manager.commit();
            System.out.println("Transaction committed");
        } catch (TransactionException e) {
            manager.rollback();
            System.out.println("Transaction rolled back: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        assignment1Calculator();
        assignment2FileProcessor();
        assignment3TransactionManager();
    }

    // Supporting classes

    static class RobustCalculator {
        double add(double a, double b) {
            return a + b;
        }
        
        double subtract(double a, double b) {
            return a - b;
        }
        
        double multiply(double a, double b) {
            return a * b;
        }
        
        double divide(double a, double b) throws ArithmeticException {
            if (b == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        }
    }

    static class RobustFileProcessor {
        void processFile(String filename) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            } catch (java.io.IOException e) {
                System.out.println("IO error: " + e.getMessage());
            }
        }
    }

    static class TransactionManager {
        void beginTransaction() throws TransactionException {
            System.out.println("Transaction begun");
        }
        
        void commit() throws TransactionException {
            System.out.println("Committing transaction");
        }
        
        void rollback() {
            System.out.println("Rolling back transaction");
        }
    }

    static class TransactionException extends Exception {
        TransactionException(String message) {
            super(message);
        }
    }
}
