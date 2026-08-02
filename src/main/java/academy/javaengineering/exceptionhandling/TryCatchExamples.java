package academy.javaengineering.exceptionhandling;

import java.io.*;
import java.util.*;

/**
 * Try-Catch Block Examples
 * 
 * Demonstrates various try-catch patterns and best practices.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class TryCatchExamples {

    /**
     * Demonstrates basic try-catch usage.
     */
    public static void basicTryCatch() {
        System.out.println("=== Basic Try-Catch ===");
        
        try {
            int[] numbers = {1, 2, 3};
            System.out.println(numbers[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates multiple catch blocks with proper ordering.
     */
    public static void multipleCatchBlocks() {
        System.out.println("=== Multiple Catch Blocks ===");
        
        try {
            String data = readFile("config.txt");
            int value = Integer.parseInt(data.trim());
            int result = 100 / value;
            System.out.println("Result: " + result);
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + e.getMessage());
            
        } catch (ArithmeticException e) {
            System.out.println("Math error: " + e.getMessage());
            
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates multi-catch block (Java 7+).
     */
    public static void multiCatchBlock() {
        System.out.println("=== Multi-Catch Block ===");
        
        try {
            processInput("abc");
        } catch (IllegalArgumentException | NumberFormatException e) {
            System.out.println("Input validation failed: " + e.getMessage());
        }
        
        System.out.println();
    }

    static void processInput(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        
        int number = Integer.parseInt(input);
        int[] array = new int[10];
        array[number] = 100;
    }

    /**
     * Demonstrates nested try-catch blocks.
     */
    public static void nestedTryCatch() {
        System.out.println("=== Nested Try-Catch ===");
        
        try {
            System.out.println("Outer try block");
            
            try {
                System.out.println("Inner try block");
                int result = 10 / 0;
                System.out.println("This won't print");
            } catch (ArithmeticException e) {
                System.out.println("Inner catch: " + e.getMessage());
            }
            
            String text = null;
            int length = text.length();
            
        } catch (NullPointerException e) {
            System.out.println("Outer catch: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception in catch block.
     */
    public static void exceptionInCatch() {
        System.out.println("=== Exception in Catch Block ===");
        
        try {
            throw new RuntimeException("Original exception");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
            
            try {
                throw new RuntimeException("Exception in catch block");
            } catch (RuntimeException catchException) {
                System.out.println("Catch block exception: " + catchException.getMessage());
            }
        }
        
        System.out.println();
    }

    /**
     * Demonstrates catch order importance.
     */
    public static void catchOrderImportance() {
        System.out.println("=== Catch Order Importance ===");
        
        // Correct order - specific to general
        try {
            riskyOperation();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        
        System.out.println();
    }

    static void riskyOperation() throws IOException {
        throw new IOException("File operation failed");
    }

    /**
     * Demonstrates exception information access.
     */
    public static void exceptionInformation() {
        System.out.println("=== Exception Information ===");
        
        try {
            throw new Exception("Test exception");
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("Class: " + e.getClass().getSimpleName());
            System.out.println("Stack trace elements: " + e.getStackTrace().length);
            
            if (e.getStackTrace().length > 0) {
                System.out.println("First stack trace: " + e.getStackTrace()[0]);
            }
        }
        
        System.out.println();
    }

    /**
     * Demonstrates multi-catch with same handling.
     */
    public static void multiCatchSameHandling() {
        System.out.println("=== Multi-Catch Same Handling ===");
        
        List<String> inputs = Arrays.asList("123", "abc", "", null);
        
        for (String input : inputs) {
            try {
                processInputSafe(input);
            } catch (IllegalArgumentException | NullPointerException e) {
                System.out.println("Input error for '" + input + "': " + e.getMessage());
            }
        }
        
        System.out.println();
    }

    static void processInputSafe(String input) {
        Objects.requireNonNull(input, "Input cannot be null");
        
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        
        Integer.parseInt(input);
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        basicTryCatch();
        multipleCatchBlocks();
        multiCatchBlock();
        nestedTryCatch();
        exceptionInCatch();
        catchOrderImportance();
        exceptionInformation();
        multiCatchSameHandling();
    }
}
