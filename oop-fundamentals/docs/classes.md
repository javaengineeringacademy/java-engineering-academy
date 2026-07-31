# Classes and Objects

## Objective
Understand the fundamental building blocks of OOP: classes as blueprints and objects as instances.

## Theory

### What is a Class?
A **class** is a blueprint or template that defines the structure and behavior of objects. It encapsulates data (fields) and operations (methods) into a single unit.

### What is an Object?
An **object** is an instance of a class. It has:
- **State**: Values of its fields (instance variables)
- **Behavior**: What it can do (methods)
- **Identity**: Unique reference in memory

### Real-World Analogy
- **Class** = Architectural blueprint for a house
- **Object** = Actual house built from the blueprint
- You can build many houses (objects) from one blueprint (class)

## Class Structure

```java
public class ClassName {
    // Fields (state)
    private Type fieldName;
    
    // Constructors (initialization)
    public ClassName(parameters) { ... }
    
    // Methods (behavior)
    public ReturnType methodName(parameters) { ... }
    
    // Getters/Setters (encapsulation)
    public Type getFieldName() { ... }
    public void setFieldName(Type value) { ... }
}
```

## Object Creation & Memory

```
Stack                          Heap
─────────────────              ─────────────────
reference: obj ──────────────▶ Object: ClassName
                                  - field1: value
                                  - field2: value
```

## Example: Person Class

```java
public final class Person {
    private final String name;
    private int age;
    private final String email;

    public Person(String name, int age, String email) {
        this.name = Objects.requireNonNull(name, "Name required");
        this.age = validateAge(age);
        this.email = Objects.requireNonNull(email, "Email required");
    }

    private int validateAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Age must be >= 0");
        return age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    public void setAge(int age) {
        this.age = validateAge(age);
    }

    @Override
    public String toString() {
        return "Person{name='%s', age=%d, email='%s'}".formatted(name, age, email);
    }
}
```

## Execution Flow (Stack vs Heap)

```java
Person person = new Person("Alice", 30, "alice@example.com");
```

**Stack**: `person` reference variable
**Heap**: `Person` object with fields `name="Alice"`, `age=30`, `email="alice@example.com"`

## Execution Flow Step-by-Step

1. **Class Loading**: JVM loads `Person.class`
2. **Memory Allocation**: Heap space for `Person` object
3. **Constructor Execution**: Fields initialized
3. **Reference Assignment**: Stack variable `person` points to heap object

## Common Mistakes

| Mistake | Correct Approach |
|---------|------------------|
| Public fields | Use `private` + getters/setters |
| Mutable fields without validation | Validate in setters |
| Missing `toString()` | Override for debugging |
| Not using `final` for immutable fields | Use `final` for immutability |

## Best Practices

- Use `private` fields + public getters/setters (encapsulation)
- Use `final` for immutable fields
- Use `final` class if not intended for inheritance
- Override `toString()` for meaningful output
- Validate in constructors and setters

## Interview Questions

1. **Difference between class and object?**
   - Class = blueprint, Object = instance

2. **What happens when you create an object?**
   - Memory allocated on heap, constructor runs, reference returned

3. **Why use `final` class?**
   - Prevents inheritance (e.g., `String`, `Integer`)

## Related Topics

- [Constructors](constructors.md) →
- [Methods](methods.md) →
- [Encapsulation](encapsulation.md) →
- [Memory Management](../theory.md#object-creation--memory)

## References

- [Java Language Specification - Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- [Effective Java Item 17: Minimize Mutability](https://www.oracle.com/technical-resources/articles/java/effective-java.html)