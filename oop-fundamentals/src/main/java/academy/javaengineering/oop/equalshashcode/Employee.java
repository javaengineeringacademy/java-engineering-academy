package academy.javaengineering.oop.equalshashcode;

/**
 * Employee - Demonstrates proper equals() and hashCode() implementation.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Employee {

    private final int id;
    private final String name;
    private final String department;

    public Employee(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee other = (Employee) obj;
        return id == other.id
            && java.util.Objects.equals(name, other.name)
            && java.util.Objects.equals(department, other.department);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, department);
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', dept='" + department + "'}";
    }
}