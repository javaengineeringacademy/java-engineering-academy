# Collections Framework Quiz

## Question 1 (MCQ)
What is the time complexity of get() and set() operations on ArrayList?
- A) O(n)
- B) O(log n)
- C) O(1)
- D) O(n²)

**Answer: C**
**Explanation:** ArrayList is backed by an array, so index-based access (get and set) is O(1) since the memory address can be calculated directly.

---

## Question 2 (MCQ)
Which collection maintains insertion order and does not allow duplicates?
- A) HashSet
- B) TreeSet
- C) LinkedHashSet
- D) PriorityQueue

**Answer: C**
**Explanation:** LinkedHashSet uses a linked list to maintain insertion order while using a hash table for O(1) lookups. HashSet is unordered, TreeSet is sorted, and PriorityQueue has no guaranteed order.

---

## Question 3 (MCQ)
What is the default load factor of a HashMap?
- A) 0.5
- B) 0.75
- C) 1.0
- D) 0.25

**Answer: B**
**Explanation:** The default load factor is 0.75, meaning the HashMap resizes when 75% of buckets are filled. This balances memory usage and collision probability.

---

## Question 4 (MCQ)
When should you use a LinkedList over an ArrayList?
- A) For random access by index
- B) For frequent insertions and deletions at the beginning
- C) For iterating over elements
- D) For storing primitive types

**Answer: B**
**Explanation:** LinkedList has O(1) insertion/deletion at the head (no element shifting), while ArrayList requires O(n) shifting. However, ArrayList is preferred for random access due to O(1) index operations.

---

## Question 5 (Code Output)
What does this code print?

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("C", "A", "B"));
        Collections.sort(list);
        System.out.println(list);

        Set<String> set = new TreeSet<>(list);
        System.out.println(set);
    }
}
```

**Answer:** [A, B, C] and [A, B, C]
**Explanation:** `Collections.sort()` sorts ArrayList in natural order. `TreeSet` maintains elements in sorted (natural) order. Both produce [A, B, C].

---

## Question 6 (Code Output)
What does this code print?

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 90);
        map.put("Bob", 85);
        map.put("Charlie", 92);
        map.put("Alice", 95);

        System.out.println(map.get("Alice"));
        System.out.println(map.size());
    }
}
```

**Answer:** 95 and 3
**Explanation:** HashMap allows only one value per key. The second `put("Alice", 95)` overwrites the first value. So `get("Alice")` returns 95, and the map has 3 entries.

---

## Question 7 (Bug Finding)
Find the bug:

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
        for (String s : list) {
            if (s.equals("B")) {
                list.remove(s);
            }
        }
        System.out.println(list);
    }
}
```

**Bug:** Modifying a collection during for-each iteration throws `ConcurrentModificationException`. The for-each loop uses an iterator internally, and structural modifications invalidate it.
**Fix:** Use an iterator explicitly:
```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("B")) {
        it.remove();
    }
}
```
Or use `list.removeIf(s -> s.equals("B"))` (Java 8+).

---

## Question 8 (Bug Finding)
Find the bug:

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("hello");
        set.add("Hello");
        set.add("HELLO");
        System.out.println(set.size());
    }
}
```

**Bug:** HashSet is case-sensitive. The bug is not in the code itself but in the developer's assumption — if the intent was to store unique words regardless of case, this code produces 3 entries instead of 1.
**Fix:** If case-insensitive matching is desired, use a TreeSet with a case-insensitive comparator:
```java
Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
```

---

## Question 9 (Scenario-based)
You need a collection to store student records where you frequently need to find students by ID, iterate in insertion order, and occasionally sort by name. Which approach is best?

- A) Use TreeMap with student ID as key
- B) Use LinkedHashMap for insertion order, maintain a separate TreeMap for sorted access
- C) Use a single ArrayList with manual searching
- D) Use HashSet for fast lookup

**Answer: B**
**Explanation:** LinkedHashMap preserves insertion order with O(1) lookup. A separate TreeMap provides sorted access. This composite approach optimizes for the different access patterns without compromising on any requirement.

---

## Question 10 (Architecture Decision)
You are building a caching system that needs to: (1) store up to 10,000 entries, (2) evict the least recently used entries when full, (3) support O(1) get and put. Which data structure should you use?

- A) HashMap
- B) LinkedHashMap with accessOrder=true
- C) TreeMap
- D) ArrayList

**Answer: B**
**Explanation:** LinkedHashMap with `accessOrder=true` maintains access order, making it suitable for LRU cache implementation. Override `removeEldestEntry()` to automatically evict the oldest entry when size exceeds the threshold. This provides O(1) get/put with LRU eviction.
