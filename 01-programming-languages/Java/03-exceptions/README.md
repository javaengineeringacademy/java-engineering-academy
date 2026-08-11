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

### Level 1: Student (Topics 00–07)

**Goal:** Understand what exceptions are and how to use them.

| Topic | What You Learn |
|-------|---------------|
| 00-throwable | Root class, message/cause/stackTrace |
| 01-exception | Checked vs unchecked, catch-or-specify |
| 02-error | JVM failures, when not to catch |
| 03-exception-hierarchy | Full class tree, how JVM dispatches exceptions |
| 04-runtime-exception | RuntimeException class, inheritance, common subclasses |
| 05-checked-exception | Recoverable conditions, API contracts, compiler enforcement |
| 06-unchecked-exception | Language category, design philosophy, when to use |
| 07-custom-exception | Creating your own exception types |

**After this level:** You can read stack traces, choose the right exception type, and create custom exceptions.

### Level 2: Engineer (Topics 08–11)

**Goal:** Write production-quality exception handling code.

| Topic | What You Learn |
|-------|---------------|
| 08-try-with-resources | AutoCloseable, automatic cleanup |
| 09-finally | Execution order, return override dangers |
| 10-multi-catch | Java 7 multi-catch syntax |
| 11-exception-chaining | Wrapping, cause preservation |

**After this level:** You handle resources correctly, chain exceptions properly, and follow established coding guidelines.

### Level 3: Senior Engineer (Topics 12–14)

**Goal:** Debug complex failures and handle concurrency edge cases.

| Topic | What You Learn |
|-------|---------------|
| 12-stack-trace | Reading, filtering, performance cost |
| 13-suppressed-exceptions | TWR suppression, manual suppression |
| 14-thread-exceptions | Uncaught handlers, ExecutorService, CompletableFuture |

**After this level:** You can diagnose production issues from stack traces, understand JVM internals, and handle exceptions in multi-threaded code.

### Level 4: Principal Engineer / Java Architect (Topic 15)

**Goal:** Design exception handling strategy for entire systems.

| Topic | What You Learn |
|-------|---------------|
| 15-production-patterns | Global handlers, error responses, monitoring, circuit breakers, retry patterns |

**After this level:** You can architect error handling for microservices, set up monitoring and alerting, and make build-vs-buy decisions for resilience libraries.

## Module Structure

| Folder | Topic | Level | Internals/Memory |
|--------|-------|-------|-----------------|
| 00-throwable | Throwable root class | Student | Yes |
| 01-exception | Exception base class | Student | Yes |
| 02-error | Error base class | Student | Yes |
| 03-exception-hierarchy | Full class hierarchy | Student | No |
| 04-runtime-exception | RuntimeException class | Student | No |
| 05-checked-exception | Checked exceptions | Student | No |
| 06-unchecked-exception | Unchecked exception category | Student | No |
| 07-custom-exception | Creating your own | Student | No |
| 08-try-with-resources | AutoCloseable + TWR | Engineer | Yes |
| 09-finally | finally block | Engineer | No |
| 10-multi-catch | Multi-catch (Java 7) | Engineer | No |
| 11-exception-chaining | Chained exceptions | Engineer | No |
| 12-stack-trace | Stack trace analysis | Senior | Yes |
| 13-suppressed-exceptions | Suppressed exceptions | Senior | Yes |
| 14-thread-exceptions | Thread exception handling | Senior | Yes |
| 15-production-patterns | Production patterns | Principal/Architect | No |

## Implementation Depth Rule

Not every topic needs internals and memory sub-folders. Here's the rule:

| Topic Type | Includes | Example |
|------------|----------|---------|
| **Core Java class** (Throwable, Exception, Error) | README + Internals + Memory + Examples + Exercises + Solutions | 00-throwable |
| **JVM behavior** (TWR bytecode, stack trace capture, suppressed exceptions) | README + Internals + Memory + Examples + Exercises + Solutions | 08-try-with-resources |
| **Language keyword/concept** (finally, multi-catch, chaining) | README + Examples + Exercises + Solutions | 09-finally |
| **Category/philosophy** (checked, unchecked, best practices) | README + Examples + Exercises + Solutions | 05-checked-exception |

**Rule:** Internals and Memory are included when the topic involves JVM-level implementation details that affect performance or debugging. Language keywords and design categories don't have JVM internals to document.

## RuntimeException vs Unchecked Exception

These are two separate topics with distinct purposes:

| Aspect | 04-runtime-exception | 06-unchecked-exception |
|--------|---------------------|----------------------|
| **Focus** | The `RuntimeException` class itself | The unchecked exception category |
| **Content** | Inheritance, API, common subclasses (NPE, IAE,ISE) | Compiler behavior, design philosophy, when to use |
| **Perspective** | "What is this class?" | "When and why should I use this?" |
| **Overlap** | None — each covers different ground | None — each covers different ground |

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
| Engineer | 08–11 | 2–3 hours |
| Senior | 12–14 | 2–3 hours |
| Principal/Architect | 15 | 1–2 hours |
| **Total** | **16 topics** | **9–14 hours** |

## Summary

| Concept | Key Point |
|---------|-----------|
| Module Structure | 16 topics organized in 4 levels: Student, Engineer, Senior, Principal |
| Exception Hierarchy | Throwable → Exception (checked/unchecked) → Error; root of all Java exceptions |
| Learning Path | Progressive: fundamentals → production quality → concurrency → architecture |
| RuntimeException vs Unchecked | Two separate topics: class vs category; different perspectives |
| Implementation Depth | Core Java classes include internals; language concepts don't |
| When to Use What | Recoverable = checked; bugs = unchecked; JVM failures = Error |
| Total Duration | 9–14 hours covering all 16 topics |
| Design Rationale | Java chose both checked and unchecked exceptions; ongoing debate |
