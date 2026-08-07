# Collections Framework Quiz

## Question 1 (Production Scenario)
Your application receives one million concurrent HTTP requests. Each request needs to look up user session data by session ID. The session data is read-heavy (95% reads, 5% writes). Which collection should you choose?

- A) `ArrayList` for fast index-based access
- B) `ConcurrentHashMap` for thread-safe O(1) lookups without full synchronization
- C) `Collections.synchronizedMap(new HashMap<>())` for simplicity
- D) `TreeMap` for sorted access by session ID

**Answer: B**
**Explanation:** `ConcurrentHashMap` uses segment locking (lock striping), allowing concurrent reads without locking the entire map. `Collections.synchronizedMap` locks the entire map for every operation, creating a bottleneck under high concurrency. `ArrayList` doesn't support key-based lookups. `TreeMap` has O(log n) lookup, slower than O(1).

---

## Question 2 (Production Scenario)
Your e-commerce platform needs to display product search results. Users expect results to appear in the order they were added to the catalog, but the system must also prevent duplicate product entries. Which collection is appropriate?

- A) `HashSet` for O(1) lookups
- B) `LinkedHashSet` for insertion order with uniqueness
- C) `TreeSet` for sorted order
- D) `ArrayList` with manual duplicate checking

**Answer: B**
**Explanation:** `LinkedHashSet` maintains insertion order while guaranteeing uniqueness via hash-based equality checks. `HashSet` doesn't preserve order. `TreeSet` sorts alphabetically (not insertion order). `ArrayList` requires O(n) duplicate checking per insertion. For display-critical ordering with deduplication, `LinkedHashSet` is the right choice.

---

## Question 3 (Debugging)
A production system throws `ConcurrentModificationException` under load. The code iterates over a `HashMap` while another thread removes entries. The developer used:

```java
for (Map.Entry<String, Object> entry : map.entrySet()) {
    if (isExpired(entry.getValue())) {
        map.remove(entry.getKey());
    }
}
```

What is the bug and the fix?

- A) Use `HashMap` instead of `ConcurrentHashMap`
- B) Use `ConcurrentHashMap` with `forEach` and `remove()` which is safe for concurrent modification
- C) Add `synchronized` around the loop
- D) Use `Iterator.remove()` with a regular HashMap

**Answer: B**
**Explanation:** `ConcurrentHashMap.forEach()` + `remove()` is safe because `ConcurrentHashMap` supports concurrent modification during iteration (weakly consistent). A regular `HashMap` throws `ConcurrentModificationException`. `Iterator.remove()` works but is single-threaded. For concurrent access, `ConcurrentHashMap` is the correct choice.

---

## Question 4 (Production Scenario)
You need a collection to store student records where you frequently need to find students by ID, iterate in insertion order, and occasionally sort by name. Which approach is best?

- A) Use `TreeMap` with student ID as key
- B) Use `LinkedHashMap` for insertion order, maintain a separate `TreeMap` for sorted access
- C) Use a single `ArrayList` with manual searching
- D) Use `HashSet` for fast lookup

**Answer: B**
**Explanation:** `LinkedHashMap` preserves insertion order with O(1) lookup. A separate `TreeMap` provides sorted access. This composite approach optimizes for the different access patterns without compromising on any requirement.

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

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Charlie", 3);
        map.put("Alice", 1);
        map.put("Bob", 2);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.print(entry.getKey() + " ");
        }
    }
}
```

A) Alice Bob Charlie
B) Charlie Alice Bob
C) Bob Alice Charlie
D) Output is unpredictable (depends on hash implementation)

**Answer: D**
**Explanation:** HashMap does not guarantee any iteration order. The order depends on the hash bucket arrangement and may change with different JVM implementations or versions. The output could be any permutation of the three keys.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> linked = new LinkedHashMap<>();
        linked.put("C", 3);
        linked.put("A", 1);
        linked.put("B", 2);

        Map<String, Integer> tree = new TreeMap<>();
        tree.put("C", 3);
        tree.put("A", 1);
        tree.put("B", 2);

        System.out.print(linked.keySet() + " ");
        System.out.print(tree.keySet());
    }
}
```

A) [C, A, B] [A, B, C]
B) [A, B, C] [A, B, C]
C) [C, A, B] [C, A, B]
D) [A, B, C] [C, A, B]

**Answer: A**
**Explanation:** LinkedHashMap preserves insertion order, so the keys are printed in the order they were added: [C, A, B]. TreeMap sorts keys by natural order (alphabetical), so the keys are [A, B, C].

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("A", "B", "C");
        List<String> unmodifiable = Collections.unmodifiableList(list);

        System.out.println(unmodifiable.size());
        unmodifiable.add("D");
    }
}
```

A) 3
B) 4
C) UnsupportedOperationException
D) 3 followed by UnsupportedOperationException

**Answer: D**
**Explanation:** `Collections.unmodifiableList()` returns a view that throws `UnsupportedOperationException` on modification attempts. `size()` works fine and returns 3. The `add("D")` call throws `UnsupportedOperationException` at runtime because the list is unmodifiable. Output: prints 3, then throws exception.

