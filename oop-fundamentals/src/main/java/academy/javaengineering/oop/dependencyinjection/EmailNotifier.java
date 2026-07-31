package academy.javaengineering.oop.dependencyinjection;

/**
 * EmailNotifier - Concrete implementation of Notifier interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class EmailNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("  [EMAIL] Sending: " + message);
    }
}