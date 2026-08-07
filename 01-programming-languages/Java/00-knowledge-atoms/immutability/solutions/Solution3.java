import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Solution3 {
    public static void main(String[] args) {
        List<String> mutableEvents = new ArrayList<>(List.of("login", "view_page"));
        EventLog log = new EventLog(mutableEvents);

        System.out.println("Original log: " + log.getEvents());

        mutableEvents.add("logout");
        System.out.println("Source after mutation: " + mutableEvents);
        System.out.println("Log unchanged: " + log.getEvents());

        EventLog updatedLog = log.addEvent("click_button");
        System.out.println("Updated log: " + updatedLog.getEvents());
        System.out.println("Original log: " + log.getEvents());
    }
}

final class EventLog {
    private final List<String> events;

    public EventLog(List<String> events) {
        this.events = new ArrayList<>(events); // Defensive copy
    }

    public List<String> getEvents() {
        return Collections.unmodifiableList(events); // Unmodifiable view
    }

    public EventLog addEvent(String event) {
        List<String> newEvents = new ArrayList<>(this.events);
        newEvents.add(event);
        return new EventLog(newEvents);
    }

    @Override
    public String toString() {
        return "EventLog{events=" + events + "}";
    }
}
