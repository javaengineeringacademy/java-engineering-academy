package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for wildcard behavior and PECS principle.
 */
@DisplayName("Wildcard Tests")
class WildcardTest {

  @Nested
  @DisplayName("Unbounded Wildcard Tests")
  class UnboundedTests {

    @Test
    @DisplayName("Should get size of any list")
    void shouldGetSizeOfAnyList() {
      assertEquals("Size: 3",
          WildcardExamples.getSize(List.of("a", "b", "c")));
      assertEquals("Size: 2",
          WildcardExamples.getSize(List.of(1, 2)));
    }
  }

  @Nested
  @DisplayName("Upper Bounded Wildcard Tests (Producer)")
  class UpperBoundedTests {

    @Test
    @DisplayName("Should sum integers")
    void shouldSumIntegers() {
      assertEquals(6.0,
          WildcardExamples.sum(List.of(1, 2, 3)));
    }

    @Test
    @DisplayName("Should sum doubles")
    void shouldSumDoubles() {
      assertEquals(6.0,
          WildcardExamples.sum(List.of(1.0, 2.0, 3.0)));
    }

    @Test
    @DisplayName("Should sum mixed number types")
    void shouldSumMixedTypes() {
      List<Number> mixed = List.of(1, 2.5, 3L, 4.0f);
      assertEquals(10.5, WildcardExamples.sum(mixed));
    }
  }

  @Nested
  @DisplayName("Lower Bounded Wildcard Tests (Consumer)")
  class LowerBoundedTests {

    @Test
    @DisplayName("Should copy integers to number list")
    void shouldCopyIntegersToNumberList() {
      List<Number> dest = new ArrayList<>();
      List<Integer> source = List.of(1, 2, 3);

      WildcardExamples.copy(dest, source);

      assertEquals(3, dest.size());
      assertTrue(dest.contains(1));
      assertTrue(dest.contains(2));
      assertTrue(dest.contains(3));
    }

    @Test
    @DisplayName("Should copy strings to object list")
    void shouldCopyStringsToObjectList() {
      List<Object> dest = new ArrayList<>();
      List<String> source = List.of("a", "b");

      WildcardExamples.copy(dest, source);

      assertEquals(2, dest.size());
    }
  }

  @Nested
  @DisplayName("PECS Tests")
  class PECSTests {

    @Test
    @DisplayName("Should collect integers to number list")
    void shouldCollectIntegersToNumberList() {
      List<Integer> source = List.of(1, 2, 3);
      List<Number> result = WildcardExamples.collectNumbers(source);

      assertEquals(3, result.size());
      assertTrue(result.contains(1));
      assertTrue(result.contains(2));
      assertTrue(result.contains(3));
    }
  }
}
