# Decision Guide: 25-immutable-objects

## When to Use
- Use 25-immutable-objects when you need clear code organization
- Use when building reusable components
- Use for complex business logic

## When NOT to Use
- Avoid for simple, one-off operations
- Don't use when performance is critical
- Skip if the overhead isn't justified

## Trade-offs
| Aspect | With 25-immutable-objects | Without 25-immutable-objects |
|--------|-------------|----------------|
| Readability | Better | Simpler |
| Performance | Overhead | Faster |
| Flexibility | More | Less |
| Testing | Easier | Harder |

## Expert Recommendation
Use 25-immutable-objects when building production systems. The initial overhead pays off in maintainability.
