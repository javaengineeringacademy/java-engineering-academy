package academy.javaengineering.oop.memory;

public class DependencyMemory {

    static class Printer {
        void print(String doc) { System.out.println("Printing: " + doc); }
    }

    static class Computer {
        void printDocument(String doc, Printer printer) {
            printer.print(doc);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Dependency Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Method Parameter Memory
        System.out.println("--- Method Parameter ---");
        System.out.println("Dependency: reference passed as parameter");
        System.out.println("Cost: 8 bytes on stack frame");
        System.out.println("Temporary - removed after method returns");

        // 2. Object Lifecycle
        System.out.println("\n--- Object Lifecycle ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Computer computer = new Computer();
        Printer printer = new Printer();
        computer.printDocument("Report", printer);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Objects: " + (after - before) + " bytes");
        System.out.println("Independent lifecycle");

        // 3. No Ownership
        System.out.println("\n--- No Ownership ---");
        System.out.println("Computer doesn't own Printer");
        System.out.println("Printer can be used elsewhere");
        System.out.println("No memory leak risk");
    }
}
