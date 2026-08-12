package academy.javaengineering.generics.erasure-generic-methods.solutions;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

/**
 * Erasure of Generic Methods Solutions - Complete implementations for all exercises.
 */
public class ErasureMethodsSolutions {

    // Exercise 1: Method Type Parameter Erasure
    public static <T> T identity(T value) {
        return value;
    }

    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static void exercise1() {
        System.out.println("Exercise 1: Method Type Parameter Erasure");
        try {
            Method identityMethod = ErasureMethodsSolutions.class.getDeclaredMethod("identity", Object.class);
            System.out.println("identity method:");
            System.out.println("  Return type: " + identityMethod.getReturnType());
            System.out.println("  Parameter types: " + Arrays.toString(identityMethod.getParameterTypes()));

            TypeVariable<?>[] typeParams = identityMethod.getTypeParameters();
            for (TypeVariable<?> param : typeParams) {
                System.out.println("  Type parameter: " + param.getName());
                System.out.println("  Bounds: " + Arrays.toString(param.getBounds()));
            }
            System.out.println("  T is erased to Object (no bounds)");

            Method maxMethod = ErasureMethodsSolutions.class.getDeclaredMethod("max", Comparable.class, Comparable.class);
            System.out.println("\nmax method:");
            System.out.println("  Parameter types: " + Arrays.toString(maxMethod.getParameterTypes()));
            System.out.println("  T is erased to Comparable (first bound)");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    // Exercise 2: Bounded Type Erasure
    public static <T extends Number> double sum(List<T> list) {
        double total = 0;
        for (T num : list) {
            total += num.doubleValue();
        }
        return total;
    }

    public static void exercise2() {
        System.out.println("\nExercise 2: Bounded Type Erasure");
        try {
            Method sumMethod = ErasureMethodsSolutions.class.getDeclaredMethod("sum", List.class);
            System.out.println("sum method:");
            System.out.println("  Parameter type: " + sumMethod.getParameterTypes()[0]);
            TypeVariable<?>[] typeParams = sumMethod.getTypeParameters();
            for (TypeVariable<?> param : typeParams) {
                System.out.println("  Type parameter bounds: " + Arrays.toString(param.getBounds()));
            }
            System.out.println("  T is erased to Number (the bound)");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    // Exercise 3: Bridge Methods from Overrides
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
        System.out.println("StringProcessor methods:");
        for (Method method : StringProcessor.class.getDeclaredMethods()) {
            String bridge = method.isBridge() ? " [BRIDGE]" : "";
            System.out.println("  Declared: " + method + bridge);
        }
        System.out.println("\nBridge method: public Object process(Object) calls String process(String)");
        System.out.println("Generated to maintain polymorphism after type erasure");
    }

    // Exercise 4: Multiple Bound Erasure
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
        try {
            Method findMinMethod = ErasureMethodsSolutions.class.getDeclaredMethod("findMin", List.class);
            System.out.println("findMin method:");
            System.out.println("  Parameter type: " + findMinMethod.getParameterTypes()[0]);
            TypeVariable<?>[] typeParams = findMinMethod.getTypeParameters();
            for (TypeVariable<?> param : typeParams) {
                System.out.println("  Type parameter bounds: " + Arrays.toString(param.getBounds()));
            }
            System.out.println("  T is erased to Comparable (first bound)");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    // Exercise 5: Runtime Type Checking
    public static <T> void checkType(T value, Class<T> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException("Expected " + expectedType.getName());
        }
        System.out.println("Type check passed: " + value);
    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Runtime Type Checking");
        String str = "Hello";
        // if (str instanceof T) { }  // COMPILE ERROR

        // Safe alternatives:
        Class<?> clazz = str.getClass();
        System.out.println("Runtime class: " + clazz);

        // Pass Class object for type checking
        checkType(str, String.class);
        checkType(42, Integer.class);
    }

    public static void main(String[] args) {
        System.out.println("=== Erasure of Generic Methods Solutions ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
