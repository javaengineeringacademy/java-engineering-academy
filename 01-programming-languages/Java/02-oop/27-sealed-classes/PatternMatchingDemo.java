/**
 * PatternMatchingDemo.java
 *
 * Demonstrates pattern matching with sealed classes in Java 17.
 * Pattern matching allows you to test and destructure objects in a single expression.
 *
 * Compile with: javac PatternMatchingDemo.java
 * Run with: java PatternMatchingDemo
 *
 * Expected Output:
 * === Pattern Matching with Sealed Classes (Java 17) ===
 *
 * --- 1. Basic Pattern Matching with instanceof ---
 * Before Java 17:
 *   Object is String: true, value: Hello World
 *   Object is Integer: true, value: 42
 *   Object is Double: true, value: 3.14
 *
 * With Java 17:
 *   Object is String: Hello World
 *   Object is Integer: 42
 *   Object is Double: 3.14
 *
 * --- 2. Pattern Matching with Sealed Classes ---
 * Processing Circle: radius=5.0, area=78.54
 * Processing Rectangle: width=4.0, height=6.0, area=24.00
 * Processing Triangle: base=3.0, height=8.0, area=12.00
 *
 * --- 3. Guarded Patterns ---
 * Small circle with radius 2.0
 * Large circle with radius 10.0
 * Tiny circle with radius 0.5
 *
 * --- 4. Pattern Matching in Switch Expressions ---
 * Shape is Circle: 78.54 sq units
 * Shape is Rectangle: 24.00 sq units
 * Shape is Triangle: 12.00 sq units
 * Unknown shape type
 *
 * --- 5. Destructuring with Pattern Matching ---
 * User: name=Alice, age=30, email=alice@example.com
 * Product: name=Widget, price=9.99, quantity=100
 * Order: id=ORD-001, total=$150.00, items=3
 *
 * --- 6. Nested Pattern Matching ---
 * Employee: name=John, department=Engineering, salary=$85000.00
 * Manager: name=Jane, department=Marketing, salary=$95000.00, teamSize=8
 * Executive: name=Bob, department=Executive, salary=$150000.00, teamSize=25, stockOptions=10000
 */
public class PatternMatchingDemo {

    // =====================================================
    // 1. Basic Pattern Matching with instanceof
    // =====================================================
    // Before Java 17, you had to cast after instanceof check
    // With Java 17, you can bind the variable directly

    static void basicPatternMatching() {
        System.out.println("--- 1. Basic Pattern Matching with instanceof ---");

        Object[] objects = {"Hello World", 42, 3.14, true, 'A'};

        // Before Java 17
        System.out.println("Before Java 17:");
        for (Object obj : objects) {
            if (obj instanceof String) {
                String s = (String) obj; // Explicit cast required
                System.out.println("  Object is String: true, value: " + s);
            } else if (obj instanceof Integer) {
                Integer i = (Integer) obj; // Explicit cast required
                System.out.println("  Object is Integer: true, value: " + i);
            } else if (obj instanceof Double) {
                Double d = (Double) obj; // Explicit cast required
                System.out.println("  Object is Double: true, value: " + d);
            }
        }

        System.out.println();

        // With Java 17
        System.out.println("With Java 17:");
        for (Object obj : objects) {
            if (obj instanceof String s) {
                System.out.println("  Object is String: " + s);
            } else if (obj instanceof Integer i) {
                System.out.println("  Object is Integer: " + i);
            } else if (obj instanceof Double d) {
                System.out.println("  Object is Double: " + d);
            }
        }

        System.out.println();
    }

    // =====================================================
    // 2. Pattern Matching with Sealed Classes
    // =====================================================
    // Sealed classes enable exhaustive pattern matching

    public sealed interface Shape permits Circle, Rectangle, Triangle {
        double area();
    }

    public record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    public record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }

    public record Triangle(double base, double height) implements Shape {
        @Override
        public double area() {
            return 0.5 * base * height;
        }
    }

    static void patternMatchingWithSealedClasses() {
        System.out.println("--- 2. Pattern Matching with Sealed Classes ---");

        Shape[] shapes = {
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 8.0)
        };

        for (Shape shape : shapes) {
            // Pattern matching with sealed classes
            if (shape instanceof Circle c) {
                System.out.printf("Processing Circle: radius=%.1f, area=%.2f%n",
                        c.radius(), c.area());
            } else if (shape instanceof Rectangle r) {
                System.out.printf("Processing Rectangle: width=%.1f, height=%.1f, area=%.2f%n",
                        r.width(), r.height(), r.area());
            } else if (shape instanceof Triangle t) {
                System.out.printf("Processing Triangle: base=%.1f, height=%.1f, area=%.2f%n",
                        t.base(), t.height(), t.area());
            }
        }

        System.out.println();
    }

    // =====================================================
    // 3. Guarded Patterns
    // =====================================================
    // You can add conditions to patterns using 'when' clauses

    static void guardedPatterns() {
        System.out.println("--- 3. Guarded Patterns ---");

        Shape[] circles = {
                new Circle(2.0),
                new Circle(10.0),
                new Circle(0.5)
        };

        for (Shape shape : circles) {
            // Guarded pattern with 'when' clause
            if (shape instanceof Circle c) {
                String size = switch (c) {
                    case Circle c2 when c2.radius() < 1.0 -> "Tiny circle with radius " + c2.radius();
                    case Circle c2 when c2.radius() <= 5.0 -> "Small circle with radius " + c2.radius();
                    case Circle c2 when c2.radius() <= 10.0 -> "Medium circle with radius " + c2.radius();
                    default -> "Large circle with radius " + c.radius();
                };
                System.out.println(size);
            }
        }

        System.out.println();
    }

    // =====================================================
    // 4. Pattern Matching in Switch Expressions
    // =====================================================
    // Switch expressions with pattern matching provide concise syntax

    static void patternMatchingInSwitch() {
        System.out.println("--- 4. Pattern Matching in Switch Expressions ---");

        Shape[] shapes = {
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 8.0),
                null
        };

        for (Shape shape : shapes) {
            // Pattern matching in switch expression
            String description = switch (shape) {
                case Circle c -> String.format("Shape is Circle: %.2f sq units", c.area());
                case Rectangle r -> String.format("Shape is Rectangle: %.2f sq units", r.area());
                case Triangle t -> String.format("Shape is Triangle: %.2f sq units", t.area());
                case null -> "Unknown shape type";
            };
            System.out.println(description);
        }

        System.out.println();
    }

    // =====================================================
    // 5. Destructuring with Pattern Matching
    // =====================================================
    // Pattern matching can destructure complex objects

    public sealed interface Entity permits User, Product, Order {
        String id();
    }

    public record User(String id, String name, int age, String email) implements Entity {
    }

    public record Product(String id, String name, double price, int quantity) implements Entity {
    }

    public record Order(String id, double total, int itemCount) implements Entity {
    }

    static void destructuringWithPatternMatching() {
        System.out.println("--- 5. Destructuring with Pattern Matching ---");

        Entity[] entities = {
                new User("USR-001", "Alice", 30, "alice@example.com"),
                new Product("PRD-001", "Widget", 9.99, 100),
                new Order("ORD-001", 150.00, 3)
        };

        for (Entity entity : entities) {
            // Pattern matching with destructuring
            if (entity instanceof User u) {
                System.out.printf("User: name=%s, age=%d, email=%s%n",
                        u.name(), u.age(), u.email());
            } else if (entity instanceof Product p) {
                System.out.printf("Product: name=%s, price=$%.2f, quantity=%d%n",
                        p.name(), p.price(), p.quantity());
            } else if (entity instanceof Order o) {
                System.out.printf("Order: id=%s, total=$%.2f, items=%d%n",
                        o.id(), o.total(), o.itemCount());
            }
        }

        System.out.println();
    }

    // =====================================================
    // 6. Nested Pattern Matching
    // =====================================================
    // Pattern matching works with nested objects

    public sealed interface Employee permits Manager, Executive {
        String name();
        String department();
        double salary();
    }

    public record Manager(String name, String department, double salary, int teamSize)
            implements Employee {
    }

    public record Executive(String name, String department, double salary,
                            int teamSize, long stockOptions) implements Employee {
    }

    static void nestedPatternMatching() {
        System.out.println("--- 6. Nested Pattern Matching ---");

        Employee[] employees = {
                new Manager("John", "Engineering", 85000.00, 8),
                new Manager("Jane", "Marketing", 95000.00, 8),
                new Executive("Bob", "Executive", 150000.00, 25, 10000)
        };

        for (Employee employee : employees) {
            // Pattern matching with nested destructuring
            if (employee instanceof Manager m) {
                System.out.printf("Employee: name=%s, department=%s, salary=$%.2f%n",
                        m.name(), m.department(), m.salary());
                System.out.printf("  Team size: %d%n", m.teamSize());
            } else if (employee instanceof Executive e) {
                System.out.printf("Executive: name=%s, department=%s, salary=$%.2f%n",
                        e.name(), e.department(), e.salary());
                System.out.printf("  Team size: %d, Stock options: %,d%n",
                        e.teamSize(), e.stockOptions());
            }
        }

        System.out.println();
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Pattern Matching with Sealed Classes (Java 17) ===\n");

        basicPatternMatching();
        patternMatchingWithSealedClasses();
        guardedPatterns();
        patternMatchingInSwitch();
        destructuringWithPatternMatching();
        nestedPatternMatching();

        System.out.println("\n=== Complete ===");
    }
}
