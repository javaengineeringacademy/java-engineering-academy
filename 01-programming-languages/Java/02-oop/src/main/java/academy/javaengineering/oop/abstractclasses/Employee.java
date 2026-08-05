package academy.javaengineering.oop.abstractclasses;

public abstract class Employee {

    protected final String name;
    protected final String id;
    protected double salary;

    public Employee(String name, String id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    public abstract double calculateBonus();
    public abstract String getRole();

    public String getName() { return name; }
    public String getId() { return id; }
    public double getSalary() { return salary; }

    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }

    public String getDetails() {
        return "%s [%s] - %s (Salary: $%.2f)".formatted(name, id, getRole(), salary);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
