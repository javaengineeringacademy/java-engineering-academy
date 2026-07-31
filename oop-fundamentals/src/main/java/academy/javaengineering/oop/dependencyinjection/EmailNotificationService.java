package academy.javaengineering.oop.dependencyinjection;

/**
 * EmailNotificationService - Concrete implementation of NotificationService.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class EmailNotificationService implements NotificationService {

    @Override
    public void notify(String message) {
        System.out.println("  [EMAIL] " + message);
    }
}