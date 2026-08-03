package academy.javaengineering.generics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates type erasure in Java generics.
 *
 * <p>Type erasure means that generic type information is removed at compile time.
 * This has several implications for runtime behavior, reflection, and array creation.</p>
 */
public class TypeErasureDemo {

  /**
   * Generic class to demonstrate type erasure.
   *
   * @param <T> the type parameter
   */
  public static class Container<T> {
    private T value;

    /**
     * Gets the value.
     *
     * @return the value
     */
    public T getValue() {
      return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(T value) {
      this.value = value;
    }
  }

  /**
   * Demonstrates that generic type information is erased at runtime.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Type erasure - runtime type is raw
    Container<String> stringContainer = new Container<>();
    Container<Integer> intContainer = new Container<>();

    System.out.println("String container class: "
        + stringContainer.getClass().getSimpleName());
    // Expected: String container class: Container

    System.out.println("Integer container class: "
        + intContainer.getClass().getSimpleName());
    // Expected: Integer container class: Container

    // Both have the same runtime class
    System.out.println("Same class? "
        + (stringContainer.getClass() == intContainer.getClass()));
    // Expected: Same class? true

    // Reflection shows erased types
    Class<?> containerClass = stringContainer.getClass();
    System.out.println("\n--- Reflection on Container ---");

    Field[] fields = containerClass.getDeclaredFields();
    for (Field field : fields) {
      System.out.println("Field: " + field.getName()
          + ", Type: " + field.getType().getSimpleName());
      // Expected: Field: value, Type: Object
    }

    Method[] methods = containerClass.getDeclaredMethods();
    for (Method method : methods) {
      System.out.println("Method: " + method.getName()
          + ", Return: " + method.getReturnType().getSimpleName());
    }

    // Array creation limitation
    System.out.println("\n--- Array Creation ---");
    try {
      // This would cause a compile error:
      // T[] array = (T[]) new Object[10];

      // Workaround using Object array
      Object[] objectArray = new Object[10];
      System.out.println("Object array created successfully");
      // Expected: Object array created successfully
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }

    // instanceof with generics
    System.out.println("\n--- instanceof with Generics ---");
    List<String> stringList = new ArrayList<>();
    List<Integer> intList = new ArrayList<>();

    // Cannot do: stringList instanceof List<String>
    // But can do: stringList instanceof List
    System.out.println("stringList is List? " + (stringList instanceof List));
    // Expected: stringList is List? true
    System.out.println("intList is List? " + (intList instanceof List));
    // Expected: intList is List? true

    // Both are just ArrayList at runtime
    System.out.println("Same list type? "
        + (stringList.getClass() == intList.getClass()));
    // Expected: Same list type? true
  }
}
