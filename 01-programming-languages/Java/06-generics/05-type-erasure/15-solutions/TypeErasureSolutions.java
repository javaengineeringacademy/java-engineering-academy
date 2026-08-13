package academy.javaengineering.generics.type-erasure.solutions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Type Erasure Solutions - Complete implementations for all exercises.
 */
public class TypeErasureSolutions {

    static class Box<T> {
        private T value;

        public Box(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    static class StringBox extends Box<String> {
        public StringBox(String value) {
            super(value);
        }

        @Override
        public void setValue(String value) {
            super.setValue(value.toUpperCase());
        }
    }

    // Exercise 1: Type Erasure at Runtime
    public static void exercise1() {
        System.out.println("Exercise 1: Type Erasure at Runtime");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);

        System.out.println("stringBox class: " + stringBox.getClass());
        System.out.println("intBox class: " + intBox.getClass());
        System.out.println("Same class? " + (stringBox.getClass() == intBox.getClass()));
        // Both are academy.javaengineering.generics.type-erasure.solutions.TypeErasureSolutions$Box
    }

    // Exercise 2: instanceof with Generics
    public static void exercise2() {
        System.out.println("\nExercise 2: instanceof with Generics");
        Box<String> box = new Box<>("Test");

        // Cannot use: if (box instanceof Box<String>) { }
        // Compile error: Cannot perform instanceof check against parameterized type

        // Safe alternatives:
        if (box instanceof Box) {
            System.out.println("box is a Box (raw type check)");
        }

        // For type checking at runtime, use Class objects
        System.out.println("Box class: " + Box.class);
    }

    // Exercise 3: Bridge Methods
    public static void exercise3() {
        System.out.println("\nExercise 3: Bridge Methods");
        StringBox stringBox = new StringBox("Hello");

        System.out.println("Methods in StringBox:");
        for (Method method : StringBox.class.getDeclaredMethods()) {
            System.out.println("  " + method);
        }

        System.out.println("\nAll methods including inherited:");
        for (Method method : StringBox.class.getMethods()) {
            if (method.getDeclaringClass() == StringBox.class) {
                System.out.println("  " + method + (method.isBridge() ? " [BRIDGE]" : ""));
            }
        }

        // Bridge method is: public void Box.setValue(Object) -> calls StringBox.setValue(String)
    }

    // Exercise 4: Generic Arrays
    public static void exercise4() {
        System.out.println("\nExercise 4: Generic Arrays");

        // Cannot create: Box<String>[] boxes = new Box<String>[10];
        // Compile error: Cannot create a generic array of Box<String>

        // Raw type approach (unsafe - bypasses type checking):
        @SuppressWarnings("unchecked")
        Box<String>[] rawBoxes = (Box<String>[]) new Box[10];
        System.out.println("Created raw Box array (unsafe)");

        // Safe approach using List:
        List<Box<String>> safeBoxes = new ArrayList<>();
        safeBoxes.add(new Box<>("Hello"));
        System.out.println("Safe approach using List: " + safeBoxes);
    }

    // Exercise 5: Bounded Type Erasure
    static <T extends Comparable<T>> void sort(List<T> list) {
        // T is replaced by Comparable at runtime
    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Bounded Type Erasure");
        try {
            Method sortMethod = TypeErasureSolutions.class.getDeclaredMethod("sort", List.class);
            System.out.println("Sort method parameter type: " + sortMethod.getParameterTypes()[0]);
            // Prints: java.util.List (not List<Comparable>)
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Type Erasure Solutions ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
