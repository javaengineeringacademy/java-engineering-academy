# Builder Pattern (TypeScript)

## Overview

The Builder pattern separates construction of a complex object from its representation.
TypeScript's type system enables fluent builders with compile-time type checking.

## When to Use

- Creating objects with many optional parameters
- Avoiding telescoping constructors
- Building different representations of same object
- Complex object initialization logic

## TypeScript Implementation

### Generic Builder

```typescript
class Builder<T> {
  private result: Partial<T> = {};

  set<K extends keyof T>(key: K, value: T[K]): this {
    this.result[key] = value;
    return this;
  }

  build(): T {
    return this.result as T;
  }
}

interface User {
  name: string;
  email: string;
  age: number;
}

const user = new Builder<User>()
  .set('name', 'John')
  .set('email', 'john@example.com')
  .set('age', 30)
  .build();
```

### Typed Builder

```typescript
interface HttpRequest {
  url: string;
  method: string;
  headers: Record<string, string>;
  body?: any;
}

class HttpRequestBuilder {
  private request: Partial<HttpRequest> = {};

  setUrl(url: string): this {
    this.request.url = url;
    return this;
  }

  setMethod(method: string): this {
    this.request.method = method;
    return this;
  }

  addHeader(key: string, value: string): this {
    this.request.headers = this.request.headers || {};
    this.request.headers[key] = value;
    return this;
  }

  build(): HttpRequest {
    if (!this.request.url || !this.request.method) {
      throw new Error('Url and method are required');
    }
    return this.request as HttpRequest;
  }
}
```

### Builder with Validation

```typescript
class UserBuilder {
  private user: Partial<User> = {};

  setName(name: string): this {
    if (name.length < 2) {
      throw new Error('Name must be at least 2 characters');
    }
    this.user.name = name;
    return this;
  }

  setEmail(email: string): this {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      throw new Error('Invalid email');
    }
    this.user.email = email;
    return this;
  }

  build(): User {
    if (!this.user.name || !this.user.email) {
      throw new Error('Missing required fields');
    }
    return this.user as User;
  }
}
```

## Best Practices

- Use generics for type safety
- Return builder instance from setter methods
- Validate in build method
- Use TypeScript's type inference for fluent API
- Document required vs optional fields

## Interview Questions

1. What problem does Builder pattern solve?
2. How does fluent interface work in TypeScript?
3. Can builder be used with TypeScript generics?
4. When should you use Builder vs Factory?
5. How do you handle validation in builders?

## References

- TypeScript Handbook: Generics
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Head First Design Patterns" by Freeman
