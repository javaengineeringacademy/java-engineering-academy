package academy.javaengineering.generics;

import java.util.Objects;

/**
 * Demonstrates generic class implementations in Java.
 *
 * <p>Generic classes allow you to create classes that work with different
 * data types while providing compile-time type safety.</p>
 */
public class GenericClassExamples {

  /**
   * Simple generic class holding a single value.
   *
   * @param <T> the type of the stored value
   */
  public static class Box<T> {
    private T content;

    /**
     * Creates an empty box.
     */
    public Box() {
    }

    /**
     * Creates a box with the specified content.
     *
     * @param content the initial content
     */
    public Box(T content) {
      this.content = content;
    }

    /**
     * Gets the content of the box.
     *
     * @return the content
     */
    public T getContent() {
      return content;
    }

    /**
     * Sets the content of the box.
     *
     * @param content the new content
     */
    public void setContent(T content) {
      this.content = content;
    }

    @Override
    public String toString() {
      return "Box{content=" + content + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Box<?> box = (Box<?>) o;
      return Objects.equals(content, box.content);
    }

    @Override
    public int hashCode() {
      return Objects.hash(content);
    }
  }

  /**
   * Generic class with multiple type parameters.
   *
   * @param <K> the key type
   * @param <V> the value type
   */
  public static class Pair<K, V> {
    private final K key;
    private final V value;

    /**
     * Creates a key-value pair.
     *
     * @param key   the key
     * @param value the value
     */
    public Pair(K key, V value) {
      this.key = key;
      this.value = value;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public K getKey() {
      return key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public V getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "Pair{key=" + key + ", value=" + value + "}";
    }
  }

  /**
   * Generic class implementing an interface.
   *
   * @param <T> the type of elements in the container
   */
  public static class SimpleContainer<T> implements Container<T> {
    private T element;

    @Override
    public T get() {
      return element;
    }

    @Override
    public void set(T element) {
      this.element = element;
    }
  }

  /**
   * A generic interface for containers.
   *
   * @param <T> the type of elements
   */
  public interface Container<T> {
    T get();

    void set(T element);
  }

  /**
   * Generic class with a recursive type bound.
   *
   * @param <T> the type that must extend Comparable
   */
  public static class SortedBox<T extends Comparable<T>> {
    private final java.util.List<T> elements = new java.util.ArrayList<>();

    /**
     * Adds an element and maintains sorted order.
     *
     * @param element the element to add
     */
    public void add(T element) {
      elements.add(element);
      elements.sort(null);
    }

    /**
     * Gets all elements in sorted order.
     *
     * @return an unmodifiable list of sorted elements
     */
    public java.util.List<T> getSorted() {
      return java.util.List.copyOf(elements);
    }
  }

  /**
   * Demonstrates generic class usage.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Simple Box usage
    Box<String> stringBox = new Box<>("Hello Generics");
    System.out.println("String Box: " + stringBox);
    // Expected: String Box: Box{content=Hello Generics}

    Box<Integer> intBox = new Box<>(42);
    System.out.println("Integer Box: " + intBox);
    // Expected: Integer Box: Box{content=42}

    // Pair usage
    Pair<String, Integer> nameAge = new Pair<>("Alice", 30);
    System.out.println("Name-Age Pair: " + nameAge);
    // Expected: Name-Age Pair: Pair{key=Alice, value=30}

    // Container usage
    SimpleContainer<Double> container = new SimpleContainer<>();
    container.set(3.14);
    System.out.println("Container value: " + container.get());
    // Expected: Container value: 3.14

    // SortedBox usage
    SortedBox<String> sortedBox = new SortedBox<>();
    sortedBox.add("Banana");
    sortedBox.add("Apple");
    sortedBox.add("Cherry");
    System.out.println("Sorted elements: " + sortedBox.getSorted());
    // Expected: Sorted elements: [Apple, Banana, Cherry]
  }
}
