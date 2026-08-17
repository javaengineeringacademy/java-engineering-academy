package academy.javaengineering.fundamentals.projectstructure;

/**
 * Demonstrates project structure memory usage patterns.
 */
public class ProjectStructureMemory {

    public static void main(String[] args) {
        System.out.println("=== Project Structure Memory Demo ===\n");

        // 1. Class loading memory
        System.out.println("--- Class Loading Memory ---");
        System.out.println("Class metadata stored in Method Area (Metaspace)");
        System.out.println("Static fields stored in Method Area");
        System.out.println("Instance fields stored in Heap");

        // 2. Package access memory
        System.out.println("\n--- Package Access Memory ---");
        System.out.println("No memory overhead for access modifiers");
        System.out.println("Access control is compile-time only");

        // 3. Inner class memory
        System.out.println("\n--- Inner Class Memory ---");
        System.out.println("Non-static inner class: implicit reference to outer (8 bytes)");
        System.out.println("Static inner class: no outer reference");

        // 4. Resource loading
        System.out.println("\n--- Resource Loading Memory ---");
        System.out.println("Resources loaded from classpath into heap");
        System.out.println("ClassLoader caches resources for reuse");

        System.out.println("\n=== Memory Demo Complete ===");
    }
}
