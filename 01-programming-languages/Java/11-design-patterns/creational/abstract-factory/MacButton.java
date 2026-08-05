package academy.javaengineering.patterns.creational;

public class MacButton implements Button {
    private Runnable clickAction;

    @Override
    public void render() {
        System.out.println("Rendering Mac-style button");
    }

    @Override
    public void onClick(Runnable action) {
        this.clickAction = action;
        System.out.println("Mac button click handler registered");
    }

    @Override
    public String getStyle() {
        return "mac";
    }

    public Runnable getClickAction() {
        return clickAction;
    }
}
