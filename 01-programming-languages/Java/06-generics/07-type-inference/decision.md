# Decision Guide: Type Inference

## Decision Tree

```
Does the compiler need to infer a type?
├── Is there a target type (assignment, parameter)?
│   ├── Yes → Java infers from target type
│   ├── No → Can you add a type witness?
│   │   ├── Yes → Use <Type> before method name
│   │   └── No → Compiler may fail
│   └── Is the inference ambiguous?
│       ├── Yes → Add explicit type witness
│       └── No → Let inference work
├── Using diamond operator <>?
│   ├── Can the type be inferred? → Use <>
│   └── Cannot be inferred → Specify type explicitly
└── Using var?
    ├── Inferred type is clear from context → Use var
    └── Type is ambiguous → Use explicit type
```

## Type Inference Mechanisms

| Mechanism | Syntax | When Used |
|---|---|---|
| Target-type inference | Assignment or parameter context | Method returns, assignments |
| Diamond operator | `new ArrayList<>()` | Constructor calls |
| Type witness | `<String>` before method | Ambiguous or contextless calls |
| `var` keyword | `var x = ...` | Local variable declarations |
| Lambda inference | `(x) -> x.length()` | Functional interfaces |

## When Inference Works Well

- Method return type matches assignment target
- Constructor calls with diamond operator
- Lambda expressions in functional interface contexts
- Method chaining with consistent types
- `var` with clear initialization

## When Inference Fails

- Discarded return value (no target type)
- Multiple applicable overloads with different inferred types
- Circular inference dependencies
- Ambiguous lambda parameter types
- Generic method called without assignment

## Decision Rules

1. **Prefer target-type inference** — let the compiler figure it out
2. **Use `<>` over explicit type in constructors** — shorter, same safety
3. **Use type witness only when inference fails** — don't over-specify
4. **Use `var` when type is obvious from context** — avoid when unclear
5. **Don't use `var` with `null`** — compiler cannot infer the type
6. **For complex inference, simplify the code** — extract variables

## Engineering Trade-offs

| Approach | Readability | Safety | Verbosity |
|---|---|---|---|
| Full explicit | High | High | High |
| Target-type inference | High | High | Low |
| Diamond operator | High | High | Low |
| Type witness | Moderate | High | Moderate |
| `var` | Variable | High | Very Low |

## Common Code Review Comments

- "Type inference fails here — add an explicit type witness"
- "Use `<>` instead of repeating the type in the constructor"
- "`var` makes this unclear — use the explicit type"
- "The return type is ambiguous without the target type"
- "Consider extracting to a typed variable for clarity"

## Production Patterns

```java
// Pattern: Diamond operator with inference
List<String> names = new ArrayList<>();
Map<String, List<Integer>> cache = new HashMap<>();

// Pattern: Type witness for ambiguity resolution
var result = Collections.<String>emptyList();

// Pattern: var for verbose generic types
var entries = map.entrySet(); // Set<Map.Entry<String, Integer>>

// Pattern: Target-type inference in method chains
List<String> filtered = list.stream()
    .filter(s -> s.length() > 3)
    .collect(Collectors.toList());
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| `var list = new ArrayList<>()` (raw inferred) | Use `var list = new ArrayList<String>()` |
| Overusing `var` making types opaque | Use explicit type when unclear |
| Type witness when inference works | Remove the witness |
| `var x = null` | Use explicit `Type x = null` |
