package academy.javaengineering.oop.solid;

/**
 * PaymentProcessor2 - Interface for Liskov Substitution Principle demonstration.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface PaymentProcessor2 {

    void process(double amount);
    String getPaymentMethod();
    boolean supportsRefund();
}