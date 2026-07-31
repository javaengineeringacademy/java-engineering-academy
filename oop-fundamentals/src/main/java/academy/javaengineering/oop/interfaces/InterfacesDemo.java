package academy.javaengineering.oop.interfaces;

/**
 * InterfacesDemo - Demonstrates interface features in Java.
 * 
 * <p><b>Interface Features:</b>
 * <ul>
 *   <li>Abstract methods (public abstract by default)</li>
 *   <li>Default methods (Java 8+)</li>
 *   <li>Static methods (Java 8+)</li>
 *   <li>Private methods (Java 9+)</li>
 *   <li>Constants (public static final)</li>
 *   <li>Multiple inheritance of type</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class InterfacesDemo {

    private InterfacesDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Interfaces Demo ===\n");

        // Basic interface implementation
        System.out.println("--- Basic Interface ---");
        Flyable bird = new Bird("Robin");
        bird.fly();
        bird.land();
        System.out.println("Max altitude: " + bird.getMaxAltitude() + "m");

        // Multiple interface implementation
        System.out.println("\n--- Multiple Interfaces ---");
        SwimmableDuck duck = new SwimmableDuck("Donald");
        duck.fly();
        duck.swim();
        duck.quack();

        // Default methods
        System.out.println("\n--- Default Methods ---");
        Sortable list1 = new SimpleList();
        list1.add(5);
        list1.add(2);
        list1.add(8);
        list1.sort();
        System.out.println("Sorted: " + list1.getAll());

        // Static methods
        System.out.println("\n--- Static Methods ---");
        Greeting greeting = Greeting.createGreeting("Hello");
        System.out.println("Created: " + greeting.getMessage());

        // Functional interface (single abstract method)
        System.out.println("\n--- Functional Interface ---");
        Predicate<String> isLong = s -> s.length() > 5;
        System.out.println("'Hello': " + isLong.test("Hello"));
        System.out.println("'Hello World': " + isLong.test("Hello World"));

        // Interface inheritance
        System.out.println("\n--- Interface Inheritance ---");
        AdvancedLogger logger = new ConsoleLogger();
        logger.log("Info message");
        logger.logError("Error occurred");
        logger.logWithTimestamp("Timestamped message");

        // Constants in interfaces
        System.out.println("\n--- Interface Constants ---");
        System.out.println("Max retries: " + AppConfig.MAX_RETRIES);
        System.out.println("Timeout: " + AppConfig.TIMEOUT_MS + "ms");
    }
}