package academy.javaengineering.jvm.memorymodel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solution 2: Thread-Safe Lazy Initialization
 */
public class Solution2 {

    private static volatile Instance instance;

    public static void main(String[] args) {
        System.out.println("=== Thread-Safe Lazy Initialization ===\n");

        // Task 1: Synchronized
        System.out.println("--- Task 1: Synchronized Method ---");
        Instance i1 = synchronizedGetInstance();
        Instance i2 = synchronizedGetInstance();
        System.out.println("  Same instance: " + (i1 == i2));

        // Task 2: Double-checked locking
        System.out.println("\n--- Task 2: Double-Checked Locking ---");
        Instance i3 = dclGetInstance();
        Instance i4 = dclGetInstance();
        System.out.println("  Same instance: " + (i3 == i4));

        // Task 3: Bill Pugh
        System.out.println("\n--- Task 3: Bill Pugh Singleton ---");
        Instance i5 = BillPughHolder.INSTANCE;
        Instance i6 = BillPughHolder.INSTANCE;
        System.out.println("  Same instance: " + (i5 == i6));
    }

    static synchronized Instance synchronizedGetInstance() {
        if (instance == null) {
            instance = new Instance();
        }
        return instance;
    }

    static Instance dclGetInstance() {
        if (instance == null) {
            synchronized (Solution2.class) {
                if (instance == null) {
                    instance = new Instance();
                }
            }
        }
        return instance;
    }

    static class BillPughHolder {
        static final Instance INSTANCE = new Instance();
    }

    static class Instance {
        Instance() { System.out.println("  Instance created"); }
    }
}
