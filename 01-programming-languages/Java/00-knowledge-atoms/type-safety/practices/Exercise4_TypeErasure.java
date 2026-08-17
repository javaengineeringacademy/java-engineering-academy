import java.util.ArrayList;
import java.util.List;

/**
 * Exercise: Type Erasure Workarounds
 *
 * Task: Demonstrate understanding of type erasure and implement workarounds.
 * - Show that generic types are erased at runtime
 * - Implement a method that preserves type info using Class parameter
 * - Create a simple TypeToken to capture generic type information
 */
public class Exercise4_TypeErasure {
    public static void main(String[] args) {
        // Task 1: Prove type erasure
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        System.out.println("Type erased: " + (strings.getClass() == integers.getClass()));

        // Task 2: Create instance using Class parameter
        String str = createInstance(String.class);
        System.out.println("Created: " + str.getClass().getSimpleName());

        // Task 3: Use TypeToken
        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        System.out.println("Captured type: " + token.getType());
    }

    /**
     * TODO: Implement method that creates an instance using Class parameter
     * This preserves type information that is erased at runtime
     */
    static <T> T createInstance(Class<T> clazz) {
        // Your code here - use clazz.getDeclaredConstructor().newInstance()
        return null;
    }

    /**
     * TODO: Implement a simple TypeToken
     * Should capture the generic type parameter using reflection
     */
    static abstract class TypeToken<T> {
        private final java.lang.reflect.Type type;

        protected TypeToken() {
            // Your code here - get generic superclass and extract type argument
            this.type = null;
        }

        java.lang.reflect.Type getType() {
            return type;
        }
    }
}
