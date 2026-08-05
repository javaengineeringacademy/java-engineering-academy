package academy.javaengineering.patterns.structural.bridge;

public class Circle extends Shape {

    public Circle(Color color) {
        super(color);
    }

    @Override
    public String draw() {
        return "Circle " + color.fill();
    }

    @Override
    public String getShapeName() {
        return "circle";
    }
}
