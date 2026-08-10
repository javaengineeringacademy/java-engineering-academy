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

## True/False

**Q13: Binary search can be implemented recursively.**
Answer: True — Both iterative and recursive implementations exist.

**Q14: Binary search has O(1) space complexity in iterative version.**
Answer: True — Iterative version uses constant space.

**Q15: Binary search works on LinkedList efficiently.**
Answer: False — LinkedList has O(n) random access, making binary search inefficient.

**Q16: mid = (left + right) / 2 can cause integer overflow.**
Answer: True — Use left + (right - left) / 2 instead.

**Q17: Binary search always finds the first occurrence of duplicate elements.**
Answer: False — Binary search finds any occurrence; modifications needed for first/last.

## Code Output

**Q18: What does this code print?**
```java
int[] arr = {1, 3, 5, 7, 9};
int left = 0, right = arr.length - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;
    if (arr[mid] == 7) {
        System.out.println(mid);
        break;
    }
    if (arr[mid] < 7) left = mid + 1;
    else right = mid - 1;
}
```
Answer: 3 — Binary search finds 7 at index 3.

**Q19: What does this code print?**
```java
int[] arr = {1, 3, 5, 7, 9};
int result = Arrays.binarySearch(arr, 4);
System.out.println(result);
```
Answer: -3 — Element not found; negative value indicates insertion point.

**Q20: What does this code print?**
```java
int[] arr = {10, 20, 30, 40, 50};
int idx = Arrays.binarySearch(arr, 30);
System.out.println(idx);
```
Answer: 2 — Binary search finds 30 at index 2.
