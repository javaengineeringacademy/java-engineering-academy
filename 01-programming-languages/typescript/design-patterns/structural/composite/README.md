# Composite Pattern (TypeScript)

## Overview

The Composite pattern lets you compose objects into tree structures to represent
part-whole hierarchies. TypeScript's recursive types enable type-safe composite
implementations.

## When to Use

- Representing part-whole hierarchies
- Treating individual and composite objects uniformly
- Building tree structures like menus or file systems
- Creating UI component hierarchies

## TypeScript Implementation

### Generic Composite

```typescript
interface Component<T> {
  value: T;
  children: Component<T>[];
  add(child: Component<T>): this;
  remove(child: Component<T>): this;
}

class Composite<T> implements Component<T> {
  children: Component<T>[] = [];

  constructor(public value: T) {}

  add(child: Component<T>): this {
    this.children.push(child);
    return this;
  }

  remove(child: Component<T>): this {
    this.children = this.children.filter(c => c !== child);
    return this;
  }
}
```

### Recursive Type

```typescript
type TreeNode<T> = {
  value: T;
  children: TreeNode<T>[];
};

function createNode<T>(value: T, children: TreeNode<T>[] = []): TreeNode<T> {
  return { value, children };
}
```

### DOM-like Composite

```typescript
interface DOMElement {
  tag: string;
  children: DOMElement[];
  attributes: Record<string, string>;
}

function createElement(tag: string, attributes: Record<string, string> = {}): DOMElement {
  return { tag, children: [], attributes };
}

function addChild(parent: DOMElement, child: DOMElement): void {
  parent.children.push(child);
}
```

### Visitor Integration

```typescript
interface Visitor<T> {
  visit(node: TreeNode<T>): void;
}

function traverse<T>(node: TreeNode<T>, visitor: Visitor<T>): void {
  visitor.visit(node);
  node.children.forEach(child => traverse(child, visitor));
}
```

## Best Practices

- Define uniform interface for all components
- Use recursive types for tree structures
- Implement iterator for composite traversal
- Consider visitor for complex operations
- Document component lifecycle

## Interview Questions

1. How does Composite differ from Decorator?
2. What is the difference between Composite and Chain of Responsibility?
3. Can composite operations fail on leaves?
4. How do you traverse a composite tree?
5. When should you avoid using Composite?

## References

- TypeScript Handbook: Types
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Head First Design Patterns" by Freeman
