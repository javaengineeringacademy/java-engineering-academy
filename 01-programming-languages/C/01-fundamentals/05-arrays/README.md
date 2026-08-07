# Arrays — C Language

## What it is
Arrays are collections of elements of the same type stored in contiguous memory.

## Why it exists
To store and access multiple values efficiently using indices.

## When to use it
When you need to store multiple values of the same type.

## How it works

### Declaration and Initialization

```c
int numbers[5];                    // Uninitialized
int values[5] = {1, 2, 3, 4, 5}; // Initialized
int zeros[5] = {0};               // All zeros
int auto_size[] = {1, 2, 3};      // Size inferred (3)
```

### Accessing Elements

```c
int first = values[0];   // First element
values[2] = 10;         // Modify third element
```

### Array Traversal

```c
for (int i = 0; i < 5; i++) {
    printf("%d\n", numbers[i]);
}
```

### Multi-dimensional Arrays

```c
int matrix[3][3] = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};
```

### Array as Function Parameter

```c
void print_array(int arr[], int size) {
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
}
```

### Array Decay to Pointer

```c
int arr[5] = {1, 2, 3, 4, 5};
int *ptr = arr;  // Array decays to pointer
```

## Production Checklist

- [ ] Always check array bounds
- [ ] Initialize arrays before use
- [ ] Pass array size to functions
- [ ] Use sizeof for array size (when possible)
- [ ] Avoid variable-length arrays in production

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses single-dimensional arrays |
| Intermediate | Works with multi-dimensional arrays |
| Advanced | Understands array-pointer relationship |

## Common Myths

1. **Myth**: Arrays know their own size
   **Truth**: Arrays decay to pointers, losing size information

2. **Myth**: Array assignment works
   **Truth**: You cannot assign arrays directly; use memcpy

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Declaration | type name[size] |
| Initialization | {val1, val2, ...} |
| Access | name[index] |
| Multi-dimensional | type name[rows][cols] |
| Decay | Arrays become pointers |
| Size | sizeof(arr) / sizeof(arr[0]) |

## Related Topics

- [Strings](../06-strings/README.md)
- [Pointers](../07-pointers/README.md)
