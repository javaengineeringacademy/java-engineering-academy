package academy.javaengineering.patterns.structural.flyweight;

public interface Flyweight {
    void operation(String extrinsicState);
    String getType();
}
