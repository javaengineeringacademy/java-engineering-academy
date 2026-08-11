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

## Student → CTO Learning Path

This module is designed for four levels of engineering maturity. Each level builds on the previous.

### Level 1: Student (Topics 00–07)

**Goal:** Understand what exceptions are and how to use them.

| Topic | What You Learn |
|-------|---------------|
| 00-throwable | Root class, message/cause/stackTrace |
| 01-exception | Checked vs unchecked, catch-or-specify |
| 02-error | JVM failures, when not to catch |
| 03-runtime-exception | Programming bugs, common subtypes |
| 04-checked-exception | Recoverable conditions, API contracts |
| 05-unchecked-exception | When to use RuntimeException |
| 06-exception-hierarchy | Full class tree, JVM dispatch |
| 07-custom-exception | Creating your own exception types |

**After this level:** You can read stack traces, choose the right exception type, and create custom exceptions.

### Level 2: Engineer (Topics 08–12)

**Goal:** Write production-quality exception handling code.

| Topic | What You Learn |
|-------|---------------|
| 08-try-with-resources | AutoCloseable, automatic cleanup |
| 09-finally | Execution order, return override dangers |
| 10-multi-catch | Java 7 multi-catch syntax |
| 11-exception-chaining | Wrapping, cause preservation |
| 12-best-practices | 10 core rules, common mistakes |

**After this level:** You handle resources correctly, chain exceptions properly, and follow established coding guidelines.

### Level 3: Senior Engineer (Topics 13–15)

**Goal:** Debug complex failures and handle concurrency edge cases.

| Topic | What You Learn |
|-------|---------------|
| 13-stack-trace | Reading, filtering, performance cost |
| 14-suppressed-exceptions | TWR suppression, manual suppression |
| 15-thread-exceptions | Uncaught handlers, ExecutorService, CompletableFuture |

**After this level:** You can diagnose production issues from stack traces, understand JVM internals, and handle exceptions in multi-threaded code.

### Level 4: Tech Lead / CTO (Topic 16)

**Goal:** Design exception handling strategy for entire systems.

| Topic | What You Learn |
|-------|---------------|
| 16-best-practices | Global handlers, error responses, monitoring, circuit breakers, retry patterns |

**After this level:** You can architect error handling for microservices, set up monitoring and alerting, and make build-vs-buy decisions for resilience libraries.

## Module Structure

| Folder | Topic | Level | Internals/Memory |
|--------|-------|-------|-----------------|
| 00-throwable | Throwable root class | Student | Yes |
| 01-exception | Exception base class | Student | Yes |
| 02-error | Error base class | Student | Yes |
| 03-runtime-exception | RuntimeException | Student | No |
| 04-checked-exception | Checked exceptions | Student | No |
| 05-unchecked-exception | Unchecked exceptions | Student | No |
| 06-exception-hierarchy | Full class hierarchy | Student | No |
| 07-custom-exception | Creating your own | Student | No |
| 08-try-with-resources | AutoCloseable + TWR | Engineer | Yes |
| 09-finally | finally block | Engineer | No |
| 10-multi-catch | Multi-catch (Java 7) | Engineer | No |
| 11-exception-chaining | Chained exceptions | Engineer | No |
| 12-best-practices | Coding guidelines | Engineer | No |
| 13-stack-trace | Stack trace analysis | Senior | Yes |
| 14-suppressed-exceptions | Suppressed exceptions | Senior | Yes |
| 15-thread-exceptions | Thread exception handling | Senior | Yes |
| 16-best-practices | Production patterns | CTO | No |

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

## Prerequisites

- Java fundamentals (variables, methods, classes)
- Basic OOP (inheritance, interfaces)
- Familiarity with `java.lang` package

## Duration

| Level | Topics | Estimated Time |
|-------|--------|---------------|
| Student | 00–07 | 4–6 hours |
| Engineer | 08–12 | 3–4 hours |
| Senior | 13–15 | 2–3 hours |
| CTO | 16 | 1–2 hours |
| **Total** | **17 topics** | **10–15 hours** |
