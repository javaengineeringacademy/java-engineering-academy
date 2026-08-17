package academy.javaengineering.knowledgeatoms.immutability;

import java.util.*;

public class ImmutabilityInternals {

    public static void main(String[] args) {
        System.out.println("=== Immutability Internals ===\n");

        // 1. Final field semantics
        System.out.println("--- Final Field Semantics ---");
        System.out.println("JMM guarantees: once constructor completes,");
        System.out.println("all final fields are visible to all threads");
        System.out.println("No synchronization needed for reading final fields");

        // 2. Defensive copying
        System.out.println("\n--- Defensive Copying ---");
        demonstrateDefensiveCopy();

        // 3. Record immutability
        System.out.println("\n--- Record Immutability ---");
        demonstrateRecords();

        // 4. String immutability
        System.out.println("\n--- String Immutability ---");
        demonstrateStringImmutability();
    }

    private static void demonstrateDefensiveCopy() {
        Date mutableDate = new Date();
        SecureDate secureDate = new SecureDate(mutableDate);

        // Mutate original
        mutableDate.setTime(0);
        System.out.println("Original date changed to epoch");

        // SecureDate protects against external mutation
        Date internal = secureDate.getDate();
        internal.setTime(999999999L);
        System.out.println("Internal copy mutation does not affect SecureDate");

        Date retrieved = secureDate.getDate();
        System.out.println("Retrieved date: " + retrieved);
        System.out.println("Original reference: " + mutableDate);
        System.out.println("They are different objects: " + (retrieved != mutableDate));
    }

    private static void demonstrateRecords() {
        record Point(int x, int y) {}
        record Person(String name, int age, List<String> hobbies) {
            public Person {
                hobbies = List.copyOf(hobbies); // defensive copy
            }
        }

        Point p = new Point(1, 2);
        System.out.println("Record: " + p);
        System.out.println("x: " + p.x() + ", y: " + p.y());

        Person person = new Person("Alice", 30, List.of("reading", "coding"));
        System.out.println("Person: " + person);
        System.out.println("Hobbies unmodifiable: " + person.hobbies().getClass().getSimpleName());
    }

    private static void demonstrateStringImmutability() {
        String s1 = "Hello";
        String s2 = s1.concat(" World");
        System.out.println("s1 after concat: " + s1 + " (unchanged)");
        System.out.println("s2 after concat: " + s2 + " (new object)");

        // String pool
        String a = "Hello";
        String b = "Hello";
        System.out.println("String pool: a == b is " + (a == b) + " (same object in pool)");

        // StringBuilder for mutation
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" World");
        System.out.println("StringBuilder: " + sb + " (mutates in place)");
    }

    // Immutable class with defensive copying
    static final class SecureDate {
        private final Date date;

        SecureDate(Date date) {
            this.date = new Date(date.getTime()); // defensive copy in constructor
        }

        Date getDate() {
            return new Date(date.getTime()); // defensive copy in getter
        }
    }
}
