# Decision Guide: 06-stream-operations

## When to Use
- Use 06-stream-operations for clean, composable code
- Use for data transformation pipelines
- Use when dealing with collections

## When NOT to Use
- Avoid for simple imperative logic
- Don't use when debugging is critical
- Skip if team isn't familiar

## Trade-offs
| Aspect | With 06-stream-operations | Without 06-stream-operations |
|--------|-------------|----------------|
| Readability | Better | Simpler |
| Performance | Slight overhead | Faster |
| Composability | High | Low |

## Expert Recommendation
Use functional programming for data pipelines. Use imperative for simple operations.
