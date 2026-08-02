package academy.javaengineering.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class ObserverExample {

    public interface Observer {
        void update(String event);
    }

    public static class Subject {
        private final List<Observer> observers = new ArrayList<>();
        private String state;

        public void register(Observer observer) {
            observers.add(observer);
        }

        public void unregister(Observer observer) {
            observers.remove(observer);
        }

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

    public static class ConcreteObserver implements Observer {
        private final String name;

        public ConcreteObserver(String name) {
            this.name = name;
        }

        @Override
        public void update(String event) {
            System.out.println(name + " received: " + event);
        }
    }

    public static void main(String[] args) {
        Subject subject = new Subject();
        subject.register(new ConcreteObserver("Observer 1"));
        subject.register(new ConcreteObserver("Observer 2"));

        subject.setState("Event occurred");
    }
}
