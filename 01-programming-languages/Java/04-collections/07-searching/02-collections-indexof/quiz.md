# Collections.indexOf() Quiz

## Questions

### Q1: What does List.indexOf() do?
**Answer:** Returns the index of the first occurrence of the specified element, or -1 if not found.

### Q2: What does List.lastIndexOf() do?
**Answer:** Returns the index of the last occurrence of the specified element, or -1 if not found.

### Q3: What is the time complexity of indexOf() on an ArrayList?
**Answer:** O(n) - it performs a linear search.

### Q4: What is the time complexity of indexOf() on a LinkedList?
**Answer:** O(n) - it traverses the list sequentially.

### Q5: How does indexOf() handle null elements?
**Answer:** It can find null elements using Objects.equals() for comparison.

### Q6: What is the difference between indexOf() and contains()?
**Answer:** indexOf() returns the index; contains() returns a boolean.

### Q7: Does indexOf() use equals() for comparison?
**Answer:** Yes, it uses the equals() method of the elements.

### Q8: What does Collections.frequency() do?
**Answer:** It counts how many times an element appears in a collection.

### Q9: How do you find all occurrences of an element in a List?
**Answer:** Use indexOf() in a loop, starting from the previous index + 1 each time.

### Q10: What does List.subList() return?
**Answer:** A view of a portion of the list between specified indices.

## Bonus Questions

### Q11: What is the difference between indexOf() on List vs Set?
**Answer:** List.indexOf() returns the index; Set does not have indexOf() as it has no index concept.

### Q12: How can you optimize searching in a frequently-searched collection?
**Answer:** Use a HashMap for O(1) lookups or keep the collection sorted and use binary search.
