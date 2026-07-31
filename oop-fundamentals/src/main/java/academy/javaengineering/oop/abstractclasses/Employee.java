package academy.javaengineering.oop.abstractclasses;

/**
 * Employee - Abstract class demonstrating constructor chaining and shared state.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class Employee {

    protected final String name;
    protected final int id;
    private static int nextId = 1000;

    protected Employee(String name) {
        this(name, nextId++);
    }

    protected Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // Abstract methods
    public abstract String getRole();
    public abstract double calculatePay();

    // Concrete methods
    public String getName() { return name; }
    public int getId() { return id; }

    @Override
    public String toString() {
        return String.format("%s{id=%d, name='%s', role='%s', pay=%.2f}",
            getClass().getSimpleName(), id, name, getRole(), calculatePay());
    }
}