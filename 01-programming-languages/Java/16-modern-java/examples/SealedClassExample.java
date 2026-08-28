package academy.javaengineering.modern;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates sealed classes with pattern matching.
 */
public class SealedClassExample {

    sealed interface Payment permits CreditCard, BankTransfer, PayPal {}
    record CreditCard(String number, String expiry) implements Payment {}
    record BankTransfer(String accountNumber, String routingNumber) implements Payment {}
    record PayPal(String email) implements Payment {}

    public static void main(String[] args) {
        List<Payment> payments = List.of(
            new CreditCard("4111-1111-1111-1111", "12/25"),
            new BankTransfer("123456789", "021000021"),
            new PayPal("user@example.com"),
            new CreditCard("5555-5555-5555-4444", "01/26")
        );

        System.out.println("=== Payment Processing with Sealed Classes ===\n");

        for (Payment payment : payments) {
            String result = processPayment(payment);
            System.out.println(result);
        }

        // Pattern matching in switch
        System.out.println("\n=== Payment Type Identification ===\n");
        for (Payment payment : payments) {
            String type = identifyPaymentType(payment);
            System.out.println(type);
        }
    }

    static String processPayment(Payment payment) {
        return switch (payment) {
            case CreditCard cc -> 
                "Processing credit card: " + cc.number().substring(0, 4) + "****";
            case BankTransfer bt -> 
                "Processing bank transfer to account: " + bt.accountNumber();
            case PayPal pp -> 
                "Processing PayPal payment to: " + pp.email();
        };
    }

    static String identifyPaymentType(Payment payment) {
        return switch (payment) {
            case CreditCard cc -> "CreditCard: " + cc.number();
            case BankTransfer bt -> "BankTransfer: " + bt.accountNumber();
            case PayPal pp -> "PayPal: " + pp.email();
        };
    }
}
