package academy.javaengineering.oop.memory;

public class AccessModifiersMemory {

    public static void main(String[] args) {
        System.out.println("=== Access Modifiers Memory Analysis ===\n");

        // 1. Access Modifier Overhead
        System.out.println("--- Access Modifier Overhead ---");
        System.out.println("No runtime cost for access modifiers");
        System.out.println("Enforced at compile-time only");
        System.out.println("No impact on object size");

        // 2. Private vs Public
        System.out.println("\n--- Private vs Public ---");
        System.out.println("Private fields: same memory as public");
        System.out.println("Getter/setter: method call overhead");
        System.out.println("JIT inlines: near-zero cost");

        // 3. Best Practice Memory
        System.out.println("\n--- Best Practice Memory ---");
        System.out.println("Use private: encapsulation");
        System.out.println("Use final: JIT optimization");
        System.out.println("Use static: shared across instances");
    }
}
