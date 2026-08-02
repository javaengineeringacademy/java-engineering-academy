# HashSet

## 1. Introduction

HashSet is the most commonly used implementation of the `Set` interface. It uses a `HashMap` internally to store elements, providing O(1) average-time performance for add, remove, and contains operations. HashSet does not allow duplicate elements and makes no guarantees about the iteration order.

HashSet is the default choice for most Set use cases because it provides the fastest lookup performance. It allows one null element and is not thread-safe. The iteration order is unpredictable and may change over time as the internal hash table is resized.

Understanding HashSet is essential because it's used everywhere: removing duplicates, membership testing, set operations (union, intersection, difference), and as building blocks for more complex data structures.

## 2. Learning Objectives

- Create and use HashSet with generics
- Understand that HashSet uses HashMap internally
- Learn about hash-based set operations (add, remove, contains)
- Understand that HashSet does not maintain insertion order
- Compare HashSet vs LinkedHashSet vs TreeSet
- Learn about null element handling
- Understand thread-safety considerations
- Master set operations: union, intersection, difference

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 15: HashMap (understand hash table internals)
- Understanding of hashCode() and equals() contracts

## 4. Why This Concept Exists

Before HashSet, developers used:
1. **Manual array checking**: O(n) for each `contains()` check
2. **Hashtable**: Synchronized, slow, legacy
3. **TreeSet**: O(log n) for all operations

HashSet provides:
1. **O(1) performance**: For add, remove, and contains
2. **No duplicates**: Automatically prevents duplicate elements
3. **Simplicity**: Easy to use API
4. **Null support**: Allows one null element

HashSet is essential for:
- Removing duplicates from collections
- Fast membership testing
- Set operations (union, intersection, difference)
- Tracking unique elements

## 5. Problem Statement

Consider building a tag system for a blog:
- Each post can have multiple tags
- Tags must be unique per post
- Fast lookup to check if a tag exists
- Quick addition and removal of tags

Without HashSet, you'd need:
- A List with manual duplicate checking: O(n) for each check
- Or a sorted array with binary search: O(log n) but requires sorting

With HashSet, all operations are O(1) average case, and duplicates are automatically prevented.

## 6. Theory

### Internal Structure

HashSet uses a HashMap internally:

```java
private transient HashMap<E,Object> map;

// All values are the same shared object
private static final Object PRESENT = new Object();
```

When you add an element to HashSet:
```java
public boolean add(E e) {
    return map.put(e, PRESENT) == null;
}
```

The element becomes the key in the HashMap, and all values are the shared `PRESENT` object.

### Hash Distribution

HashSet relies on the `hashCode()` method of elements for bucket placement. Good hash codes distribute elements uniformly across the hash table, minimizing collisions.

### Load Factor and Resizing

HashSet uses the same load factor and resizing mechanism as HashMap:
- Default load factor: 0.75
- When `size > capacity * loadFactor`, the table is resized
- New capacity = old capacity * 2

## 7. Internal Working

### The add() Operation

```java
public boolean add(E e) {
    return map.put(e, PRESENT) == null;
}

// HashMap.put() returns null if key is new, old value if key exists
// Since all values are PRESENT, we check if return is null
// null return means key was added (new element)
// PRESENT return means key existed (duplicate)
```

### The contains() Operation

```java
public boolean contains(Object o) {
    return map.containsKey(o);
}

// HashMap.containsKey() is O(1) average
```

### The remove() Operation

```java
public boolean remove(Object o) {
    return map.remove(o) == PRESENT;
}

// HashMap.remove() returns the value if key was present
// We check if it was PRESENT to confirm removal
```

## 8. JVM Perspective

### Memory Allocation

```java
HashSet<String> set = new HashSet<>();
// JVM allocates:
// - HashSet object header: 12 bytes
// - map reference: 8 bytes
// Total HashSet object: ~24 bytes

// When adding elements:
// - HashMap with 16 buckets: ~128 bytes
// - Each Entry: ~40 bytes (hash, key, value, next)
// - Each element: varies (String object)
```

### JIT Optimization

The JIT compiler optimizes HashSet operations by:
- Inlining HashMap methods
- Optimizing hash calculations
- Eliminating redundant null checks

## 9. Memory Representation

```
HashSet<String> set = new HashSet<>();
set.add("Apple");
set.add("Banana");
set.add("Cherry");

Memory layout:
┌───────────────────────────────┐
│ HashSet object                │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ map ──────────────────────────┐
└───────────────────────────────┘
                                │
                                ▼
                         HashMap<String, Object>
                         ┌────────────────────────┐
                         │ table → Entry[]         │
                         │ size = 3                │
                         └────────────────────────┘
                                    │
                                    ▼
                         Entry[] bucket array:
                         ┌────────────────────────┐
                         │ [0] → null             │
                         │ [1] → null             │
                         │ [2] → Entry("Apple")   │
                         │ [3] → null             │
                         │ [4] → Entry("Banana")  │
                         │ [5] → null             │
                         │ [6] → Entry("Cherry")  │
                         │ ...                    │
                         └────────────────────────┘

Entry("Apple"):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ hash (int, 4 bytes)         │
│ key → String "Apple"        │
│ value → Object PRESENT      │
│ next → null                 │
└─────────────────────────────┘
```

## 10. Syntax

```java
import java.util.HashSet;
import java.util.Set;

// ============================================
// CREATION
// ============================================
Set<String> set = new HashSet<>();
Set<String> set = new HashSet<>(16);           // Initial capacity
Set<String> set = new HashSet<>(16, 0.75f);   // Capacity and load factor
Set<String> set = new HashSet<>(otherCollection); // From collection
Set<String> immutable = Set.of("A", "B", "C"); // Java 9+

// ============================================
// ADDING ELEMENTS
// ============================================
set.add("element");              // Returns true if added
set.addAll(collection);          // Add all from collection

// ============================================
// REMOVING ELEMENTS
// ============================================
set.remove("element");           // Returns true if removed
set.removeIf(predicate);         // Conditional removal
set.clear();                     // Remove all

// ============================================
// CHECKING
// ============================================
boolean has = set.contains("element");  // O(1) average
boolean empty = set.isEmpty();
int size = set.size();

// ============================================
// SET OPERATIONS
// ============================================
// Union
Set<String> union = new HashSet<>(set1);
union.addAll(set2);

// Intersection
Set<String> intersection = new HashSet<>(set1);
intersection.retainAll(set2);

// Difference
Set<String> difference = new HashSet<>(set1);
difference.removeAll(set2);

// Symmetric Difference
Set<String> symDiff = new HashSet<>(set1);
symDiff.addAll(set2);
Set<String> common = new HashSet<>(set1);
common.retainAll(set2);
symDiff.removeAll(common);

// ============================================
// CONVERSIONS
// ============================================
String[] array = set.toArray(new String[0]);
List<String> list = new ArrayList<>(set);

// ============================================
// ITERATION
// ============================================
for (String element : set) {
    System.out.println(element);
}

set.forEach(System.out::println);

Iterator<String> it = set.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}
```

## 11. Easy Example

```java
import java.util.HashSet;
import java.util.Set;

public class HashSetBasics {
    public static void main(String[] args) {
        // Create and populate
        Set<String> fruits = new HashSet<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate ignored

        System.out.println("Set: " + fruits);
        System.out.println("Size: " + fruits.size());

        // Check if contains
        System.out.println("Contains Apple: " + fruits.contains("Apple"));
        System.out.println("Contains Grape: " + fruits.contains("Grape"));

        // Remove
        fruits.remove("Banana");
        System.out.println("After removing Banana: " + fruits);

        // Iterate
        System.out.print("Iterating: ");
        for (String fruit : fruits) {
            System.out.print(fruit + " ");
        }
        System.out.println();
    }
}
```

## 12. Medium Example

```java
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class HashSetOperations {
    public static void main(String[] args) {
        // Remove duplicates from list
        List<Integer> numbersWithDuplicates = List.of(1, 2, 3, 1, 2, 4, 5, 3);
        Set<Integer> uniqueNumbers = new HashSet<>(numbersWithDuplicates);
        System.out.println("Original: " + numbersWithDuplicates);
        System.out.println("Unique: " + uniqueNumbers);

        // Set operations
        Set<String> set1 = new HashSet<>(Set.of("A", "B", "C", "D"));
        Set<String> set2 = new HashSet<>(Set.of("C", "D", "E", "F"));

        // Union
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);

        // Intersection
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);

        // Difference
        Set<String> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference (set1 - set2): " + difference);

        // Find common elements
        List<String> list1 = List.of("A", "B", "C", "D");
        List<String> list2 = List.of("C", "D", "E", "F");
        Set<String> common = new HashSet<>(list1);
        common.retainAll(list2);
        System.out.println("Common elements: " + common);
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedHashSet {
    public static void main(String[] args) {
        // Pattern 1: Custom hashCode/equals
        System.out.println("=== Custom Objects ===");
        Set<Employee> employees = new HashSet<>();
        employees.add(new Employee(1, "Alice", "Engineering"));
        employees.add(new Employee(2, "Bob", "Marketing"));
        employees.add(new Employee(1, "Alice", "Management")); // Replaces
        employees.forEach(e -> System.out.println("  " + e.name()));

        // Pattern 2: Set-based deduplication
        System.out.println("\n=== Deduplication ===");
        List<Transaction> transactions = List.of(
            new Transaction("T1", 100),
            new Transaction("T2", 200),
            new Transaction("T1", 100), // Duplicate
            new Transaction("T3", 300)
        );
        Set<Transaction> unique = new HashSet<>(transactions);
        System.out.println("Unique transactions: " + unique.size());

        // Pattern 3: Find missing numbers
        System.out.println("\n=== Missing Numbers ===");
        Set<Integer> allNumbers = new HashSet<>(IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toSet()));
        Set<Integer> present = Set.of(1, 2, 4, 6, 7, 9);
        allNumbers.removeAll(present);
        System.out.println("Missing: " + allNumbers);

        // Pattern 4: Anagram groups
        System.out.println("\n=== Anagram Groups ===");
        String[] words = {"listen", "silent", "enlist", "hello", "olleh"};
        Map<String, Set<String>> anagramGroups = groupAnagrams(words);
        anagramGroups.forEach((key, group) ->
            System.out.println("  " + key + ": " + group)
        );
    }

    static Map<String, Set<String>> groupAnagrams(String[] words) {
        Map<String, Set<String>> groups = new HashMap<>();
        for (String word : words) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);
            groups.computeIfAbsent(sorted, k -> new HashSet<>()).add(word);
        }
        return groups;
    }

    record Employee(int id, String name, String department) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return id == employee.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    record Transaction(String id, double amount) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Transaction that = (Transaction) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TagManager {
    private final Map<String, Set<String>> contentTags; // contentId -> tags
    private final Map<String, Set<String>> tagContents; // tag -> contentIds

    public TagManager() {
        this.contentTags = new ConcurrentHashMap<>();
        this.tagContents = new ConcurrentHashMap<>();
    }

    public void addTag(String contentId, String tag) {
        contentTags.computeIfAbsent(contentId, k -> ConcurrentHashMap.newKeySet()).add(tag);
        tagContents.computeIfAbsent(tag, k -> ConcurrentHashMap.newKeySet()).add(contentId);
    }

    public void removeTag(String contentId, String tag) {
        Set<String> tags = contentTags.get(contentId);
        if (tags != null) {
            tags.remove(tag);
        }
        Set<String> contents = tagContents.get(tag);
        if (contents != null) {
            contents.remove(contentId);
        }
    }

    public Set<String> getTags(String contentId) {
        return contentTags.getOrDefault(contentId, Set.of());
    }

    public Set<String> getContentsByTag(String tag) {
        return tagContents.getOrDefault(tag, Set.of());
    }

    public Set<String> getCommonTags(String contentId1, String contentId2) {
        Set<String> tags1 = new HashSet<>(getTags(contentId1));
        Set<String> tags2 = getTags(contentId2);
        tags1.retainAll(tags2);
        return tags1;
    }

    public Map<String, Long> getTagFrequency() {
        return tagContents.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> (long) e.getValue().size()
            ));
    }

    public static void main(String[] args) {
        TagManager manager = new TagManager();

        manager.addTag("article1", "java");
        manager.addTag("article1", "programming");
        manager.addTag("article2", "java");
        manager.addTag("article2", "tutorial");
        manager.addTag("article3", "python");
        manager.addTag("article3", "programming");

        System.out.println("=== Tags for article1 ===");
        manager.getTags("article1").forEach(tag ->
            System.out.println("  " + tag)
        );

        System.out.println("\n=== Contents with 'java' tag ===");
        manager.getContentsByTag("java").forEach(content ->
            System.out.println("  " + content)
        );

        System.out.println("\n=== Common tags (article1 & article2) ===");
        manager.getCommonTags("article1", "article2").forEach(tag ->
            System.out.println("  " + tag)
        );

        System.out.println("\n=== Tag frequency ===");
        manager.getTagFrequency().forEach((tag, count) ->
            System.out.printf("  %s: %d contents%n", tag, count)
        );
    }
}
```

## 15. Performance

### Time Complexity

| Operation | Average | Worst Case | Notes |
|-----------|---------|------------|-------|
| add() | O(1) | O(n) | With hash collisions |
| remove() | O(1) | O(n) | With hash collisions |
| contains() | O(1) | O(n) | With hash collisions |
| size() | O(1) | O(1) | Field access |
| iteration | O(n) | O(n) | All elements |

### HashSet vs LinkedHashSet vs TreeSet

| Feature | HashSet | LinkedHashSet | TreeSet |
|---------|---------|---------------|---------|
| Structure | Hash table | Hash table + linked list | Red-black tree |
| Ordering | None | Insertion/access | Sorted |
| add() | O(1) | O(1) | O(log n) |
| remove() | O(1) | O(1) | O(log n) |
| contains() | O(1) | O(1) | O(log n) |
| Memory | Less | More | More |
| Null elements | One | One | None |

### Memory Usage

| Collection | Per Element Overhead |
|------------|---------------------|
| HashSet | ~32 bytes (Entry object) |
| LinkedHashSet | ~40 bytes (Entry + linked list pointers) |
| TreeSet | ~40 bytes (Entry + tree pointers) |

## 16. Best Practices

1. **Override hashCode/equals**: For custom objects in HashSet
2. **Set initial capacity**: For known sizes to avoid resizing
3. **Use LinkedHashSet**: When insertion order matters
4. **Use TreeSet**: When sorted order is required
5. **Prefer HashSet**: For most use cases (fastest)
6. **Use Set.of()**: For immutable sets
7. **Thread safety**: Use `ConcurrentHashMap.newKeySet()` for concurrent access

## 17. Common Mistakes

```java
// Mistake 1: Not overriding hashCode/equals for custom objects
class BadKey {
    String value;
    // Missing equals() and hashCode()!
}
Set<BadKey> set = new HashSet<>();
BadKey key1 = new BadKey("test");
BadKey key2 = new BadKey("test");
set.add(key1);
set.add(key2); // Added! Different objects (reference equality)

// Mistake 2: Using mutable objects as elements
List<String> key = new ArrayList<>(List.of("a"));
Set<List<String>> set = new HashSet<>();
set.add(key);
key.add("b"); // Changes hashCode!
set.contains(key); // May return false!

// Mistake 3: Assuming iteration order
Set<String> set = Set.of("C", "A", "B");
// Don't assume order is [C, A, B] or [A, B, C]

// Mistake 4: Using HashSet when order matters
// Bad
Set<String> set = new HashSet<>();
// Good
Set<String> set = new LinkedHashSet<>();
```

## 18. Pitfalls

### No Ordering Guarantees
HashSet does not maintain any order. If you need insertion order, use LinkedHashSet. If you need sorted order, use TreeSet.

### Thread Safety
HashSet is NOT thread-safe. Use `Collections.synchronizedSet()` or `ConcurrentHashMap.newKeySet()` for concurrent access.

### Null Elements
HashSet allows one null element. This can cause issues with some algorithms. Consider filtering nulls explicitly.

### Memory Overhead
HashSet has more overhead than a simple array due to the backing HashMap. For small, fixed-size sets, consider using an EnumSet.

### Hash Collisions
Poor hashCode() implementations can cause all elements to collide in one bucket, degrading performance to O(n).

## 19. Debugging Tips

1. **Override toString()**: For custom objects in HashSet
2. **Check hashCode distribution**: Use debugger to inspect bucket distribution
3. **Monitor size**: Verify no unexpected duplicates
4. **Use assertions**: Check set invariants
5. **Profile memory**: Use JProfiler to check HashSet memory usage
6. **Test with multiple threads**: Verify thread safety

## 20. Comparison Table

| Feature | HashSet | LinkedHashSet | TreeSet | EnumSet |
|---------|---------|---------------|---------|---------|
| Structure | Hash table | Hash table + linked list | Red-black tree | Bit vector |
| Ordering | None | Insertion/access | Sorted | Enum natural |
| Performance | O(1) | O(1) | O(log n) | O(1) |
| Null elements | One | One | None | None |
| Memory | Less | More | More | Least |
| Thread-safe | No | No | No | No |

## 21. Decision Tree

```
Need a Set?
├── Yes → Need sorted elements?
│   ├── Yes → TreeSet
│   └── No → Need insertion order?
│       ├── Yes → LinkedHashSet
│       └── No → HashSet (default)
├── Need enum elements?
│   └── Use EnumSet
└── Need thread safety?
    └── Use ConcurrentHashMap.newKeySet()
```

## 22. Interview Questions

### Q1: How does HashSet use HashMap internally?
**A**: HashSet maintains a HashMap where elements are keys and all values are a shared `PRESENT` object. `add()` calls `map.put(e, PRESENT)` and checks if the return is null (new element).

### Q2: What happens when two elements have the same hashCode?
**A**: They are stored in the same bucket (linked list or tree in Java 8+). The `equals()` method determines if they are the same element. This is why both hashCode() and equals() must be overridden.

### Q3: Can HashSet contain null elements?
**A**: Yes, HashSet allows one null element. The null key is placed in bucket 0.

### Q4: What is the difference between HashSet and LinkedHashSet?
**A**: HashSet uses a plain HashMap (no order). LinkedHashSet uses a LinkedHashMap (maintains insertion order). Both provide O(1) operations.

### Q5: When would you use TreeSet over HashSet?
**A**: When you need elements in sorted order, need range queries (subSet, headSet, tailSet), or need guaranteed O(log n) performance.

### Q6: How do you remove duplicates from a List?
**A**: `new ArrayList<>(new HashSet<>(list))` removes duplicates but doesn't preserve order. For ordered deduplication, use `LinkedHashSet` or Stream API with `distinct()`.

### Q7: Is HashSet thread-safe?
**A**: No. Use `Collections.synchronizedSet()` or `ConcurrentHashMap.newKeySet()` for concurrent access. For high concurrency, consider `CopyOnWriteArraySet`.

## 23. Exercises

### Exercise 1: Remove Duplicates
Write a method that removes duplicates from a List while preserving order.

### Exercise 2: Set Operations
Implement union, intersection, and difference operations for two Sets.

### Exercise 3: Find Missing Elements
Given two Lists, find elements present in one but not the other.

### Exercise 4: Anagram Groups
Group a list of words into anagrams using HashSet.

## 24. Assignments

### Assignment 1: Tag System
Build a tag management system using HashSet:
- Add/remove tags to content
- Find content by tag
- Find common tags between content items
- Export tag statistics

### Assignment 2: Unique Visitor Tracker
Create a visitor tracking system:
- Track unique visitors by IP
- Calculate unique visitor count
- Find most common visitors
- Export visitor data

## 25. Mini Project

### Spell Checker

Build a spell checker using HashSet:

```java
// Features:
// 1. Load dictionary into HashSet
// 2. Check if word is spelled correctly
// 3. Suggest corrections for misspelled words
// 4. Add custom words to dictionary
// 5. Track word frequency
```

**Requirements:**
- Use HashSet for dictionary storage
- Implement edit distance algorithm for suggestions
- Handle large dictionaries efficiently
- Support adding/removing words

## 26. Summary

HashSet is the most commonly used Set implementation:

- **Internal structure**: HashMap with shared PRESENT object
- **Performance**: O(1) for add, remove, contains
- **Ordering**: None (hash-based)
- **Null elements**: One allowed
- **Best for**: Fast membership testing, deduplication
- **Avoid for**: When order matters (use LinkedHashSet) or sorted order needed (use TreeSet)

## 27. References

### Official Documentation
- [HashSet JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)
- [Set Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html)

### Books
- *Effective Java* by Joshua Bloch

### Online Resources
- [Baeldung HashSet Guide](https://www.baeldung.com/java-hashset)
- [GeeksforGeeks HashSet](https://www.geeksforgeeks.org/hashset-in-java/)

### Related Topics
- [LinkedHashSet](../12-linkedhashset/README.md)
- [TreeSet](../13-treeset/README.md)
- [HashMap](../15-hashmap/README.md)
