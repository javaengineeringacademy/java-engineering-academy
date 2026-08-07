import java.util.ArrayList;
import java.util.List;

/**
 * Exercise: Generic Type Safety
 *
 * Task: Implement the generic Container class and its methods.
 * - The class should hold a single value of type T
 * - Implement getValue() and setValue() methods
 * - Implement isSameType() to check if another Container holds the same type
 */
public class Exercise2_Generics {
    public static void main(String[] args) {
        Container<String> strContainer = new Container<>("Hello");
        Container<Integer> intContainer = new Container<>(42);

        System.out.println("String value: " + strContainer.getValue());
        System.out.println("Integer value: " + intContainer.getValue());
        System.out.println("Same type? " + strContainer.isSameType(intContainer)); // false

        Container<String> anotherStr = new Container<>("World");
        System.out.println("Same type? " + strContainer.isSameType(anotherStr)); // true
    }

    /**
     * TODO: Implement the generic Container class
     */
    static class Container<T> {
        // Your code here

        // TODO: Constructor that takes a value of type T
        Container(T value) {
            // Your code here
        }

        // TODO: Return the stored value
        T getValue() {
            // Your code here
            return null;
        }

        // TODO: Set a new value
        void setValue(T value) {
            // Your code here
        }

        // TODO: Check if this container holds the same type as another
        boolean isSameType(Container<?> other) {
            // Your code here
            return false;
        }
    }
}
