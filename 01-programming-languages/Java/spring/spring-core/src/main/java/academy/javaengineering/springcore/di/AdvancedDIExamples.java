package academy.javaengineering.springcore.di;

/**
 * Demonstrates Method Injection and Interface Injection patterns.
 */
public class AdvancedDIExamples {

    // 1. Method Injection - Using abstract methods
    public static abstract class AbstractNotificationSender {
        // Spring will override this method to provide the dependency
        public abstract DIExamples.NotificationService getNotificationService();
        
        public void send(String message) {
            getNotificationService().sendNotification(message);
        }
    }

    // 2. Interface Injection - Interface defines injection point
    public interface InjectDependency {
        void inject(DIExamples.NotificationService service);
    }

    public static class ReportingService implements InjectDependency {
        private DIExamples.NotificationService notificationService;
        
        @Override
        public void inject(DIExamples.NotificationService service) {
            this.notificationService = service;
        }
        
        public void generateReport() {
            System.out.println("Report generated");
            if (notificationService != null) {
                notificationService.sendNotification("Report is ready!");
            }
        }
    }

    // 3. Factory Pattern with DI
    public static class NotificationFactory {
        private final java.util.Map<String, DIExamples.NotificationService> services;
        
        public NotificationFactory(java.util.Map<String, DIExamples.NotificationService> services) {
            this.services = services;
        }
        
        public DIExamples.NotificationService getService(String type) {
            return services.get(type);
        }
    }

    // 4. Provider Pattern
    public interface Provider<T> {
        T get();
    }

    public static class NotificationProvider implements Provider<DIExamples.NotificationService> {
        private final DIExamples.NotificationService service;
        
        public NotificationProvider(DIExamples.NotificationService service) {
            this.service = service;
        }
        
        @Override
        public DIExamples.NotificationService get() {
            return service;
        }
    }
}
