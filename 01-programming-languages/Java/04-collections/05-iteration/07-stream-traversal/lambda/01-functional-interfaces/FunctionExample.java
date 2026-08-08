import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class FunctionExample {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("alice", "bob", "charlie", "david", "eve");

        // Basic function usage with map
        Function<String, String> toUpperCase = String::toUpperCase;
        List<String> upperNames = names.stream()
            .map(toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Uppercase names: " + upperNames);

        // Function with apply()
        Function<String, Integer> toLength = String::length;
        List<Integer> lengths = names.stream()
            .map(toLength)
            .collect(Collectors.toList());
        System.out.println("Name lengths: " + lengths);

        // Function composition with andThen
        Function<String, String> addExclamation = s -> s + "!";
        Function<String, String> shout = toUpperCase.andThen(addExclamation);
        List<String> shouted = names.stream()
            .map(shout)
            .collect(Collectors.toList());
        System.out.println("Shouted names: " + shouted);

        // Function composition with compose
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;
        Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
        Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);

        System.out.println("Double then add 10 (5): " + doubleThenAdd.apply(5));
        System.out.println("Add then double (5): " + addThenDouble.apply(5));

        // Function with flatMap
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5, 6),
            Arrays.asList(7, 8, 9)
        );

        Function<List<Integer>, Stream<Integer>> flatten = List::stream;
        List<Integer> flat = nested.stream()
            .flatMap(flatten)
            .collect(Collectors.toList());
        System.out.println("Flattened: " + flat);

        // Function.identity()
        List<String> identity = names.stream()
            .map(Function.identity())
            .collect(Collectors.toList());
        System.out.println("Identity: " + identity);

        // Function for string manipulation
        Function<String, String> reverse = s -> new StringBuilder(s).reverse().toString();
        List<String> reversed = names.stream()
            .map(reverse)
            .collect(Collectors.toList());
        System.out.println("Reversed names: " + reversed);

        // Function with conditionals
        Function<Integer, String> categorize = n -> {
            if (n < 3) return "small";
            if (n < 7) return "medium";
            return "large";
        };

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<String> categories = numbers.stream()
            .map(categorize)
            .collect(Collectors.toList());
        System.out.println("Categories: " + categories);

        // Function chaining with multiple transforms
        Function<String, String> trim = String::trim;
        Function<String, String> toLower = String::toLowerCase;
        Function<String, String> removeSpaces = s -> s.replace(" ", "");

        Function<String, String> normalize = trim
            .andThen(toLower)
            .andThen(removeSpaces);

        List<String> messy = Arrays.asList("  Hello ", " WORLD  ", " Java  ");
        List<String> clean = messy.stream()
            .map(normalize)
            .collect(Collectors.toList());
        System.out.println("Cleaned: " + clean);

        // Function with collect and grouping
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by length: " + byLength);

        // BiFunction usage
        BiFunction<String, Integer, String> padLeft = (s, n) -> {
            StringBuilder sb = new StringBuilder();
            for (int i = s.length(); i < n; i++) {
                sb.append(' ');
            }
            sb.append(s);
            return sb.toString();
        };

        List<String> padded = names.stream()
            .map(name -> padLeft.apply(name, 10))
            .collect(Collectors.toList());
        padded.forEach(System.out::println);
    }
}
