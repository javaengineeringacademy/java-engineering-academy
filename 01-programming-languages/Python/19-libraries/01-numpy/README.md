# NumPy

## Why NumPy Exists

Every Python developer who works with numbers eventually hits a wall: Python lists are too slow for scientific computing. When you need to multiply two arrays of 10,000 elements, a Python loop takes seconds while NumPy does it in microseconds. NumPy was created to solve this fundamental problem by providing C-optimized array operations that run 10-100x faster than equivalent Python loops.

## What You'll Learn

By the end of this section, you'll be able to:

- Create and manipulate multidimensional arrays with vectorized operations
- Use broadcasting to perform operations on arrays of different shapes without explicit loops
- Apply linear algebra operations using BLAS/LAPACK-backed routines

## When to Use NumPy

| Use Case | Why NumPy | Alternative |
|----------|-----------|-------------|
| Matrix multiplication | BLAS-optimized, O(n³) with cache efficiency | Python loops |
| Element-wise operations | Vectorized, no Python loop overhead | List comprehensions |
| Random sampling | Reproducible, efficient distributions | `random` module |
| Filtering data | Boolean indexing for conditional selection | List comprehensions |
| Linear algebra | LAPACK-backed solvers | Manual implementations |
| Fourier transforms | Optimized FFT implementations | `cmath` module |

## How NumPy Works Internally

NumPy stores data in contiguous C arrays rather than Python objects. When you create `np.array([1, 2, 3])`, NumPy allocates a single block of memory and stores the values directly, unlike Python lists which store pointers to Python integer objects. This eliminates per-element overhead and enables CPU cache-friendly computation.

Broadcasting is NumPy's most powerful feature. When you add a `(n, 1)` array to a `(1, m)` array, NumPy doesn't create the intermediate `(n, m)` array. Instead, it computes strides on-the-fly to simulate the expanded shape. This means operations like `matrix + vector` work without copying data or writing explicit loops.

```python
import numpy as np

# Array creation
arr = np.array([1, 2, 3, 4, 5])
matrix = np.array([[1, 2, 3], [4, 5, 6]])

# Broadcasting
vector = np.array([10, 20, 30])
result = matrix + vector  # [[11, 22, 33], [14, 25, 36]]

# View vs Copy
view = arr[1:4]       # Shares memory
copy = arr[1:4].copy() # Independent data
```

## Production Checklist

### ✅ Before using NumPy in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: NumPy is just faster Python lists
**Reality:** NumPy isn't just faster lists — it's a fundamentally different abstraction. Arrays are homogeneous, contiguous memory blocks with fixed types. Lists are heterogeneous, pointer-based collections. The performance difference comes from eliminating per-element Python overhead.

### ❌ Myth 2: Broadcasting is just syntactic sugar for loops
**Reality:** Broadcasting creates actual memory-efficient operations. It doesn't copy data — it computes strides on-the-fly to simulate expanded shapes. A `(n, 1) + (1, m)` operation produces `(n, m)` without allocating the intermediate array.

### ❌ Myth 3: NumPy arrays are always faster than lists
**Reality:** For very small arrays (1-10 elements), Python lists can be faster due to NumPy's function call overhead. For string-heavy or object-typed operations, lists often win. NumPy shines for numeric operations on arrays of 100+ elements.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Fast, memory-efficient multidimensional arrays |
| Complexity | O(n) for most operations |
| Thread Safe | No (GIL-limited) |
| Best Alternative | Python lists for small data |
| When to Use | Numeric computation, scientific computing |
| When to Avoid | String-heavy data, very small arrays |

## Related Topics

- [02-pandas](../02-pandas/) - Built on NumPy for tabular data
- [14-matplotlib](../14-matplotlib/) - Visualization built on NumPy
- [15-pydantic](../15-pydantic/) - Typed NumPy arrays with NDArray

## References
- NumPy Documentation: https://numpy.org/doc/
- NumPy Tutorial: https://numpy.org/doc/stable/user/quickstart.html
- NumPy Source: https://github.com/numpy/numpy
- PEP 3118: Rebuffering Protocol
- NumPy: A guide to NumPy (T. Oliphant)

## Version Validation
- Verified against: NumPy 1.26+, Python 3.10+
