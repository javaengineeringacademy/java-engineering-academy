package academy.javaengineering.oop.dependencyinjection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for dependency injection demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class DependencyInjectionTest {

    @Test
    void shouldCreateLooselyCoupledService() {
        Notifier notifier = new EmailNotifier();
        Repository repository = new DatabaseRepository();
        
        LooselyCoupledOrderService service = 
            new LooselyCoupledOrderService(notifier, repository);
        
        assertNotNull(service);
    }

    @Test
    void shouldSwapImplementations() {
        // Email implementation
        LooselyCoupledOrderService emailService = 
            new LooselyCoupledOrderService(new EmailNotifier(), new DatabaseRepository());
        
        // SMS implementation
        LooselyCoupledOrderService smsService = 
            new LooselyCoupledOrderService(new SmsNotifier(), new FileRepository());
        
        // Both work without changing OrderService code
        assertDoesNotThrow(() -> emailService.placeOrder("Test", 1));
        assertDoesNotThrow(() -> smsService.placeOrder("Test", 1));
    }

    @Test
    void shouldProcessOrderWithInjection() {
        NotificationService notifier = new EmailNotificationService();
        OrderProcessor processor = new OrderProcessor(notifier);
        
        assertDoesNotThrow(() -> processor.processOrder("Laptop", 1));
    }

    @Test
    void shouldUseSetterInjection() {
        ReportGenerator report = new ReportGenerator();
        report.setFormatter(new HtmlFormatter());
        
        assertDoesNotThrow(() -> report.generate("Test Data"));
    }

    @Test
    void shouldUseMethodInjection() {
        DataProcessor processor = new DataProcessor();
        
        assertDoesNotThrow(() -> processor.processData(new JsonTransformer(), "data"));
        assertDoesNotThrow(() -> processor.processData(new XmlTransformer(), "data"));
    }

    @Test
    void shouldUseServiceContainer() {
        ServiceContainer container = new ServiceContainer();
        UserService userService = container.getService(UserService.class);
        
        assertNotNull(userService);
        assertDoesNotThrow(() -> userService.createUser("Test"));
    }

    @Test
    void shouldThrowForUnregisteredService() {
        ServiceContainer container = new ServiceContainer();
        
        assertThrows(IllegalArgumentException.class, 
            () -> container.getService(Repository.class));
    }
}