# Iterator Pattern

## Overview
The Iterator pattern provides a way to access elements of a collection sequentially without exposing its underlying representation. It separates the traversal algorithm from the collection structure.

## When to Use
- Accessing collection elements without exposing internal structure
- Supporting multiple traversal methods for a collection
- Providing a uniform interface for different collection types
- Collections with complex internal structures (trees, graphs)

## Code Structure
```
Iterator<T> (interface)    Iterable<T> (interface)
    |                          |
BookIterator (concrete)   BookCollection (concrete)
```

## Key Benefits
- Supports traversal of complex data structures
- Simplifies the collection interface
- Multiple iterators can traverse same collection simultaneously
- Follows Single Responsibility Principle

## Common Mistakes
- Modifying collection during iteration causing ConcurrentModificationException
- Not implementing reset when needed
- Forgetting to handle empty collections

## Interview Questions
1. What is the difference between Iterator and Iterable?
2. How does Java's Iterator differ from ListIterator?
3. What happens when collection is modified during iteration?
4. How would you implement a reverse iterator?

## Performance

Iterator traversal is O(n) for linear collections, O(log n) for trees. The overhead is one virtual method call per element (~5ns). Java's `ArrayList` iterator is cache-friendly; `LinkedList` iterator has poor locality. Concurrent iterators (CopyOnWriteArrayList) snapshot the array — O(n) copy but no locking during iteration. `Spliterator` enables parallel iteration for large datasets.

## Examples

```java
// Custom tree iterator
class TreeNode<T> {
    T data;
    List<TreeNode<T>> children = new ArrayList<>();
    
    TreeNode(T data) { this.data = data; }
    
    void addChild(TreeNode<T> child) { children.add(child); }
    
    // Depth-first iterator
    Iterator<T> depthFirstIterator() {
        return new Iterator<T>() {
            private final Deque<TreeNode<T>> stack = new ArrayDeque<>();
            { stack.push(TreeNode.this); }
            
            @Override
            public boolean hasNext() { return !stack.isEmpty(); }
            
            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                TreeNode<T> node = stack.pop();
                // Push children in reverse for left-to-right order
                for (int i = node.children.size() - 1; i >= 0; i--) {
                    stack.push(node.children.get(i));
                }
                return node.data;
            }
        };
    }
}

// Usage
TreeNode<String> root = new TreeNode<>("Root");
TreeNode<String> child1 = new TreeNode<>("Child1");
TreeNode<String> child2 = new TreeNode<>("Child2");
root.addChild(child1);
root.addChild(child2);
child1.addChild(new TreeNode<>("Grandchild"));

Iterator<String> iter = root.depthFirstIterator();
while (iter.hasNext()) {
    System.out.println(iter.next());
}
// Output: Root, Child1, Grandchild, Child2
```

## Internal Working

The iterator provides a uniform interface (`hasNext()`, `next()`) for traversing collections regardless of internal structure. Each collection implements its own iterator that knows how to walk its data structure. The client uses the iterator interface without knowing whether the collection is an array, linked list, or tree. Java's `Iterable` interface enables enhanced for-each loop support.

## Why This Concept Exists

Collections have diverse internal structures: arrays, linked lists, trees, hash tables. Exposing the internal structure for traversal couples clients to implementation details. Iterator provides a uniform interface — the client calls `hasNext()` and `next()` regardless of the collection type. This enables polymorphic iteration and multiple traversal strategies on the same collection.

## Pitfalls

1. **ConcurrentModificationException**: Modifying the collection during iteration fails fast — use `CopyOnWriteArrayList` or `ConcurrentHashMap`
2. **One-shot**: Standard iterators are single-pass — cannot restart without creating a new iterator
3. **Remove safety**: `Iterator.remove()` is optional — some iterators throw `UnsupportedOperationException`
4. **Infinite iterators**: Lazy iterators (generators) must be careful to terminate
5. **Thread safety**: Iterators are not thread-safe — synchronize externally in multi-threaded contexts

## References

- [Refactoring.Guru - Iterator Pattern](https://refactoring.guru/design-patterns/iterator)
- [Java Iterator Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Iterator.html)
- [Java Spliterator](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Spliterator.html)
