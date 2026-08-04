package academy.javaengineering.generics;

import academy.javaengineering.generics.BoundedTypeExamples.Statistics;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for bounded type parameters in generics.
 */
@DisplayName("Bounded Type Tests")
class BoundedTypeTest {

  @Nested
  @DisplayName("Upper Bounded Types")
  class UpperBoundedTests {

    @Test
    @DisplayName("Should sum list of integers")
    void shouldSumIntegers() {
      List<Integer> integers = List.of(1, 2, 3, 4, 5);
      assertEquals(15.0, BoundedTypeExamples.sumOfNumbers(integers));
    }

    @Test
    @DisplayName("Should sum list of doubles")
    void shouldSumDoubles() {
      List<Double> doubles = List.of(1.5, 2.5, 3.5);
      assertEquals(7.5, BoundedTypeExamples.sumOfNumbers(doubles));
    }

    @Test
    @DisplayName("Should handle empty list")
    void shouldHandleEmptyList() {
      List<Integer> empty = List.of();
      assertEquals(0.0, BoundedTypeExamples.sumOfNumbers(empty));
    }
  }

  @Nested
  @DisplayName("findMin Tests")
  class FindMinTests {

    @Test
    @DisplayName("Should find minimum integer")
    void shouldFindMinInteger() {
      List<Integer> numbers = List.of(5, 3, 8, 1, 9);
      assertEquals(1, BoundedTypeExamples.findMin(numbers));
    }

    @Test
    @DisplayName("Should find minimum string")
    void shouldFindMinString() {
      List<String> words = List.of("cherry", "apple", "banana");
      assertEquals("apple", BoundedTypeExamples.findMin(words));
    }

    @Test
    @DisplayName("Should throw on empty list")
    void shouldThrowOnEmptyList() {
      List<Integer> empty = List.of();
      assertThrows(IllegalArgumentException.class,
          () -> BoundedTypeExamples.findMin(empty));
    }
  }

  @Nested
  @DisplayName("Lower Bounded Types")
  class LowerBoundedTests {

    @Test
    @DisplayName("Should add integers to Number list")
    void shouldAddToNumberList() {
      List<Number> numberList = new ArrayList<>();
      BoundedTypeExamples.addNumbers(numberList, List.of(10, 20, 30));
      assertEquals(3, numberList.size());
      assertEquals(10, numberList.get(0));
    }
  }

  @Nested
  @DisplayName("Statistics Class Tests")
  class StatisticsTests {

    @Test
    @DisplayName("Should calculate average of integers")
    void shouldCalculateIntegerAverage() {
      Statistics<Integer> stats = new Statistics<>();
      stats.add(10);
      stats.add(20);
      stats.add(30);
      assertEquals(20.0, stats.average());
    }

    @Test
    @DisplayName("Should calculate average of doubles")
    void shouldCalculateDoubleAverage() {
      Statistics<Double> stats = new Statistics<>();
      stats.add(1.0);
      stats.add(2.0);
      stats.add(3.0);
      assertEquals(2.0, stats.average());
    }
  }
}
