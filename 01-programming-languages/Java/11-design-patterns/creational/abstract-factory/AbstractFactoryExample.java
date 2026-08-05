package academy.javaengineering.patterns.creational;

public class AbstractFactoryExample {

    private static void buildUI(AbstractFactory factory) {
        Button button = factory.createButton();
        TextBox textBox = factory.createTextBox();

        button.render();
        textBox.render();

        button.onClick(() -> System.out.println("Button clicked! Action: " + textBox.getText()));
        textBox.setText("Hello from " + button.getStyle() + " UI");
    }

    public static void main(String[] args) {
        String os = System.getProperty("os.name", "").toLowerCase();

        AbstractFactory factory;
        if (os.contains("mac")) {
            factory = new MacFactory();
        } else if (os.contains("win")) {
            factory = new WindowsFactory();
        } else {
            factory = new MacFactory();
        }

        System.out.println("=== Building UI for: " + os + " ===");
        buildUI(factory);
    }
}
