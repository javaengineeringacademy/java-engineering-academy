package academy.javaengineering.oop.dependencyinjection;

/**
 * SmsNotifier - Concrete implementation of Notifier interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SmsNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("  [SMS] Sending: " + message);
    }
}