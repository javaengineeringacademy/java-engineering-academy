package academy.javaengineering.generics.memory;

import java.lang.reflect.*;
import java.util.*;

public class TypeErasureMemory {

    static class Generic<T> {
        T value;
        public Generic(T value) { this.value = value; }
    }

    static class BoundedGeneric<T extends Number> {
        T value;
        public BoundedGeneric(T value) { this.value = value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Type Erasure Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. No Generic Info at Runtime
        System.out.println("--- Runtime Type Info ---");
        System.out.println("Generic<T> bytecode: value is Object");
        System.out.println("BoundedGeneric<T extends Number>: value is Number");
        System.out.println("No generic signature in .class file at runtime");

        // 2. Bridge Method Overhead
        System.out.println("\n--- Bridge Method Overhead ---");
        Method[] methods = Generic.class.getDeclaredMethods();
        System.out.println("Generic methods: " + methods.length);
        for (Method m : methods) {
            System.out.println("  " + m.getName() + " -> " + m.getReturnType().getSimpleName());
        }
        System.out.println("Bridge methods: ~16 bytes per method");

        // 3. Cast Insertion Cost
        System.out.println("\n--- Cast Cost ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) list.add("item" + i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Cast overhead: ~1-2 cycles per access");
        System.out.println("Total: " + (after - before) + " bytes");

        // 4. Raw Type Memory
        System.out.println("\n--- Raw vs Generic ---");
        System.out.println("Same memory layout");
        System.out.println("Raw: unchecked warnings");
        System.out.println("Generic: compile-time safety only");
    }
}
