import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class LambdaTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testFilterEvenNumbers();
        testMapToUpperCase();
        testReduceToSum();
        testPredicateComposition();
        testFunctionComposition();
        testConsumerForEach();
        testSupplierCreation();
        testMethodReferenceStatic();
        testMethodReferenceInstance();
        testStreamCollectToMap();

        System.out.println("\n========================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("========================================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testFilterEvenNumbers() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        assertResult("Filter even numbers", evens.equals(Arrays.asList(2, 4, 6, 8, 10)));
    }

    static void testMapToUpperCase() {
        List<String> lower = Arrays.asList("hello", "world");
        List<String> upper = lower.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        assertResult("Map to uppercase", upper.equals(Arrays.asList("HELLO", "WORLD")));
    }

    static void testReduceToSum() {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
        int sum = nums.stream().reduce(0, Integer::sum);
        assertResult("Reduce to sum", sum == 15);
    }

    static void testPredicateComposition() {
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> positiveAndEven = isPositive.and(isEven);
        assertResult("Predicate AND", positiveAndEven.test(4));
        assertResult("Predicate AND false case", !positiveAndEven.test(3));

        Predicate<Integer> positiveOrEven = isPositive.or(isEven);
        assertResult("Predicate OR", positiveOrEven.test(-2));
        assertResult("Predicate OR true case", positiveOrEven.test(3));

        Predicate<Integer> notPositive = isPositive.negate();
        assertResult("Predicate NEGATE", notPositive.test(-1));
    }

    static void testFunctionComposition() {
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;

        Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
        assertResult("Function andThen", doubleThenAdd.apply(5) == 20);

        Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
        assertResult("Function compose", addThenDouble.apply(5) == 30);
    }

    static void testConsumerForEach() {
        List<String> captured = new ArrayList<>();
        Consumer<String> capturer = captured::add;
        Arrays.asList("a", "b", "c").forEach(capturer);
        assertResult("Consumer forEach", captured.equals(Arrays.asList("a", "b", "c")));
    }

    static void testSupplierCreation() {
        Supplier<ArrayList<String>> factory = ArrayList::new;
        ArrayList<String> list = factory.get();
        list.add("test");
        assertResult("Supplier creates list", list.size() == 1 && list.get(0).equals("test"));
    }

    static void testMethodReferenceStatic() {
        Function<String, Integer> parser = Integer::parseInt;
        assertResult("Method ref static", parser.apply("42") == 42);
    }

    static void testMethodReferenceInstance() {
        String str = "hello";
        Supplier<Integer> lengthSupplier = str::length;
        assertResult("Method ref instance", lengthSupplier.get() == 5);
    }

    static void testStreamCollectToMap() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        Map<String, Integer> nameLengths = names.stream()
            .collect(Collectors.toMap(name -> name, String::length));
        assertResult("Collect to map", nameLengths.get("Alice") == 5
            && nameLengths.get("Bob") == 3
            && nameLengths.get("Charlie") == 7);
    }

    static void assertResult(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }
}
