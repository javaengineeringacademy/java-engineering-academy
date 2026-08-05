# Iterator Pattern (C#)

## Overview

The Iterator pattern provides a way to access elements of a collection sequentially
without exposing its underlying representation. C# provides built-in IEnumerable and
IEnumerator interfaces.

## When to Use

- Accessing collection elements without exposing representation
- Supporting multiple traversal strategies
- Providing uniform interface for different collections
- Enabling foreach iteration

## C# Implementation

### IEnumerable Implementation

```csharp
public class BookCollection : IEnumerable<Book>
{
    private readonly List<Book> _books = new();

    public void Add(Book book) => _books.Add(book);

    public IEnumerator<Book> GetEnumerator() => _books.GetEnumerator();

    IEnumerator IEnumerable.GetEnumerator() => GetEnumerator();
}

public class Book
{
    public string Title { get; set; }
    public string Author { get; set; }
}
```

### Custom Iterator

```csharp
public class Tree<T>
{
    public TreeNode<T> Root { get; set; }

    public IEnumerable<T> InOrder()
    {
        return Traverse(Root);
    }

    private IEnumerable<T> Traverse(TreeNode<T> node)
    {
        if (node == null) yield break;

        foreach (var item in Traverse(node.Left))
            yield return item;

        yield return node.Value;

        foreach (var item in Traverse(node.Right))
            yield return item;
    }
}
```

### Filtered Iterator

```csharp
public static class IteratorExtensions
{
    public static IEnumerable<T> Where<T>(
        this IEnumerable<T> source,
        Func<T, bool> predicate)
    {
        foreach (var item in source)
        {
            if (predicate(item))
                yield return item;
        }
    }
}
```

## Best Practices

- Use yield return for lazy evaluation
- Implement IDisposable for resource cleanup
- Consider IReadOnlyCollection for read-only access
- Use LINQ for common iteration patterns
- Document thread safety of iterators

## Interview Questions

1. What is the difference between IEnumerable and IEnumerator?
2. How does yield return work?
3. Can iterators be infinite sequences?
4. How do you handle iterator disposal?
5. When should you use custom iterator vs LINQ?

## References

- Microsoft Docs: IEnumerable Interface
- "Design Patterns" by Gamma et al.
- "C# in Depth" by Jon Skeet
