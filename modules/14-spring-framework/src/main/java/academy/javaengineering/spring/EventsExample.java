package academy.javaengineering.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Events - ApplicationEvent, @EventListener.
 */
public class EventsExample {

    public static class Event {
        private final String type;
        private final String data;

        public Event(String type, String data) {
            this.type = type;
            this.data = data;
        }

        public String getType() { return type; }
        public String getData() { return data; }
    }

    public interface EventListener {
        void onEvent(Event event);
    }

    public static class EventBus {
        private final List<EventListener> listeners = new ArrayList<>();

        public void subscribe(EventListener listener) { listeners.add(listener); }

        public void publish(Event event) {
            listeners.forEach(l -> l.onEvent(event));
        }
    }

    public static void main(String[] args) {
        EventBus bus = new EventBus();
        bus.subscribe(e -> System.out.println("Listener 1: " + e.getType()));
        bus.subscribe(e -> System.out.println("Listener 2: " + e.getData()));
        bus.publish(new Event("USER_CREATED", "John"));
    }
}
