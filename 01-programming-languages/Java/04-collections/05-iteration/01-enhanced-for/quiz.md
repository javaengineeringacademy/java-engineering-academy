# Enhanced For Loop Quiz

## Questions

### Q1: What is the enhanced for loop in Java?
**Answer:** Also known as the for-each loop, it simplifies iteration over arrays and collections: for (Type var : collection).

### Q2: Can you modify elements of an array using the enhanced for loop?
**Answer:** No, the enhanced for loop only provides read access to elements.

### Q3: What interface must a class implement to be used with the enhanced for loop?
**Answer:** The Iterable interface.

### Q4: What is the difference between for-each and traditional for loop?
**Answer:** for-each hides the index/iterator, is more readable, but cannot access the index directly.

### Q5: Can you use the enhanced for loop with a Map?
**Answer:** Not directly, but you can iterate over entrySet(), keySet(), or values().

### Q6: What happens if you try to remove an element during enhanced for loop iteration?
**Answer:** It throws ConcurrentModificationException. Use Iterator.remove() instead.

### Q7: Can the enhanced for loop iterate over a String?
**Answer:** Yes, String implements Iterable and you can iterate over its characters.

### Q8: What is the syntax for enhanced for loop over a List?
**Answer:** for (String s : myList) { System.out.println(s); }

### Q9: Can the enhanced for loop be used with primitive arrays?
**Answer:** Yes, it works with arrays of all types including primitives.

### Q10: Is the enhanced for loop more or less performant than a traditional for loop?
**Answer:** It has similar performance as it uses an iterator under the hood.

## Bonus Questions

### Q11: What is the bytecode equivalent of the enhanced for loop?
**Answer:** It is converted to an Iterator-based loop at compile time.

### Q12: Can you use break and continue with the enhanced for loop?
**Answer:** Yes, break and continue work normally within enhanced for loops.
