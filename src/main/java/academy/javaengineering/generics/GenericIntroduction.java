package academy.javaengineering.generics;

/**
 * Topic 01: Introduction to Generics.
 *
 * <p>This class demonstrates the fundamentals of Java generics,
 * including type safety, type inference, and the diamond operator.</p>
 *
 * @param <T> the type of the contained value
 */
public class GenericIntroduction<T> {

    private T content;

    public GenericIntroduction() {
    }

    public GenericIntroduction(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "GenericIntroduction[" + content + "]";
    }

    /**
     * Demonstrates basic generic usage.
     */
    public static void main(String[] args) {
        // Type inference with diamond operator
        GenericIntroduction<String> stringBox = new GenericIntroduction<>("Hello Generics");
        GenericIntroduction<Integer> intBox = new GenericIntroduction<>(42);

        System.out.println(stringBox);
        System.out.println(intBox);

        // Compile-time type safety
        // intBox.setContent("wrong"); // Compile error!

        // Type inference in method calls
        String value = stringBox.getContent(); // No cast needed
        System.out.println("Value: " + value);

        // Heterogeneous container pattern
        HeterogeneousContainer container = new HeterogeneousContainer();
        container.put("name", "Alice");
        container.put("age", 30);
        container.put("active", true);

        String name = container.get("name", String.class);
        int age = container.get("age", Integer.class);
        boolean active = container.get("active", Boolean.class);

        System.out.printf("Name: %s, Age: %d, Active: %b%n", name, age, active);
    }

    /**
     * Type-safe heterogeneous container using class tokens.
     */
    static class HeterogeneousContainer {
        private final java.util.Map<String, Object> map = new java.util.HashMap<>();

        public <T> void put(String key, T value) {
            map.put(key, value);
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key, Class<T> type) {
            return type.cast(map.get(key));
        }
    }
}
