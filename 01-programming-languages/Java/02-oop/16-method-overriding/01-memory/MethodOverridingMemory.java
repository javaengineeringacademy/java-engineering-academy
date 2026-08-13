package academy.javaengineering.oop.memory;

public class MethodOverridingMemory {

    static class Animal {
        void makeSound() { System.out.println("Sound"); }
    }

    static class Dog extends Animal {
        @Override
        void makeSound() { System.out.println("Bark"); }
    }

    public static void main(String[] args) {
        System.out.println("=== Method Overriding Memory ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Virtual Method Table
        System.out.println("--- Virtual Method Table ---");
        System.out.println("Each class has vtable");
        System.out.println("Dog vtable: overrides makeSound()");
        System.out.println("Cost: ~8 bytes per class");

        // 2. Method Lookup Cost
        System.out.println("\n--- Method Lookup ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Animal animal = new Dog();
        for (int i = 0; i < 1000000; i++) {
            animal.makeSound();
        }
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Lookup cost: ~2-3 cycles");
        System.out.println("JIT may inline for performance");

        // 3. No Extra Memory Per Object
        System.out.println("\n--- Object Memory ---");
        System.out.println("Dog object: same size as without override");
        System.out.println("Override: only method table entry");
        System.out.println("No per-object overhead");
    }
}
