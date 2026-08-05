package academy.javaengineering.patterns.creational;

public class MacTextBox implements TextBox {
    private String text = "";

    @Override
    public void render() {
        System.out.println("Rendering Mac-style textbox");
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
        return "mac";
    }
}
