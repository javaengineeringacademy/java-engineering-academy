# Filtering Quiz

## Questions

### Q1: What does the filter() operation do in Stream?
**Answer:** It selects elements that match a given Predicate, creating a new stream with only those elements.

### Q2: What is a Predicate in Java?
**Answer:** A functional interface that takes one argument and returns a boolean value.

### Q3: Is filter() an intermediate or terminal operation?
**Answer:** Intermediate - it returns a new stream and is lazy.

### Q4: What is an example of filter() usage?
**Answer:** stream.filter(s -> s.length() > 5) keeps strings longer than 5 characters.

### Q5: Can you chain multiple filter() operations?
**Answer:** Yes, each filter is applied sequentially.

### Q6: What does filter() return?
**Answer:** A new Stream containing only elements that match the predicate.

### Q7: Does filter() modify the original collection?
**Answer:** No, streams are immutable and do not modify the source.

### Q8: What is the difference between filter() and removeIf()?
**Answer:** filter() creates a new stream; removeIf() modifies the collection in place.

### Q9: Can filter() be used with method references?
**Answer:** Yes: stream.filter(String::isEmpty) filters empty strings.

### Q10: Is filter() evaluated eagerly or lazily?
**Answer:** Lazily - elements are filtered only when a terminal operation is invoked.

## Bonus Questions

### Q11: What is the difference between filter() and findFirst()?
**Answer:** filter() selects multiple matching elements; findFirst() returns the first match as an Optional.

### Q12: How do you filter distinct elements in a stream?
**Answer:** Using the distinct() operation: stream.distinct().
