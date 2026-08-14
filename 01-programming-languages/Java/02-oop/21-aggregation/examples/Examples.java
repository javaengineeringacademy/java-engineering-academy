package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Aggregation Patterns ===\n");

        // WHY: Aggregation = "part-of" with independent lifecycles
        // INTERNAL: Weak reference semantics - contained object can exist independently
        // ENGINEERING: Use when parts can be shared or exist without the whole

        Department engineering = new Department("Engineering");
        Employee e1 = new Employee("Alice", 1001);
        Employee e2 = new Employee("Bob", 1002);

        engineering.addEmployee(e1);
        engineering.addEmployee(e2);

        // Employees exist independently of department
        System.out.println(engineering);
        System.out.println("Alice's department: " + e1.getName());

        // TRADE-OFF: Aggregation vs Composition
        // Aggregation: shared, independent lifecycle, weaker relationship
        // Composition: exclusive, dependent lifecycle, stronger relationship
    }
}

class Department {
    private final String name;
    private final java.util.List<Employee> employees = new java.util.ArrayList<>();

    Department(String name) { this.name = name; }

    public void addEmployee(Employee e) { employees.add(e); }

    @Override
    public String toString() {
        return name + " (" + employees.size() + " employees)";
    }
}

class Employee {
    private final String name;
    private final int id;

    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
}
