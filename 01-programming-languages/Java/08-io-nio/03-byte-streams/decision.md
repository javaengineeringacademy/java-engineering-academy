# Decision Guide: 03-byte-streams

## When to Use
- Use 03-byte-streams for file I/O operations
- Use when processing large files
- Use for network communication

## When NOT to Use
- Avoid for simple text processing
- Don't use when memory is limited
- Skip if NIO isn't needed

## Trade-offs
| Aspect | With 03-byte-streams | Without 03-byte-streams |
|--------|-------------|----------------|
| Performance | Optimized | Simpler |
| Complexity | Higher | Lower |
| Blocking | Depends | Blocking |

## Expert Recommendation
Use traditional I/O for simple tasks. Use NIO for large files or non-blocking needs.
