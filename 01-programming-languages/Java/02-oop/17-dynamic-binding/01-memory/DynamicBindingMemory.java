package academy.javaengineering.oop.memory;

public class DynamicBindingMemory {

    static class Animal {
        void makeSound() { System.out.println("Sound"); }
    }

    static class Dog extends Animal {
        @Override
        void makeSound() { System.out.println("Bark"); }
    }

    public static void main(String[] args) {
        System.out.println("=== Dynamic Binding Memory ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Method Table Memory
        System.out.println("--- Method Table ---");
        System.out.println("Each class has method table in Metaspace");
        System.out.println("Dog table: overrides Animal.makeSound()");
        System.out.println("Cost: ~8 bytes per method in table");

        // 2. Virtual Dispatch Cost
        System.out.println("\n--- Virtual Dispatch ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Animal animal = new Dog();
        for (int i = 0; i < 1000000; i++) {
            animal.makeSound();
        }
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Dispatch cost: ~2-3 cycles");
        System.out.println("JIT optimizes: inlining, devirtualization");

        // 3. No Per-Object Cost
        System.out.println("\n--- Per-Object Cost ---");
        System.out.println("Dynamic binding: no extra memory");
        System.out.println("Method table: shared per class");
        System.out.println("Object: only stores fields");
    }
}
