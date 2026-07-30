# Polymorphism

## What is Polymorphism?
"Many forms" - ability to treat objects of different classes uniformly through a common interface.

## Types

### Compile-time (Static) Polymorphism
- **Method Overloading**: Same name, different parameters
- Resolved at compile time

### Runtime (Dynamic) Polymorphism
- **Method Overriding**: Subclass provides specific implementation
- Resolved at runtime via virtual method table

## Example
```java
Animal animal = new Dog();  // Upcasting
animal.makeSound();  // Calls Dog.makeSound()

// Downcasting (with check)
if (animal instanceof Dog) {
    Dog dog = (Dog) animal;
    dog.fetch();
}
```

## Method Dispatch
```
Reference Type: Animal          Object Type: Dog
animal.makeSound() ──────────▶ Dog.makeSound()  (Runtime)
```

## Upcasting vs Downcasting
- **Upcasting**: Subclass → Superclass (implicit, safe)
- **Downcasting**: Superclass → Subclass (explicit, needs check)

## `instanceof` Pattern Matching (Java 16+)
```java
if (animal instanceof Dog dog) {
    dog.fetch();  // No explicit cast needed
}
```