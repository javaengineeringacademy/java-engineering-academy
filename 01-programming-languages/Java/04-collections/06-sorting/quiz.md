# Sorting Quiz

## Questions

### Q1: What is the difference between Comparable and Comparator?
**Answer:** Comparable defines natural ordering (single). Comparator defines custom ordering (multiple possible).

### Q2: What method must Comparable implement?
**Answer:** compareTo(T o) — returns negative, zero, or positive integer.

### Q3: What is the time complexity of Collections.sort()?
**Answer:** O(n log n) — uses TimSort algorithm.

### Q4: What is TimSort?
**Answer:** A hybrid sorting algorithm (merge sort + insertion sort). It's stable and O(n log n).

### Q5: What is the difference between sort() and sorted()?
**Answer:** sort() modifies the original list. sorted() returns a new sorted stream (doesn't modify original).

### Q6: What is a stable sort?
**Answer:** A sort that preserves the relative order of equal elements. TimSort is stable.

### Q7: How do you sort in reverse order?
**Answer:** Use Collections.reverseOrder() as comparator, or Comparator.reverseOrder(), or Collections.reverse().

### Q8: Can you sort a LinkedList efficiently?
**Answer:** Yes. Collections.sort() converts to array, sorts, then copies back. It's O(n log n).

### Q9: What is the difference between Arrays.sort() and Collections.sort()?
**Answer:** Arrays.sort() uses DualPivotQuicksort for primitives, TimSort for objects. Collections.sort() uses TimSort.

### Q10: What happens if elements are not Comparable?
**Answer:** Throws ClassCastException. You must provide a Comparator.

## Bonus Questions

### Q11: What is the difference between sort() and parallelSort()?
**Answer:** sort() is sequential. parallelSort() uses multiple threads for large arrays.

### Q12: How do you sort a Map by values?
**Answer:** Use map.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect()...
