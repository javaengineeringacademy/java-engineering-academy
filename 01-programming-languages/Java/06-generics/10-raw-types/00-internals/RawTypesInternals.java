package academy.javaengineering.generics.internals;

import java.util.*;

public class RawTypesInternals {

    @SuppressWarnings("rawtypes")
    static class OldContainer {
        Object value;
        public OldContainer(Object value) { this.value = value; }
        public Object getValue() { return value; }
    }

    static class TypedContainer<T> {
        T value;
        public TypedContainer(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Raw Types Internals ===\n");

        // 1. What Are Raw Types
        System.out.println("--- Raw Types ---");
        @SuppressWarnings("rawtypes")
        List list = new ArrayList();
        list.add("Hello");
        list.add(42);
        System.out.println("Raw List: accepts any type");
        System.out.println("No compile-time type checking");

        // 2. Raw vs Parameterized
        System.out.println("\n--- Raw vs Parameterized ---");
        System.out.println("Raw: List (no type info)");
        System.out.println("Parameterized: List<String> (type info)");
        System.out.println("Raw: backward compatible with pre-generics code");

        // 3. Conversion Rules
        System.out.println("\n--- Conversion ---");
        System.out.println("Parameterized -> Raw: allowed (unchecked)");
        System.out.println("Raw -> Parameterized: allowed (unchecked)");
        System.out.println("Both produce unchecked warnings");

        // 4. instanceof Check
        System.out.println("\n--- instanceof ---");
        System.out.println("Can: list instanceof List");
        System.out.println("Cannot: list instanceof List<String>");
        System.out.println("Raw type check only");

        // 5. Legacy Code Integration
        System.out.println("\n--- Legacy Code ---");
        System.out.println("Raw types in method signatures");
        System.out.println("Causes: unchecked warnings");
        System.out.println("Fix: add type parameters");
    }
}
