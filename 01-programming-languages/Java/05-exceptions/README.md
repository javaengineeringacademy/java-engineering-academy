# Exception Handling in Java

## Scope

This module covers Java's exception handling mechanism — from the root `Throwable` class through custom exceptions, try-with-resources, and production-grade error handling patterns.

## Why It Exists

Before Java 1.0, error handling was primitive:
- C-style `setjmp`/`longjmp` for non-local jumps
- Error codes returned from functions
- No language-level guarantee of resource cleanup

Java introduced exceptions as first-class objects with compiler-enforced handling. Every checked exception must be caught or declared, creating a contract between caller and callee.

## Design Rationale

Java's exception hierarchy splits errors into three categories:

1. **Throwable** — the root; anything that can be thrown
2. **Exception** — recoverable conditions (checked or unchecked)
3. **Error** — unrecoverable JVM/system failures

**Trade-offs**: Checked exceptions enforce handling but add boilerplate. Unchecked exceptions are flexible but can slip through at runtime. The JDK designers chose both, leading to ongoing debate.

## Module Structure

| Folder | Topic | Internals/Memory |
|--------|-------|-----------------|
| 00-throwable | Throwable root class | Yes |
| 01-exception | Exception base class | Yes |
| 02-error | Error base class | Yes |
| 03-runtime-exception | RuntimeException | No |
| 04-checked-exception | Checked exceptions | No |
| 05-unchecked-exception | Unchecked exceptions | No |
| 06-exception-hierarchy | Full class hierarchy | No |
| 07-custom-exception | Creating your own | No |
| 08-try-with-resources | AutoCloseable + TWR | Yes |
| 09-finally | finally block | No |
| 10-multi-catch | Multi-catch (Java 7) | No |
| 11-exception-chaining | Chained exceptions | No |
| 12-best-practices | Coding guidelines | No |
| 13-stack-trace | Stack trace analysis | Yes |
| 14-suppressed-exceptions | Suppressed exceptions | Yes |
| 15-thread-exceptions | Thread exception handling | Yes |
| 16-best-practices | Production patterns | No |

## Quick Reference

```
Throwable
├── Exception
│   ├── RuntimeException (unchecked)
│   │   ├── NullPointerException
│   │   ├── ArrayIndexOutOfBoundsException
│   │   ├── IllegalArgumentException
│   │   ├── IllegalStateException
│   │   └── ...
│   ├── IOException (checked)
│   ├── SQLException (checked)
│   └── ...
└── Error
    ├── OutOfMemoryError
    ├── StackOverflowError
    ├── NoClassDefFoundError
    └── ...
```

## When to Use What

| Situation | Use |
|-----------|-----|
| Recoverable condition | Exception (checked) |
| Programming bug | RuntimeException (unchecked) |
| JVM/system failure | Error |
| Custom domain error | Extend Exception or RuntimeException |
| Resource cleanup | try-with-resources |
| Legacy cleanup | finally block |
