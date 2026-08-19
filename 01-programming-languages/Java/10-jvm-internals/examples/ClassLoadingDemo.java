package academy.javaengineering.jvm.examples;

import java.lang.reflect.Method;

/**
 * Class Loading Lifecycle Demo
 * Demonstrates the complete lifecycle of class loading:
 * Loading -> Linking (Verification, Preparation, Resolution) -> Initialization
 */
public class ClassLoadingDemo {

    // Static field - initialized during <clinit>
    private static final String PHASE = "INITIALIZED";
    private static int counter;

    static {
        counter = 100;
        System.out.println("[<clinit>] Static initializer executed, counter = " + counter);
    }

    // Instance initializer runs before constructor
    {
        System.out.println("[instance init] Instance initializer executed");
    }

    public ClassLoadingDemo() {
        System.out.println("[constructor] Constructor executed");
    }

    /**
     * DEMO 1: Loading Phase
     * The classloader subsystem locates the .class file and reads the bytes.
     * Three sources: local filesystem, network (JAR/WAR), dynamic proxy generation.
     */
    public static void demonstrateLoading() {
        System.out.println("=== Phase 1: LOADING ===");
        System.out.println("1. ClassLoader finds the .class file");
        System.out.println("2. Reads raw bytes from the file");
        System.out.println("3. Creates java.lang.Class object in heap");
        System.out.println("4. Class object serves as gateway to runtime data");

        // Force loading of a class
        try {
            Class.forName("academy.javaengineering.jvm.examples.ClassLoadingDemo");
            System.out.println("Class loaded successfully via Class.forName()");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * DEMO 2: Verification Phase
     * Bytecode verifier ensures the .class file is valid and safe.
     * Checks: magic number, version, constant pool, stack maps, type safety
     */
    public static void demonstrateVerification() {
        System.out.println("\n=== Phase 2a: VERIFICATION ===");
        System.out.println("Checks performed:");
        System.out.println("  1. Magic number (0xCAFEBABE)");
        System.out.println("  2. Version numbers (major/minor)");
        System.out.println("  3. Constant pool structure");
        System.out.println("  4. Bytecode validity and stack map frames");
        System.out.println("  5. Type safety (no stack overflow/underflow)");
        System.out.println("  6. Access control (private/protected access)");
        System.out.println("  7. Final class/method checks");
    }

    /**
     * DEMO 3: Preparation Phase
     * JVM allocates memory for static variables and sets default values.
     */
    public static void demonstratePreparation() {
        System.out.println("\n=== Phase 2b: PREPARATION ===");
        System.out.println("Memory allocated for static fields with default values:");
        System.out.println("  int -> 0");
        System.out.println("  long -> 0L");
        System.out.println("  float -> 0.0f");
        System.out.println("  double -> 0.0d");
        System.out.println("  boolean -> false");
        System.out.println("  reference -> null");
        System.out.println("  String -> null (not \"INITIALIZED\" yet)");
    }

    /**
     * DEMO 4: Resolution Phase
     * Symbolic references in the constant pool are replaced with direct references.
     */
    public static void demonstrateResolution() {
        System.out.println("\n=== Phase 2c: RESOLUTION ===");
        System.out.println("Symbolic references -> Direct references:");
        System.out.println("  \"java/lang/Object\" -> memory address of Object.class");
        System.out.println("  \"System.out\" -> reference to PrintStream object");
        System.out.println("  Method refs -> direct pointer to method bytecode");
        System.out.println("  Field refs -> direct pointer to field memory");
        System.out.println("Resolution can be lazy (on first use) or eager");
    }

    /**
     * DEMO 5: Initialization Phase
     * Static initializers and static variable assignments execute in order.
     * Thread-safe: JVM guarantees only one thread initializes a class.
     */
    public static void demonstrateInitialization() {
        System.out.println("\n=== Phase 3: INITIALIZATION ===");
        System.out.println("Static fields get their actual values:");
        System.out.println("  PHASE = \"" + PHASE + "\"");
        System.out.println("  counter = " + counter);
        System.out.println("\nInitialization triggers:");
        System.out.println("  1. new object creation");
        System.out.println("  2. static method invocation");
        System.out.println("  3. static field access (not compile-time constants)");
        System.out.println("  4. reflection (Class.forName)");
        System.out.println("  5. subclass initialization triggers superclass");
    }

    /**
     * DEMO 6: Initialization Order
     */
    public static void demonstrateInitOrder() {
        System.out.println("\n=== Initialization Order ===");
        System.out.println("Order of execution for 'new MyClass()':");
        System.out.println("  1. superclass <clinit> (if not already done)");
        System.out.println("  2. subclass <clinit> (if not already done)");
        System.out.println("  3. superclass constructor");
        System.out.println("  4. instance initializers in declaration order");
        System.out.println("  5. subclass constructor");

        System.out.println("\nCreating new ClassLoadingDemo instance:");
        new ClassLoadingDemo();
    }

    /**
     * DEMO 7: Class Unloading
     * Classes can be unloaded when their ClassLoader is garbage collected.
     */
    public static void demonstrateUnloading() {
        System.out.println("\n=== Class Unloading ===");
        System.out.println("Conditions for unloading:");
        System.out.println("  1. ClassLoader that loaded it becomes unreachable");
        System.out.println("  2. No instances of the class exist on heap");
        System.out.println("  3. No static references to the class");
        System.out.println("\nClasses loaded by bootstrap/platform/app loaders");
        System.out.println("  are NEVER unloaded during JVM lifetime");
        System.out.println("\nOnly classes loaded by custom classloaders can be unloaded");
    }

    /**
     * DEMO 8: Incompatible Class Change Errors
     */
    public static void demonstrateIncompatibleChanges() {
        System.out.println("\n=== Incompatible Class Change Errors ===");
        System.out.println("IncompatibleClassChangeError occurs when:");
        System.out.println("  1. Non-abstract class becomes abstract");
        System.out.println("  2. Class becomes interface");
        System.out.println("  3. Interface becomes class");
        System.out.println("  4. Field/method access changes");
        System.out.println("  5. Final class/method becomes non-final");
        System.out.println("\nNoClassDefFoundError: class definition not found at runtime");
        System.out.println("ClassNotFoundException: classloader cannot find the class");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   CLASS LOADING LIFECYCLE DEMO      ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateLoading();
        demonstrateVerification();
        demonstratePreparation();
        demonstrateResolution();
        demonstrateInitialization();
        demonstrateInitOrder();
        demonstrateUnloading();
        demonstrateIncompatibleChanges();

        // Demonstrate reflection loading
        System.out.println("\n=== Reflection Loading ===");
        Class<?> clazz = Class.forName("java.lang.String");
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("String has " + methods.length + " declared methods");
        System.out.println("Class loaded by: " + clazz.getClassLoader());
    }
}
