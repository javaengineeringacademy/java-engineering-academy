package academy.javaengineering.patterns.mediator;

import java.util.ArrayList;
import java.util.List;

// Mediator Interface
interface ChatMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

// Colleague
abstract class User {
    protected ChatMediator mediator;
    protected String name;
    
    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }
    
    public abstract void send(String message);
    public abstract void receive(String message, String from);
}

// Concrete Mediator
class ChatRoom implements ChatMediator {
    private final List<User> users = new ArrayList<>();
    
    @Override
    public void addUser(User user) {
        users.add(user);
        System.out.println(user.name + " joined the chat");
    }
    
    @Override
    public void sendMessage(String message, User sender) {
        for (User user : users) {
            if (user != sender) {
                user.receive(message, sender.name);
            }
        }
    }
}

// Concrete Colleagues
class ChatUser extends User {
    public ChatUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }
    
    @Override
    public void send(String message) {
        System.out.println(this.name + " sends: " + message);
        mediator.sendMessage(message, this);
    }
    
    @Override
    public void receive(String message, String from) {
        System.out.println(this.name + " receives from " + from + ": " + message);
    }
}

// Private Chat Mediator
class PrivateChatMediator implements ChatMediator {
    private User user1;
    private User user2;
    
    public void setUsers(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
    
    @Override
    public void sendMessage(String message, User sender) {
        if (sender == user1) {
            user2.receive(message, user1.name);
        } else {
            user1.receive(message, user2.name);
        }
    }
    
    @Override
    public void addUser(User user) {
        if (user1 == null) user1 = user;
        else user2 = user;
    }
}

public class MediatorExample {
    public static void main(String[] args) {
        System.out.println("=== Mediator Pattern ===\n");
        
        System.out.println("--- Group Chat ---");
        ChatMediator chatRoom = new ChatRoom();
        
        User alice = new ChatUser(chatRoom, "Alice");
        User bob = new ChatUser(chatRoom, "Bob");
        User charlie = new ChatUser(chatRoom, "Charlie");
        
        chatRoom.addUser(alice);
        chatRoom.addUser(bob);
        chatRoom.addUser(charlie);
        
        System.out.println();
        alice.send("Hello everyone!");
        System.out.println();
        bob.send("Hi Alice!");
        
        System.out.println("\n--- Private Chat ---");
        PrivateChatMediator privateChat = new PrivateChatMediator();
        User dave = new ChatUser(privateChat, "Dave");
        User eve = new ChatUser(privateChat, "Eve");
        privateChat.setUsers(dave, eve);
        
        System.out.println();
        dave.send("Hey Eve, this is private!");
    }
}
