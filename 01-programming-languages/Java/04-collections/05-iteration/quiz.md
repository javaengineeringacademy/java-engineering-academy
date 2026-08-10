# Iteration Patterns Quiz

## Questions

### Q1: What is the difference between for loop and for-each loop?
**Answer:** for loop provides index access. for-each is cleaner but no index, no removal during iteration.

### Q2: What happens if you modify a collection during for-each iteration?
**Answer:** Throws ConcurrentModificationException. Use Iterator.remove() or removeIf() instead.

### Q3: What is the time complexity of Iterator.remove()?
**Answer:** O(1) for most implementations. It's the safe way to remove during iteration.

### Q4: What is the difference between Iterator and ListIterator?
**Answer:** Iterator is forward-only. ListIterator is bidirectional and can add/modify elements.

### Q5: What is Enumeration and why is it legacy?
**Answer:** Enumeration is from Java 1.0 (pre-Collections). It's legacy because Iterator has more features (remove, better naming).

### Q6: What is Spliterator used for?
**Answer:** Spliterator enables parallel stream processing. It splits data for parallel processing.

### Q7: What is the difference between forEach() and stream().forEach()?
**Answer:** forEach() is on Iterable, processes in order. stream().forEach() is unordered unless you use forEachOrdered().

### Q8: When should you use a traditional for loop over for-each?
**Answer:** When you need the index, or when you need to modify the collection during iteration.

### Q9: What is ConcurrentModificationException?
**Answer:** Thrown when a collection is modified structurally during iteration (except through Iterator.remove()).

### Q10: What is the best way to remove elements during iteration?
**Answer:** Use Iterator.remove() or Collection.removeIf(). Never use for-each loop with remove().

## Bonus Questions

### Q11: What is the difference between Iterator and ListIterator?
**Answer:** Iterator: forward only, no add, no index. ListIterator: bidirectional, can add, has index.

### Q12: How does Java 8 forEach() work internally?
**Answer:** forEach() is a default method on Iterable that calls accept() on each element using Consumer interface.

## True/False

**Q13: for-each loop is faster than traditional for loop.**
Answer: False — Both have similar performance; for-each is syntactic sugar.

**Q14: Iterator can traverse both List and Set.**
Answer: True — Iterator works on any Collection.

**Q15: ListIterator can traverse in both directions.**
Answer: True — ListIterator has hasNext() and hasPrevious().

**Q16: Enumeration is the preferred way to iterate in modern Java.**
Answer: False — Enumeration is legacy; use Iterator or for-each.

**Q17: Spliterator is used for parallel stream processing.**
Answer: True — Spliterator splits data for parallel processing.

## Code Output

**Q18: What does this code print?**
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
int sum = 0;
for (int n : list) {
    if (n % 2 == 0) sum += n;
}
System.out.println(sum);
```
Answer: 6 — Sum of even numbers: 2 + 4 = 6.

**Q19: What does this code print?**
```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) it.remove();
}
System.out.println(list);
```
Answer: [A, C] — Iterator.remove() safely removes "B".

**Q20: What does this code print?**
```java
List<Integer> list = Arrays.asList(1, 2, 3);
list.forEach(n -> System.out.print(n + " "));
```
Answer: 1 2 3 — forEach prints each element.
