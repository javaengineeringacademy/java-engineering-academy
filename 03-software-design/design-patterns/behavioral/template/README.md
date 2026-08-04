# Template Method Pattern

The Template Method pattern defines the skeleton of an algorithm in a base class, letting subclasses override specific steps without changing the algorithm's structure.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Template](#basic-template)
3. [Hook Methods](#hook-methods)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Template Method?

Template Method defines algorithm skeleton in base class, deferring some steps to subclasses.

```
AbstractClass ──▶ templateMethod()
  │                   │
  │           ┌───────┼───────┐
  │        step1() step2() step3()
  │           │       │       │
ConcreteClassA  ConcreteClassB
```

### When to Use

- Algorithm structure is fixed, steps vary
- Common behavior in multiple classes
- Control subclass extension points

---

## Basic Template

### Data Processing

```java
public abstract class DataProcessor {
    // Template method
    public final void process() {
        readData();
        processData();
        writeData();
        logResult();
    }

    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();

    // Default implementation
    protected void logResult() {
        System.out.println("Processing complete");
    }
}

public class CsvProcessor extends DataProcessor {
    @Override
    protected void readData() { System.out.println("Reading CSV"); }

    @Override
    protected void processData() { System.out.println("Processing CSV rows"); }

    @Override
    protected void writeData() { System.out.println("Writing CSV output"); }
}

public class JsonProcessor extends DataProcessor {
    @Override
    protected void readData() { System.out.println("Reading JSON"); }

    @Override
    protected void processData() { System.out.println("Processing JSON objects"); }

    @Override
    protected void writeData() { System.out.println("Writing JSON output"); }
}

// Usage
DataProcessor csv = new CsvProcessor();
csv.process();

DataProcessor json = new JsonProcessor();
json.process();
```

---

## Hook Methods

### Extensible Template

```java
public abstract class Game {
    // Template method
    public final void play() {
        initialize();
        startPlay();
        endPlay();
        if (isPostGameEnabled()) {  // Hook
            postGame();
        }
    }

    protected abstract void initialize();
    protected abstract void startPlay();
    protected abstract void endPlay();

    // Hook methods - can be overridden
    protected boolean isPostGameEnabled() { return true; }
    protected void postGame() { System.out.println("Post-game analysis"); }
}

public class Football extends Game {
    @Override
    protected void initialize() { System.out.println("Set up field"); }

    @Override
    protected void startPlay() { System.out.println("Kickoff!"); }

    @Override
    protected void endPlay() { System.out.println("Final whistle"); }

    @Override
    protected boolean isPostGameEnabled() { return false; }  // Disable hook
}

public class Basketball extends Game {
    @Override
    protected void initialize() { System.out.println("Set up court"); }

    @Override
    protected void startPlay() { System.out.println("Tip-off!"); }

    @Override
    protected void endPlay() { System.out.println("Buzzer!"); }

    @Override
    protected void postGame() { System.out.println("Player interviews"); }
}

// Usage
Game football = new Football();
football.play();  // No post-game

Game basketball = new Basketball();
basketball.play();  // Custom post-game
```

---

## Best Practices

### Do

```java
// 1. Make template method final
public final void templateMethod() {
    step1();
    step2();
    step3();
}

// 2. Use hooks for optional steps
protected boolean shouldDoOptional() { return true; }
protected void optionalStep() { }
```

### Don't

```java
// 1. Don't let subclasses override template method
// Use final to prevent

// 2. Don't have too many abstract methods
// Keep it focused
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Template Method** | Algorithm skeleton in base class |
| **Abstract Steps** | Must be implemented by subclass |
| **Hook Methods** | Optional override points |
| **final** | Prevents overriding template method |
| **Code Reuse** | Common structure in base class |
| **Use Cases** | Frameworks, data processing, tests |
