package academy.javaengineering.generics;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Topic 06: Type Erasure.
 *
 * <p>This class demonstrates type erasure, its implications,
 * and workarounds for accessing runtime type information.</p>
 */
public final class TypeErasureDemo {

    private TypeErasureDemo() {
    }

    /**
     * Demonstrates that generic types are erased at runtime.
     */
    public static void demonstrateErasure() {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();

        // Both are the same class at runtime
        System.out.println("Same class: " +
                (strings.getClass() == integers.getClass())); // true
        System.out.println("Class name: " + strings.getClass().getName());
    }

    /**
     * TypeToken pattern for runtime type information.
     */
    public abstract static class TypeToken<T> {
        private final Type type;

        protected TypeToken() {
            Type superclass = getClass().getGenericSuperclass();
            ParameterizedType pt = (ParameterizedType) superclass;
            type = pt.getActualTypeArguments()[0];
        }

        public Type getType() {
            return type;
        }

        @SuppressWarnings("unchecked")
        public Class<T> getRawType() {
            return (Class<T>) type;
        }
    }

    /**
     * Safe casting using class tokens.
     */
    @SuppressWarnings("unchecked")
    public static <T> T safeCast(Object obj, Class<T> type) {
        return type.cast(obj);
    }

    /**
     * Get generic type parameter from subclass.
     */
    public static Type getSuperclassTypeParameter(Class<?> subclass, int index) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType pt) {
            return pt.getActualTypeArguments()[index];
        }
        throw new IllegalArgumentException(
                subclass.getName() + " is not parameterized");
    }

    /**
     * Create array using reflection (workaround for generic arrays).
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> type, int size) {
        return (T[]) java.lang.reflect.Array.newInstance(type, size);
    }

    /**
     * Example class for type token testing.
     */
    public static class StringList extends ArrayList<String> {
    }

    /**
     * Example class for type parameter extraction.
     */
    public static class NumberList extends ArrayList<Number> {
    }

    /**
     * Demonstrates type erasure workarounds.
     */
    public static void main(String[] args) {
        // Basic erasure demonstration
        demonstrateErasure();

        // TypeToken usage
        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        System.out.println("Type: " + token.getType());
        System.out.println("Raw type: " + token.getRawType());

        // Safe casting
        Object obj = "hello";
        String str = safeCast(obj, String.class);
        System.out.println("Safe cast: " + str);

        // Get superclass type parameter
        Type type = getSuperclassTypeParameter(StringList.class, 0);
        System.out.println("StringList element type: " + type);

        Type numType = getSuperclassTypeParameter(NumberList.class, 0);
        System.out.println("NumberList element type: " + numType);

        // Create generic array
        String[] array = createArray(String.class, 5);
        System.out.println("Array length: " + array.length);
        System.out.println("Array class: " + array.getClass().getName());
    }
}
