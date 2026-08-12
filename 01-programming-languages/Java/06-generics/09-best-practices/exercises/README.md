# Best Practices Exercises

Apply best practices when using Java generics.

## Exercises

1. **Bounded Types** - Use proper bounds instead of wildcards when needed
2. **List vs Array** - Prefer List over array for type safety
3. **Safe Varargs** - Use @SafeVarargs for varargs with generics
4. **Documentation** - Document generic type parameters properly
5. **Type Witnesses** - Know when to use explicit type arguments

## Instructions

- Complete each exercise by implementing the TODO sections
- Run the main method to see the output
- Compare bad vs good approaches

## Key Concepts

- Use bounded type parameters for read-write scenarios
- Use wildcards for read-only scenarios
- Prefer List<T> over T[] for type safety
- Document generic types in Javadoc
- Use type witnesses only when inference fails
