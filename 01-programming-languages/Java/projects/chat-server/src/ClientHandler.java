import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Handles communication with an individual client.
 * Reads messages from the client and routes them appropriately.
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private final Socket socket;
    private final ChatServer server;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private ChatRoom currentRoom;
    private boolean connected;

    /**
     * Creates a new ClientHandler for a client socket.
     * @param socket the client's socket connection
     * @param server reference to the main server
     */
    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        this.connected = false;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connected = true;

            // Authentication phase
            out.println("Welcome to the Chat Server!");
            out.println("Enter your username:");
            username = in.readLine();
            if (username == null || username.trim().isEmpty()) {
                disconnect();
                return;
            }
            username = username.trim();
            server.registerClient(username, this);

            // Join default room
            currentRoom = server.getRoom("General");
            currentRoom.addParticipant(username);
            broadcastToRoom(username + " has joined the chat", Message.Type.JOIN);

            // Message loop
            String inputLine;
            while (connected && (inputLine = in.readLine()) != null) {
                processMessage(inputLine);
            }
        } catch (IOException e) {
            logger.warning("Client connection error: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    /**
     * Processes an incoming message from the client.
     * Routes messages based on command prefixes.
     * @param input the raw message string
     */
    private void processMessage(String input) {
        if (input.startsWith("/")) {
            handleCommand(input);
        } else {
            // Regular chat message
            Message msg = new Message(username, currentRoom.getName(), input, Message.Type.CHAT);
            currentRoom.broadcast(msg, this);
        }
    }

    /**
     * Handles chat commands starting with '/'.
     * @param command the command string
     */
    private void handleCommand(String command) {
        String[] parts = command.split(" ", 3);
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "/join":
                if (parts.length > 1) {
                    joinRoom(parts[1]);
                } else {
                    sendMessage(new Message("System", username, "Usage: /join <room>", Message.Type.SYSTEM));
                }
                break;
            case "/leave":
                leaveRoom();
                break;
            case "/create":
                if (parts.length > 1) {
                    createRoom(parts[1]);
                }
                break;
            case "/whisper":
                if (parts.length >= 3) {
                    sendPrivateMessage(parts[1], parts[2]);
                }
                break;
            case "/quit":
                disconnect();
                break;
            default:
                sendMessage(new Message("System", username, "Unknown command: " + cmd, Message.Type.SYSTEM));
        }
    }

    /**
     * Joins a chat room by name.
     * @param roomName the room to join
     */
    private void joinRoom(String roomName) {
        if (currentRoom != null) {
            currentRoom.removeParticipant(username);
            broadcastToRoom(username + " left the room", Message.Type.LEAVE);
        }
        currentRoom = server.getRoom(roomName);
        if (currentRoom == null) {
            server.createRoom(roomName);
            currentRoom = server.getRoom(roomName);
        }
        currentRoom.addParticipant(username);
        broadcastToRoom(username + " has joined the room", Message.Type.JOIN);
        sendMessage(new Message("System", username, "Joined room: " + roomName, Message.Type.SYSTEM));
    }

    /**
     * Leaves the current room and returns to General.
     */
    private void leaveRoom() {
        if (currentRoom != null && !currentRoom.getName().equals("General")) {
            broadcastToRoom(username + " left the room", Message.Type.LEAVE);
            currentRoom.removeParticipant(username);
            currentRoom = server.getRoom("General");
            currentRoom.addParticipant(username);
            broadcastToRoom(username + " has joined the room", Message.Type.JOIN);
        }
    }

    /**
     * Creates a new chat room.
     * @param roomName name of the room to create
     */
    private void createRoom(String roomName) {
        if (server.createRoom(roomName)) {
            sendMessage(new Message("System", username, "Room created: " + roomName, Message.Type.SYSTEM));
        } else {
            sendMessage(new Message("System", username, "Room already exists: " + roomName, Message.Type.SYSTEM));
        }
    }

    /**
     * Sends a private message to another user.
     * @param recipient target username
     * @param content message content
     */
    private void sendPrivateMessage(String recipient, String content) {
        server.sendPrivateMessage(username, recipient, content);
        sendMessage(new Message(username, recipient, content, Message.Type.PRIVATE));
    }

    /**
     * Broadcasts a message to all participants in the current room.
     * @param content message content
     * @param type message type
     */
    private void broadcastToRoom(String content, Message.Type type) {
        if (currentRoom != null) {
            Message msg = new Message(username, currentRoom.getName(), content, type);
            currentRoom.broadcast(msg, this);
        }
    }

    /**
     * Sends a message to this client.
     * @param message the message to send
     */
    public void sendMessage(Message message) {
        if (out != null) {
            out.println(message.getFormatted());
        }
    }

    /**
     * Disconnects the client and cleans up resources.
     */
    public void disconnect() {
        connected = false;
        if (currentRoom != null && username != null) {
            broadcastToRoom(username + " has disconnected", Message.Type.LEAVE);
            currentRoom.removeParticipant(username);
        }
        if (username != null) {
            server.removeClient(username);
        }
        try {
            socket.close();
        } catch (IOException e) {
            logger.warning("Error closing socket: " + e.getMessage());
        }
    }
}
