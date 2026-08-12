package academy.javaengineering.generics.restrictions-generics.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Restrictions on Generics Solutions - Complete implementations for all exercises.
 */
public class RestrictionsSolutions {

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    // Exercise 1: Primitive Type Restrictions
    public static void exercise1() {
        System.out.println("Exercise 1: Primitive Type Restrictions");

        // Cannot use primitives:
        // Box<int> intBox = new Box<>(42);      // COMPILE ERROR
        // Box<double> doubleBox = new Box<>(3.14);  // COMPILE ERROR

        // Must use wrapper classes:
        Box<Integer> integerBox = new Box<>(42);
        Box<Double> doubleBox = new Box<>(3.14);
        Box<Character> charBox = new Box>('A');
        Box<Boolean> boolBox = new Box<>(true);

        System.out.println("Integer: " + integerBox.getValue());
        System.out.println("Double: " + doubleBox.getValue());
        System.out.println("Character: " + charBox.getValue());
        System.out.println("Boolean: " + boolBox.getValue());
    }

    // Exercise 2: Cannot Create Instances
    public static <T> T createInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        // Cannot do: return new T();  // COMPILE ERROR
        // Must use Class object
        return clazz.newInstance();
    }

    public static void exercise2() {
        System.out.println("\nExercise 2: Cannot Create Instances");
        try {
            String str = createInstance(String.class);
            System.out.println("Created String instance: " + str.getClass());
            Integer num = createInstance(Integer.class);
            System.out.println("Created Integer instance: " + num.getClass());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Why: T is erased at runtime, JVM doesn't know the constructor");
    }

    // Exercise 3: Cannot Create Generic Arrays
    public static void exercise3() {
        System.out.println("\nExercise 3: Cannot Create Generic Arrays");

        // Cannot create generic arrays:
        // Box<String>[] boxes = new Box<String>[10];  // COMPILE ERROR
        // T[] array = new T[10];                      // COMPILE ERROR

        // Workaround 1: Raw type and cast (unsafe)
        @SuppressWarnings("unchecked")
        Box<String>[] boxes = (Box<String>[]) new Box[10];
        boxes[0] = new Box<>("Hello");
        System.out.println("Raw type approach: " + boxes[0].getValue());

        // Workaround 2: Use List (safe and preferred)
        List<Box<String>> list = new ArrayList<>();
        list.add(new Box<>("Safe"));
        System.out.println("List approach: " + list.get(0).getValue());
    }

    // Exercise 4: Cannot Use instanceof
    public static void exercise4() {
        System.out.println("\nExercise 4: Cannot Use instanceof");
        Box<String> box = new Box<>("Hello");

        // Cannot do: if (box instanceof Box<String>) { }  // COMPILE ERROR
        // Because type parameter is erased at runtime

        // What you can do:
        if (box instanceof Box) {
            System.out.println("box is a Box (raw type check)");
            // Can cast to raw type
            Box rawBox = box;
            System.out.println("Raw box value: " + rawBox.getValue());
        }
    }

    // Exercise 5: Cannot Use Static Context
    static class GenericContainer<T> {
        // Cannot do: private static T value;  // COMPILE ERROR
        // Why: Type parameters are per-instance, static members are per-class

        // What you can do:
        private static Object staticValue;
        private T instanceValue;

        public GenericContainer(T value) {
            this.instanceValue = value;
        }

        public T getValue() { return instanceValue; }
        public static Object getStaticValue() { return staticValue; }
        public static void setStaticValue(Object value) { staticValue = value; }
    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Cannot Use Static Context");
        GenericContainer<String> c1 = new GenericContainer<>("First");
        GenericContainer<Integer> c2 = new GenericContainer<>(42);

        // Static members are shared across all parameterizations
        GenericContainer.setStaticValue("Shared");
        System.out.println("c1 value: " + c1.getValue());
        System.out.println("c2 value: " + c2.getValue());
        System.out.println("Static value: " + GenericContainer.getStaticValue());
    }

    public static void main(String[] args) {
        System.out.println("=== Restrictions on Generics Solutions ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
