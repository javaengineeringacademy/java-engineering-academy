package academy.javaengineering.cleancode;

import java.util.List;
import java.util.ArrayList;

/**
 * Demonstrates clean code comments and documentation.
 */
public class CleanComments {

    // Bad comments
    // This method adds two numbers
    int add(int a, int b) {
        return a + b;
    }

    // TODO: Fix this later
    // HACK: Temporary workaround
    // FIXME: This is broken

    // Good comments: Explain why, not what
    // Using TreeMap because we need sorted keys for reporting
    // Exception is rethrown to maintain transaction integrity
    // This value is magic number from legacy system API
    static final int LEGACY_SYSTEM_TIMEOUT = 30000;

    // Javadoc for public API
    /**
     * Calculates the total price including tax and shipping.
     *
     * @param items list of items to calculate
     * @param taxRate tax rate as percentage (e.g., 8.5 for 8.5%)
     * @param shippingCost flat shipping cost
     * @return total price including all costs
     * @throws IllegalArgumentException if items is null or empty
     */
    double calculateTotal(List<Double> items, double taxRate, double shippingCost) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items cannot be null or empty");
        }
        
        double subtotal = items.stream().mapToDouble(Double::doubleValue).sum();
        double tax = subtotal * (taxRate / 100);
        return subtotal + tax + shippingCost;
    }
}
