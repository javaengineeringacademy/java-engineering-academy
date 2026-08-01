# super Keyword

## Objective
Understand the `super` keyword for accessing parent class members and constructors.

## Learning Objectives
- Understand what `super` refers to and how it differs from `this`
- Use `super()` to call parent constructors with various argument patterns
- Access hidden parent class fields and methods via `super`
- Apply `super` in inheritance hierarchies to extend parent behavior

## Prerequisites
- Class inheritance (`extends` keyword)
- Constructor basics
- Method overriding
- `this` keyword familiarity

## Why This Concept Exists
In Java inheritance, child classes can shadow parent fields or override parent methods. The `super` keyword provides an explicit mechanism to access the original parent implementation, enabling code reuse and extension rather than complete replacement.

## Problem Statement
Without `super`, a child class that overrides a method or shadows a field has no way to reference the parent's version. This makes it impossible to extend (rather than replace) parent behavior, breaking the open/closed principle.

## Theory

### What is `super`?
The `super` keyword is a reference variable that refers to the **parent class object**. It is used to access parent class members that are hidden or overridden in the child class.

### How `super` Works Internally
When a child class is instantiated:
1. The child constructor calls `super()` (implicitly or explicitly)
2. The parent class constructor executes first
3. Parent fields are initialized before child fields
4. `super.field` accesses the parent's copy of a field
5. `super.method()` invokes the parent's implementation of an overridden method

### `super` and the Call Stack
```java
class A { A() { System.out.println("A"); } }
class B extends A { B() { super(); System.out.println("B"); } }
class C extends B { C() { super(); System.out.println("C"); } }

// new C() prints: A, B, C
// Constructors execute top-down in the inheritance hierarchy
```

### Where `super` Is Allowed
- Inside constructors: `super()` or `super(args)`
- Inside instance methods: `super.field`, `super.method()`
- NOT allowed in static methods, static blocks, or interfaces

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

### 4. Chained Constructor Calls (Java 21)
```java
public abstract class Shape {
    protected final String color;

    protected Shape(String color) {
        this.color = color;
    }
}

public class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        this(radius, "Black");  // Calls 2-arg constructor
    }

    public Circle(double radius, String color) {
        super(color);  // Must be first
        this.radius = radius;
    }
}
```

### 5. Extending Overridden Behavior (Template Method Pattern)
```java
abstract class DataProcessor {
    public void process() {
        validate();
        transform();
        save();
    }

    protected void validate() {
        System.out.println("Basic validation");
    }

    protected abstract void transform();

    protected void save() {
        System.out.println("Saving to database");
    }
}

class CsvProcessor extends DataProcessor {
    @Override
    protected void validate() {
        super.validate();  // Reuse parent validation
        System.out.println("CSV-specific validation");  // Extend
    }

    @Override
    protected void transform() {
        System.out.println("Parsing CSV");
    }
}
```

## Internal Working

### Constructor Chaining Mechanism
```
new Dog("Rex", "Labrador")
│
├─ Dog(name, breed) {
│   └─ super(name);         ← Calls Animal(String)
│       └─ Animal(name) {
│           └─ super();      ← Calls Object()
│           └─ this.name = name;
│       }
│   └─ this.breed = breed;
│ }
```

### Method Resolution with `super`
```java
class Parent {
    void display() { System.out.println("Parent"); }
}

class Child extends Parent {
    @Override
    void display() { System.out.println("Child"); }

    void show() {
        display();       // Calls Child.display() (dynamic dispatch)
        super.display(); // Calls Parent.display() (explicit parent)
    }
}
```

## JVM Perspective
- `super()` is syntactic sugar for the JVM's `invokespecial` instruction
- `super.method()` bypasses virtual dispatch and calls the parent's method directly
- `super.field` accesses the field slot defined in the parent class

## Memory Representation

```
Stack:                    Heap:
┌──────────────┐        ┌──────────────────────────┐
│ dog ref ─────┼───────▶│ Dog object               │
└──────────────┘        │  ┌─ Animal fields ──────┐ │
                        │  │ name: "Rex"          │ │
                        │  └──────────────────────┘ │
                        │  ┌─ Dog fields ─────────┐ │
                        │  │ breed: "Labrador"    │ │
                        │  └──────────────────────┘ │
                        └──────────────────────────┘
```

## Syntax

```java
// Constructor call
super();
super(args);

// Field access
super.fieldName;

// Method call
super.methodName(args);
```

## Easy Example

```java
class Animal {
    String type = "Animal";

    void speak() {
        System.out.println("Some sound");
    }
}

class Dog extends Animal {
    String type = "Dog";

    @Override
    void speak() {
        super.speak();  // "Some sound"
        System.out.println("Woof!");
    }

    void identify() {
        System.out.println("Child type: " + type);    // Dog
        System.out.println("Parent type: " + super.type); // Animal
    }
}
```

## Medium Example

```java
public class Employee {
    protected double baseSalary = 50000;

    public double calculatePay() {
        return baseSalary;
    }
}

public class Manager extends Employee {
    private double bonus = 10000;

    @Override
    public double calculatePay() {
        double base = super.calculatePay();  // Reuse parent logic
        return base + bonus;
    }
}
```

## Hard Example

```java
abstract class AbstractRepository<T> {
    protected abstract T findById(Object id);

    protected void validateId(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }
}

abstract class CachingRepository<T> extends AbstractRepository<T> {
    private final Map<Object, T> cache = new HashMap<>();

    @Override
    protected T findById(Object id) {
        super.validateId(id);  // Reuse parent validation
        return cache.computeIfAbsent(id, this::loadFromDb);
    }

    protected abstract T loadFromDb(Object id);
}

public class UserRepository extends CachingRepository<User> {
    @Override
    protected User loadFromDb(Object id) {
        return database.query("SELECT * FROM users WHERE id = ?", id);
    }
}
```

## Enterprise Example

```java
public abstract class BaseService<T, R extends JpaRepository<T, Long>> {
    protected final R repository;

    protected BaseService(R repository) {
        this.repository = repository;
    }

    @Transactional
    public R save(T entity) {
        validate(entity);
        return repository.save(entity);
    }

    protected void validate(T entity) {
        // Base validation logic
    }
}

public class UserService extends BaseService<User, UserRepository> {
    private final EmailService emailService;

    public UserService(UserRepository repository, EmailService emailService) {
        super(repository);  // Pass to parent constructor
        this.emailService = emailService;
    }

    @Override
    protected void validate(User user) {
        super.validate(user);  // Base validation first
        if (user.getEmail() == null) {
            throw new ValidationException("Email required");
        }
    }

    @Transactional
    public User register(User user) {
        User saved = super.save(user);  // Reuse parent save logic
        emailService.sendWelcome(saved);
        return saved;
    }
}
```

## Performance
- `super.method()` calls are not more expensive than regular method calls
- The JVM can inline `super` calls when the parent class is known
- Constructor chaining via `super()` has negligible overhead
- No performance difference between `super.field` and direct field access

## Best Practices
- Use `super()` in constructors explicitly when you have a parameterized parent constructor
- Always call `super.validate()` or similar methods to reuse parent logic
- Avoid deep inheritance hierarchies where `super` chains become confusing
- Prefer composition over deep `super` chains
- Document when your method relies on calling `super.method()`

## Common Mistakes

### Mistake 1: `super()` Not as First Statement
```java
class Child extends Parent {
    public Child() {
        System.out.println("Hello");
        super();  // Compile error: call to super must be first statement
    }
}
```

### Mistake 2: `super()` in Static Context
```java
class Child extends Parent {
    public static void create() {
        super();  // Compile error: call to super not allowed here
    }
}
```

### Mistake 3: Forgetting Parent Constructor Has No Default
```java
class Parent {
    Parent(String name) { }  // No default constructor
}

class Child extends Parent {
    Child() {
        super();  // Compile error: Parent() not found
    }
    // Fix: super("default");
}
```

### Mistake 4: Using `super` to Access Sibling Class
```java
class A extends B { }
class C extends B { }

class A {
    void method() {
        super.field;  // Refers to B, not C. This is fine.
        C c = new C();
        c.super.field;  // Compile error: cannot access sibling's parent
    }
}
```

## Pitfalls
- `super` only goes one level up; you cannot use `super.super.method()`
- `super` cannot be used in lambda expressions or anonymous classes
- `super()` and `this()` cannot coexist in the same constructor
- In interfaces, `super` is used in default methods to call parent interface methods

## Debugging Tips
- Place breakpoints on `super()` calls to trace constructor execution order
- Use `jadx` or `javap` to see how `super.method()` translates to bytecode
- When inheritance issues arise, draw the full constructor chain diagram
- Log in both parent and child to see execution order

## Comparison Table

| Aspect | `super` | `this` |
|--------|---------|--------|
| Refers to | Parent class instance | Current class instance |
| Constructor call | `super()` / `super(args)` | `this()` / `this(args)` |
| Field access | `super.field` (parent's field) | `this.field` (current class field) |
| Method call | `super.method()` (parent's impl) | `this.method()` (current class impl) |
| Static context | Not allowed | Not allowed |
| Must be first in constructor | Yes (`super()`) | Yes (`this()`) |
| Can chain | One level up only | Same class only |

## Decision Tree

```
Need to access parent class member?
├── Yes, constructor → super() or super(args)
├── Yes, method → super.method() (if overridden)
├── Yes, field → super.field (if shadowed)
├── No, need current object → this
└── Confused?
    ├── Field shadowed? → super.field
    ├── Method overridden? → super.method()
    └── Constructor chaining? → super() first line
```

## Interview Questions

1. **Can we use `super()` and `this()` together?**
   No. Both must be the first statement in a constructor, so they are mutually exclusive.

2. **Can `super()` call a parameterized constructor?**
   Yes. `super(arg1, arg2)` calls the matching parent constructor.

3. **Can we call `super.super.method()`?**
   No. Java does not support multi-level parent access via `super`.

4. **What happens if a parent class has no no-arg constructor?**
   The child must explicitly call `super(args)` with the correct arguments, or a compile error occurs.

5. **Can `super` be used in an interface?**
   Yes, in default methods: `Interface.super.method()` calls the parent interface's default method.

6. **Does `super` create a new parent object?**
   No. `super` references the parent portion of the existing object already in memory.

7. **Can `super` be used in a lambda?**
   No. Lambdas do not have a `super` reference. Use an anonymous class instead.

8. **What is the relationship between `super` and the `extends` clause?**
   `extends` defines the parent-child relationship. `super` is the runtime mechanism to access parent members within that relationship.

## Exercises
1. Create a class hierarchy with 3 levels (Animal → Dog → Puppy). Use `super` in each constructor to chain calls.
2. Write a `BankAccount` base class and `SavingsAccount` subclass. Override `calculateInterest()` using `super.calculateInterest()`.
3. Implement a Template Method pattern where the child uses `super.step1()` to extend parent behavior.
4. Create an interface with a default method and a class that calls `Interface.super.method()`.

## Assignments
1. Build a shape hierarchy (Shape → Circle, Rectangle) where each constructor uses `super` to pass color to the parent.
2. Refactor an existing class to use `super.validate()` for reusable validation logic.
3. Write unit tests that verify constructor chaining order using `super()`.

## Mini Project
**Inheritance Logger:** Create a `Logger` base class with `log()`, `warn()`, `error()` methods. Build `FileLogger` and `ConsoleLogger` subclasses that use `super.log()` to reuse common formatting before adding their own output logic.

## Summary
- `super` references the parent class portion of the current object
- `super()` calls the parent constructor (must be first statement)
- `super.method()` invokes the parent's implementation of an overridden method
- `super.field` accesses a parent field hidden by a child field
- `super` works only in instance contexts, never in static methods
- You cannot chain `super.super` — only one level up is allowed

## References
- [JLS - The super Keyword](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.11.2)
- [JLS - Constructor Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.8)
- [Effective Java - Item 19: Design and document for inheritance or else prohibit it](https://books.google.com/books?id=BIpKEttKoLYC)
- [Java Tutorials - Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html)