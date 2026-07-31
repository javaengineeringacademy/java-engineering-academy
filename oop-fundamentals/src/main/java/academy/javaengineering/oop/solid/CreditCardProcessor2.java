package academy.javaengineering.oop.solid;

/**
 * CreditCardProcessor2 - Demonstrates Liskov Substitution.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CreditCardProcessor2 implements PaymentProcessor2 {

    @Override
    public void process(double amount) {
        System.out.println("  [CREDIT CARD] Processing $" + amount);
    }

    @Override
    public String getPaymentMethod() {
        return "Credit Card";
    }

    @Override
    public boolean supportsRefund() {
        return true;
    }
}