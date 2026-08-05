# Strategy Pattern (JavaScript)

## Overview

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes
them interchangeable. JavaScript's first-class functions make strategies particularly
elegant.

## When to Use

- Multiple algorithms for specific task
- Need to switch algorithms at runtime
- Avoiding conditional statements
- Isolating algorithm implementation

## JavaScript Implementation

### Classic Strategy

```javascript
class Sorter {
  constructor(strategy) {
    this.strategy = strategy;
  }

  setStrategy(strategy) {
    this.strategy = strategy;
  }

  sort(data) {
    return this.strategy(data);
  }
}

const bubbleSort = (arr) => {
  console.log('Bubble sort');
  return [...arr].sort((a, b) => a - b);
};

const quickSort = (arr) => {
  console.log('Quick sort');
  return [...arr].sort((a, b) => a - b);
};
```

### Functional Strategy

```javascript
function createProcessor(strategy) {
  return {
    execute: (data) => strategy(data)
  };
}

const doubleStrategy = (x) => x * 2;
const squareStrategy = (x) => x * x;

const processor = createProcessor(doubleStrategy);
```

### Strategy with Configuration

```javascript
const strategies = {
  tax: {
    US: (amount) => amount * 0.1,
    EU: (amount) => amount * 0.2,
    UK: (amount) => amount * 0.2
  },
  discount: {
    regular: (amount) => amount * 0.9,
    premium: (amount) => amount * 0.8
  }
};

function calculate(amount, taxRate, discountRate) {
  const tax = strategies.tax[taxRate](amount);
  const discount = strategies.discount[discountRate](amount + tax);
  return amount + tax - discount;
}
```

### Strategy Class

```javascript
class ValidationStrategy {
  constructor(strategy) {
    this.strategy = strategy;
  }

  validate(data) {
    return this.strategy(data);
  }
}

const emailStrategy = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
const phoneStrategy = (phone) => /^\d{10}$/.test(phone);

const emailValidator = new ValidationStrategy(emailStrategy);
```

## Best Practices

- Keep strategy interface small
- Use functions for simple strategies
- Document strategy selection criteria
- Make strategies stateless when possible
- Consider using dependency injection

## Interview Questions

1. How does Strategy differ from State?
2. When should you use functions over classes?
3. Can strategies have state?
4. How do you handle strategy selection?
5. When is Strategy better than inheritance?

## References

- MDN: Strategy Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Functional Programming in JavaScript" by Luis Atencio
