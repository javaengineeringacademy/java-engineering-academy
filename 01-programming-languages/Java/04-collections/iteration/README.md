# Iteration

## 1. What Is Iteration

Iteration is the process of traversing through elements of a collection sequentially. Java provides multiple ways to iterate over collections, each with different performance characteristics and use cases.

## 2. 8 Traversal Methods Overview

### 1. Enhanced For Loop (for-each)

```java
for (String element : list) {
    System.out.println(element);
}
```

| Aspect | Description |
|--------|-------------|
| Syntax | Simple, readable |
| Performance | Good (compiler optimizes) |
| Modification | Cannot remove during iteration |
| Use case | Simple traversal |

### 2. Iterator

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String element = it.next();
    if (element.isEmpty()) {
        it.remove();  // Safe removal
    }
}
```

| Aspect | Description |
|--------|-------------|
| Syntax | Verbose but flexible |
| Performance | Good |
| Modification | Can remove during iteration |
| Use case | Conditional removal |

### 3. ListIterator

```java
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    String element = lit.next();
    lit.set(element.toUpperCase());  // Replace
    lit.add("new");  // Insert
}
```

| Aspect | Description |
|--------|-------------|
| Syntax | Verbose |
| Performance | Good |
| Modification | Can add, set, remove |
| Use case | Bidirectional traversal, modification |

### 4. forEach() with Lambda

```java
list.forEach(element -> System.out.println(element));
list.forEach(System.out::println);
```

| Aspect | Description |
|--------|-------------|
| Syntax | Concise |
| Performance | Good |
| Modification | Cannot remove (unless using removeIf) |
| Use case | Simple operations |

### 5. Stream API

```java
list.stream()
    .filter(e -> e.length() > 3)
    .map(String::toUpperCase)
    .forEach(System.out::println);
```

| Aspect | Description |
|--------|-------------|
| Syntax | Functional, composable |
| Performance | Slight overhead |
| Modification | Immutable operations |
| Use case | Complex transformations |

### 6. while loop with Iterator

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}
```

| Aspect | Description |
|--------|-------------|
| Syntax | Verbose |
| Performance | Good |
| Modification | Can remove during iteration |
| Use case | Complex logic |

### 7. Collections.forEach()

```java
Collections.forEach(list, System.out::println);
```

| Aspect | Description |
|--------|-------------|
| Syntax | Static method |
| Performance | Good |
| Modification | Cannot remove |
| Use case | Utility method |

### 8. toArray() + for loop

```java
Object[] array = list.toArray();
for (int i = 0; i < array.length; i++) {
    System.out.println(array[i]);
}
```

| Aspect | Description |
|--------|-------------|
| Syntax | Traditional |
| Performance | Good (array access) |
| Modification | Cannot modify collection |
| Use case | Array processing |

## 3. When to Use Each

| Method | Best For | Avoid When |
|--------|----------|------------|
| Enhanced for | Simple traversal | Need to remove elements |
| Iterator | Conditional removal | Simple traversal |
| ListIterator | Bidirectional, modification | Simple traversal |
| forEach() lambda | Concise traversal | Need to remove |
| Stream API | Complex transformations | Simple traversal |
| while + Iterator | Complex logic | Simple traversal |
| toArray + for | Array processing | Need collection updates |

## 4. Performance Comparison

| Method | Time | Space | Modification |
|--------|------|-------|--------------|
| Enhanced for | O(n) | O(1) | No |
| Iterator | O(n) | O(1) | Remove only |
| ListIterator | O(n) | O(1) | Add, set, remove |
| forEach() | O(n) | O(1) | No |
| Stream | O(n) | O(1) | No |
| while + Iterator | O(n) | O(1) | Remove only |
| toArray + for | O(n) | O(n) | No |

## 5. Common Mistakes

### 1. Modifying During Enhanced For Loop

```java
// BAD - throws ConcurrentModificationException
for (String element : list) {
    if (element.isEmpty()) {
        list.remove(element);
    }
}

// GOOD - use Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().isEmpty()) {
        it.remove();
    }
}

// GOOD - use removeIf
list.removeIf(String::isEmpty);
```

### 2. Using Index-Based Loop on LinkedList

```java
// BAD - O(n^2) for LinkedList
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));  // O(n) per call
}

// GOOD - use enhanced for
for (String element : list) {
    System.out.println(element);  // O(1) per call
}
```

### 3. Not Using removeIf()

```java
// BAD - manual removal
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().length() < 3) {
        it.remove();
    }
}

// GOOD - use removeIf
list.removeIf(e -> e.length() < 3);
```

## 6. Iteration and Thread Safety

| Collection | Iterator Type | Behavior |
|------------|--------------|----------|
| ArrayList | Fail-fast | Throws ConcurrentModificationException |
| HashMap | Fail-fast | Throws ConcurrentModificationException |
| CopyOnWriteArrayList | Snapshot | No exception, reflects state at creation |
| ConcurrentHashMap | Weakly consistent | No exception, may miss some updates |

## 7. One-Minute Revision

- 8 ways to iterate in Java
- Enhanced for: simplest, no modification
- Iterator: safe removal during iteration
- ListIterator: bidirectional, add/set/remove
- forEach(): concise lambda syntax
- Stream API: functional, composable
- Don't modify collection during enhanced for loop
- Use removeIf() for conditional removal

## 8. References

- [Oracle Java Documentation - Iterator](https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 58: Use for-each loops](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
