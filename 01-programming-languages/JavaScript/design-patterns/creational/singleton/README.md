# Singleton Pattern (JavaScript)

## Overview

The Singleton pattern ensures a class has only one instance. JavaScript's module system
provides natural singleton behavior through module caching and closures.

## When to Use

- Managing global state
- Database connections
- Configuration objects
- Logging services
- Cache implementations

## JavaScript Implementation

### Module Pattern

```javascript
const Singleton = (() => {
  let instance;

  function createInstance() {
    return {
      data: Math.random(),
      getData() {
        return this.data;
      }
    };
  }

  return {
    getInstance() {
      if (!instance) {
        instance = createInstance();
      }
      return instance;
    }
  };
})();
```

### ES6 Class Singleton

```javascript
class Database {
  static #instance = null;

  constructor() {
    if (Database.#instance) {
      return Database.#instance;
    }
    this.connection = this.connect();
    Database.#instance = this;
  }

  connect() {
    return { connected: true };
  }

  static getInstance() {
    if (!Database.#instance) {
      Database.#instance = new Database();
    }
    return Database.#instance;
  }
}
```

### Export Singleton

```javascript
// config.js
class Config {
  constructor() {
    this.settings = {};
  }

  get(key) {
    return this.settings[key];
  }

  set(key, value) {
    this.settings[key] = value;
  }
}

export default new Config();
```

## Best Practices

- Use module exports for simple singletons
- Consider dependency injection as alternative
- Avoid global state when possible
- Use private fields for encapsulation
- Document singleton usage clearly

## Interview Questions

1. Why is singleton considered anti-pattern in some cases?
2. How does JavaScript module system provide singleton behavior?
3. Can you make singleton thread-safe in JS?
4. When should you use singleton vs dependency injection?
5. How do you test code using singletons?

## References

- MDN: Module Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "You Don't Know JS" by Kyle Simpson
