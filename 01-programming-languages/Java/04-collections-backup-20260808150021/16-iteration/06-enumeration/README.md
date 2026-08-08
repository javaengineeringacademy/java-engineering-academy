# Enumeration — The Legacy Iterator

## Why Enumeration Exists

Enumeration was Java's **first iteration mechanism**, introduced in JDK 1.0 with Vector and Hashtable. It's the predecessor to Iterator. You'll rarely use it in new code, but you MUST understand it because it's everywhere in legacy systems.

**Production incident:** A banking system's core module was written in 2003 using Hashtable extensively. Every developer kept accidentally using Iterator (which doesn't work on Hashtable). The team spent 2 weeks debugging why iteration wasn't working. They needed Enumeration.

## The Pain Point

Before Java 1.2 (Collections Framework), Java had:
- `Vector` (thread-safe ArrayList)
- `Hashtable` (thread-safe HashMap)
- `StringTokenizer`

These legacy classes don't implement `Iterable<T>`. They only have `elements()` which returns `Enumeration`.

```java
// Vector's only iteration method
Vector<String> vector = new Vector<>();
Enumeration<String> e = vector.elements();

// You CANNOT use enhanced for on Enumeration
for (String s : vector) { }  // Compile error — Vector doesn't implement Iterable

// You CAN use enhanced for on ArrayList
ArrayList<String> list = new ArrayList<>();
for (String s : list) { }  // OK — ArrayList implements Iterable
```

## Enumeration Interface

```java
public interface Enumeration<E> {
    boolean hasMoreElements();  // Like hasNext()
    E nextElement();            // Like next() — throws NoSuchElementException
}
```

## Basic Usage

```java
// Classic Enumeration usage
Vector<String> names = new Vector<>(List.of("Alice", "Bob", "Charlie"));
Enumeration<String> e = names.elements();

while (e.hasMoreElements()) {
    String name = e.nextElement();
    process(name);
}

// With for loop (ugly but works)
for (Enumeration<String> e = names.elements(); e.hasMoreElements(); ) {
    String name = e.nextElement();
    process(name);
}
```

## Legacy Classes That Use Enumeration

```java
// Vector
Vector<String> vector = new Vector<>();
Enumeration<String> e1 = vector.elements();

// Hashtable
Hashtable<String, Integer> table = new Hashtable<>();
Enumeration<String> keys = table.keys();
Enumeration<Integer> values = table.elements();

// StringTokenizer
StringTokenizer st = new StringTokenizer("Hello,World,Java");
while (st.hasMoreTokens()) {
    String token = st.nextToken();
    System.out.println(token);
}

// ResourceBundle
ResourceBundle bundle = ResourceBundle.getBundle("messages");
Enumeration<String> keys = bundle.getKeys();
```

## Iterator vs Enumeration

| Feature | Iterator | Enumeration |
|---------|----------|-------------|
| Package | java.util | java.util |
| Methods | hasNext(), next(), remove() | hasMoreElements(), nextElement() |
| remove() | Yes | No |
| Thread-safe | No | No |
| CME detection | Yes (fail-fast) | No |
| Introduced | Java 1.2 | Java 1.0 |
| Modern | Yes | Legacy |

```java
// Converting Enumeration to Iterator
Vector<String> vector = new Vector<>();
Enumeration<String> e = vector.elements();

// Manual conversion
Iterator<String> it = new Iterator<String>() {
    public boolean hasNext() { return e.hasMoreElements(); }
    public String next() { return e.nextElement(); }
    public void remove() { throw new UnsupportedOperationException(); }
};

// Using Collections.list() (Java 1.2+)
List<String> list = Collections.list(vector.elements());
// Now use enhanced for
for (String s : list) { ... }
```

## Thread-Safe Iteration with Vector

```java
// Vector is synchronized, but iteration is NOT atomic
Vector<Integer> vector = new Vector<>();
vector.add(1); vector.add(2); vector.add(3);

// This can still throw ConcurrentModificationException!
// (though Vector's iterator is not fail-fast like ArrayList's)

// Proper synchronization
synchronized (vector) {
    Enumeration<Integer> e = vector.elements();
    while (e.hasMoreElements()) {
        process(e.nextElement());
    }
}

// Better: use Collections.synchronizedList or CopyOnWriteArrayList
List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
// or
List<Integer> cowList = new CopyOnWriteArrayList<>();
```

## When to Use / When NOT to Use

### ✅ USE Enumeration When:
- Working with legacy Vector/Hashtable code
- Maintaining old systems (can't change to ArrayList)
- Using StringTokenizer (still in some codebases)
- Reading ResourceBundle keys

### ❌ DON'T Use Enumeration When:
- Writing new code → use Iterator or enhanced for
- Need to remove elements → use Iterator
- Need fail-fast detection → use Iterator
- Can migrate to modern Collections → do it

## Common Mistakes

### Mistake 1: Confusing Enumeration with Iterator
```java
// WRONG: Enumeration doesn't have remove()
Vector<String> vector = new Vector<>();
Enumeration<String> e = vector.elements();
while (e.hasMoreElements()) {
    String name = e.nextElement();
    e.remove();  // Compile error — Enumeration has no remove()
}

// RIGHT: use Iterator for removal
Iterator<String> it = vector.iterator();
while (it.hasNext()) {
    String name = it.next();
    it.remove();  // OK
}
```

### Mistake 2: Using Enumeration in New Code
```java
// WRONG: legacy code for no reason
Vector<String> vector = new Vector<>();
Enumeration<String> e = vector.elements();
while (e.hasMoreElements()) {
    process(e.nextElement());
}

// RIGHT: use modern alternatives
ArrayList<String> list = new ArrayList<>();
for (String s : list) {
    process(s);
}
// Or better yet: list.forEach(this::process);
```

### Mistake 3: Assuming Enumeration is Fail-Fast
```java
// Enumeration does NOT throw ConcurrentModificationException
Vector<String> vector = new Vector<>();
vector.add("A"); vector.add("B");

Enumeration<String> e = vector.elements();
while (e.hasMoreElements()) {
    String name = e.nextElement();
    vector.add("X");  // Modifies collection — but no exception!
    // Enumeration doesn't track modifications
}

// This can cause infinite loop or unexpected behavior
// but won't throw CME
```

## Performance

```
Operation            │ Vector+Enumeration │ ArrayList+Iterator
─────────────────────┼────────────────────┼────────────────────
Creation             │ O(1)               │ O(1)
hasNext/hasMoreElems │ O(1)               │ O(1)
next/nextElement     │ O(1)*              │ O(1)
remove               │ N/A                │ O(n)
Thread-safety        │ Synchronized       │ Not synchronized

* Vector's synchronized overhead adds ~5-10% cost
```

## Interview Questions

**Q: What is Enumeration?**
A: Java's original iteration interface (JDK 1.0), used with Vector and Hashtable. Predecessor to Iterator.

**Q: Why doesn't Enumeration have remove()?**
A: It was designed before the Collections Framework. It was meant for simple read-only traversal of legacy classes.

**Q: Can you use enhanced for on Vector?**
A: No, Vector doesn't implement Iterable<T>. You must use its elements() method returning Enumeration.

**Q: How do you convert Enumeration to Iterator?**
A: Use Collections.list(enumeration) to get a List, then call iterator() on it.

**Q: Should you use Enumeration in new code?**
A: No. Use Iterator, enhanced for, or Stream. Enumeration is legacy.
