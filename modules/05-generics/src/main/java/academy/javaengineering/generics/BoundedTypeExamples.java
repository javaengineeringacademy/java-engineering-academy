package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Number;

/**
 * Demonstrates bounded type parameters in Java generics.
 *
 * <p>Bounded type parameters allow you to restrict the types that can be
 * used as type arguments, enabling more precise type safety.</p>
 */
public class BoundedTypeExamples {

  /**
   * Upper bounded type parameter (extends).
   * Accepts Number or any subclass of Number.
   *
   * @param numbers a list of numbers
   * @return the sum as a double
   */
  public static double sumOfNumbers(List<? extends Number> numbers) {
    double sum = 0;
    for (Number number : numbers) {
      sum += number.doubleValue();
    }
    return sum;
  }

  /**
   * Upper bounded with multiple bounds.
   * T must implement both Comparable and have a no-arg constructor.
   *
   * @param list the list to find the minimum in
   * @param <T>  the type (must be Comparable)
   * @return the minimum element
   */
  public static <T extends Comparable<T>> T findMin(List<T> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException("List cannot be empty");
    }
    T min = list.getFirst();
    for (T element : list) {
      if (element.compareTo(min) < 0) {
        min = element;
      }
    }
    return min;
  }

  /**
   * Lower bounded type parameter (super).
   * Accepts Integer or any superclass of Integer.
   *
   * @param destination the destination list
   * @param source      the source list of Integers
   */
  public static void addNumbers(List<? super Integer> destination,
      List<Integer> source) {
    for (Integer number : source) {
      destination.add(number);
    }
  }

  /**
   * Class with an upper bounded type parameter.
   *
   * @param <T> the type (must extend Number)
   */
  public static class Statistics<T extends Number> {
    private final List<T> data = new ArrayList<>();

    /**
     * Adds a data point.
     *
     * @param value the value to add
     */
    public void add(T value) {
      data.add(value);
    }

    /**
     * Calculates the average of all data points.
     *
     * @return the average value
     */
    public double average() {
      return sumOfNumbers(data) / data.size();
    }
  }

  /**
   * Demonstrates bounded type usage.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Upper bounded - works with any Number subclass
    List<Integer> integers = List.of(1, 2, 3, 4, 5);
    List<Double> doubles = List.of(1.5, 2.5, 3.5);

    System.out.println("Sum of integers: " + sumOfNumbers(integers));
    // Expected: Sum of integers: 15.0

    System.out.println("Sum of doubles: " + sumOfNumbers(doubles));
    // Expected: Sum of doubles: 7.5

    // findMin usage
    System.out.println("Min integer: " + findMin(integers));
    // Expected: Min integer: 1

    List<String> words = List.of("cherry", "apple", "banana");
    System.out.println("Min string: " + findMin(words));
    // Expected: Min string: apple

    // Lower bounded - adding to a List<Number> from List<Integer>
    List<Number> numberList = new ArrayList<>();
    addNumbers(numberList, List.of(10, 20, 30));
    System.out.println("Number list after add: " + numberList);
    // Expected: Number list after add: [10, 20, 30]

    // Statistics usage
    Statistics<Integer> stats = new Statistics<>();
    stats.add(10);
    stats.add(20);
    stats.add(30);
    System.out.println("Average: " + stats.average());
    // Expected: Average: 20.0
  }
}
