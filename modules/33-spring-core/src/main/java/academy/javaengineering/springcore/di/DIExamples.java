package academy.javaengineering.springcore.di;

/**
 * Demonstrates different types of Dependency Injection.
 */
public class DIExamples {

    // Interface for Injection
    public interface NotificationService {
        void sendNotification(String message);
    }

    // Implementation 1: Email
    public static class EmailNotificationService implements NotificationService {
        @Override
        public void sendNotification(String message) {
            System.out.println("Email: " + message);
        }
    }

    // Implementation 2: SMS
    public static class SmsNotificationService implements NotificationService {
        @Override
        public void sendNotification(String message) {
            System.out.println("SMS: " + message);
        }
    }

    // Implementation 3: Push
    public static class PushNotificationService implements NotificationService {
        @Override
        public void sendNotification(String message) {
            System.out.println("Push: " + message);
        }
    }

    // 1. Constructor Injection (Recommended)
    public static class OrderService {
        private final NotificationService notificationService;
        
        // Constructor injection - immutable, required dependencies
        public OrderService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }
        
        public void placeOrder(String orderId) {
            System.out.println("Order placed: " + orderId);
            notificationService.sendNotification("Order " + orderId + " confirmed!");
        }
    }

    // 2. Setter Injection (Optional dependencies)
    public static class UserService {
        private NotificationService notificationService;
        private String defaultRole;
        
        // Setter injection - optional dependencies
        public void setNotificationService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }
        
        public void setDefaultRole(String defaultRole) {
            this.defaultRole = defaultRole;
        }
        
        public void createUser(String username) {
            System.out.println("User created: " + username + " with role: " + defaultRole);
            if (notificationService != null) {
                notificationService.sendNotification("Welcome " + username + "!");
            }
        }
    }

    // 3. Interface Injection
    public interface ServiceLocator {
        void setServiceLocator(ServiceLocator locator);
    }

    public static class ServiceLocatorImpl implements ServiceLocator {
        @Override
        public void setServiceLocator(ServiceLocator locator) {
            System.out.println("Service locator set");
        }
    }

    // 4. Method Injection (Lookup method)
    public static abstract class MessageProcessor {
        // Abstract method - Spring will override this
        public abstract NotificationService createNotificationService();
        
        public void processMessage(String message) {
            NotificationService service = createNotificationService();
            service.sendNotification(message);
        }
    }
}
