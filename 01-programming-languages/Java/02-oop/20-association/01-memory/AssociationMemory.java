package academy.javaengineering.oop.memory;

public class AssociationMemory {

    static class Teacher {
        String name;
        Teacher(String name) { this.name = name; }
    }

    static class Student {
        String name;
        Student(String name) { this.name = name; }
    }

    public static void main(String[] args) {
        System.out.println("=== Association Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Reference Memory
        System.out.println("--- Reference Memory ---");
        System.out.println("Association: reference in class");
        System.out.println("Cost: 8 bytes per reference");
        System.out.println("Points to separate object");

        // 2. Object Independence
        System.out.println("\n--- Object Independence ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Teacher teacher = new Teacher("Mr. Smith");
        Student student = new Student("Alice");
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Two objects: " + (after - before) + " bytes");
        System.out.println("Independent lifecycle");

        // 3. GC Behavior
        System.out.println("\n--- GC Behavior ---");
        System.out.println("Objects collected independently");
        System.out.println("No circular dependency issues");
        System.out.println("Memory freed when reference removed");
    }
}
