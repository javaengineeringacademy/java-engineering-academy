# Bounded Type Parameters Exercises

Practice using bounded type parameters to restrict generic types.

## Exercises

1. **sum** - Sum numbers using upper-bounded wildcard
2. **Statistics** - Create a generic class with upper bound
3. **findMin** - Find minimum with multiple bounds (Comparable & Serializable)
4. **countGreaterThan** - Count elements greater than a value
5. **copy** - Copy between lists using lower and upper bounds

## Instructions

- Complete each exercise by implementing the TODO sections
- Run the main method to test your implementations
- Focus on understanding upper vs lower bounds

## Key Concepts

- Upper bounds: `T extends Number`
- Lower bounds: `T super Integer`
- Multiple bounds: `T extends A & B`
- Recursive bounds: `T extends Comparable<T>`
