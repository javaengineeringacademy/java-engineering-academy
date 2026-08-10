# ListIterator Quiz

## Questions

### Q1: What is a ListIterator?
**Answer:** An Iterator for Lists that allows bidirectional traversal and element modification.

### Q2: What additional methods does ListIterator have compared to Iterator?
**Answer:** hasPrevious(), previous(), nextIndex(), previousIndex(), set(), and add().

### Q3: How do you create a ListIterator from a List?
**Answer:** list.listIterator() or list.listIterator(index) to start from a specific position.

### Q4: What does ListIterator.add() do?
**Answer:** It inserts an element before the element that would be returned by next().

### Q5: What does ListIterator.set() do?
**Answer:** It replaces the last element returned by next() or previous() with the specified element.

### Q6: Can ListIterator be used on any Collection?
**Answer:** No, it is specific to List implementations.

### Q7: What does hasPrevious() return?
**Answer:** true if there are elements before the current position in the list.

### Q8: What does previous() return?
**Answer:** The previous element in the list and moves the cursor backward.

### Q9: What is the initial cursor position of a ListIterator?
**Answer:** Before the first element (index 0).

### Q10: What happens if you call set() before calling next() or previous()?
**Answer:** It throws IllegalStateException.

## Bonus Questions

### Q11: What is the difference between Iterator and ListIterator?
**Answer:** Iterator is forward-only and works with all Collections; ListIterator is bidirectional and works only with Lists.

### Q12: How can you use ListIterator to iterate a List in reverse?
**Answer:** Call list.listIterator(list.size()), then use hasPrevious() and previous() in a loop.
