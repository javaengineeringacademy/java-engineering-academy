# Data Structures — C Language

## What it is
Data structures are organized ways to store and manage data efficiently.

## Why it exists
To optimize data access, insertion, deletion, and search operations.

## When to use it
When built-in arrays and structures are insufficient for your needs.

## How it works

### Linked List

```c
typedef struct Node {
    int data;
    struct Node *next;
} Node;

Node *create_node(int data) {
    Node *node = malloc(sizeof(Node));
    node->data = data;
    node->next = NULL;
    return node;
}
```

### Stack

```c
typedef struct {
    int *data;
    int top;
    int capacity;
} Stack;

void push(Stack *s, int value) {
    s->data[++s->top] = value;
}

int pop(Stack *s) {
    return s->data[s->top--];
}
```

### Queue

```c
typedef struct {
    int *data;
    int front, rear, size;
    int capacity;
} Queue;

void enqueue(Queue *q, int value) {
    q->data[q->rear++] = value;
    q->size++;
}
```

### Hash Table

```c
typedef struct Entry {
    char *key;
    int value;
    struct Entry *next;
} Entry;

typedef struct {
    Entry **buckets;
    int size;
} HashTable;
```

### Tree

```c
typedef struct TreeNode {
    int data;
    struct TreeNode *left;
    struct TreeNode *right;
} TreeNode;
```

### Graph (Adjacency List)

```c
typedef struct Edge {
    int target;
    int weight;
    struct Edge *next;
} Edge;

typedef struct {
    Edge **adj;
    int vertices;
} Graph;
```

## Production Incidents

### Incident 1: Stack Overflow in Linked List

**Problem:** A recursive linked list traversal crashes with stack overflow on lists longer than 10,000 nodes.

**Cause:** Recursive traversal allocates a stack frame for each node:

```c
void traverse(Node *head) {
    if (head == NULL) return;
    process(head->data);
    traverse(head->next);  // Recursive call per node
}
```

**Impact:** Stack overflow at ~10K nodes, process crashes with SIGSEGV. Customer data pipelines fail on large datasets.

**Detection:** Core dump shows stack overflow. `ulimit -s` shows 8MB default. Backtrace shows 10,000+ recursive frames.

**Solution:** Convert to iterative traversal:

```c
void traverse(Node *head) {
    Node *current = head;
    while (current != NULL) {
        process(current->data);
        current = current->next;
    }
}
```

**Prevention:** Prefer iterative solutions for unbounded recursion, set stack size limits, use `-Wstack-usage=N` compiler flag, profile stack depth in testing.

---

### Incident 2: Hash Table Collision Attack

**Problem:** A web application's login endpoint slows from 10ms to 5 seconds under attack.

**Cause:** Attacker sends requests with keys that all hash to the same bucket, degrading hash table to O(n) linked list traversal:

```c
Entry *hash_lookup(HashTable *ht, const char *key) {
    int idx = hash(key) % ht->size;
    Entry *e = ht->buckets[idx];
    while (e) {
        if (strcmp(e->key, key) == 0) return e;
        e = e->next;  // Traverses 10,000+ entries
    }
    return NULL;
}
```

**Impact:** DoS condition, all users experience slow response times, service degraded for 30 minutes until rate limiting kicks in.

**Detection:** Profiling shows 99% of time in `hash_lookup`. Bucket size monitoring reveals one bucket with 10,000+ entries.

**Solution:** Use balanced tree for collision chains and limit bucket chain length:

```c
Entry *hash_lookup(HashTable *ht, const char *key) {
    int idx = hash(key) % ht->size;
    Entry *e = ht->buckets[idx];
    while (e) {
        if (strcmp(e->key, key) == 0) return e;
        e = e->next;
    }
    return NULL;
}
// Switch to AVL tree or red-black tree for buckets
// Add bucket chain length limit
```

**Prevention:** Use randomized hash functions (SipHash), switch to tree-based collision resolution, monitor bucket chain lengths, implement rate limiting.

## Production Checklist

- [ ] Choose the right structure for the use case
- [ ] Handle memory allocation failures
- [ ] Free all allocated memory
- [ ] Handle edge cases (empty, single element)
- [ ] Consider cache efficiency

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Implements linked list and stack |
| Intermediate | Implements queue and hash table |
| Advanced | Implements tree and graph |

## Common Myths

1. **Myth**: Linked lists are always better than arrays
   **Truth**: Arrays have better cache locality; choose based on operations

2. **Myth**: Hash tables have no collisions
   **Truth**: Collisions are inevitable; handle with chaining or probing

## One-Minute Revision

| Structure | Access | Insert | Delete | Search |
|-----------|--------|--------|--------|--------|
| Array | O(1) | O(n) | O(n) | O(n) |
| Linked List | O(n) | O(1) | O(1) | O(n) |
| Stack | O(1) top | O(1) | O(1) | O(n) |
| Queue | O(1) | O(1) | O(1) | O(n) |
| Hash Table | O(1) avg | O(1) avg | O(1) avg | O(1) avg |
| BST | O(log n) | O(log n) | O(log n) | O(log n) |

## Related Topics

- [Structures](../02-structures/README.md)
- [Algorithms](../07-algorithms/README.md)
