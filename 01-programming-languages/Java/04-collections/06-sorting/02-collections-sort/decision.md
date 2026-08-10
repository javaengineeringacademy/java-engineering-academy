# Collections.sort() Decision Guide

## Decision Tree

```
Need to sort a list?
├── Need to sort List? → Collections.sort()
├── Need to sort array? → Arrays.sort()
├── Need sorted copy? → stream().sorted().collect()
├── Need to sort LinkedList? → Convert to ArrayList first
└── Need parallel sort? → Arrays.parallelSort()
```

## Comparison Matrix

| Method | Modifies Original | Time | Space | Use Case |
|--------|-------------------|------|-------|----------|
| Collections.sort() | Yes | O(n log n) | O(n) | Sort List in-place |
| List.sort() | Yes | O(n log n) | O(n) | Sort List in-place |
| Arrays.sort() | Yes | O(n log n) | O(log n) | Sort array in-place |
| stream().sorted() | No | O(n log n) | O(n) | Sorted copy |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Sort List in-place | Collections.sort() | Modifies original list |
| Sort array in-place | Arrays.sort() | Modifies original array |
| Sorted copy | stream().sorted().collect() | Doesn't modify original |
| Sort LinkedList | Convert to ArrayList first | LinkedList is slow for random access |
| Parallel sort | Arrays.parallelSort() | Multi-threaded |

## Production Recommendations

> **Use Collections.sort() for List sorting** — it's TimSort, O(n log n) and stable.

> **Use List.sort() for modern code** — it's equivalent to Collections.sort() but more readable.

> **Pre-size your list** — avoid resizing during the sort.

> **Use stream().sorted().collect() for sorted copies** — don't modify the original list.

## Engineering Trade-offs

| Trade-off | Collections.sort() | Alternative |
|-----------|-------------------|-------------|
| In-place vs Copy | Modifies original | stream().sorted(): creates copy |
| Simplicity vs Performance | Simple | stream().sorted(): more verbose |
| Immutability vs Mutability | Mutable | stream().sorted(): immutable |
| Sequential vs Parallel | Sequential | Arrays.parallelSort(): parallel |

## Common Code Review Comments

- "Use List.sort() instead of Collections.sort() for modern code."
- "Pre-size the list before sorting."
- "Use stream().sorted().collect() if you need a sorted copy."
- "This sort is O(n^2) — use Collections.sort() instead."

## Common Production Mistakes

> Notice: Collections.sort() modifies the original list — use stream().sorted().collect() if you need a sorted copy.

> Notice: Collections.sort() requires random access — for LinkedList, convert to ArrayList first for better performance.

> Notice: Collections.sort() throws ClassCastException if elements are not mutually comparable.

> Notice: Collections.sort() throws UnsupportedOperationException if the list is immutable.