package academy.javaengineering.oop.internals;

public class AggregationInternals {

    static class Department {
        String name;
        Department(String name) { this.name = name; }
    }

    static class University {
        private String name;
        private List<Department> departments; // Aggregation

        University(String name, List<Department> departments) {
            this.name = name;
            this.departments = departments; // Passed from outside
        }

        void display() {
            System.out.println("University: " + name);
            System.out.println("Departments: " + departments.size());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Aggregation Internals ===\n");

        // 1. HAS-A with Shared Ownership
        System.out.println("--- Aggregation ---");
        List<Department> depts = Arrays.asList(
            new Department("CS"),
            new Department("Math"),
            new Department("Physics")
        );
        University uni = new University("MIT", depts);
        uni.display();
        System.out.println("Departments exist independently");

        // 2. Aggregation vs Composition
        System.out.println("\n--- Aggregation vs Composition ---");
        System.out.println("Aggregation: shared ownership");
        System.out.println("Composition: exclusive ownership");
        System.out.println("Aggregation: weaker relationship");

        // 3. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("1. Reusability");
        System.out.println("2. Flexibility");
        System.out.println("3. Loose coupling");
    }
}
