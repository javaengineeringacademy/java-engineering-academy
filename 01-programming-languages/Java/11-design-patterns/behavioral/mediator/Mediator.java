package academy.javaengineering.patterns.behavioral.mediator;

/**
 * Mediator interface for coordinating communication between objects.
 * Defines the contract for how colleagues interact through the mediator.
 */
public interface Mediator {

    /**
     * Register a colleague with the mediator.
     *
     * @param colleague the colleague to register
     */
    void register(User colleague);

    /**
     * Send a message from one user to others.
     *
     * @param message the message to send
     * @param sender  the user sending the message
     */
    void sendMessage(String message, User sender);
}
