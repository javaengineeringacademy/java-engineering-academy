# Stream Traversal and Lambda Quiz

## Questions

### Q1: What is a Stream in Java?
**Answer:** A sequence of elements supporting functional-style aggregate operations.

### Q2: How do you create a Stream from a Collection?
**Answer:** Using collection.stream() or collection.parallelStream().

### Q3: What is the difference between stream() and parallelStream()?
**Answer:** stream() processes sequentially; parallelStream() processes elements in parallel using multiple threads.

### Q4: What is a lambda expression in Java?
**Answer:** An anonymous function that can be passed as an argument to methods expecting a functional interface.

### Q5: What is a functional interface?
**Answer:** An interface with exactly one abstract method, annotated with @FunctionalInterface.

### Q6: What does forEach() do on a Stream?
**Answer:** It performs an action for each element of the stream (terminal operation).

### Q7: What is the difference between map() and flatMap()?
**Answer:** map transforms each element; flatMap transforms each element to a stream and flattens the results.

### Q8: What is method reference in Java?
**Answer:** A shorthand notation for lambda expressions that call a method: ClassName::methodName.

### Q9: What are intermediate vs terminal operations?
**Answer:** Intermediate operations are lazy (filter, map); terminal operations trigger processing (forEach, collect).

### Q10: How do you collect stream results into a List?
**Answer:** Using stream.collect(Collectors.toList()).

## Bonus Questions

### Q11: What does the reduce() operation do?
**Answer:** It combines stream elements into a single result using an associative accumulation function.

### Q12: What is the difference between peek() and map()?
**Answer:** peek performs an action without modifying elements (for debugging); map transforms elements.
