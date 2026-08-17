# Decision Guide: Equals & HashCode

## When to Use
- Use when objects will be stored in hash-based collections (`HashMap`, `HashSet`, `LinkedHashMap`)
- Use when object comparison needs to be based on logical equality (field values) not reference identity
- Use when implementing domain objects that represent business entities

## When NOT to Use
- Skip for valueless objects where reference identity is sufficient
- Skip for Java records (14+) — they auto-generate equals/hashCode
- Don't override equals/hashCode on mutable objects used as map keys

## Trade-offs
| Aspect | With Override | Without Override |
|--------|-------------|----------------|
| HashMap behavior | Correct lookup | Broken — equal objects not found |
| HashSet behavior | Deduplication works | Duplicates allowed |
| Performance | Slight overhead per comparison | Default identity comparison |
| Maintenance | Must update when fields change | No maintenance needed |

## Expert Recommendation
Always override both equals() and hashCode() together. Use `Objects.hash()` for hashCode and `Objects.equals()` for null-safe comparisons. Consider using records (Java 14+) for data classes to avoid manual implementation.
