# Decision Guide: 02-file-operations

## When to Use
- Use 02-file-operations for file I/O operations
- Use when processing large files
- Use for network communication

## When NOT to Use
- Avoid for simple text processing
- Don't use when memory is limited
- Skip if NIO isn't needed

## Trade-offs
| Aspect | With 02-file-operations | Without 02-file-operations |
|--------|-------------|----------------|
| Performance | Optimized | Simpler |
| Complexity | Higher | Lower |
| Blocking | Depends | Blocking |

## Expert Recommendation
Use traditional I/O for simple tasks. Use NIO for large files or non-blocking needs.
