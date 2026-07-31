package academy.javaengineering.oop.solid;

/**
 * PayPalProcessor2 - Demonstrates Liskov Substitution.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class PayPalProcessor2 implements PaymentProcessor2 {

    @Override
    public void process(double amount) {
        System.out.println("  [PAYPAL] Processing $" + amount);
    }

    @Override
    public String getPaymentMethod() {
        return "PayPal";
    }

    @Override
    public boolean supportsRefund() {
        return true;
    }
}