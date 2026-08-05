# Abstract Factory Pattern (JavaScript)

## Overview

The Abstract Factory pattern provides an interface for creating families of related
objects without specifying their concrete classes. JavaScript's dynamic typing enables
flexible factory implementations.

## When to Use

- Creating families of related objects
- System needs to be independent of object creation
- Need to enforce constraints between related objects
- Platform-independent UI development

## JavaScript Implementation

### Basic Abstract Factory

```javascript
const VehicleFactory = {
  createFactory: (type) => {
    const factories = {
      car: {
        create: () => ({
          type: 'car',
          wheels: 4,
          drive: () => console.log('Driving car')
        })
      },
      bike: {
        create: () => ({
          type: 'bike',
          wheels: 2,
          ride: () => console.log('Riding bike')
        })
      }
    };
    return factories[type];
  }
};

const carFactory = VehicleFactory.createFactory('car');
const car = carFactory.create();
```

### Theme-Based Factory

```javascript
const ThemeFactory = {
  createButton: (theme) => {
    const styles = {
      dark: { bg: '#333', color: '#fff' },
      light: { bg: '#fff', color: '#333' }
    };
    return { ...styles[theme], render: () => 'Button' };
  },

  createInput: (theme) => {
    const styles = {
      dark: { bg: '#333', border: '#666' },
      light: { bg: '#fff', border: '#ccc' }
    };
    return { ...styles[theme], render: () => 'Input' };
  }
};

const darkButton = ThemeFactory.createButton('dark');
const darkInput = ThemeFactory.createInput('dark');
```

## Best Practices

- Keep factory families consistent
- Use object spread for inheritance patterns
- Document relationships between products
- Consider using ES6 classes for complex factories
- Use dependency injection when possible

## Interview Questions

1. How does Abstract Factory differ from Factory Method?
2. When should you use Abstract Factory in JavaScript?
3. Can Abstract Factory return different types?
4. How do you extend Abstract Factory with new products?
5. When should you choose Abstract Factory over simple Factory?

## References

- "Learning JavaScript Design Patterns" by Addy Osmani
- Gang of Four, "Design Patterns"
- MDN Web Docs
