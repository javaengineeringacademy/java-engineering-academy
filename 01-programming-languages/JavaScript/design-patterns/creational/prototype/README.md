# Prototype Pattern (JavaScript)

## Overview

The Prototype pattern creates new objects by cloning existing instances. JavaScript's
prototypal inheritance makes this pattern particularly natural and powerful.

## When to Use

- Creating objects expensive to construct
- When object creation is complex
- Need many similar objects
- Avoiding subclassing for object creation

## JavaScript Implementation

### Object.create()

```javascript
const carPrototype = {
  wheels: 4,
  start() {
    console.log(`${this.model} started`);
  },
  describe() {
    return `${this.model} with ${this.wheels} wheels`;
  }
};

const tesla = Object.create(carPrototype);
tesla.model = 'Tesla';
tesla.wheels = 4;
```

### Clone Method

```javascript
class UserProfile {
  constructor(name, settings) {
    this.name = name;
    this.settings = { ...settings };
  }

  clone() {
    return new UserProfile(this.name, { ...this.settings });
  }
}

const original = new UserProfile('John', { theme: 'dark', lang: 'en' });
const copy = original.clone();
```

### Deep Clone

```javascript
function deepClone(obj) {
  if (obj === null || typeof obj !== 'object') {
    return obj;
  }

  if (Array.isArray(obj)) {
    return obj.map(item => deepClone(item));
  }

  return Object.fromEntries(
    Object.entries(obj).map(([key, value]) => [key, deepClone(value)])
  );
}

const original = { nested: { value: 1 }, array: [1, 2, 3] };
const copy = deepClone(original);
```

### Prototype Chain

```javascript
const animal = {
  speak() {
    console.log(`${this.name} makes a noise`);
  }
};

const dog = Object.create(animal);
dog.bark = function() {
  console.log(`${this.name} barks`);
};
```

## Best Practices

- Use Object.create() for prototype inheritance
- Implement clone method for complex objects
- Use spread operator for shallow cloning
- Consider deep clone for nested objects
- Document clone semantics

## Interview Questions

1. What is the difference between shallow and deep clone?
2. How does prototypal inheritance work in JavaScript?
3. When should you use Object.create() vs class inheritance?
4. Can you clone objects with circular references?
5. How do you implement clone without modifying prototype?

## References

- MDN: Prototype-based inheritance
- "You Don't Know JS: this & Object Prototypes"
- "Learning JavaScript Design Patterns" by Addy Osmani
