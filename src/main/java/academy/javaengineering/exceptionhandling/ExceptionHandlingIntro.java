package academy.javaengineering.exceptionhandling;

/**
 * Exception Handling Introduction - Basic Examples
 * 
 * This class demonstrates the fundamental concepts of exception handling in Java.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ExceptionHandlingIntro {

    /**
     * Demonstrates basic exception handling with try-catch.
     */
    public static void basicExceptionHandling() {
        System.out.println("=== Basic Exception Handling ===");
        
        try {
            int result = 10 / 0;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("Program continues after exception\n");
    }

    /**
     * Demonstrates multiple catch blocks.
     */
    public static void multipleCatchBlocks() {
        System.out.println("=== Multiple Catch Blocks ===");
        
        try {
            String text = null;
            int length = text.length();
        } catch (NullPointerException e) {
            System.out.println("Null pointer error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception hierarchy.
     */
    public static void exceptionHierarchy() {
        System.out.println("=== Exception Hierarchy ===");
        
        // Checked exception example
        try {
            java.io.FileReader file = new java.io.FileReader("nonexistent.txt");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found (checked exception): " + e.getMessage());
        }
        
        // Unchecked exception example
        try {
            int[] numbers = new int[5];
            numbers[10] = 100;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index out of bounds (unchecked): " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception chaining.
     */
    public static void exceptionChaining() {
        System.out.println("=== Exception Chaining ===");
        
        try {
            processData();
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Caused by: " + e.getCause().getMessage());
        }
        
        System.out.println();
    }

    static void processData() throws Exception {
        try {
            int result = Integer.parseInt("invalid");
        } catch (NumberFormatException e) {
            throw new Exception("Failed to process data", e);
        }
    }

    /**
     * Demonstrates try-with-resources.
     */
    public static void tryWithResources() {
        System.out.println("=== Try-With-Resources ===");
        
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.StringReader("Hello\nWorld"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (java.io.IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates custom exceptions.
     */
    public static void customExceptions() {
        System.out.println("=== Custom Exceptions ===");
        
        try {
            validateAge(-5);
        } catch (InvalidAgeException e) {
            System.out.println("Invalid age: " + e.getMessage());
            System.out.println("Age value: " + e.getAge());
        }
        
        System.out.println();
    }

    static void validateAge(int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Age must be between 0 and 150", age);
        }
        System.out.println("Valid age: " + age);
    }

    /**
     * Custom exception class for invalid age.
     */
    static class InvalidAgeException extends Exception {
        private final int age;
        
        InvalidAgeException(String message, int age) {
            super(message);
            this.age = age;
        }
        
        int getAge() {
            return age;
        }
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        basicExceptionHandling();
        multipleCatchBlocks();
        exceptionHierarchy();
        exceptionChaining();
        tryWithResources();
        customExceptions();
    }
}
