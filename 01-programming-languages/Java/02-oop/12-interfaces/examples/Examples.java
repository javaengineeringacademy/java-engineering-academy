package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Interface Patterns ===\n");

        // WHY: Interfaces define pure contracts. Enable polymorphism and testability.
        // INTERNAL: invokeinterface instruction, default methods use invokespecial
        // ENGINEERING: Prefer interfaces over abstract classes for flexibility

        PaymentProcessor card = new CreditCardPayment("4111-1111-1111-1111");
        PaymentProcessor paypal = new PaypalPayment("user@email.com");

        processPayment(card, 99.99);
        processPayment(paypal, 49.99);

        // TRADE-OFF: Interface vs abstract class
        // Interface: multiple inheritance, no state, more flexible
        // Abstract class: shared state, constructors, evolution easier
        // Java 8+ default methods blur the line
    }

    static void processPayment(PaymentProcessor processor, double amount) {
        if (processor.validate()) {
            String ref = processor.charge(amount);
            System.out.println("Charged $" + amount + " via " + processor.getType() + " ref=" + ref);
        }
    }
}

interface PaymentProcessor {
    boolean validate();
    String charge(double amount);
    String getType();
    default void refund(String ref) {
        System.out.println("Refund initiated for: " + ref);
    }
}

class CreditCardPayment implements PaymentProcessor {
    private final String cardNumber;
    CreditCardPayment(String cardNumber) { this.cardNumber = cardNumber; }

    @Override public boolean validate() { return cardNumber != null && cardNumber.length() == 19; }
    @Override public String charge(double amount) { return "CC-" + System.currentTimeMillis(); }
    @Override public String getType() { return "CREDIT_CARD"; }
}

class PaypalPayment implements PaymentProcessor {
    private final String email;
    PaypalPayment(String email) { this.email = email; }

    @Override public boolean validate() { return email != null && email.contains("@"); }
    @Override public String charge(double amount) { return "PP-" + System.currentTimeMillis(); }
    @Override public String getType() { return "PAYPAL"; }
}
