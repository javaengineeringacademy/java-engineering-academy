package academy.javaengineering.generics.erasure-generic-types.exercises;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Erasure of Generic Types Exercises
 * Understand how type erasure affects generic types.
 */
public class ErasureTypesExercises {

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

    // Exercise 1: Show type parameter erasure
    // TODO: Demonstrate that T is erased to Object or bound
    public static void exercise1() {
        System.out.println("Exercise 1: Type Parameter Erasure");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);

        // TODO: Compare classes
        System.out.println("stringBox class: " + stringBox.getClass());
        System.out.println("intBox class: " + intBox.getClass());
        System.out.println("Same class? " + (stringBox.getClass() == intBox.getClass()));

        // TODO: Show type parameter info
        TypeVariable<?>[] typeParams = Box.class.getTypeParameters();
        for (TypeVariable<?> param : typeParams) {
            System.out.println("Type parameter: " + param.getName());
            System.out.println("Bounds: " + java.util.Arrays.toString(param.getBounds()));
        }
    }

    // Exercise 2: Show field type erasure
    // TODO: Demonstrate how field types are erased
    public static void exercise2() {
        System.out.println("\nExercise 2: Field Type Erasure");
        // TODO: Use reflection to show field types
        for (Field field : Box.class.getDeclaredFields()) {
            System.out.println("Field: " + field.getName());
            System.out.println("Type: " + field.getType());
        }
    }

    // Exercise 3: Show method type erasure
    // TODO: Demonstrate how method types are erased
    public static void exercise3() {
        System.out.println("\nExercise 3: Method Type Erasure");
        // TODO: Use reflection to show method signatures
        for (Method method : Box.class.getDeclaredMethods()) {
            System.out.println("Method: " + method.getName());
            System.out.println("Return type: " + method.getReturnType());
            System.out.println("Parameter types: " + java.util.Arrays.toString(method.getParameterTypes()));
        }
    }

    // Exercise 4: Show bridge method generation
    // TODO: Demonstrate bridge methods in subclass
    public static void exercise4() {
        System.out.println("\nExercise 4: Bridge Methods");
        // TODO: Show all methods in NamedBox
        for (Method method : NamedBox.class.getDeclaredMethods()) {
            System.out.println("Method: " + method);
        }
        for (Method method : NamedBox.class.getMethods()) {
            if (method.getDeclaringClass() == NamedBox.class) {
                System.out.println("Inherited: " + method + (method.isBridge() ? " [BRIDGE]" : ""));
            }
        }
    }

    // Exercise 5: Show that instanceof works with erased types
    // TODO: Demonstrate type checking at runtime
    public static void exercise5() {
        System.out.println("\nExercise 5: Runtime Type Checking");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);

        // TODO: Show instanceof checks
        System.out.println("stringBox instanceof Box: " + (stringBox instanceof Box));
        // System.out.println("stringBox instanceof Box<String>: " + (stringBox instanceof Box<String>));

        // TODO: Show getClass() comparison
        System.out.println("stringBox.getClass() == intBox.getClass(): " + (stringBox.getClass() == intBox.getClass()));
    }

    public static void main(String[] args) {
        System.out.println("=== Erasure of Generic Types Exercises ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
