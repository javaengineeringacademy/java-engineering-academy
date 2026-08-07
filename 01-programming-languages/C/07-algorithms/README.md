# Algorithms — C Language

## What it is
Algorithms are step-by-step procedures for solving problems efficiently.

## Why it exists
To process data and solve problems with optimal time and space complexity.

## When to use it
When you need to sort, search, optimize, or process data.

## How it works

### Sorting Algorithms

```c
// Bubble Sort
void bubble_sort(int arr[], int n) {
    for (int i = 0; i < n-1; i++)
        for (int j = 0; j < n-i-1; j++)
            if (arr[j] > arr[j+1])
                swap(&arr[j], &arr[j+1]);
}

// Quick Sort
void quick_sort(int arr[], int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quick_sort(arr, low, pi-1);
        quick_sort(arr, pi+1, high);
    }
}
```

### Search Algorithms

```c
// Linear Search
int linear_search(int arr[], int n, int key) {
    for (int i = 0; i < n; i++)
        if (arr[i] == key) return i;
    return -1;
}

// Binary Search
int binary_search(int arr[], int n, int key) {
    int low = 0, high = n-1;
    while (low <= high) {
        int mid = (low + high) / 2;
        if (arr[mid] == key) return mid;
        if (arr[mid] < key) low = mid + 1;
        else high = mid - 1;
    }
    return -1;
}
```

### Graph Algorithms

```c
// BFS
void bfs(Graph *g, int start) {
    Queue q;
    enqueue(&q, start);
    visited[start] = 1;
    while (!empty(&q)) {
        int v = dequeue(&q);
        // process v
        for (Edge *e = g->adj[v]; e; e = e->next)
            if (!visited[e->target]) {
                visited[e->target] = 1;
                enqueue(&q, e->target);
            }
    }
}
```

### Dynamic Programming

```c
// Fibonacci with memoization
int fib(int n, int *memo) {
    if (n <= 1) return n;
    if (memo[n] != -1) return memo[n];
    memo[n] = fib(n-1, memo) + fib(n-2, memo);
    return memo[n];
}
```

## Production Checklist

- [ ] Choose appropriate algorithm for problem
- [ ] Consider time and space complexity
- [ ] Test with edge cases
- [ ] Optimize for cache performance
- [ ] Use standard library when available

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Implements basic sort and search |
| Intermediate | Implements tree and graph algorithms |
| Advanced | Masters dynamic programming |

## Common Myths

1. **Myth**: QuickSort is always fastest
   **Truth**: MergeSort is better for linked lists; insertion sort for small arrays

2. **Myth**: Recursion is always slower
   **Truth**: Tail recursion can be optimized to iteration

## One-Minute Revision

| Algorithm | Time (avg) | Space | Use Case |
|-----------|------------|-------|----------|
| Bubble Sort | O(n²) | O(1) | Small arrays |
| QuickSort | O(n log n) | O(log n) | General purpose |
| MergeSort | O(n log n) | O(n) | Linked lists |
| Binary Search | O(log n) | O(1) | Sorted arrays |
| BFS | O(V+E) | O(V) | Level-order traversal |
| DFS | O(V+E) | O(V) | Path finding |

## Related Topics

- [Data Structures](../06-data-structures/README.md)
- [Performance](../12-performance/README.md)
