# super Keyword

## Objective
Understand the `super` keyword for accessing parent class members and constructors.

## Theory

### What is `super`?
The `super` keyword is a reference variable that refers to the **parent class object**. It's used to access parent class members hidden by child class.

## Use Cases

### 1. Call Parent Constructor
```java
public class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }
}

public class Dog extends Animal {
    private String breed;

    public Dog(String name, String breed) {
        super(name);  // Must be first statement
        this.breed = breed;
    }
}
```

### 2. Access Parent Field
```java
class Vehicle {
    protected String brand = "Generic";
}

class Car extends Vehicle {
    private String brand = "Toyota";

    public void printBrands() {
        System.out.println("Child: " + brand);      // Toyota
        System.out.println("Parent: " + super.brand);  // Generic
    }
}
```

### 3. Call Parent Method
```java
class Parent {
    public void greet() {
        System.out.println("Hello from Parent");
    }
}

class Child extends Parent {
    @Override
    public void greet() {
        super.greet();  // Call parent implementation
        System.out.println("Hello from Child");
    }
}
```

## Rules
| Rule | Description |
|------|-------------|
| `super()` first line | Must be first statement in constructor |
| Cannot use in static context | Only in instance methods/constructors |
| `super.method()` | Calls parent implementation |
| `super.field` | Accesses parent field (not hidden) |

## super vs this

| Aspect | `this` | `super` |
|--------|--------|---------|
| Refers to | Current object | Parent object |
| Constructor call | `this()` | `super()` |
| Field access | Current class | Parent class |
| Method access | Current class | Parent class |
| Static context | Not allowed | Not allowed |

## Common Mistakes

| Mistake | Correct |
|---------|---------|
| `super()` not first line | Must be first statement |
| `super()` in static method | Not allowed |
| `super` to access sibling | Not possible |

## Interview Questions

1. **Can we use `super()` and `this()` together?** No, both must be first statement
2. **Can `super()` call parameterized constructor?** Yes, `super(arg)`
3. **Can we call `super.super.method()`?** No, only one level up

## Related Topics
← [this Keyword](this-keyword.md) | → [Instance Members](instance-members.md)

## References
- [Java Language Specification - super](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.11.2)