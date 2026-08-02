package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates wildcard behavior and the PECS principle.
 *
 * <p>PECS stands for Producer Extends, Consumer Super. This principle
 * helps determine when to use upper bounds (extends) vs lower bounds (super).</p>
 */
public class WildcardExamples {

  /**
   * Unbounded wildcard - accepts any type.
   * Only allows reading (returns Object).
   *
   * @param list a list of any type
   * @return the string representation of the list size
   */
  public static String getSize(List<?> list) {
    return "Size: " + list.size();
  }

  /**
   * Upper bounded wildcard (extends) - Producer.
   * Can read but not write (except null).
   *
   * @param numbers a list of Number subclasses
   * @return the sum
   */
  public static double sum(List<? extends Number> numbers) {
    double total = 0;
    for (Number num : numbers) {
      total += num.doubleValue();
    }
    return total;
  }

  /**
   * Lower bounded wildcard (super) - Consumer.
   * Can write but reading returns Object.
   *
   * @param dest   the destination list
   * @param source the source list to copy from
   * @param <T>    the element type
   */
  public static <T> void copy(List<? super T> dest, List<? extends T> source) {
    for (T element : source) {
      dest.add(element);
    }
  }

  /**
   * Demonstrates PECS with a practical example.
   * This method accepts a list that produces Integer values.
   *
   * @param numbers a list that produces Integers
   * @return a list that consumes Integers
   */
  public static List<Number> collectNumbers(List<? extends Integer> numbers) {
    List<Number> result = new ArrayList<>();
    for (Integer num : numbers) {
      result.add(num);
    }
    return result;
  }

  /**
   * Demonstrates wildcard usage.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Unbounded wildcard
    List<String> strings = List.of("a", "b", "c");
    List<Integer> numbers = List.of(1, 2, 3);
    System.out.println(getSize(strings));
    // Expected: Size: 3
    System.out.println(getSize(numbers));
    // Expected: Size: 3

    // Upper bounded wildcard (Producer)
    List<Integer> ints = List.of(1, 2, 3);
    List<Double> doubles = List.of(1.5, 2.5, 3.5);
    System.out.println("Sum of ints: " + sum(ints));
    // Expected: Sum of ints: 6.0
    System.out.println("Sum of doubles: " + sum(doubles));
    // Expected: Sum of doubles: 7.5

    // Lower bounded wildcard (Consumer)
    List<Number> numberList = new ArrayList<>();
    List<Integer> intList = List.of(10, 20, 30);
    copy(numberList, intList);
    System.out.println("Copied numbers: " + numberList);
    // Expected: Copied numbers: [10, 20, 30]

    // PECS example
    List<Integer> intSource = List.of(1, 2, 3);
    List<Number> collected = collectNumbers(intSource);
    System.out.println("Collected numbers: " + collected);
    // Expected: Collected numbers: [1, 2, 3]
  }
}
