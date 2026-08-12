# Restrictions on Generics Exercises

Understand what you cannot do with Java generics.

## Exercises

1. **Primitive Types** - Cannot use int, double, etc. as type arguments
2. **Cannot Create Instances** - Cannot use `new T()`
3. **Cannot Create Generic Arrays** - Cannot create `T[]` or `Box<T>[]`
4. **Cannot Use instanceof** - Cannot check with parameterized types
5. **Static Context** - Static members cannot use type parameters

## Instructions

- Complete each exercise by implementing the TODO sections
- Run the main method to see the output
- Understand the workarounds for each restriction

## Key Concepts

- Type erasure causes these restrictions
- Use wrapper classes for primitives
- Use Class<T> for instance creation
- Use List instead of arrays
- Use raw type checks with instanceof
