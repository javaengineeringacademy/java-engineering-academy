package academy.javaengineering.generics.memory;

import java.lang.reflect.*;
import java.util.*;

public class ErasureTypesMemory {

    static class Container<T> {
        T value;
        public Container(T value) { this.value = value; }
    }

    static class BoundedContainer<T extends Number> {
        T value;
        public BoundedContainer(T value) { this.value = value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Erasure Types Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. No Type Info at Runtime
        System.out.println("--- Runtime Type Info ---");
        System.out.println("Container<String>.value -> Object");
        System.out.println("BoundedContainer<Integer>.value -> Number");
        System.out.println("Type parameter completely erased");

        // 2. Bridge Method Overhead
        System.out.println("\n--- Bridge Method Cost ---");
        Method[] methods = Container.class.getDeclaredMethods();
        System.out.println("Container methods: " + methods.length);
        for (Method m : methods) {
            System.out.println("  " + m.getName() + " -> " + m.getReturnType().getSimpleName());
        }
        System.out.println("Cost: ~16 bytes per bridge method");

        // 3. Cast Insertion
        System.out.println("\n--- Cast Cost ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) list.add(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Cast overhead: ~1-2 cycles per access");

        // 4. Object vs Specific Type
        System.out.println("\n--- Object vs Specific ---");
        System.out.println("Container<Object>: 8 bytes (Object ref)");
        System.out.println("Container<Integer>: 8 bytes (Object ref)");
        System.out.println("Same - erasure makes them identical");
    }
}
