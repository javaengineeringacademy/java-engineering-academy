package academy.javaengineering.patterns.behavioral.observer;

/**
 * Observer interface for receiving updates from subjects.
 * Defines the contract for objects that want to be notified
 * of changes in the subject they observe.
 */
public interface Observer {

    /**
     * Called when the subject's state changes.
     *
     * @param message the update message from the subject
     */
    void update(String message);
}
