package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * MemoryModel - Heap, stack, metaspace demo
 *
 * Covers:
 * - Heap memory (Young Gen, Old Gen)
 * - Stack memory (frames, local variables)
 * - Metaspace (class metadata)
 * - Memory allocation and garbage collection basics
 */
public class MemoryModel {

    private static int staticVariable = 100;
    private int instanceVariable = 200;

    public static void main(String[] args) {
        System.out.println("=== Heap vs Stack ===");
        heapVsStack();

        System.out.println("\n=== Stack Frame Demo ===");
        stackFrameDemo();

        System.out.println("\n=== Object Creation ===");
        objectCreation();

        System.out.println("\n=== Memory Leaks ===");
        memoryLeakDemo();
    }

    static void heapVsStack() {
        // Stack: primitive types, local variables, method calls
        int localVar = 42;
        double pi = 3.14159;
        boolean flag = true;

        System.out.println("Stack variables (primitives):");
        System.out.println("  localVar: " + localVar);
        System.out.println("  pi: " + pi);
        System.out.println("  flag: " + flag);

        // Heap: objects, arrays
        String heapString = new String("Hello from Heap");
        int[] heapArray = {1, 2, 3, 4, 5};
        MemoryModel obj = new MemoryModel();

        System.out.println("\nHeap objects:");
        System.out.println("  String: " + heapString);
        System.out.println("  Array: " + heapArray);
        System.out.println("  Object hashCode: " + obj.hashCode());
    }

    static void stackFrameDemo() {
        System.out.println("Method call stack demonstration:");
        methodA(1);
    }

    static void methodA(int param) {
        int localVarA = 10;
        System.out.println("  methodA called with param=" + param + ", localVarA=" + localVarA);
        methodB(2);
    }

    static void methodB(int param) {
        int localVarB = 20;
        System.out.println("  methodB called with param=" + param + ", localVarB=" + localVarB);
        methodC(3);
    }

    static void methodC(int param) {
        int localVarC = 30;
        System.out.println("  methodC called with param=" + param + ", localVarC=" + localVarC);
        System.out.println("  Stack depth: 3 frames");
    }

    static void objectCreation() {
        // String pool (part of heap in Java 7+)
        String poolString1 = "Java";
        String poolString2 = "Java";
        String heapString = new String("Java");

        System.out.println("String Pool:");
        System.out.println("  poolString1 == poolString2: " + (poolString1 == poolString2));
        System.out.println("  poolString1 == heapString: " + (poolString1 == heapString));
        System.out.println("  poolString1.equals(heapString): " + poolString1.equals(heapString));

        // Array allocation
        int[] smallArray = new int[10];
        int[] largeArray = new int[1000000];

        System.out.println("\nArray allocation:");
        System.out.println("  Small array size: " + smallArray.length);
        System.out.println("  Large array size: " + largeArray.length);

        // Object graph
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");

        System.out.println("\nObject graph:");
        System.out.println("  List size: " + list.size());
        System.out.println("  List class loader: " + list.getClass().getClassLoader());
    }

    static void memoryLeakDemo() {
        System.out.println("Memory leak example (preventable):");

        // Don't do this in production!
        List<byte[]> memoryLeak = new ArrayList<>();

        // Simulate adding data without cleanup
        for (int i = 0; i < 10; i++) {
            memoryLeak.add(new byte[1024 * 1024]); // 1MB each
            Runtime runtime = Runtime.getRuntime();
            long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
            System.out.println("  Iteration " + i + ", Used: " + used + " MB");
        }

        // Clear to prevent actual OOM
        memoryLeak.clear();
        System.out.println("  Cleared list to free memory");
    }

    @Override
    protected void finalize() {
        System.out.println("Object finalized: " + hashCode());
    }
}