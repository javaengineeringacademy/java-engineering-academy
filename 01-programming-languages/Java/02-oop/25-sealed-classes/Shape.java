public sealed class Shape permits Circle, Rectangle {

    private final String color;

    public Shape(String color) {
        this.color = color;
    }

    public String getColor() { return color; }

    public double area() {
        return 0.0;
    }
}