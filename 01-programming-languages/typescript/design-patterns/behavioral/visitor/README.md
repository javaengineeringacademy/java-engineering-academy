# Visitor Pattern (TypeScript)

## Overview

The Visitor pattern lets you separate algorithms from the objects on which they operate.
TypeScript's interfaces and discriminated unions enable type-safe visitor implementations.

## When to Use

- Object structure contains classes with many interfaces
- Need to perform operations on objects without changing classes
- Operations vary across object types
- Related operations should be grouped together

## TypeScript Implementation

### Typed Visitor

```typescript
interface Visitor<T> {
  visit(item: T): any;
}

interface Visitable<T> {
  accept(visitor: Visitor<T>): any;
}

class Book implements Visitable<Book> {
  constructor(public title: string, public price: number) {}

  accept(visitor: Visitor<Book>): any {
    return visitor.visit(this);
  }
}
```

### Discriminated Union Visitor

```typescript
type AST =
  | { type: 'number'; value: number }
  | { type: 'add'; left: AST; right: AST }
  | { type: 'multiply'; left: AST; right: AST };

const interpreter = {
  number: (node: { type: 'number'; value: number }) => node.value,
  add: (node: { type: 'add'; left: AST; right: AST }) =>
    interpret(node.left) + interpret(node.right),
  multiply: (node: { type: 'multiply'; left: AST; right: AST }) =>
    interpret(node.left) * interpret(node.right)
};

function interpret(ast: AST): number {
  return interpreter[ast.type](ast as any);
}
```

### Generic Visitor

```typescript
interface TypedVisitor<T> {
  visit(item: T): any;
}

class CompositeVisitor<T> {
  private visitors: TypedVisitor<T>[] = [];

  add(visitor: TypedVisitor<T>): void {
    this.visitors.push(visitor);
  }

  visit(item: T): any[] {
    return this.visitors.map(visitor => visitor.visit(item));
  }
}
```

### Pattern Matching Visitor

```typescript
function visit<T extends { type: string }>(
  item: T,
  handlers: { [K in T['type']]: (item: any) => any }
): any {
  return handlers[item.type](item);
}
```

## Best Practices

- Use discriminated unions for type safety
- Keep visitor interface focused
- Document visitor responsibilities clearly
- Use visitor when operations change frequently
- Handle null elements gracefully

## Interview Questions

1. What is double dispatch in Visitor?
2. How does Visitor violate encapsulation?
3. Can you add new elements without changing visitor?
4. When should you use Visitor vs Strategy?
5. How do you handle null elements in Visitor?

## References

- TypeScript Handbook: Discriminated Unions
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Refactoring to Patterns" by Kerievsky
