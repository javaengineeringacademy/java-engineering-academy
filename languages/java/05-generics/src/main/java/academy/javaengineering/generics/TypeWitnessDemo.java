package academy.javaengineering.generics;

import java.util.List;
import java.util.Map;

/**
 * Demonstrates type witness syntax in Java generics.
 *
 * <p>A type witness is an explicit type argument provided when calling a
 * generic method. It uses the syntax {@code <Type>method()} and is
 * needed when the compiler cannot infer the type automatically.</p>
 */
public class TypeWitnessDemo {

  /**
   * Generic method that returns the first element of a list.
   *
   * @param list the input list
   * @param <T>  the element type
   * @return the first element
   */
  public static <T> T getFirst(List<T> list) {
    return list == null || list.isEmpty() ? null : list.getFirst();
  }

  /**
   * Generic method with multiple type parameters.
   *
   * @param key   the key
   * @param value the value
   * @param <K>   the key type
   * @param <V>   the value type
   * @return a map entry
   */
  public static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
    return Map.entry(key, value);
  }

  /**
   * Generic method that creates a list of given elements.
   *
   * @param elements the elements
   * @param <T>      the element type
   * @return a new list
   */
  @SafeVarargs
  public static <T> List<T> listOf(T... elements) {
    return List.of(elements);
  }

  /**
   * Demonstrates when type witness is needed vs. type inference.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Case 1: Type inference works - no witness needed
    List<String> strings = List.of("a", "b", "c");
    String first = getFirst(strings);
    System.out.println("First string (inferred): " + first);
    // Expected: First string (inferred): a

    // Case 2: Type witness needed when compiler can't infer
    // Without witness: getFirst(null) - compiler error (ambiguous)
    String result = TypeWitnessDemo.<String>getFirst(null);
    System.out.println("First with witness: " + result);
    // Expected: First with witness: null

    // Case 3: Diamond operator vs explicit type witness
    // Diamond operator - type inferred from context
    List<Integer> numbers = List.of(1, 2, 3);
    Integer firstNum = getFirst(numbers);
    System.out.println("First number: " + firstNum);
    // Expected: First number: 1

    // Explicit witness on method call
    Integer secondNum = TypeWitnessDemo.<Integer>getFirst(List.of(10, 20));
    System.out.println("Second with witness: " + secondNum);
    // Expected: Second with witness: 10

    // Case 4: Multiple type parameters with witness
    Map.Entry<String, Integer> entry =
        TypeWitnessDemo.<String, Integer>entryOf("age", 30);
    System.out.println("Entry: " + entry);
    // Expected: Entry: age=30

    // Case 5: Varargs with type witness
    List<String> langs = TypeWitnessDemo.<String>listOf("Java", "Python");
    System.out.println("Languages: " + langs);
    // Expected: Languages: [Java, Python]
  }
}
