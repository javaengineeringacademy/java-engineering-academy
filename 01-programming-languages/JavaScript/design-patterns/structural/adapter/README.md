# Adapter Pattern (JavaScript)

## Overview

The Adapter pattern converts the interface of a class into another interface clients
expect. JavaScript's duck typing and dynamic nature make adapters particularly flexible.

## When to Use

- Integrating third-party libraries
- Reusing existing classes with incompatible interfaces
- Building legacy system integration
- Converting data formats

## JavaScript Implementation

### Object Adapter

```javascript
class OldAPI {
  getData() {
    return { data: 'old format', timestamp: Date.now() };
  }
}

class NewAPI {
  fetchData() {
    return Promise.resolve({ data: 'new format', time: Date.now() });
  }
}

class APIAdapter {
  constructor(api) {
    this.api = api;
  }

  async fetchData() {
    if (this.api instanceof OldAPI) {
      const data = this.api.getData();
      return { data: data.data, time: data.timestamp };
    }
    return this.api.fetchData();
  }
}
```

### Function Adapter

```javascript
function adaptOldAPI(oldAPI) {
  return {
    fetchData: () => {
      const data = oldAPI.getData();
      return Promise.resolve({ data: data.data, time: data.timestamp });
    }
  };
}
```

### Data Format Adapter

```javascript
class XMLToJSONAdapter {
  constructor(xmlData) {
    this.xmlData = xmlData;
  }

  toJSON() {
    // Simple XML to JSON conversion
    const json = {};
    const matches = this.xmlData.matchAll(/<(\w+)>(.*?)<\/\1>/g);
    for (const match of matches) {
      json[match[1]] = match[2];
    }
    return json;
  }
}
```

## Best Practices

- Keep adapter interface consistent
- Use duck typing when possible
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

- MDN: Adapter Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Head First Design Patterns" by Freeman
