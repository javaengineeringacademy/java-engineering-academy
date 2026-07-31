# this Keyword

## Objective
Understand the `this` keyword for referring to the current object instance.

## Theory

### What is `this`?
The `this` keyword is a reference variable that refers to the **current object instance**. It's implicitly available in all instance methods and constructors.

## Use Cases

### 1. Disambiguate Field vs Parameter
```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;  // this.name = field, name = parameter
        this.age = age;
    }
}
```

### 2. Constructor Chaining
```java
public class Person {
    private String name;
    private int age;
    private String address;

    // Primary constructor
    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Chaining to primary
    public Person(String name, int age) {
        this(name, age, "Unknown");  // Calls primary constructor
    }

    public Person(String name) {
        this(name, 0);  // Chains to two-arg constructor
    }
}
```

### 3. Returning Current Instance (Method Chaining)
```java
public class StringBuilder {
    private StringBuilder sb = new StringBuilder();

    public StringBuilder append(String s) {
        this.sb.append(s);
        return this;  // Return current instance for chaining
    }

    public StringBuilder toUpperCase() {
        // ...
        return this;
    }
}

// Usage: new StringBuilder().append("Hello").toUpperCase().append(" World");
```

### 4. Passing Current Object
```java
public class EventManager {
    public void register(Listener listener) {
        listener.onEvent(new Event(this));  // Pass current object
    }
}
```

## Rules
| Rule | Description |
|------|-------------|
| Only in instance context | Cannot use in `static` methods |
| Implicit in constructors | `this()` calls another constructor |
| Must be first statement | `this()` or `super()` must be first line |
| Cannot use in static context | Compile error in static methods |

## Common Mistakes

| Mistake | Correct |
|---------|---------|
| `this` in static method | Use class name or remove static |
| `this()` not first in constructor | Must be first statement |
| Confusing `this` with `super` | `this` = current class, `super` = parent |

## Interview Questions

1. **Can we use `this` in static method?** No, static context has no instance
2. **Can constructor call another constructor?** Yes, via `this(...)`, must be first line
3. **Can `this` be null?** No, always refers to current instance
4. **Can we assign to `this`?** No, `this` is final reference

## Related Topics
← [Constructors](constructors.md) | → [super Keyword](super-keyword.md) | → [Methods](methods.md)

## References
- [Java Language Specification - this](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.8.3)