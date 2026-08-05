package academy.javaengineering.patterns.behavioral.chain;

/**
 * Handler interface for the Chain of Responsibility pattern.
 * Defines the contract for handling requests and passing them along.
 */
public interface Handler {

    /**
     * Set the next handler in the chain.
     *
     * @param next the next handler
     * @return the next handler for chaining
     */
    Handler setNext(Handler next);

    /**
     * Handle the request.
     *
     * @param request the request to handle
     * @return true if handled, false otherwise
     */
    boolean handle(String request);
}
