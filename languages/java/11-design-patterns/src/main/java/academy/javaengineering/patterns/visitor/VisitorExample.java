package academy.javaengineering.patterns.visitor;

import java.util.ArrayList;
import java.util.List;

// Visitor Interface
interface ShapeVisitor {
    void visit(Circle circle);
    void visit(Rectangle rectangle);
    void visit(Triangle triangle);
    void visit(CompoundShape compound);
}

// Element Interface
interface Shape {
    void accept(ShapeVisitor visitor);
    String getName();
}

// Concrete Elements
class Circle implements Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    public double getRadius() { return radius; }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String getName() { return "Circle"; }
}

class Rectangle implements Shape {
    private final double width;
    private final double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String getName() { return "Rectangle"; }
}

class Triangle implements Shape {
    private final double base;
    private final double height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    public double getBase() { return base; }
    public double getHeight() { return height; }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String getName() { return "Triangle"; }
}

class CompoundShape implements Shape {
    private final List<Shape> children = new ArrayList<>();
    private final String name;
    
    public CompoundShape(String name) {
        this.name = name;
    }
    
    public void add(Shape shape) {
        children.add(shape);
    }
    
    public List<Shape> getChildren() { return children; }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
        for (Shape child : children) {
            child.accept(visitor);
        }
    }
    
    @Override
    public String getName() { return name; }
}

// Concrete Visitors
class AreaCalculator implements ShapeVisitor {
    private double totalArea = 0;
    
    @Override
    public void visit(Circle circle) {
        double area = Math.PI * circle.getRadius() * circle.getRadius();
        System.out.println("Circle area: " + String.format("%.2f", area));
        totalArea += area;
    }
    
    @Override
    public void visit(Rectangle rectangle) {
        double area = rectangle.getWidth() * rectangle.getHeight();
        System.out.println("Rectangle area: " + area);
        totalArea += area;
    }
    
    @Override
    public void visit(Triangle triangle) {
        double area = 0.5 * triangle.getBase() * triangle.getHeight();
        System.out.println("Triangle area: " + area);
        totalArea += area;
    }
    
    @Override
    public void visit(CompoundShape compound) {
        System.out.println("Compound shape '" + compound.getName() + "' with " + compound.getChildren().size() + " children");
    }
    
    public double getTotalArea() { return totalArea; }
}

class SvgExporter implements ShapeVisitor {
    private final StringBuilder svg = new StringBuilder();
    
    @Override
    public void visit(Circle circle) {
        svg.append(String.format("<circle r=\"%.2f\"/>\n", circle.getRadius()));
        System.out.println("Exported circle to SVG");
    }
    
    @Override
    public void visit(Rectangle rectangle) {
        svg.append(String.format("<rect width=\"%.2f\" height=\"%.2f\"/>\n", 
            rectangle.getWidth(), rectangle.getHeight()));
        System.out.println("Exported rectangle to SVG");
    }
    
    @Override
    public void visit(Triangle triangle) {
        svg.append(String.format("<polygon points=\"0,%.2f %.2f,0 0,0\"/>\n", 
            triangle.getHeight(), triangle.getBase()));
        System.out.println("Exported triangle to SVG");
    }
    
    @Override
    public void visit(CompoundShape compound) {
        svg.append("<g>\n");
        System.out.println("Exporting compound shape '" + compound.getName() + "'");
    }
    
    public String getSvg() {
        return "<svg>\n" + svg + "</svg>";
    }
}

public class VisitorExample {
    public static void main(String[] args) {
        System.out.println("=== Visitor Pattern ===\n");
        
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5));
        shapes.add(new Rectangle(4, 6));
        shapes.add(new Triangle(3, 8));
        
        CompoundShape compound = new CompoundShape("Group 1");
        compound.add(new Circle(2));
        compound.add(new Rectangle(3, 4));
        shapes.add(compound);
        
        System.out.println("--- Area Calculator ---");
        AreaCalculator areaCalc = new AreaCalculator();
        for (Shape shape : shapes) {
            shape.accept(areaCalc);
            System.out.println();
        }
        System.out.println("Total area: " + String.format("%.2f", areaCalc.getTotalArea()));
        
        System.out.println("\n--- SVG Exporter ---");
        SvgExporter svgExporter = new SvgExporter();
        for (Shape shape : shapes) {
            shape.accept(svgExporter);
        }
        System.out.println("\nGenerated SVG:");
        System.out.println(svgExporter.getSvg());
    }
}
