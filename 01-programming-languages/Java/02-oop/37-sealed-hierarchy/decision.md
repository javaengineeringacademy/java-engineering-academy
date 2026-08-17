# Decision Guide: Sealed Hierarchies

## When to Use
- Closed type hierarchies where all subtypes are known at compile time
- Pattern matching with exhaustive switch expressions (Java 21+)
- Domain modeling with fixed variants (AST nodes, payment types, result types)
- API design where you want to control who can extend your types
- State machines with defined states

## When NOT to Use
- Open hierarchies where new subtypes may be added by third parties
- Plugin architectures requiring extensibility
- When you need to allow arbitrary implementations (use interfaces)
- Simple hierarchies where sealed adds unnecessary complexity

## Trade-offs

| Aspect | Sealed Class | Regular Interface | Abstract Class |
|--------|-------------|-------------------|----------------|
| Subtype control | Restricted | Open | Partially restricted |
| Pattern matching | Exhaustive (21+) | Not exhaustive | Not exhaustive |
| Implementation | Can have | No | Yes |
| Extensibility | Controlled | Open | Partially open |
| Module boundaries | Same module required | Any module | Any module |

## Expert Recommendation
Use sealed hierarchies when modeling closed domains with known variants. Combined with records and pattern matching (Java 21+), they enable exhaustive, type-safe data modeling. Reserve for situations where you genuinely need to restrict the type hierarchy — don't seal unnecessarily.
