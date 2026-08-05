package academy.javaengineering.patterns.behavioral.observer;

/**
 * Subject interface for managing observers and notifying them of changes.
 * Defines methods for attaching, detaching, and notifying observers.
 */
public interface Subject {

    /**
     * Attach an observer to receive notifications.
     *
     * @param observer the observer to attach
     */
    void attach(Observer observer);

    /**
     * Detach an observer from receiving notifications.
     *
     * @param observer the observer to detach
     */
    void detach(Observer observer);

    /**
     * Notify all attached observers of a change.
     *
     * @param message the message to send to observers
     */
    void notifyObservers(String message);
}
