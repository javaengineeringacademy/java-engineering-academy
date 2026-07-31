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

1. **Overloading vs Overriding?**
   - Overloading: same name, diff params (compile-time)
   - Overriding: same signature, subclass impl (runtime)

2. **Can we override static method?** No, static methods are hidden, not overridden.

3. **Can we override private method?** No, private not visible to subclass.

4. **What is dynamic method dispatch?** Runtime resolution of overridden method.

4. **What is covariant return type?** Overriding method can return subclass of parent's return type.