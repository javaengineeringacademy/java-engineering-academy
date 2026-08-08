import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ConsumerExample {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

        // Basic consumer with forEach
        Consumer<String> print = System.out::println;
        System.out.println("Printing all names:");
        names.forEach(print);

        // Consumer with lambda
        System.out.println("\nNames with index:");
        for (int i = 0; i < names.size(); i++) {
            Consumer<String> indexed = name ->
                System.out.println("  " + (i + 1) + ". " + name);
            indexed.accept(names.get(i));
        }

        // Consumer chaining with andThen
        Consumer<String> toUpper = s -> System.out.print(s.toUpperCase());
        Consumer<String> withNewline = s -> System.out.println();
        Consumer<String> shoutAndNewline = toUpper.andThen(withNewline);

        System.out.println("\nShouting names:");
        names.forEach(shoutAndNewline);

        // Consumer for collecting side effects
        List<String> collected = new ArrayList<>();
        Consumer<String> addToCollection = collected::add;
        names.forEach(addToCollection);
        System.out.println("\nCollected: " + collected);

        // Consumer with peek for debugging
        List<String> result = names.stream()
            .peek(name -> System.out.println("Processing: " + name))
            .filter(name -> name.length() > 3)
            .peek(name -> System.out.println("After filter: " + name))
            .map(String::toUpperCase)
            .peek(name -> System.out.println("After map: " + name))
            .collect(Collectors.toList());
        System.out.println("\nFinal result: " + result);

        // Consumer with integers
        Consumer<Integer> printSquare = n -> System.out.print(n * n + " ");
        System.out.print("\nSquares: ");
        Arrays.asList(1, 2, 3, 4, 5).forEach(printSquare);
        System.out.println();

        // BiConsumer usage
        BiConsumer<String, Integer> printWithIndex = (name, index) ->
            System.out.println("  " + index + ": " + name);
        System.out.println("\nBiConsumer with index:");
        names.forEach(printWithIndex);

        // BiConsumer for map operations
        Map<String, Integer> nameLengths = new HashMap<>();
        BiConsumer<String, Integer> addToMap = nameLengths::put;
        names.forEach(name -> addToMap.accept(name, name.length()));
        System.out.println("\nName lengths map: " + nameLengths);

        // Consumer chain for multi-step processing
        Consumer<String> log = s -> System.out.println("[LOG] " + s);
        Consumer<String> validate = s -> {
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
        };
        Consumer<String> process = validate.andThen(log);

        System.out.println("\nProcessing names:");
        names.forEach(process);

        // Consumer with conditional logic
        Consumer<String> conditionalPrint = s -> {
            if (s.startsWith("A") || s.startsWith("C")) {
                System.out.println("  Match: " + s);
            } else {
                System.out.println("  Skip: " + s);
            }
        };

        System.out.println("\nConditional consumer:");
        names.forEach(conditionalPrint);

        // Consumer for building strings
        StringBuilder sb = new StringBuilder();
        Consumer<String> appendWithComma = s -> {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(s);
        };
        names.forEach(appendWithComma);
        System.out.println("\nJoined: " + sb.toString());
    }
}
