package academy.javaengineering.patterns.behavioral.mediator;

/**
 * Real-world example demonstrating the Mediator pattern.
 * Shows users communicating through a chat room mediator.
 */
public class MediatorExample {

    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom();

        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        chatRoom.register(alice);
        chatRoom.register(bob);
        chatRoom.register(charlie);

        System.out.println("\n=== Chat Messages ===");
        alice.send("Hello everyone!");
        System.out.println();
        bob.send("Hi Alice!");
        System.out.println();
        charlie.send("Good morning!");
    }
}
