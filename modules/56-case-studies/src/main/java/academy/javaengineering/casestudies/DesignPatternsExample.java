package academy.javaengineering.casestudies;

/**
 * Demonstrates design patterns from case studies.
 */
public class DesignPatternsExample {

    public interface PaymentProcessor {
        boolean processPayment(double amount);
    }

    public static class CreditCardProcessor implements PaymentProcessor {
        @Override
        public boolean processPayment(double amount) {
            System.out.println("Processing credit card payment: $" + amount);
            return true;
        }
    }

    public static class PayPalProcessor implements PaymentProcessor {
        @Override
        public boolean processPayment(double amount) {
            System.out.println("Processing PayPal payment: $" + amount);
            return true;
        }
    }

    public static class PaymentProcessorFactory {
        public static PaymentProcessor createProcessor(String type) {
            return switch (type.toLowerCase()) {
                case "creditcard" -> new CreditCardProcessor();
                case "paypal" -> new PayPalProcessor();
                default -> throw new IllegalArgumentException("Unknown payment type: " + type);
            };
        }
    }
}
