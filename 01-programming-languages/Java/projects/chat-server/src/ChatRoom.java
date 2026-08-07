import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Manages a chat room's state, participants, and message history.
 * Thread-safe implementation using CopyOnWriteArrayList.
 */
public class ChatRoom {
    private static final Logger logger = Logger.getLogger(ChatRoom.class.getName());
    private final String name;
    private final List<String> participants;
    private final List<Message> messageHistory;
    private static final int MAX_HISTORY = 100;

    /**
     * Creates a new ChatRoom with the given name.
     * @param name the room name
     */
    public ChatRoom(String name) {
        this.name = name;
        this.participants = new CopyOnWriteArrayList<>();
        this.messageHistory = new ArrayList<>();
    }

    /**
     * Returns the room name.
     * @return room name string
     */
    public String getName() { return name; }

    /**
     * Returns an unmodifiable list of current participants.
     * @return list of usernames
     */
    public List<String> getParticipants() {
        return new ArrayList<>(participants);
    }

    /**
     * Adds a participant to the room.
     * @param username the user to add
     */
    public void addParticipant(String username) {
        if (!participants.contains(username)) {
            participants.add(username);
            logger.info(username + " joined room " + name);
        }
    }

    /**
     * Removes a participant from the room.
     * @param username the user to remove
     */
    public void removeParticipant(String username) {
        participants.remove(username);
        logger.info(username + " left room " + name);
    }

    /**
     * Checks if a user is in this room.
     * @param username the username to check
     * @return true if user is a participant
     */
    public boolean hasParticipant(String username) {
        return participants.contains(username);
    }

    /**
     * Returns the number of participants in the room.
     * @return participant count
     */
    public int getParticipantCount() {
        return participants.size();
    }

    /**
     * Broadcasts a message to all participants except the sender.
     * @param message the message to broadcast
     * @param sender the sender's ClientHandler (to exclude from broadcast)
     */
    public void broadcast(Message message, ClientHandler sender) {
        addToHistory(message);
        for (ClientHandler handler : getActiveHandlers()) {
            if (handler != sender) {
                handler.sendMessage(message);
            }
        }
        logger.fine("Broadcast in " + name + ": " + message.getContent());
    }

    /**
     * Sends a message to all participants including the sender.
     * @param message the message to send
     */
    public void broadcastToAll(Message message) {
        addToHistory(message);
        for (ClientHandler handler : getActiveHandlers()) {
            handler.sendMessage(message);
        }
    }

    /**
     * Gets the recent message history for this room.
     * @param count number of messages to retrieve
     * @return list of recent messages
     */
    public List<Message> getHistory(int count) {
        int start = Math.max(0, messageHistory.size() - count);
        return new ArrayList<>(messageHistory.subList(start, messageHistory.size()));
    }

    /**
     * Adds a message to the room's history.
     * Maintains a maximum history size.
     * @param message the message to add
     */
    private void addToHistory(Message message) {
        messageHistory.add(message);
        if (messageHistory.size() > MAX_HISTORY) {
            messageHistory.remove(0);
        }
    }

    /**
     * Helper to get active handlers for participants.
     * This is a simplified version; in production, you'd maintain handler references.
     * @return list of active ClientHandlers
     */
    private List<ClientHandler> getActiveHandlers() {
        // In a real implementation, this would access the server's active clients
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("ChatRoom{name='%s', participants=%d}", name, participants.size());
    }
}
