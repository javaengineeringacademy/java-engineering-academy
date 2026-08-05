package academy.javaengineering.patterns.creational;

public interface Shape extends Cloneable {
    void draw();
    double area();
    String getType();
    Shape clone();
}
