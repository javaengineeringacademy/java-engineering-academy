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
