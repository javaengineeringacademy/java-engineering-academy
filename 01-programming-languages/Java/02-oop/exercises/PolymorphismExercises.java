package academy.javaengineering.exercises;

/**
 * Exercises: Polymorphism and Interface Implementation
 *
 * Complete the TODO sections below.
 */
public class PolymorphismExercises {

    // TODO 1: Create a Payment system with polymorphism
    // Interface: PaymentProcessor
    // - boolean processPayment(double amount)
    // - String getPaymentMethod()
    // - double getFee(double amount) - default: returns 0.0
    //
    // Class: CreditCardPayment implements PaymentProcessor
    // - Fields: cardNumber (String), processingFee = 0.029 (2.9%)
    // - processPayment returns true if amount > 0 and cardNumber is not blank
    // - getFee returns amount * processingFee
    // - getPaymentMethod returns "Credit Card"
    //
    // Class: BankTransferPayment implements PaymentProcessor
    // - Fields: accountNumber (String), flatFee = 2.50
    // - processPayment returns true if amount > 0
    // - getFee returns flatFee
    // - getPaymentMethod returns "Bank Transfer"
    //
    // Class: PayPalPayment implements PaymentProcessor
    // - Fields: email (String), processingFee = 0.034 (3.4%)
    // - processPayment returns true if email contains "@"
    // - getFee returns amount * processingFee
    // - getPaymentMethod returns "PayPal"

    // TODO 2: Create a notification system using polymorphism
    // Interface: NotificationService
    // - void send(String recipient, String message)
    // - String getChannel()
    //
    // Class: EmailNotification implements NotificationService
    // - Stores sent messages in a List<String> field
    // - send adds "Email to X: Y" to the list
    // - getChannel returns "Email"
    // - getSentMessages() returns the list
    //
    // Class: SMSNotification implements NotificationService
    // - Stores sent messages in a List<String> field
    // - send adds "SMS to X: Y" to the list
    // - getChannel returns "SMS"
    // - getSentMessages() returns the list

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        System.out.println("=== PolymorphismExercises Tests ===\n");

        // Test 1: Payment System
        total++;
        try {
            // Uncomment after implementing Payment classes
            // PaymentProcessor creditCard = new CreditCardPayment("4111-1111-1111-1111");
            // PaymentProcessor bankTransfer = new BankTransferPayment("ACC-9876");
            // PaymentProcessor paypal = new PayPalPayment("user@example.com");
            //
            // // Test polymorphic behavior
            // PaymentProcessor[] processors = {creditCard, bankTransfer, paypal};
            // double[] fees = new double[3];
            // for (int i = 0; i < processors.length; i++) {
            //     fees[i] = processors[i].getFee(100.0);
            // }
            //
            // if (creditCard.processPayment(50.0)
            //     && Math.abs(fees[0] - 2.9) < 0.01
            //     && Math.abs(fees[1] - 2.5) < 0.01
            //     && Math.abs(fees[2] - 3.4) < 0.01
            //     && "Credit Card".equals(creditCard.getPaymentMethod())
            //     && "Bank Transfer".equals(bankTransfer.getPaymentMethod())
            //     && "PayPal".equals(paypal.getPaymentMethod())) {
            //     System.out.println("Test 1 PASSED: Payment system");
            //     passed++;
            // } else {
            //     System.out.println("Test 1 FAILED: Payment system");
            // }
            System.out.println("Test 1 SKIPPED: Payment system - implement classes");
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: Payment system - " + e.getMessage());
        }

        // Test 2: Notification System
        total++;
        try {
            // Uncomment after implementing Notification classes
            // EmailNotification email = new EmailNotification();
            // SMSNotification sms = new SMSNotification();
            //
            // // Test polymorphic usage
            // NotificationService[] services = {email, sms};
            // for (NotificationService service : services) {
            //     service.send("user@test.com", "Hello!");
            // }
            //
            // if (email.getSentMessages().size() == 1
            //     && email.getSentMessages().get(0).contains("user@test.com")
            //     && sms.getSentMessages().size() == 1
            //     && "Email".equals(email.getChannel())
            //     && "SMS".equals(sms.getChannel())) {
            //     System.out.println("Test 2 PASSED: Notification system");
            //     passed++;
            // } else {
            //     System.out.println("Test 2 FAILED: Notification system");
            // }
            System.out.println("Test 2 SKIPPED: Notification system - implement classes");
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: Notification system - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
        System.out.println("Note: Uncomment the test code above after implementing the classes.");
    }
}
