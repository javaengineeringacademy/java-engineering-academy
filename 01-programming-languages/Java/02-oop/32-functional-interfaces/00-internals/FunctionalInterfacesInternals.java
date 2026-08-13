package academy.javaengineering.oop.internals;

import java.util.function.*;

public class FunctionalInterfacesInternals {

    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    @FunctionalInterface
    interface Processor<T> {
        T process(T input);
    }

    public static void main(String[] args) {
        System.out.println("=== Functional Interfaces Internals ===\n");

        // 1. @FunctionalInterface
        System.out.println("--- @FunctionalInterface ---");
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        System.out.println("add(2,3) = " + add.calculate(2, 3));
        System.out.println("multiply(2,3) = " + multiply.calculate(2, 3));

        // 2. Generic Functional Interface
        System.out.println("\n--- Generic Functional Interface ---");
        Processor<String> toUpper = s -> s.toUpperCase();
        Processor<Integer> doubleIt = n -> n * 2;
        System.out.println("toUpper: " + toUpper.process("hello"));
        System.out.println("doubleIt: " + doubleIt.process(5));

        // 3. Built-in Functional Interfaces
        System.out.println("\n--- Built-in Interfaces ---");
        System.out.println("Predicate<T>: boolean test(T)");
        System.out.println("Function<T,R>: R apply(T)");
        System.out.println("Consumer<T>: void accept(T)");
        System.out.println("Supplier<T>: T get()");
    }
}
