package academy.javaengineering.generics;

import academy.javaengineering.generics.TypeErasureDemo.Container;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for type erasure behavior in Java generics.
 */
@DisplayName("Type Erasure Tests")
class TypeErasureTest {

  @Nested
  @DisplayName("Runtime Type Equality")
  class RuntimeTypeTests {

    @Test
    @DisplayName("Should have same runtime class for different type parameters")
    void shouldHaveSameRuntimeClass() {
      Container<String> stringContainer = new Container<>();
      Container<Integer> intContainer = new Container<>();
      assertEquals(stringContainer.getClass(), intContainer.getClass());
    }

    @Test
    @DisplayName("Should return simple class name without type info")
    void shouldReturnSimpleName() {
      Container<String> container = new Container<>();
      assertEquals("Container", container.getClass().getSimpleName());
    }
  }

  @Nested
  @DisplayName("Reflection on Erased Types")
  class ReflectionTests {

    @Test
    @DisplayName("Should show Object as field type via reflection")
    void shouldShowObjectFieldType() throws NoSuchFieldException {
      Container<String> container = new Container<>();
      Field field = container.getClass().getDeclaredField("value");
      assertEquals(Object.class, field.getType());
    }

    @Test
    @DisplayName("Should show Object as return type via reflection")
    void shouldShowObjectReturnType() throws NoSuchMethodException {
      Container<String> container = new Container<>();
      Method method = container.getClass().getDeclaredMethod("getValue");
      assertEquals(Object.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should list all declared methods")
    void shouldListDeclaredMethods() {
      Container<String> container = new Container<>();
      Method[] methods = container.getClass().getDeclaredMethods();
      assertNotNull(methods);
      assertTrue(methods.length > 0);
    }
  }

  @Nested
  @DisplayName("List Type Erasure")
  class ListErasureTests {

    @Test
    @DisplayName("Should have same class for different generic lists")
    void shouldHaveSameListClass() {
      List<String> stringList = new ArrayList<>();
      List<Integer> intList = new ArrayList<>();
      assertEquals(stringList.getClass(), intList.getClass());
    }

    @Test
    @DisplayName("Should pass instanceof List check")
    void shouldPassInstanceofList() {
      List<String> stringList = new ArrayList<>();
      assertTrue(stringList instanceof List);
    }

    @Test
    @DisplayName("Should have same runtime type for all List instances")
    void shouldHaveSameRuntimeType() {
      List<String> stringList = new ArrayList<>();
      List<Double> doubleList = new ArrayList<>();
      assertNotEquals(List.class, stringList.getClass().getSimpleName());
      assertEquals("ArrayList", stringList.getClass().getSimpleName());
      assertEquals(stringList.getClass(), doubleList.getClass());
    }
  }
}
