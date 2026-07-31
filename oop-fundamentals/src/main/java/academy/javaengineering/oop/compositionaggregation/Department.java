package academy.javaengineering.oop.compositionaggregation;

/**
 * Department - Demonstrates aggregation (references external Employees).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Department {

    private final String name;
    private final java.util.List<Employee> employees = new java.util.ArrayList<>();

    public Department(String name, Employee... initialEmployees) {
        this.name = name;
        for (Employee emp : initialEmployees) {
            employees.add(emp);
        }
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public void listEmployees() {
        for (Employee emp : employees) {
            System.out.println("  - " + emp.getName());
        }
    }

    public String getName() { return name; }
    public int getEmployeeCount() { return employees.size(); }
}