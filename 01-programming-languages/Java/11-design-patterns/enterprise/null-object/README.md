# Null Object Pattern

## Intent
Provide a default object that implements a no-op behavior, eliminating null checks and providing a consistent interface.

## Key Components
- **Interface**: Defines the contract
- **Real Implementation**: Does actual work
- **Null Object**: Does nothing (no-op) but satisfies the interface

## When to Use
- You have many null checks scattered in the code
- The default behavior is "do nothing"
- You want to avoid NullPointerException
- Polymorphic collections may contain "missing" items

## Benefits
- Eliminates null checks
- Simplifies client code
- Provides a default behavior
- Follows the Liskov Substitution Principle

## Example
```java
Animal dog = new Dog("Rex");
Animal none = NullAnimal.getInstance();

dog.speak(); // "Woof!"
none.speak(); // (no output)
none.isReal(); // false
```
