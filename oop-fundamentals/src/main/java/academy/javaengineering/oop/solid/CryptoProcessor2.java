package academy.javaengineering.oop.solid;

/**
 * CryptoProcessor2 - Demonstrates Liskov Substitution.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CryptoProcessor2 implements PaymentProcessor2 {

    @Override
    public void process(double amount) {
        System.out.println("  [CRYPTO] Processing $" + amount + " in Bitcoin");
    }

    @Override
    public String getPaymentMethod() {
        return "Cryptocurrency";
    }

    @Override
    public boolean supportsRefund() {
        return false; // Crypto transactions typically non-refundable
    }
}