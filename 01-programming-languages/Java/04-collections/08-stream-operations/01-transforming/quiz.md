# Transforming Quiz

## Questions

### Q1: What does the map() operation do in Stream?
**Answer:** It transforms each element using a Function and returns a new stream of the transformed values.

### Q2: What is a Function in Java?
**Answer:** A functional interface that takes one argument and returns a result.

### Q3: Is map() an intermediate or terminal operation?
**Answer:** Intermediate - it returns a new stream and is lazy.

### Q4: What is the difference between map() and flatMap()?
**Answer:** map transforms one element to one result; flatMap transforms one element to a stream and flattens all streams.

### Q5: Can map() change the element type?
**Answer:** Yes, you can map from one type to another (e.g., String to Integer).

### Q6: What is an example of map() usage?
**Answer:** stream.map(String::toUpperCase) converts all strings to uppercase.

### Q7: What does flatMap() return?
**Answer:** A flattened stream after mapping each element to a stream and merging them.

### Q8: When should you use flatMap() over map()?
**Answer:** When the mapping function returns a stream or collection that needs to be flattened.

### Q9: Can map() be used with method references?
**Answer:** Yes: stream.map(String::length) maps strings to their lengths.

### Q10: Is map() evaluated eagerly or lazily?
**Answer:** Lazily - transformation occurs only when a terminal operation is invoked.

## Bonus Questions

### Q11: What is the difference between mapToInt() and map()?
**Answer:** mapToInt() returns an IntStream (primitive specialization); map() returns Stream<Integer>.

### Q12: How do you transform and filter in one step?
**Answer:** You cannot do both in one operation; use filter() then map() or vice versa in a chain.
