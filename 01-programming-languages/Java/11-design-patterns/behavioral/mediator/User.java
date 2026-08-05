package academy.javaengineering.patterns.behavioral.mediator;

/**
 * Colleague class - User in the chat room.
 * Communicates with other users through the mediator.
 */
public class User {

    private final String name;
    private Mediator mediator;

    public User(String name) {
        this.name = name;
    }

    public void send(String message) {
        System.out.println(name + " sends: " + message);
        mediator.sendMessage(message, this);
    }

    public void receive(String message, String senderName) {
        System.out.println(name + " received from " + senderName + ": " + message);
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public String getName() {
        return name;
    }

    public Mediator getMediator() {
        return mediator;
    }
}
