package academy.javaengineering.cleancode;

import java.util.List;
import java.util.ArrayList;

/**
 * Demonstrates clean code functions and methods.
 */
public class CleanFunctions {

    // Bad: Long function with multiple responsibilities
    void processOrderBad(String orderId) {
        // Validate order
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        
        // Calculate total
        double total = 0;
        // ... calculation logic
        
        // Apply discount
        if (total > 100) {
            total *= 0.9;
        }
        
        // Send notification
        System.out.println("Order processed: " + orderId);
        
        // Update inventory
        System.out.println("Inventory updated");
    }

    // Good: Short, focused functions
    void processOrderGood(String orderId) {
        validateOrderId(orderId);
        double total = calculateTotal(orderId);
        total = applyDiscount(total);
        sendOrderConfirmation(orderId, total);
        updateInventory(orderId);
    }

    private void validateOrderId(String orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
    }

    private double calculateTotal(String orderId) {
        return 0.0;
    }

    private double applyDiscount(double total) {
        return total > 100 ? total * 0.9 : total;
    }

    private void sendOrderConfirmation(String orderId, double total) {
        System.out.printf("Order %s confirmed. Total: $%.2f%n", orderId, total);
    }

    private void updateInventory(String orderId) {
        System.out.println("Inventory updated for order: " + orderId);
    }

    // Function parameters
    void createUser(String name, String email, int age) { }

    // Avoid boolean parameters
    void sendEmail(String to, String subject, String body) { }
    void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment) { }
}
