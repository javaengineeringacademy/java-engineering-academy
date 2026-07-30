# OOP Cheatsheet - Sprint 2 Quick Reference

---

## 🎯 Core Concepts

| Concept | Keyword | Description |
|---------|---------|-------------|
| Class | `class` | Blueprint for objects |
| Object | `new` | Instance of class |
| Encapsulation | `private` + getters/setters | Hide internal state |
| Inheritance | `extends` | Reuse + extend |
| Polymorphism | `@Override` | Same interface, different behavior |
| Abstraction | `abstract` / `interface` | Hide complexity |

---

## 📋 Class Structure

```java
public class ClassName {
    // Fields
    private Type fieldName;
    
    // Constructors
    public ClassName() {}
    public ClassName(Type param) { this.field = param; }
    
    // Methods
    public ReturnType methodName(ParamType param) { ... }
    
    // Getters/Setters
    public Type getField() { return field; }
    public void setField(Type value) { this.field = value; }
}
```

---

## 🏗️ Constructors

```java
// No-arg
public ClassName() {}

// Parameterized
public ClassName(Type param) { this.field = param; }

// Copy constructor
public ClassName(ClassName other) { this.field = other.field; }

// Constructor chaining
public ClassName() { this(defaultValue); }
public ClassName(Type param) { this.field = param; }
```

---

## 🔐 Access Modifiers

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| (default) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

---

## 🔄 Inheritance

```java
public class Child extends Parent {
    // Inherits non-private fields/methods
    // Can override methods
    // Can add new members
    
    @Override
    public void method() {
        super.method(); // Call parent
        // child logic
    }
}
```

**Rules:**
- Single inheritance only
- `private` not inherited (use getters)
- Constructors not inherited
- All classes extend `Object` implicitly

---

## 🔄 Polymorphism

### Compile-time (Static)
```java
// Overloading
void print(String s) {}
void print(int i) {}
void print(String s, int count) {}
```

### Runtime (Dynamic)
```java
// Overriding
@Override
public void method() { ... }

// Upcasting
Animal a = new Dog();

// Downcasting
if (animal instanceof Dog dog) {
    dog.bark();
}
```

---

## 📐 Abstract Classes

```java
public abstract class Shape {
    protected String color;
    
    public Shape(String color) { this.color = color; }
    
    public abstract double area();      // Must implement
    public abstract double perimeter(); // Must implement
    
    public String getColor() { return color; } // Concrete
}
```

---

## 📋 Interfaces

```java
public interface Payable {
    void pay(BigDecimal amount);           // Abstract
    
    default void printReceipt() {          // Default (Java 8+)
        System.out.println("Receipt printed");
    }
    
    static BigDecimal tax(BigDecimal amt) { // Static
        return amt.multiply(BigDecimal.valueOf(0.18));
    }
}
```

**Rules:**
- Multiple implementation: `class A implements B, C {}`
- All methods `public` by default
- Fields are `public static final` constants
- Functional interface = 1 abstract method → lambda

---

## 🎭 Abstract vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Inheritance | Single | Multiple |
| Fields | Instance + constants | Constants only |
| Constructors | Yes | No |
| Methods | Abstract + concrete | Abstract + default/static |
| Use case | Shared code + contract | Pure contract |

---

## 🧱 Composition vs Inheritance

| Aspect | Composition | Inheritance |
|--------|-------------|-------------|
| Relationship | Has-a | Is-a |
| Coupling | Loose | Tight |
| Flexibility | High (runtime) | Low (compile) |
| Code reuse | Delegation | Inheritance |

```java
// Composition
class Department {
    private List<Employee> employees = new ArrayList<>();
}

// Inheritance
class Manager extends Employee { }
```

---

## 🔗 Dependency Injection

```java
// Constructor Injection (Best)
class OrderService {
    private final PaymentProcessor processor;
    public OrderService(PaymentProcessor p) { this.processor = p; }
}

// Setter Injection
class OrderService {
    private PaymentProcessor processor;
    public void setProcessor(PaymentProcessor p) { this.processor = p; }
}
```

---

## 🎯 SOLID Principles

| Principle | Definition | Example |
|-----------|------------|---------|
| **S**RP | One reason to change | Split `UserManager` → `UserRepository`, `EmailService` |
| **O**CP | Open for extension, closed for modification | Add new `PaymentMethod` without changing `OrderService` |
| **L**SP | Subtype substitutable for base | `Square` shouldn't extend `Rectangle` |
| **I**SP | Many specific > one fat interface | `Swimmable`, `Flyable` vs `Animal` with all methods |
| **D**IP | Depend on abstractions | `OrderService` depends on `PaymentProcessor` interface |

---

## 🎯 equals() & hashCode()

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyClass that = (MyClass) o;
    return primitive == that.primitive && 
           Objects.equals(ref, that.ref);
}

@Override
public int hashCode() {
    return Objects.hash(primitive, ref);
}
```

**Contract:** If `equals()` true → `hashCode()` must be equal

---

## 📦 Records (Java 16+)

```java
public record Person(String name, int age, String email) {
    public Person {
        if (name == null || name.isBlank()) throw new IllegalArgumentException();
        if (age < 0) throw new IllegalArgumentException();
    }
    
    // Auto: constructor, getters, equals, hashCode, toString
}
```

---

## 📋 Design Patterns Quick Reference

| Pattern | Type | Use Case |
|---------|------|----------|
| **Singleton** | Creational | Global access, one instance |
| **Factory** | Creational | Object creation logic |
| **Builder** | Creational | Complex objects, many params |
| **Adapter** | Structural | Incompatible interfaces |
| **Decorator** | Structural | Add behavior dynamically |
| **Strategy** | Behavioral | Swap algorithms at runtime |
| **Observer** | Behavioral | Event notification |
| **Template Method** | Behavioral | Algorithm skeleton |

---

## 🔑 Key Annotations

| Annotation | Use |
|------------|-----|
| `@Override` | Verify method overriding |
| `@SuppressWarnings` | Suppress compiler warnings |
| `@Deprecated` | Mark deprecated API |
| `@FunctionalInterface` | Verify single abstract method |
| `@SafeVarargs` | Suppress heap pollution warning |

---

## 🚫 Common Mistakes

| Mistake | Fix |
|---------|-----|
| `Square extends Rectangle` | Use composition or separate interface |
| `equals` without `hashCode` | Always override both |
| `==` for String comparison | Use `.equals()` |
| Mutable fields in `hashCode` | Use only immutable fields |
| `final` on method instead of class | `final class` prevents extension |
| Missing `@Override` | Add to catch signature errors |
| `instanceof` + cast everywhere | Use polymorphism |

---

## 🎯 Quick Commands

```bash
# Compile
mvn compile

# Run tests
mvn test

# Quality checks
mvn checkstyle:check spotbugs:check pmd:check

# Full verify
mvn clean verify -Pci

# Generate Javadoc
mvn javadoc:javadoc
```

---

## 📚 Quick Navigation

| Topic | File |
|-------|------|
| Classes/Objects | `classes.md` |
| Constructors | `constructors.md` |
| Methods | `methods.md` |
| Encapsulation | `encapsulation.md` |
| Inheritance | `inheritance.md` |
| Polymorphism | `polymorphism.md` |
| Abstraction | `abstraction.md` |
| Interfaces | `interfaces.md` |
| Object Class | `object-class.md` |
| Equals/HashCode | `equals-hashcode.md` |
| Composition | `composition-aggregation.md` |
| Dependency Injection | `dependency-injection.md` |
| SOLID | `solid.md` |

---

*Keep this handy during development and interviews!*