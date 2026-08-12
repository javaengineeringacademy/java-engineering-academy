# 10 - Raw Types

> **Reading:** 30 min | **Practice:** 30 min

## Overview

Raw types are generic types used without their type parameters. They exist for backward compatibility with pre-generics code but lose compile-time type safety.

## Why It Exists

Raw types exist because:
- Java 5 introduced generics while maintaining backward compatibility
- Legacy code uses raw types (e.g., `List` instead of `List<String>`)
- Raw types allow pre-generics code to work with post-generics code

## What is a Raw Type

A raw type is the name of a generic class or interface without its type arguments:

```java
// Generic type
List<String> list = new ArrayList<>();

// Raw type (no type parameter)
List rawList = new ArrayList();
```

## Problems with Raw Types

| Problem | Description |
|---------|-------------|
| No type safety | Compiler cannot catch type mismatches |
| Manual casting required | Must cast when retrieving elements |
| Runtime ClassCastException | Wrong types cause runtime errors |
| Warnings | Compiler generates unchecked warnings |

## When to Use Raw Types

### Use Raw Types when:
- Maintaining legacy code that predates Java 5
- Interfacing with APIs that don't support generics
- The type is truly unknown (use `Object` instead)

### Avoid Raw Types when:
- Writing new code (always parameterize)
- The type is known (use generic type parameters)
- You need type safety (use wildcards: `List<?>`)

## Decision Rules

1. **Never use raw types in new code** — always parameterize
2. **Use wildcards for unknown types** — `List<?>` instead of `List`
3. **Gradually migrate** — add type parameters to legacy code
4. **Suppress warnings carefully** — document why raw type is necessary

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Using raw `List` | Loses type safety | Use `List<String>` |
| Raw type in method parameter | Callers lose type safety | Use `List<?>` or `List<T>` |
| Raw type in field | Class loses type safety | Use generic field type |
| Ignoring warnings | Type safety issues hidden | Address warnings or suppress with justification |

## Summary

| Concept | Key Takeaway |
|---------|--------------|
| Raw type | Generic class without type parameters |
| Problem | Loses compile-time type safety |
| When to use | Only for backward compatibility |
| Alternative | Use `<?>` wildcard for unknown types |
| Migration | Gradually add type parameters to legacy code |
