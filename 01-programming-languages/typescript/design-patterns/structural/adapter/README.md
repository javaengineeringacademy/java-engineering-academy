# Adapter Pattern (TypeScript)

## Overview

The Adapter pattern converts the interface of a class into another interface clients
expect. TypeScript's interfaces enable compile-time type checking for adapter
implementations.

## When to Use

- Integrating third-party libraries
- Reusing existing classes with incompatible interfaces
- Building legacy system integration
- Converting data formats

## TypeScript Implementation

### Interface Adapter

```typescript
interface Target {
  request(): string;
}

class Adaptee {
  specificRequest(): string {
    return 'Adaptee request';
  }
}

class Adapter implements Target {
  private adaptee: Adaptee;

  constructor(adaptee: Adaptee) {
    this.adaptee = adaptee;
  }

  request(): string {
    return this.adaptee.specificRequest();
  }
}
```

### Generic Adapter

```typescript
interface LegacyAPI {
  getData(): { data: string; timestamp: number };
}

interface NewAPI {
  fetchData(): Promise<{ data: string; time: number }>;
}

class APIAdapter implements NewAPI {
  constructor(private legacyAPI: LegacyAPI) {}

  async fetchData(): Promise<{ data: string; time: number }> {
    const data = this.legacyAPI.getData();
    return { data: data.data, time: data.timestamp };
  }
}
```

### Function Adapter

```typescript
function adaptOldAPI(oldAPI: LegacyAPI): NewAPI {
  return {
    fetchData: async () => {
      const data = oldAPI.getData();
      return { data: data.data, time: data.timestamp };
    }
  };
}
```

### Data Format Adapter

```typescript
interface XMLData {
  [key: string]: string;
}

interface JSONData {
  [key: string]: string;
}

class XMLToJSONAdapter {
  constructor(private xmlData: XMLData) {}

  toJSON(): JSONData {
    return { ...this.xmlData };
  }
}
```

## Best Practices

- Keep adapter interface consistent
- Use interfaces for type safety
- Document interface differences
- Keep adapters simple and focused
- Consider testing adapter behavior thoroughly

## Interview Questions

1. How does Adapter differ from Facade?
2. Can you use adapters for data format conversion?
3. When should you use Adapter vs Wrapper?
4. How do you handle multiple interface adaptations?
5. Can adapters add new functionality?

## References

- TypeScript Handbook: Interfaces
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Head First Design Patterns" by Freeman
