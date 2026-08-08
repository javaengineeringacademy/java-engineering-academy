# Iterator — The Collection Navigator

## Why Iterator Exists

Collections store data in different ways — arrays, linked lists, trees, hash tables. The Iterator interface provides a **uniform way to traverse any collection** without knowing its internal structure. It's the backbone of Java's collection iteration.

**Production incident:** A search engine's index updater used `list.remove()` inside enhanced for-loop. Under concurrent load, it corrupted the index — 2M documents became inaccessible. Fixed by using `Iterator.remove()` with proper synchronization.

## The Pain Point

Without Iterator, you'd need different traversal code for every collection type:
```java
// ArrayList: use index
for (int i = 0; i < arrayList.size(); i++) { ... }

// LinkedList: use node traversal
Node current = linkedList.head;
while (current != null) { ... }

// HashSet: use bucket traversal
for (Bucket bucket : hashSet.buckets) { ... }
```

Iterator unifies this — one interface, works everywhere.

## Iterator Interface

```java
public interface Iterator<E> {
    boolean hasNext();  // Are there more elements?
    E next();           // Get the next element
    default void remove() { ... }  // Remove current element
    default void forEachRemaining(Consumer<? super E> action) { ... }
}
```

## Basic Usage

```java
// Manual Iterator usage
List<String> names = List.of("Alice", "Bob", "Charlie");
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    String name = it.next();
    process(name);
}

// With for loop
for (Iterator<String> it = names.iterator(); it.hasNext(); ) {
    String name = it.next();
    process(name);
}
```

## hasNext() and next()

```java
Iterator<String> it = list.iterator();

// Always check hasNext() before next()
if (it.hasNext()) {
    String element = it.next();
}

// NoSuchElementException if no more elements
// String element = it.next();  // Throws if empty!
```

### How next() Works Internally

```java
// Simplified ArrayList Iterator implementation
private class Itr implements Iterator<E> {
    int cursor = 0;       // Index of next element
    int lastRet = -1;     // Index of last returned element

    public boolean hasNext() {
        return cursor != size;
    }

    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }
}
```

## Fail-Fast Behavior

Java's Iterator is **fail-fast** — if the collection is modified structurally after Iterator creation, the Iterator throws `ConcurrentModificationException`.

```java
List<String> names = new ArrayList<>(List.of("Alice", "Bob"));
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    String name = it.next();
    if (name.equals("Bob")) {
        names.add("Dave");  // Structural modification!
        // Iterator detects this at next next() call
    }
}

// What happens:
// 1. names.add("Dave") — modifies modCount to 3
// 2. it.next() checks expectedModCount (2) != modCount (3)
// 3. Throws ConcurrentModificationException
```

### modCount Mechanism

```java
// ArrayList tracks modifications
transient int modCount = 0;

// Iterator captures expectedModCount on creation
Iterator<String> it = list.iterator();
// expectedModCount = modCount at this point

// Any structural modification increments modCount
list.add("X");     // modCount++
list.remove(0);    // modCount++

// Iterator checks at next()/remove()
if (modCount != expectedModCount) {
    throw new ConcurrentModificationException();
}
```

## remove() for Safe Deletion

```java
// The SAFE way to remove during iteration
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String name = it.next();
    if (name.isEmpty()) {
        it.remove();  // Removes current element, updates modCount correctly
    }
}

// What remove() does internally:
// 1. Calls ArrayList.this.remove(lastRet)
// 2. Updates cursor to account for shifted elements
// 3. Resets expectedModCount = modCount (synchronizes)
```

### What Happens with list.remove()?

```java
// DANGEROUS: list.remove() during Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String name = it.next();
    if (name.equals("Bob")) {
        list.remove(name);  // modCount increases, Iterator doesn't know
        // next() call will detect mismatch → CME
    }
}
```

## When to Use / When NOT to Use

### ✅ USE Iterator When:
- Removing elements during iteration
- Need explicit control over traversal
- Writing library/framework code
- Implementing custom traversal logic
- Multiple threads reading (with external sync)

### ❌ DON'T Use Iterator When:
- Simple forward traversal → use enhanced for
- Need index → use for loop
- Need bidirectional traversal → use ListIterator
- Need parallel processing → use Stream

## Performance: O(1) Per Step

```
Operation             │ Time  │ Notes
──────────────────────┼───────┼──────────────────────
Iterator creation      │ O(1)  │ Captures modCount
hasNext()             │ O(1)  │ Simple check
next()                │ O(1)  │ Returns current, advances cursor
remove()              │ O(1)  │ Removes current, adjusts cursor
forEachRemaining()    │ O(n)  │ Iterates remaining elements
```

Iterator itself adds no overhead — it's just a cursor over the collection.

## Common Mistakes

### Mistake 1: Calling remove() Without next()
```java
// WRONG: IllegalStateException
Iterator<String> it = list.iterator();
it.remove();  // No next() called yet!

// RIGHT: must call next() first
Iterator<String> it = list.iterator();
it.next();
it.remove();  // OK
```

### Mistake 2: Using Iterator After Collection Modification
```java
// WRONG: Iterator becomes invalid
Iterator<String> it = list.iterator();
list.add("New");  // Structural modification
it.next();  // CME!

// RIGHT: create new Iterator after modification
list.add("New");
Iterator<String> it = list.iterator();  // Fresh Iterator
```

### Mistake 3: Not Checking hasNext()
```java
// WRONG: might throw NoSuchElementException
Iterator<String> it = emptyList.iterator();
String name = it.next();  // Throws!

// RIGHT: always check
if (it.hasNext()) {
    String name = it.next();
}
```

### Mistake 4: Confusing Iterator with Enumeration
```java
// Iterator: has remove(), throws NoSuchElementException
Iterator<String> it = list.iterator();

// Enumeration: no remove(), no CME detection
Enumeration<String> e = Collections.enumeration(list);
```

## Interview Questions

**Q: What is fail-fast behavior?**
A: Iterator detects structural modifications to the collection and throws ConcurrentModificationException if modification occurs.

**Q: How does Iterator detect ConcurrentModificationException?**
A: Via `modCount` field. Iterator captures expected modCount on creation; checks at each next()/remove().

**Q: Can two Iterators traverse the same collection simultaneously?**
A: Yes, but each Iterator has its own cursor. Modifying collection via one Iterator affects the other (CME).

**Q: What's the difference between Iterator.remove() and Collection.remove()?**
A: Iterator.remove() removes current element and updates Iterator state. Collection.remove() modifies collection directly, invalidating any active Iterators.

**Q: Is Iterator thread-safe?**
A: No. Iterator is not synchronized. Concurrent access requires external synchronization or CopyOnWriteArrayList.
