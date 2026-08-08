import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class PredicateExample {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Basic predicate usage with filter
        Predicate<Integer> isEven = n -> n % 2 == 0;
        List<Integer> evens = numbers.stream()
            .filter(isEven)
            .collect(Collectors.toList());
        System.out.println("Even numbers: " + evens);

        // Predicate with test()
        Predicate<String> isLong = s -> s.length() > 5;
        System.out.println("'hello' is long: " + isLong.test("hello"));
        System.out.println("'hello world' is long: " + isLong.test("hello world"));

        // Predicate AND composition
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isLessThanFive = n -> n < 5;
        Predicate<Integer> positiveAndLessThanFive = isPositive.and(isLessThanFive);

        List<Integer> result = numbers.stream()
            .filter(positiveAndLessThanFive)
            .collect(Collectors.toList());
        System.out.println("Positive and < 5: " + result);

        // Predicate OR composition
        Predicate<Integer> isGreaterThanSeven = n -> n > 7;
        Predicate<Integer> isThree = n -> n == 3;
        Predicate<Integer> greaterThanSevenOrThree = isGreaterThanSeven.or(isThree);

        List<Integer> orResult = numbers.stream()
            .filter(greaterThanSevenOrThree)
            .collect(Collectors.toList());
        System.out.println("Greater than 7 or equal to 3: " + orResult);

        // Predicate NEGATE composition
        Predicate<Integer> isOdd = isEven.negate();
        List<Integer> odds = numbers.stream()
            .filter(isOdd)
            .collect(Collectors.toList());
        System.out.println("Odd numbers: " + odds);

        // Predicate with strings
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

        Predicate<String> startsWithA = s -> s.startsWith("A");
        Predicate<String> hasFourChars = s -> s.length() == 4;

        List<String> aNames = names.stream()
            .filter(startsWithA)
            .collect(Collectors.toList());
        System.out.println("Names starting with A: " + aNames);

        // Chained predicates
        List<String> fourCharNames = names.stream()
            .filter(hasFourChars)
            .collect(Collectors.toList());
        System.out.println("Names with 4 chars: " + fourCharNames);

        // Complex predicate composition
        Predicate<String> startsWithC = s -> s.startsWith("C");
        Predicate<String> startsWithD = s -> s.startsWith("D");
        Predicate<String> startsWithAorC = startsWithA.or(startsWithC);
        Predicate<String> startsWithAorCorD = startsWithAorC.or(startsWithD);

        List<String> acdNames = names.stream()
            .filter(startsWithAorCorD)
            .collect(Collectors.toList());
        System.out.println("Names starting with A, C, or D: " + acdNames);

        // Predicate for range checking
        Predicate<Integer> inRange = n -> n >= 3 && n <= 7;
        List<Integer> inRangeNums = numbers.stream()
            .filter(inRange)
            .collect(Collectors.toList());
        System.out.println("Numbers in range [3,7]: " + inRangeNums);

        // Predicate with allMatch, anyMatch, noneMatch
        boolean allPositive = numbers.stream()
            .allMatch(isPositive);
        System.out.println("All numbers positive: " + allPositive);

        boolean anyEven = numbers.stream()
            .anyMatch(isEven);
        System.out.println("Any number even: " + anyEven);

        boolean noneNegative = numbers.stream()
            .noneMatch(n -> n < 0);
        System.out.println("No negative numbers: " + noneNegative);

        // Predicate with Objects::nonNull
        List<String> mixed = Arrays.asList("hello", null, "world", null, "java");
        List<String> nonNull = mixed.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        System.out.println("Non-null strings: " + nonNull);
    }
}
