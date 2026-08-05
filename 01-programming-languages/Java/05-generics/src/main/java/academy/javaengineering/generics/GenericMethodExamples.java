package academy.javaengineering.generics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demonstrates generic methods in Java.
 *
 * <p>Generic methods are methods that introduce their own type parameters,
 * independent of the class's type parameters.</p>
 */
public class GenericMethodExamples {

  /**
   * Generic method to find the maximum element in a list.
   *
   * @param list   the list to search
   * @param <T>    the type of elements (must be Comparable)
   * @return the maximum element
   * @throws IllegalArgumentException if the list is empty
   */
  public static <T extends Comparable<T>> T findMax(List<T> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException("List cannot be empty");
    }
    T max = list.getFirst();
    for (T element : list) {
      if (element.compareTo(max) > 0) {
        max = element;
      }
    }
    return max;
  }

  /**
   * Generic method to swap elements in an array.
   *
   * @param array the array
   * @param i     the first index
   * @param j     the second index
   * @param <T>   the element type
   */
  public static <T> void swap(T[] array, int i, int j) {
    T temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Generic method to create an unmodifiable list of pairs from two lists.
   *
   * @param keys   the list of keys
   * @param values the list of values
   * @param <K>    the key type
   * @param <V>    the value type
   * @return a list of key-value pairs
   */
  public static <K, V> List<Map.Entry<K, V>> zipToEntries(
      List<K> keys, List<V> values) {
    int size = Math.min(keys.size(), values.size());
    var result = new java.util.ArrayList<Map.Entry<K, V>>();
    for (int i = 0; i < size; i++) {
      result.add(Map.entry(keys.get(i), values.get(i)));
    }
    return List.copyOf(result);
  }

  /**
   * Generic method that works with any number of arguments.
   *
   * @param elements the elements to join
   * @param <T>      the element type
   * @return a list containing all elements
   @SafeVarargs
  */
  public static <T> List<T> asList(T... elements) {
    return Arrays.asList(elements);
  }

  /**
   * Generic method using type inference with diamond operator.
   *
   * @param key   the map key
   * @param value the map value
   * @param <K>   the key type
   * @param <V>   the value type
   * @return a new Map entry
   */
  public static <K, V> Map.Entry<K, V> entry(K key, V value) {
    return Map.entry(key, value);
  }

  /**
   * Demonstrates generic method usage.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // findMax with different types
    List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);
    System.out.println("Max integer: " + findMax(numbers));
    // Expected: Max integer: 9

    List<String> words = List.of("banana", "apple", "cherry");
    System.out.println("Max string: " + findMax(words));
    // Expected: Max string: cherry

    // swap demonstration
    String[] names = {"Alice", "Bob", "Charlie"};
    System.out.println("Before swap: " + Arrays.toString(names));
    swap(names, 0, 2);
    System.out.println("After swap: " + Arrays.toString(names));
    // Expected: Before swap: [Alice, Bob, Charlie]
    // Expected: After swap: [Charlie, Bob, Alice]

    // asList with varargs
    List<Integer> numberList = asList(1, 2, 3, 4, 5);
    System.out.println("Number list: " + numberList);
    // Expected: Number list: [1, 2, 3, 4, 5]

    // zipToEntries
    List<String> keys = List.of("a", "b", "c");
    List<Integer> values = List.of(1, 2, 3);
    var entries = zipToEntries(keys, values);
    System.out.println("Zipped entries: " + entries);
    // Expected: Zipped entries: [a=1, b=2, c=3]
  }
}
