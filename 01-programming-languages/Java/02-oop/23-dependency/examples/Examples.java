package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Dependency Injection Patterns ===\n");

        // WHY: DI decouples creation from usage. Enables testing, flexibility.
        // INTERNAL: Dependencies passed via constructor, setter, or interface
        // ENGINEERING: Constructor injection for required, setter for optional

        // Without DI - tightly coupled
        NotificationService tightCoupled = new NotificationService();
        tightCoupled.send("Hello");

        // With DI - loosely coupled
        MessageSender sender = new EmailSender();
        NotificationService looseCoupled = new NotificationService(sender);
        looseCoupled.send("Hello");

        // TRADE-OFF: DI frameworks vs manual injection
        // Manual: explicit, simple, no magic
        // Framework (Spring, Guice): powerful, configurable, adds complexity
    }
}

class NotificationService {
    private final MessageSender sender;

    // Constructor injection (preferred)
    NotificationService(MessageSender sender) {
        this.sender = sender;
    }

    // For demo: no-arg constructor (tight coupling)
    NotificationService() {
        this.sender = new EmailSender(); // Default
    }

    void send(String message) {
        sender.send(message);
    }
}

interface MessageSender {
    void send(String message);
}

class EmailSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
