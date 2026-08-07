import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Main server class that accepts TCP connections and manages chat rooms.
 * Uses a thread pool to handle multiple clients concurrently.
 */
public class ChatServer {
    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());
    private final int port;
    private final ExecutorService threadPool;
    private final Map<String, ChatRoom> chatRooms;
    private final Map<String, ClientHandler> activeClients;
    private volatile boolean running;

    /**
     * Creates a ChatServer listening on the specified port.
     * @param port the TCP port to listen on
     */
    public ChatServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(50);
        this.chatRooms = new ConcurrentHashMap<>();
        this.activeClients = new ConcurrentHashMap<>();
        this.running = false;
    }

    /**
     * Starts the server, accepting incoming connections.
     * Creates a default "General" chat room on startup.
     */
    public void start() {
        running = true;
        chatRooms.put("General", new ChatRoom("General"));
        logger.info("ChatServer started on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Listening for connections on port " + port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                logger.info("New connection from: " + clientSocket.getInetAddress());
                ClientHandler handler = new ClientHandler(clientSocket, this);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            logger.severe("Server error: " + e.getMessage());
        }
    }

    /**
     * Registers a client with the server.
     * @param username the client's username
     * @param handler the ClientHandler for this client
     */
    public void registerClient(String username, ClientHandler handler) {
        activeClients.put(username, handler);
        logger.info("Client registered: " + username);
    }

    /**
     * Removes a client from the server.
     * @param username the client's username
     */
    public void removeClient(String username) {
        activeClients.remove(username);
        // Remove from all rooms
        for (ChatRoom room : chatRooms.values()) {
            room.removeParticipant(username);
        }
        logger.info("Client removed: " + username);
    }

    /**
     * Creates a new chat room.
     * @param roomName name of the room
     * @return true if room was created, false if it already exists
     */
    public boolean createRoom(String roomName) {
        if (chatRooms.containsKey(roomName)) {
            return false;
        }
        chatRooms.put(roomName, new ChatRoom(roomName));
        logger.info("Room created: " + roomName);
        return true;
    }

    /**
     * Gets an existing chat room by name.
     * @param roomName name of the room
     * @return the ChatRoom or null if not found
     */
    public ChatRoom getRoom(String roomName) {
        return chatRooms.get(roomName);
    }

    /**
     * Sends a private message to a specific user.
     * @param sender the sender's username
     * @param recipient the recipient's username
     * @param content the message content
     */
    public void sendPrivateMessage(String sender, String recipient, String content) {
        ClientHandler recipientHandler = activeClients.get(recipient);
        if (recipientHandler != null) {
            Message msg = new Message(sender, recipient, content, Message.Type.PRIVATE);
            recipientHandler.sendMessage(msg);
        }
    }

    /**
     * Returns the active client handlers map.
     * @return map of usernames to ClientHandlers
     */
    public Map<String, ClientHandler> getActiveClients() {
        return activeClients;
    }

    /**
     * Stops the server and shuts down the thread pool.
     */
    public void stop() {
        running = false;
        threadPool.shutdown();
        logger.info("Server stopped");
    }

    /**
     * Main entry point for the chat server.
     * @param args command line arguments (optional port number)
     */
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8888;
        ChatServer server = new ChatServer(port);
        server.start();
    }
}
