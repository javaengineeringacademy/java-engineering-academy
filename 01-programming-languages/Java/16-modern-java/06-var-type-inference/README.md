# var Type Inference (Java 10)

The `var` keyword allows you to declare local variables without explicitly specifying their type. The compiler infers the type from the initializer.

## Key Features

- **Type inference** - Compiler determines the type
- **Concise code** - Reduces verbosity
- **Readability** - More readable for complex types
- **Local variables only** - Cannot be used for fields or return types

## Syntax

```java
// Before
List<String> names = new ArrayList<>();

// After
var names = new ArrayList<String>();

// Type is inferred as ArrayList<String>
```

## Rules

1. Must have an initializer
2. Cannot be `null` without explicit type
3. Cannot be used for:
   - Method parameters (until Java 11)
   - Return types
   - Fields
   - Lambda parameters (until Java 11)

## Examples

```java
// Simple types
var x = 10;          // int
var name = "Hello";  // String
var list = List.of(1, 2, 3);  // List<Integer>

// Complex types
var map = new HashMap<String, List<Integer>>();
var stream = list.stream().filter(i -> i > 2);

// Diamond operator
var set = new HashSet<String>();
```

## When to Use

- Complex generic types
- Obvious types from context
- Lambda expressions
- For-each loops

## When NOT to Use

- When type is not obvious
- Public API signatures
- When clarity is more important than brevity
