# Iterator Pattern

The Iterator pattern provides a way to access elements of a collection sequentially without exposing its underlying representation.

## Table of Contents

1. [Concepts](#concepts)
2. [Custom Iterator](#custom-iterator)
3. [Iterable Interface](#iterable-interface)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Iterator?

Iterator provides a uniform way to traverse a collection without exposing its structure.

```
Client ──▶ Iterator ──▶ Collection
         (hasNext, next)
```

### When to Use

- Hide collection implementation
- Support multiple traversal strategies
- Provide uniform iteration interface

---

## Custom Iterator

### Tree Iterator

```java
public class TreeNode<T> {
    private final T data;
    private final List<TreeNode<T>> children = new ArrayList<>();

    public TreeNode(T data) { this.data = data; }
    public void addChild(TreeNode<T> child) { children.add(child); }
    public T getData() { return data; }
    public List<TreeNode<T>> getChildren() { return children; }

    public Iterator<T> depthFirstIterator() {
        return new Iterator<T>() {
            private final Stack<TreeNode<T>> stack = new Stack<>();
            { stack.push(TreeNode.this); }

            @Override
            public boolean hasNext() { return !stack.isEmpty(); }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                TreeNode<T> node = stack.pop();
                List<TreeNode<T>> kids = node.getChildren();
                for (int i = kids.size() - 1; i >= 0; i--) {
                    stack.push(kids.get(i));
                }
                return node.getData();
            }
        };
    }

    public Iterator<T> breadthFirstIterator() {
        return new Iterator<T>() {
            private final Queue<TreeNode<T>> queue = new LinkedList<>();
            { queue.add(TreeNode.this); }

            @Override
            public boolean hasNext() { return !queue.isEmpty(); }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                TreeNode<T> node = queue.poll();
                queue.addAll(node.getChildren());
                return node.getData();
            }
        };
    }
}

// Usage
TreeNode<String> root = new TreeNode<>("Root");
root.addChild(new TreeNode<>("Child1"));
root.addChild(new TreeNode<>("Child2"));

Iterator<String> df = root.depthFirstIterator();
while (df.hasNext()) System.out.println(df.next());
```

---

## Iterable Interface

### Implementing Iterable

```java
public class Range implements Iterable<Integer> {
    private final int start;
    private final int end;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int current = start;

            @Override
            public boolean hasNext() { return current < end; }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return current++;
            }
        };
    }
}

// Works with for-each
for (int i : new Range(1, 5)) {
    System.out.println(i);  // 1, 2, 3, 4
}
```

---

## Best Practices

### Do

```java
// 1. Implement Iterator interface
public class MyIterator<T> implements Iterator<T> {
    @Override public boolean hasNext() { ... }
    @Override public T next() { ... }
}

// 2. Throw NoSuchElementException when exhausted
@Override
public T next() {
    if (!hasNext()) throw new NoSuchElementException();
    // ...
}

// 3. Implement Iterable for for-each support
public class MyCollection<T> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() { ... }
}
```

### Don't

```java
// 1. Don't modify collection during iteration
// ConcurrentModificationException!

// 2. Don't ignore hasNext() check
// Always check before calling next()
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Iterator** | Uniform traversal interface |
| **hasNext** | Check if more elements |
| **next** | Get next element |
| **Iterable** | Enables for-each loop |
| **Traversal** | DFS, BFS, custom strategies |
| **Encapsulation** | Hide collection structure |
