# Module 07: Functional Programming in Java

## Overview

This module provides a comprehensive deep-dive into Functional Programming (FP) concepts in Java 21. You will master lambda expressions, functional interfaces, method references, the Stream API, Collectors, Optional, and function composition. Through progressive examples—from simple syntax to enterprise-grade patterns—you will learn how to write cleaner, more testable, and highly concurrent code using Java's functional paradigms.

## Status

✅ **Complete** — Full content implementation

## Learning Objectives

By the end of this module, you will be able to:

- [ ] Explain the core principles of functional programming and how Java implements them
- [ ] Write lambda expressions with correct scoping and variable capture rules
- [ ] Identify and use built-in functional interfaces (Predicate, Function, Consumer, Supplier)
- [ ] Create custom functional interfaces with default and static methods
- [ ] Apply method references to simplify lambda expressions
- [ ] Build efficient Stream pipelines for data processing
- [ ] Use parallel streams correctly and understand ForkJoinPool behavior
- [ ] Implement custom Collectors for complex aggregation tasks
- [ ] Apply Optional to eliminate null-related bugs
- [ ] Compose functions using andThen, compose, andThenApply patterns
- [ ] Avoid common functional programming pitfalls in production code

## Prerequisites

- Module 01: Java Fundamentals (variables, types, control flow)
- Module 02: Object-Oriented Programming (classes, interfaces, inheritance)
- Module 03: Exception Handling
- Module 04: Collections Framework
- Module 05: Generics
- Module 06: Java I/O and NIO

## Topics

| # | Topic | Est. Time |
|---|-------|-----------|
| 01 | [Introduction to Functional Programming](01-introduction/) | 1.5 hours |
| 02 | [Lambda Expressions](02-lambda-expressions/) | 2.5 hours |
| 03 | [Functional Interfaces](03-functional-interfaces/) | 2 hours |
| 04 | [Method References](04-method-references/) | 1.5 hours |
| 05 | [Stream API](05-stream-api/) | 2.5 hours |
| 06 | [Stream Operations](06-stream-operations/) | 3 hours |
| 07 | [Collectors](07-collectors/) | 2.5 hours |
| 08 | [Optional](08-optional/) | 2 hours |
| 09 | [Function Composition](09-composition/) | 2 hours |
| 10 | [Best Practices](10-best-practices/) | 1.5 hours |
| 11 | [Mini Project: Functional Data Pipeline](11-mini-project/) | 3 hours |

## Estimated Total Time

**24 hours** (including exercises and mini project)

## Module Project

Build a **Functional Data Pipeline Engine** that processes real-world datasets using pure functional techniques. The project will demonstrate:

- Lambda-based data transformations
- Custom collectors for domain-specific aggregations
- Optional-based null-safe data processing
- Function composition for building complex processing pipelines
- Stream-based parallel data processing

## Resources

- [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)
- [Oracle Java Tutorials: Streams](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/stream/package-summary.html)
- [Effective Java, 3rd Edition - Item 42-44: Functional Programming](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java 21 JEPs](https://openjdk.org/projects/jdk/21/)

## Module Structure

```
07-functional-programming/
├── README.md                          # This file
├── src/main/java/academy/javaengineering/functional/
│   ├── introduction/                  # Topic 01 source files
│   ├── lambda/                        # Topic 02 source files
│   ├── interfaces/                    # Topic 03 source files
│   ├── references/                    # Topic 04 source files
│   ├── streams/                       # Topic 05 source files
│   ├── operations/                    # Topic 06 source files
│   ├── collectors/                    # Topic 07 source files
│   ├── optional/                      # Topic 08 source files
│   ├── composition/                   # Topic 09 source files
│   ├── bestpractices/                 # Topic 10 source files
│   └── project/                       # Topic 11 mini project files
├── 01-introduction/
│   └── README.md
├── 02-lambda-expressions/
│   └── README.md
├── 03-functional-interfaces/
│   └── README.md
├── 04-method-references/
│   └── README.md
├── 05-stream-api/
│   └── README.md
├── 06-stream-operations/
│   └── README.md
├── 07-collectors/
│   └── README.md
├── 08-optional/
│   └── README.md
├── 09-composition/
│   └── README.md
├── 10-best-practices/
│   └── README.md
└── 11-mini-project/
    └── README.md
```

**Next Module**: Module 08: Multithreading
