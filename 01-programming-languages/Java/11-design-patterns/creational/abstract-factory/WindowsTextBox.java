package academy.javaengineering.patterns.creational;

public class WindowsTextBox implements TextBox {
    private String text = "";

    @Override
    public void render() {
        System.out.println("Rendering Windows-style textbox");
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getStyle() {
        return "windows";
    }
}
