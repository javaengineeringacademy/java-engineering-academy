package academy.javaengineering.oop.examples;

/**
 * Classes Examples - Why classes exist and how to design them.
 * 
 * WHY CLASSES EXIST:
 * - Blueprint for creating objects
 * - Encapsulates data and behavior
 * - Enables code reuse
 * 
 * DESIGN PRINCIPLES:
 * - Single Responsibility: One class, one purpose
 * - Open/Closed: Open for extension, closed for modification
 * - Liskov Substitution: Subtypes must be substitutable
 */
public class ClassesExamples {

    public static void main(String[] args) {
        System.out.println("=== Classes Examples ===\n");

        // Example 1: Class Design
        example1_ClassDesign();

        // Example 2: Constructors
        example2_Constructors();

        // Example 3: Methods
        example3_Methods();
    }

    /**
     * WHY: Classes define the structure and behavior of objects.
     * 
     * ENGINEERING DECISION: Design classes before writing code.
     */
    private static void example1_ClassDesign() {
        System.out.println("--- Example 1: Class Design ---");

        // Good class design: clear purpose, proper encapsulation
        Rectangle rectangle = new Rectangle(5, 10);
        System.out.println("Rectangle: " + rectangle.getWidth() + " x " + rectangle.getHeight());
        System.out.println("Area: " + rectangle.calculateArea());
        System.out.println("Perimeter: " + rectangle.calculatePerimeter());
    }

    /**
     * WHY: Constructors initialize object state.
     * 
     * ENGINEERING DECISION: Use multiple constructors for flexibility.
     */
    private static void example2_Constructors() {
        System.out.println("\n--- Example 2: Constructors ---");

        // Different constructors for different use cases
        Student s1 = new Student("Alice");
        Student s2 = new Student("Bob", 3.5);
        Student s3 = new Student("Charlie", 3.8, "CS");

        s1.displayInfo();
        s2.displayInfo();
        s3.displayInfo();
    }

    /**
     * WHY: Methods define object behavior.
     * 
     * ENGINEERING DECISION: Methods should do one thing well.
     */
    private static void example3_Methods() {
        System.out.println("\n--- Example 3: Methods ---");

        Calculator calc = new Calculator();
        System.out.println("Add: " + calc.add(5, 3));
        System.out.println("Subtract: " + calc.subtract(10, 4));
        System.out.println("Multiply: " + calc.multiply(6, 7));
        System.out.println("Divide: " + calc.divide(15, 3));
    }

    // Supporting classes
    static class Rectangle {
        private double width;
        private double height;

        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        public double getWidth() { return width; }
        public double getHeight() { return height; }
        public double calculateArea() { return width * height; }
        public double calculatePerimeter() { return 2 * (width + height); }
    }

    static class Student {
        private String name;
        private double gpa;
        private String major;

        public Student(String name) {
            this(name, 0.0, "Undeclared");
        }

        public Student(String name, double gpa) {
            this(name, gpa, "Undeclared");
        }

        public Student(String name, double gpa, String major) {
            this.name = name;
            this.gpa = gpa;
            this.major = major;
        }

        public void displayInfo() {
            System.out.println(name + " - GPA: " + gpa + ", Major: " + major);
        }
    }

    static class Calculator {
        public int add(int a, int b) { return a + b; }
        public int subtract(int a, int b) { return a - b; }
        public int multiply(int a, int b) { return a * b; }
        public double divide(int a, int b) { return (double) a / b; }
    }
}
