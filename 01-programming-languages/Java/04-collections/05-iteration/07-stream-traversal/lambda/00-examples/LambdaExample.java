import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class LambdaExample {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Filter even numbers
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Even numbers: " + evens);

        // Map to squares
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("Squares: " + squares);

        // Reduce to sum
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // String operations
        List<String> names = Arrays.asList("alice", "bob", "charlie", "david");

        // Filter, transform, sort
        List<String> result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Long names uppercase: " + result);

        // Method reference with forEach
        System.out.print("Names: ");
        names.forEach(name -> System.out.print(name + " "));
        System.out.println();

        // Chained operations
        String joined = names.stream()
            .filter(name -> name.startsWith("a") || name.startsWith("c"))
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
        System.out.println("A or C names: " + joined);

        // Predicate composition
        Predicate<String> startsWithA = s -> s.startsWith("a");
        Predicate<String> hasFourChars = s -> s.length() == 4;
        Predicate<String> combined = startsWithA.or(hasFourChars);

        List<String> filtered = names.stream()
            .filter(combined)
            .collect(Collectors.toList());
        System.out.println("Starts with A or has 4 chars: " + filtered);

        // Function composition
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;
        Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);

        List<Integer> transformed = numbers.stream()
            .map(doubleThenAdd)
            .collect(Collectors.toList());
        System.out.println("Double then add 10: " + transformed);

        // Supplier example
        Supplier<List<String>> listFactory = ArrayList::new;
        List<String> newList = listFactory.get();
        newList.add("created by supplier");
        System.out.println("Supplier created: " + newList);

        // BinaryOperator
        BinaryOperator<Integer> maxOp = BinaryOperator.maxBy(Integer::compareTo);
        Optional<Integer> max = numbers.stream()
            .reduce(maxOp);
        System.out.println("Max value: " + max.orElse(0));
    }
}
