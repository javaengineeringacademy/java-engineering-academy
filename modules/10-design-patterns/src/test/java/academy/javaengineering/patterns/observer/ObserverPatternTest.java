package academy.javaengineering.patterns.observer;

import academy.javaengineering.patterns.observer.ObserverExample.Subject;
import academy.javaengineering.patterns.observer.ObserverExample.Observer;
import academy.javaengineering.patterns.observer.ObserverExample.ConcreteObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverPatternTest {

    private Subject subject;

    @BeforeEach
    void setUp() {
        subject = new Subject();
    }

    @Test
    @DisplayName("Should notify registered observer")
    void shouldNotifyRegisteredObserver() {
        List<String> events = new ArrayList<>();
        Observer observer = events::add;
        subject.register(observer);

        subject.setState("test-event");
        assertEquals(1, events.size());
        assertEquals("test-event", events.get(0));
    }

    @Test
    @DisplayName("Should notify all registered observers")
    void shouldNotifyAllObservers() {
        List<String> events1 = new ArrayList<>();
        List<String> events2 = new ArrayList<>();
        subject.register(events1::add);
        subject.register(events2::add);

        subject.setState("event");
        assertEquals(1, events1.size());
        assertEquals(1, events2.size());
    }

    @Test
    @DisplayName("Should not notify unregistered observer")
    void shouldNotNotifyUnregisteredObserver() {
        List<String> events = new ArrayList<>();
        Observer observer = events::add;
        subject.register(observer);
        subject.unregister(observer);

        subject.setState("event");
        assertTrue(events.isEmpty(), "Unregistered observer should not receive events");
    }

    @Test
    @DisplayName("Should handle no observers gracefully")
    void shouldHandleNoObservers() {
        assertDoesNotThrow(() -> subject.setState("event"),
                "Setting state with no observers should not throw");
    }

    @Test
    @DisplayName("Should pass correct state to observers")
    void shouldPassCorrectState() {
        List<String> received = new ArrayList<>();
        subject.register(received::add);

        subject.setState("first");
        subject.setState("second");

        assertEquals(List.of("first", "second"), received);
    }

    @Test
    @DisplayName("Should notify with ConcreteObserver without error")
    void shouldWorkWithConcreteObserver() {
        ConcreteObserver observer = new ConcreteObserver("TestObserver");
        subject.register(observer);
        assertDoesNotThrow(() -> subject.setState("test-event"));
    }

    @Test
    @DisplayName("Should support multiple state changes")
    void shouldSupportMultipleStateChanges() {
        List<String> events = new ArrayList<>();
        subject.register(events::add);

        for (int i = 0; i < 10; i++) {
            subject.setState("event-" + i);
        }

        assertEquals(10, events.size());
        assertEquals("event-9", events.get(9));
    }

    @Test
    @DisplayName("Should only notify observers registered at time of notification")
    void shouldNotifyOnlyCurrentObservers() {
        List<String> earlyEvents = new ArrayList<>();
        List<String> lateEvents = new ArrayList<>();

        subject.register(earlyEvents::add);
        subject.setState("first");

        subject.register(lateEvents::add);
        subject.setState("second");

        assertEquals(2, earlyEvents.size(), "Early observer should see both events");
        assertEquals(1, lateEvents.size(), "Late observer should see only second event");
    }

    @Test
    @DisplayName("Unregistering non-registered observer should not affect others")
    void shouldHandleUnregisteringNonRegistered() {
        List<String> events = new ArrayList<>();
        Observer registered = events::add;
        Observer notRegistered = e -> {};

        subject.register(registered);
        subject.unregister(notRegistered);

        subject.setState("event");
        assertEquals(1, events.size());
    }
}
