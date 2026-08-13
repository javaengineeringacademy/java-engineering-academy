package academy.javaengineering.generics.internals;

import java.lang.reflect.*;
import java.util.*;

public class ErasureTypesInternals {

    static class Container<T> {
        T value;
        public Container(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    static class BoundedContainer<T extends Number> {
        T value;
        public BoundedContainer(T value) { this.value = value; }
        public double getDouble() { return value.doubleValue(); }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Erasure of Generic Types ===\n");

        // 1. Type Erasure Process
        System.out.println("--- Erasure Process ---");
        System.out.println("Step 1: Replace T with Object");
        System.out.println("Step 2: Replace T extends X with X");
        System.out.println("Step 3: Insert casts");
        System.out.println("Step 4: Generate bridge methods");

        // 2. Reflection Proof
        System.out.println("\n--- Reflection ---");
        Field[] fields = Container.class.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("Container.value type: " + f.getType());
        }
        fields = BoundedContainer.class.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("BoundedContainer.value type: " + f.getType());
        }

        // 3. Bridge Methods
        System.out.println("\n--- Bridge Methods ---");
        Method[] methods = Container.class.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("Container: " + m.getName() + " -> " + m.getReturnType());
        }

        // 4. Cast Insertion
        System.out.println("\n--- Cast Insertion ---");
        Container<String> cs = new Container<>("Hi");
        Object obj = cs.value; // No cast needed
        String str = cs.getValue(); // Cast inserted here
        System.out.println("Compiler inserts: (String)getValue()");

        // 5. ClassCastException Scenarios
        System.out.println("\n--- CCE Scenarios ---");
        System.out.println("1. Unchecked cast to wrong type");
        System.out.println("2. Array store with erased type");
        System.out.println("3. Bridge method mismatch");
    }
}
