package academy.javaengineering.patterns.creational;

public interface TextBox {
    void render();
    void setText(String text);
    String getText();
    String getStyle();
}
