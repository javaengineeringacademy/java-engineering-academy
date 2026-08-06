# Slices and Maps in Go

Slices are dynamic arrays; maps are key-value stores. Both are reference types.

## Slice Operations

```go
// Create
s := []int{1, 2, 3}
s := make([]int, 5)      // len=5, cap=5
s := make([]int, 0, 10)  // len=0, cap=10

// Operations
s = append(s, 4, 5)
copy(dst, src)
len(s)
cap(s)
s[1:3]  // sub-slice
```

## Map Operations

```go
// Create
m := map[string]int{"a": 1}
m := make(map[string]int)

// Operations
m["key"] = value
delete(m, "key")
v, ok := m["key"]  // Check existence
len(m)
```

## Key Points
- Slices are reference types (backed by array)
- Append may reallocate
- Maps are unordered
- Maps are not safe for concurrent use (use sync.Map)
- Nil slice vs empty slice
- Zero value of map is nil
