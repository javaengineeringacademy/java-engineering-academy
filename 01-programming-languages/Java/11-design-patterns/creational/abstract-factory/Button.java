package academy.javaengineering.patterns.creational;

public interface Button {
    void render();
    void onClick(Runnable action);
    String getStyle();
}
