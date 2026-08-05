# Factory Pattern (JavaScript)

## Overview

The Factory pattern provides an interface for creating objects without specifying their
concrete classes. JavaScript functions and closures make factory implementations concise
and flexible.

## When to Use

- Object creation logic is complex
- Need to create different types based on input
- Avoiding code duplication in object creation
- Creating objects from configuration

## JavaScript Implementation

### Basic Factory Function

```javascript
function createUser(type) {
  const users = {
    admin: { role: 'admin', permissions: ['read', 'write', 'delete'] },
    guest: { role: 'guest', permissions: ['read'] },
    user: { role: 'user', permissions: ['read', 'write'] }
  };

  return {
    ...users[type],
    type,
    describe() {
      return `${this.role} with ${this.permissions.length} permissions`;
    }
  };
}

const admin = createUser('admin');
```

### Factory with Constructor

```javascript
class CarFactory {
  static create(type) {
    const cars = {
      suv: { type: 'SUV', seats: 7 },
      sedan: { type: 'Sedan', seats: 5 },
      truck: { type: 'Truck', seats: 2 }
    };

    return {
      ...cars[type],
      start() {
        console.log(`${this.type} started`);
      }
    };
  }
}
```

### Abstract Factory

```javascript
const UIFactory = {
  createButton: (theme) => {
    const buttons = {
      light: { color: 'white', background: 'black' },
      dark: { color: 'black', background: 'white' }
    };
    return buttons[theme];
  },
  createInput: (theme) => {
    const inputs = {
      light: { border: '1px solid black' },
      dark: { border: '1px solid white' }
    };
    return inputs[theme];
  }
};
```

## Best Practices

- Keep factory functions small and focused
- Use descriptive names for factory methods
- Consider using object spread for defaults
- Document return type expectations
- Use factories when object creation is complex

## Interview Questions

1. What is the difference between Factory and Abstract Factory?
2. When should you use factory function vs constructor?
3. Can factory pattern work with ES6 classes?
4. How do you handle factory errors?
5. When is factory better than direct object creation?

## References

- MDN: Factory Functions
- "Learning JavaScript Design Patterns" by Addy Osmani
- "You Don't Know JS" by Kyle Simpson
