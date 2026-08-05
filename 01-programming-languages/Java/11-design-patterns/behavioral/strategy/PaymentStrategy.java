package academy.javaengineering.patterns.behavioral.strategy;

/**
 * Strategy interface for payment processing.
 * Each payment method implements this interface.
 */
public interface PaymentStrategy {

    /**
     * Process a payment.
     *
     * @param amount the amount to pay
     * @return true if payment was successful
     */
    boolean pay(double amount);
}
