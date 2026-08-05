# Composite Pattern (JavaScript)

## Overview

The Composite pattern lets you compose objects into tree structures to represent
part-whole hierarchies. JavaScript's recursive data structures make composites
particularly natural.

## When to Use

- Representing part-whole hierarchies
- Treating individual and composite objects uniformly
- Building tree structures like menus or file systems
- Creating UI component hierarchies

## JavaScript Implementation

### Basic Composite

```javascript
class Component {
  constructor(name) {
    this.name = name;
    this.children = [];
  }

  add(component) {
    this.children.push(component);
    return this;
  }

  remove(component) {
    this.children = this.children.filter(c => c !== component);
    return this;
  }

  display(depth = 0) {
    console.log('-'.repeat(depth) + this.name);
    this.children.forEach(child => child.display(depth + 2));
  }
}
```

### Functional Composite

```javascript
function createComposite(name) {
  const children = [];

  return {
    name,
    add(child) {
      children.push(child);
      return this;
    },
    remove(child) {
      const index = children.indexOf(child);
      if (index > -1) children.splice(index, 1);
      return this;
    },
    display(depth = 0) {
      console.log('-'.repeat(depth) + this.name);
      children.forEach(child => child.display(depth + 2));
    },
    [Symbol.iterator]() {
      let index = 0;
      return {
        next() {
          return index < children.length
            ? { value: children[index++], done: false }
            : { done: true };
        }
      };
    }
  };
}
```

### DOM-like Composite

```javascript
class DOMComposite {
  constructor(tag) {
    this.tag = tag;
    this.children = [];
    this.attributes = {};
  }

  add(child) {
    this.children.push(child);
    return this;
  }

  setAttribute(key, value) {
    this.attributes[key] = value;
    return this;
  }

  render() {
    const attrs = Object.entries(this.attributes)
      .map(([k, v]) => `${k}="${v}"`)
      .join(' ');
    const children = this.children.map(c => c.render()).join('');
    return `<${this.tag} ${attrs}>${children}</${this.tag}>`;
  }
}
```

## Best Practices

- Define uniform interface for all components
- Consider making leaf operations no-ops
- Use recursion for traversal
- Implement iterator for composite traversal
- Consider visitor for complex operations

## Interview Questions

1. How does Composite differ from Decorator?
2. What is the difference between Composite and Chain of Responsibility?
3. Can composite operations fail on leaves?
4. How do you traverse a composite tree?
5. When should you avoid using Composite?

## References

- MDN: Composite Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Head First Design Patterns" by Freeman
