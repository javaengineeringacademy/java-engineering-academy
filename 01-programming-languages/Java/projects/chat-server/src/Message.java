import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a chat message with metadata including type, sender, and timestamp.
 * Immutable data class for message passing between components.
 */
public class Message {
    /**
     * Enum representing the different types of messages.
     */
    public enum Type {
        CHAT,       // Regular chat message
        JOIN,       // User joined notification
        LEAVE,      // User left notification
        PRIVATE,    // Private/direct message
        SYSTEM      // System message
    }

    private final String sender;
    private final String recipient;
    private final String content;
    private final Type type;
    private final LocalDateTime timestamp;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Creates a new Message.
     * @param sender the message sender
     * @param recipient the message recipient or room name
     * @param content the message content
     * @param type the message type
     */
    public Message(String sender, String recipient, String content, Type type) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Returns the sender's username.
     * @return sender name
     */
    public String getSender() { return sender; }

    /**
     * Returns the recipient or room name.
     * @return recipient name
     */
    public String getRecipient() { return recipient; }

    /**
     * Returns the message content.
     * @return content string
     */
    public String getContent() { return content; }

    /**
     * Returns the message type.
     * @return Type enum value
     */
    public Type getType() { return type; }

    /**
     * Returns the timestamp when the message was created.
     * @return LocalDateTime of creation
     */
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Formats the message for display with timestamp and type indicator.
     * @return formatted message string
     */
    public String getFormatted() {
        String time = timestamp.format(formatter);
        switch (type) {
            case CHAT:
                return String.format("[%s] %s: %s", time, sender, content);
            case PRIVATE:
                return String.format("[%s] [PM] %s -> %s: %s", time, sender, recipient, content);
            case JOIN:
                return String.format("[%s] >> %s %s the room", time, content.split(" ")[0], "joined");
            case LEAVE:
                return String.format("[%s] << %s %s the room", time, content.split(" ")[0], "left");
            case SYSTEM:
                return String.format("[%s] [SYSTEM] %s", time, content);
            default:
                return String.format("[%s] %s", time, content);
        }
    }

    /**
     * Returns whether this is a system message.
     * @return true if type is SYSTEM
     */
    public boolean isSystem() { return type == Type.SYSTEM; }

    /**
     * Returns whether this is a private message.
     * @return true if type is PRIVATE
     */
    public boolean isPrivate() { return type == Type.PRIVATE; }

    @Override
    public String toString() {
        return getFormatted();
    }
}
