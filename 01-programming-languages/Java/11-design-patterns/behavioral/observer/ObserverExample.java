package academy.javaengineering.patterns.behavioral.observer;

/**
 * Real-world example demonstrating the Observer pattern.
 * Shows a news agency publishing news to multiple readers.
 */
public class ObserverExample {

    public static void main(String[] args) {
        NewsAgency agency = new NewsAgency();

        NewsReader alice = new NewsReader("Alice");
        NewsReader bob = new NewsReader("Bob");
        NewsReader charlie = new NewsReader("Charlie");

        agency.attach(alice);
        agency.attach(bob);
        agency.attach(charlie);

        System.out.println("=== First News Broadcast ===");
        agency.publishNews("Java 21 released with new features!");

        System.out.println("\n=== Detaching Bob ===");
        agency.detach(bob);

        System.out.println("\n=== Second News Broadcast ===");
        agency.publishNews("Spring Boot 3.2 announced!");

        System.out.println("\n=== Current subscribers: Alice, Charlie ===");
    }
}
