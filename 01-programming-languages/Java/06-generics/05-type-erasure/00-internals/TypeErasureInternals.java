package academy.javaengineering.generics.internals;

import java.lang.reflect.*;
import java.util.*;

public class TypeErasureInternals {

    static class Generic<T> {
        T value;
        public Generic(T value) { this.value = value; }
        public T getValue() { return value; }
        public void setValue(T value) { this.value = value; }
    }

    static class BoundedGeneric<T extends Number> {
        T value;
        public BoundedGeneric(T value) { this.value = value; }
        public double doubleValue() { return value.doubleValue(); }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Type Erasure Internals ===\n");

        // 1. Compile-Time vs Runtime
        System.out.println("--- Compile vs Runtime ---");
        Generic<String> gs = new Generic<>("Hello");
        Generic<Integer> gi = new Generic<>(42);
        System.out.println("Compile-time: Generic<String> vs Generic<Integer>");
        System.out.println("Runtime: both are just Generic");
        System.out.println("gs.getClass() == gi.getClass(): " + (gs.getClass() == gi.getClass()));

        // 2. Reflection Proof
        System.out.println("\n--- Reflection Proof ---");
        Field[] fields = Generic.class.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("Field: " + f.getName() + " -> " + f.getType());
        }

        // 3. Bridge Methods
        System.out.println("\n--- Bridge Methods ---");
        Method[] methods = Generic.class.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("Method: " + m.getName() + " -> " + m.getReturnType());
        }

        // 4. Type Erasure Rules
        System.out.println("\n--- Erasure Rules ---");
        System.out.println("T -> Object (unbounded)");
        System.out.println("T extends Number -> Number");
        System.out.println("T extends Comparable -> Comparable");
        System.out.println("? -> Object");
        System.out.println("? extends Number -> Number");

        // 5. Cast Insertion
        System.out.println("\n--- Cast Insertion ---");
        System.out.println("Compiler inserts casts after erasure");
        System.out.println("String s = (String) genericObject.value");
    }
}
