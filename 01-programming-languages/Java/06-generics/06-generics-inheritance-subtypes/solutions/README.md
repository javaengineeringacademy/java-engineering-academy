# Generics, Inheritance, and Subtypes Solutions

Complete implementations demonstrating generic inheritance concepts.

## Solutions

1. **Generic Type Relationships** - Shows invariance of parameterized types
2. **List Relationships** - Wildcards enable read-only access
3. **Assignment Compatibility** - Demonstrates compile-time type safety
4. **printAnimals** - Uses `? extends Animal` for flexibility
5. **addDogs** - Uses `? super Dog` for safe insertion

## Usage

Run `InheritanceSubtypesSolutions.java` to see all solutions in action.

## Key Takeaways

- `Dog extends Animal` does NOT mean `Box<Dog> extends Box<Animal>`
- Use `? extends T` to read from generic types
- Use `? super T` to write to generic types
- Wildcards provide flexibility while maintaining type safety
