# Generics, Inheritance, and Subtypes Exercises

Understand how generics interact with Java's inheritance system.

## Exercises

1. **Generic Type Relationships** - Dog IS-A Animal but Box<Dog> IS-NOT-A Box<Animal>
2. **List Relationships** - Use wildcards to establish type relationships
3. **Assignment Compatibility** - Demonstrate invariance in generic types
4. **printAnimals** - Accept list of any Animal subtype
5. **addDogs** - Add animals to a list using lower bounds

## Instructions

- Complete each exercise by implementing the TODO sections
- Run the main method to see the output
- Observe compile-time errors when types don't match

## Key Concepts

- Generics are invariant: no inheritance between parameterized types
- Wildcards establish type relationships
- Upper bounds for reading, lower bounds for writing
- PECS principle applies to inheritance scenarios
