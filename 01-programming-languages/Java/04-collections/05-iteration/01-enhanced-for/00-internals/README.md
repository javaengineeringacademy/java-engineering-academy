# Enhanced For Loop (for-each)

## What
Simplified iteration without index.

## When
- Don't need index
- Read-only iteration
- Cleanest syntax

## Syntax
```java
for (T element : collection) {
    System.out.println(element);
}
```

## Limitation
- Cannot modify collection during iteration
- Cannot get index
