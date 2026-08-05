# Visitor Pattern (JavaScript)

## Overview

The Visitor pattern lets you separate algorithms from the objects on which they operate.
JavaScript's duck typing and functions make visitor implementations flexible.

## When to Use

- Object structure contains classes with many interfaces
- Need to perform operations on objects without changing classes
- Operations vary across object types
- Related operations should be grouped together

## JavaScript Implementation

### Basic Visitor

```javascript
class Visitor {
  visitBook(book) { return book.price; }
  visitElectronics(product) { return product.price * 0.9; }
}

class Book {
  constructor(title, price) {
    this.title = title;
    this.price = price;
  }

  accept(visitor) {
    return visitor.visitBook(this);
  }
}

class Electronics {
  constructor(name, price) {
    this.name = name;
    this.price = price;
  }

  accept(visitor) {
    return visitor.visitElectronics(this);
  }
}
```

### Functional Visitor

```javascript
const visitors = {
  book: (item) => item.price,
  electronics: (item) => item.price * 0.9,
  food: (item) => item.price * 0.8
};

function visit(item) {
  return visitors[item.type](item);
}
```

### Tree Visitor

```javascript
class TreeVisitor {
  visit(node) {
    if (node.type === 'number') {
      return node.value;
    } else if (node.type === 'add') {
      return this.visit(node.left) + this.visit(node.right);
    } else if (node.type === 'multiply') {
      return this.visit(node.left) * this.visit(node.right);
    }
  }
}
```

### AST Visitor

```javascript
const interpret = {
  Number: (node) => node.value,
  Add: (node) => interpret[node.left.type](node.left) + interpret[node.right.type](node.right),
  Multiply: (node) => interpret[node.left.type](node.left) * interpret[node.right.type](node.right)
};

function evaluate(ast) {
  return interpret[ast.type](ast);
}
```

## Best Practices

- Keep visitor interface focused
- Document visitor responsibilities clearly
- Use visitor when operations change frequently
- Consider using pattern matching in modern JS
- Handle null elements gracefully

## Interview Questions

1. What is double dispatch in Visitor?
2. How does Visitor violate encapsulation?
3. Can you add new elements without changing visitor?
4. When should you use Visitor vs Strategy?
5. How do you handle null elements in Visitor?

## References

- MDN: Visitor Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Refactoring to Patterns" by Kerievsky
