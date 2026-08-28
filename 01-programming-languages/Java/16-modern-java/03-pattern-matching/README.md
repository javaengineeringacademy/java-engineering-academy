# Pattern Matching (Java 21)

Pattern Matching for switch allows you to test and destructure data based on patterns, eliminating verbose instanceof checks and explicit casts.

## Key Features

- **Type patterns** - Match by type and bind a variable
- **Guarded patterns** - Add conditions with `&&`
- **Null handling** - `case null` is supported
- **Exhaustiveness** - Compiler verifies all cases are handled

## Syntax

```java
// Basic pattern matching
Object obj = "Hello";
String result = switch (obj) {
    case Integer i -> "Integer: " + i;
    case String s -> "String: " + s;
    case null -> "null";
    default -> "Unknown";
};

// Guarded pattern
case String s && s.length() > 5 -> "Long string: " + s;

// Multiple patterns
case 1, 2, 3 -> "Small number";
```

## Pattern Types

| Pattern | Description |
|---------|-------------|
| Type pattern | `case Type t` - matches type and binds variable |
| Guarded pattern | `case Type t && condition` - type with condition |
| Null pattern | `case null` - matches null |
| Default | `default` - matches anything not matched |

## Rules

1. Pattern variables are final in scope
2. Patterns must be exhaustive (cover all cases)
3. Null must be handled explicitly or via default
4. Pattern variables can be used in the case body

## When to Use

- Replacing instanceof chains
- Type-specific processing without casting
- Complex conditional logic based on types
- Working with sealed hierarchies
