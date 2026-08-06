# Decorator Design Pattern

## Overview
Decorator pattern attaches additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.

## When to Use
- You need to add responsibilities to objects dynamically without affecting other objects
- You need to support open/closed principle - extending functionality through composition
- You want to layer behaviors that can be combined in various ways

## Code Example

```java
public interface Coffee {
    String getDescription();
    double getCost();
}

public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }
}

public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", milk";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 1.5;
    }
}
```

## Common Mistakes
- Using decorators when inheritance would be simpler
- Creating too many decorator layers that are hard to debug
- Not maintaining the same interface as the component

## Interview Questions
1. What is the difference between Decorator and Proxy patterns?
2. How does Decorator pattern differ from inheritance?
3. Can decorators be removed after being applied?

## Performance

Each decorator layer adds one method delegation (~5-10ns per layer). Deeply nested decorators (5+) can add measurable overhead in tight loops. For I/O-bound operations, the overhead is negligible. Java's `BufferedInputStream` wrapping `FileInputStream` is a decorator — the buffering benefit far outweighs the delegation cost.

## Examples

```java
// Text formatting decorator chain
interface TextFormatter {
    String format(String text);
}

class PlainText implements TextFormatter {
    @Override
    public String format(String text) { return text; }
}

abstract class TextDecorator implements TextFormatter {
    protected final TextFormatter delegate;
    TextDecorator(TextFormatter delegate) { this.delegate = delegate; }
}

class BoldDecorator extends TextDecorator {
    BoldDecorator(TextFormatter delegate) { super(delegate); }
    
    @Override
    public String format(String text) {
        return "<b>" + delegate.format(text) + "</b>";
    }
}

class ItalicDecorator extends TextFormatter {
    private final TextFormatter delegate;
    ItalicDecorator(TextFormatter delegate) { this.delegate = delegate; }
    
    @Override
    public String format(String text) {
        return "<i>" + delegate.format(text) + "</i>";
    }
}

// Usage - stack decorators
TextFormatter formatter = new BoldDecorator(new ItalicDecorator(new PlainText()));
System.out.println(formatter.format("Hello"));
// Output: <b><i>Hello</i></b>
```

## Internal Working

Each decorator implements the same interface as the component it wraps. It holds a reference to the wrapped component and delegates calls to it, adding behavior before or after. The client sees only the interface — it does not know whether it is calling the original object or a decorator. Multiple decorators can be stacked: each wraps the previous one.

## Why This Concept Exists

Inheritance-based extension is static — the behavior is fixed at compile time. Decorator adds responsibilities dynamically at runtime. The classic example is I/O streams: `BufferedInputStream` decorates `FileInputStream` with buffering, `LineNumberInputStream` adds line counting. You can combine them arbitrarily. Subclassing would require classes for every combination (BufferedLineNumberFileInputStream, etc.).

## Pitfalls

1. **Many small classes**: Each decorator is a separate class — can bloat the codebase
2. **Order sensitivity**: `Bold(Italic(text))` differs from `Italic(Bold(text))` — order matters
3. **Identity confusion**: `instanceof` checks may fail for decorated objects
4. **Debugging difficulty**: Stack traces show decorator delegation chains
5. **Too many layers**: Deep decoration (5+) makes code hard to follow — consider composition instead

## References

- [Refactoring.Guru - Decorator Pattern](https://refactoring.guru/design-patterns/decorator)
- [Java I/O Stream Hierarchy](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/package-summary.html)
- [Head First Design Patterns - Decorator Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
