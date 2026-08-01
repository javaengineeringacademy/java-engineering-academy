# Polymorphism

## Compile-time (Static) Polymorphism

### Method Overloading
Same name, different parameter list:

```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public int add(int... numbers) { return Arrays.stream(numbers).sum(); }
}
```

**Rules:**
- Different parameter list (number, type, order)
- Return type can differ
- Access modifier can differ

## Runtime (Dynamic) Polymorphism

### Method Overriding
Subclass provides specific implementation:

```java
class Animal {
    public void makeSound() { System.out.println("Animal sound"); }
}

class Dog extends Animal {
    @Override
    public void makeSound() { System.out.println("Woof!"); }
}

class Cat extends Animal {
    @Override
    public void makeSound() { System.out.println("Meow!"); }
}
```

**Rules:**
- Same signature (name + parameters)
- Return type: covariant (same or subclass)
- Access modifier: same or less restrictive
- Cannot override: `final`, `static`, `private`, `final` methods

## Upcasting & Downcasting

### Upcasting (Implicit, Safe)
```java
Animal animal = new Dog();  // Upcasting
animal.makeSound(); // Calls Dog.makeSound()
```

### Downcasting (Explicit, Needs Check)
```java
Animal animal = new Dog();
if (animal instanceof Dog dog) {
    dog.fetch();  // Safe downcast
}

// Pattern matching (Java 16+)
if (animal instanceof Dog dog) {
    dog.fetch();
}
```

## Method Dispatch

### Static Dispatch (Compile-time)
- Method overloading
- Resolved at compile time
- Based on reference type

### Dynamic Dispatch (Runtime)
- Method overriding
- Resolved at runtime
- Based on actual object type

```
Reference Type: Animal          Object Type: Dog
animal.makeSound() ──────────▶ Dog.makeSound()  (Runtime)
```

## Upcasting vs Downcasting

| Aspect | Upcasting | Downcasting |
|--------|-----------|-------------|
| Direction | Subclass → Superclass | Superclass → Subclass |
| Safety | Always safe | Requires check |
| Cast | Implicit | Explicit `(Type)` |
| Use case | Polymorphism | Access subclass members |

## `instanceof` Pattern Matching (Java 16+)

```java
// Old way
if (animal instanceof Dog) {
    Dog dog = (Dog) animal;
    dog.fetch();
}

// Pattern matching (Java 16+)
if (animal instanceof Dog dog) {
    dog.fetch();  // Direct use
}

// Switch expressions (Java 17+)
String sound = switch (animal) {
    case Dog d -> "Woof";
    case Cat c -> "Meow";
    default -> "Unknown";
};
```

## Method Dispatch Visualization

### Static Dispatch (Overloading)
```java
class Printer {
    void print(int i) { System.out.println("int: " + i); }
    void print(String s) { System.out.println("String: " + s); }
}

// Compile-time: Printer::print(int) chosen
printer.print(42);
```

### Dynamic Dispatch (Overriding)
```java
Animal a = new Dog();  // Actual: Dog
a.makeSound();  // Runtime: Dog.makeSound()
```

**Dispatch Process:**
1. JVM looks at actual object type (Dog)
2. Finds overridden method in Dog
3. Executes Dog's implementation

## Covariant Return Types

```java
class Animal {
    Animal getSelf() { return this; }
}

class Dog extends Animal {
    @Override
    Dog getSelf() { return this; }  // Covariant return
}
```

## Bridge Methods

```java
interface Comparable<T> {
    int compareTo(T o);
}

class MyClass implements Comparable<MyClass> {
    @Override
    public int compareTo(MyClass o) { return 0; }
    
    // Compiler generates bridge method:
    // public int compareTo(Object o) { return compareTo((MyClass)o); }
}
```

## Interview Questions

### Core Concepts

1. **What is polymorphism?**
   Polymorphism means "many forms." It allows objects of different types to be treated through a single interface. Java supports compile-time (overloading) and runtime (overriding) polymorphism.

2. **Overloading vs Overriding?**
   - Overloading: same name, different parameter list (compile-time resolution via static dispatch)
   - Overriding: same signature in subclass, provides specific implementation (runtime resolution via dynamic dispatch)

3. **What is dynamic method dispatch?**
   The JVM resolves the actual method to call at runtime based on the object's real type, not the reference type. This enables polymorphic behavior.

4. **What is covariant return type?**
   An overriding method can return a subclass of the parent method's return type (Java 5+).

### Technical Questions

5. **Can we override static methods?**
   No. Static methods belong to the class, not instances. They are hidden (not overridden). The method called depends on the reference type at compile time.

6. **Can we override private methods?**
   No. Private methods are not visible to subclasses and cannot be overridden. They are resolved at compile time.

7. **Can we override final methods?**
   No. The `final` modifier prevents method overriding. A compile-time error occurs if you attempt it.

8. **Can constructors be overloaded?**
   Yes. Constructors can be overloaded by changing the parameter list. This is the basis for `this()` constructor chaining.

9. **Can constructors be overridden?**
   No. Constructors are not inherited and cannot be overridden. Each class defines its own constructors.

10. **What is the difference between method hiding and method overriding?**
    Method hiding occurs with static methods where the reference type determines which method is called. Method overriding applies to instance methods where the actual object type determines the call.

### Advanced Questions

11. **Explain bridge methods.**
    The compiler generates bridge methods to preserve polymorphism with generics. When a generic class overrides a method, the bridge method handles type erasure by casting and delegating.

12. **What is the Liskov Substitution Principle (LSP)?**
    Subtypes must be substitutable for their base types without altering correctness. Overriding methods must not strengthen preconditions or weaken postconditions.

13. **How does `instanceof` pattern matching (Java 16+) help with polymorphism?**
    It eliminates explicit casting and boilerplate. `if (obj instanceof Dog dog)` combines the type check and cast in one step.

14. **What is the performance impact of dynamic dispatch?**
    Dynamic dispatch has a minor overhead due to vtable lookup. The JVM uses inline caches and devirtualization to mitigate this. The overhead is negligible in most applications.

15. **Can overloaded methods have the same return type?**
    Yes. Overloading is determined by the parameter list, not the return type. Two methods with the same name, same parameters, and different return types will cause a compile error.