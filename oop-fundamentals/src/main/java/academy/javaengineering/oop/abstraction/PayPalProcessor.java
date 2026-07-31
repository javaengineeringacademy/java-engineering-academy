package academy.javaengineering.oop.abstraction;

/**
 * PayPalProcessor - Concrete implementation of PaymentProcessor.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class PayPalProcessor implements PaymentProcessor {

    @Override
    public void processPayment(double amount) {
        if (!validateAmount(amount)) return;
        System.out.println("  Processing PayPal payment of $" + amount);
        System.out.println("  Redirecting to PayPal for authentication...");
        System.out.println("  Payment completed via PayPal!");
    }

    @Override
    public String getProvider() {
        return "PayPal";
    }
}