package academy.javaengineering.exceptionhandling;

import java.io.*;

/**
 * Throws Keyword Examples
 * 
 * Demonstrates the usage of the throws keyword in method signatures.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ThrowsExamples {

    /**
     * Demonstrates basic throws usage.
     */
    public static void basicThrows() throws IOException {
        System.out.println("=== Basic Throws ===");
        
        readFile("test.txt");
        
        System.out.println();
    }

    static void readFile(String filename) throws IOException {
        FileReader reader = new FileReader(filename);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        System.out.println("Read: " + line);
        br.close();
    }

    /**
     * Demonstrates throws with multiple exceptions.
     */
    public static void multipleExceptions() throws IOException, SQLException {
        System.out.println("=== Multiple Exceptions ===");
        
        readData();
        saveToDatabase();
        
        System.out.println();
    }

    static void readData() throws IOException {
        // Simulate IO operation
        throw new IOException("Read operation failed");
    }

    static void saveToDatabase() throws SQLException {
        // Simulate database operation
        throw new SQLException("Database operation failed");
    }

    static class SQLException extends Exception {
        SQLException(String message) {
            super(message);
        }
    }

    /**
     * Demonstrates throws with try-catch.
     */
    public static void throwsWithTryCatch() {
        System.out.println("=== Throws With Try-Catch ===");
        
        try {
            riskyOperation();
        } catch (IOException e) {
            System.out.println("Caught in caller: " + e.getMessage());
        }
        
        System.out.println();
    }

    static void riskyOperation() throws IOException {
        try {
            throw new IOException("Operation failed");
        } catch (IOException e) {
            // Log and rethrow
            System logged = new System();
            System.out.println("Logging: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Demonstrates throws in method overriding.
     */
    public static void methodOverriding() {
        System.out.println("=== Method Overriding ===");
        
        Parent parent = new Child();
        
        try {
            parent.process();
        } catch (IOException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        
        System.out.println();
    }

    static class Parent {
        void process() throws IOException {
            System.out.println("Parent processing");
        }
    }

    static class Child extends Parent {
        @Override
        void process() throws IOException {
            System.out.println("Child processing");
            // Can declare same or narrower exceptions
        }
    }

    /**
     * Demonstrates throws with constructor.
     */
    public static void constructorThrows() {
        System.out.println("=== Constructor Throws ===");
        
        try {
            DatabaseConnection conn = new DatabaseConnection("jdbc:mysql://localhost/db");
            System.out.println("Connected: " + conn);
        } catch (ConnectionException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        
        System.out.println();
    }

    static class DatabaseConnection {
        private final String url;
        
        DatabaseConnection(String url) throws ConnectionException {
            if (url == null || url.isEmpty()) {
                throw new ConnectionException("URL cannot be null or empty");
            }
            this.url = url;
        }
        
        @Override
        public String toString() {
            return "DatabaseConnection{url='" + url + "'}";
        }
    }

    static class ConnectionException extends Exception {
        ConnectionException(String message) {
            super(message);
        }
    }

    /**
     * Demonstrates throws with interface.
     */
    public static void interfaceThrows() {
        System.out.println("=== Interface Throws ===");
        
        DataProcessor processor = new CSVProcessor();
        
        try {
            processor.processData("test,data");
        } catch (InvalidDataException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    interface DataProcessor {
        void processData(String data) throws InvalidDataException, IOException;
        
        default void processDataWithRetry(String data, int retries) 
                throws InvalidDataException, IOException {
            for (int i = 0; i < retries; i++) {
                try {
                    processData(data);
                    return;
                } catch (IOException e) {
                    if (i == retries - 1) {
                        throw e;
                    }
                    System.out.println("Retry " + (i + 1) + " failed");
                }
            }
        }
    }

    static class InvalidDataException extends Exception {
        InvalidDataException(String message) {
            super(message);
        }
    }

    static class CSVProcessor implements DataProcessor {
        @Override
        public void processData(String data) throws InvalidDataException, IOException {
            if (data == null || data.isEmpty()) {
                throw new InvalidDataException("Data cannot be empty");
            }
            System.out.println("Processing: " + data);
        }
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        try {
            basicThrows();
        } catch (IOException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        
        try {
            multipleExceptions();
        } catch (IOException | SQLException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        
        throwsWithTryCatch();
        methodOverriding();
        constructorThrows();
        interfaceThrows();
    }
}
