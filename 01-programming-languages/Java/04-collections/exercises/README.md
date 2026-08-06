# Collections Exercises

Practice exercises covering Java Collections: Lists, Sets, Maps, and combined data structures.

---

## List (3 Exercises)

### Exercise 1: Remove Duplicates

**Problem:** Write a method that removes duplicate elements from a list while preserving the original order. Do not use a Set for the solution — implement it using only List operations.

**Expected Behavior:**
```java
removeDuplicates([1, 2, 3, 2, 4, 3, 5]) -> [1, 2, 3, 4, 5]
removeDuplicates(["a", "b", "a", "c", "b"]) -> ["a", "b", "c"]
removeDuplicates([]) -> []
```

**Implementation Hints:**
- Create a new list to store unique elements
- Iterate through the original list
- Only add elements that are not already in the new list
- Time complexity: O(n²) — consider how to optimize with LinkedHashMap

**Solution Reference:** `ListExercises.java` — method `removeDuplicates()`

---

### Exercise 2: Merge Sorted Lists

**Problem:** Given two sorted lists, merge them into a single sorted list without using `Collections.sort()`.

**Expected Behavior:**
```java
mergeSorted([1, 3, 5, 7], [2, 4, 6, 8]) -> [1, 2, 3, 4, 5, 6, 7, 8]
mergeSorted([1, 1, 3], [2, 4, 4]) -> [1, 1, 2, 3, 4, 4]
mergeSorted([], [1, 2, 3]) -> [1, 2, 3]
```

**Implementation Hints:**
- Use two pointers, one for each list
- Compare elements at current pointers
- Add the smaller element to the result and advance that pointer
- Time complexity: O(n + m)

**Solution Reference:** `ListExercises.java` — method `mergeSorted()`

---

### Exercise 3: Find Common Elements

**Problem:** Find common elements between two lists. Return a list containing elements present in both lists (no duplicates).

**Expected Behavior:**
```java
findCommon([1, 2, 3, 4, 5], [3, 4, 5, 6, 7]) -> [3, 4, 5]
findCommon([1, 1, 2, 2], [2, 2, 3, 3]) -> [2]
findCommon([1, 2, 3], [4, 5, 6]) -> []
```

**Implementation Hints:**
- Convert one list to a Set for O(1) lookups
- Iterate through the other list and check membership
- Use a LinkedHashSet to preserve order and avoid duplicates

**Solution Reference:** `ListExercises.java` — method `findCommonElements()`

---

## Set (3 Exercises)

### Exercise 4: Set Operations

**Problem:** Implement union, intersection, and difference operations on two sets.

**Expected Behavior:**
```java
Set<Integer> setA = Set.of(1, 2, 3, 4, 5);
Set<Integer> setB = Set.of(4, 5, 6, 7, 8);

union(setA, setB) -> [1, 2, 3, 4, 5, 6, 7, 8]
intersection(setA, setB) -> [4, 5]
difference(setA, setB) -> [1, 2, 3]
difference(setB, setA) -> [6, 7, 8]
```

**Implementation Hints:**
- Union: Combine both sets (use `addAll()` or streams)
- Intersection: Keep only elements present in both (use `retainAll()`)
- Difference: Remove elements found in the other set (use `removeAll()`)
- Create copies to avoid modifying original sets

**Solution Reference:** `SetExercises.java` — methods `union()`, `intersection()`, `difference()`

---

### Exercise 5: Find Intersection of Multiple Sets

**Problem:** Given a list of sets, find the common elements present in ALL sets.

**Expected Behavior:**
```java
findIntersection([Set.of(1,2,3,4), Set.of(2,3,4,5), Set.of(3,4,5,6)]) -> [3, 4]
findIntersection([Set.of("a","b"), Set.of("b","c"), Set.of("a","b","c")]) -> [b]
findIntersection([Set.of(1), Set.of(2)]) -> []
```

**Implementation Hints:**
- Start with the first set
- Iteratively retain only elements present in each subsequent set
- Handle edge case of empty input list

**Solution Reference:** `SetExercises.java` — method `findMultiSetIntersection()`

---

### Exercise 6: Group by Property

**Problem:** Given a list of objects, group them by a specific property using Sets.

**Expected Behavior:**
```java
List<Student> students = List.of(
    new Student("Alice", "CS"),
    new Student("Bob", "Math"),
    new Student("Charlie", "CS"),
    new Student("Diana", "Math")
);

groupByMajor(students) -> {
    "CS" -> {"Alice", "Charlie"},
    "Math" -> {"Bob", "Diana"}
}
```

**Implementation Hints:**
- Use a `Map<String, Set<Student>>` to store groups
- Iterate through the list and add to appropriate set
- Consider using `computeIfAbsent()` for cleaner code

**Solution Reference:** `SetExercises.java` — method `groupByProperty()`

---

## Map (3 Exercises)

### Exercise 7: Word Frequency Counter

**Problem:** Count the frequency of each word in a given text. Return a map of word to count.

**Expected Behavior:**
```java
countWords("the cat sat on the mat the cat") -> 
    {"the": 3, "cat": 2, "sat": 1, "on": 1, "mat": 1}

countWords("hello HELLO Hello") -> 
    {"hello": 3}  // case-insensitive

countWords("") -> {}
```

**Implementation Hints:**
- Convert text to lowercase and split by whitespace
- Use `Map<String, Integer>` to store counts
- Use `map.getOrDefault(word, 0) + 1` for incrementing

**Solution Reference:** `MapExercises.java` — method `countWordFrequency()`

---

### Exercise 8: Group Anagrams

**Problem:** Given a list of strings, group them by their anagram. Strings are anagrams if they contain the same characters in the same frequency.

**Expected Behavior:**
```java
groupAnagrams(["eat", "tea", "tan", "ate", "nat", "bat"]) -> 
    {
        "aet": ["eat", "tea", "ate"],
        "ant": ["tan", "nat"],
        "abt": ["bat"]
    }

groupAnagrams(["hello", "world"]) -> 
    {
        "ehllo": ["hello"],
        "dlorw": ["world"]
    }
```

**Implementation Hints:**
- Sort characters in each string to create a key
- Use the sorted string as the map key
- Group original strings by their sorted key

**Solution Reference:** `MapExercises.java` — method `groupAnagrams()`

---

### Exercise 9: Top K Elements

**Problem:** Find the top K most frequent elements in a list using a Map.

**Expected Behavior:**
```java
topKElements([1, 1, 1, 2, 2, 3], 2) -> [1, 2]
topKElements(["a", "a", "b", "b", "b", "c"], 1) -> ["b"]
topKElements([1, 2, 3], 5) -> [1, 2, 3]  // k > unique elements
```

**Implementation Hints:**
- Count frequencies using a Map
- Sort entries by frequency in descending order
- Return the top K entries
- Consider using `PriorityQueue` for efficiency

**Solution Reference:** `MapExercises.java` — method `topKElements()`

---

## Combined (1 Exercise)

### Exercise 10: Phone Book Implementation

**Problem:** Implement a phone book using Maps and Lists. Support adding contacts, searching by name, searching by number, and grouping by first letter.

**Expected Behavior:**
```java
PhoneBook phoneBook = new PhoneBook();
phoneBook.addContact("Alice", "555-0101");
phoneBook.addContact("Bob", "555-0102");
phoneBook.addContact("Charlie", "555-0103");
phoneBook.addContact("Alice Smith", "555-0104");

phoneBook.searchByName("Alice");           // [Alice: 555-0101, Alice Smith: 555-0104]
phoneBook.searchByNumber("555-0102");      // Bob
phoneBook.groupByFirstLetter();            // {A: [Alice, Alice Smith], B: [Bob], C: [Charlie]}
phoneBook.getContactCount();               // 4
```

**Classes to Create:**
- `Contact` — name and phone number
- `PhoneBook` — main phone book class

**Implementation Hints:**
- Use `Map<String, List<Contact>>` for letter grouping
- Use `Map<String, Contact>` for number lookup
- Implement search with `containsIgnoreCase()`
- Handle duplicate names gracefully

**Solution Reference:** `CombinedExercises.java` — class `PhoneBook`

---

## Solutions

All solutions are provided in the `solutions/` directory. Each file contains complete implementations with test cases.

```bash
javac -d out exercises/collections/*.java
java -cp out exercises.collections.ListExercises
```
