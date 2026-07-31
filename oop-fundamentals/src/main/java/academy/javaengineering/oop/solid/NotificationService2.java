package academy.javaengineering.oop.solid;

/**
 * NotificationService2 - Single Responsibility: Only handles notifications.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class NotificationService2 {

    public void sendEmail(String to, String message) {
        System.out.println("  [NOTIFICATION] Email to " + to + ": " + message);
    }

    public void sendSms(String phone, String message) {
        System.out.println("  [NOTIFICATION] SMS to " + phone + ": " + message);
    }
}