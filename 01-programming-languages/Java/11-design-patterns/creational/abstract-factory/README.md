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
