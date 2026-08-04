# Java Programming Language - Complete Guide

## Overview

Java is a class-based, object-oriented programming language designed to have as few implementation dependencies as possible. It follows the "write once, run anywhere" (WORA) principle, meaning compiled Java code can run on all platforms that support Java without recompilation.

## Key Features

- **Platform Independent**: Bytecode runs on any JVM
- **Object-Oriented**: Everything revolves around objects and classes
- **Strongly Typed**: Explicit type declarations required
- **Garbage Collected**: Automatic memory management
- **Multithreaded**: Built-in concurrency support
- **Rich Standard Library**: Extensive APIs for common tasks

## Learning Path

### 1. [Fundamentals](fundamentals/README.md)
Variables, data types, operators, control flow, arrays, methods, OOP basics, String handling, packages, and imports. Start here if you're new to Java.

### 2. [Advanced Topics](advanced/README.md)
Generics, annotations, reflection, streams, optional, records, sealed classes, pattern matching, text blocks, and var keyword. Master these to write modern Java code.

### 3. [Collections Framework](collections/README.md)
List, Set, Map, Queue, Deque, Comparable, Comparator, Iterator, Collections utility, concurrent collections, and performance comparisons. Essential for data manipulation.

### 4. [Concurrency](concurrency/README.md)
Thread creation, synchronization, locks, atomic variables, executor service, CompletableFuture, virtual threads, and StructuredTaskScope. Build responsive applications.

### 5. [Memory Management](memory-management/README.md)
JVM memory areas, heap/stack, garbage collection (G1, ZGC, Shenandoah), memory leaks, profiling, JOL, and weak/soft references. Optimize memory usage.

### 6. [JVM Internals](internals/README.md)
Class loading, bytecode, JIT compilation, JVM architecture, JVM arguments, and diagnostic tools. Understand how Java really works under the hood.

### 7. [Performance](performance/README.md)
Profiling, benchmarking with JMH, microbenchmarks, GC tuning, thread pool tuning, connection pool tuning, and caching. Make your applications fast.

### 8. [Best Practices](best-practices/README.md)
Code organization, error handling, logging, testing, documentation, security, and performance tips. Write production-ready code.

### 9. [Project Ideas](projects/README.md)
Hands-on projects to practice your skills. Build real applications to solidify your knowledge.

### 10. [Interview Questions](interview-questions/README.md)
100+ Java interview questions organized by topic with detailed answers. Prepare for technical interviews.

## Java Version History

| Version | Release Date | Key Features |
|---------|--------------|--------------|
| Java 8 | 2014 | Lambda expressions, Stream API, Optional |
| Java 9 | 2017 | Module system, JShell, Process API |
| Java 10 | 2018 | Local variable type inference (var) |
| Java 11 | 2018 | HTTP Client, String methods, single-file programs |
| Java 12 | 2019 | Switch expressions, Teeing Collector |
| Java 13 | 2019 | Text blocks preview |
| Java 14 | 2020 | Records preview, Pattern matching instanceof |
| Java 15 | 2020 | Text blocks, Sealed classes preview |
| Java 16 | 2021 | Records, Pattern matching instanceof |
| Java 17 | 2021 | Sealed classes, Pattern matching for switch preview |
| Java 18 | 2022 | Simple web server, Code snippets in javadoc |
| Java 19 | 2022 | Virtual threads, Structured concurrency preview |
| Java 20 | 2023 | Record patterns, Pattern matching for switch |
| Java 21 | 2023 | Virtual threads GA, Sequenced collections, String templates preview |
| Java 22 | 2024 | Unnamed variables, Statements before super(), Stream gatherers preview |

## Development Environment Setup

### JDK Installation

```bash
# macOS (using Homebrew)
brew install openjdk@21

# Ubuntu/Debian
sudo apt install openjdk-21-jdk

# Windows (using Chocolatey)
choco install openjdk21
```

### IDE Options

- **IntelliJ IDEA**: Most popular, excellent Java support
- **Eclipse**: Free, open-source, extensive plugin ecosystem
- **VS Code**: Lightweight, with Java extensions

### Build Tools

- **Maven**: XML-based configuration, convention over configuration
- **Gradle**: Groovy/Kotlin DSL, flexible and powerful

## Quick Start Example

```java
import java.util.List;
import java.util.stream.Collectors;

public class HelloWorld {
    public static void main(String[] args) {
        // Modern Java features
        var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        var evenSquares = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .collect(Collectors.toList());
        
        System.out.println("Even squares: " + evenSquares);
        
        // Records (Java 16+)
        record Point(int x, int y) {}
        
        var points = List.of(new Point(1, 2), new Point(3, 4), new Point(5, 6));
        
        // Pattern matching instanceof (Java 16+)
        Object obj = "Hello, World!";
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("Long string: " + s.toUpperCase());
        }
    }
}
```

## Common Design Patterns in Java

1. **Singleton**: Ensure a class has only one instance
2. **Factory**: Create objects without specifying exact class
3. **Builder**: Construct complex objects step by step
4. **Observer**: Define subscription mechanism for notifications
5. **Strategy**: Define family of algorithms and make them interchangeable
6. **Decorator**: Add responsibilities to objects dynamically

## Resources

- [Oracle Java Documentation](https://docs.oracle.com/en/java/)
- [Baeldung](https://www.baeldung.com/) - Java tutorials and guides
- [Java Brains](https://javabrains.io/) - Video tutorials
- [Effective Java](https://www.oreilly.com/library/view/effective-java/9780134686097/) - Joshua Bloch's book

---

*Last updated: August 2026*
