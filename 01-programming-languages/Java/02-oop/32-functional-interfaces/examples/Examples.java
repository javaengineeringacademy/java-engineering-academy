package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Functional Interface Patterns ===\n");

        // WHY: Functional interfaces enable lambda expressions and method references
        // INTERNAL: @FunctionalInterface ensures single abstract method
        // ENGINEERING: Use for callbacks, strategies, operations

        // Built-in functional interfaces
        java.util.function.Predicate<String> isLong = s -> s.length() > 5;
        java.util.function.Function<String, Integer> toLength = String::length;
        java.util.function.Consumer<String> printer = System.out::println;
        java.util.function.Supplier<String> greeting = () -> "Hello!";

        // Custom functional interface
        Transformer<String, String> upperCase = String::toUpperCase;
        Transformer<String, String> trim = String::trim;

        String input = "  Hello World  ";
        System.out.println("Original: '" + input + "'");
        System.out.println("Upper: '" + upperCase.transform(input) + "'");
        System.out.println("Trim: '" + trim.transform(input) + "'");

        // TRADE-OFF: @FunctionalInterface annotation is optional but recommended
        // Catches accidental second abstract method at compile time
    }
}

@FunctionalInterface
interface Transformer<T, R> {
    R transform(T input);

    // Can have default methods
    default Transformer<T, R> andThen(Transformer<R, R> after) {
        return input -> after.transform(this.transform(input));
    }
}
