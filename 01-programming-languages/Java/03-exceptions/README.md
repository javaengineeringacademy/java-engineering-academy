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

### Level 1: Student (Topics 00–08)

**Goal:** Understand what exceptions are and how to use them.

| Topic | What You Learn |
|-------|---------------|
| 00-throwable | Root class, message/cause/stackTrace |
| 01-exception | Checked vs unchecked, catch-or-specify |
| 02-error | JVM failures, when not to catch |
| 03-exception-hierarchy | Full class tree, how JVM dispatches exceptions |
| 04-finally | Execution order, return override dangers |
| 05-multi-catch | Java 7 multi-catch syntax |
| 06-try-with-resources-internals | TWR bytecode internals |
| 07-try-with-resources | AutoCloseable, automatic cleanup |
| 08-runtime-exception | RuntimeException class, inheritance, common subclasses |

**After this level:** You can read stack traces, choose the right exception type, and create custom exceptions.

### Level 2: Engineer (Topics 09–11)

**Goal:** Write production-quality exception handling code.

| Topic | What You Learn |
|-------|---------------|
| 09-checked-exceptions | Recoverable conditions, API contracts, compiler enforcement |
| 10-unchecked-exceptions | Language category, design philosophy, when to use |
| 11-custom-exceptions | Creating your own exception types |

**After this level:** You handle resources correctly, chain exceptions properly, and follow established coding guidelines.

### Level 3: Senior Engineer (Topics 12–14)

**Goal:** Debug complex failures and handle concurrency edge cases.

| Topic | What You Learn |
|-------|---------------|
| 12-exception-hierarchy | Full class hierarchy, JVM dispatch |
| 13-exception-chaining | Wrapping, cause preservation |
| 14-stack-trace | Reading, filtering, performance cost |

**After this level:** You can diagnose production issues from stack traces, understand JVM internals, and handle exceptions in multi-threaded code.

### Level 4: Staff Engineer (Topics 15–16)

**Goal:** Handle advanced exception scenarios.

| Topic | What You Learn |
|-------|---------------|
| 15-suppressed-exceptions | TWR suppression, manual suppression |
| 16-thread-exceptions | Uncaught handlers, ExecutorService, CompletableFuture |

**After this level:** You understand suppressed exception mechanics and can handle exceptions in concurrent and asynchronous code.

### Level 5: Principal Engineer / Java Architect (Topic 17)

**Goal:** Design exception handling strategy for entire systems.

| Topic | What You Learn |
|-------|---------------|
| 17-production-patterns | Global handlers, error responses, monitoring, circuit breakers, retry patterns |

**After this level:** You can architect error handling for microservices, set up monitoring and alerting, and make build-vs-buy decisions for resilience libraries.

## Module Structure

| Folder | Topic | Level | Internals/Memory |
|--------|-------|-------|-----------------|
| 00-throwable | Throwable root class | Student | Yes |
| 01-exception | Exception base class | Student | Yes |
| 02-error | Error base class | Student | Yes |
| 03-exception-hierarchy | Full class hierarchy | Student | No |
| 04-finally | finally block | Student | No |
| 05-multi-catch | Multi-catch (Java 7) | Student | No |
| 06-try-with-resources-internals | TWR bytecode internals | Student | Yes |
| 07-try-with-resources | AutoCloseable + TWR | Student | No |
| 08-runtime-exception | RuntimeException class | Student | No |
| 09-checked-exceptions | Checked exceptions | Engineer | No |
| 10-unchecked-exceptions | Unchecked exception category | Engineer | No |
| 11-custom-exceptions | Creating your own | Engineer | No |
| 12-exception-hierarchy | Full class hierarchy | Senior | No |
| 13-exception-chaining | Chained exceptions | Senior | No |
| 14-stack-trace | Stack trace analysis | Senior | Yes |
| 15-suppressed-exceptions | Suppressed exceptions | Staff | Yes |
| 16-thread-exceptions | Thread exception handling | Staff | Yes |
| 17-production-patterns | Production patterns | Principal/Architect | No |

## Implementation Depth Rule

Not every topic needs internals and memory sub-folders. Here's the rule:

| Topic Type | Includes | Example |
|------------|----------|---------|
| **Core Java class** (Throwable, Exception, Error) | README + Internals + Memory + Examples + Practices + Solutions | 00-throwable |
| **JVM behavior** (TWR bytecode, stack trace capture, suppressed exceptions) | README + Internals + Memory + Examples + Practices + Solutions | 07-try-with-resources |
| **Language keyword/concept** (throw, throws, finally, chaining) | README + Internals + Memory + Examples + Practices + Solutions | 05-throw |
| **Category/philosophy** (checked, unchecked, custom, hierarchy) | README + Internals + Memory + Examples + Practices + Solutions | 09-checked-exceptions |

**Every topic has:** `00-examples/`, `01-practices/`, `02-solutions/`, `03-internals/`, `04-memory/`

## RuntimeException vs Unchecked Exception

These are two separate topics with distinct purposes:

| Aspect | 08-runtime-exception | 10-unchecked-exception |
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

## Practice Exercises

10 standalone practices covering the entire exception hierarchy, with separate solutions:

| # | File | Concept |
|---|------|---------|
| 01 | BasicTryCatch | try-catch with ArithmeticException |
| 02 | MultipleCatch | Multiple catch blocks, different handling |
| 03 | MultiCatchWithThrow | Multi-catch (\|) + throw |
| 04 | FinallyCleanup | Finally for guaranteed cleanup |
| 05 | TWRResources | Try-with-resources + IOException |
| 06 | ExceptionChaining | Chaining with cause |
| 07 | CustomCheckedException | Custom checked exception with deficit field |
| 08 | CustomUncheckedException | Custom unchecked exception with fieldName |
| 09 | RuntimeExceptionRecovery | Fallback on RuntimeException |
| 10 | ThrowableHierarchy | Classifying Throwable subtypes |

**Folders:**
- `practices/` — 10 standalone practice files (fill in the TODO)
- `solutions/` — 10 matching solution files

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
| Student | 00–08 | 5–7 hours |
| Engineer | 09–11 | 2–3 hours |
| Senior | 12–14 | 2–3 hours |
| Staff | 15–16 | 2–3 hours |
| Principal/Architect | 17 | 1–2 hours |
| **Total** | **18 topics** | **12–18 hours** |

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | Exception handling introduced: `try-catch-finally`, checked exceptions |
| JDK 1.2 | Exception chaining added (`initCause()`, `getCause()`) |
| JDK 5 | Enhanced for-loop and autoboxing reduced common exception causes |
| JDK 7 | Try-with-resources, multi-catch, suppressed exceptions (JSR 334) |
| JDK 8 | Lambda expressions affected checked exception handling in functional interfaces |
| JDK 9 | Effectively final variables in try-with-resources |
| JDK 14 | Switch expressions previewed with exception-like exhaustiveness |
| JDK 17 | Sealed classes enabled more precise exception hierarchies |
| JDK 21 | Pattern matching for switch improved exception type dispatch |

## Summary

| Concept | Key Point |
|---------|-----------|
| Module Structure | 19 topics organized in 5 levels: Student, Engineer, Senior, Staff, Principal |
| Folder Structure | Every topic: 00-examples, 01-practices, 02-solutions, 03-internals, 04-memory |
| Top-Level Practice | 10 standalone practices + 10 solutions in practices/ and solutions/ |
| Exception Hierarchy | Throwable → Exception (checked/unchecked) → Error; root of all Java exceptions |
| Learning Path | Progressive: fundamentals → production quality → concurrency → architecture |
| RuntimeException vs Unchecked | Two separate topics: class vs category; different perspectives |
| Implementation Depth | Every topic includes internals and memory sub-folders |
| When to Use What | Recoverable = checked; bugs = unchecked; JVM failures = Error |
| Total Duration | 12–18 hours covering all 19 topics |
| Design Rationale | Java chose both checked and unchecked exceptions; ongoing debate |
