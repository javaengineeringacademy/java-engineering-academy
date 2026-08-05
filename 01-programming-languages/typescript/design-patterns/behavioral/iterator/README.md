# Iterator Pattern (TypeScript)

## Overview

The Iterator pattern provides a way to access elements of a collection sequentially
without exposing its underlying representation. TypeScript's generator functions and
Symbol.iterator provide type-safe iterator support.

## When to Use

- Accessing collection elements without exposing representation
- Supporting multiple traversal strategies
- Creating custom iteration patterns
- Implementing lazy evaluation

## TypeScript Implementation

### Typed Iterator

```typescript
interface Iterator<T> {
  next(): IteratorResult<T>;
}

class RangeIterator implements Iterator<number> {
  private current: number;

  constructor(private start: number, private end: number) {
    this.current = start;
  }

  next(): IteratorResult<number> {
    return this.current <= this.end
      ? { value: this.current++, done: false }
      : { done: true, value: undefined };
  }
}
```

### Iterable Interface

```typescript
class Range implements Iterable<number> {
  constructor(private start: number, private end: number) {}

  [Symbol.iterator](): Iterator<number> {
    let current = this.start;
    const end = this.end;

    return {
      next() {
        return current <= end
          ? { value: current++, done: false }
          : { done: true, value: undefined };
      }
    };
  }
}

for (const num of new Range(1, 5)) {
  console.log(num);
}
```

### Generator Function

```typescript
function* fibonacci(): Generator<number> {
  let a = 0, b = 1;

  while (true) {
    yield a;
    [a, b] = [b, a + b];
  }
}

const fib = fibonacci();
console.log(fib.next().value);
```

### Async Iterator

```typescript
async function* fetchPages(urls: string[]): AsyncGenerator<any> {
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

- TypeScript Handbook: Iterators and Generators
- "You Don't Know JS: Iterators & Generators"
- "TypeScript Design Patterns" by Vaskaran Sarcar
