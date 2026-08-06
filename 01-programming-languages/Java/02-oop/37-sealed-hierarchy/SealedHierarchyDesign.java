/**
 * Sealed Classes in Java (Java 17+)
 * Controlled class hierarchies with pattern matching.
 */
public class SealedHierarchyDesign {

    // --- Sealed class declaration ---
    sealed interface Shape
            permits Circle, Rectangle, Triangle, CompoundShape {
    }

    record Circle(double radius) implements Shape {
        double area() { return Math.PI * radius * radius; }
    }

    record Rectangle(double width, double height) implements Shape {
        double area() { return width * height; }
    }

    record Triangle(double base, double height) implements Shape {
        double area() { return 0.5 * base * height; }
    }

    sealed interface CompoundShape extends Shape
            permits CompositeShape, ShapeGroup {
    }

    record CompositeShape(Shape outer, Shape inner) implements CompoundShape {
        double totalArea() { return outer_area() + inner_area(); }
        // Note: would need helper methods for area calculation
    }

    record ShapeGroup(java.util.List<Shape> shapes) implements CompoundShape {
        double totalArea() {
            return shapes.stream()
                .mapToDouble(SealedHierarchyDesign::shapeArea)
                .sum();
        }
    }

    // --- Sealed class for domain modeling ---
    sealed interface OrderEvent
            permits OrderPlaced, OrderShipped, OrderDelivered, OrderCancelled {
    }

    record OrderPlaced(String orderId, String customerId, double total)
            implements OrderEvent {}
    record OrderShipped(String orderId, String trackingNumber, String carrier)
            implements OrderEvent {}
    record OrderDelivered(String orderId, String deliveredTo, java.time.Instant time)
            implements OrderEvent {}
    record OrderCancelled(String orderId, String reason)
            implements OrderEvent {}

    // --- Sealed with permits in same file ---
    sealed interface Result<T>
            permits Success, Failure, Loading {
    }

    record Success<T>(T data) implements Result<T> {}
    record Failure<T>(String error, Exception cause) implements Result<T> {}
    record Loading<T>() implements Result<T> {}

    // --- Pattern matching with sealed ---
    static String describeShape(Shape shape) {
        return switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
            case Triangle t -> "Triangle base=" + t.base() + " height=" + t.height();
            case CompoundShape cs -> "Compound shape";
            // No default needed - compiler knows all cases covered!
        };
    }

    static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c -> c.area();
            case Rectangle r -> r.area();
            case Triangle t -> t.area();
            case CompoundShape cs -> 0.0; // Handle compound
        };
    }

    // --- Pattern matching with guards ---
    static String classifyShape(Shape shape) {
        return switch (shape) {
            case Circle c when c.radius() < 1 -> "Small circle";
            case Circle c when c.radius() > 10 -> "Large circle";
            case Circle c -> "Medium circle";
            case Rectangle r when r.width() == r.height() -> "Square";
            case Rectangle r -> "Rectangle";
            case Triangle t when t.base() == t.height() -> "Isosceles triangle";
            case Triangle t -> "Triangle";
            case CompoundShape cs -> "Compound";
        };
    }

    // --- Process order events with sealed ---
    static void processEvent(OrderEvent event) {
        switch (event) {
            case OrderPlaced e -> {
                System.out.println("Order placed: " + e.orderId() +
                    " total: $" + e.total());
            }
            case OrderShipped e -> {
                System.out.println("Order shipped: " + e.orderId() +
                    " via " + e.carrier());
            }
            case OrderDelivered e -> {
                System.out.println("Order delivered: " + e.orderId() +
                    " at " + e.time());
            }
            case OrderCancelled e -> {
                System.out.println("Order cancelled: " + e.orderId() +
                    " reason: " + e.reason());
            }
        }
    }

    // --- Result type pattern ---
    static <T> String describeResult(Result<T> result) {
        return switch (result) {
            case Success<T> s -> "Success: " + s.data();
            case Failure<T> f -> "Failure: " + f.error();
            case Loading<T> l -> "Loading...";
        };
    }

    public static void main(String[] args) {
        System.out.println("=== Sealed Classes in Java ===\n");

        sealedBasics();
        patternMatching();
        sealedForDomain();
        sealedWithGuards();
        resultPattern();

        System.out.println("\n=== Complete ===");
    }

    static void sealedBasics() {
        System.out.println("--- Sealed Class Basics ---");

        Shape circle = new Circle(5);
        Shape rect = new Rectangle(4, 6);
        Shape tri = new Triangle(3, 8);

        System.out.println("Circle: " + describeShape(circle));
        System.out.println("Rectangle: " + describeShape(rect));
        System.out.println("Triangle: " + describeShape(tri));

        System.out.println("Circle area: " + calculateArea(circle));
        System.out.println("Rect area: " + calculateArea(rect));

        System.out.println();
    }

    static void patternMatching() {
        System.out.println("--- Pattern Matching ---");

        Shape[] shapes = {
            new Circle(3),
            new Rectangle(5, 5),
            new Triangle(4, 6),
            new Circle(12)
        };

        for (Shape s : shapes) {
            String desc = switch (s) {
                case Circle c -> "Circle(r=" + c.radius() + ")";
                case Rectangle r -> "Rect(" + r.width() + "x" + r.height() + ")";
                case Triangle t -> "Tri(b=" + t.base() + ")";
                case CompoundShape cs -> "Compound";
            };
            System.out.println(desc);
        }

        System.out.println();
    }

    static void sealedForDomain() {
        System.out.println("--- Sealed for Domain Events ---");

        OrderEvent[] events = {
            new OrderPlaced("ORD-1", "CUST-1", 99.99),
            new OrderShipped("ORD-1", "TRACK-123", "FedEx"),
            new OrderDelivered("ORD-1", "Front door",
                java.time.Instant.now()),
            new OrderCancelled("ORD-2", "Customer request")
        };

        for (OrderEvent event : events) {
            processEvent(event);
        }

        System.out.println();
    }

    static void sealedWithGuards() {
        System.out.println("--- Sealed with Guards ---");

        Shape[] shapes = {
            new Circle(0.5),
            new Circle(5),
            new Circle(15),
            new Rectangle(4, 4),
            new Rectangle(3, 7),
            new Triangle(5, 5)
        };

        for (Shape s : shapes) {
            System.out.println(s + " -> " + classifyShape(s));
        }

        System.out.println();
    }

    static void resultPattern() {
        System.out.println("--- Result Type Pattern ---");

        Result<String> success = new Success<>("data loaded");
        Result<String> failure = new Failure<>("timeout", new Exception("conn refused"));
        Result<String> loading = new Loading<>();

        System.out.println(describeResult(success));
        System.out.println(describeResult(failure));
        System.out.println(describeResult(loading));

        // Compile-time safety: must handle all cases
        // Missing case would be compile error
        // switch (shape) {
        //     case Circle c -> // ...
        //     // Missing Rectangle, Triangle, etc = compile error
        // }

        System.out.println();
    }

    static double shapeArea(Shape shape) {
        return switch (shape) {
            case Circle c -> c.area();
            case Rectangle r -> r.area();
            case Triangle t -> t.area();
            case CompoundShape cs -> 0;
        };
    }
}
