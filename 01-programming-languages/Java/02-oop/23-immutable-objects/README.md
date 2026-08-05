# Immutable Objects in Java

## Overview
Immutable objects cannot be modified after creation. They are thread-safe and provide predictable behavior.

## When to Use
- For thread safety without synchronization
- For values that should not change (coordinates, dates)
- As map keys or set elements
- For caching and pooling

## Code Example
See `src/main/java/academy/javaengineering/oop/immutable/ImmutablePoint.java`

```java
ImmutablePoint p = new ImmutablePoint(3, 4);
ImmutablePoint moved = p.translate(2, 3); // p unchanged
```

## Common Mistakes
1. Not making the class final
2. Not making all fields final
3. Returning mutable objects directly
4. Not making defensive copies in constructor

## Interview Questions
1. What makes a class immutable?
2. Why are immutable objects thread-safe?
3. What are the benefits of immutability?
4. How do you create an immutable class?
5. What Java classes are immutable by design?
