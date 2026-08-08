# Algorithms — C Language

## Why It Matters

When you need to sort search results, find shortest paths in networks, compress files, or solve optimization problems, data structures alone aren't enough — you need algorithms to process the data. C's lack of a standard library for complex algorithms means you must understand the fundamentals to implement them correctly and efficiently, choosing the right algorithm for your data size to avoid the difference between milliseconds and hours of runtime.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Sorting, searching, graph traversal, optimization problems | Standard library (`qsort`, `bsearch`) for simple cases |
| When NOT to use | Premature optimization — profile first | Algorithmic improvements beat micro-optimizations |
| Alternatives | C++ `<algorithm>`, Rust iterators, specialized libraries | More abstractions, less control |
| Production Examples | Linux scheduler (rbtree), Redis (quicklist), SQLite (btree) | Custom algorithms for specific workloads |
| Common Mistakes | QuickSort on sorted input (O(n²)), integer overflow in binary search mid | Median-of-three pivot, overflow-safe mid |

## What It Is

Algorithms are step-by-step procedures for solving problems with guaranteed performance characteristics:

| Category | Algorithms | Use Case |
|----------|-----------|----------|
| Sorting | QuickSort, MergeSort, HeapSort, Radix Sort | Ordering data |
| Searching | Linear Search, Binary Search | Finding elements |
| Graph | BFS, DFS, Dijkstra, Bellman-Ford | Network analysis |
| String | KMP, Boyer-Moore, Rabin-Karp | Text processing |
| Dynamic Programming | Fibonacci, Knapsack, Edit Distance | Optimization |
| Cryptographic | AES, SHA-256, RSA | Security |

## Why It Exists

C's efficiency makes it the language of choice for algorithm implementation when performance matters. The Linux kernel, database engines, and cryptography libraries all implement their algorithms in C for maximum speed.

### Architecture: Algorithm Selection Framework

```
Is the data sorted?
├── Yes → Binary Search O(log n)
└── No
    ├── Need stable sort? → MergeSort O(n log n) stable
    ├── Need in-place? → QuickSort O(n log n) avg
    ├── Small data (< 50)? → InsertionSort O(n²) low overhead
    └── Integer data? → RadixSort O(nk)

Is the graph sparse?
├── Yes → Adjacency list + BFS/DFS
└── No → Adjacency matrix + Floyd-Warshall

Need optimal substructure?
├── Yes → Dynamic Programming
└── No → Greedy or Brute Force
```

## Expanded Code Examples

### Sorting Algorithms — Practical Comparison

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

// Bubble Sort — O(n²), stable, in-place
// Good for: nearly sorted data, small arrays (< 50)
void bubble_sort(int arr[], int n) {
    for (int i = 0; i < n - 1; i++) {
        int swapped = 0;
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapped = 1;
            }
        }
        if (!swapped) break;  // Already sorted
    }
}

// Insertion Sort — O(n²), stable, in-place
// Good for: small arrays, nearly sorted data
void insertion_sort(int arr[], int n) {
    for (int i = 1; i < n; i++) {
        int key = arr[i];
        int j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}

// QuickSort — O(n log n) avg, O(n²) worst, not stable
// Good for: general purpose, large arrays
void quick_sort(int arr[], int low, int high) {
    if (low >= high) return;

    int pivot = arr[high];
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (arr[j] <= pivot) {
            int temp = arr[++i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    int temp = arr[i + 1];
    arr[i + 1] = arr[high];
    arr[high] = temp;

    int pi = i + 1;
    quick_sort(arr, low, pi - 1);
    quick_sort(arr, pi + 1, high);
}

// Merge Sort — O(n log n), stable, O(n) extra space
// Good for: linked lists, stable sort needed
void merge(int arr[], int left, int mid, int right) {
    int n1 = mid - left + 1, n2 = right - mid;
    int *L = malloc(n1 * sizeof(int));
    int *R = malloc(n2 * sizeof(int));

    for (int i = 0; i < n1; i++) L[i] = arr[left + i];
    for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

    int i = 0, j = 0, k = left;
    while (i < n1 && j < n2) {
        arr[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
    }
    while (i < n1) arr[k++] = L[i++];
    while (j < n2) arr[k++] = R[j++];

    free(L);
    free(R);
}

void merge_sort(int arr[], int left, int right) {
    if (left >= right) return;
    int mid = left + (right - left) / 2;
    merge_sort(arr, left, mid);
    merge_sort(arr, mid + 1, right);
    merge(arr, left, mid, right);
}
```

### Searching Algorithms

```c
#include <stdio.h>

// Linear Search — O(n)
// Good for: unsorted data, small arrays
int linear_search(const int arr[], int n, int key) {
    for (int i = 0; i < n; i++) {
        if (arr[i] == key) return i;
    }
    return -1;
}

// Binary Search — O(log n)
// Good for: sorted arrays
int binary_search(const int arr[], int n, int key) {
    int low = 0, high = n - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;  // Avoid overflow
        if (arr[mid] == key) return mid;
        if (arr[mid] < key) low = mid + 1;
        else high = mid - 1;
    }
    return -1;
}

// Lower bound — first element >= key
int lower_bound(const int arr[], int n, int key) {
    int low = 0, high = n;
    while (low < high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] < key) low = mid + 1;
        else high = mid;
    }
    return low;
}

// Upper bound — first element > key
int upper_bound(const int arr[], int n, int key) {
    int low = 0, high = n;
    while (low < high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] <= key) low = mid + 1;
        else high = mid;
    }
    return low;
}
```

### Graph Algorithms

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_VERTICES 100

// Adjacency list representation
typedef struct Edge {
    int target;
    int weight;
    struct Edge *next;
} Edge;

typedef struct {
    Edge *adj[MAX_VERTICES];
    int vertices;
} Graph;

void graph_add_edge(Graph *g, int from, int to, int weight) {
    Edge *e = malloc(sizeof(Edge));
    e->target = to;
    e->weight = weight;
    e->next = g->adj[from];
    g->adj[from] = e;
}

// BFS — Level-order traversal, shortest path in unweighted graph
void bfs(Graph *g, int start) {
    int visited[MAX_VERTICES] = {0};
    int queue[MAX_VERTICES], front = 0, rear = 0;

    visited[start] = 1;
    queue[rear++] = start;

    while (front < rear) {
        int v = queue[front++];
        printf("Visit %d\n", v);

        for (Edge *e = g->adj[v]; e; e = e->next) {
            if (!visited[e->target]) {
                visited[e->target] = 1;
                queue[rear++] = e->target;
            }
        }
    }
}

// DFS — Depth-first traversal
void dfs_visit(Graph *g, int v, int visited[]) {
    visited[v] = 1;
    printf("Visit %d\n", v);

    for (Edge *e = g->adj[v]; e; e = e->next) {
        if (!visited[e->target]) {
            dfs_visit(g, e->target, visited);
        }
    }
}

void dfs(Graph *g, int start) {
    int visited[MAX_VERTICES] = {0};
    dfs_visit(g, start, visited);
}

// Dijkstra's shortest path (simplified)
void dijkstra(Graph *g, int start, int dist[]) {
    int visited[MAX_VERTICES] = {0};
    for (int i = 0; i < g->vertices; i++) dist[i] = INT_MAX;
    dist[start] = 0;

    for (int i = 0; i < g->vertices; i++) {
        // Find minimum distance unvisited vertex
        int u = -1;
        for (int v = 0; v < g->vertices; v++) {
            if (!visited[v] && (u == -1 || dist[v] < dist[u]))
                u = v;
        }
        if (u == -1 || dist[u] == INT_MAX) break;
        visited[u] = 1;

        // Relax edges
        for (Edge *e = g->adj[u]; e; e = e->next) {
            if (dist[u] + e->weight < dist[e->target]) {
                dist[e->target] = dist[u] + e->weight;
            }
        }
    }
}
```

### Dynamic Programming

```c
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// Fibonacci with memoization — O(n) time, O(n) space
long fib_memo(int n, long *memo) {
    if (n <= 1) return n;
    if (memo[n] != -1) return memo[n];
    memo[n] = fib_memo(n - 1, memo) + fib_memo(n - 2, memo);
    return memo[n];
}

long fibonacci(int n) {
    long *memo = malloc((n + 1) * sizeof(long));
    for (int i = 0; i <= n; i++) memo[i] = -1;
    long result = fib_memo(n, memo);
    free(memo);
    return result;
}

// Knapsack problem — O(nW) time and space
int knapsack(int W, const int wt[], const int val[], int n) {
    int **dp = malloc((n + 1) * sizeof(int *));
    for (int i = 0; i <= n; i++) {
        dp[i] = calloc(W + 1, sizeof(int));
    }

    for (int i = 1; i <= n; i++) {
        for (int w = 0; w <= W; w++) {
            if (wt[i - 1] <= w) {
                int include = val[i - 1] + dp[i - 1][w - wt[i - 1]];
                int exclude = dp[i - 1][w];
                dp[i][w] = (include > exclude) ? include : exclude;
            } else {
                dp[i][w] = dp[i - 1][w];
            }
        }
    }

    int result = dp[n][W];
    for (int i = 0; i <= n; i++) free(dp[i]);
    free(dp);
    return result;
}

// Edit distance — O(mn) time and space
int edit_distance(const char *s1, const char *s2) {
    int m = strlen(s1), n = strlen(s2);
    int **dp = malloc((m + 1) * sizeof(int *));
    for (int i = 0; i <= m; i++) dp[i] = malloc((n + 1) * sizeof(int));

    for (int i = 0; i <= m; i++) dp[i][0] = i;
    for (int j = 0; j <= n; j++) dp[0][j] = j;

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1[i - 1] == s2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = 1 + (dp[i - 1][j] < dp[i][j - 1] ?
                    (dp[i - 1][j] < dp[i - 1][j - 1] ? dp[i - 1][j] : dp[i - 1][j - 1]) :
                    (dp[i][j - 1] < dp[i - 1][j - 1] ? dp[i][j - 1] : dp[i - 1][j - 1]));
            }
        }
    }

    int result = dp[m][n];
    for (int i = 0; i <= m; i++) free(dp[i]);
    free(dp);
    return result;
}
```

## Production Incidents

### Incident 1: QuickSort Worst Case on Sorted Input

**Problem**: A sorting function takes minutes instead of milliseconds on already-sorted data.

**Cause**: Naive QuickSort picks first element as pivot, causing O(n²) on sorted input:

```c
// Bad: always picks first element as pivot
int partition(int arr[], int low, int high) {
    int pivot = arr[low];  // O(n²) on sorted data
    // ...
}
```

**Solution**: Use median-of-three or random pivot:

```c
// Median-of-three pivot selection
int median_of_three(int arr[], int low, int high) {
    int mid = low + (high - low) / 2;
    if (arr[low] > arr[mid]) { int t = arr[low]; arr[low] = arr[mid]; arr[mid] = t; }
    if (arr[low] > arr[high]) { int t = arr[low]; arr[low] = arr[high]; arr[high] = t; }
    if (arr[mid] > arr[high]) { int t = arr[mid]; arr[mid] = arr[high]; arr[high] = t; }
    return mid;
}
```

### Incident 2: Integer Overflow in Binary Search

**Problem**: Binary search crashes on large arrays due to integer overflow in midpoint calculation.

```c
int mid = (low + high) / 2;  // overflow when low + high > INT_MAX
```

**Solution**: Use overflow-safe midpoint:

```c
int mid = low + (high - low) / 2;  // No overflow
```

## Production Checklist

- [ ] Choose algorithm based on data size and characteristics
- [ ] Handle edge cases (empty input, single element, all same)
- [ ] Test with worst-case inputs
- [ ] Profile before optimizing
- [ ] Use standard library (`qsort`, `bsearch`) when available
- [ ] Check for integer overflow in index calculations
- [ ] Free any allocated memory in algorithms

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Implements basic sort and search | Bubble sort, linear search, binary search |
| **Intermediate** | Implements tree and graph algorithms | BFS, DFS, BST operations |
| **Advanced** | Masters dynamic programming | Memoization, tabulation, optimization |
| **Expert** | Optimizes for cache, parallelism, and specific hardware | SIMD sorting, cache-oblivious algorithms |

## Common Myths Debunked

1. **Myth**: QuickSort is always fastest
   **Truth**: MergeSort is better for linked lists, insertion sort for small arrays, radix sort for integers.

2. **Myth**: Recursion is always slower
   **Truth**: Tail recursion can be optimized to iteration by the compiler. But manual iteration is more portable.

3. **Myth**: Big-O is the only thing that matters
   **Truth**: Constant factors, cache behavior, and branch prediction matter enormously in practice. O(n) with bad cache behavior can be slower than O(n log n) with good locality.

## One-Minute Revision

| Algorithm | Time (avg) | Time (worst) | Space | Stable | Use Case |
|-----------|------------|--------------|-------|--------|----------|
| Bubble Sort | O(n²) | O(n²) | O(1) | Yes | Nearly sorted |
| Insertion Sort | O(n²) | O(n²) | O(1) | Yes | Small arrays |
| QuickSort | O(n log n) | O(n²) | O(log n) | No | General purpose |
| MergeSort | O(n log n) | O(n log n) | O(n) | Yes | Linked lists |
| Binary Search | O(log n) | O(log n) | O(1) | Yes | Sorted arrays |
| BFS/DFS | O(V+E) | O(V+E) | O(V) | — | Graph traversal |

## Related Topics

- [Data Structures](../06-data-structures/README.md) — Structures that algorithms operate on
- [Performance](../12-performance/README.md) — Profiling and optimizing algorithm performance
- [Best Practices](../15-best-practices/README.md) — Coding standards for algorithm implementation
