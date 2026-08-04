package academy.javaengineering.generics;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for type witness syntax in generics.
 */
@DisplayName("Type Witness Tests")
class TypeWitnessTest {

  @Nested
  @DisplayName("Basic Type Witness")
  class BasicWitnessTests {

    @Test
    @DisplayName("Should get first element with type witness")
    void shouldGetFirstWithWitness() {
      String result = TypeWitnessDemo.<String>getFirst(List.of("a", "b"));
      assertEquals("a", result);
    }

    @Test
    @DisplayName("Should get first integer with type witness")
    void shouldGetFirstIntegerWithWitness() {
      Integer result = TypeWitnessDemo.<Integer>getFirst(List.of(10, 20));
      assertEquals(10, result);
    }

    @Test
    @DisplayName("Should return null when list is empty")
    void shouldReturnNullForEmptyList() {
      String result = TypeWitnessDemo.<String>getFirst(List.of());
      assertNull(result);
    }
  }

  @Nested
  @DisplayName("Multiple Type Parameters")
  class MultipleTypeParamsTests {

    @Test
    @DisplayName("Should create entry with type witness")
    void shouldCreateEntryWithWitness() {
      Map.Entry<String, Integer> entry =
          TypeWitnessDemo.<String, Integer>entryOf("key", 42);
      assertEquals("key", entry.getKey());
      assertEquals(42, entry.getValue());
    }

    @Test
    @DisplayName("Should create entry with different types")
    void shouldCreateEntryWithDifferentTypes() {
      Map.Entry<Integer, Boolean> entry =
          TypeWitnessDemo.<Integer, Boolean>entryOf(1, true);
      assertEquals(1, entry.getKey());
      assertEquals(true, entry.getValue());
    }
  }

  @Nested
  @DisplayName("Type Inference vs Explicit Witness")
  class InferenceVsExplicitTests {

    @Test
    @DisplayName("Should infer type without witness")
    void shouldInferType() {
      List<String> strings = List.of("hello", "world");
      String first = TypeWitnessDemo.getFirst(strings);
      assertEquals("hello", first);
    }

    @Test
    @DisplayName("Should use witness for ambiguous null")
    void shouldUseWitnessForNull() {
      Object result = TypeWitnessDemo.<String>getFirst(null);
      assertNull(result);
    }

    @Test
    @DisplayName("Should create list with type witness")
    void shouldCreateListWithWitness() {
      List<String> langs = TypeWitnessDemo.<String>listOf("Java", "Python");
      assertEquals(2, langs.size());
      assertEquals("Java", langs.get(0));
      assertEquals("Python", langs.get(1));
    }

    @Test
    @DisplayName("Should create integer list with type witness")
    void shouldCreateIntegerListWithWitness() {
      List<Integer> nums = TypeWitnessDemo.<Integer>listOf(1, 2, 3);
      assertEquals(3, nums.size());
      assertEquals(List.of(1, 2, 3), nums);
    }
  }
}
