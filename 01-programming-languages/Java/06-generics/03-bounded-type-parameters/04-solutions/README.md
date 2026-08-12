# Bounded Type Parameters Solutions

Complete implementations for all Bounded Type Parameters exercises.

## Solutions

1. **sum** - Uses `? extends Number` to accept any Number subclass
2. **Statistics** - Generic class with `T extends Number` bound
3. **findMin** - Multiple bounds: `Comparable` and `Serializable`
4. **countGreaterThan** - Recursive bound with `Comparable<T>`
5. **copy** - Demonstrates PECS principle (Producer Extends, Consumer Super)

## Usage

Run `BoundedTypeSolutions.java` to see all solutions in action.

## Key Takeaways

- Upper bounds allow reading but not writing (except null)
- Lower bounds allow writing but limit reading
- Multiple bounds use `&` operator
- First bound must be a class, interfaces follow
