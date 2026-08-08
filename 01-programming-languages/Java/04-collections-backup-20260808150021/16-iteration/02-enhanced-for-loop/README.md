# Enhanced For Loop — Syntactic Sugar

## Why Enhanced For Loop Exists

Java 5 introduced the enhanced for loop (`for (Type var : collection)`) to eliminate boilerplate when you just need to visit every element. It's cleaner, safer, and communicates intent better.

**Production incident:** A legacy system had 2,000+ `for (int i = 0; i < list.size(); i++)` loops that were all just doing `process(list.get(i))`. Migrating to enhanced for reduced codebase by 15% and eliminated 47 index-related bugs.

## The Pain Point

Writing `for (int i = 0; i < collection.size(); i++)` when you don't need the index is:
- Verbose
- Error-prone (off-by-one)
- Hides intent (are you just visiting elements?)
- Requires different syntax for arrays vs Collections

Enhanced for unifies all iteration with one clean syntax.

## Basic Syntax

```java
// Arrays
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}

// Collections
List<String> names = List.of("Alice", "Bob", "Charlie");
for (String name : names) {
    System.out.println(name);
}

// Sets
Set<Integer> set = Set.of(1, 2, 3);
for (int value : set) {
    System.out.println(value);
}

// Maps (iterate entries)
Map<String, Integer> map = Map.of("a", 1, "b", 2);
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

// Iterating just keys
for (String key : map.keySet()) {
    System.out.println(key);
}

// Iterating just values
for (Integer value : map.values()) {
    System.out.println(value);
}
```

## Under the Hood: It's Just Iterator

The enhanced for loop is **syntactic sugar** — the compiler transforms it into an Iterator-based loop. This is critical to understand.

```java
// What you write:
for (String name : names) {
    process(name);
}

// What the compiler generates:
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    String name = it.next();
    process(name);
}
```

### Bytecode Proof

```bash
# Compile and decompile
javac EnhancedForLoopDemo.java
javap -c EnhancedForLoopDemo.class

# You'll see:
#   invokeinterface #iterator()
#   invokeinterface #hasNext()
#   invokeinterface #next()
```

This is why enhanced for throws `ConcurrentModificationException` — it uses Iterator under the hood.

## Cannot Modify Collection During Iteration

```java
// THIS WILL THROW ConcurrentModificationException
List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
for (String name : names) {
    if (name.equals("Bob")) {
        names.remove(name);  // CME! Modifies underlying collection
    }
}
```

**Why?** The Iterator checks a `modCount` (modification count) at each `next()` call. If the collection was modified outside the Iterator, it detects tampering and throws.

```java
// What happens internally:
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    String name = it.next();  // Checks modCount here
    // If collection was modified since last check → CME
}
```

## Cannot Get Index

```java
// If you need the index, you MUST use traditional for loop
for (int i = 0; i < list.size(); i++) {
    System.out.println("Element " + i + ": " + list.get(i));
}

// Enhanced for gives you no way to know the position
for (String name : names) {
    // What's the index? No way to know!
}
```

### Workaround: Use AtomicInteger

```java
AtomicInteger index = new AtomicInteger(0);
for (String name : names) {
    System.out.println("Element " + index.getAndIncrement() + ": " + name);
}
```

## Works with Arrays and Iterable

```java
// ANY type that implements Iterable<T> works
public class Sentence implements Iterable<String> {
    private String[] words;

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(words).iterator();
    }
}

// Now you can use enhanced for
Sentence sentence = new Sentence("Hello World");
for (String word : sentence) {
    System.out.println(word);
}
```

## When to Use / When NOT to Use

### ✅ USE Enhanced For When:
- You only need the values (no index)
- Simple forward traversal
- Working with any Iterable (List, Set, custom)
- Code clarity is important
- You want compile-time safety (no index errors)

### ❌ DON'T Use Enhanced For When:
- You need the index → use `for (int i = 0; ...)`
- You need to modify the collection → use `Iterator` or `removeIf()`
- You need to go backwards → use `ListIterator` or reverse `for`
- Performance-critical code with LinkedList → use `Iterator` directly

## Common Mistakes

### Mistake 1: ConcurrentModificationException
```java
// WRONG: modifying collection during enhanced for
for (User user : users) {
    if (user.isBanned()) {
        users.remove(user);  // CME!
    }
}

// RIGHT: use Iterator explicitly
Iterator<User> it = users.iterator();
while (it.hasNext()) {
    User user = it.next();
    if (user.isBanned()) {
        it.remove();  // Safe!
    }
}

// BETTER: use removeIf (Java 8+)
users.removeIf(User::isBanned);
```

### Mistake 2: Confusing with Arrays
```java
// WRONG: enhanced for doesn't modify the array
int[] nums = {1, 2, 3, 4, 5};
for (int num : nums) {
    num = num * 2;  // Only modifies local variable, NOT the array!
}
// nums is still {1, 2, 3, 4, 5}

// RIGHT: use traditional for loop for array modification
for (int i = 0; i < nums.length; i++) {
    nums[i] = nums[i] * 2;
}
```

### Mistake 3: Null Collection
```java
// WRONG: NullPointerException
List<String> names = getNames();  // might return null
for (String name : names) {  // NPE if names is null!
    System.out.println(name);
}

// RIGHT: null check first
List<String> names = getNames();
if (names != null) {
    for (String name : names) {
        System.out.println(name);
    }
}
```

### Mistake 4: Type Mismatch
```java
// WRONG: Object type, requires casting
for (Object obj : list) {
    String s = (String) obj;  // ClassCastException if not String
}

// RIGHT: use correct generic type
for (String s : list) {
    System.out.println(s);
}
```

## Performance Comparison

```
Mechanism          │ ArrayList │ LinkedList │ HashSet
───────────────────┼───────────┼────────────┼─────────
for (int i=0;...)  │ O(n)      │ O(n²)*     │ N/A
enhanced for       │ O(n)      │ O(n)       │ O(n)
Iterator explicit  │ O(n)      │ O(n)       │ O(n)

* LinkedList.get(i) is O(n), making total O(n²)
```

Enhanced for and Iterator have identical performance — both use Iterator internally.

## Interview Questions

**Q: What's the bytecode equivalent of enhanced for?**
A: It compiles to Iterator usage: `iterator()`, `hasNext()`, `next()`.

**Q: Why can't you modify a collection during enhanced for?**
A: The Iterator checks `modCount` at each `next()`. Collection modification outside Iterator invalidates it.

**Q: Can enhanced for iterate over a Map directly?**
A: No. You must iterate over `entrySet()`, `keySet()`, or `values()`.

**Q: Does enhanced for work with arrays?**
A: Yes, but it creates a copy of the element — modifying the loop variable doesn't change the array.

**Q: What's the difference between enhanced for and Iterator?**
A: Functionally identical, but Iterator gives you `remove()` and is explicit. Enhanced for is cleaner when you don't need modification.
