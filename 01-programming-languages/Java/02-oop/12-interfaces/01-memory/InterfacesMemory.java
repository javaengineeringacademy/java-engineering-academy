package academy.javaengineering.oop.memory;

public class InterfacesMemory {

    interface Drawable { void draw(); }
    interface Resizable { void resize(int factor); }

    static class Circle implements Drawable, Resizable {
        double radius;
        Circle(double r) { radius = r; }
        public void draw() { System.out.println("Drawing"); }
        public void resize(int f) { radius *= f; }
    }

    public static void main(String[] args) {
        System.out.println("=== Interfaces Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Interface vs Abstract Class Memory
        System.out.println("--- Interface Memory ---");
        System.out.println("Interface: no instance fields");
        System.out.println("Abstract class: may have fields");
        System.out.println("Interface: lighter weight");

        // 2. Multiple Interface Implementation
        System.out.println("\n--- Multiple Interfaces ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Circle circle = new Circle(5);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Circle (2 interfaces): " + (after - before) + " bytes");
        System.out.println("Same as single interface - no overhead");

        // 3. Default Method Memory
        System.out.println("\n--- Default Method ---");
        System.out.println("Default methods: stored in class");
        System.out.println("No extra per-object cost");
        System.out.println("Shared via method table");
    }
}
