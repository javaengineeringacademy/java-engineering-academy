# Java

Java is a class-based, object-oriented programming language designed for portability and reliability.

---

## Knowledge Atoms

Shared concepts explained ONCE, linked from everywhere.

| Atom | Description |
|------|-------------|
| [Java Memory Model](00-knowledge-atoms/java-memory-model/) | Heap, Stack, Metaspace |
| [Garbage Collection](00-knowledge-atoms/garbage-collection/) | GC algorithms, tuning |
| [equals() and hashCode()](00-knowledge-atoms/equals-hashcode/) | Contract, implementation |
| [Immutability](00-knowledge-atoms/immutability/) | Benefits, creation |
| [Pass by Value](00-knowledge-atoms/pass-by-value/) | Java semantics |
| [Autoboxing](00-knowledge-atoms/autoboxing/) | Wrapper classes |
| [Type Safety](00-knowledge-atoms/type-safety/) | Compile-time vs runtime |

---

## Modules

| # | Module | Level | What You'll Learn |
|---|--------|-------|-------------------|
| 01 | [Fundamentals](01-fundamentals/) | Student | Variables, types, operators, control flow, methods, arrays, strings |
| 02 | [OOP](02-oop/) | Student | Classes, objects, inheritance, polymorphism, encapsulation |
| 03 | [Collections](04-collections/) | Student-Junior | List, Set, Map, Queue, iterators |
| 04 | [Generics](05-generics/) | Junior | Generic classes, methods, wildcards |
| 05 | [Exceptions](03-exception-handling/) | Junior | Try-catch, custom exceptions |
| 06 | [Strings](06-strings/) | Student | String, StringBuilder, formatting |
| 07 | [Functional Programming](07-functional-programming/) | Junior | Lambdas, streams, optional |
| 08 | [IO/NIO](08-io-nio/) | Junior | Files, streams, buffers, channels |
| 09 | [Multithreading](09-multithreading/) | Mid-Level | Threads, synchronization, locks |
| 10 | [JVM Internals](10-jvm-internals/) | Mid-Senior | Classloading, memory, GC, JIT |
| 11 | [Design Patterns](11-design-patterns/) | Mid-Senior | Creational, structural, behavioral |
| 12 | [Testing](12-testing/) | Junior | JUnit 5, Mockito |
| 13 | [Reflection & Annotations](13-reflection-annotations/) | Mid-Level | Runtime type info |
| 14 | [Logging](14-logging/) | Junior | SLF4J, Logback |
| 15 | [Senior Topics](15-senior/) | Senior | Performance, advanced concurrency, production patterns |

---

## Learning Path

```
01-Fundamentals → 02-OOP → 03-Collections → 04-Generics → 05-Exceptions
                                                        ↓
                                              06-Strings → 07-Functional → 08-IO
                                                                      ↓
                                              09-Multithreading → 10-JVM → 11-Patterns
                                                                      ↓
                                              12-Testing → 13-Reflection → 14-Logging
                                                                      ↓
                                                                  15-Senior
```

---

## Quick Start

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

---

## Prerequisites

- Java 21+ installed
- A code editor (IntelliJ IDEA recommended)
