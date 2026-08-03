package academy.javaengineering.springcore.di;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dependency Injection Tests")
class DITest {

    @Test
    @DisplayName("Constructor injection should work")
    void testConstructorInjection() {
        var emailService = new DIExamples.EmailNotificationService();
        var orderService = new DIExamples.OrderService(emailService);
        
        assertDoesNotThrow(() -> orderService.placeOrder("ORD-001"));
    }

    @Test
    @DisplayName("Setter injection should work")
    void testSetterInjection() {
        var smsService = new DIExamples.SmsNotificationService();
        var userService = new DIExamples.UserService();
        
        userService.setNotificationService(smsService);
        userService.setDefaultRole("USER");
        
        assertDoesNotThrow(() -> userService.createUser("john"));
    }

    @Test
    @DisplayName("Interface injection should work")
    void testInterfaceInjection() {
        var pushService = new DIExamples.PushNotificationService();
        var reportingService = new AdvancedDIExamples.ReportingService();
        
        reportingService.inject(pushService);
        assertDoesNotThrow(() -> reportingService.generateReport());
    }

    @Test
    @DisplayName("Different notification services should work")
    void testNotificationServices() {
        var emailService = new DIExamples.EmailNotificationService();
        var smsService = new DIExamples.SmsNotificationService();
        var pushService = new DIExamples.PushNotificationService();
        
        assertDoesNotThrow(() -> emailService.sendNotification("Test"));
        assertDoesNotThrow(() -> smsService.sendNotification("Test"));
        assertDoesNotThrow(() -> pushService.sendNotification("Test"));
    }
}
