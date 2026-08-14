package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Object Class Methods ===\n");

        // WHY: Every class inherits from Object. These methods define fundamental behavior.
        // INTERNAL: Object is root of class hierarchy, all methods are virtual
        // ENGINEERING: Override toString, equals, hashCode, clone for custom classes

        Employee e1 = new Employee(1, "Alice", "Engineering");
        Employee e2 = new Employee(1, "Alice", "Engineering");
        Employee e3 = new Employee(2, "Bob", "Marketing");

        System.out.println("toString: " + e1);
        System.out.println("e1.equals(e2): " + e1.equals(e2));
        System.out.println("e1.equals(e3): " + e1.equals(e3));
        System.out.println("e1.hashCode() == e2.hashCode(): " + (e1.hashCode() == e2.hashCode()));

        // TRADE-OFF: Clone vs Copy Constructor
        // Clone: preserves runtime type, but has well-known issues (shallow copy, final fields)
        // Copy Constructor: explicit, safe, can enforce deep copy
        Employee cloned = e1.clone();
        System.out.println("Cloned: " + cloned);
        System.out.println("Same reference? " + (e1 == cloned));
    }
}

class Employee implements Cloneable {
    private final int id;
    private final String name;
    private final String department;

    Employee(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', dept='" + department + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee e = (Employee) o;
        return id == e.id && name.equals(e.name) && department.equals(e.department);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, department);
    }

    @Override
    public Employee clone() {
        try {
            return (Employee) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }
}
