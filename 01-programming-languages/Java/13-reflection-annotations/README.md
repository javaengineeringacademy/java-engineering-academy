# Java Reflection & Annotations — Complete Mini-Course

> **Module 13** · 5,000+ lines · 9 topics · Exercises · Projects · Interview Prep

---

## Why This Module Exists

Reflection and annotations are the backbone of every major Java framework you've ever used. Spring's `@Autowired`, JPA's `@Entity`, JUnit's `@Test`, Lombok's `@Data` — none of them work without reflection. Understanding reflection means understanding how Java frameworks *actually* work under the hood, not just how to use their magic annotations.

This module takes you from "what is reflection?" to "I can build my own annotation processor and AOP proxy." Every concept is taught with a **WHY-first** approach — you'll understand the problem before you see the solution.

---

## Learning Path

```
01-introduction          → Why reflection exists, when to use it
02-class-introspection   → Getting Class objects, metadata queries
03-field-access          → Reading/writing private fields at runtime
04-method-invocation     → Calling methods dynamically
05-constructor-access    → Creating objects without knowing the type at compile time
06-dynamic-proxy         → AOP, logging, transaction management patterns
07-custom-annotations    → Creating @interface, retention, targets
08-annotation-processing → Compile-time code generation
09-real-world-use-cases  → Spring, JPA, JUnit, Jackson, Lombok
```

**Recommended order:** Follow the numbered topics sequentially. Each builds on the previous.

---

## Module Structure

```
13-reflection-annotations/
├── README.md                         ← You are here
├── roadmap.md                        ← Visual learning path
├── 01-introduction/
│   └── README.md                     ← What is reflection, why it exists
├── 02-class-introspection/
│   └── README.md                     ← getClass(), .class, Class.forName()
├── 03-field-access/
│   └── README.md                     ← getDeclaredFields(), set(), get()
├── 04-method-invocation/
│   └── README.md                     ← getDeclaredMethods(), invoke()
├── 05-constructor-access/
│   └── README.md                     ← getDeclaredConstructors(), newInstance()
├── 06-dynamic-proxy/
│   └── README.md                     ← Proxy.newProxyInstance(), InvocationHandler
├── 07-custom-annotations/
│   └── README.md                     ← @interface, retention, target
├── 08-annotation-processing/
│   └── README.md                     ← AbstractProcessor, code generation
├── 09-real-world-use-cases/
│   └── README.md                     ← Spring, JPA, JUnit, serialization
├── examples/                         ← Standalone runnable examples
├── exercises/                        ← 5 exercises per topic (45 total)
├── solutions/                        ← All exercise solutions
├── quizzes/                          ← Topic quizzes
├── interview/                        ← Common interview questions
├── projects/                         ← Mini-projects to apply knowledge
├── references/                       ← Cheat sheets, further reading
└── src/                              ← Existing Maven source code
```

---

## Prerequisites

| Topic | Required Knowledge |
|-------|-------------------|
| 01-05 | Java basics, OOP, access modifiers |
| 06 | Interfaces, anonymous classes |
| 07-08 | Java basics (annotations are new syntax) |
| 09 | Familiarity with any Java framework |

---

## Quick Start

```java
// Get a Class object — the gateway to reflection
Class<?> clazz = String.class;
Class<?> clazz2 = "hello".getClass();
Class<?> clazz3 = Class.forName("java.lang.String");

// Access all declared fields (including private)
Field[] fields = clazz.getDeclaredFields();

// Access all declared methods (including private)
Method[] methods = clazz.getDeclaredMethods();

// Create an instance without knowing the type at compile time
Object obj = clazz.getDeclaredConstructor().newInstance();
```

---

## Key Takeaways

1. **Reflection is slow** — avoid it in hot paths; cache `Class`/`Method`/`Field` objects
2. **Reflection breaks encapsulation** — use `setAccessible(true)` carefully
3. **Annotations are metadata** — they do nothing by themselves; processors read them
4. **Compile-time processing > runtime processing** when possible (performance + error detection)
5. **Every major framework uses reflection** — understanding it is non-optional for Java professionals

---

## Common Pitfalls

| Pitfall | Why It Happens | Solution |
|---------|---------------|----------|
| `ClassNotFoundException` | Wrong fully-qualified name | Verify class name including package |
| `NoSuchMethodException` | Wrong parameter types | Use `getDeclaredMethod(name, ParamType.class)` |
| `IllegalAccessException` | Forgot `setAccessible(true)` | Call `setAccessible(true)` before access |
| `InvocationTargetException` | Target method threw exception | Unwrap with `getTargetException()` |
| `InaccessibleObjectException` | Java 9+ module system blocks access | Add `--add-opens` JVM flag |

---

## When to Use Reflection

| Use Reflection | Avoid Reflection |
|---------------|-----------------|
| Framework internals | Application business logic |
| Plugin architectures | Performance-critical paths |
| Serialization/deserialization | Static, known types |
| Testing utilities | Compile-time verifiable code |
| IDE tooling | Simple delegation patterns |

---

## Estimated Time

| Section | Hours |
|---------|-------|
| 01-03: Fundamentals | 4-6 |
| 04-06: Advanced | 6-8 |
| 07-08: Annotations | 4-6 |
| 09: Real-world | 3-4 |
| Exercises & Projects | 8-12 |
| **Total** | **25-36** |

---

*Last updated: August 2026*
