/**
 * PatternMatchingDemo.java
 *
 * Demonstrates pattern matching for instanceof in Java 17.
 * This feature eliminates the need for explicit casting after instanceof checks.
 *
 * Compile with: javac PatternMatchingDemo.java
 * Run with: java PatternMatchingDemo
 *
 * Expected Output:
 * === Pattern Matching for instanceof (Java 17) ===
 *
 * --- 1. Before Java 17: Verbose Casting ---
 * Object type: String, value: Hello
 * Object type: Integer, value: 42
 * Object type: Double, value: 3.14
 * Object type: Boolean, value: true
 * Object type: Unknown
 *
 * --- 2. With Java 17: Pattern Matching ---
 * Object is String: Hello
 * Object is Integer: 42
 * Object is Double: 3.14
 * Object is Boolean: true
 * Object is Unknown type
 *
 * --- 3. Pattern Matching in Conditions ---
 * String 'Hello' has length 5
 * Integer 42 is positive
 * Double 3.14 is between 1 and 10
 * Boolean true is true
 *
 * --- 4. Pattern Matching with Sealed Classes ---
 * Shape is Circle: radius=5.0, area=78.54
 * Shape is Rectangle: width=4.0, height=6.0, area=24.00
 * Shape is Triangle: base=3.0, height=8.0, area=12.00
 *
 * --- 5. Pattern Matching with Records ---
 * Person: name=Alice, age=30, email=alice@example.com
 * Product: name=Widget, price=$9.99, quantity=100
 *
 * --- 6. Pattern Matching in Switch Expressions ---
 * String length: 5
 * Integer value: 42
 * Double value: 3.14
 * Boolean value: true
 * Unknown type
 *
 * --- 7. Pattern Matching with Guards ---
 * Small string: Hi
 * Medium string: Hello
 * Large string: Hello World!
 * Small integer: 5
 * Large integer: 100
 * Default: 42
 */
public class PatternMatchingDemo {

    // =====================================================
    // 1. Before Java 17: Verbose Casting
    // =====================================================
    // This shows the traditional approach without pattern matching

    static void beforePatternMatching() {
        System.out.println("--- 1. Before Java 17: Verbose Casting ---");

        Object[] objects = {"Hello", 42, 3.14, true, new int[]{1, 2, 3}};

        for (Object obj : objects) {
            // Traditional approach: check type, then cast
            if (obj instanceof String) {
                String s = (String) obj; // Explicit cast required
                System.out.println("Object type: String, value: " + s);
            } else if (obj instanceof Integer) {
                Integer i = (Integer) obj; // Explicit cast required
                System.out.println("Object type: Integer, value: " + i);
            } else if (obj instanceof Double) {
                Double d = (Double) obj; // Explicit cast required
                System.out.println("Object type: Double, value: " + d);
            } else if (obj instanceof Boolean) {
                Boolean b = (Boolean) obj; // Explicit cast required
                System.out.println("Object type: Boolean, value: " + b);
            } else {
                System.out.println("Object type: Unknown");
            }
        }

        System.out.println();
    }

    // =====================================================
    // 2. With Java 17: Pattern Matching
    // =====================================================
    // Pattern matching eliminates explicit casting

    static void withPatternMatching() {
        System.out.println("--- 2. With Java 17: Pattern Matching ---");

        Object[] objects = {"Hello", 42, 3.14, true, new int[]{1, 2, 3}};

        for (Object obj : objects) {
            // Pattern matching: check type and bind variable in one step
            if (obj instanceof String s) {
                System.out.println("Object is String: " + s);
            } else if (obj instanceof Integer i) {
                System.out.println("Object is Integer: " + i);
            } else if (obj instanceof Double d) {
                System.out.println("Object is Double: " + d);
            } else if (obj instanceof Boolean b) {
                System.out.println("Object is Boolean: " + b);
            } else {
                System.out.println("Object is Unknown type");
            }
        }

        System.out.println();
    }

    // =====================================================
    // 3. Pattern Matching in Conditions
    // =====================================================
    // Pattern matching can be used in complex conditions

    static void patternMatchingInConditions() {
        System.out.println("--- 3. Pattern Matching in Conditions ---");

        Object[] objects = {"Hello", 42, 3.14, true};

        for (Object obj : objects) {
            // Pattern matching with additional conditions
            if (obj instanceof String s && s.length() > 0) {
                System.out.printf("String '%s' has length %d%n", s, s.length());
            } else if (obj instanceof Integer i && i > 0) {
                System.out.printf("Integer %d is positive%n", i);
            } else if (obj instanceof Double d && d > 1.0 && d < 10.0) {
                System.out.printf("Double %.2f is between 1 and 10%n", d);
            } else if (obj instanceof Boolean b) {
                System.out.printf("Boolean %s is %s%n", b, b ? "true" : "false");
            }
        }

        System.out.println();
    }

    // =====================================================
    // 4. Pattern Matching with Sealed Classes
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
        System.out.println("--- 4. Pattern Matching with Sealed Classes ---");

        Shape[] shapes = {
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 8.0)
        };

        for (Shape shape : shapes) {
            // Exhaustive pattern matching with sealed classes
            if (shape instanceof Circle c) {
                System.out.printf("Shape is Circle: radius=%.1f, area=%.2f%n",
                        c.radius(), c.area());
            } else if (shape instanceof Rectangle r) {
                System.out.printf("Shape is Rectangle: width=%.1f, height=%.1f, area=%.2f%n",
                        r.width(), r.height(), r.area());
            } else if (shape instanceof Triangle t) {
                System.out.printf("Shape is Triangle: base=%.1f, height=%.1f, area=%.2f%n",
                        t.base(), t.height(), t.area());
            }
        }

        System.out.println();
    }

    // =====================================================
    // 5. Pattern Matching with Records
    // =====================================================
    // Records work naturally with pattern matching

    public record Person(String name, int age, String email) {
    }

    public record Product(String name, double price, int quantity) {
    }

    static void patternMatchingWithRecords() {
        System.out.println("--- 5. Pattern Matching with Records ---");

        Object[] records = {
                new Person("Alice", 30, "alice@example.com"),
                new Product("Widget", 9.99, 100)
        };

        for (Object obj : records) {
            // Pattern matching with records
            if (obj instanceof Person p) {
                System.out.printf("Person: name=%s, age=%d, email=%s%n",
                        p.name(), p.age(), p.email());
            } else if (obj instanceof Product pr) {
                System.out.printf("Product: name=%s, price=$%.2f, quantity=%d%n",
                        pr.name(), pr.price(), pr.quantity());
            }
        }

        System.out.println();
    }

    // =====================================================
    // 6. Pattern Matching in Switch Expressions
    // =====================================================
    // Switch expressions with pattern matching provide concise syntax

    static void patternMatchingInSwitch() {
        System.out.println("--- 6. Pattern Matching in Switch Expressions ---");

        Object[] objects = {"Hello", 42, 3.14, true, null};

        for (Object obj : objects) {
            // Pattern matching in switch expression
            String result = switch (obj) {
                case String s -> "String length: " + s.length();
                case Integer i -> "Integer value: " + i;
                case Double d -> "Double value: " + d;
                case Boolean b -> "Boolean value: " + b;
                case null -> "Unknown type";
                default -> "Other type: " + obj.getClass().getSimpleName();
            };
            System.out.println(result);
        }

        System.out.println();
    }

    // =====================================================
    // 7. Pattern Matching with Guards
    // =====================================================
    // Guards allow additional conditions in pattern matching

    static void patternMatchingWithGuards() {
        System.out.println("--- 7. Pattern Matching with Guards ---");

        Object[] objects = {"Hi", "Hello", "Hello World!", 5, 100, 42};

        for (Object obj : objects) {
            // Pattern matching with guards
            String result = switch (obj) {
                case String s when s.length() <= 3 -> "Small string: " + s;
                case String s when s.length() <= 7 -> "Medium string: " + s;
                case String s -> "Large string: " + s;
                case Integer i when i < 10 -> "Small integer: " + i;
                case Integer i when i > 50 -> "Large integer: " + i;
                case Integer i -> "Default: " + i;
                default -> "Unknown: " + obj;
            };
            System.out.println(result);
        }

        System.out.println();
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Pattern Matching for instanceof (Java 17) ===\n");

        beforePatternMatching();
        withPatternMatching();
        patternMatchingInConditions();
        patternMatchingWithSealedClasses();
        patternMatchingWithRecords();
        patternMatchingInSwitch();
        patternMatchingWithGuards();

        System.out.println("\n=== Complete ===");
    }
}
