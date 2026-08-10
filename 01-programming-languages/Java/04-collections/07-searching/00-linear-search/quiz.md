# Linear Search Quiz

## Questions

### Q1: What is linear search?
**Answer:** A simple search algorithm that checks each element sequentially until the target is found or the list ends.

### Q2: What is the time complexity of linear search?
**Answer:** O(n) in the worst case, where n is the number of elements.

### Q3: Does linear search require the data to be sorted?
**Answer:** No, linear search works on both sorted and unsorted collections.

### Q4: What is the best case time complexity of linear search?
**Answer:** O(1), when the target is the first element.

### Q5: How do you implement linear search in Java?
**Answer:** Using a for loop or List.indexOf() method.

### Q6: What is the disadvantage of linear search?
**Answer:** It is slow for large datasets because it examines every element sequentially.

### Q7: When should you use linear search?
**Answer:** When the data is small, unsorted, or searched only once.

### Q8: Can linear search be used on a LinkedList?
**Answer:** Yes, though it is O(n) since LinkedList does not support random access.

### Q9: What does List.indexOf() use internally?
**Answer:** Linear search to find the first occurrence of the element.

### Q10: What is the maximum number of comparisons in linear search?
**Answer:** n comparisons, where n is the number of elements.

## Bonus Questions

### Q11: What is sentinel linear search?
**Answer:** A variation where a sentinel value is placed at the end to eliminate the bounds check in each iteration.

### Q12: What is the difference between linear search and binary search?
**Answer:** Linear search is O(n) and works on unsorted data; binary search is O(log n) but requires sorted data.
