# Builder Pattern (JavaScript)

## Overview

The Builder pattern separates construction of a complex object from its representation.
JavaScript's fluent interface support makes builder patterns particularly elegant.

## When to Use

- Creating objects with many optional parameters
- Avoiding telescoping constructors
- Building different representations of same object
- Complex object initialization logic

## JavaScript Implementation

### Fluent Builder

```javascript
class HttpRequestBuilder {
  constructor() {
    this.request = {
      method: 'GET',
      headers: {},
      body: null
    };
  }

  setMethod(method) {
    this.request.method = method;
    return this;
  }

  setUrl(url) {
    this.request.url = url;
    return this;
  }

  addHeader(key, value) {
    this.request.headers[key] = value;
    return this;
  }

  setBody(body) {
    this.request.body = body;
    return this;
  }

  build() {
    return { ...this.request };
  }
}

const request = new HttpRequestBuilder()
  .setUrl('https://api.example.com')
  .setMethod('POST')
  .addHeader('Content-Type', 'application/json')
  .setBody({ data: 'test' })
  .build();
```

### Builder with Validation

```javascript
class UserBuilder {
  constructor() {
    this.user = {};
  }

  setName(name) {
    if (!name || name.length < 2) {
      throw new Error('Invalid name');
    }
    this.user.name = name;
    return this;
  }

  setEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      throw new Error('Invalid email');
    }
    this.user.email = email;
    return this;
  }

  build() {
    if (!this.user.name || !this.user.email) {
      throw new Error('Missing required fields');
    }
    return { ...this.user };
  }
}
```

## Best Practices

- Return builder instance from setter methods
- Validate in build method
- Use spread operator for immutable copies
- Keep builder chainable and fluent
- Consider using factory for complex objects

## Interview Questions

1. What problem does Builder pattern solve?
2. How does fluent interface work in JavaScript?
3. Can builder be used with ES6 classes?
4. When should you use Builder vs Factory?
5. How do you handle validation in builders?

## References

- "Learning JavaScript Design Patterns" by Addy Osmani
- "Head First Design Patterns" by Freeman
- MDN Web Docs
