# Searching Quiz

## Questions

### Q1: What is the time complexity of linear search?
**Answer:** O(n) — must check every element in worst case.

### Q2: What is the time complexity of binary search?
**Answer:** O(log n) — halves the search space each step.

### Q3: What prerequisite does binary search have?
**Answer:** The collection must be sorted. Binary search on unsorted data gives wrong results.

### Q4: What does indexOf() return if element is not found?
**Answer:** -1. contains() returns false.

### Q5: What is the difference between indexOf() and lastIndexOf()?
**Answer:** indexOf() returns first occurrence. lastIndexOf() returns last occurrence.

### Q6: What is the difference between Collections.binarySearch() and Arrays.binarySearch()?
**Answer:** Collections.binarySearch() works on Lists. Arrays.binarySearch() works on arrays.

### Q7: What happens if you call binarySearch() on an unsorted list?
**Answer:** The result is undefined. You may get wrong index or -1.

### Q8: What is the difference between contains() and indexOf()?
**Answer:** contains() returns boolean. indexOf() returns the index (-1 if not found).

### Q9: When should you use HashSet for searching?
**Answer:** When you need O(1) membership checks. HashSet.contains() is faster than List.indexOf().

### Q10: What is the time complexity of TreeSet.contains()?
**Answer:** O(log n) — TreeSet uses a balanced binary search tree.

## Bonus Questions

### Q11: How do you search in a Map?
**Answer:** Use containsKey() for key lookup, containsValue() for value lookup, get() to retrieve value.

### Q12: What is the difference between findFirst() and findAny()?
**Answer:** findFirst() returns the first element (ordered). findAny() returns any element (faster for parallel streams).

## True/False

**Q13: Linear search requires sorted data.**
Answer: False — Linear search works on any data.

**Q14: Binary search has O(n) time complexity.**
Answer: False — Binary search has O(log n) time complexity.

**Q15: indexOf() uses linear search internally.**
Answer: True — indexOf() scans elements sequentially.

**Q16: Binary search is always faster than linear search.**
Answer: False — For small data, linear search can be faster.

**Q17: HashSet.contains() uses binary search.**
Answer: False — HashSet uses hash-based lookup (O(1)).

## Code Output

**Q18: What does this code print?**
```java
List<Integer> list = Arrays.asList(1, 3, 5, 7, 9);
int index = Collections.binarySearch(list, 5);
System.out.println(index);
```
Answer: 2 — Binary search returns index of element 5.

**Q19: What does this code print?**
```java
List<Integer> list = Arrays.asList(1, 3, 5, 7, 9);
int index = Collections.binarySearch(list, 4);
System.out.println(index);
```
Answer: -3 — Negative value indicates insertion point (-(insertion point) - 1).

**Q20: What does this code print?**
```java
String[] arr = {"a", "b", "c", "d"};
int index = Arrays.binarySearch(arr, "c");
System.out.println(index);
```
Answer: 2 — Binary search returns index of "c".
