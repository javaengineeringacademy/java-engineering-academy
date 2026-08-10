# Reducing Quiz

## Questions

### Q1: What does the reduce() operation do in Stream?
**Answer:** It combines stream elements into a single result using an associative accumulation function.

### Q2: What are the three versions of reduce()?
**Answer:** reduce(BinaryOperator), reduce(T identity, BinaryOperator), and reduce(U identity, BiFunction, BinaryOperator).

### Q3: What is a BinaryOperator?
**Answer:** A functional interface that takes two arguments of the same type and returns a result of the same type.

### Q4: What does reduce() return when no identity is provided?
**Answer:** An Optional<T> containing the result.

### Q5: What is the identity value in reduce()?
**Answer:** The initial value and default value if the stream is empty.

### Q6: Is reduce() an intermediate or terminal operation?
**Answer:** Terminal - it produces a single result.

### Q7: What is an example of reduce() usage?
**Answer:** stream.reduce(0, Integer::sum) sums all integers in the stream.

### Q8: What is the difference between reduce() and collect()?
**Answer:** reduce() is for combining values; collect() is for mutable reduction into containers.

### Q9: Can reduce() be used for string concatenation?
**Answer:** Yes: stream.reduce("", String::concat) or stream.reduce("", (a, b) -> a + b).

### Q10: What happens if the stream is empty and an identity is provided?
**Answer:** The identity value is returned.

## Bonus Questions

### Q11: What is the difference between reduce() and min()/max()?
**Answer:** reduce() is general-purpose; min() and max() are specialized for finding smallest/largest values.

### Q12: How does reduce() work in parallel streams?
**Answer:** It uses the combiner function to merge partial results from different threads.
