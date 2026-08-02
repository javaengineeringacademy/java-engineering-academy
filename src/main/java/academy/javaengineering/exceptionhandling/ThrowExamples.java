package academy.javaengineering.exceptionhandling;

/**
 * Throw Keyword Examples
 * 
 * Demonstrates the usage of the throw keyword to explicitly抛出 exceptions.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ThrowExamples {

    /**
     * Demonstrates basic throw usage.
     */
    public static void basicThrow() {
        System.out.println("=== Basic Throw ===");
        
        try {
            int age = -5;
            validateAge(age);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
        
        System.out.println();
    }

    static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        System.out.println("Valid age: " + age);
    }

    /**
     * Demonstrates throwing different exception types.
     */
    public static void differentExceptionTypes() {
        System.out.println("=== Different Exception Types ===");
        
        // Throw RuntimeException
        try {
            throw new RuntimeException("Runtime error");
        } catch (RuntimeException e) {
            System.out.println("Runtime: " + e.getMessage());
        }
        
        // Throw checked exception
        try {
            throw new Exception("Checked error");
        } catch (Exception e) {
            System.out.println("Checked: " + e.getMessage());
        }
        
        // Throw Error (not recommended)
        try {
            throw new Error("Error");
        } catch (Error e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates throw with exception chaining.
     */
    public static void exceptionChaining() {
        System.out.println("=== Exception Chaining ===");
        
        try {
            processData();
        } catch (DataProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
        
        System.out.println();
    }

    static void processData() throws DataProcessingException {
        try {
            Integer.parseInt("invalid");
        } catch (NumberFormatException e) {
            throw new DataProcessingException("Failed to process data", e);
        }
    }

    static class DataProcessingException extends Exception {
        DataProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Demonstrates throw in method chain.
     */
    public static void throwInMethodChain() {
        System.out.println("=== Throw In Method Chain ===");
        
        try {
            step1();
        } catch (ApplicationException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Caused by: " + e.getCause().getMessage());
        }
        
        System.out.println();
    }

    static void step1() throws ApplicationException {
        try {
            step2();
        } catch (Exception e) {
            throw new ApplicationException("Step 1 failed", e);
        }
    }

    static void step2() throws Exception {
        try {
            step3();
        } catch (Exception e) {
            throw new Exception("Step 2 failed", e);
        }
    }

    static void step3() throws Exception {
        throw new Exception("Step 3 failed - root cause");
    }

    static class ApplicationException extends Exception {
        ApplicationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Demonstrates throw with custom exception.
     */
    public static void throwWithCustomException() {
        System.out.println("=== Throw With Custom Exception ===");
        
        try {
            String email = "invalid-email";
            validateEmail(email);
        } catch (InvalidEmailException e) {
            System.out.println("Invalid email: " + e.getMessage());
            System.out.println("Email value: " + e.getEmail());
        }
        
        System.out.println();
    }

    static void validateEmail(String email) throws InvalidEmailException {
        if (email == null || !email.contains("@")) {
            throw new InvalidEmailException("Invalid email format", email);
        }
        System.out.println("Valid email: " + email);
    }

    static class InvalidEmailException extends Exception {
        private final String email;
        
        InvalidEmailException(String message, String email) {
            super(message);
            this.email = email;
        }
        
        String getEmail() {
            return email;
        }
    }

    /**
     * Demonstrates throw in constructor.
     */
    public static void throwInConstructor() {
        System.out.println("=== Throw In Constructor ===");
        
        try {
            Resource resource = Resource.create();
            System.out.println("Resource created: " + resource);
        } catch (ResourceInitializationException e) {
            System.out.println("Failed to create resource: " + e.getMessage());
        }
        
        System.out.println();
    }

    static class Resource {
        private final String name;
        
        private Resource(String name) {
            this.name = name;
        }
        
        static Resource create() throws ResourceInitializationException {
            // Simulate initialization failure
            if (Math.random() > 0.5) {
                throw new ResourceInitializationException("Failed to initialize resource");
            }
            return new Resource("MyResource");
        }
        
        @Override
        public String toString() {
            return "Resource{name='" + name + "'}";
        }
    }

    static class ResourceInitializationException extends Exception {
        ResourceInitializationException(String message) {
            super(message);
        }
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        basicThrow();
        differentExceptionTypes();
        exceptionChaining();
        throwInMethodChain();
        throwWithCustomException();
        throwInConstructor();
    }
}
