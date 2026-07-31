package academy.javaengineering.oop.abstraction;

/**
 * CreditCardProcessor - Concrete implementation of PaymentProcessor.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CreditCardProcessor implements PaymentProcessor {

    @Override
    public void processPayment(double amount) {
        if (!validateAmount(amount)) return;
        System.out.println("  Processing credit card payment of $" + amount);
        System.out.println("  Payment authorized and completed!");
    }

    @Override
    public String getProvider() {
        return "Credit Card (Visa/Mastercard)";
    }
}