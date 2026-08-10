# Tim Sort Quiz

## Questions

### Q1: What is Tim Sort?
**Answer:** A hybrid stable sorting algorithm combining merge sort and insertion sort, optimized for real-world data.

### Q2: Who developed Tim Sort?
**Answer:** Tim Peters in 2002 for Python.

### Q3: What is the time complexity of Tim Sort?
**Answer:** O(n log n) in worst case, O(n) in best case (already sorted data).

### Q4: Why is Tim Sort used in Java?
**Answer:** It's efficient for partially sorted data, stable, and performs well in practice.

### Q5: What is a run in Tim Sort?
**Answer:** A sequence of consecutive elements that are already sorted (ascending or descending).

### Q6: How does Tim Sort handle small arrays?
**Answer:** It uses insertion sort for small runs (typically less than 32-64 elements).

### Q7: Is Tim Sort a stable sorting algorithm?
**Answer:** Yes, it preserves the relative order of equal elements.

### Q8: What is the minimum run length in Tim Sort?
**Answer:** Typically 32 or 64, calculated based on the array size.

### Q9: What is the space complexity of Tim Sort?
**Answer:** O(n) additional space for the merge operation.

### Q10: Which Java methods use Tim Sort internally?
**Answer:** Arrays.sort() for objects and Collections.sort().

## Bonus Questions

### Q11: How does Tim Sort optimize for already sorted data?
**Answer:** It detects natural runs (sorted sequences) and merges them, achieving O(n) time for sorted input.

### Q12: What is the difference between Tim Sort and Merge Sort?
**Answer:** Tim Sort uses insertion sort for small arrays and exploits existing order in data, while standard Merge Sort divides uniformly.
