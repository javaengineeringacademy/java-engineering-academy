package academy.javaengineering.oop.solid;

/**
 * SOLIDDemo - Demonstrates the 5 SOLID principles of OOP design.
 * 
 * <p><b>SOLID Principles:</b>
 * <ul>
 *   <li><b>S</b> - Single Responsibility: Class has one reason to change</li>
 *   <li><b>O</b> - Open/Closed: Open for extension, closed for modification</li>
 *   <li><b>L</b> - Liskov Substitution: Substitutable for base type</li>
 *   <li><b>I</b> - Interface Segregation: Many specific interfaces</li>
 *   <li><b>D</b> - Dependency Inversion: Depend on abstractions</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class SOLIDDemo {

    private SOLIDDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== SOLID Principles Demo ===\n");

        // Single Responsibility
        System.out.println("--- Single Responsibility Principle ---");
        UserService2 userService = new UserService2();
        NotificationService2 notifService = new NotificationService2();
        ReportService reportService = new ReportService();
        
        userService.createUser("Alice");
        notifService.sendEmail("alice@example.com", "Welcome!");
        reportService.generateSalesReport();

        // Open/Closed
        System.out.println("\n--- Open/Closed Principle ---");
        ShapeAreaCalculator calculator = new ShapeAreaCalculator();
        calculator.calculate(new Circle3(5.0));
        calculator.calculate(new Rectangle3(4.0, 6.0));
        calculator.calculate(new Triangle3(3.0, 4.0, 5.0));
        // Can add new shapes without modifying ShapeAreaCalculator!

        // Liskov Substitution
        System.out.println("\n--- Liskov Substitution Principle ---");
        PaymentProcessor2 creditCard = new CreditCardProcessor2();
        PaymentProcessor2 paypal = new PayPalProcessor2();
        PaymentProcessor2 crypto = new CryptoProcessor2();
        
        processPayment2(creditCard, 100.0);
        processPayment2(paypal, 50.0);
        processPayment2(crypto, 200.0);

        // Interface Segregation
        System.out.println("\n--- Interface Segregation Principle ---");
        Printer printer = new SimplePrinter();
        Scanner scanner = new SimpleScanner();
        FaxMachine fax = new SimpleFaxMachine();
        
        printer.print("Document");
        scanner.scan("Document");
        fax.fax("Document");
        // Each implements only what it needs!

        // Dependency Inversion
        System.out.println("\n--- Dependency Inversion Principle ---");
        EmailService emailService = new EmailService();
        SmsService smsService = new SmsService();
        
        OrderService3 orderService1 = new OrderService3(emailService);
        OrderService3 orderService2 = new OrderService3(smsService);
        
        orderService1.placeOrder("Laptop");
        orderService2.placeOrder("Phone");

        // Summary
        System.out.println("\n=== SOLID Summary ===");
        System.out.println("S: One class = One job");
        System.out.println("O: Add features, don't modify existing code");
        System.out.println("L: Subclasses must be substitutable for parents");
        System.out.println("I: Many small interfaces > one fat interface");
        System.out.println("D: Depend on abstractions, not concretions");
    }

    static void processPayment2(PaymentProcessor2 processor, double amount) {
        System.out.println("  Processing $" + amount + " via " + processor.getPaymentMethod());
        processor.process(amount);
    }
}