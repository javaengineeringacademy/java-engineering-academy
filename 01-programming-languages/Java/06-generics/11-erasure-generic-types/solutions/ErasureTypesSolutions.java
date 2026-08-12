package academy.javaengineering.generics.erasure-generic-types.solutions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * Erasure of Generic Types Solutions - Complete implementations for all exercises.
 */
public class ErasureTypesSolutions {

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
        public void setValue(T value) { this.value = value; }
    }

    static class NamedBox<T extends Comparable<T>> extends Box<T> {
        private String name;
        public NamedBox(T value, String name) {
            super(value);
            this.name = name;
        }
        public String getName() { return name; }
    }

    // Exercise 1: Type Parameter Erasure
    public static void exercise1() {
        System.out.println("Exercise 1: Type Parameter Erasure");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);

        System.out.println("stringBox class: " + stringBox.getClass());
        System.out.println("intBox class: " + intBox.getClass());
        System.out.println("Same class? " + (stringBox.getClass() == intBox.getClass()));
        System.out.println("Both are: Box (type parameter erased to Object)");

        // Type parameter info
        TypeVariable<?>[] typeParams = Box.class.getTypeParameters();
        for (TypeVariable<?> param : typeParams) {
            System.out.println("Type parameter: " + param.getName());
            System.out.println("Bounds: " + Arrays.toString(param.getBounds()));
        }
    }

    // Exercise 2: Field Type Erasure
    public static void exercise2() {
        System.out.println("\nExercise 2: Field Type Erasure");
        for (Field field : Box.class.getDeclaredFields()) {
            System.out.println("Field: " + field.getName());
            System.out.println("  Declared type: " + field.getType());
            System.out.println("  Generic type: " + field.getGenericType());
        }
        System.out.println("Field 'value' has type Object (T erased to Object)");
    }

    // Exercise 3: Method Type Erasure
    public static void exercise3() {
        System.out.println("\nExercise 3: Method Type Erasure");
        for (Method method : Box.class.getDeclaredMethods()) {
            System.out.println("Method: " + method.getName());
            System.out.println("  Return type: " + method.getReturnType());
            System.out.println("  Parameter types: " + Arrays.toString(method.getParameterTypes()));
        }
        System.out.println("Methods use Object instead of T");
    }

    // Exercise 4: Bridge Methods
    public static void exercise4() {
        System.out.println("\nExercise 4: Bridge Methods");
        System.out.println("NamedBox declared methods:");
        for (Method method : NamedBox.class.getDeclaredMethods()) {
            System.out.println("  " + method);
        }

        System.out.println("\nNamedBox all methods (including inherited):");
        for (Method method : NamedBox.class.getMethods()) {
            if (method.getDeclaringClass() == NamedBox.class) {
                String bridge = method.isBridge() ? " [BRIDGE]" : "";
                System.out.println("  " + method + bridge);
            }
        }
        System.out.println("Bridge method: public void Box.setValue(Object) -> calls NamedBox.setValue(Comparable)");
    }

    // Exercise 5: Runtime Type Checking
    public static void exercise5() {
        System.out.println("\nExercise 5: Runtime Type Checking");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);

        System.out.println("stringBox instanceof Box: " + (stringBox instanceof Box));
        // System.out.println("stringBox instanceof Box<String>: COMPILE ERROR");
        System.out.println("Cannot use instanceof with parameterized types");

        System.out.println("stringBox.getClass() == intBox.getClass(): " + (stringBox.getClass() == intBox.getClass()));
        System.out.println("Both are same class at runtime (type erased)");
    }

    public static void main(String[] args) {
        System.out.println("=== Erasure of Generic Types Solutions ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
