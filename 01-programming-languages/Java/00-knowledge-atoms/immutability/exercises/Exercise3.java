import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Exercise3 {
    public static void main(String[] args) {
        List<String> mutableEvents = new ArrayList<>(List.of("login", "view_page"));
        EventLog log = new EventLog(mutableEvents);

        System.out.println("Original log: " + log.getEvents());

        // Modifying source does not affect the immutable log
        mutableEvents.add("logout");
        System.out.println("Source after mutation: " + mutableEvents);
        System.out.println("Log unchanged: " + log.getEvents());

        // Adding event returns a new log
        EventLog updatedLog = log.addEvent("click_button");
        System.out.println("Updated log: " + updatedLog.getEvents());
        System.out.println("Original log: " + log.getEvents());
    }
}

/*
 * TODO: Implement the immutable EventLog class below.
 *
 * Requirements:
 * - Constructor takes a mutable List<String> and makes a defensive copy
 * - getEvents() returns an unmodifiable view of the events
 * - addEvent() returns a NEW EventLog with the additional event
 * - No modifications to internal state after construction
 */
final class EventLog {
    private final List<String> events;

    // TODO: Constructor (defensive copy)

    // TODO: getEvents (unmodifiable view)

    // TODO: addEvent method

    // TODO: toString
}
