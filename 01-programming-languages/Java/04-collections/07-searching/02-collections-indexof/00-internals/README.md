# Collections.indexOf Internals

## How Collections.indexOf Works

```
Collections.indexOf performs linear search on List:

1. Iterate through list from index 0
2. Compare each element with target using equals()
3. Return first matching index
4. Return -1 if not found
```

## Step-by-Step Example

```
List: ["Java", "Python", "C++", "Java"]
Target: "Java"

Step 1: Compare "Java" with "Java" → Match at index 0
Return 0
```

## Implementation in Java

```java
// ArrayList.indexOf
public int indexOf(Object o) {
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i] == null) return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i])) return i;
    }
    return -1;
}

// LinkedList.indexOf
public int indexOf(Object o) {
    int index = 0;
    if (o == null) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (x.item == null) return index;
            index++;
        }
    } else {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o.equals(x.item)) return index;
            index++;
        }
    }
    return -1;
}
```

## Time Complexity

```
Best Case:    O(1) - target is first element
Worst Case:   O(n) - target is last element or not present
Average Case: O(n) - target is somewhere in middle

Space Complexity: O(1) - no extra space needed
```

## Collections.binarySearch vs indexOf

```
┌──────────────────┬─────────────┬─────────────┐
│ Method           │ indexOf     │ binarySearch│
├──────────────────┼─────────────┼─────────────┤
│ Algorithm        │ Linear      │ Binary      │
│ Time             │ O(n)        │ O(log n)    │
│ Requires sorted  │ No          │ Yes         │
│ Returns          │ First index │ Any index   │
│ Null handling    │ Special case│ Exception   │
└──────────────────┴─────────────┴─────────────┘
```

## Null Handling

```
indexOf handles null specially:

List: [null, "Java", null, "Python"]
indexOf(null) → 0 (first null)
indexOf("Java") → 1

binarySearch does NOT handle null:
Collections.binarySearch(list, null) → NullPointerException
```

## lastIndexOf Implementation

```java
// ArrayList.lastIndexOf
public int lastIndexOf(Object o) {
    if (o == null) {
        for (int i = size - 1; i >= 0; i--)
            if (elementData[i] == null) return i;
    } else {
        for (int i = size - 1; i >= 0; i--)
            if (o.equals(elementData[i])) return i;
    }
    return -1;
}
```

## Performance Comparison

```
┌──────────────────┬─────────────┬─────────────┬─────────────┐
│ Operation        │ ArrayList   │ LinkedList  │ HashSet     │
├──────────────────┼─────────────┼─────────────┼─────────────┤
│ indexOf          │ O(n)        │ O(n)        │ O(1)        │
│ contains         │ O(n)        │ O(n)        │ O(1)        │
│ get(index)       │ O(1)        │ O(n)        │ N/A         │
└──────────────────┴─────────────┴─────────────┴─────────────┘
```

## When to Use Each

### Use indexOf when:
- List is small (< 100 elements)
- Need index of first occurrence
- List is not sorted
- Need to handle null values

### Use binarySearch when:
- List is sorted
- List is large (> 1000 elements)
- Need O(log n) performance
- Don't need first occurrence

### Use HashSet when:
- Only need to check existence (contains)
- Don't need index
- Can afford O(n) extra space
- Need O(1) contains operations

## Thread Safety

```
indexOf is NOT thread-safe:
- List may be modified during iteration
- Can throw ConcurrentModificationException

Safe alternatives:
1. synchronized(list) { list.indexOf(target); }
2. CopyOnWriteArrayList (thread-safe List)
3. Collections.synchronizedList(list)
```

## Key Implementation Details

1. **equals() semantics** - Uses Object.equals(), not ==
2. **Null handling** - indexOf handles null, binarySearch doesn't
3. **First occurrence** - Returns first matching index
4. **Generics** - Works with any type via equals()
5. **Fail-fast** - Throws ConcurrentModificationException if list modified