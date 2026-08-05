package academy.javaengineering.patterns.behavioral.memento;

/**
 * Memento class for storing the state of the originator.
 * Provides a snapshot of the object's state at a point in time.
 */
public class Memento {

    private final String content;
    private final long timestamp;

    public Memento(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
