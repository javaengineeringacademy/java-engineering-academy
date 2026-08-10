# Binary Search Quiz

## Questions

### Q1: What is binary search?
**Answer:** An efficient search algorithm that finds an element by repeatedly dividing the search interval in half.

### Q2: What is the time complexity of binary search?
**Answer:** O(log n) in the worst case.

### Q3: What is the prerequisite for binary search?
**Answer:** The data must be sorted.

### Q4: How does binary search work?
**Answer:** It compares the target with the middle element, then searches the left or right half based on the comparison.

### Q5: What are the two implementations of binary search?
**Answer:** Iterative (using a loop) and recursive (using method calls).

### Q6: What is the space complexity of iterative binary search?
**Answer:** O(1) - constant space.

### Q7: What is the space complexity of recursive binary search?
**Answer:** O(log n) due to the call stack.

### Q8: How do you use binary search in Java?
**Answer:** Using Collections.binarySearch(list, key) or Arrays.binarySearch(array, key).

### Q9: What does Collections.binarySearch() return if the element is not found?
**Answer:** -(insertion point) - 1, a negative value indicating where the element should be inserted.

### Q10: Can binary search be used on a LinkedList?
**Answer:** Technically yes via Collections.binarySearch(), but it is inefficient due to O(n) random access.

## Bonus Questions

### Q11: What is the difference between lower bound and upper bound in binary search?
**Answer:** Lower bound finds the first position >= target; upper bound finds the first position > target.

### Q12: What is interpolation search and how does it differ from binary search?
**Answer:** Interpolation search estimates position based on value distribution; it is O(log log n) for uniform data but O(n) worst case.
