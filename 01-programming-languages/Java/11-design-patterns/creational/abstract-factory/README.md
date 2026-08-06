# Abstract Factory Pattern

## Overview
Abstract Factory provides an interface for creating families of related objects without specifying their concrete classes.

## When to Use
- UI toolkit implementations (Windows, Mac, Linux)
- Database driver families
- Cross-platform applications
- When object families must be used together

## Code Structure

### Abstract Factory
```java
public interface AbstractFactory {
    Button createButton();
    TextBox createTextBox();
}
```

### Concrete Factory
```java
public class WindowsFactory implements AbstractFactory {
    public Button createButton() { return new WindowsButton(); }
    public TextBox createTextBox() { return new WindowsTextBox(); }
}
```

### Client Code
```java
AbstractFactory factory = new WindowsFactory();
Button button = factory.createButton();
TextBox textBox = factory.createTextBox();
```

## Common Mistakes
1. Creating too many product types
2. Not ensuring family consistency
3. Over-engineering when simple Factory suffices
4. Hardcoding factory selection

## Interview Questions
1. What is the difference between Factory and Abstract Factory?
2. How does Abstract Factory enforce product family consistency?
3. When would you choose Abstract Factory over Factory Method?
4. How does Abstract Factory relate to dependency injection?
5. What are the drawbacks of Abstract Factory?

## Performance

Abstract factory adds ~10-50ns overhead (factory method call plus constructor). The real cost is in the product creation itself, which is the same regardless of the factory pattern used. For UI toolkits, the factory is created once at startup — the per-object overhead is irrelevant. In performance-critical paths, pre-instantiate products and cache them.

## Examples

```java
// Cross-platform UI factory
interface Button {
    void render();
    void onClick(Runnable handler);
}

interface TextBox {
    void render();
    void setText(String text);
}

class WindowsButton implements Button {
    @Override
    public void render() { System.out.println("Windows Button"); }
    @Override
    public void onClick(Runnable handler) { handler.run(); }
}

class MacButton implements Button {
    @Override
    public void render() { System.out.println("Mac Button"); }
    @Override
    public void onClick(Runnable handler) { handler.run(); }
}

class WindowsTextBox implements TextBox {
    @Override
    public void render() { System.out.println("Windows TextBox"); }
    @Override
    public void setText(String text) { System.out.println("Windows: " + text); }
}

class MacTextBox implements TextBox {
    @Override
    public void render() { System.out.println("Mac TextBox"); }
    @Override
    public void setText(String text) { System.out.println("Mac: " + text); }
}

interface GUIFactory {
    Button createButton();
    TextBox createTextBox();
}

class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() { return new WindowsButton(); }
    @Override
    public TextBox createTextBox() { return new WindowsTextBox(); }
}

class MacFactory implements GUIFactory {
    @Override
    public Button createButton() { return new MacButton(); }
    @Override
    public TextBox createTextBox() { return new MacTextBox(); }
}

// Usage
GUIFactory factory = System.getProperty("os.name").contains("Windows")
    ? new WindowsFactory() : new MacFactory();
Button button = factory.createButton();
button.render();
```

## Internal Working

The abstract factory declares creation methods for each product type. Concrete factories implement these methods to return platform-specific products. The client calls factory methods and receives abstract product types — it never sees concrete classes. Adding a new product family means creating a new concrete factory; adding a new product type means modifying the abstract factory interface and all concrete factories.

## Why This Concept Exists

When objects come in families (WindowsButton + WindowsTextBox, MacButton + MacTextBox), mixing families breaks consistency. Abstract factory ensures all products in a client belong to the same family. It decouples client code from concrete types, enabling runtime selection of product families (e.g., based on OS or configuration).

## Pitfalls

1. **Rigid interface**: Adding a new product type requires modifying ALL concrete factories
2. **Factory explosion**: Each new platform requires a new factory class
3. **Over-engineering**: Simple systems with 1-2 product types don't need abstract factory
4. **Hard to test**: Testing requires mocking the factory, which adds complexity
5. **Tight coupling**: Concrete factories are often selected by conditional logic in one place

## References

- [Refactoring.Guru - Abstract Factory](https://refactoring.guru/design-patterns/abstract-factory)
- [Head First Design Patterns - Abstract Factory](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java AWT Toolkit](https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/java/awt/Toolkit.html)
