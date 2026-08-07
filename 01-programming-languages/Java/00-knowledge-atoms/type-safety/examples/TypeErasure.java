import java.util.ArrayList;
import java.util.List;

public class TypeErasure {
    public static void main(String[] args) {
        // 1. Generics are erased at runtime
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();

        System.out.println("strings class: " + strings.getClass());
        System.out.println("integers class: " + integers.getClass());
        System.out.println("Same class? " + (strings.getClass() == integers.getClass())); // true

        // 2. Cannot use instanceof with parameterized types
        // if (strings instanceof List<String>) {} // Error

        // 3. Cannot create generic arrays
        // T[] array = new T[10]; // Error

        // 4. Workaround: pass Class object to preserve type info
        String created = createInstance(String.class);
        System.out.println("Created: " + created.getClass().getSimpleName());

        // 5. TypeToken workaround for complex generic types
        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        System.out.println("Type token type: " + token.getType());
    }

    // Workaround 1: Pass Class object
    static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance", e);
        }
    }

    // Workaround 2: TypeToken for generic type info
    static abstract class TypeToken<T> {
        private final java.lang.reflect.Type type;

        protected TypeToken() {
            java.lang.reflect.Type superClass = getClass().getGenericSuperclass();
            this.type = ((java.lang.reflect.ParameterizedType) superClass)
                    .getActualTypeArguments()[0];
        }

        java.lang.reflect.Type getType() { return type; }
    }
}
