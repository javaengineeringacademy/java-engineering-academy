# Enumeration

## Overview

`Enumeration` is a legacy interface in Java that provides a way to traverse elements in a collection one at a time. Introduced in JDK 1.0, it was the original traversal mechanism before `Iterator`. `Enumeration` is simpler than `Iterator` but lacks features like removal and bidirectional traversal.

## Learning Objectives

- Understand the `Enumeration` interface and its legacy status
- Learn about `StringTokenizer` and its use cases
- Understand `Hashtable.elements()` and `Hashtable.keys()`
- Compare `Enumeration` with `Iterator`
- Recognize when to use `Enumeration` in modern applications

## Enumeration Interface Methods

- `boolean hasMoreElements()`: Returns true if more elements exist
- `E nextElement()`: Returns the next element

## Enumeration vs Iterator

| Feature | Enumeration | Iterator |
|---------|-------------|----------|
| Methods | `hasMoreElements()`, `nextElement()` | `hasNext()`, `next()`, `remove()` |
| Removal | Not supported | Supported |
| Bidirectional | No | ListIterator only |
| Legacy | Yes (JDK 1.0) | No (JDK 1.2) |
| Modern usage | Rare | Common |

## Syntax

```java
// From Vector
Vector<String> vector = new Vector<>();
Enumeration<String> enumeration = vector.elements();

// From Hashtable
Hashtable<String, Integer> hashtable = new Hashtable<>();
Enumeration<String> keys = hashtable.keys();
Enumeration<Integer> values = hashtable.elements();

// From List (using Collections utility)
Enumeration<String> listEnum = Collections.enumeration(list);

// Using StringTokenizer
StringTokenizer tokenizer = new StringTokenizer("Hello,World,Java");
while (tokenizer.hasMoreTokens()) {
    String token = tokenizer.nextToken();
    System.out.println(token);
}
```

## When to Use

- **Legacy APIs**: When working with `Vector`, `Hashtable`, or `StringTokenizer`
- **Backward compatibility**: When maintaining older codebases
- **New code**: Use `Iterator` or for-each loop instead

## Common Use Cases

- Traversing `Hashtable` keys/values
- Parsing strings with `StringTokenizer`
- Converting legacy collection APIs to modern ones via `Collections.enumeration()`
