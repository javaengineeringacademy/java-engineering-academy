# Template Method Pattern (TypeScript)

## Overview

The Template Method pattern defines the skeleton of an algorithm in a base class,
letting subclasses override specific steps. TypeScript's abstract classes and generics
enable type-safe template implementations.

## When to Use

- Common algorithm structure with varying implementations
- Eliminating code duplication
- Enforcing algorithm structure
- Subclass customization points

## TypeScript Implementation

### Abstract Template

```typescript
abstract class DataMiner {
  mine(): void {
    this.openFile();
    this.extractData();
    this.parseData();
    this.analyzeData();
    this.sendReport();
    this.closeFile();
  }

  protected abstract openFile(): void;
  protected abstract extractData(): void;
  protected parseData(): void {
    console.log('Parsing data...');
  }
  protected analyzeData(): void {
    console.log('Analyzing...');
  }
  protected abstract sendReport(): void;
  protected abstract closeFile(): void;
}
```

### Generic Template

```typescript
abstract class Pipeline<T> {
  execute(input: T): void {
    const transformed = this.transform(input);
    this.process(transformed);
    this.output(transformed);
  }

  protected abstract transform(input: T): T;
  protected abstract process(input: T): void;
  protected output(input: T): void {
    console.log(input);
  }
}
```

### Hook Methods

```typescript
abstract class WebCrawler {
  async crawl(): Promise<void> {
    if (this.beforeCrawl()) {
      await this.connect();
      await this.download();
      await this.process();
      this.afterCrawl();
    }
  }

  protected beforeCrawl(): boolean {
    return true;
  }
  protected afterCrawl(): void {}
  protected abstract connect(): Promise<void>;
  protected abstract download(): Promise<void>;
  protected abstract process(): Promise<void>;
}
```

### Functional Template

```typescript
function createTemplate<T>(steps: Array<(input: T) => T>): (input: T) => T {
  return (input: T) => steps.reduce((acc, step) => step(acc), input);
}

const process = createTemplate<number>([
  (x) => x + 1,
  (x) => x * 2,
  (x) => x - 3
]);
```

## Best Practices

- Use abstract classes for type safety
- Keep template method small
- Use hook methods for optional steps
- Document customization points
- Avoid calling virtual methods from constructor

## Interview Questions

1. How does Template Method differ from Strategy?
2. What are hook methods?
3. Can template methods be async?
4. How do you handle template method with parameters?
5. When should you use Template Method vs composition?

## References

- TypeScript Handbook: Classes
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Clean Code" by Robert C. Martin
