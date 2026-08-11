# Exception Module Decision Guide

## Decision Tree

```
Need to handle an error condition?
├── Is it a programming bug? → RuntimeException (unchecked)
│   ├── Null argument? → NullPointerException
│   ├── Array bounds? → ArrayIndexOutOfBoundsException
│   ├── Invalid state? → IllegalStateException
│   └── Bad argument? → IllegalArgumentException
├── Is it recoverable? → Exception (checked)
│   ├── I/O failure? → IOException
│   ├── SQL error? → SQLException
│   ├── Interrupted? → InterruptedException
│   └── Custom domain? → YourCustomException
├── Is it a JVM failure? → Error (don't catch)
│   ├── Out of memory? → OutOfMemoryError
│   ├── Stack overflow? → StackOverflowError
│   └── Class not found? → NoClassDefFoundError
└── Need custom semantics? → Create your own exception
    ├── Recoverable? → extends Exception
    └── Programming bug? → extends RuntimeException
```

## Checked vs Unchecked Decision Matrix

| Factor | Checked | Unchecked |
|--------|---------|-----------|
| Caller can recover | Yes | No |
| Programming bug | No | Yes |
| Compiler enforces handling | Yes | No |
| API contract clarity | High | Low |
| Boilerplate | More | Less |
| When to use | External failures | Internal bugs |

## Exception Hierarchy Selection

| Scenario | Exception Type | Why |
|----------|---------------|-----|
| File not found | FileNotFoundException (checked) | Caller can retry or prompt |
| Null pointer | NullPointerException (unchecked) | Programming bug |
| Disk full | IOException (checked) | Caller can free space |
| Division by zero | ArithmeticException (unchecked) | Programming bug |
| Connection timeout | TimeoutException (checked) | Caller can retry |
| Invalid enum value | IllegalArgumentException (unchecked) | Programming bug |

## Resource Cleanup Decision

| Approach | When to Use |
|----------|-------------|
| try-with-resources | AutoCloseable resources (Java 7+) |
| finally block | Non-closeable cleanup, legacy code |
| Both together | Resource + additional cleanup |

## Production Recommendations

1. **Default to unchecked** for programming bugs
2. **Use checked** for recoverable external failures
3. **Always include message** in exception constructors
4. **Chain exceptions** when wrapping — preserve cause
5. **Never catch Exception or Throwable** broadly
6. **Log at the right level** — warn for recoverable, error for unexpected
7. **Use try-with-resources** for all Closeable/AutoCloseable
8. **Don't use exceptions for control flow** — performance killer
9. **Document checked exceptions** in Javadoc `@throws`
10. **Consider custom exceptions** for domain-specific errors

## Common Production Mistakes

| Mistake | Consequence | Fix |
|---------|-------------|-----|
| Catching generic Exception | Masks bugs | Catch specific types |
| Swallowing exceptions | Silent failures | Always log or rethrow |
| Throwing in finally | Overrides original | Avoid throwing in finally |
| Using exceptions for flow | 10-100x slower | Use conditionals |
| Not chaining causes | Lost context | Always pass cause |
| Catching Error | Hides JVM issues | Let Errors propagate |
