# Comparator Quiz

## Questions

### Q1: What is a Comparator in Java?
**Answer:** An interface that defines an external comparison strategy for objects.

### Q2: What method does Comparator define?
**Answer:** compare(T o1, T o2), which returns a negative integer, zero, or positive integer.

### Q3: What is the difference between Comparator and Comparable?
**Answer:** Comparable is implemented by the class itself (natural ordering); Comparator is passed externally.

### Q4: Can a class have multiple Comparators?
**Answer:** Yes, you can create multiple Comparator implementations for different ordering strategies.

### Q5: How do you sort a List using a Comparator?
**Answer:** By passing it to Collections.sort(list, comparator) or list.sort(comparator).

### Q6: What does Comparator.naturalOrder() return?
**Answer:** A Comparator that sorts elements using their natural ordering (Comparable).

### Q7: What does Comparator.reverseOrder() return?
**Answer:** A Comparator that sorts elements in reverse of their natural ordering.

### Q8: How do you chain Comparators?
**Answer:** Using thenComparing(): comparator1.thenComparing(comparator2).

### Q9: What does Comparator.nullsFirst() do?
**Answer:** It returns a Comparator that places null values before non-null values.

### Q10: What does Comparator.nullsLast() do?
**Answer:** It returns a Comparator that places null values after non-null values.

## Bonus Questions

### Q11: How do you create a Comparator from a lambda expression?
**Answer:** Comparator<String> comp = (s1, s2) -> s1.compareTo(s2);

### Q12: What does Comparator.comparing() do?
**Answer:** It creates a Comparator that extracts a Comparable key from an object using a function.
