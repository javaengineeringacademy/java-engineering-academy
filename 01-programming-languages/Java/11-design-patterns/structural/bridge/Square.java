package academy.javaengineering.patterns.structural.bridge;

public class Square extends Shape {

    public Square(Color color) {
        super(color);
    }

    @Override
    public String draw() {
        return "Square " + color.fill();
    }

    @Override
    public String getShapeName() {
        return "square";
    }
}
