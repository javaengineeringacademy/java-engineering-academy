# Stream Operations Quiz

## Multiple Choice Questions

### Question 1
Which method keeps elements matching a predicate?
- A) map()
- B) filter()
- C) reduce()
- D) collect()

### Question 2
What is the return type of filter()?
- A) List
- B) Collection
- C) Stream
- D) Optional

### Question 3
Which method removes duplicates from a stream?
- A) distinct()
- B) unique()
- C) deduplicate()
- D) filter()

### Question 4
What does map() do?
- A) Filters elements
- B) Transforms each element
- C) Reduces to single value
- D) Sorts elements

### Question 5
Which method flattens nested streams?
- A) flatten()
- B) flatMap()
- C) concat()
- D) merge()

### Question 6
What does reduce() return when no identity is provided?
- A) The value directly
- B) Optional
- C) List
- D) Stream

### Question 7
Which collector joins strings?
- A) Collectors.together()
- B) Collectors.concat()
- C) Collectors.joining()
- D) Collectors.merge()

### Question 8
What does groupingBy() return?
- A) List
- B) Set
- C) Map
- D) Stream

### Question 9
Which method creates a parallel stream?
- A) parallel()
- B) parallelStream()
- C) concurrent()
- D) async()

### Question 10
What does sorted() use by default?
- A) Reverse order
- B) Random order
- C) Natural order (Comparable)
- D) No order

### Question 11
Which method counts elements?
- A) size()
- B) length()
- C) count()
- D) total()

### Question 12
What does takeWhile() do?
- A) Takes all elements
- B) Takes while predicate is true
- C) Takes first N elements
- D) Takes random elements

### Question 13
Which is a terminal operation?
- A) filter()
- B) map()
- C) collect()
- D) sorted()

### Question 14
What does flatMap() return?
- A) List
- B) Stream
- C) Optional
- D) Array

### Question 15
Which creates a Comparator?
- A) Comparator.of()
- B) Comparator.comparing()
- C) Comparator.create()
- D) Comparator.new()

### Question 16
What does partitioningBy() always create?
- A) Three groups
- B) Two groups (true/false)
- C) N groups
- D) No groups

### Question 17
Which is NOT an intermediate operation?
- A) filter()
- B) map()
- C) forEach()
- D) sorted()

### Question 18
What does Stream.iterate() create?
- A) Finite stream
- B) Infinite stream
- C) Empty stream
- D) Parallel stream

### Question 19
Which collects to a Map?
- A) Collectors.toList()
- B) Collectors.toSet()
- C) Collectors.toMap()
- D) All of the above

### Question 20
When should you use parallel streams?
- A) Small datasets
- B) I/O-bound operations
- C) Large, CPU-intensive datasets
- D) When order matters

---

## Answers

1. **B) filter()** - filter() keeps elements matching the predicate
2. **C) Stream** - filter() returns a new Stream
3. **A) distinct()** - distinct() removes duplicates using equals/hashCode
4. **B) Transforms each element** - map() applies function to each element
5. **B) flatMap()** - flatMap() flattens nested streams into one
6. **B) Optional** - Without identity, reduce() returns Optional
7. **C) Collectors.joining()** - joining() concatenates strings
8. **C) Map** - groupingBy() returns Map<K, List<T>>
9. **B) parallelStream()** - parallelStream() creates parallel stream
10. **C) Natural order (Comparable)** - sorted() uses natural ordering
11. **C) count()** - count() returns long count of elements
12. **B) Takes while predicate is true** - Stops at first false
13. **C) collect()** - collect() is terminal, others are intermediate
14. **B) Stream** - flatMap() returns flattened Stream
15. **B) Comparator.comparing()** - comparing() creates Comparator from key
16. **B) Two groups (true/false)** - Partitioning creates Boolean-keyed map
17. **C) forEach()** - forEach() is terminal, others are intermediate
18. **B) Infinite stream** - iterate() creates infinite stream from seed
19. **C) Collectors.toMap()** - toMap() collects to Map
20. **C) Large, CPU-intensive datasets** - Parallel best for large CPU work

---

## True/False Questions

21. Streams can be consumed multiple times. (False)
22. filter() is an intermediate operation. (True)
23. reduce() always requires an identity value. (False)
24. parallelStream() is always faster. (False)
25. sorted() modifies the original list. (False)

---

## Code Completion Questions

26. Write a stream to filter names starting with "A" and collect to list:
```java
List<String> result = names.stream()
    .filter(n -> n.startsWith("A"))
    .collect(Collectors.toList());
```

27. Write a stream to get sum of even numbers:
```java
int sum = numbers.stream()
    .filter(n -> n % 2 == 0)
    .reduce(0, Integer::sum);
```

28. Write a stream to group words by length:
```java
Map<Integer, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(String::length));
```

29. Write a stream to sort by multiple fields:
```java
list.stream()
    .sorted(Comparator.comparing(Type::field1)
        .thenComparing(Type::field2))
    .collect(Collectors.toList());
```

30. Write a parallel stream to count elements:
```java
long count = list.parallelStream()
    .filter(condition)
    .count();
```
