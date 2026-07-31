package academy.javaengineering.oop.dependencyinjection;

/**
 * Notifier - Interface for notification strategy.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Notifier {

    void send(String message);
}