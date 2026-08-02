# LinkedHashSet

## Introduction

LinkedHashSet is a Set implementation that maintains insertion order using a linked list alongside the hash table.

## Learning Objectives

- Create and use LinkedHashSet
- Understand insertion order maintenance
- Learn LinkedHashSet's performance characteristics
- Know when to use LinkedHashSet vs HashSet

## Prerequisites

- HashSet
- Set Interface

## Why This Matters

LinkedHashSet provides the same O(1) performance as HashSet while maintaining insertion order, making it useful when order matters.

## Syntax

```java
// Creating LinkedHashSet
Set<E> set = new LinkedHashSet<>();           // Empty
Set<E> set = new LinkedHashSet<>(capacity);   // With initial capacity
Set<E> set = new LinkedHashSet<>(collection); // From another collection

// Same operations as HashSet
set.add(element);
set.remove(element);
set.contains(element);
set.size();
```

## Examples

```java
// Example 1: Insertion order maintained
Set<String> names = new LinkedHashSet<>();
names.add("Charlie");
names.add("Alice");
names.add("Bob");

System.out.println(names);  // [Charlie, Alice, Bob] - insertion order

// Example 2: LinkedHashSet vs HashSet
Set<Integer> hashSet = new HashSet<>();
Set<Integer> linkedHashSet = new LinkedHashSet<>();

for (int i = 5; i > 0; i--) {
    hashSet.add(i);
    linkedHashSet.add(i);
}

System.out.println("HashSet: " + hashSet);           // [1, 2, 3, 4, 5] (order may vary)
System.out.println("LinkedHashSet: " + linkedHashSet); // [5, 4, 3, 2, 1] - insertion order

// Example 3: Recent items tracking
public class RecentItems<T> {
    private final int maxSize;
    private final Set<T> items;

    public RecentItems(int maxSize) {
        this.maxSize = maxSize;
        this.items = new LinkedHashSet<>(maxSize, 0.75f);
    }

    public void add(T item) {
        if (items.size() >= maxSize) {
            Iterator<T> iterator = items.iterator();
            iterator.next();
            iterator.remove();
        }
        items.add(item);
    }

    public List<T> getRecent() {
        return new ArrayList<>(items);
    }
}

RecentItems<String> recent = new RecentItems<>(3);
recent.add("First");
recent.add("Second");
recent.add("Third");
recent.add("Fourth");
System.out.println(recent.getRecent());  // [Second, Third, Fourth]
```

## Exercises

1. Create a LinkedHashSet that maintains strings in insertion order.
2. Implement an LRU cache using LinkedHashSet.
3. Write a method that returns elements in insertion order from a mixed collection.

## Interview Questions

- What is the difference between HashSet and LinkedHashSet?
- What is the performance overhead of LinkedHashSet compared to HashSet?
- When would you use LinkedHashSet over TreeSet?

## Common Pitfalls

- Not realizing LinkedHashSet is slightly slower than HashSet
- Assuming LinkedHashSet is sorted (it's insertion-ordered)
- Using LinkedHashSet when order doesn't matter

## Best Practices

- Use LinkedHashSet when insertion order matters
- Use HashSet when order doesn't matter (better performance)
- Use TreeSet when you need sorted order
- Consider memory overhead for large datasets

## Real World Applications

- Maintaining recently accessed items
- Preserving insertion order for display
- Implementing LRU caches
- Tracking ordered unique elements

## References

- [LinkedHashSet Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/LinkedHashSet.html)
- [Java Collections Tutorial](https://docs.oracle.com/en/java/javase/21/collections/implementations/hashset.html)

## Summary

In this topic, you learned about LinkedHashSet and its ability to maintain insertion order while providing O(1) performance. It's ideal when you need both uniqueness and order. Practice with the exercises before learning about TreeSet.
