package academy.javaengineering.jvm.classloading;

/**
 * Solution 1: Observe Class Loading Phases
 *
 * Demonstrates the three phases of class loading: Loading, Linking, and Initialization
 * using Class.forName() and ClassLoader.loadClass().
 */
public class Solution1 {

    static {
        System.out.println("[INIT] Solution1 static initializer executed");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Class Loading Phases Observation ===\n");

        // Task 1: Load Without Initialize
        System.out.println("--- Task 1: Load Without Initialize ---");
        Class<?> clazz = Class.forName(
            "academy.javaengineering.jvm.classloading.DelayedInit",
            false,
            Solution1.class.getClassLoader()
        );
        System.out.println("Class loaded but NOT initialized: " + clazz.getName());
        System.out.println("Static field value (default): " + clazz.getField("value").getInt(null));
        System.out.println();

        // Now initialize the class
        System.out.println("--- Now Initialize ---");
        Class.forName("academy.javaengineering.jvm.classloading.DelayedInit");
        System.out.println("After initialization, value: " + clazz.getField("value").getInt(null));

        // Task 2: loadClass vs forName
        System.out.println("\n--- Task 2: loadClass vs forName ---");
        ClassLoader loader = Solution1.class.getClassLoader();

        System.out.println("Using loadClass (no init):");
        Class<?> loaded = loader.loadClass("academy.javaengineering.jvm.classloading.Exercise1$DelayedInit");
        System.out.println("  Loaded: " + loaded.getName());

        System.out.println("Using forName (with init):");
        Class.forName("academy.javaengineering.jvm.classloading.Exercise1$DelayedInit");

        // Task 3: Observe linking phases
        System.out.println("\n--- Task 3: Linking Phases ---");
        System.out.println("Run with: java -verbose:class Solution1");
        System.out.println("You will see: (Loaded, Verified, Prepared, Initialized)");
    }
}

class DelayedInit {
    static {
        System.out.println("[INIT] DelayedInit static block executed");
    }

    static int value = 42;
}
