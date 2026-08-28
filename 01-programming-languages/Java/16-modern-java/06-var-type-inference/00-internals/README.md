# var Type Inference Internals

## Compilation Process

The compiler infers the type of a var variable from its initializer:

### Type Resolution
```java
var x = 10;          // Compiler infers int
var name = "Hello";  // Compiler infers String
var list = List.of(1, 2, 3);  // Compiler infers List<Integer>
```

### Effectively Final
Variables declared with var are effectively final - they cannot be reassigned.

### Type Widening
The compiler uses the most specific type that matches the initializer.

## Restrictions

1. **Must have initializer** - `var x;` is invalid
2. **Cannot be null** - `var x = null;` is invalid without explicit type
3. **Local variables only** - Cannot be used for fields or parameters
4. **No multiple declarations** - `var x, y = 10;` is invalid

## Performance

var has no performance impact - it's purely a compile-time feature:
- Same bytecode as explicit type declaration
- No runtime overhead
- Same GC behavior

## IDE Support

Modern IDEs support var:
- Type hint on hover
- Type inspection
- Refactoring support
- Code completion
