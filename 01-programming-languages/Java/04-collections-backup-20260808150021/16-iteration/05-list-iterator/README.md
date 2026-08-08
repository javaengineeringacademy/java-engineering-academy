# List Iterator — Bidirectional Power

## Why List Iterator Exists

Iterator only goes forward. But sometimes you need to:
- Traverse a List backwards
- Add elements during iteration
- Replace the current element
- Start from a specific position

ListIterator extends Iterator to provide **bidirectional traversal with modification capabilities** — it's the Swiss Army Knife of List iteration.

**Production incident:** A document editor needed to find and replace text while traversing backwards (to handle overlapping edits). Enhanced for couldn't go backwards. Iterator couldn't add elements. ListIterator solved both.

## The Pain Point

```java
// Iterator: forward only
Iterator<String> it = list.iterator();
while (it.hasNext()) { ... }
// No hasPrevious(), no previous()

// Can't go backwards with Iterator
// Can't add during iteration with Iterator
// Can't set/replace current element with Iterator
```

ListIterator provides all three capabilities.

## ListIterator Interface

```java
public interface ListIterator<E> extends Iterator<E> {
    // Navigation
    boolean hasNext();
    E next();
    int nextIndex();
    boolean hasPrevious();
    E previous();
    int previousIndex();

    // Modification
    void set(E e);       // Replace current element
    void add(E e);       // Insert before next element
    void remove();       // Remove current element (same as Iterator.remove())
}
```

## Bidirectional Traversal

```java
// Forward traversal (same as Iterator)
ListIterator<String> it = list.listIterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// Backward traversal
ListIterator<String> revIt = list.listIterator(list.size());
while (revIt.hasPrevious()) {
    System.out.println(revIt.previous());
}

// Forward then backward
ListIterator<String> it = list.listIterator();
while (it.hasNext()) {
    System.out.println(it.next());  // Forward: Alice, Bob, Charlie
}
// Now at end
while (it.hasPrevious()) {
    System.out.println(it.previous());  // Backward: Charlie, Bob, Alice
}
```

## hasPrevious() and previous()

```java
List<String> list = List.of("Alice", "Bob", "Charlie");
ListIterator<String> it = list.listIterator();

// Forward to middle
it.next();  // Alice
it.next();  // Bob

// Now go backwards
if (it.hasPrevious()) {
    String prev = it.previous();  // Bob
    System.out.println("Went back to: " + prev);
}

// previous() returns element BEFORE cursor
// next() returns element AFTER cursor
```

## add() and set() During Iteration

```java
// Add during iteration
List<String> list = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
ListIterator<String> it = list.listIterator();

while (it.hasNext()) {
    String name = it.next();
    if (name.equals("Bob")) {
        it.add("Bob Jr.");  // Inserts BEFORE next element (after Bob)
    }
}
// Result: [Alice, Bob, Bob Jr., Charlie]
// add() inserts before the element that next() would return

// set() replaces last element returned by next() or previous()
ListIterator<String> it = list.listIterator();
while (it.hasNext()) {
    String name = it.next();
    it.set(name.toUpperCase());  // Replaces current element
}
// Result: [ALICE, BOB, CHARLIE]
```

### How add() Affects Cursor

```java
// list: [A, B, C]
// cursor at index 0 (before A)

it.next();           // Returns A, cursor at 1
it.add("X");         // Inserts X at cursor position
// list: [A, X, B, C]
// cursor still at 1 (now pointing to X)
// next() will return X

it.next();           // Returns X (not B!)
// This is a common gotcha!
```

## Starting from Specific Index

```java
// Start from beginning (default)
ListIterator<String> it = list.listIterator();

// Start from specific index
ListIterator<String> it = list.listIterator(3);  // Start at index 3
// next() returns element at index 3
// hasPrevious() is true (elements 0-2 exist)

// Start from end
ListIterator<String> it = list.listIterator(list.size());
// hasPrevious() is true
// hasNext() is false
// previous() returns last element

// Start from middle
ListIterator<String> it = list.listIterator(list.size() / 2);
// nextIndex() returns list.size() / 2
```

## Modification Capabilities Summary

```java
// 1. set() — replace current element
it.next();
it.set("NEW_VALUE");  // Replaces last returned

// 2. add() — insert before next position
it.add("INSERTED");  // Goes before what next() would return

// 3. remove() — remove current element
it.next();
it.remove();  // Removes last returned

// ALL three modify the list AND update Iterator state
// No ConcurrentModificationException!
```

## When to Use / When NOT to Use

### ✅ USE ListIterator When:
- Bidirectional traversal needed
- Adding elements during iteration
- Replacing elements during iteration
- Starting from specific position
- Processing palindromes (forward + backward)

### ❌ DON'T Use ListIterator When:
- Forward-only traversal → use enhanced for
- Simple removal → use removeIf()
- Random access → use for loop
- Not a List → use Iterator

## Performance

```
Operation          │ Time  │ Notes
───────────────────┼───────┼──────────────────────
Creation           │ O(1)  │ Cursor-based
hasNext()          │ O(1)  │ Position check
next()             │ O(1)  │ ArrayList, O(n) LinkedList
hasPrevious()      │ O(1)  │ Position check
previous()         │ O(1)  │ ArrayList, O(n) LinkedList
add()              │ O(n)  │ Shifts elements
set()              │ O(1)  │ Replaces in place
remove()           │ O(n)  │ Shifts elements
```

## Common Mistakes

### Mistake 1: add() Position Confusion
```java
// list: [A, B, C]
ListIterator<String> it = list.listIterator();
it.next();  // Returns A
it.add("X");  // Inserts at cursor (after A)

// What you might expect: [A, X, B, C]
// What you get: [A, X, B, C] ✓
// BUT if you call next() again:
it.next();  // Returns X, NOT B!
// The add() inserts before what next() would return
```

### Mistake 2: set() After add()
```java
// WRONG: set() operates on last returned by next()/previous()
it.next();        // Returns element
it.add("NEW");    // Adds new element
it.set("REPLACE"); // WRONG: replaces the NEW element, not original!

// RIGHT: set() right after next()/previous()
it.next();        // Returns element
it.set("REPLACE"); // Replaces the element just returned
it.add("NEW");     // Then add
```

### Mistake 3: Wrong Starting Index
```java
// WRONG: empty list with index
List<String> list = new ArrayList<>();
ListIterator<String> it = list.listIterator(0);  // OK for empty list
it.next();  // NoSuchElementException!

// Check size first
if (!list.isEmpty()) {
    ListIterator<String> it = list.listIterator(list.size() - 1);
    // ...
}
```

## Interview Questions

**Q: What does ListIterator add over Iterator?**
A: Bidirectional traversal (hasPrevious, previous), add(), set(), and index-aware navigation (nextIndex, previousIndex).

**Q: How does add() work in ListIterator?**
A: It inserts an element before the position that next() would return. The cursor stays at the same position.

**Q: Can ListIterator be used on any Collection?**
A: No, only on List. You get a ListIterator via list.listIterator().

**Q: What happens if you call set() without next()/previous()?**
A: IllegalStateException. set() replaces the element last returned by next() or previous().

**Q: When would you use listIterator(n)?**
A: When you need to start iteration from a specific index, or iterate backwards from the end.
