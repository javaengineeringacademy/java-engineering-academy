package academy.javaengineering.jvm.classloading;

/**
 * Exercise 1: Observe Class Loading Phases
 *
 * Task: Use -verbose:class and Class.forName() to observe the three phases
 * of class loading: Loading, Linking, and Initialization.
 *
 * Run this program with: java -verbose:class Exercise1
 */
public class Exercise1 {

    static {
        System.out.println("[INIT] Static initializer executed for Exercise1");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Class Loading Phases Observation ===\n");

        // Task 1: Observe when a class is loaded vs initialized
        System.out.println("--- Task 1: Load Without Initialize ---");
        // TODO: Use Class.forName with initialize=false to load a class without running its static block
        // Class<?> clazz = Class.forName("academy.javaengineering.jvm.classloading.DelayedInit", false, ...);
        // System.out.println("Class loaded but not initialized: " + clazz.getName());

        // Task 2: Compare loadClass() vs forName()
        System.out.println("\n--- Task 2: loadClass vs forName ---");
        // TODO: Use ClassLoader.loadClass() to load without initialization
        // TODO: Use Class.forName() to load WITH initialization
        // Print which one triggered the static block

        // Task 3: Observe linking phases
        System.out.println("\n--- Task 3: Linking Phases ---");
        // TODO: Load a class and observe:
        // - Loading: Class object created
        // - Verification: bytecode checked
        // - Preparation: static fields get default values
        // - Resolution: symbolic references replaced
        // - Initialization: static block runs

        System.out.println("\n[Complete the TODO sections above]");
    }
}

class DelayedInit {
    static {
        System.out.println("[INIT] DelayedInit static block executed");
    }

    static int value = 42;
}
