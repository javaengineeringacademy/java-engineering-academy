# instanceof Pattern Internals

## Bytecode Generation

Pattern matching for instanceof generates optimized bytecode:

### Type Check
The compiler generates a single type check instruction, not separate instanceof and cast.

### Pattern Variable Binding
The variable is bound directly after the type check, no additional cast instruction.

### Guard Evaluation
Boolean guards are evaluated after the type check, before variable binding.

## Optimization

The compiler can optimize:
1. **Single type check** - No redundant instanceof
2. **Direct variable binding** - No separate cast
3. **Short-circuit evaluation** - Guards evaluated in order
4. **Scope analysis** - Variables only in accessible regions

## Scope Rules

Pattern variables follow definite assignment rules:
- **Then branch:** Variable is in scope
- **Else branch:** Variable is NOT in scope
- **After if:** Variable is in scope only if condition is true

```java
// Valid
if (obj instanceof String s) {
    // s is in scope
}

// Invalid
if (!(obj instanceof String s)) {
    // s is NOT in scope
}

// Valid
if (obj instanceof String s) {
    // s is in scope
} else {
    // s is NOT in scope
}
```

## Performance

Pattern matching instanceof has the same performance as:
- Plain instanceof check
- instanceof + explicit cast
- No additional overhead
