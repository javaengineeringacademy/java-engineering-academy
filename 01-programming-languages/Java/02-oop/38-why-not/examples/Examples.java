package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== When NOT to Use OOP ===\n");

        // WHY: OOP isn't always the best tool. Know when to use alternatives.
        // INTERNAL: OOP adds overhead, complexity, indirection
        // ENGINEERING: Use simple functions for stateless operations

        // BAD: OOP overkill for simple transformation
        System.out.println("Over-engineered: " + new StringTransformer("hello").transform());

        // GOOD: Simple static method
        System.out.println("Simple: " + toUpperCase("hello"));

        // BAD: Class hierarchy for utility methods
        System.out.println("Bad design: " + new MathHelper().add(2, 3));

        // GOOD: Static utility class
        System.out.println("Good design: " + MathUtil2.add(2, 3));

        // TRADE-OFF: OOP adds value when:
        // - State management needed
        // - Polymorphism required
        // - Complex domain modeling
        // - BUT: adds overhead for simple operations
    }

    static String toUpperCase(String s) { return s.toUpperCase(); }
}

class StringTransformer {
    private final String input;
    StringTransformer(String input) { this.input = input; }
    String transform() { return input.toUpperCase(); }
}

class MathHelper {
    int add(int a, int b) { return a + b; }
}

class MathUtil2 {
    static int add(int a, int b) { return a + b; }
}
