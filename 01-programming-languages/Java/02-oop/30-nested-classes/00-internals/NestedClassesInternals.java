package academy.javaengineering.oop.internals;

public class NestedClassesInternals {

    static class Outer {
        private String outerField = "Outer";

        class Inner {
            void display() {
                System.out.println("Inner: " + outerField);
            }
        }

        static class StaticNested {
            void display() {
                System.out.println("Static nested");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Nested Classes Internals ===\n");

        // 1. Member Inner Class
        System.out.println("--- Member Inner Class ---");
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.display();

        // 2. Static Nested Class
        System.out.println("\n--- Static Nested Class ---");
        Outer.StaticNested nested = new Outer.StaticNested();
        nested.display();

        // 3. When to Use
        System.out.println("\n--- When to Use ---");
        System.out.println("Inner: when class depends on outer");
        System.out.println("Static nested: when independent");
        System.out.println("Prefer static nested for performance");
    }
}
