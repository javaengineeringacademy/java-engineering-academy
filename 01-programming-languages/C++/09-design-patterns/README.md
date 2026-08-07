# Design Patterns

## What it is
Reusable solutions to common software design problems.

## Why it exists
To provide proven solutions and common vocabulary for developers.

## When to use it
When facing recurring design problems in your codebase.

## How it works

### Singleton Pattern
```cpp
class Singleton {
public:
    static Singleton& getInstance() {
        static Singleton instance;
        return instance;
    }
private:
    Singleton() = default;
};
```

### Factory Pattern
```cpp
class Shape {
public:
    virtual void draw() = 0;
};

class ShapeFactory {
public:
    static std::unique_ptr<Shape> create(const std::string& type);
};
```

### Observer Pattern
```cpp
class Observer {
public:
    virtual void update(int value) = 0;
};

class Subject {
    std::vector<Observer*> observers;
public:
    void attach(Observer* obs);
    void notify(int value);
};
```

### Strategy Pattern
```cpp
class Strategy {
public:
    virtual int execute(int a, int b) = 0;
};

class Context {
    Strategy* strategy;
public:
    void setStrategy(Strategy* s);
    int executeStrategy(int a, int b);
};
```

## Production Checklist
- [ ] Use patterns to solve specific problems
- [ ] Don't over-engineer with unnecessary patterns
- [ ] Prefer composition over inheritance
- [ ] Keep patterns simple and focused
- [ ] Document pattern usage in codebase
- [ ] Review patterns during code reviews

## Maturity Levels
- **Beginner**: Singleton, Factory, Observer
- **Intermediate**: Strategy, Decorator, Adapter
- **Advanced**: Composite, Flyweight, Chain of Responsibility

## Common Myths
- ❌ "Patterns are always necessary"
- ❌ "More patterns mean better design"
- ❌ "Patterns are language-specific"

## One-Minute Revision
| Pattern | Purpose |
|---------|---------|
| Singleton | Single instance |
| Factory | Object creation |
| Observer | Event notification |
| Strategy | Algorithm selection |
| Decorator | Add behavior dynamically |

## Related Topics
- [OOP](../02-oop/)
- [Best Practices](../14-best-practices/)
- [Testing](../10-testing/)