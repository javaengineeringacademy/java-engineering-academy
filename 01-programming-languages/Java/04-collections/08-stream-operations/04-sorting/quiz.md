# Sorting Quiz

## Questions

### Q1: What does the sorted() operation do in Stream?
**Answer:** It returns a stream with elements sorted according to natural ordering or a Comparator.

### Q2: What are the two versions of sorted()?
**Answer:** sorted() for natural ordering and sorted(Comparator) for custom ordering.

### Q3: Is sorted() an intermediate or terminal operation?
**Answer:** Intermediate - it returns a new sorted stream.

### Q4: Is sorted() eager or lazy?
**Answer:** Lazy - sorting is deferred until a terminal operation is invoked.

### Q5: What is the time complexity of sorted()?
**Answer:** O(n log n) for the sort operation.

### Q6: Does sorted() preserve the original collection?
**Answer:** Yes, streams are immutable; the original data is unchanged.

### Q7: Is sorted() a stable sort?
**Answer:** Yes, it preserves the relative order of equal elements.

### Q8: How do you sort in descending order?
**Answer:** Using sorted(Comparator.reverseOrder()).

### Q9: What is the difference between sorted() and Collections.sort()?
**Answer:** sorted() returns a new stream; Collections.sort() modifies the list in place.

### Q10: Can you sort by a specific field using sorted()?
**Answer:** Yes, using Comparator.comparing() to extract the field.

## Bonus Questions

### Q11: How do you sort and then limit results?
**Answer:** Using stream.sorted(comparator).limit(n).

### Q12: What is the difference between sorted().findFirst() and min()?
**Answer:** Both find the smallest element; min() is more efficient as it doesn't need to sort the entire stream.
