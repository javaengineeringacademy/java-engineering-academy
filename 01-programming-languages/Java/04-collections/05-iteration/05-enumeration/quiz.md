# Enumeration Quiz

## Questions

### Q1: What is an Enumeration in Java?
**Answer:** A legacy interface for traversing Vector and Hashtable elements one at a time.

### Q2: What are the two methods of Enumeration?
**Answer:** hasMoreElements() and nextElement().

### Q3: Why is Enumeration considered legacy?
**Answer:** It was replaced by Iterator, which adds the remove() method and better design.

### Q4: Can Enumeration be used with modern Collections like ArrayList?
**Answer:** No, it only works with legacy classes like Vector and Hashtable.

### Q5: What is the difference between Iterator and Enumeration?
**Answer:** Iterator has remove() and is for all Collections; Enumeration has no remove() and is for legacy classes only.

### Q6: How do you get an Enumeration from a Vector?
**Answer:** By calling vector.elements().

### Q7: What happens if you call nextElement() when no more elements exist?
**Answer:** It throws NoSuchElementException.

### Q8: Does Enumeration support generics?
**Answer:** Yes, Enumeration<E> is parameterized.

### Q9: When should you use Enumeration instead of Iterator?
**Answer:** When working with legacy classes like Vector or Hashtable that provide the elements() method.

### Q10: What is the default iterator for Hashtable?
**Answer:** Enumeration, accessed via elements() and keys() methods.

## Bonus Questions

### Q11: What method in Collections converts an Enumeration to an Iterator?
**Answer:** Collections.enumeration() creates an Enumeration, but there is no direct converter; use a manual loop instead.

### Q12: Is Enumeration thread-safe?
**Answer:** Not inherently, though the legacy classes it works with (Vector, Hashtable) are synchronized.
