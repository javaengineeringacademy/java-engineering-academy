package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Enum Patterns ===\n");

        // WHY: Enums are type-safe constants with behavior. Better than int constants.
        // INTERNAL: Compiler generates final class extending java.lang.Enum
        // ENGINEERING: Use for fixed sets of values, add behavior via methods

        Day today = Day.MONDAY;
        System.out.println(today + " is a " + today.getType());
        System.out.println(today + " is weekend? " + today.isWeekend());

        // Enum with abstract method (each constant provides implementation)
        Operation add = new Operation() {
            public double apply(double a, double b) { return a + b; }
            public String symbol() { return "+"; }
        };

        Operation subtract = Operation.SUBTRACT;
        System.out.println("10 + 5 = " + add.apply(10, 5));
        System.out.println("10 - 5 = " + subtract.apply(10, 5));

        // TRADE-OFF: Enum vs int constants vs sealed class
        // Enum: type-safe, can have behavior, singleton
        // Int constants: no type safety, faster
        // Sealed class: more flexible, more complex
    }
}

enum Day {
    MONDAY("Weekday"), TUESDAY("Weekday"), WEDNESDAY("Weekday"),
    THURSDAY("Weekday"), FRIDAY("Weekday"), SATURDAY("Weekend"), SUNDAY("Weekend");

    private final String type;
    Day(String type) { this.type = type; }
    public String getType() { return type; }
    public boolean isWeekend() { return this == SATURDAY || this == SUNDAY; }
}

interface Operation {
    double apply(double a, double b);
    String symbol();

    Operation ADD = (a, b) -> a + b;
    Operation SUBTRACT = (a, b) -> a - b;
    Operation MULTIPLY = (a, b) -> a * b;

    default Operation andThen(Operation next) {
        return (a, b) -> next.apply(this.apply(a, b), 0);
    }
}
