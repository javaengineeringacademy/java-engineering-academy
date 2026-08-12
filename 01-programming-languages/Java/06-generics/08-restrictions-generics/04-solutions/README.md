# Restrictions on Generics Solutions

Complete implementations demonstrating generic restrictions and workarounds.

## Solutions

1. **Primitive Types** - Use wrapper classes (Integer, Double, etc.)
2. **Cannot Create Instances** - Use Class<T> and reflection
3. **Cannot Create Generic Arrays** - Use List<T> or raw arrays
4. **Cannot Use instanceof** - Use raw type checks
5. **Static Context** - Use Object for static members

## Usage

Run `RestrictionsSolutions.java` to see all solutions in action.

## Key Takeaways

- All restrictions stem from type erasure
- Workarounds exist for most restrictions
- Some restrictions cannot be bypassed safely
- Understand the limitations to write better generic code
