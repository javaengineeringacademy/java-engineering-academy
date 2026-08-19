package academy.javaengineering.jvm.examples;

/**
 * JVM Architecture Demo
 * Demonstrates the major components of JVM architecture:
 * - Class Loader Subsystem
 * - Runtime Data Areas (Method Area, Heap, Stack, PC Registers, Native Method Stack)
 * - Execution Engine (Interpreter, JIT Compiler, Garbage Collector)
 * - Native Method Interface (JNI)
 */
public class JvmArchitectureDemo {

    // Method Area stores class metadata, static variables, constant pool
    private static int staticField = 42;
    private static final String CONSTANT = "JVM_ARCHITECTURE";

    // Instance fields stored in Heap
    private int instanceField;
    private String name;

    public JvmArchitectureDemo(String name) {
        this.name = name;
        this.instanceField = 100;
    }

    /**
     * DEMO 1: Runtime Data Areas
     * Each thread gets its own Program Counter and JVM Stack.
     * The Heap and Method Area are shared among all threads.
     */
    public void demonstrateDataAreas() {
        System.out.println("=== Runtime Data Areas ===");

        // Stack frames for this method call
        long stackBase = Thread.currentThread().getId();
        System.out.println("Current Thread ID (uses own Stack + PC): " + stackBase);

        // Method Area: class metadata
        Class<?> clazz = this.getClass();
        System.out.println("Class loaded in Method Area: " + clazz.getName());
        System.out.println("Static field (Method Area): " + staticField);
        System.out.println("Constant (Method Area constant pool): " + CONSTANT);

        // Heap: object instances
        JvmArchitectureDemo obj = new JvmArchitectureDemo("heap_object");
        System.out.println("Object instance on Heap: " + obj.name);
        System.out.println("Instance field (Heap): " + obj.instanceField);
    }

    /**
     * DEMO 2: Stack Frame Anatomy
     * Each method invocation creates a stack frame containing:
     * - Local Variable Array
     * - Operand Stack
     * - Frame Data (constant pool reference, return address)
     */
    public void demonstrateStackFrames() {
        System.out.println("\n=== Stack Frame Anatomy ===");
        int localVar = 10;
        double anotherLocal = 3.14;

        System.out.println("Local Variable Array holds: localVar=" + localVar
                + ", anotherLocal=" + anotherLocal);
        System.out.println("Operand Stack used for: method invocations, "
                + "arithmetic, field access");

        // Recursive call shows multiple stack frames
        int result = factorial(5);
        System.out.println("factorial(5) = " + result
                + " (5 stack frames were created)");
    }

    private int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    /**
     * DEMO 3: Execution Engine Components
     */
    public void demonstrateExecutionEngine() {
        System.out.println("\n=== Execution Engine ===");

        // Interpreter: executes bytecode line by line
        System.out.println("Interpreter: Translates bytecode -> machine code one instruction at a time");

        // JIT Compiler: compiles hot methods to native code
        long start = System.nanoTime();
        hotMethod();
        long elapsed = System.nanoTime() - start;
        System.out.println("JIT compiled hot method execution: " + elapsed + "ns");

        // Demonstrate tiered compilation
        System.out.println("Tiered Compilation: C1 (client) -> C2 (server) compiler pipeline");
        System.out.println("  - Tier 0: Interpretation");
        System.out.println("  - Tier 1-3: C1 compiler (fast compile, basic optimizations)");
        System.out.println("  - Tier 4: C2 compiler (slow compile, aggressive optimizations)");
    }

    // Method called many times to trigger JIT compilation
    private int hotMethod() {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }

    /**
     * DEMO 4: Memory Layout Comparison
     */
    public void demonstrateMemoryLayout() {
        System.out.println("\n=== Memory Layout ===");

        // Object header (16 bytes on 64-bit JVM with compressed oops)
        // + padding to 8-byte boundary
        System.out.println("Object Header: 12 bytes (mark word 8 + klass pointer 4)");
        System.out.println("  with compressed oops: 16 bytes aligned");

        // Array layout
        int[] arr = new int[10];
        System.out.println("Array header: 16 bytes + 4 bytes per element");
        System.out.println("int[10] size: " + (16 + 10 * 4) + " bytes");

        // Field layout in class
        System.out.println("Field alignment: fields ordered by size "
                + "(double/long -> int/float -> short/char -> byte/boolean -> reference)");
    }

    /**
     * DEMO 5: Native Method Interface
     */
    public void demonstrateJNI() {
        System.out.println("\n=== Native Method Interface ===");
        System.out.println("JNI bridges Java -> native code (C/C++)");

        // Runtime library uses native methods
        String os = System.getProperty("os.name");
        System.out.println("OS detected via native method: " + os);

        System.out.println("Native methods: System.arraycopy, "
                + "Class.forName0, Thread.currentThread0");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      JVM ARCHITECTURE DEMO          ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        JvmArchitectureDemo demo = new JvmArchitectureDemo("demo");
        demo.demonstrateDataAreas();
        demo.demonstrateStackFrames();
        demo.demonstrateExecutionEngine();
        demo.demonstrateMemoryLayout();
        demo.demonstrateJNI();

        // Print JVM memory info
        System.out.println("\n=== JVM Memory Info ===");
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Memory: " + (runtime.maxMemory() / 1024 / 1024) + " MB");
        System.out.println("Total Memory: " + (runtime.totalMemory() / 1024 / 1024) + " MB");
        System.out.println("Free Memory: " + (runtime.freeMemory() / 1024 / 1024) + " MB");
        System.out.println("Available Processors: " + runtime.availableProcessors());
    }
}
