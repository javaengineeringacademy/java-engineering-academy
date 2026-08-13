package academy.javaengineering.oop.internals;

public class AggregationMemory {

    static class Department {
        String name;
        Department(String name) { this.name = name; }
    }

    static class University {
        String name;
        List<Department> departments;
        University(String name, List<Department> depts) {
            this.name = name;
            this.departments = depts;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Aggregation Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Shared Reference Memory
        System.out.println("--- Shared Reference ---");
        System.out.println("University: 8 bytes for departments list");
        System.out.println("Departments: separate objects");
        System.out.println("Shared ownership model");

        // 2. Memory Efficiency
        System.out.println("\n--- Memory Efficiency ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<Department> depts = Arrays.asList(
            new Department("CS"),
            new Department("Math")
        );
        University uni = new University("MIT", depts);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Aggregation: " + (after - before) + " bytes");
        System.out.println("Departments exist independently");

        // 3. GC Behavior
        System.out.println("\n--- GC Behavior ---");
        System.out.println("Departments collected when no references");
        System.out.println("University doesn't own departments");
        System.out.println("More flexible memory management");
    }
}
