# Comparable Quiz

## Questions

### Q1: What is the Comparable interface in Java?
**Answer:** An interface that defines the natural ordering of a class by implementing the compareTo() method.

### Q2: What method does Comparable define?
**Answer:** compareTo(T o), which returns a negative integer, zero, or positive integer.

### Q3: What does compareTo() return if this < other?
**Answer:** A negative integer.

### Q4: What does compareTo() return if this == other?
**Answer:** Zero.

### Q5: What does compareTo() return if this > other?
**Answer:** A positive integer.

### Q6: What is the difference between Comparable and Comparator?
**Answer:** Comparable defines natural ordering within the class; Comparator is an external strategy for ordering.

### Q7: Can a class have multiple Comparable implementations?
**Answer:** No, a class can implement Comparable only once, defining one natural ordering.

### Q8: What does Collections.sort() use by default?
**Answer:** The natural ordering defined by the Comparable implementation.

### Q9: What exception does compareTo() throw?
**Answer:** ClassCastException if the objects cannot be compared.

### Q10: What is the Comparable interface's type parameter used for?
**Answer:** To specify the type of object being compared: Comparable<T>.

## Bonus Questions

### Q11: What is the recommended pattern for implementing compareTo()?
**Answer:** Use Integer.compare() or similar helper methods to handle null safety and avoid subtraction overflow.

### Q12: How do you make a class sortable?
**Answer:** Implement the Comparable<T> interface and override compareTo(T o).
