# Custom Exceptions: Decision Guide

## Checked vs Unchecked

```
Is the error recoverable by the caller?
├── Yes → Checked (extends Exception)
│   └── Can caller reasonably handle it?
│       ├── Yes → Checked
│       └── No  → Unchecked
└── No  → Unchecked (extends RuntimeException)
```

## When to Create a Custom Exception

| Scenario | Custom Exception? | Why |
|----------|-------------------|-----|
| User not found | Yes | Domain-specific, callers need typed handling |
| Invalid input | No | Use `IllegalArgumentException` |
| Payment declined | Yes | Carries payment-specific data |
| Null argument | No | Use `NullPointerException` |
| Network timeout | No | Use `SocketTimeoutException` |
| Business rule violation | Yes | Expresses domain constraint |
| API contract error | Yes | Documents public API errors |

## Naming Checklist

- [ ] Suffix with `Exception`
- [ ] Descriptive, not generic
- [ ] Not `Error` suffix (reserved for JVM issues)

## Constructors Checklist

- [ ] `MyException(String message)`
- [ ] `MyException(String message, Throwable cause)`
- [ ] `MyException(Throwable cause)`
- [ ] Domain-specific fields constructor (if needed)
- [ ] `serialVersionUID` (for checked exceptions)

## Hierarchy Depth

| Project Size | Recommended Depth |
|--------------|-------------------|
| Small (< 5 exceptions) | Flat |
| Medium (5-15 exceptions) | 2 levels |
| Large (> 15 exceptions) | 3 levels max |

## Red Flags

1. Creating an exception for every tiny error variation
2. Catching custom exceptions too broadly
3. Using exceptions for normal flow control
4. Losing the original cause when wrapping
5. Missing `serialVersionUID` on checked exceptions
