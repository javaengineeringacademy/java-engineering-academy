package academy.javaengineering.oop.memory;

public class SuperKeywordMemory {

    static class Animal {
        String type;
        Animal(String type) { this.type = type; }
    }

    static class Dog extends Animal {
        String breed;
        Dog(String type, String breed) {
            super(type);
            this.breed = breed;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 'super' Keyword Memory ===\n");

        // 1. Parent Object Memory
        System.out.println("--- Parent Object Memory ---");
        System.out.println("Dog object contains Animal part");
        System.out.println("Memory layout: Animal fields + Dog fields");
        System.out.println("super accesses parent part");

        // 2. Constructor Chaining Memory
        System.out.println("\n--- Constructor Chaining ---");
        System.out.println("super() initializes parent part first");
        System.out.println("Then child part is initialized");
        System.out.println("Same object, different initialization phases");

        // 3. Method Override Memory
        System.out.println("\n--- Method Override ---");
        System.out.println("Parent method still exists in class");
        System.out.println("super.call() bypasses override");
        System.out.println("No extra memory overhead");
    }
}
