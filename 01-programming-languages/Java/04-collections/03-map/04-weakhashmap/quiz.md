# WeakHashMap Quiz

## Questions

### Q1: What is a WeakHashMap in Java?
**Answer:** A Map implementation that stores weak references to keys, allowing entries to be garbage collected when the key is no longer in ordinary use.

### Q2: What happens when a key in WeakHashMap is garbage collected?
**Answer:** The entire entry (key-value pair) is automatically removed from the map.

### Q3: What type of reference does WeakHashMap use for keys?
**Answer:** WeakReference.

### Q4: What is the primary use case for WeakHashMap?
**Answer:** Implementing caches where entries should be automatically cleaned up when keys are no longer referenced elsewhere.

### Q5: Can WeakHashMap have null keys?
**Answer:** Yes, it allows one null key.

### Q6: Is WeakHashMap thread-safe?
**Answer:** No, it is not synchronized.

### Q7: What is the time complexity of WeakHashMap operations?
**Answer:** O(1) for get(), put(), and remove() operations.

### Q8: How does WeakHashMap differ from HashMap?
**Answer:** WeakHashMap holds weak references to keys, allowing GC to remove entries, while HashMap uses strong references.

### Q9: When is an entry in WeakHashMap eligible for removal?
**Answer:** When the key has no more strong or soft references pointing to it.

### Q10: What is the difference between WeakHashMap and SoftHashMap?
**Answer:** WeakHashMap uses weak references (cleared eagerly), while SoftHashMap uses soft references (cleared only before memory exhaustion).

## Bonus Questions

### Q11: Why doesn't WeakHashMap have a size() that reflects live entries?
**Answer:** GC can remove entries at any time, so size() may count entries that will be removed on the next access.

### Q12: What happens if you use a strong reference to the key after putting it in WeakHashMap?
**Answer:** The entry will not be garbage collected as long as the strong reference exists.
