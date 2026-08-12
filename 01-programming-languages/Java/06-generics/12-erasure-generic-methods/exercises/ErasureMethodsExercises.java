package academy.javaengineering.generics.erasure-generic-methods.exercises;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

/**
 * Erasure of Generic Methods Exercises
 * Understand how type erasure affects generic methods.
 */
public class ErasureMethodsExercises {

    // Exercise 1: Show type parameter erasure in methods
    // TODO: Demonstrate that method type parameters are erased
    public static <T> T identity(T value) {
        return value;
    }

    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static void exercise1() {
        System.out.println("Exercise 1: Method Type Parameter Erasure");
        // TODO: Use reflection to show method signatures
        try {
            Method identityMethod = ErasureMethodsExercises.class.getDeclaredMethod("identity", Object.class);
            System.out.println("identity method:");
            System.out.println("  Return type: " + identityMethod.getReturnType());
            System.out.println("  Parameter types: " + Arrays.toString(identityMethod.getParameterTypes()));

            TypeVariable<?>[] typeParams = identityMethod.getTypeParameters();
            for (TypeVariable<?> param : typeParams) {
                System.out.println("  Type parameter: " + param.getName());
                System.out.println("  Bounds: " + Arrays.toString(param.getBounds()));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    // Exercise 2: Show method type erasure with bounded types
    // TODO: Demonstrate bounded type erasure
    public static <T extends Number> double sum(List<T> list) {
        double total = 0;
        for (T num : list) {
            total += num.doubleValue();
        }
        return total;
    }

    public static void exercise2() {
        System.out.println("\nExercise 2: Bounded Type Erasure");
        // TODO: Show that T is replaced by Number
        try {
            Method sumMethod = ErasureMethodsExercises.class.getDeclaredMethod("sum", List.class);
            System.out.println("sum method:");
            System.out.println("  Parameter type: " + sumMethod.getParameterTypes()[0]);
            TypeVariable<?>[] typeParams = sumMethod.getTypeParameters();
            for (TypeVariable<?> param : typeParams) {
                System.out.println("  Type parameter bounds: " + Arrays.toString(param.getBounds()));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    // Exercise 3: Show bridge method generation in generic methods
    // TODO: Demonstrate bridge methods from overrides
    static abstract class Processor<T> {
        public abstract T process(T input);
    }

    static class StringProcessor extends Processor<String> {
        @Override
        public String process(String input) {
            return input.toUpperCase();
        }
    }

    public static void exercise3() {
        System.out.println("\nExercise 3: Bridge Methods from Overrides");
        // TODO: Show bridge method in StringProcessor
        for (Method method : StringProcessor.class.getDeclaredMethods()) {
            System.out.println("Declared: " + method + (method.isBridge() ? " [BRIDGE]" : ""));
        }
    }

    // Exercise 4: Show type erasure with multiple bounds
    // TODO: Demonstrate multiple bound erasure
    public static <T extends Comparable<T> & java.io.Serializable> T findMin(List<T> list) {
        T min = list.get(0);
        for (T item : list) {
            if (item.compareTo(min) < 0) {
                min = item;
            }
        }
        return min;
    }

    public static void exercise4() {
        System.out.println("\nExercise 4: Multiple Bound Erasure");
        // TODO: Show that T is replaced by first bound (Comparable)
        try {
            Method findMinMethod = ErasureMethodsExercises.class.getDeclaredMethod("findMin", List.class);
            System.out.println("findMin method:");
            System.out.println("  Parameter type: " + findMinMethod.getParameterTypes()[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    // Exercise 5: Show runtime type checking with methods
    // TODO: Demonstrate type checking limitations
    public static <T> void checkType(T value, Class<T> expectedType) {
        // TODO: Show how to check types at runtime
    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Runtime Type Checking");
        // TODO: Show that you cannot check generic type at runtime
        String str = "Hello";
        // if (str instanceof T) { }  // COMPILE ERROR

        // Alternative: pass Class object
        Class<?> clazz = str.getClass();
        System.out.println("Runtime class: " + clazz);
        System.out.println("Cannot use instanceof with type parameter");
    }

    public static void main(String[] args) {
        System.out.println("=== Erasure of Generic Methods Exercises ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
