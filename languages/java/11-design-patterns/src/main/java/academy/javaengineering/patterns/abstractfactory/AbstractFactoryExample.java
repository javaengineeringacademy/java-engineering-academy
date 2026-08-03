package academy.javaengineering.patterns.abstractfactory;

// Abstract Product
interface Button {
    void render();
    void onClick(Runnable action);
}

interface Checkbox {
    void render();
    void toggle();
}

// Concrete Products - Windows
class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("[Windows Button]");
    }
    
    @Override
    public void onClick(Runnable action) {
        System.out.println("Windows button clicked");
        action.run();
    }
}

class WindowsCheckbox implements Checkbox {
    private boolean checked = false;
    
    @Override
    public void render() {
        System.out.println("[Windows Checkbox] " + (checked ? "✓" : "☐"));
    }
    
    @Override
    public void toggle() {
        checked = !checked;
        System.out.println("Windows checkbox toggled: " + checked);
    }
}

// Concrete Products - Mac
class MacButton implements Button {
    @Override
    public void render() {
        System.out.println("[Mac Button]");
    }
    
    @Override
    public void onClick(Runnable action) {
        System.out.println("Mac button clicked");
        action.run();
    }
}

class MacCheckbox implements Checkbox {
    private boolean checked = false;
    
    @Override
    public void render() {
        System.out.println("[Mac Checkbox] " + (checked ? "●" : "○"));
    }
    
    @Override
    public void toggle() {
        checked = !checked;
        System.out.println("Mac checkbox toggled: " + checked);
    }
}

// Abstract Factory
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

// Concrete Factories
class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }
    
    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}

class MacFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }
    
    @Override
    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}

// Client
class Application {
    private final Button button;
    private final Checkbox checkbox;
    
    public Application(GUIFactory factory) {
        this.button = factory.createButton();
        this.checkbox = factory.createCheckbox();
    }
    
    public void render() {
        button.render();
        checkbox.render();
    }
    
    public void interact() {
        button.onClick(() -> checkbox.toggle());
    }
}

// Factory Provider
class FactoryProvider {
    public static GUIFactory getFactory(String osType) {
        return switch (osType.toLowerCase()) {
            case "windows" -> new WindowsFactory();
            case "mac" -> new MacFactory();
            default -> throw new IllegalArgumentException("Unknown OS: " + osType);
        };
    }
}

public class AbstractFactoryExample {
    public static void main(String[] args) {
        System.out.println("=== Abstract Factory Pattern ===\n");
        
        String osType = "mac";
        GUIFactory factory = FactoryProvider.getFactory(osType);
        Application app = new Application(factory);
        
        System.out.println("OS: " + osType);
        app.render();
        app.interact();
    }
}
