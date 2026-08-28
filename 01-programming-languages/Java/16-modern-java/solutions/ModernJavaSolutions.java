package academy.javaengineering.modern;

import java.util.List;

/**
 * Solutions for modern Java practice exercises.
 */
public class ModernJavaSolutions {

    // Exercise 1: Book Record
    public record Book(String title, String author, double price, int year) {
        public boolean isRecent() {
            return year > 2020;
        }
    }

    // Exercise 2: Sealed Classes with Pattern Matching
    sealed interface Animal permits Dog, Cat, Bird {}
    record Dog(String name, String breed) implements Animal {}
    record Cat(String name, boolean indoor) implements Animal {}
    record Bird(String name, boolean canFly) implements Animal {}

    public static String describeAnimal(Animal animal) {
        return switch (animal) {
            case Dog d -> "Dog: " + d.name() + " (" + d.breed() + ")";
            case Cat c -> "Cat: " + c.name() + (c.indoor() ? " (indoor)" : " (outdoor)");
            case Bird b -> "Bird: " + b.name() + (b.canFly() ? " (can fly)" : " (flightless)");
        };
    }

    // Exercise 3: var and Text Blocks
    public static void varAndTextBlocks() {
        // var with complex types
        var numbers = List.of(1, 2, 3, 4, 5);
        var names = List.of("Alice", "Bob", "Charlie");

        // Text block for formatted report
        String report = """
                ================================
                |          REPORT              |
                ================================
                | Names: %s
                | Numbers: %s
                | Total items: %d
                ================================
                """.formatted(names, numbers, numbers.size());

        System.out.println(report);
    }

    // Exercise 4: Switch Expressions
    public static String getGradeDescription(char grade) {
        return switch (grade) {
            case 'A' -> "Excellent";
            case 'B' -> "Good";
            case 'C' -> "Average";
            case 'D' -> "Below Average";
            case 'F' -> "Failing";
            default -> "Invalid grade";
        };
    }

    // Exercise 5: Multi-catch and instanceof Pattern Matching
    public static int parseInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }

    public static String checkType(Object obj) {
        if (obj instanceof String s && !s.isEmpty()) {
            return "String with length: " + s.length();
        } else if (obj instanceof Integer i && i > 0) {
            return "Positive integer: " + i;
        } else if (obj instanceof List<?> list && !list.isEmpty()) {
            return "Non-empty list with " + list.size() + " elements";
        }
        return "Unknown or invalid object";
    }

    public static void main(String[] args) {
        // Test Exercise 1
        var book = new Book("Modern Java", "John Doe", 49.99, 2023);
        System.out.println("Book: " + book);
        System.out.println("Is recent: " + book.isRecent());

        // Test Exercise 2
        System.out.println("\n--- Exercise 2 ---");
        Animal dog = new Dog("Rex", "German Shepherd");
        Animal cat = new Cat("Whiskers", true);
        Animal bird = new Bird("Tweety", true);
        System.out.println(describeAnimal(dog));
        System.out.println(describeAnimal(cat));
        System.out.println(describeAnimal(bird));

        // Test Exercise 3
        System.out.println("\n--- Exercise 3 ---");
        varAndTextBlocks();

        // Test Exercise 4
        System.out.println("--- Exercise 4 ---");
        for (char grade : new char[]{'A', 'B', 'C', 'D', 'F'}) {
            System.out.println(grade + ": " + getGradeDescription(grade));
        }

        // Test Exercise 5
        System.out.println("\n--- Exercise 5 ---");
        System.out.println(parseInteger("123"));
        System.out.println(parseInteger("abc"));
        System.out.println(checkType("Hello"));
        System.out.println(checkType(42));
    }
}
