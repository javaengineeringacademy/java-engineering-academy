# ListIterator Memory Behavior

## Memory Characteristics

### ListIterator Object
- Extends Iterator with bidirectional capability
- Maintains current index position
- Supports add() and set() operations

### Internal State
- Current position index
- Last returned element reference
- Expected modification count

## ListIterator vs Iterator

| Feature | Iterator | ListIterator |
|---------|----------|--------------|
| Direction | Forward only | Bidirectional |
| Add | Not supported | add() method |
| Set | Not supported | set() method |
| Index | Not available | nextIndex()/previousIndex() |
| Memory | Lower | Higher |

## Memory Patterns

```java
// Forward traversal
ListIterator<T> it = list.listIterator();
while (it.hasNext()) {
    T item = it.next();
}

// Backward traversal
while (it.hasPrevious()) {
    T item = it.previous();
}

// Insert at position
it.add(newElement);
```

## Best Practices

1. Use ListIterator for bidirectional traversal
2. Use set() to replace current element
3. Use add() to insert without removing
4. Consider memory overhead for simple operations
