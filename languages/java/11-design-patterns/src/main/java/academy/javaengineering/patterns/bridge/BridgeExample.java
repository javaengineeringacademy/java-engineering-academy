package academy.javaengineering.patterns.bridge;

// Abstraction
interface Shape {
    void draw();
    void resize(double factor);
}

// Implementation
interface Color {
    String fill();
}

// Concrete Implementations
class Red implements Color {
    @Override
    public String fill() { return "Red"; }
}

class Blue implements Color {
    @Override
    public String fill() { return "Blue"; }
}

class Green implements Color {
    @Override
    public String fill() { return "Green"; }
}

// Refined Abstractions
class Circle implements Shape {
    private Color color;
    private double radius;
    
    public Circle(Color color, double radius) {
        this.color = color;
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing " + color.fill() + " circle with radius " + radius);
    }
    
    @Override
    public void resize(double factor) {
        radius *= factor;
        System.out.println("Resized circle to radius " + radius);
    }
}

class Rectangle implements Shape {
    private Color color;
    private double width;
    private double height;
    
    public Rectangle(Color color, double width, double height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing " + color.fill() + " rectangle " + width + "x" + height);
    }
    
    @Override
    public void resize(double factor) {
        width *= factor;
        height *= factor;
        System.out.println("Resized rectangle to " + width + "x" + height);
    }
}

class Triangle implements Shape {
    private Color color;
    private double base;
    private double height;
    
    public Triangle(Color color, double base, double height) {
        this.color = color;
        this.base = base;
        this.height = height;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing " + color.fill() + " triangle with base " + base + " and height " + height);
    }
    
    @Override
    public void resize(double factor) {
        base *= factor;
        height *= factor;
        System.out.println("Resized triangle to base " + base + " and height " + height);
    }
}

public class BridgeExample {
    public static void main(String[] args) {
        System.out.println("=== Bridge Pattern ===\n");
        
        Shape redCircle = new Circle(new Red(), 5);
        Shape blueRect = new Rectangle(new Blue(), 10, 20);
        Shape greenTriangle = new Triangle(new Green(), 8, 12);
        
        redCircle.draw();
        blueRect.draw();
        greenTriangle.draw();
        
        System.out.println("\nResizing:");
        redCircle.resize(2);
        blueRect.resize(0.5);
    }
}
