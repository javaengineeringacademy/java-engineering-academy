# Spliterator Memory Behavior

## Memory Characteristics

### Spliterator Object
- Supports parallel traversal of source elements
- Can split into multiple Spliterators
- Enables parallel stream processing

### Splitting Behavior
- trySplit() creates new Spliterator
- Original keeps first half, new gets second half
- Balanced splitting for parallel efficiency

## Spliterator Characteristics

| Characteristic | Meaning |
|----------------|---------|
| ORDERED | Elements have defined order |
| DISTINCT | No duplicate elements |
| SORTED | Elements are sorted |
| SIZED | Exact size known |
| NONNULL | No null elements |
| IMMUTABLE | No structural modifications |
| CONCURRENT | Safe for concurrent modification |
| SUBSIZED | Split results are sized |

## Memory Patterns

```java
// Basic usage
Spliterator<T> spl = list.spliterator();
spl.forEachRemaining(System.out::println);

// Split for parallel
Spliterator<T> spl1 = list.spliterator();
Spliterator<T> spl2 = spl1.trySplit();
```

## Best Practices

1. Use Spliterator for parallel stream sources
2. Check characteristics before assuming behavior
3. Consider trySplit() for divide-and-conquer
4. Prefer stream() for most parallel operations
