package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EventsTest {

    @Test
    void shouldNotifyAllListeners() {
        EventsExample.EventBus bus = new EventsExample.EventBus();
        List<String> received = new ArrayList<>();
        bus.subscribe(e -> received.add(e.getType()));
        bus.subscribe(e -> received.add(e.getData()));
        bus.publish(new EventsExample.Event("TEST", "data"));
        assertEquals(2, received.size());
    }
}
