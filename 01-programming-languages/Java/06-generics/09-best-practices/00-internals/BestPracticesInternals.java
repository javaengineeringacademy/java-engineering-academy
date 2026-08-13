package academy.javaengineering.generics.internals;

import java.util.*;

public class BestPracticesInternals {

    static <T extends Comparable<T>> T findMax(List<T> list) {
        return list.stream().max(Comparator.naturalOrder()).orElse(null);
    }

    public static void main(String[] args) {
        System.out.println("=== Best Practices Internals ===\n");

        // 1. Prefer Bounded Types
        System.out.println("--- Bounded Types ---");
        System.out.println("Use: <T extends Number>");
        System.out.println("Avoid: <T> (unbounded)");
        System.out.println("Bounded gives more operations");

        // 2. Use Wildcards for Read-Only
        System.out.println("\n--- Wildcards for Read ---");
        System.out.println("Use: <? extends T> for producers");
        System.out.println("Use: <? super T> for consumers");
        System.out.println("Use:<?> for unknown");

        // 3. Avoid Raw Types
        System.out.println("\n--- Avoid Raw Types ---");
        System.out.println("Raw: List list = new ArrayList()");
        System.out.println("Typed: List<String> list = new ArrayList<>()");
        System.out.println("Raw loses type safety");

        // 4. Recursive Bounds
        System.out.println("\n--- Recursive Bounds ---");
        System.out.println("<T extends Comparable<T>>");
        System.out.println("Self-referential bound");
        System.out.println("Used in sorting, searching");

        // 5. Generic Method vs Generic Class
        System.out.println("\n--- Method vs Class ---");
        System.out.println("Use generic methods for single operations");
        System.out.println("Use generic classes for type containers");
        System.out.println("Methods: type inference per call");
    }
}
