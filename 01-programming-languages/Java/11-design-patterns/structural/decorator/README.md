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
