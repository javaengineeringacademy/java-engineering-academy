# Reflection & Annotations

> Comprehensive guide to Java Reflection API and custom annotations — introspection, dynamic proxy, annotation processing, and real-world use cases.

## Why Reflection & Annotations?

Reflection and annotations are essential for:
- **Framework development** — Spring, Hibernate, Jackson use reflection
- **Code generation** — Lombok, MapStruct use annotation processing
- **Testing** — JUnit uses reflection to discover and run tests
- **Dynamic behavior** — Create proxies, interceptors, and AOP
- **Configuration** — Declarative configuration via annotations

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | [Introduction](01-introduction/) | Reflection overview and use cases |
| 02 | [Class Introspection](02-class-introspection/) | Getting Class objects, metadata |
| 03 | [Field Access](03-field-access/) | Reading and writing fields |
| 04 | [Method Invocation](04-method-invocation/) | Calling methods dynamically |
| 05 | [Constructor Access](05-constructor-access/) | Creating objects dynamically |
| 06 | [Dynamic Proxy](06-dynamic-proxy/) | Proxy pattern with java.lang.reflect |
| 07 | [Custom Annotations](07-custom-annotations/) | Creating annotation types |
| 08 | [Annotation Processing](08-annotation-processing/) | Compile-time and runtime processing |
| 09 | [Real-World Use Cases](09-real-world-use-cases/) | Frameworks and libraries |

## Reflection Overview

```
┌─────────────────────────────────────┐
│       Java Reflection API           │
├─────────────────────────────────────┤
│  Class Objects                      │
│  ┌─────────────────────────────┐    │
│  │ getClass(), .class,         │    │
│  │ Class.forName()             │    │
│  └─────────────────────────────┘    │
│                                     │
│  Members                            │
│  ┌─────────────────────────────┐    │
│  │ Fields, Methods,            │    │
│  │ Constructors, Annotations   │    │
│  └─────────────────────────────┘    │
│                                     │
│  Dynamic Operations                 │
│  ┌─────────────────────────────┐    │
│  │ invoke(), get/set(),        │    │
│  │ newInstance(), Proxy        │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

## Annotation Processing

```
┌─────────────────────────────────────┐
│    Annotation Processing Flow       │
├─────────────────────────────────────┤
│  Source Code                        │
│  ┌─────────────────────────────┐    │
│  │ @Override                    │    │
│  │ @Test                        │    │
│  │ @Custom Annotation           │    │
│  └─────────────────────────────┘    │
│           ↓                         │
│  Processing                         │
│  ┌─────────────────────────────┐    │
│  │ Compile-time: APT           │    │
│  │ Runtime: Reflection          │    │
│  └─────────────────────────────┘    │
│           ↓                         │
│  Output                             │
│  ┌─────────────────────────────┐    │
│  │ Generated code,             │    │
│  │ Validation, Logging         │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

## Resources

- [Oracle Reflection Tutorial](https://docs.oracle.com/javase/tutorial/reflect/)
- [Java Annotations Tutorial](https://www.baeldung.com/java-annotations)
- [Dynamic Proxy](https://www.baeldung.com/java-dynamic-proxy)
- [Annotation Processing](https://www.baeldung.com/java-annotation-processing-builder)

