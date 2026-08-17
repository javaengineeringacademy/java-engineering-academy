package academy.javaengineering.knowledgeatoms.typesafety;

import java.util.*;

public class TypeSafetyInternals {

    public static void main(String[] args) {
        System.out.println("=== Type Safety Internals ===\n");

        // 1. Type erasure
        System.out.println("--- Type Erasure ---");
        demonstrateTypeErasure();

        // 2. Generic constraints
        System.out.println("\n--- Generic Constraints ---");
        demonstrateGenericConstraints();

        // 3. Cast mechanics
        System.out.println("\n--- Cast Mechanics ---");
        demonstrateCasting();

        // 4. Pattern matching
        System.out.println("\n--- Pattern Matching (Java 16+) ---");
        demonstratePatternMatching();
    }

    private static void demonstrateTypeErasure() {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();

        System.out.println("List<String> class: " + strings.getClass().getName());
        System.out.println("List<Integer> class: " + integers.getClass().getName());
        System.out.println("Same class at runtime: " + (strings.getClass() == integers.getClass()));
        System.out.println("Type parameter erased to: Object (or bound)");

        // Cannot do these at runtime:
        System.out.println("\nCannot do at runtime (type erasure):");
        System.out.println("  - new T(): type parameter not available");
        System.out.println("  - new T[]: cannot create generic arrays");
        System.out.println("  - obj instanceof List<String>: cannot check generic type");
    }

    private static void demonstrateGenericConstraints() {
        // Bounded type parameters
        System.out.println("Bounded type parameters restrict types at compile time:");
        System.out.println("  <T extends Number> — T must be Number or subclass");
        System.out.println("  <T extends Comparable<T>> — T must be Comparable");

        // Wildcards
        System.out.println("\nWildcards provide flexibility:");
        System.out.println("  ? extends T — upper bound (read-only)");
        System.out.println("  ? super T — lower bound (write-only)");
        System.out.println("  ? — unbounded (read-only)");

        // Type inference
        System.out.println("\nType inference (Java 7+):");
        System.out.println("  List<String> list = new ArrayList<>(); // diamond operator");
        System.out.println("  var list = List.of(1, 2, 3); // local variable type inference");
    }

    private static void demonstrateCasting() {
        Object obj = "Hello";

        // Safe cast with instanceof
        if (obj instanceof String s) {
            System.out.println("Pattern matching: " + s.toUpperCase());
        }

        // Unsafe cast
        try {
            Integer num = (Integer) obj;
        } catch (ClassCastException e) {
            System.out.println("ClassCastException: " + e.getMessage());
        }

        // Generic type erasure causes hidden casts
        List<String> list = List.of("a", "b", "c");
        Object first = list.get(0);  // implicit cast at bytecode level
        System.out.println("Generic get() performs implicit cast: " + first);
    }

    private static void demonstratePatternMatching() {
        // Pattern matching with instanceof
        Object obj = 42;
        if (obj instanceof Integer i && i > 0) {
            System.out.println("Positive integer: " + i);
        }

        // Pattern matching with switch (Java 21)
        System.out.println("\nSwitch expression with patterns:");
        Object value = "Hello";
        String result = switch (value) {
            case Integer i -> "Integer: " + i;
            case String s -> "String: " + s;
            case Double d -> "Double: " + d;
            default -> "Unknown: " + value;
        };
        System.out.println(result);
    }
}
