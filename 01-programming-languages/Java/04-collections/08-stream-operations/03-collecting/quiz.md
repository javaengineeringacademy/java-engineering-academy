# Collecting Quiz

## Questions

### Q1: What does the collect() operation do in Stream?
**Answer:** It performs a mutable reduction, accumulating elements into a container (List, Set, Map, etc.).

### Q2: What is a Collector?
**Answer:** An interface that defines how to accumulate elements into a result container.

### Q3: What is the most common Collector?
**Answer:** Collectors.toList() - accumulates elements into a List.

### Q4: How do you collect elements into a Set?
**Answer:** Using stream.collect(Collectors.toSet()).

### Q5: How do you collect elements into a String?
**Answer:** Using stream.collect(Collectors.joining()) or Collectors.joining(delimiter).

### Q6: What is Collectors.groupingBy() used for?
**Answer:** It groups elements by a classifier function into a Map<K, List<T>>.

### Q7: What is Collectors.partitioningBy() used for?
**Answer:** It partitions elements into two groups based on a Predicate (true/false).

### Q8: What is the difference between toList() and toUnmodifiableList()?
**Answer:** toList() returns a mutable List; toUnmodifiableList() returns an immutable List.

### Q9: How do you count elements using collect()?
**Answer:** Using stream.collect(Collectors.counting()) or stream.count().

### Q10: What is Collectors.summarizingInt()?
**Answer:** It collects statistics (count, sum, min, max, average) about integer values.

## Bonus Questions

### Q11: What is the difference between collect() and reduce()?
**Answer:** collect() uses a mutable container (more efficient); reduce() uses immutable accumulation.

### Q12: How do you create a custom Collector?
**Answer:** Implement the Collector interface with supplier(), accumulator(), combiner(), and finisher() methods.
