package academy.javaengineering.exceptionhandling;

public class BestPractices {

    public static void main(String[] args) {
        avoidCatchingGenericException();
        useSpecificExceptions();
        preserveCause();
    }

    public static void avoidCatchingGenericException() {
        try {
            String input = "abc";
            int value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Bad number: " + e.getMessage());
        }
    }

    public static void useSpecificExceptions() {
        try {
            processOrder(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument: " + e.getMessage());
        }
    }

    public static void preserveCause() {
        try {
            riskyOperation();
        } catch (Exception e) {
            System.out.println("Cause: " + e.getCause());
        }
    }

    public static void processOrder(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        System.out.println("Processing order: " + orderId);
    }

    public static void riskyOperation() throws Exception {
        try {
            throw new RuntimeException("Database error");
        } catch (RuntimeException e) {
            throw new Exception("Service unavailable", e);
        }
    }

    public static void useTryWithResources() {
        try (var resource = new AutoCloseableResource()) {
            resource.doWork();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static class AutoCloseableResource implements AutoCloseable {
        void doWork() {
            System.out.println("Working with resource");
        }

        @Override
        public void close() {
            System.out.println("Resource closed automatically");
        }
    }
}
