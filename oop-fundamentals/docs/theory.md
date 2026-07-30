# Sprint 2: Object Oriented Programming - Theory

## 1. Classes and Objects

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

### Class Structure
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

### Object Creation & Memory
```
Stack                          Heap
─────────────────              ─────────────────
reference: obj ──────────────▶ Object: ClassName
                                 - field1: value
                                 - field2: value
```

---

## 2. Constructors

### Types of Constructors
1. **No-arg Constructor**: `public ClassName() { ... }`
2. **Parameterized Constructor**: `public ClassName(Type param) { ... }`
3. **Copy Constructor**: `public ClassName(ClassName other) { ... }`
4. **Builder Pattern**: For complex objects with many optional fields

### Constructor Rules
- Same name as class
- No return type (not even void)
- Can be overloaded
- If no constructor defined → compiler provides default no-arg

### Constructor Chaining
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
        this(name, age, "Unknown");
    }
    
    public Person(String name) {
        this(name, 0);
    }
}
```

### Best Practices
- Validate parameters in constructors
- Use `final` for immutable fields
- Prefer constructor over setters for required fields
- Keep constructors simple; delegate complex logic to factory methods

---

## 3. Methods

### Method Signature
```java
[modifiers] ReturnType methodName(ParameterType paramName) [throws Exception] {
    // body
}
```

### Method Types
| Type | Keyword | Use Case |
|------|---------|----------|
| Instance | (none) | Operates on object state |
| Static | `static` | Utility, no instance needed |
| Final | `final` | Cannot be overridden |
| Abstract | `abstract` | No body, must be implemented |
| Synchronized | `synchronized` | Thread-safe |

### Method Overloading
Same name, different parameter list (number, type, or order):
```java
public void print(String s) { ... }
public void print(int i) { ... }
public void print(String s, int count) { ... }
```

### Method Overriding
Subclass provides specific implementation:
```java
@Override
public String toString() { ... }
```

**Rules:**
- Same signature (name + parameters)
- Return type must be covariant
- Access modifier cannot be more restrictive
- Cannot override `final`, `static`, or `private` methods

### Varargs
```java
public void printAll(String... items) {
    for (String item : items) System.out.println(item);
}
// Call: printAll("a", "b", "c") or printAll(new String[]{"a","b"})
```

---

## 4. Encapsulation

### What is Encapsulation?
Bundling data (fields) and methods that operate on that data, restricting direct access to internal state.

### Access Modifiers
| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| default (package) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

### Encapsulation Pattern
```java
public class BankAccount {
    private BigDecimal balance;  // Hidden
    
    public BigDecimal getBalance() {  // Controlled read
        return balance;
    }
    
    public void deposit(BigDecimal amount) {  // Controlled write
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance = balance.add(amount);
    }
}
```

### Benefits
- **Control**: Validate before setting
- **Flexibility**: Change implementation without breaking clients
- **Security**: Protect invariants
- **Maintainability**: Single point of change

---

## 5. Inheritance

### What is Inheritance?
Mechanism where a new class (subclass) derives from an existing class (superclass), inheriting fields and methods.

### Syntax
```java
public class SubClass extends SuperClass { ... }
```

### What is Inherited?
| Member | Inherited? |
|--------|------------|
| `public` fields/methods | ✓ |
| `protected` fields/methods | ✓ |
| Package-private (same package) | ✓ |
| `private` fields/methods | ✗ (accessible via getters) |
| Constructors | ✗ (but `super()` calls parent) |

### `super` Keyword
```java
public class Child extends Parent {
    public Child() {
        super(); // Call parent no-arg constructor
    }
    
    @Override
    public void method() {
        super.method(); // Call parent implementation
        // child-specific logic
    }
}
```

### Rules
- Java supports **single inheritance** (one parent)
- Multiple inheritance via interfaces
- All classes implicitly extend `Object`

### `final` Keyword
| Applied To | Effect |
|------------|--------|
| Class | Cannot be extended |
| Method | Cannot be overridden |
| Field | Value cannot change after initialization |
| Variable | Reference cannot change |

---

## 6. Polymorphism

### What is Polymorphism?
"Many forms" - ability to treat objects of different classes uniformly through a common interface.

### Types

#### Compile-time (Static) Polymorphism
- **Method Overloading**: Same name, different parameters
- Resolved at compile time

#### Runtime (Dynamic) Polymorphism
- **Method Overriding**: Subclass provides specific implementation
- Resolved at runtime via virtual method table

### Example
```java
Animal animal = new Dog();  // Upcasting
animal.makeSound();  // Calls Dog.makeSound()

// Downcasting (with check)
if (animal instanceof Dog) {
    Dog dog = (Dog) animal;
    dog.fetch();
}
```

### Method Dispatch
```
Reference Type: Animal          Object Type: Dog
animal.makeSound() ──────────▶ Dog.makeSound()  (Runtime)
```

---

## 7. Abstraction

### Abstract Class
- Cannot be instantiated
- May contain abstract and concrete methods
- Used when classes share common behavior but differ in specifics

```java
public abstract class Shape {
    protected String color;
    
    public Shape(String color) { this.color = color; }
    
    public abstract double area();  // Must implement
    public abstract double perimeter();
    
    public String getColor() { return color; }  // Concrete
}
```

### Abstract Methods
- No body, ends with semicolon
- Must be implemented by concrete subclass
- Class with abstract methods must be abstract

### When to Use
- "Is-a" relationship with shared code
- Template Method pattern
- Need to enforce contract

---

## 8. Interfaces

### What is an Interface?
Contract specifying what a class can do, without saying how. Pure abstraction (pre-Java 8).

### Modern Interfaces (Java 8+)
```java
public interface Payable {
    void pay(BigDecimal amount);  // Abstract
    
    default void printReceipt() {  // Default implementation
        System.out.println("Receipt printed");
    }
    
    static BigDecimal calculateTax(BigDecimal amount) {  // Static
        return amount.multiply(BigDecimal.valueOf(0.18));
    }
}
```

### Interface Rules
- All fields: `public static final` (constants)
- All methods: `public` (default since Java 8)
- Multiple inheritance: `class A implements B, C { }`
- Functional interface: Single abstract method → lambda support

### Interface vs Abstract Class
| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Multiple impl | Yes | No (single) |
| Fields | Constants only | Instance fields OK |
| Constructors | No | Yes |
| Default methods | Yes (Java 8+) | N/A |
| Use case | Contract, capability | Shared code + contract |

---

## 9. Object Class Methods

### Key Methods to Override

#### `toString()`
```java
@Override
public String toString() {
    return "Person{name='%s', age=%d}".formatted(name, age);
}
```

#### `equals(Object obj)` & `hashCode()`
**Contract:** If `a.equals(b)` then `a.hashCode() == b.hashCode()`

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Person person = (Person) obj;
    return age == person.age && Objects.equals(name, person.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

#### `clone()`
```java
@Override
protected Object clone() throws CloneNotSupportedException {
    return super.clone();  // Shallow copy
}
```

### Other Methods
- `finalize()`: Deprecated (Java 9+), use try-with-resources/Cleaner
- `getClass()`: Returns runtime class
- `notify()`, `notifyAll()`, `wait()`: Thread synchronization

---

## 10. equals() and hashCode()

### Contract
1. **Reflexive**: `x.equals(x)` → `true`
2. **Symmetric**: `x.equals(y)` ↔ `y.equals(x)`
3. **Transitive**: `x.equals(y)` && `y.equals(z)` → `x.equals(z)`
4. **Consistent**: Multiple calls return same result
5. **Null**: `x.equals(null)` → `false`

### Implementation Template
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyClass that = (MyClass) o;
    return primitiveField == that.primitiveField &&
           Objects.equals(referenceField, that.referenceField);
}

@Override
public int hashCode() {
    return Objects.hash(primitiveField, referenceField);
}
```

### Common Mistakes
- Using `==` instead of `.equals()` for objects
- Overriding `equals` but not `hashCode`
- Using mutable fields in `hashCode`
- Not checking `null` or class type

---

## 11. Composition, Aggregation, Association

### Association (General)
Relationship where objects know about each other.

### Aggregation (Weak "Has-A")
- Whole can exist without parts
- Parts can belong to multiple wholes
- **Example**: Department —▶ Employees

```java
class Department {
    private List<Employee> employees = new ArrayList<>();  // Aggregation
}
```

### Composition (Strong "Has-A")
- Part cannot exist without whole
- Lifecycle tied to whole
- **Example**: House —▶ Rooms

```java
class House {
    private final List<Room> rooms = new ArrayList<>();  // Composition
    
    public House() {
        rooms.add(new Room("Kitchen"));
        rooms.add(new Room("Bedroom"));
    }
}
```

### Decision Guide
| Question | Composition | Aggregation |
|----------|-------------|-------------|
| Can part exist independently? | No | Yes |
| Lifecycle tied to whole? | Yes | No |
| Part belongs to multiple wholes? | No | Yes |

---

## 12. Dependency Injection

### What is DI?
Providing dependencies from outside rather than creating them internally.

### Types
```java
// Constructor Injection (Recommended)
public class OrderService {
    private final PaymentProcessor processor;
    
    public OrderService(PaymentProcessor processor) {
        this.processor = processor;
    }
}

// Setter Injection
public class OrderService {
    private PaymentProcessor processor;
    
    public void setPaymentProcessor(PaymentProcessor processor) {
        this.processor = processor;
    }
}

// Field Injection (Avoid - hard to test)
public class OrderService {
    @Autowired
    private PaymentProcessor processor;
}
```

### Benefits
- **Testability**: Easy to mock dependencies
- **Flexibility**: Swap implementations
- **Loose Coupling**: Classes don't create dependencies
- **Single Responsibility**: Creation delegated

---

## 13. SOLID Principles

| Principle | Definition | Violation Example |
|-----------|------------|-------------------|
| **S**ingle Responsibility | Class has one reason to change | God class doing everything |
| **O**pen/Closed | Open for extension, closed for modification | `if (type == A) ... else if (type == B)` |
| **L**iskov Substitution | Subtype must be substitutable for base | Square extends Rectangle (can't set width/height independently) |
| **I**nterface Segregation | Many specific interfaces > one general | `IShape` with `draw()`, `resize()`, `rotate()` for all |
| **D**ependency Inversion | Depend on abstractions, not concretions | `OrderService` depends on `PaymentProcessor` interface, not `StripeProcessor` |

---

## 14. Records (Java 16+)

### What are Records?
Immutable data carriers with automatic implementations:
```java
public record Person(String name, int age, String email) { }

// Equivalent to:
public final class Person {
    private final String name;
    private final int age;
    private final String email;
    
    public Person(String name, int age, String email) { ... }
    public String name() { return name; }
    public int age() { return age; }
    public String email() { return email; }
    // equals, hashCode, toString auto-generated
}
```

### Compact Constructors (Validation)
```java
public record Person(String name, int age) {
    public Person {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name required");
        }
        if (age < 0) throw new IllegalArgumentException("Age >= 0");
    }
}
```

### When to Use
- DTOs, value objects, data carriers
- Immutable data carriers
- Not for mutable entities with behavior

---

## Summary: Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Class/Object** | Blueprint vs Instance |
| **Encapsulation** | Private fields + public methods |
| **Inheritance** | `extends`, single inheritance, `super` |
| **Polymorphism** | Compile-time (overload) vs Runtime (override) |
| **Abstraction** | Abstract class (partial) vs Interface (full) |
| **Encapsulation** | Private fields, public getters/setters |
| **Composition > Inheritance** | Favor composition for flexibility |
| **Dependency Injection** | Constructor injection preferred |
| **SOLID** | Design principles for maintainable code |
| **Records** | Immutable data carriers (Java 16+) |

---

## Interview Questions Preview

1. **Difference between abstract class and interface?**
2. **Can we override static method? Why?**
3. **Why override `hashCode` with `equals`?**
4. **Composition vs Inheritance - when to use which?**
5. **What is Dependency Injection? Types?**
5. **Explain SOLID with examples**
6. **Method overloading vs overriding?**
7. **Can constructor be private? When?**
8. **What is method hiding vs overriding?**
9. **Difference between `==` and `.equals()`?**
10. **When to use abstract class vs interface?**