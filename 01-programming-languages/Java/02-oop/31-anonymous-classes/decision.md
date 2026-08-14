# Decision Guide: 31-anonymous-classes

## When to Use
- Use 31-anonymous-classes when you need clear code organization
- Use when building reusable components
- Use for complex business logic

## When NOT to Use
- Avoid for simple, one-off operations
- Don't use when performance is critical
- Skip if the overhead isn't justified

## Trade-offs
| Aspect | With 31-anonymous-classes | Without 31-anonymous-classes |
|--------|-------------|----------------|
| Readability | Better | Simpler |
| Performance | Overhead | Faster |
| Flexibility | More | Less |
| Testing | Easier | Harder |

## Expert Recommendation
Use 31-anonymous-classes when building production systems. The initial overhead pays off in maintainability.
