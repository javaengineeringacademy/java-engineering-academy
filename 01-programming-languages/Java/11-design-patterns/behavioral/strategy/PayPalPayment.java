package academy.javaengineering.patterns.behavioral.strategy;

/**
 * Concrete Strategy implementation - PayPal payment.
 * Processes payments using PayPal account.
 */
public class PayPalPayment implements PaymentStrategy {

    private final String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public boolean pay(double amount) {
        System.out.printf("PayPal Payment: $%.2f to account %s%n", amount, email);
        return true;
    }

    public String getEmail() {
        return email;
    }
}
