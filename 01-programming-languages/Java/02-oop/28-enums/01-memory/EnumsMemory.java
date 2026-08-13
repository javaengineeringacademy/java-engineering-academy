package academy.javaengineering.oop.memory;

public class EnumsMemory {

    enum Color { RED, GREEN, BLUE }

    public static void main(String[] args) {
        System.out.println("=== Enums Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Enum Object Size
        System.out.println("--- Enum Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Color color = Color.RED;
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Enum: " + (after - before) + " bytes");
        System.out.println("Each enum constant: separate object");

        // 2. Enum vs Constants
        System.out.println("\n--- Enum vs Constants ---");
        System.out.println("Enum: type-safe, has methods");
        System.out.println("int constants: no type safety");
        System.out.println("Enum: ~16 bytes per constant");

        // 3. Enum Singleton
        System.out.println("\n--- Enum Singleton ---");
        System.out.println("Only one instance per constant");
        System.out.println("JVM guarantees single instance");
        System.out.println("Thread-safe by design");
    }
}
