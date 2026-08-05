package academy.javaengineering.patterns.creational;

public class WindowsButton implements Button {
    private Runnable clickAction;

    @Override
    public void render() {
        System.out.println("Rendering Windows-style button");
    }

    @Override
    public void onClick(Runnable action) {
        this.clickAction = action;
        System.out.println("Windows button click handler registered");
    }

    @Override
    public String getStyle() {
        return "windows";
    }

    public Runnable getClickAction() {
        return clickAction;
    }
}
