package academy.javaengineering.patterns.creational;

public class MacFactory implements AbstractFactory {

    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public TextBox createTextBox() {
        return new MacTextBox();
    }
}
