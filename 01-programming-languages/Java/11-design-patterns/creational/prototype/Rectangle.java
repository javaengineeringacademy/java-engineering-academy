package academy.javaengineering.patterns.creational;

public class Rectangle implements Shape {
    private double width;
    private double height;
    private String color;

    public Rectangle(double width, double height, String color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Rectangle(Rectangle source) {
        this.width = source.width;
        this.height = source.height;
        this.color = source.color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Rectangle: " + width + "x" + height + ", color=" + color);
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String getType() {
        return "Rectangle";
    }

    @Override
    public Rectangle clone() {
        return new Rectangle(this);
    }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
