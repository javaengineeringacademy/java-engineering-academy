package academy.javaengineering.generics;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for generic methods.
 */
@DisplayName("Generic Method Tests")
class GenericMethodTest {

  @Nested
  @DisplayName("findMax Tests")
  class FindMaxTests {

    @Test
    @DisplayName("Should find max integer")
    void shouldFindMaxInteger() {
      List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9);
      assertEquals(9, GenericMethodExamples.findMax(numbers));
    }

    @Test
    @DisplayName("Should find max string")
    void shouldFindMaxString() {
      List<String> words = List.of("apple", "banana", "cherry");
      assertEquals("cherry", GenericMethodExamples.findMax(words));
    }

    @Test
    @DisplayName("Should throw exception for empty list")
    void shouldThrowForEmptyList() {
      List<Integer> empty = List.of();
      assertThrows(IllegalArgumentException.class,
          () -> GenericMethodExamples.findMax(empty));
    }

    @Test
    @DisplayName("Should handle single element")
    void shouldHandleSingleElement() {
      List<Double> single = List.of(3.14);
      assertEquals(3.14, GenericMethodExamples.findMax(single));
    }
  }

  @Nested
  @DisplayName("swap Tests")
  class SwapTests {

    @Test
    @DisplayName("Should swap elements in array")
    void shouldSwapElements() {
      String[] array = {"A", "B", "C"};
      GenericMethodExamples.swap(array, 0, 2);
      assertEquals("C", array[0]);
      assertEquals("A", array[2]);
    }

    @Test
    @DisplayName("Should swap same index")
    void shouldSwapSameIndex() {
      Integer[] array = {1, 2, 3};
      GenericMethodExamples.swap(array, 1, 1);
      assertEquals(2, array[1]);
    }
  }

  @Nested
  @DisplayName("asList Tests")
  class AsListTests {

    @Test
    @DisplayName("Should create list from varargs")
    void shouldCreateListFromVarargs() {
      List<Integer> list = GenericMethodExamples.asList(1, 2, 3);
      assertEquals(List.of(1, 2, 3), list);
    }

    @Test
    @DisplayName("Should create empty list")
    void shouldCreateEmptyList() {
      List<String> list = GenericMethodExamples.asList();
      assertEquals(0, list.size());
    }
  }

  @Nested
  @DisplayName("zipToEntries Tests")
  class ZipToEntriesTests {

    @Test
    @DisplayName("Should zip keys and values")
    void shouldZipKeysAndValues() {
      List<String> keys = List.of("a", "b", "c");
      List<Integer> values = List.of(1, 2, 3);

      var entries = GenericMethodExamples.zipToEntries(keys, values);

      assertEquals(3, entries.size());
      assertEquals(Map.entry("a", 1), entries.get(0));
      assertEquals(Map.entry("b", 2), entries.get(1));
      assertEquals(Map.entry("c", 3), entries.get(2));
    }

    @Test
    @DisplayName("Should handle unequal list sizes")
    void shouldHandleUnequalSizes() {
      List<String> keys = List.of("a", "b", "c");
      List<Integer> values = List.of(1, 2);

      var entries = GenericMethodExamples.zipToEntries(keys, values);

      assertEquals(2, entries.size());
    }
  }
}
