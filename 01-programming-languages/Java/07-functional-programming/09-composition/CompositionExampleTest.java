import java.util.function.*;
import java.util.List;
import java.util.Arrays;

/**
 * Tests for CompositionExample.
 */
class CompositionExampleTest {

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but got false");
        }
    }

    static void testBasicComposition() {
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;

        Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
        assertEquals(20, doubleThenAdd.apply(5));

        Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
        assertEquals(30, addThenDouble.apply(5));

        Function<Integer, Integer> pipeline = doubleIt
            .andThen(addTen)
            .andThen(x -> x * x);
        assertEquals(400, pipeline.apply(5));
    }

    static void testPredicateComposition() {
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;

        Predicate<Integer> isPositiveEven = isPositive.and(isEven);
        assertTrue(isPositiveEven.test(6));
        assertTrue(!isPositiveEven.test(5));

        Predicate<Integer> isNotPositive = isPositive.negate();
        assertTrue(isNotPositive.test(-1));
        assertTrue(!isNotPositive.test(1));
    }

    public static void main(String[] args) {
        testBasicComposition();
        testPredicateComposition();
        System.out.println("All CompositionExample tests passed!");
    }
}
