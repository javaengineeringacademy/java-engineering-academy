package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Demonstrates the generic builder pattern.
 *
 * <p>The builder pattern with generics allows for type-safe construction
 * of complex objects with fluent API support.</p>
 */
public class GenericBuilder {

  /**
   * Generic builder interface.
   *
   * @param <T> the type being built
   */
  public interface Builder<T> {
    T build();
  }

  /**
   * Fluent builder for creating products.
   *
   * @param <T> the product type
   */
  public static class ProductBuilder<T> implements Builder<T> {
    private final Factory<T> factory;
    private final List<Consumer<T>> configurations = new ArrayList<>();

    /**
     * Creates a builder with a factory.
     *
     * @param factory the factory to create the product
     */
    public ProductBuilder(Factory<T> factory) {
      this.factory = factory;
    }

    /**
     * Adds a configuration to be applied during build.
     *
     * @param config the configuration consumer
     * @return this builder
     */
    public ProductBuilder<T> configure(Consumer<T> config) {
      configurations.add(config);
      return this;
    }

    @Override
    public T build() {
      T product = factory.create();
      for (Consumer<T> config : configurations) {
        config.accept(product);
      }
      return product;
    }
  }

  /**
   * Factory interface for creating objects.
   *
   * @param <T> the object type
   */
  @FunctionalInterface
  public interface Factory<T> {
    T create();
  }

  /**
   * Simple product class for demonstration.
   */
  public static class Computer {
    private String cpu;
    private int ram;
    private int storage;
    private String gpu;

    /**
     * Sets the CPU.
     *
     * @param cpu the CPU model
     */
    public void setCpu(String cpu) {
      this.cpu = cpu;
    }

    /**
     * Sets the RAM.
     *
     * @param ram RAM in GB
     */
    public void setRam(int ram) {
      this.ram = ram;
    }

    /**
     * Sets the storage.
     *
     * @param storage storage in GB
     */
    public void setStorage(int storage) {
      this.storage = storage;
    }

    /**
     * Sets the GPU.
     *
     * @param gpu the GPU model
     */
    public void setGpu(String gpu) {
      this.gpu = gpu;
    }

    @Override
    public String toString() {
      return "Computer{cpu='" + cpu + "', ram=" + ram
          + "GB, storage=" + storage + "GB, gpu='" + gpu + "'}";
    }
  }

  /**
   * Generic builder for creating collections.
   *
   * @param <T> the element type
   */
  public static class CollectionBuilder<T> {
    private final List<T> elements = new ArrayList<>();

    /**
     * Adds an element.
     *
     * @param element the element to add
     * @return this builder
     */
    public CollectionBuilder<T> add(T element) {
      elements.add(element);
      return this;
    }

    /**
     * Adds multiple elements.
     *
     * @param newElements the elements to add
     * @return this builder
     */
    @SafeVarargs
    public final CollectionBuilder<T> addAll(T... newElements) {
      for (T element : newElements) {
        elements.add(element);
      }
      return this;
    }

    /**
     * Builds an unmodifiable list.
     *
     * @return the immutable list
     */
    public List<T> build() {
      return List.copyOf(elements);
    }
  }

  /**
   * Demonstrates generic builder usage.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Fluent builder pattern
    Computer laptop = new ProductBuilder<Computer>(Computer::new)
        .configure(c -> c.setCpu("Intel i7"))
        .configure(c -> c.setRam(16))
        .configure(c -> c.setStorage(512))
        .configure(c -> c.setGpu("NVIDIA RTX 3060"))
        .build();
    System.out.println("Laptop: " + laptop);
    // Expected: Laptop: Computer{cpu='Intel i7', ram=16GB,
    //   storage=512GB, gpu='NVIDIA RTX 3060'}

    // Collection builder
    List<String> languages = new CollectionBuilder<String>()
        .add("Java")
        .add("Python")
        .addAll("JavaScript", "TypeScript")
        .build();
    System.out.println("Languages: " + languages);
    // Expected: Languages: [Java, Python, JavaScript, TypeScript]

    // Builder with different types
    List<Integer> numbers = new CollectionBuilder<Integer>()
        .addAll(1, 2, 3, 4, 5)
        .build();
    System.out.println("Numbers: " + numbers);
    // Expected: Numbers: [1, 2, 3, 4, 5]
  }
}
