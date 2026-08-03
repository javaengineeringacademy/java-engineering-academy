package academy.javaengineering.patterns.strategy;

/**
 * Demonstrates the Strategy design pattern for interchangeable algorithms.
 *
 * <p>The Strategy pattern defines a family of algorithms, encapsulates each one,
 * and makes them interchangeable. It lets the algorithm vary independently from
 * clients that use it.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Strategy interface defining algorithm contract</li>
 *   <li>Concrete strategy implementations</li>
 *   <li>Context delegates to strategy at runtime</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class StrategyExample {

    /**
     * Strategy interface for payment processing algorithms.
     */
    public interface PaymentStrategy {
        /**
         * Processes a payment of the specified amount.
         *
         * @param amount the payment amount
         */
        void pay(double amount);
    }

    /**
     * Credit card payment strategy implementation.
     */
    public static class CreditCardPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Paid " + amount + " via Credit Card");
        }
    }

    /**
     * PayPal payment strategy implementation.
     */
    public static class PayPalPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Paid " + amount + " via PayPal");
        }
    }

    /**
     * Shopping cart context that uses a payment strategy.
     */
    public static class ShoppingCart {
        private PaymentStrategy paymentStrategy;

        /**
         * Sets the payment strategy to use for checkout.
         *
         * @param strategy the payment strategy
         */
        public void setPaymentStrategy(PaymentStrategy strategy) {
            this.paymentStrategy = strategy;
        }

        /**
         * Processes checkout using the configured strategy.
         *
         * @param total the total amount to pay
         */
        public void checkout(double total) {
            paymentStrategy.pay(total);
        }
    }

    /**
     * Demonstrates strategy pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.setPaymentStrategy(new CreditCardPayment());
        cart.checkout(99.99);

        cart.setPaymentStrategy(new PayPalPayment());
        cart.checkout(49.99);
    }
}
