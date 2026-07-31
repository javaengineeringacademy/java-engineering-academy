package academy.javaengineering.oop.compositionaggregation;

/**
 * Employee - Part of Department aggregation relationship.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Employee {

    private final String name;
    private final String department;

    public Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', dept='" + department + "'}";
    }
}