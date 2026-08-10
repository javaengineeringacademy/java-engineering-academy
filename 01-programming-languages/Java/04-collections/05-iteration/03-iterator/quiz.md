# Iterator Quiz

## Questions

### Q1: What is an Iterator in Java?
**Answer:** An object that enables traversal of a collection, one element at a time.

### Q2: What are the main methods of an Iterator?
**Answer:** hasNext(), next(), and remove().

### Q3: What happens if you call next() when hasNext() returns false?
**Answer:** It throws NoSuchElementException.

### Q4: What is the purpose of the remove() method in Iterator?
**Answer:** It removes the last element returned by next() from the underlying collection.

### Q5: How do you get an Iterator from a Collection?
**Answer:** By calling the iterator() method: Iterator<T> it = collection.iterator();

### Q6: What is ConcurrentModificationException?
**Answer:** Thrown when a collection is modified structurally while being iterated without using the iterator's remove method.

### Q7: Can Iterator go backward through a collection?
**Answer:** No, Iterator is forward-only. Use ListIterator for bidirectional traversal.

### Q8: What is the difference between Iterator and Iterable?
**Answer:** Iterable provides the iterator() method, while Iterator provides hasNext(), next(), and remove().

### Q9: Does Iterator support generics?
**Answer:** Yes, Iterator<T> is parameterized with the element type.

### Q10: What is the default iterator for an ArrayList?
**Answer:** An implementation of ListIterator that traverses in forward order.

## Bonus Questions

### Q11: What is the forEachRemaining() method?
**Answer:** A default method in Iterator that performs the given action for each remaining element.

### Q12: Can you create your own Iterator implementation?
**Answer:** Yes, implement the Iterator interface and provide hasNext(), next(), and optionally remove().
