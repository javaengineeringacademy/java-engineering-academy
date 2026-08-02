package academy.javaengineering.generics;

import academy.javaengineering.generics.GenericClassExamples.Box;
import academy.javaengineering.generics.GenericClassExamples.Pair;
import academy.javaengineering.generics.GenericClassExamples.SimpleContainer;
import academy.javaengineering.generics.GenericClassExamples.SortedBox;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for generic class implementations.
 */
@DisplayName("Generic Class Tests")
class GenericClassTest {

  @Nested
  @DisplayName("Box Tests")
  class BoxTests {

    @Test
    @DisplayName("Should create empty box")
    void shouldCreateEmptyBox() {
      Box<String> box = new Box<>();
      assertNull(box.getContent());
    }

    @Test
    @DisplayName("Should create box with content")
    void shouldCreateBoxWithContent() {
      Box<String> box = new Box<>("Hello");
      assertEquals("Hello", box.getContent());
    }

    @Test
    @DisplayName("Should set content")
    void shouldSetContent() {
      Box<Integer> box = new Box<>();
      box.setContent(42);
      assertEquals(42, box.getContent());
    }

    @Test
    @DisplayName("Should have correct toString")
    void shouldHaveCorrectToString() {
      Box<String> box = new Box<>("Test");
      assertEquals("Box{content=Test}", box.toString());
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEquals() {
      Box<String> box1 = new Box<>("Same");
      Box<String> box2 = new Box<>("Same");
      Box<String> box3 = new Box<>("Different");

      assertEquals(box1, box2);
      assertNotEquals(box1, box3);
    }
  }

  @Nested
  @DisplayName("Pair Tests")
  class PairTests {

    @Test
    @DisplayName("Should create key-value pair")
    void shouldCreatePair() {
      Pair<String, Integer> pair = new Pair<>("age", 30);
      assertEquals("age", pair.getKey());
      assertEquals(30, pair.getValue());
    }

    @Test
    @DisplayName("Should have correct toString")
    void shouldHaveCorrectToString() {
      Pair<String, Integer> pair = new Pair<>("key", 1);
      assertEquals("Pair{key=key, value=1}", pair.toString());
    }
  }

  @Nested
  @DisplayName("SimpleContainer Tests")
  class ContainerTests {

    @Test
    @DisplayName("Should set and get value")
    void shouldSetAndGet() {
      SimpleContainer<Double> container = new SimpleContainer<>();
      container.set(3.14);
      assertEquals(3.14, container.get());
    }
  }

  @Nested
  @DisplayName("SortedBox Tests")
  class SortedBoxTests {

    @Test
    @DisplayName("Should maintain sorted order")
    void shouldMaintainSortedOrder() {
      SortedBox<String> sortedBox = new SortedBox<>();
      sortedBox.add("Banana");
      sortedBox.add("Apple");
      sortedBox.add("Cherry");

      List<String> sorted = sortedBox.getSorted();
      assertEquals(List.of("Apple", "Banana", "Cherry"), sorted);
    }

    @Test
    @DisplayName("Should handle single element")
    void shouldHandleSingleElement() {
      SortedBox<Integer> sortedBox = new SortedBox<>();
      sortedBox.add(42);
      assertEquals(List.of(42), sortedBox.getSorted());
    }
  }
}
