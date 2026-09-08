# Data Structures — C Language

## Why It Matters

When you're building databases, compilers, operating systems, or any application managing non-trivial amounts of data, arrays alone won't cut it — they're fast but inflexible, with O(n) insertions and wasted or overflowing fixed sizes. C has no built-in collections, so you implement data structures from scratch using structs and pointers, gaining speed, size, and predictability that managed-language counterparts cannot match.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Dynamic data, fast lookup, sorted iteration, priority queues | Arrays for small, fixed collections |
| When NOT to use | Linked lists for most cases (poor cache locality) | Dynamic arrays (`realloc`) often faster |
| Alternatives | C++ STL, Rust collections, third-party libraries | More features, less control |
| Production Examples | Redis dict (hash table), Linux scheduler (rbtree), SQLite btree | Custom allocators for performance |
| Common Mistakes | Recursive tree traversal (stack overflow), no load factor monitoring | Use iterative traversal, monitor load factor |

## What It Is

Data structures are organized ways to store and manage data efficiently. Each structure optimizes different operations:

| Structure | Insert | Delete | Search | Ordered | Use Case |
|-----------|--------|--------|--------|---------|----------|
| Array | O(n) | O(n) | O(n) | Yes | Fixed collections |
| Linked List | O(1)* | O(1)* | O(n) | No | Dynamic collections |
| Stack | O(1) | O(1) | O(n) | No | LIFO operations |
| Queue | O(1) | O(1) | O(n) | No | FIFO operations |
| Hash Table | O(1) avg | O(1) avg | O(1) avg | No | Fast lookup |
| BST | O(log n) | O(log n) | O(log n) | Yes | Sorted data |
| Heap | O(log n) | O(log n) | O(n) | Partial | Priority queues |
| Graph | O(1)* | O(1)* | O(V+E) | No | Relationships |

*at known position

## Why It Exists

Every system software component is built on data structures:
- **Operating systems**: Process lists (linked lists), file systems (trees), memory allocators (free lists)
- **Databases**: B-trees for indexes, hash tables for caching, skip lists for sorted data
- **Compilers**: Symbol tables (hash tables), ASTs (trees), parse stacks
- **Networks**: Routing tables (hash tables), packet queues (circular buffers)

### Architecture: Choosing the Right Structure

```
Need fast lookup by key? → Hash Table
Need sorted iteration? → BST (AVL/Red-Black)
Need FIFO processing? → Queue (circular buffer)
Need LIFO processing? → Stack
Need priority ordering? → Heap
Need sparse connections? → Adjacency list (graph)
```

## Expanded Code Examples

### Linked List — Dynamic Collection

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct Node {
    int data;
    struct Node *next;
} Node;

// Create a new node
Node *node_create(int data) {
    Node *node = malloc(sizeof(Node));
    if (!node) return NULL;
    node->data = data;
    node->next = NULL;
    return node;
}

// Insert at head — O(1)
void list_push_head(Node **head, int data) {
    Node *node = node_create(data);
    if (!node) return;
    node->next = *head;
    *head = node;
}

// Insert at tail — O(n) without tail pointer
void list_push_tail(Node **head, int data) {
    Node *node = node_create(data);
    if (!node) return;
    if (*head == NULL) {
        *head = node;
        return;
    }
    Node *curr = *head;
    while (curr->next) curr = curr->next;
    curr->next = node;
}

// Delete first occurrence — O(n)
int list_delete(Node **head, int data) {
    Node *curr = *head, *prev = NULL;
    while (curr) {
        if (curr->data == data) {
            if (prev) prev->next = curr->next;
            else *head = curr->next;
            free(curr);
            return 0;
        }
        prev = curr;
        curr = curr->next;
    }
    return -1;  // Not found
}

// Reverse linked list — O(n)
void list_reverse(Node **head) {
    Node *prev = NULL, *curr = *head, *next;
    while (curr) {
        next = curr->next;
        curr->next = prev;
        prev = curr;
        curr = next;
    }
    *head = prev;
}

// Free entire list
void list_free(Node **head) {
    Node *curr = *head;
    while (curr) {
        Node *next = curr->next;
        free(curr);
        curr = next;
    }
    *head = NULL;
}
```

### Stack — LIFO Operations

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int *data;
    int top;
    int capacity;
} Stack;

Stack *stack_create(int capacity) {
    Stack *s = malloc(sizeof(Stack));
    if (!s) return NULL;
    s->data = malloc(capacity * sizeof(int));
    if (!s->data) { free(s); return NULL; }
    s->top = -1;
    s->capacity = capacity;
    return s;
}

int stack_push(Stack *s, int value) {
    if (s->top >= s->capacity - 1) return -1;  // Full
    s->data[++s->top] = value;
    return 0;
}

int stack_pop(Stack *s, int *value) {
    if (s->top < 0) return -1;  // Empty
    *value = s->data[s->top--];
    return 0;
}

int stack_peek(const Stack *s, int *value) {
    if (s->top < 0) return -1;
    *value = s->data[s->top];
    return 0;
}

void stack_free(Stack *s) {
    if (s) {
        free(s->data);
        free(s);
    }
}

// Application: balanced parentheses checker
int is_balanced(const char *expr) {
    Stack *s = stack_create(256);
    int balanced = 1;

    for (int i = 0; expr[i] && balanced; i++) {
        char c = expr[i];
        if (c == '(' || c == '[' || c == '{') {
            stack_push(s, c);
        } else if (c == ')' || c == ']' || c == '}') {
            int top;
            if (stack_pop(s, &top) != 0) {
                balanced = 0;
            } else if ((c == ')' && top != '(') ||
                       (c == ']' && top != '[') ||
                       (c == '}' && top != '{')) {
                balanced = 0;
            }
        }
    }

    int empty = (s->top < 0);
    stack_free(s);
    return balanced && empty;
}
```

### Queue — FIFO with Circular Buffer

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int *data;
    int front;
    int rear;
    int size;
    int capacity;
} Queue;

Queue *queue_create(int capacity) {
    Queue *q = malloc(sizeof(Queue));
    if (!q) return NULL;
    q->data = malloc(capacity * sizeof(int));
    if (!q->data) { free(q); return NULL; }
    q->front = 0;
    q->rear = -1;
    q->size = 0;
    q->capacity = capacity;
    return q;
}

int queue_enqueue(Queue *q, int value) {
    if (q->size >= q->capacity) return -1;  // Full
    q->rear = (q->rear + 1) % q->capacity;
    q->data[q->rear] = value;
    q->size++;
    return 0;
}

int queue_dequeue(Queue *q, int *value) {
    if (q->size <= 0) return -1;  // Empty
    *value = q->data[q->front];
    q->front = (q->front + 1) % q->capacity;
    q->size--;
    return 0;
}

int queue_peek(const Queue *q, int *value) {
    if (q->size <= 0) return -1;
    *value = q->data[q->front];
    return 0;
}

void queue_free(Queue *q) {
    if (q) {
        free(q->data);
        free(q);
    }
}
```

### Hash Table — Fast Key-Value Lookup

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define HT_INITIAL_CAPACITY 16

typedef struct Entry {
    char *key;
    int value;
    struct Entry *next;  // Chaining for collisions
} Entry;

typedef struct {
    Entry **buckets;
    int size;
    int count;
} HashTable;

// Simple hash function (djb2)
unsigned int hash(const char *key) {
    unsigned int hash = 5381;
    int c;
    while ((c = *key++)) {
        hash = ((hash << 5) + hash) + c;
    }
    return hash;
}

HashTable *ht_create(int size) {
    HashTable *ht = malloc(sizeof(HashTable));
    if (!ht) return NULL;
    ht->buckets = calloc(size, sizeof(Entry *));
    if (!ht->buckets) { free(ht); return NULL; }
    ht->size = size;
    ht->count = 0;
    return ht;
}

int ht_set(HashTable *ht, const char *key, int value) {
    unsigned int idx = hash(key) % ht->size;

    // Check if key exists
    Entry *e = ht->buckets[idx];
    while (e) {
        if (strcmp(e->key, key) == 0) {
            e->value = value;
            return 0;
        }
        e = e->next;
    }

    // New entry
    e = malloc(sizeof(Entry));
    if (!e) return -1;
    e->key = strdup(key);
    e->value = value;
    e->next = ht->buckets[idx];
    ht->buckets[idx] = e;
    ht->count++;
    return 0;
}

int ht_get(HashTable *ht, const char *key, int *value) {
    unsigned int idx = hash(key) % ht->size;
    Entry *e = ht->buckets[idx];
    while (e) {
        if (strcmp(e->key, key) == 0) {
            *value = e->value;
            return 0;
        }
        e = e->next;
    }
    return -1;  // Not found
}

void ht_free(HashTable *ht) {
    for (int i = 0; i < ht->size; i++) {
        Entry *e = ht->buckets[i];
        while (e) {
            Entry *next = e->next;
            free(e->key);
            free(e);
            e = next;
        }
    }
    free(ht->buckets);
    free(ht);
}
```

### Binary Search Tree

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct TreeNode {
    int data;
    struct TreeNode *left;
    struct TreeNode *right;
} TreeNode;

TreeNode *tree_insert(TreeNode *root, int data) {
    if (root == NULL) {
        TreeNode *node = malloc(sizeof(TreeNode));
        if (!node) return NULL;
        node->data = data;
        node->left = node->right = NULL;
        return node;
    }
    if (data < root->data)
        root->left = tree_insert(root->left, data);
    else if (data > root->data)
        root->right = tree_insert(root->right, data);
    return root;
}

TreeNode *tree_search(TreeNode *root, int data) {
    if (root == NULL || root->data == data) return root;
    if (data < root->data) return tree_search(root->left, data);
    return tree_search(root->right, data);
}

void tree_inorder(TreeNode *root) {
    if (root == NULL) return;
    tree_inorder(root->left);
    printf("%d ", root->data);
    tree_inorder(root->right);
}

void tree_free(TreeNode *root) {
    if (root == NULL) return;
    tree_free(root->left);
    tree_free(root->right);
    free(root);
}
```

## Production Incidents

### Incident 1: Stack Overflow in Linked List Traversal

**Problem**: Recursive linked list traversal crashes on lists longer than 10,000 nodes.

**Cause**: Each recursive call adds a stack frame:

```c
void traverse(Node *head) {
    if (head == NULL) return;
    process(head->data);
    traverse(head->next);  // Stack frame per node
}
```

**Solution**: Convert to iterative traversal:

```c
void traverse(Node *head) {
    Node *current = head;
    while (current != NULL) {
        process(current->data);
        current = current->next;
    }
}
```

### Incident 2: Hash Table Collision Attack

**Problem**: Web application slows from 10ms to 5 seconds under attack.

**Cause**: Attacker sends keys that all hash to the same bucket, degrading lookup to O(n):

```c
// Attacker crafts 10,000 keys that all hash to bucket 0
// hash_lookup now traverses a 10,000-element linked list per lookup
```

**Solution**: Use randomized hash functions (SipHash), limit bucket chain length, or switch to tree-based collision resolution.

### Incident 3: Binary Tree Memory Leak on Deletion

**Problem**: A binary search tree leaks memory when nodes are deleted because child pointers are not properly freed.

```c
Node *bst_delete(Node *root, int key) {
    if (root == NULL) return NULL;
    if (key < root->key) {
        root->left = bst_delete(root->left, key);
    } else if (key > root->key) {
        root->right = bst_delete(root->right, key);
    } else {
        if (root->left == NULL) return root->right;
        if (root->right == NULL) return root->left;
        // Two children: replace with in-order successor
        Node *successor = root->right;
        while (successor->left) successor = successor->left;
        root->key = successor->key;
        root->right = bst_delete(root->right, successor->key);
        // Memory leak: successor node is not freed!
    }
    return root;
}
```

**Cause**: The in-order successor node is replaced but never freed.

**Impact**: Memory leak grows with each deletion. Long-running service eventually runs out of memory.

**Solution**: Free the successor node after copying its data:

```c
Node *bst_delete(Node *root, int key) {
    if (root == NULL) return NULL;
    if (key < root->key) {
        root->left = bst_delete(root->left, key);
    } else if (key > root->key) {
        root->right = bst_delete(root->right, key);
    } else {
        if (root->left == NULL) {
            Node *temp = root->right;
            free(root);
            return temp;
        }
        if (root->right == NULL) {
            Node *temp = root->left;
            free(root);
            return temp;
        }
        Node *successor = root->right;
        while (successor->left) successor = successor->left;
        root->key = successor->key;
        root->right = bst_delete(root->right, successor->key);
    }
    return root;
}
```

**Prevention**: Always free nodes during deletion; use AddressSanitizer to detect leaks; test with Valgrind.

---

### Incident 4: Circular Buffer Wraparound Bug

**Problem**: A circular buffer (ring buffer) loses data when the head and tail pointers wrap around incorrectly.

```c
typedef struct {
    int *data;
    size_t size;
    size_t head;
    size_t tail;
    size_t count;
} RingBuffer;

void ring_push(RingBuffer *rb, int value) {
    if (rb->count == rb->size) return;  // Full
    rb->data[rb->tail] = value;
    rb->tail = (rb->tail + 1) % rb->size;  // Wraparound
    rb->count++;
}

int ring_pop(RingBuffer *rb) {
    if (rb->count == 0) return -1;  // Empty
    int value = rb->data[rb->head];
    rb->head = (rb->head + 1) % rb->size;  // Wraparound
    rb->count--;
    return value;
}
```

**Cause**: If the modulo operation is not applied consistently, head/tail can go out of bounds.

**Impact**: Data corruption, out-of-bounds access, crash.

**Solution**: Always use modulo for wraparound; add bounds checking:

```c
void ring_push(RingBuffer *rb, int value) {
    if (rb == NULL || rb->data == NULL) return;
    if (rb->count >= rb->size) return;  // Full
    rb->data[rb->tail % rb->size] = value;
    rb->tail = (rb->tail + 1) % rb->size;
    rb->count++;
}
```

**Prevention**: Always apply modulo for circular indexing; add NULL checks; use AddressSanitizer.

---

### Incident 5: Graph Traversal Memory Leak

**Problem**: A breadth-first search (BFS) implementation leaks memory for the queue and visited array on large graphs.

```c
void bfs(Graph *g, int start) {
    Queue *q = queue_create(g->num_vertices);
    bool *visited = calloc(g->num_vertices, sizeof(bool));
    
    queue_push(q, start);
    visited[start] = true;
    
    while (!queue_empty(q)) {
        int v = queue_pop(q);
        process(v);
        for (int i = 0; i < g->adj_count[v]; i++) {
            int neighbor = g->adj[v][i];
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue_push(q, neighbor);
            }
        }
    }
    // Memory leak: q and visited not freed
}
```

**Cause**: The queue and visited array are allocated but never freed.

**Impact**: Memory leak proportional to graph size. Repeated BFS calls exhaust memory.

**Solution**: Free all allocated resources:

```c
void bfs(Graph *g, int start) {
    Queue *q = queue_create(g->num_vertices);
    bool *visited = calloc(g->num_vertices, sizeof(bool));
    if (!q || !visited) {
        queue_destroy(q);
        free(visited);
        return;
    }
    
    queue_push(q, start);
    visited[start] = true;
    
    while (!queue_empty(q)) {
        int v = queue_pop(q);
        process(v);
        for (int i = 0; i < g->adj_count[v]; i++) {
            int neighbor = g->adj[v][i];
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue_push(q, neighbor);
            }
        }
    }
    
    queue_destroy(q);
    free(visited);
}
```

**Prevention**: Always free all allocated resources; use goto cleanup pattern; run Valgrind/AddressSanitizer in CI.

## Production Checklist

- [ ] Choose the right structure for the use case
- [ ] Handle memory allocation failures
- [ ] Free all allocated memory (including linked list nodes, hash entries)
- [ ] Handle edge cases (empty, single element, full)
- [ ] Consider cache efficiency (arrays > linked lists for traversal)
- [ ] Use load factor monitoring for hash tables
- [ ] Implement iterator patterns for clean traversal

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Implements linked list and stack | Understands nodes, pointers, push/pop |
| **Intermediate** | Implements queue and hash table | Handles collisions, circular buffers |
| **Advanced** | Implements tree and graph | BST, AVL, adjacency list, BFS/DFS |
| **Expert** | Optimizes for cache, implements custom allocators | Pool allocators, lock-free structures |

## Common Myths Debunked

1. **Myth**: Linked lists are always better than arrays
   **Truth**: Arrays have better cache locality. For most use cases, dynamic arrays (`realloc`) outperform linked lists.

2. **Myth**: Hash tables have no collisions
   **Truth**: Collisions are inevitable. Good hash functions and collision resolution strategies (chaining, open addressing) minimize impact.

3. **Myth**: You need to implement data structures from scratch in production
   **Truth**: For critical systems, custom implementations offer control. For most applications, use well-tested libraries.

## One-Minute Revision

| Structure | Access | Insert | Delete | Search | Cache |
|-----------|--------|--------|--------|--------|-------|
| Array | O(1) | O(n) | O(n) | O(n) | Excellent |
| Linked List | O(n) | O(1)* | O(1)* | O(n) | Poor |
| Stack | O(1) top | O(1) | O(1) | O(n) | Good |
| Queue | O(1) | O(1) | O(1) | O(n) | Good |
| Hash Table | O(1) avg | O(1) avg | O(1) avg | O(1) avg | Fair |
| BST | O(log n) | O(log n) | O(log n) | O(log n) | Fair |

## Related Topics

- [Structures](../02-structures/README.md) — Structs that build data structures
- [Algorithms](../07-algorithms/README.md) — Algorithms that operate on data structures
- [Memory Management](../08-memory-management/README.md) — Custom allocators for data structures

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Linked list cycle (infinite loop) | Floyd's tortoise and hare | Use slow/fast pointer detection; print pointers during traversal to identify the cycle node |
| Hash table performance degradation | Load factor monitoring + profiling | Track `count/size` ratio; rehash when > 0.75; use `perf` to profile bucket chain traversal |
| Stack overflow from deep recursion (tree traversal) | Convert to iterative + explicit stack | Replace recursive traversal with while-loop and explicit stack; monitor stack usage with `-fstack-usage` |
| Use-after-free in linked list node deletion | AddressSanitizer | Compile with `-fsanitize=address`; detects use-after-free immediately with stack trace |
| Incorrect BST height after rotation (AVL tree) | In-order traversal verification | After every rotation, verify in-order traversal produces sorted output; check `_Static_assert` for node sizes |

## Code Review Checklist

- [ ] All `malloc`/`calloc` return values checked before use
- [ ] All nodes/entries freed in cleanup functions (no orphaned memory)
- [ ] Edge cases handled: empty structure, single element, full structure
- [ ] Hash table load factor monitored; rehash triggered before degradation
- [ ] Iterator patterns used for traversal (avoids exposing internal structure)
- [ ] Recursive tree functions have explicit depth limit or are converted to iterative
- [ ] Custom allocator used for high-frequency allocation patterns (pool, arena)

## Architecture Considerations

Data structures are the backbone of systems software: operating systems use linked lists for process queues, databases use B-trees for indexes, and networks use hash tables for routing. The choice of structure depends on access patterns — arrays for sequential access, hash tables for key lookup, trees for ordered iteration. Cache efficiency often matters more than algorithmic complexity for small-to-medium datasets.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Dynamic array (`realloc`) | General-purpose growable collection | Excellent cache locality but O(n) insertion at arbitrary positions |
| Hash table with chaining | Fast key-value lookup | O(1) average but poor cache locality; vulnerable to hash DoS attacks |
| Red-black tree | Sorted data with frequent insert/delete | O(log n) for all operations but complex implementation |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Hash collision DoS attacks | O(n) degradation, denial of service | Use randomized hash functions (SipHash); limit bucket chain length |
| Recursive tree traversal stack overflow | Crash, denial of service | Convert to iterative traversal with explicit stack; set recursion depth limits |
| Unbounded dynamic array growth | Memory exhaustion, OOM crash | Set maximum capacity; validate allocation size before `realloc` |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added flexible array members, `bool` type | Use flexible arrays for variable-length data structures |
| C99 → C11 | Added `<stdatomic.h>` for lock-free structures, `_Static_assert` | Use atomics for concurrent data structures; add compile-time size checks |
| C11 → C23 | Added `typeof`, improved `_Generic` | Use `typeof` for type-generic data structure operations; adopt `constexpr` for compile-time constants |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| Flexible array members (`int data[]`) | C99 | Standard — preferred for variable-length structures |
| `_Static_assert` for structure sizes | C11 | Standard — use to validate data structure layout |
| `<stdatomic.h>` for lock-free operations | C11 | Standard — use for concurrent data structures |
| `typeof` for type-generic macros | C23 (standardized) | Use for type-safe data structure operations |

## Interview Questions

1. **When would you choose a dynamic array over a linked list?**: Dynamic arrays (`realloc`) are preferred for most use cases due to better cache locality (sequential memory access). Linked lists are only preferable when you need O(1) insertion/deletion at arbitrary positions AND have a pointer to the position.
2. **How do you prevent hash table collision DoS attacks?**: Use randomized hash functions (SipHash), limit bucket chain length, switch to tree-based collision resolution (like Java 8+), or use universal hashing. Monitor load factor and rehash proactively.
3. **What is the trade-off between separate chaining and open addressing?**: Separate chaining uses linked lists at each bucket — simple but extra allocation overhead. Open addressing stores entries in the array itself — better cache locality but more complex deletion and higher load factor sensitivity.
4. **How do you implement a thread-safe data structure in C?**: Use `<stdatomic.h>` for lock-free operations on simple structures (counters, stacks). For complex structures, use mutexes with fine-grained locking (per-bucket, per-node). Design for minimal critical section length.
5. **When should you use a memory pool for a data structure?**: Use a memory pool when you allocate many small, same-sized objects (linked list nodes, hash table entries). Pools reduce `malloc` overhead, improve cache locality, and simplify cleanup (free everything at once).

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Introduction to Algorithms (CLRS)](https://mitpress.mit.edu/9780262046305/introduction-to-algorithms/)
- [The Art of Computer Programming (Knuth)](https://www-cs-faculty.stanford.edu/~knuth/taocp.html)

## Overview

The Data Structures module covers implementing fundamental data structures in C: linked lists, stacks, queues, hash tables, and binary search trees. C has no built-in collections, so you implement these from scratch using structs and pointers, gaining speed, size, and predictability.

## Learning Objectives

- Implement linked lists (singly and doubly)
- Build stack and queue data structures
- Create hash tables with collision handling
- Implement binary search trees
- Choose the right data structure for the problem

## Prerequisites

- Completion of Module 05 (Pointers Advanced)
- Understanding of structures and pointers
- Basic algorithm concepts

## History

- **1972** — Arrays and pointers in original C
- **1978** — K&R C documented linked list patterns
- **1989** — ANSI C standardized struct syntax
- **1999** — C99 added flexible array members
- **2011** — C11 added `_Alignas` for data alignment
- **2023** — C23 added improved type inference

## Production Notes

- **Where is it used?** Databases, operating systems, compilers, interpreters
- **Why is it useful?** Efficient data management, fast lookup, sorted iteration
- **When should it be avoided?** Arrays for small, fixed collections
- **Alternative?** C++ STL, Rust collections, third-party libraries

## Core Concepts

### Data Structure Comparison

| Structure | Insert | Delete | Search | Ordered | Use Case |
|-----------|--------|--------|--------|---------|----------|
| Array | O(n) | O(n) | O(n) | Yes | Fixed collections |
| Linked List | O(1)* | O(1)* | O(n) | No | Dynamic collections |
| Stack | O(1) | O(1) | O(n) | No | LIFO operations |
| Queue | O(1) | O(1) | O(n) | No | FIFO operations |
| Hash Table | O(1) avg | O(1) avg | O(1) avg | No | Fast lookup |
| BST | O(log n) | O(log n) | O(log n) | Yes | Sorted data |

### When to Use Each

| Problem | Best Structure | Why |
|---------|----------------|-----|
| Fast lookup by key | Hash Table | O(1) average |
| Sorted iteration | BST | In-order traversal |
| LIFO operations | Stack | Push/pop O(1) |
| FIFO operations | Queue | Enqueue/dequeue O(1) |
| Dynamic sizing | Linked List | O(1) insert/delete |

## Internal Working

### Memory Layout Comparison

```
Array: [1][2][3][4][5]  — Contiguous
Linked List: 1→2→3→4→5  — Scattered
Stack: Top→[5][4][3][2][1]  — LIFO
Queue: [1][2][3][4][5]→Tail  — FIFO
Hash Table: [bucket0]→[bucket1]→[bucket2]  — Chaining
BST:      3
         / \
        1   5
       / \
      0   2
```

### Hash Table Collision Resolution

| Method | Description | Trade-off |
|--------|-------------|-----------|
| Chaining | Linked list at each bucket | Simple, memory overhead |
| Open Addressing | Probe for next empty slot | Cache-friendly, clustering |
| Robin Hood | Rehash to minimize variance | Balanced, complex |

## Syntax

```c
// Linked list node
struct Node {
    int data;
    struct Node *next;
};

// Stack
typedef struct {
    int *data;
    int top;
    int capacity;
} Stack;

// Queue (circular)
typedef struct {
    int *data;
    int front;
    int rear;
    int capacity;
} Queue;

// Hash table entry
typedef struct Entry {
    char *key;
    int value;
    struct Entry *next;
} Entry;

typedef struct {
    Entry **buckets;
    int size;
} HashTable;

// BST node
typedef struct TreeNode {
    int data;
    struct TreeNode *left;
    struct TreeNode *right;
} TreeNode;
```

## Examples

### Easy Example: Linked List

```c
#include <stdio.h>
#include <stdlib.h>

struct Node {
    int data;
    struct Node *next;
};

void push(struct Node **head, int data) {
    struct Node *new = malloc(sizeof(struct Node));
    new->data = data;
    new->next = *head;
    *head = new;
}

void print(struct Node *head) {
    while (head) {
        printf("%d ", head->data);
        head = head->next;
    }
}

int main(void) {
    struct Node *list = NULL;
    push(&list, 3);
    push(&list, 2);
    push(&list, 1);
    print(list);  // 1 2 3
    return 0;
}
```

### Medium Example: Stack

```c
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int *data;
    int top;
    int capacity;
} Stack;

Stack *stack_new(int cap) {
    Stack *s = malloc(sizeof(Stack));
    s->data = malloc(cap * sizeof(int));
    s->top = -1;
    s->capacity = cap;
    return s;
}

void push(Stack *s, int val) {
    if (s->top < s->capacity - 1) {
        s->data[++s->top] = val;
    }
}

int pop(Stack *s) {
    if (s->top >= 0) {
        return s->data[s->top--];
    }
    return -1;
}
```

### Hard Example: Hash Table

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TABLE_SIZE 100

typedef struct Entry {
    char *key;
    int value;
    struct Entry *next;
} Entry;

typedef struct {
    Entry *buckets[TABLE_SIZE];
} HashTable;

unsigned int hash(const char *key) {
    unsigned int h = 0;
    while (*key) {
        h = h * 31 + *key++;
    }
    return h % TABLE_SIZE;
}

void insert(HashTable *ht, const char *key, int value) {
    unsigned int idx = hash(key);
    Entry *e = malloc(sizeof(Entry));
    e->key = strdup(key);
    e->value = value;
    e->next = ht->buckets[idx];
    ht->buckets[idx] = e;
}

int search(HashTable *ht, const char *key) {
    unsigned int idx = hash(key);
    Entry *e = ht->buckets[idx];
    while (e) {
        if (strcmp(e->key, key) == 0) return e->value;
        e = e->next;
    }
    return -1;
}
```

### Enterprise Example: Thread-Safe Queue

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct {
    int *data;
    int front, rear, size, capacity;
    pthread_mutex_t mutex;
    pthread_cond_t not_empty;
    pthread_cond_t not_full;
} ThreadSafeQueue;

void enqueue(ThreadSafeQueue *q, int val) {
    pthread_mutex_lock(&q->mutex);
    while (q->size == q->capacity) {
        pthread_cond_wait(&q->not_full, &q->mutex);
    }
    q->data[q->rear] = val;
    q->rear = (q->rear + 1) % q->capacity;
    q->size++;
    pthread_cond_signal(&q->not_empty);
    pthread_mutex_unlock(&q->mutex);
}

int dequeue(ThreadSafeQueue *q) {
    pthread_mutex_lock(&q->mutex);
    while (q->size == 0) {
        pthread_cond_wait(&q->not_empty, &q->mutex);
    }
    int val = q->data[q->front];
    q->front = (q->front + 1) % q->capacity;
    q->size--;
    pthread_cond_signal(&q->not_full);
    pthread_mutex_unlock(&q->mutex);
    return val;
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Cache locality | Arrays > linked lists | Use arrays when possible |
| Memory overhead | Pointers add overhead | Use compact representations |
| Hash function | Distribution quality | Use FNV-1a or MurmurHash |
| Tree balancing | BST can degrade | Use AVL or Red-Black trees |
| Allocation | malloc/free overhead | Use memory pools |

## Best Practices

- Do:
  - Choose the right data structure for the problem
  - Free all allocated memory
  - Check for NULL after malloc
  - Use const for read-only parameters
  - Document interface contracts
  
- Don't:
  - Use linked lists for random access
  - Ignore hash table load factor
  - Recurse on deep trees (stack overflow)
  - Assume O(1) for all operations
  - Mix data structure interfaces

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Memory leak in linked list | Resource exhaustion | Free all nodes on destruction |
| Hash table collision attack | O(n) degradation | Use cryptographic hash, limit size |
| Stack overflow in recursion | Crash | Use iterative traversal |
| Off-by-one in queue | Buffer overflow | Use circular buffer |
| Uninitialized pointers | Undefined behavior | Initialize all pointers |

## Interview Questions

### Q1: What is the difference between array and linked list?
**Answer:** Array: contiguous memory, O(1) access, O(n) insert. Linked list: scattered memory, O(n) access, O(1) insert.

### Q2: What is a hash table collision?
**Answer:** Two keys hash to the same bucket. Resolved by chaining (linked list) or open addressing (probing).

### Q3: What is the difference between stack and queue?
**Answer:** Stack: LIFO (last in, first out). Queue: FIFO (first in, first out).

### Q4: What is a binary search tree?
**Answer:** A tree where left child < parent < right child. Enables O(log n) search, insert, delete.

### Q5: What is the time complexity of hash table lookup?
**Answer:** O(1) average, O(n) worst case (all keys collide). Good hash function minimizes collisions.

### Q6: What is the difference between singly and doubly linked list?
**Answer:** Singly: next pointer only. Doubly: next and prev pointers. Doubly allows O(1) deletion but uses more memory.

### Q7: What is a circular buffer?
**Answer:** A fixed-size array with head and tail pointers that wrap around. Used for queues and streams.

### Q8: What is the load factor of a hash table?
**Answer:** Number of entries / number of buckets. When > 0.75, resize to maintain O(1) performance.

### Q9: What is the difference between BST and heap?
**Answer:** BST: left < root < right. Heap: parent >= children (max-heap). BST enables search; heap enables priority queue.

### Q10: What is a self-balancing tree?
**Answer:** A tree that automatically adjusts to maintain O(log n) height. Examples: AVL, Red-Black, B-tree.

### Q11: What is the difference between depth-first and breadth-first search?
**Answer:** DFS: go deep first (stack). BFS: go wide first (queue). DFS uses less memory; BFS finds shortest path.

### Q12: What is the purpose of a sentinel node?
**Answer:** A dummy node that simplifies edge cases (empty list, head deletion). Reduces conditional logic.

### Q13: What is the difference between open addressing and chaining?
**Answer:** Open addressing: store in array, probe for empty slot. Chaining: linked list at each bucket. Chaining is simpler.

### Q14: What is a skip list?
**Answer:** A linked list with multiple layers for O(log n) search. Alternative to balanced trees.

### Q15: What is the difference between static and dynamic data structures?
**Answer:** Static: fixed size (array). Dynamic: resizable (linked list, dynamic array). Dynamic uses more memory.

## Cross-References

- **Previous Module:** [05 - Pointers Advanced](../05-pointers-advanced/)
- **Next Module:** [07 - Algorithms](../07-algorithms/)
- **Related:** [02 - Structures](../02-structures/) — Building blocks
- **Related:** [08 - Memory Management](../08-memory-management/) — Custom allocators
- **External:** [Introduction to Algorithms (CLRS)](https://mitpress.mit.edu/9780262046305/)
- **External:** [The Art of Computer Programming (Knuth)](https://www-cs-faculty.stanford.edu/~knuth/taocp.html)
