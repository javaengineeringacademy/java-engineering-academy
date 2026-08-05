package academy.javaengineering.patterns.behavioral.chain;

/**
 * Real-world example demonstrating the Chain of Responsibility pattern.
 * Shows a chain of handlers processing different request types.
 */
public class ChainExample {

    public static void main(String[] args) {
        Handler auth = new AuthHandler();
        Handler logging = new LoggingHandler();
        Handler validation = new ValidationHandler();

        auth.setNext(logging).setNext(validation);

        System.out.println("=== Processing Requests ===");
        auth.handle("auth:user123");
        auth.handle("log:system started");
        auth.handle("validate:email@test.com");
        auth.handle("unknown:request");
    }
}
