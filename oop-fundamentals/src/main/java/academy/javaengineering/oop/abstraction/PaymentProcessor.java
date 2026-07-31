package academy.javaengineering.oop.abstraction;

/**
 * PaymentProcessor - Interface for payment processing abstraction.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface PaymentProcessor {

    void processPayment(double amount);

    String getProvider();

    default boolean validateAmount(double amount) {
        if (amount <= 0) {
            System.out.println("  Error: Amount must be positive");
            return false;
        }
        if (amount > 10000) {
            System.out.println("  Warning: Amount exceeds single transaction limit");
            return false;
        }
        return true;
    }

    default void sendReceipt(String email, double amount) {
        System.out.println("  Receipt sent to " + email + " for $" + amount);
    }
}