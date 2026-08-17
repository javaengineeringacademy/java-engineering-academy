# When to Use Java Fundamentals

## Decision Framework

Use Java fundamentals concepts when you need to:

| Scenario | Concept | Why |
|----------|---------|-----|
| Store a single value | Variables & Data Types | Choose the right type for memory efficiency and precision |
| Perform calculations | Operators | Arithmetic, logical, and bitwise operations |
| Make decisions in code | Control Flow (if/else, switch) | Branch logic based on conditions |
| Repeat tasks | Loops (for, while, do-while) | Iterate over data or repeat operations |
| Organize reusable logic | Methods | Encapsulate behavior, enable reuse |
| Store multiple values of same type | Arrays | Fixed-size collections with O(1) access |
| Handle text | Strings | Immutable character sequences |
| Structure a project | Packages & Project Structure | Organize classes logically |
| Manage dependencies | Maven | Build automation and dependency resolution |
| Understand language reserved words | Keywords | Know what you can and cannot name |

## When to Choose Primitives vs Wrapper Classes

| Use Primitives | Use Wrapper Classes |
|----------------|---------------------|
| Performance-critical code | Collections (Generics require objects) |
| Local variables in methods | Nullable fields |
| Mathematical computations | API boundaries |
| Loop counters | When null indicates "no value" |

## When to Use StringBuilder vs String Concatenation

| Use StringBuilder | Use String Concatenation |
|-------------------|--------------------------|
| Loops building strings | Simple one-time concatenation |
| Performance-sensitive paths | Readability matters more |
| Building large strings | Fewer than 5 concatenations |

## When to Use Arrays vs Collections

| Use Arrays | Use Collections |
|------------|-----------------|
| Fixed size known at compile time | Dynamic sizing needed |
| Performance-critical access | Rich API needed (add, remove, contains) |
| Multi-dimensional data | Generic type safety |
| Low memory overhead required | Frequent insertions/deletions |

## Common Production Decisions

### Financial Calculations
- **Never use** `double` or `float` for money
- **Always use** `BigDecimal` for precise decimal arithmetic
- Rounding mode must be explicitly specified

### String Comparison
- **Always use** `.equals()` for content comparison
- **Never use** `==` for String comparison
- Use `Objects.equals()` for null-safe comparison

### Null Handling
- Use `Optional` for method return values that may be absent
- Use `Objects.requireNonNull()` for precondition validation
- Prefer primitive types when null is not meaningful

### Error Handling
- Use checked exceptions for recoverable conditions
- Use unchecked exceptions for programming errors
- Always log exceptions with context
