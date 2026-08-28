# Modern Java Features

A comprehensive guide to modern Java features introduced from Java 10 through Java 21+.

## Module Overview

This module covers significant language features that have transformed Java from a verbose, ceremony-heavy language into a concise, expressive platform.

## Topics Covered

| Package | Feature | Java Version |
|---------|---------|--------------|
| 01-records | Records | Java 14+ (Preview) / Java 16 (Final) |
| 02-sealed-classes | Sealed Classes | Java 15+ (Preview) / Java 17 (Final) |
| 03-pattern-matching | Pattern Matching for switch | Java 17+ (Preview) / Java 21 (Final) |
| 04-text-blocks | Text Blocks | Java 13+ (Preview) / Java 15 (Final) |
| 05-switch-expressions | Switch Expressions | Java 12+ (Preview) / Java 14 (Final) |
| 06-var-type-inference | var Type Inference | Java 10+ |
| 07-help-commands | Help Commands | Java 10+ |
| 08-multi-catch | Multi-catch Exception Handling | Java 7+ |
| 09-instanceof-pattern | instanceof Pattern Matching | Java 14+ (Preview) / Java 16 (Final) |
| 10-record-patterns | Record Patterns | Java 19+ (Preview) / Java 21 (Final) |

## Prerequisites

- JDK 17 or later (for all features)
- Maven 3.6+ or Gradle 7+
- IDE with Java language server support

## Learning Path

1. **Records** - Immutable data carriers
2. **Sealed Classes** - Restricted class hierarchies
3. **Pattern Matching** - Type-safe conditionals
4. **Text Blocks** - Multi-line string literals
5. **Switch Expressions** - Expressive switch
6. **var Type Inference** - Local variable type inference
7. **Multi-catch** - Exception handling improvements
8. **instanceof Pattern** - Type checks with binding
9. **Record Patterns** - Deconstruction patterns

## Build

```bash
mvn clean compile
mvn test
```
