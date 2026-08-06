package academy.javaengineering.exercises;

/**
 * Exercises: Inheritance and Method Overriding
 *
 * Complete the TODO sections below.
 */
public class InheritanceExercises {

    // TODO 1: Create a Shape hierarchy
    // Base class: Shape with color (String) and filled (boolean)
    // - Constructor: Shape(String color, boolean filled)
    // - Methods: getColor(), isFilled(), toString() returning "Shape[color=X, filled=Y]"
    //
    // Subclass: Circle extends Shape with radius (double)
    // - Constructor: Circle(double radius, String color, boolean filled)
    // - Methods: getRadius(), getArea(), getPerimeter()
    // - Override toString() returning "Circle[radius=X, color=Y, filled=Z]"
    //
    // Subclass: Rectangle extends Shape with width (double) and height (double)
    // - Constructor: Rectangle(double width, double height, String color, boolean filled)
    // - Methods: getWidth(), getHeight(), getArea(), getPerimeter()
    // - Override toString() returning "Rectangle[width=X, height=Y, color=Z, filled=W]"

    // TODO 2: Create an Employee hierarchy
    // Base class: Employee with name (String), id (int), baseSalary (double)
    // - Constructor: Employee(String name, int id, double baseSalary)
    // - Methods: getName(), getId(), getSalary() returns baseSalary, getRole() returns "Employee"
    // - toString() returns "Employee[id=X, name=Y]"
    //
    // Subclass: Manager extends Employee with teamSize (int)
    // - Constructor: Manager(String name, int id, double baseSalary, int teamSize)
    // - Override getSalary() returns baseSalary + (teamSize * 500)
    // - Override getRole() returns "Manager"
    // - getTeamSize() returns teamSize
    //
    // Subclass: Developer extends Employee with programmingLanguage (String)
    // - Constructor: Developer(String name, int id, double baseSalary, String language)
    // - Override getSalary() returns baseSalary + 1000 (skill bonus)
    // - Override getRole() returns "Developer"
    // - getProgrammingLanguage() returns programmingLanguage

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        System.out.println("=== InheritanceExercises Tests ===\n");

        // Test 1: Shape hierarchy
        total++;
        try {
            // Uncomment after implementing Shape classes
            // Shape shape = new Shape("red", true);
            // Circle circle = new Circle(5.0, "blue", false);
            // Rectangle rect = new Rectangle(4.0, 6.0, "green", true);
            //
            // if ("red".equals(shape.getColor())
            //     && shape.isFilled()
            //     && Math.abs(circle.getArea() - 78.54) < 0.01
            //     && Math.abs(rect.getPerimeter() - 20.0) < 0.01
            //     && circle.toString().contains("5.0")
            //     && rect.toString().contains("4.0")) {
            //     System.out.println("Test 1 PASSED: Shape hierarchy");
            //     passed++;
            // } else {
            //     System.out.println("Test 1 FAILED: Shape hierarchy");
            // }
            System.out.println("Test 1 SKIPPED: Shape hierarchy - implement classes");
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.out.println("Test 1 FAILED: Shape hierarchy - " + e.getMessage());
        }

        // Test 2: Employee hierarchy
        total++;
        try {
            // Uncomment after implementing Employee classes
            // Employee emp = new Employee("John", 1, 50000);
            // Manager mgr = new Manager("Jane", 2, 60000, 5);
            // Developer dev = new Developer("Bob", 3, 55000, "Java");
            //
            // if (Math.abs(emp.getSalary() - 50000) < 0.01
            //     && "Employee".equals(emp.getRole())
            //     && Math.abs(mgr.getSalary() - 62500) < 0.01
            //     && "Manager".equals(mgr.getRole())
            //     && mgr.getTeamSize() == 5
            //     && Math.abs(dev.getSalary() - 56000) < 0.01
            //     && "Developer".equals(dev.getRole())
            //     && "Java".equals(dev.getProgrammingLanguage())) {
            //     System.out.println("Test 2 PASSED: Employee hierarchy");
            //     passed++;
            // } else {
            //     System.out.println("Test 2 FAILED: Employee hierarchy");
            // }
            System.out.println("Test 2 SKIPPED: Employee hierarchy - implement classes");
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.out.println("Test 2 FAILED: Employee hierarchy - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
        System.out.println("Note: Uncomment the test code above after implementing the classes.");
    }
}
