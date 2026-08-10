# Spliterator Quiz

## Questions

### Q1: What is a Spliterator?
**Answer:** A specialized Iterator for parallel traversal of elements in a source, supporting splitting for parallel streams.

### Q2: What does Spliterator stand for?
**Answer:** Splitable Iterator.

### Q3: What is the main method for processing elements in Spliterator?
**Answer:** tryAdvance(Consumer action) which processes the next element if available.

### Q4: What does trySplit() do?
**Answer:** It splits the Spliterator into two parts for parallel processing.

### Q5: What is the purpose of the characteristics() method?
**Answer:** It returns flags indicating properties like ORDERED, DISTINCT, SIZED, etc.

### Q6: How does Spliterator relate to parallel streams?
**Answer:** Spliterator enables parallel stream operations by splitting data sources for concurrent processing.

### Q7: What are the key Spliterator characteristics?
**Answer:** ORDERED, DISTINCT, SORTED, SIZED, NONNULL, IMMUTABLE, CONCURRENT, SUBSIZED.

### Q8: What is the difference between Spliterator and Iterator?
**Answer:** Spliterator supports splitting for parallel processing; Iterator is sequential and single-direction.

### Q9: What does Spliterator.estimateSize() return?
**Answer:** An estimate of the number of remaining elements.

### Q10: What is a late-binding Spliterator?
**Answer:** A Spliterator that defers binding to the data source until first use, allowing for more efficient traversal.

## Bonus Questions

### Q11: How do you get a Spliterator from a Collection?
**Answer:** By calling collection.spliterator().

### Q12: What is the difference between tryAdvance() and forEachRemaining()?
**Answer:** tryAdvance processes one element at a time; forEachRemaining processes all remaining elements in one call.
