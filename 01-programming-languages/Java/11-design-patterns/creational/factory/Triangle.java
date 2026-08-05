package academy.javaengineering.patterns.creational;

public class Triangle implements Shape {
    private final double base;
    private final double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Triangle: base=" + base + ", height=" + height);
    }

    @Override
    public double area() {
        return 0.5 * base * height;
    }

    @Override
    public String getType() {
        return "Triangle";
    }

    public double getBase() {
        return base;
    }

    public double getHeight() {
        return height;
    }
}
