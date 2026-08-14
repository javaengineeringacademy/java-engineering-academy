package academy.javaengineering.functional.examples;

import java.util.*;
import java.util.stream.*;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== 02-lambda-expressions Examples ===\n");

        // WHY: Functional programming enables declarative, composable, testable code
        // INTERNAL: Lambdas compile to invokedynamic, method references to specific invoke instructions
        // ENGINEERING: Use for data transformation, callbacks, stream processing

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

        // Filter and transform
        List<String> result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.toList());

        System.out.println("Names > 3 chars: " + result);

        // TRADE-OFF: Readability vs performance
        // Functional: declarative, composable, but can have overhead
        // Imperative: explicit, sometimes faster, but more verbose
    }
}
