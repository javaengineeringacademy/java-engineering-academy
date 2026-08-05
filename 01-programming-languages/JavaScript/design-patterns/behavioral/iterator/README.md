# Iterator Pattern (JavaScript)

## Overview

The Iterator pattern provides a way to access elements of a collection sequentially
without exposing its underlying representation. JavaScript's generator functions and
Symbol.iterator provide built-in iterator support.

## When to Use

- Accessing collection elements without exposing representation
- Supporting multiple traversal strategies
- Creating custom iteration patterns
- Implementing lazy evaluation

## JavaScript Implementation

### Symbol.iterator

```javascript
class Range {
  constructor(start, end) {
    this.start = start;
    this.end = end;
  }

  [Symbol.iterator]() {
    let current = this.start;
    const end = this.end;

    return {
      next() {
        return current <= end
          ? { value: current++, done: false }
          : { done: true };
      }
    };
  }
}

for (const num of new Range(1, 5)) {
  console.log(num);
}
```

### Generator Function

```javascript
function* fibonacci() {
  let a = 0, b = 1;

  while (true) {
    yield a;
    [a, b] = [b, a + b];
  }
}

const fib = fibonacci();
console.log(fib.next().value);
```

### Custom Iterator

```javascript
class TreeIterator {
  constructor(root) {
    this.stack = [];
    this.current = root;
  }

  next() {
    while (this.current || this.stack.length > 0) {
      while (this.current) {
        this.stack.push(this.current);
        this.current = this.current.left;
      }

      this.current = this.stack.pop();
      const value = this.current.value;
      this.current = this.current.right;

      return { value, done: false };
    }

    return { done: true };
  }
}
```

### Async Iterator

```javascript
async function* fetchPages(urls) {
  for (const url of urls) {
    const response = await fetch(url);
    yield await response.json();
  }
}

async function processPages() {
  for await (const page of fetchPages(['url1', 'url2'])) {
    console.log(page);
  }
}
```

## Best Practices

- Use Symbol.iterator for custom iteration
- Use generators for lazy evaluation
- Implement return method for cleanup
- Consider using for...of for iteration
- Handle infinite sequences carefully

## Interview Questions

1. What is the difference between iterator and generator?
2. How does Symbol.iterator work?
3. Can iterators be infinite sequences?
4. How do you handle iterator disposal?
5. When should you use custom iterator vs array methods?

## References

- MDN: Iterators and Generators
- "You Don't Know JS: Iterators & Generators"
- "Learning JavaScript Design Patterns" by Addy Osmani
