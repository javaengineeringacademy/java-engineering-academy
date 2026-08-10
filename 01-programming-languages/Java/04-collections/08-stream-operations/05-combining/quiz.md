# Combining Quiz

## Questions

### Q1: What does the concat() operation do in Stream?
**Answer:** It merges two streams into one, concatenating the second stream after the first.

### Q2: What is the difference between concat() and flatMap()?
**Answer:** concat() joins two streams; flatMap() maps each element to a stream and flattens them.

### Q3: Is concat() an intermediate or terminal operation?
**Answer:** Intermediate - it returns a new stream.

### Q4: How do you merge two Lists using streams?
**Answer:** Using Stream.concat(list1.stream(), list2.stream()).

### Q5: What does flatMap() do when combining?
**Answer:** It flattens nested streams (e.g., Stream<Stream<T>> to Stream<T>).

### Q6: Can you combine more than two streams?
**Answer:** Yes, using Stream.concat() multiple times or flatMap.

### Q7: What is the difference between concat() and merge()?
**Answer:** concat() is sequential; merge() is for zipping two streams together.

### Q8: How do you combine streams without duplicates?
**Answer:** Using concat().distinct() or flatMap().distinct().

### Q9: What is Stream.of() used for?
**Answer:** Creating a stream from individual values: Stream.of(a, b, c).

### Q10: Can you combine a stream with an array?
**Answer:** Yes, using Stream.concat(stream, Arrays.stream(array)).

## Bonus Questions

### Q11: What is the difference between concat() and Stream.builder()?
**Answer:** concat() merges existing streams; Stream.builder() constructs a stream by adding elements one by one.

### Q12: How do you combine streams conditionally?
**Answer:** Using flatMap with a function that returns a stream conditionally based on the element.
