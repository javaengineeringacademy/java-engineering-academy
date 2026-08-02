package academy.javaengineering.patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the Observer design pattern for event-driven communication.
 *
 * <p>The Observer pattern defines a one-to-many dependency between objects so that
 * when one object changes state, all its dependents are notified and updated
 * automatically.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Subject maintains list of observers</li>
 *   <li>Observers receive notifications on state changes</li>
 *   <li>Loose coupling between subject and observers</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ObserverExample {

    /**
     * Observer interface for receiving event notifications.
     */
    public interface Observer {
        /**
         * Called when the observed subject's state changes.
         *
         * @param event the new state or event description
         */
        void update(String event);
    }

    /**
     * Subject class that maintains and notifies observers.
     */
    public static class Subject {
        private final List<Observer> observers = new ArrayList<>();
        private String state;

        /**
         * Registers an observer to receive notifications.
         *
         * @param observer the observer to add
         */
        public void register(Observer observer) {
            observers.add(observer);
        }

        /**
         * Unregisters an observer from receiving notifications.
         *
         * @param observer the observer to remove
         */
        public void unregister(Observer observer) {
            observers.remove(observer);
        }

        /**
         * Sets the state and notifies all registered observers.
         *
         * @param state the new state
         */
        public void setState(String state) {
            this.state = state;
            notifyObservers();
        }

        private void notifyObservers() {
            for (Observer observer : observers) {
                observer.update(state);
            }
        }
    }

    /**
     * Concrete observer that prints received events.
     */
    public static class ConcreteObserver implements Observer {
        private final String name;

        /**
         * Creates a named observer.
         *
         * @param name the observer's identifier
         */
        public ConcreteObserver(String name) {
            this.name = name;
        }

        @Override
        public void update(String event) {
            System.out.println(name + " received: " + event);
        }
    }

    /**
     * Demonstrates observer pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Subject subject = new Subject();
        subject.register(new ConcreteObserver("Observer 1"));
        subject.register(new ConcreteObserver("Observer 2"));

        subject.setState("Event occurred");
    }
}
