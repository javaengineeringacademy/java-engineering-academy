package academy.javaengineering.generics.memory;

import java.util.*;

public class BoundedTypeMemory {

    public static void main(String[] args) {
        System.out.println("=== Bounded Type Parameters Memory ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Bounded vs Unbounded
        System.out.println("--- Bounded vs Unbounded ---");
        System.out.println("Bounded: T extends Number (4 bytes for ref)");
        System.out.println("Unbounded: T (8 bytes for Object ref)");
        System.out.println("Bounded allows primitive-like access");

        // 2. Bridge Methods
        System.out.println("\n--- Bridge Methods ---");
        System.out.println("Compiler generates synthetic bridge methods");
        System.out.println("Bridge method: Object -> T conversion");
        System.out.println("Cost: extra method in bytecode");

        // 3. Multiple Bounds Memory
        System.out.println("\n--- Multiple Bounds ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<Comparable<?>> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) list.add(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Bounded list: " + (after - before) + " bytes");

        // 4. Interface vs Abstract Class
        System.out.println("\n--- Interface vs Abstract Class ---");
        System.out.println("Interface: no memory overhead");
        System.out.println("Abstract class: adds object header");
        System.out.println("Use interfaces for bounds when possible");
    }
}
