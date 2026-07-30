# Classes and Objects

## What is a Class?
A **class** is a blueprint or template that defines the structure and behavior of objects. It encapsulates data (fields) and operations (methods) into a single unit.

## What is an Object?
An **object** is an instance of a class. It has:
- **State**: Values of its fields (instance variables)
- **Behavior**: What it can do (methods)
- **Identity**: Unique reference in memory

## Real-World Analogy
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
        this.name = name;
        this.age = validateAge(age);
        this.email = email;
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

## Key Points
- Classes define structure, objects hold state
- Use `private` fields + public getters/setters for encapsulation
- Use `final` for immutable fields
- Override `toString()` for meaningful output