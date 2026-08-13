# Erasure of Generic Types Exercises

Understand how type erasure affects generic types in Java.

## Exercises

1. **Type Parameter Erasure** - T is erased to Object or bound
2. **Field Type Erasure** - Generic fields become Object
3. **Method Type Erasure** - Generic methods use Object parameters
4. **Bridge Methods** - Synthetic methods for polymorphism
5. **Runtime Type Checking** - instanceof with erased types

## Instructions

- Complete each exercise by implementing the TODO sections
- Run the main method to see the output
- Use reflection to inspect erased types

## Key Concepts

- Type parameters erased to Object or first bound
- Bridge methods maintain polymorphism
- No generic type info at runtime (except via reflection)
- Same class for all parameterizations
