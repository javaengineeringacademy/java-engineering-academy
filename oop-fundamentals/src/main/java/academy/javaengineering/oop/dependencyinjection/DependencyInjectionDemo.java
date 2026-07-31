package academy.javaengineering.oop.dependencyinjection;

/**
 * DependencyInjectionDemo - Demonstrates dependency injection patterns.
 * 
 * <p><b>Dependency Injection (DI)</b> inverts the control of object creation:
 * <ul>
 *   <li><b>Without DI:</b> Class creates its own dependencies</li>
 *   <li><b>With DI:</b> Dependencies are provided from outside</li>
 * </ul>
 * 
 * <p><b>Types of DI:</b>
 * <ul>
 *   <li><b>Constructor Injection:</b> Dependencies via constructor (most common)</li>
 *   <li><b>Setter Injection:</b> Dependencies via setter methods</li>
 *   <li><b>Field Injection:</b> Dependencies injected directly into fields</li>
 * </ul>
 * 
 * <p><b>Benefits:</b>
 * <ul>
 *   <li>Loose coupling between classes</li>
 *   <li>Easier testing (can inject mocks)</li>
 *   <li>Flexibility to change implementations</li>
 *   <li>Single Responsibility Principle adherence</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class DependencyInjectionDemo {

    private DependencyInjectionDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Dependency Injection Demo ===\n");

        // Without DI - tightly coupled
        System.out.println("--- Without DI (Tightly Coupled) ---");
        TightCoupledOrderService tightService = new TightCoupledOrderService();
        tightService.placeOrder("Laptop", 1);

        // With DI - loosely coupled
        System.out.println("\n--- With DI (Loosely Coupled) ---");
        EmailNotifier emailNotifier = new EmailNotifier();
        DatabaseRepository repository = new DatabaseRepository();
        
        LooselyCoupledOrderService looseService = 
            new LooselyCoupledOrderService(emailNotifier, repository);
        looseService.placeOrder("Laptop", 1);

        // Swapping implementations easily
        System.out.println("\n--- Swapping Implementations ---");
        SmsNotifier smsNotifier = new SmsNotifier();
        FileRepository fileRepo = new FileRepository();
        
        LooselyCoupledOrderService smsService = 
            new LooselyCoupledOrderService(smsNotifier, fileRepo);
        smsService.placeOrder("Phone", 2);

        // Constructor injection
        System.out.println("\n--- Constructor Injection ---");
        NotificationService emailService = new EmailNotificationService();
        OrderProcessor processor = new OrderProcessor(emailService);
        processor.processOrder("Tablet", 3);

        // Setter injection
        System.out.println("\n--- Setter Injection ---");
        ReportGenerator report = new ReportGenerator();
        report.setFormatter(new HtmlFormatter());
        report.generate("Sales Report");
        report.setFormatter(new CsvFormatter());
        report.generate("Sales Report (CSV)");

        // Method injection
        System.out.println("\n--- Method Injection ---");
        DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.processData(new JsonTransformer(), "raw data");
        dataProcessor.processData(new XmlTransformer(), "raw data");

        // Simulating Spring-like DI
        System.out.println("\n--- Spring-like Container Simulation ---");
        ServiceContainer container = new ServiceContainer();
        UserService userService = container.getService(UserService.class);
        userService.createUser("Alice");
    }
}