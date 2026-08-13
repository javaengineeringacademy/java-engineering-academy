package academy.javaengineering.generics.examples;

import java.util.HashMap;
import java.util.Map;

/**
 * Type-Safe Heterogeneous Container example.
 *
 * <p>This example demonstrates a container that stores different types
 * in a type-safe manner using Class objects as keys.</p>
 */
public class TypeSafeContainerExample {

    /**
     * Type-safe heterogeneous container.
     * Uses Class objects as keys to store and retrieve values of different types.
     */
    public static class TypeSafeContainer {
        private final Map<Class<?>, Object> map = new HashMap<>();

        /**
         * Store a value with its type as key.
         *
         * @param <T>   the type of the value
         * @param type  the Class object representing the type
         * @param value the value to store
         */
        public <T> void put(Class<T> type, T value) {
            map.put(type, type.cast(value));
        }

        /**
         * Retrieve a value by its type.
         *
         * @param <T>  the type of the value
         * @param type the Class object representing the type
         * @return the value, or null if not found
         */
        @SuppressWarnings("unchecked")
        public <T> T get(Class<T> type) {
            return type.cast(map.get(type));
        }

        /**
         * Check if a type is stored.
         *
         * @param type the Class object representing the type
         * @return true if the type is stored
         */
        public boolean contains(Class<?> type) {
            return map.containsKey(type);
        }
    }

    public static void main(String[] args) {
        TypeSafeContainer container = new TypeSafeContainer();

        container.put(String.class, "Hello, World!");
        container.put(Integer.class, 42);
        container.put(Boolean.class, true);

        String str = container.get(String.class);
        Integer num = container.get(Integer.class);
        Boolean flag = container.get(Boolean.class);

        System.out.println("String: " + str);
        System.out.println("Integer: " + num);
        System.out.println("Boolean: " + flag);
        System.out.println("Contains String: " + container.contains(String.class));
    }
}
