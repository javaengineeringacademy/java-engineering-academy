# Stream Operations Quiz

## Questions

### Q1: What is the difference between map() and flatMap()?
**Answer:** map() transforms each element (1→1). flatMap() flattens nested structures (1→many).

### Q2: What is the difference between reduce() and collect()?
**Answer:** reduce() combines elements to single value. collect() collects to collection or uses Collector.

### Q3: What is the difference between filter() and removeIf()?
**Answer:** filter() returns new stream (lazy). removeIf() modifies original collection (eager).

### Q4: What is the difference between sorted() and sort()?
**Answer:** sorted() returns new stream (doesn't modify original). sort() modifies original list.

### Q5: What is the difference between parallelStream() and stream()?
**Answer:** stream() is sequential. parallelStream() uses multiple threads for parallel processing.

### Q6: What is a Collector?
**Answer:** An object that accumulates input elements into a mutable result container (e.g., toList(), toSet(), joining()).

### Q7: What is the difference between anyMatch() and allMatch()?
**Answer:** anyMatch() returns true if any element matches. allMatch() returns true if all elements match.

### Q8: What is the difference between findFirst() and findAny()?
**Answer:** findFirst() returns first element (ordered). findAny() returns any element (faster for parallel).

### Q9: What is the difference between forEach() and forEachOrdered()?
**Answer:** forEach() is unordered. forEachOrdered() processes elements in encounter order.

### Q10: When should you use parallelStream()?
**Answer:** When you have large datasets and CPU-intensive operations. Don't use for small datasets or ordered results.

## Bonus Questions

### Q11: What is the difference between mapToInt() and map()?
**Answer:** mapToInt() returns IntStream (primitive). map() returns Stream<Integer> (boxed).

### Q12: How do you convert a Stream to a List?
**Answer:** Use stream().collect(Collectors.toList()) or stream().toList() (Java 16+).
