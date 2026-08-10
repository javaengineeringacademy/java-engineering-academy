# Collections.sort() Quiz

## Questions

### Q1: What does Collections.sort() do?
**Answer:** Sorts the specified list into ascending order according to natural ordering or a Comparator.

### Q2: What are the two versions of Collections.sort()?
**Answer:** sort(List<T>) for natural ordering and sort(List<T>, Comparator<? super T>) for custom ordering.

### Q3: What algorithm does Collections.sort() use?
**Answer:** A modified merge sort (Tim Sort in modern Java versions).

### Q4: Is Collections.sort() stable?
**Answer:** Yes, it preserves the relative order of equal elements.

### Q5: What is the time complexity of Collections.sort()?
**Answer:** O(n log n) in the average and worst cases.

### Q6: Can Collections.sort() sort a list of objects?
**Answer:** Yes, if the objects implement Comparable or a Comparator is provided.

### Q7: What happens if you try to sort an immutable list?
**Answer:** It throws UnsupportedOperationException.

### Q8: What is the difference between Arrays.sort() and Collections.sort()?
**Answer:** Arrays.sort() sorts arrays; Collections.sort() sorts Lists. Both use Tim Sort.

### Q9: How do you sort in descending order?
**Answer:** Using Collections.sort(list, Comparator.reverseOrder()) or list.sort(Comparator.reverseOrder()).

### Q10: Does Collections.sort() modify the original list?
**Answer:** Yes, it sorts the list in place (in-place sorting).

## Bonus Questions

### Q11: What is Tim Sort?
**Answer:** A hybrid stable sorting algorithm derived from merge sort and insertion sort, used as the default in Java.

### Q12: What is the difference between sort() and stream().sorted()?
**Answer:** sort() modifies the list in place; stream().sorted() returns a new sorted stream without modifying the original.
