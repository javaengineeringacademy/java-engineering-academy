package academy.javaengineering.testing;

/**
 * Mockito Advanced - Spy, Mock Void Methods, BDD Style.
 */
public class MockitoAdvancedExample {

    public interface NotificationService {
        void sendNotification(String message);
        boolean isNotificationSent();
    }

    public interface EmailValidator {
        boolean isValid(String email);
    }

    private final NotificationService notificationService;
    private final EmailValidator emailValidator;

    public MockitoAdvancedExample(NotificationService notificationService, EmailValidator emailValidator) {
        this.notificationService = notificationService;
        this.emailValidator = emailValidator;
    }

    public boolean processOrder(String orderId, String customerEmail) {
        if (!emailValidator.isValid(customerEmail)) {
            return false;
        }
        notificationService.sendNotification("Order " + orderId + " confirmed");
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Mockito Advanced - See tests for spy and BDD examples");
    }
}
