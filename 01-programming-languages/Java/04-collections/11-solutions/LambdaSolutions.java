package academy.javaengineering.collections.solutions;

import java.util.*;
import java.util.function.*;

public class LambdaSolutions {

    // Exercise 21: Predicate that checks starts with 'A'
    public static Predicate<String> startsWithA() {
        return s -> s.startsWith("A") || s.startsWith("a");
    }

    // Exercise 22: Function that converts to uppercase
    public static Function<String, String> toUpperCase() {
        return String::toUpperCase;
    }

    // Exercise 23: Consumer that prints list elements
    public static Consumer<List<String>> printAll() {
        return list -> list.forEach(System.out::println);
    }

    // Exercise 24: Supplier that generates random integers
    public static Supplier<Integer> randomInt() {
        return () -> new Random().nextInt(100);
    }

    // Exercise 25: Chain Predicate, Function, Consumer
    public static void processStrings(List<String> strings, Predicate<String> filter,
                                       Function<String, String> transform, Consumer<String> output) {
        strings.stream()
            .filter(filter)
            .map(transform)
            .forEach(output);
    }
}