package academy.javaengineering.generics;

import academy.javaengineering.generics.GenericBuilder.CollectionBuilder;
import academy.javaengineering.generics.GenericBuilder.Computer;
import academy.javaengineering.generics.GenericBuilder.ProductBuilder;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for generic builder patterns.
 */
@DisplayName("Generic Builder Tests")
class GenericBuilderTest {

  @Nested
  @DisplayName("ProductBuilder Tests")
  class ProductBuilderTests {

    @Test
    @DisplayName("Should build computer with all configurations")
    void shouldBuildComputer() {
      Computer computer = new ProductBuilder<>(Computer::new)
          .configure(c -> c.setCpu("Intel i7"))
          .configure(c -> c.setRam(16))
          .configure(c -> c.setStorage(512))
          .configure(c -> c.setGpu("NVIDIA RTX 3060"))
          .build();

      assertNotNull(computer);
      assertEquals("Computer{cpu='Intel i7', ram=16GB, storage=512GB, "
          + "gpu='NVIDIA RTX 3060'}", computer.toString());
    }

    @Test
    @DisplayName("Should build computer with partial configuration")
    void shouldBuildPartialComputer() {
      Computer computer = new ProductBuilder<>(Computer::new)
          .configure(c -> c.setCpu("AMD Ryzen 5"))
          .build();

      assertNotNull(computer);
      assertTrue(computer.toString().contains("AMD Ryzen 5"));
    }

    @Test
    @DisplayName("Should build with single configuration")
    void shouldBuildWithSingleConfig() {
      Computer computer = new ProductBuilder<>(Computer::new)
          .configure(c -> c.setRam(32))
          .build();

      assertNotNull(computer);
      assertTrue(computer.toString().contains("ram=32GB"));
    }
  }

  @Nested
  @DisplayName("CollectionBuilder Tests")
  class CollectionBuilderTests {

    @Test
    @DisplayName("Should build list from single elements")
    void shouldBuildFromSingleElements() {
      List<String> languages = new CollectionBuilder<String>()
          .add("Java")
          .add("Python")
          .build();

      assertEquals(2, languages.size());
      assertEquals("Java", languages.get(0));
      assertEquals("Python", languages.get(1));
    }

    @Test
    @DisplayName("Should build list from varargs")
    void shouldBuildFromVarargs() {
      List<Integer> numbers = new CollectionBuilder<Integer>()
          .addAll(1, 2, 3, 4, 5)
          .build();

      assertEquals(5, numbers.size());
      assertEquals(List.of(1, 2, 3, 4, 5), numbers);
    }

    @Test
    @DisplayName("Should build list from mixed methods")
    void shouldBuildFromMixedMethods() {
      List<String> items = new CollectionBuilder<String>()
          .add("first")
          .addAll("second", "third")
          .add("fourth")
          .build();

      assertEquals(4, items.size());
    }

    @Test
    @DisplayName("Should build empty list")
    void shouldBuildEmptyList() {
      List<String> empty = new CollectionBuilder<String>().build();
      assertEquals(0, empty.size());
    }

    @Test
    @DisplayName("Should build unmodifiable list")
    void shouldBuildUnmodifiableList() {
      List<String> list = new CollectionBuilder<String>()
          .add("test")
          .build();

      assertThrows(UnsupportedOperationException.class,
          () -> list.add("another"));
    }
  }
}
