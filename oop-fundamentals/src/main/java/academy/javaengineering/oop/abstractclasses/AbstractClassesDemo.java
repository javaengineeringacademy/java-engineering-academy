package academy.javaengineering.oop.abstractclasses;

/**
 * AbstractClassesDemo - Demonstrates abstract class features and use cases.
 * 
 * <p><b>Abstract Classes:</b>
 * <ul>
 *   <li>Cannot be instantiated directly</li>
 *   <li>Can have constructors</li>
 *   <li>Can have instance variables (state)</li>
 *   <li>Can have both abstract and concrete methods</li>
 *   <li>Single inheritance only (extends one class)</li>
 *   <li>Constructor chaining: super() called implicitly</li>
 * </ul>
 * 
 * <p><b>When to use Abstract Classes vs Interfaces:</b>
 * <ul>
 *   <li>Abstract class: IS-A relationship, shared state, constructor logic</li>
 *   <li>Interface: CAN-DO relationship, multiple capabilities, no state</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class AbstractClassesDemo {

    private AbstractClassesDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Abstract Classes Demo ===\n");

        // Cannot instantiate abstract class
        // Shape shape = new Shape(); // COMPILE ERROR!

        // Concrete implementations
        System.out.println("--- Concrete Implementations ---");
        Shape2D circle = new CircleShape(5.0);
        Shape2D rectangle = new RectangleShape(4.0, 6.0);

        System.out.println("Circle area: " + circle.getArea());
        System.out.println("Circle perimeter: " + circle.getPerimeter());
        System.out.println("Rectangle area: " + rectangle.getArea());
        System.out.println("Rectangle perimeter: " + rectangle.getPerimeter());

        // Constructor chaining demonstration
        System.out.println("\n--- Constructor Chaining ---");
        Employee employee = new Developer("Alice", 1001, "Java");
        System.out.println("Created: " + employee);

        // Abstract class with shared state
        System.out.println("\n--- Shared State ---");
        BankAccount savings = new SavingsAccount2("Bob", 1000, 0.05);
        BankAccount checking = new CheckingAccount2("Charlie", 500, 200);
        
        savings.deposit(500);
        checking.withdraw(100);
        
        System.out.println("Savings balance: $" + savings.getBalance());
        System.out.println("Checking balance: $" + checking.getBalance());

        // Polymorphism with abstract classes
        System.out.println("\n--- Polymorphic Usage ---");
        processShape(new CircleShape(3.0));
        processShape(new RectangleShape(2.0, 4.0));

        // Template method pattern
        System.out.println("\n--- Template Method Pattern ---");
        Game game1 = new Chess();
        Game game2 = new Checkers();
        game1.play();
        System.out.println();
        game2.play();
    }

    static void processShape(Shape2D shape) {
        System.out.printf("Processing %s: area=%.2f%n",
            shape.getClass().getSimpleName(), shape.getArea());
    }
}