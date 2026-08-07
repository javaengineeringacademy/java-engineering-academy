/**
 * Solution: Generic Type Safety
 */
public class Solution2_Generics {
    public static void main(String[] args) {
        Container<String> strContainer = new Container<>("Hello");
        Container<Integer> intContainer = new Container<>(42);

        System.out.println("String value: " + strContainer.getValue());
        System.out.println("Integer value: " + intContainer.getValue());
        System.out.println("Same type? " + strContainer.isSameType(intContainer)); // false

        Container<String> anotherStr = new Container<>("World");
        System.out.println("Same type? " + strContainer.isSameType(anotherStr)); // true
    }

    static class Container<T> {
        private T value;

        Container(T value) {
            this.value = value;
        }

        T getValue() {
            return value;
        }

        void setValue(T value) {
            this.value = value;
        }

        boolean isSameType(Container<?> other) {
            return this.getClass() == other.getClass() &&
                   this.value.getClass() == other.value.getClass();
        }
    }
}
