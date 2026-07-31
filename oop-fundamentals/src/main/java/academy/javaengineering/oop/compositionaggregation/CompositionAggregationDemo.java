package academy.javaengineering.oop.compositionaggregation;

/**
 * CompositionAggregationDemo - Demonstrates composition vs aggregation relationships.
 * 
 * <p><b>Composition (Strong "has-a"):</b>
 * <ul>
 *   <li>Part cannot exist without the whole</li>
 *   <li>Part lifecycle managed by the whole</li>
 *   <li>Example: House HAS rooms (rooms don't exist without house)</li>
 * </ul>
 * 
 * <p><b>Aggregation (Weak "has-a"):</b>
 * <ul>
 *   <li>Part can exist independently of the whole</li>
 *   <li>Part lifecycle not managed by the whole</li>
 *   <li>Example: Department HAS employees (employees exist independently)</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class CompositionAggregationDemo {

    private CompositionAggregationDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Composition vs Aggregation Demo ===\n");

        // Composition - parts created inside whole
        System.out.println("--- Composition (Strong Relationship) ---");
        Engine engine = new Engine("V8", 400);
        Car car = new Car("Toyota", "Camry", engine);
        System.out.println("Car: " + car.getMake() + " " + car.getModel());
        System.out.println("Engine: " + car.getEngine().getSpecification());
        
        // If car is destroyed, engine is destroyed too
        car.displayInfo();

        // Aggregation - parts passed in from outside
        System.out.println("\n--- Aggregation (Weak Relationship) ---");
        Employee emp1 = new Employee("Alice", "Engineering");
        Employee emp2 = new Employee("Bob", "Engineering");
        Employee emp3 = new Employee("Charlie", "Marketing");

        Department engineering = new Department("Engineering", emp1);
        engineering.addEmployee(emp2);
        
        Department marketing = new Department("Marketing", emp3);

        System.out.println("Engineering dept:");
        engineering.listEmployees();
        
        System.out.println("Marketing dept:");
        marketing.listEmployees();

        // Employees can exist independently
        System.out.println("\nEmployees exist independently of departments:");
        System.out.println(emp1.getName() + " works in " + emp1.getDepartment());
        System.out.println(emp3.getName() + " works in " + emp3.getDepartment());

        // Computer example - deep composition
        System.out.println("\n--- Deep Composition ---");
        Computer computer = new Computer("Intel", 16, 512);
        Workstation workstation = new Workstation("Developer Station", computer);
        workstation.displayConfiguration();

        // Compare lifecycles
        System.out.println("\n--- Lifecycle Comparison ---");
        System.out.println("Composition: Part dies with whole");
        System.out.println("  - Engine destroyed when Car is destroyed");
        System.out.println("  - Room destroyed when House is destroyed");
        System.out.println("  - CPU destroyed when Computer is destroyed");
        System.out.println("\nAggregation: Part survives whole");
        System.out.println("  - Employee survives Department");
        System.out.println("  - Student survives Classroom");
        System.out.println("  - Book survives Library");
    }
}