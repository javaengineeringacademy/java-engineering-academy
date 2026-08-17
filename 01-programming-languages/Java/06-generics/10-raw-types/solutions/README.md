# Raw Types Solutions

Complete implementations demonstrating raw type concepts.

## Solutions

1. **Raw vs Parameterized** - Raw types return Object, parameterized return specific type
2. **Unsafe Operations** - Raw types allow mixed types and unsafe casts
3. **Method Parameters** - Raw parameters lose type safety
4. **Raw vs Wildcard** - Wildcard is read-only but safe
5. **Necessary Raw Types** - Legacy code and reflection require raw types

## Usage

Run `RawTypesSolutions.java` to see all solutions in action.

## Key Takeaways

- Avoid raw types when possible
- Use `@SuppressWarnings` to acknowledge necessary raw types
- Wildcards provide type safety without specifying exact type
- Raw types are sometimes necessary for interop with non-generic code
