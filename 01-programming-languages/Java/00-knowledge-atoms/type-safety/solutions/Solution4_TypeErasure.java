import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Solution: Type Erasure Workarounds
 */
public class Solution4_TypeErasure {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        System.out.println("Type erased: " + (strings.getClass() == integers.getClass()));

        String str = createInstance(String.class);
        System.out.println("Created: " + str.getClass().getSimpleName());

        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        System.out.println("Captured type: " + token.getType());
    }

    static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance", e);
        }
    }

    static abstract class TypeToken<T> {
        private final Type type;

        protected TypeToken() {
            Type superClass = getClass().getGenericSuperclass();
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }

        Type getType() {
            return type;
        }
    }
}
