/**
 * Base Shape class for polymorphism demonstration.
 */
public class Shape {

    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public String getColor() { return color; }

    /** Base implementation - will be overridden. */
    public double area() {
        return 0.0;
    }

    public String describe() {
        return "Shape[color=" + color + "]";
    }

    @Override
    public String toString() {
        return "%s{color='%s', area=%.2f}".formatted(
                getClass().getSimpleName(), color, area());
    }
}