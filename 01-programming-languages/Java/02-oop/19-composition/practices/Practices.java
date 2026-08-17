package academy.javaengineering.oop.practices;

/**
 * Practice: Composition in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating "has-a" relationships between classes
 * - Composing objects from other objects
 * - Delegating behavior to composed objects
 * - Lifecycle management of composed objects
 * - Choosing composition over inheritance
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 19-composition ===\n");

        // Test Exercise 1: Create an Engine and Car using composition
        Engine engine = new Engine("V6", 300);
        Car car = new Car("Toyota", "Camry", engine);
        System.out.println("Exercise 1 - Car with Engine: "
            + ("Toyota".equals(car.getMake()) && "Camry".equals(car.getModel()) ? "PASS" : "FAIL"));

        // Test Exercise 2: Car delegates start() to Engine
        String startResult = car.start();
        System.out.println("Exercise 2 - car.start(): "
            + (startResult != null && startResult.contains("started") ? "PASS" : "FAIL"));

        // Test Exercise 3: Engine reports its state
        String info = engine.getInfo();
        System.out.println("Exercise 3 - engine.getInfo(): "
            + (info != null && info.contains("V6") && info.contains("300") ? "PASS" : "FAIL"));

        // Test Exercise 4: Car composes multiple objects
        Car complexCar = new Car("Ford", "Mustang", new Engine("V8", 450));
        System.out.println("Exercise 4 - Complex car: "
            + (complexCar.getEngine().getHorsepower() == 450 ? "PASS" : "FAIL"));

        // Test Exercise 5: Composition allows swapping components
        Engine newEngine = new Engine("Electric", 670);
        Car evCar = new Car("Tesla", "Model S", newEngine);
        System.out.println("Exercise 5 - Swappable engine: "
            + ("Electric".equals(evCar.getEngine().getType()) ? "PASS" : "FAIL"));
    }
}

/**
 * TODO 1: Complete the Engine class with:
 * - Private fields: type (String), horsepower (int)
 * - Constructor that initializes both fields
 * - Getters for type and horsepower
 * - getInfo() method returning "Engine{type='V6', hp=300}"
 */
class Engine {
    // YOUR CODE HERE
}

/**
 * TODO 2: Complete the Car class with:
 * - Private fields: make (String), model (String), engine (Engine)
 * - Constructor that initializes make, model, and engine
 * - Getters for all fields
 * - start() method that delegates to the engine and returns:
 *   "Car started with Engine{type='V6', hp=300}"
 *   Use engine.getInfo() to build the string
 */
class Car {
    // YOUR CODE HERE
}
