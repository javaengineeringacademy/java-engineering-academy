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

### Incident 3: Stack Overflow from Deep Recursion in DFS

**Problem**: A depth-first search on a deep graph (100,000+ nodes) crashes with stack overflow.

```c
void dfs(Graph *g, int node, bool *visited) {
    visited[node] = true;
    process(node);
    for (int i = 0; i < g->adj_count[node]; i++) {
        int neighbor = g->adj[node][i];
        if (!visited[neighbor]) {
            dfs(g, neighbor, visited);  // Recursive: stack grows with depth
        }
    }
}
```

**Cause**: Each recursive call adds a stack frame. For a graph with depth 100,000, the stack overflows.

**Impact**: Segmentation fault, crash.

**Solution**: Convert to iterative DFS using an explicit stack:

```c
void dfs_iterative(Graph *g, int start) {
    bool *visited = calloc(g->num_vertices, sizeof(bool));
    Stack *stack = stack_create(g->num_vertices);
    
    stack_push(stack, start);
    while (!stack_empty(stack)) {
        int node = stack_pop(stack);
        if (visited[node]) continue;
        visited[node] = true;
        process(node);
        for (int i = g->adj_count[node] - 1; i >= 0; i--) {
            int neighbor = g->adj[node][i];
            if (!visited[neighbor]) {
                stack_push(stack, neighbor);
            }
        }
    }
    stack_destroy(stack);
    free(visited);
}
```

**Prevention**: Use iterative algorithms for deep graphs; set stack size with `-Wl,--stack,size` on Windows or `ulimit -s` on Linux.

---

### Incident 4: Hash Function Distribution Causing Hotspot

**Problem**: A hash function produces poor distribution, causing one bucket to receive 80% of entries.

```c
size_t bad_hash(int key) {
    return key % 10;  // Only 10 buckets, poor distribution
}
```

**Cause**: Simple modulo hash with small table size and non-random key patterns.

**Impact**: Degraded performance (O(n) lookups), load imbalance.

**Solution**: Use better hash functions and table sizing:

```c
size_t better_hash(int key, size_t table_size) {
    // Multiplication method
    double A = 0.6180339887;  // (sqrt(5) - 1) / 2
    double val = key * A;
    val = val - (long long)val;  // Fractional part
    return (size_t)(table_size * val);
}
```

**Prevention**: Use established hash functions (FNV-1a, MurmurHash); size tables as prime numbers; monitor bucket distribution.

---

### Incident 5: Dijkstra's Algorithm with Negative Weights

**Problem**: Dijkstra's algorithm produces incorrect shortest paths when the graph contains negative edge weights.

```c
// Dijkstra's assumes all edges are non-negative
// With negative edges, it may return suboptimal paths
```

**Cause**: Dijkstra's greedy approach doesn't reconsider nodes once finalized. Negative edges can create shorter paths through previously finalized nodes.

**Impact**: Incorrect shortest path calculation, wrong routing decisions.

**Solution**: Use Bellman-Ford or detect negative cycles:

```c
bool bellman_ford(Graph *g, int source, int *dist) {
    // Initialize distances
    for (int i = 0; i < g->num_vertices; i++)
        dist[i] = INT_MAX;
    dist[source] = 0;
    
    // Relax edges V-1 times
    for (int i = 0; i < g->num_vertices - 1; i++) {
        for (int j = 0; j < g->num_edges; j++) {
            int u = g->edges[j].u;
            int v = g->edges[j].v;
            int w = g->edges[j].weight;
            if (dist[u] != INT_MAX && dist[u] + w < dist[v])
                dist[v] = dist[u] + w;
        }
    }
    
    // Check for negative cycles
    for (int j = 0; j < g->num_edges; j++) {
        int u = g->edges[j].u;
        int v = g->edges[j].v;
        int w = g->edges[j].weight;
        if (dist[u] != INT_MAX && dist[u] + w < dist[v])
            return true;  // Negative cycle detected
    }
    return false;
}
```

**Prevention**: Detect negative edges before using Dijkstra; use Bellman-Ford for graphs with negative weights; validate graph properties.

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

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| QuickSort worst case on sorted input (O(n²)) | Generate sorted test input | Test with already-sorted arrays; verify pivot selection uses median-of-three or random pivot |
| Binary search integer overflow in midpoint | Add overflow-safe assertion | Use `int mid = low + (high - low) / 2` instead of `(low + high) / 2`; test with `INT_MAX` boundaries |
| Graph algorithm infinite loop (missing visited check) | Print visited array at each step | Add debug prints showing `visited[v]` before and after each node visit |
| Dynamic programming table not properly initialized | Print DP table after execution | Dump the DP table after computation; verify base cases are correctly set at indices 0 and 0 |
| Merge sort memory leak (forgotten `free`) | AddressSanitizer | Compile with `-fsanitize=address`; detects leaked temporary arrays in merge function |

## Code Review Checklist

- [ ] Edge cases handled: empty input, single element, all identical values
- [ ] Integer overflow checked in index/midpoint calculations (`low + (high - low) / 2`)
- [ ] Standard library used when available (`qsort`, `bsearch`) unless custom implementation is required
- [ ] All allocated memory freed in algorithms (merge sort temp arrays, DP tables)
- [ ] Worst-case inputs tested (sorted arrays for QuickSort, dense graphs for Dijkstra)
- [ ] Algorithm complexity documented in comments (time and space)
- [ ] No unnecessary recursion (convert to iterative for deep recursion risk)

## Architecture Considerations

Algorithms are the computational engine behind data structures. The choice of algorithm depends on data size, access patterns, and performance requirements. For most production systems, use the standard library (`qsort`, `bsearch`) and profile before implementing custom algorithms. Algorithmic improvements (O(n²) → O(n log n)) dwarf micro-optimizations — choose the right algorithm first.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| QuickSort | General-purpose in-place sorting | O(n log n) average but O(n²) worst case; not stable |
| MergeSort | Stable sort, linked lists | O(n log n) guaranteed but O(n) extra space |
| Binary search | Lookup in sorted data | O(log n) but requires sorted input; beware integer overflow in midpoint |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Integer overflow in binary search midpoint | Incorrect comparison, out-of-bounds access | Use `low + (high - low) / 2` instead of `(low + high) / 2` |
| Hash collision DoS in hash-based algorithms | O(n) degradation, denial of service | Use randomized hash functions (SipHash); limit chain length |
| Unbounded recursion in divide-and-conquer | Stack overflow, crash | Convert to iterative implementation; set recursion depth limits |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `qsort`/`bsearch` standard library, `_Bool` | Use standard library for simple sorting; implement custom only when needed |
| C99 → C11 | Added `<stdatomic.h>` for lock-free algorithms, `<threads.h>` | Use atomics for concurrent algorithms; use `<threads.h>` for portable threading |
| C11 → C23 | Added `typeof`, improved `_Generic`, `constexpr` | Use `typeof` for type-generic algorithm macros; use `constexpr` for compile-time constants |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| `qsort`/`bsearch` (standard library) | C89 | Standard — use for simple sorting/searching |
| `<stdatomic.h>` for lock-free algorithms | C11 | Standard — use for concurrent data structures |
| `typeof` for type-generic operations | C23 (standardized) | Use for type-safe algorithm macros |
| `restrict` for pointer aliasing optimization | C99 | Standard — add to hot-path pointer parameters |

## Interview Questions

1. **What is the worst case of QuickSort and how do you mitigate it?**: QuickSort is O(n²) on already-sorted or reverse-sorted input when the pivot is poorly chosen (first/last element). Mitigate with median-of-three pivot selection or random pivot. Alternatively, use IntroSort (switch to HeapSort when recursion depth exceeds O(log n)).
2. **How do you prevent integer overflow in binary search?**: Use `int mid = low + (high - low) / 2` instead of `(low + high) / 2`. The latter overflows when `low + high > INT_MAX`. For unsigned types, the same formula applies.
3. **When is MergeSort preferred over QuickSort?**: MergeSort is preferred for linked lists (no random access needed, O(1) extra space on linked lists), when stability is required (preserves relative order of equal elements), or when O(n log n) worst-case guarantee is needed.
4. **How does cache behavior affect algorithm performance?**: Algorithms with sequential memory access (arrays) have better cache locality than pointer-chasing algorithms (linked lists). An O(n) algorithm with poor cache behavior can be slower than an O(n log n) algorithm with good locality. Block processing improves cache utilization.
5. **What is memoization and when should you use it?**: Memoization caches results of recursive function calls to avoid redundant computation. Use it for problems with optimal substructure and overlapping subproblems (Fibonacci, edit distance, knapsack). It trades O(n) or O(n²) space for O(n) or O(n²) time reduction.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Introduction to Algorithms (CLRS)](https://mitpress.mit.edu/9780262046305/introduction-to-algorithms/)
- [The Art of Computer Programming (Knuth)](https://www-cs-faculty.stanford.edu/~knuth/taocp.html)

## Overview

The Algorithms module covers fundamental algorithms for sorting, searching, graph traversal, and dynamic programming. C's lack of a standard library for complex algorithms means you must understand the fundamentals to implement them correctly and efficiently.

## Learning Objectives

- Implement sorting algorithms (QuickSort, MergeSort, HeapSort)
- Apply searching algorithms (Linear, Binary Search)
- Traverse graphs using BFS and DFS
- Solve problems with dynamic programming
- Analyze algorithm complexity (Big O notation)

## Prerequisites

- Completion of Module 06 (Data Structures)
- Understanding of arrays and pointers
- Basic mathematical concepts

## History

- **1972** — Sorting algorithms implemented in early C
- **1978** — K&R C documented `qsort` and `bsearch`
- **1989** — ANSI C standardized library functions
- **1999** — C99 added `<stdint.h>` for fixed-width types
- **2011** — C11 added `<stdalign.h>` for alignment
- **2023** — C23 added improved type inference

## Production Notes

- **Where is it used?** Databases, operating systems, compilers, search engines
- **Why is it useful?** Efficient data processing, optimized performance
- **When should it be avoided?** Use standard library for simple cases (`qsort`, `bsearch`)
- **Alternative?** C++ `<algorithm>`, Rust iterators, specialized libraries

## Core Concepts

### Algorithm Categories

| Category | Algorithms | Use Case |
|----------|-----------|----------|
| Sorting | QuickSort, MergeSort, HeapSort, Radix Sort | Ordering data |
| Searching | Linear Search, Binary Search | Finding elements |
| Graph | BFS, DFS, Dijkstra, Bellman-Ford | Network analysis |
| String | KMP, Boyer-Moore, Rabin-Karp | Text processing |
| Dynamic Programming | Fibonacci, Knapsack, Edit Distance | Optimization |

### Time Complexity Comparison

| Algorithm | Best | Average | Worst | Space |
|-----------|------|---------|-------|-------|
| QuickSort | O(n log n) | O(n log n) | O(n²) | O(log n) |
| MergeSort | O(n log n) | O(n log n) | O(n log n) | O(n) |
| HeapSort | O(n log n) | O(n log n) | O(n log n) | O(1) |
| Binary Search | O(1) | O(log n) | O(log n) | O(1) |

## Internal Working

### QuickSort Partitioning

```
Array: [3, 6, 8, 10, 1, 2, 1]
Pivot: 1
Partition: [1, 1, 8, 10, 6, 3, 2]
         <1  =1  >1
Recurse on left and right partitions
```

### BFS vs DFS

```
BFS (Queue): Visit neighbors level by level
DFS (Stack): Go deep, then backtrack

Graph:
    A
   / \
  B   C
 / \
D   E

BFS: A B C D E
DFS: A B D E C
```

## Syntax

```c
// qsort comparison function
int compare(const void *a, const void *b) {
    return (*(int *)a - *(int *)b);
}

// Using qsort
int arr[] = {5, 2, 8, 1, 9};
qsort(arr, 5, sizeof(int), compare);

// Binary search
int bsearch(const void *key, const void *base, 
            size_t nmemb, size_t size,
            int (*compar)(const void *, const void *));

// Linked list sort
void merge_sort(struct Node **head) {
    // Implementation
}
```

## Examples

### Easy Example: Linear Search

```c
#include <stdio.h>

int linear_search(int *arr, int n, int target) {
    for (int i = 0; i < n; i++) {
        if (arr[i] == target) return i;
    }
    return -1;
}

int main(void) {
    int arr[] = {1, 2, 3, 4, 5};
    int idx = linear_search(arr, 5, 3);
    printf("Found at index: %d\n", idx);
    return 0;
}
```

### Medium Example: QuickSort

```c
#include <stdio.h>

void swap(int *a, int *b) {
    int t = *a; *a = *b; *b = t;
}

int partition(int *arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            swap(&arr[++i], &arr[j]);
        }
    }
    swap(&arr[i + 1], &arr[high]);
    return i + 1;
}

void quicksort(int *arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quicksort(arr, low, pi - 1);
        quicksort(arr, pi + 1, high);
    }
}
```

### Hard Example: Dijkstra's Algorithm

```c
#include <stdio.h>
#include <limits.h>
#include <stdbool.h>

#define V 9

int min_distance(int dist[], bool spt_set[]) {
    int min = INT_MAX, min_idx;
    for (int v = 0; v < V; v++) {
        if (!spt_set[v] && dist[v] <= min) {
            min = dist[v];
            min_idx = v;
        }
    }
    return min_idx;
}

void dijkstra(int graph[V][V], int src) {
    int dist[V];
    bool spt_set[V];
    
    for (int i = 0; i < V; i++) {
        dist[i] = INT_MAX;
        spt_set[i] = false;
    }
    dist[src] = 0;
    
    for (int count = 0; count < V - 1; count++) {
        int u = min_distance(dist, spt_set);
        spt_set[u] = true;
        for (int v = 0; v < V; v++) {
            if (!spt_set[v] && graph[u][v] && 
                dist[u] != INT_MAX && 
                dist[u] + graph[u][v] < dist[v]) {
                dist[v] = dist[u] + graph[u][v];
            }
        }
    }
}
```

### Enterprise Example: Parallel Merge Sort

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define THRESHOLD 1000

void merge(int *arr, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;
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
    free(L); free(R);
}

void parallel_mergesort(int *arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        if (right - left > THRESHOLD) {
            parallel_mergesort(arr, left, mid);
            parallel_mergesort(arr, mid + 1, right);
        }
        merge(arr, left, mid, right);
    }
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Cache | Sequential access | Use iterative algorithms |
| Branch prediction | Conditional branches | Minimize branches in hot loops |
| Memory | Allocation overhead | Use stack allocation when possible |
| Parallelism | Multi-threading | Use thread pool for large datasets |
| SIMD | Vector operations | Use intrinsics for bulk operations |

## Best Practices

- Do:
  - Profile before optimizing
  - Choose the right algorithm for the data size
  - Use standard library functions when available
  - Test with edge cases (empty, single element, sorted)
  - Document time/space complexity
  
- Don't:
  - Prematurely optimize without profiling
  - Ignore worst-case complexity
  - Use recursion for deep structures (stack overflow)
  - Assume input is already sorted
  - Ignore integer overflow in calculations

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| QuickSort on sorted input | O(n²) worst case | Use median-of-three pivot |
| Integer overflow in binary search | Wrong midpoint | Use `low + (high - low) / 2` |
| Uninitialized variables | Undefined behavior | Initialize all variables |
| Stack overflow in recursion | Crash | Use iterative approach |
| Ignoring null pointers | Crash | Check before dereferencing |

## Interview Questions

### Q1: What is the difference between QuickSort and MergeSort?
**Answer:** QuickSort: in-place, O(log n) space, unstable. MergeSort: O(n) space, stable, guaranteed O(n log n).

### Q2: What is Big O notation?
**Answer:** Describes algorithm growth rate. O(1) constant, O(log n) logarithmic, O(n) linear, O(n log n) linearithmic, O(n²) quadratic.

### Q3: When is binary search better than linear search?
**Answer:** When data is sorted. Binary search: O(log n). Linear search: O(n).

### Q4: What is the difference between BFS and DFS?
**Answer:** BFS: uses queue, level-order, shortest path. DFS: uses stack, depth-first, less memory.

### Q5: What is dynamic programming?
**Answer:** Solving complex problems by breaking into overlapping subproblems, storing results to avoid recomputation.

### Q6: What is the time complexity of hash table lookup?
**Answer:** O(1) average, O(n) worst case. Depends on hash function quality and load factor.

### Q7: What is the difference between stable and unstable sort?
**Answer:** Stable: preserves relative order of equal elements. Unstable: may change order. Important for multi-key sorting.

### Q8: What is the difference between depth-first and breadth-first search?
**Answer:** DFS: go deep first (stack). BFS: go wide first (queue). DFS uses less memory; BFS finds shortest path.

### Q9: What is the difference between best, average, and worst case?
**Answer:** Best: minimum time. Average: expected time. Worst: maximum time. Always analyze worst case for guarantees.

### Q10: What is the difference between iterative and recursive algorithms?
**Answer:** Iterative: uses loops. Recursive: calls itself. Iterative uses less stack; recursive is often simpler.

### Q11: What is the difference between comparison and non-comparison sorts?
**Answer:** Comparison: compare elements (QuickSort, MergeSort). Non-comparison: use element values (Radix, Counting). Non-comparison can be O(n).

### Q12: What is the difference between in-place and out-of-place algorithms?
**Answer:** In-place: O(1) extra space (QuickSort). Out-of-place: O(n) extra space (MergeSort). In-place is memory efficient.

### Q13: What is the difference between divide-and-conquer and dynamic programming?
**Answer:** Divide-and-conquer: independent subproblems (MergeSort). Dynamic programming: overlapping subproblems (Fibonacci).

### Q14: What is the difference between greedy and dynamic programming?
**Answer:** Greedy: make locally optimal choice. DP: consider all possibilities. Greedy is faster but not always optimal.

### Q15: What is the difference between amortized and worst-case analysis?
**Answer:** Amortized: average over sequence of operations. Worst-case: single operation maximum. Dynamic arrays have O(1) amortized push.

## Cross-References

- **Previous Module:** [06 - Data Structures](../06-data-structures/)
- **Next Module:** [08 - Memory Management](../08-memory-management/)
- **Related:** [12 - Performance](../12-performance/) — Optimization techniques
- **Related:** [09 - Concurrency](../09-concurrency/) — Parallel algorithms
- **External:** [Introduction to Algorithms (CLRS)](https://mitpress.mit.edu/9780262046305/)
- **External:** [The Art of Computer Programming (Knuth)](https://www-cs-faculty.stanford.edu/~knuth/taocp.html)
