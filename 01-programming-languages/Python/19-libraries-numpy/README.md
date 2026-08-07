# NumPy — The Foundation of Numerical Python

> **If Python had a scientific computing superpower, NumPy would be it.**

## What

NumPy provides the `ndarray` — a fast, memory-efficient multidimensional array object that serves as the foundation for nearly all scientific computing in Python. It includes vectorized operations, broadcasting, linear algebra routines, and random number generation.

NumPy replaces slow Python loops with C-optimized array operations, delivering 10-100x performance gains on numerical workloads.

## Why

- **Performance:** Vectorized operations on arrays are orders of magnitude faster than Python loops.
- **Memory:** Contiguous C arrays use far less memory than Python lists of lists.
- **Broadcasting:** Operations between arrays of different shapes without explicit loops.
- **Ecosystem:** Pandas, SciPy, scikit-learn, TensorFlow, PyTorch — all built on NumPy.
- **Indexing:** Powerful slicing and boolean indexing for selecting and manipulating data.

## When

| Scenario | NumPy Approach | Why |
|----------|---------------|-----|
| Matrix multiplication | `np.dot()` or `@` | BLAS-optimized, O(n³) with cache efficiency |
| Element-wise operations | `np.add()`, `+` operator | Vectorized, no Python loop overhead |
| Random sampling | `np.random.default_rng()` | Reproducible, efficient distributions |
| Filtering data | Boolean indexing | Select elements matching conditions |
| Linear algebra | `np.linalg.solve()` | LAPACK-backed solvers |
| Fourier transforms | `np.fft.fft()` | Optimized FFT implementations |
| Image processing | Array slicing + operations | Pixel-level manipulation in one line |
| Statistical analysis | `np.mean()`, `np.std()` | Built-in axis support |

## How

### Array Creation

```python
import numpy as np

# From Python lists
arr = np.array([1, 2, 3, 4, 5])           # 1D
matrix = np.array([[1, 2], [3, 4]])        # 2D

# Built-in constructors
zeros = np.zeros((3, 4))                   # 3x4 zeros
ones = np.ones((2, 3), dtype=np.float32)   # 2x3 ones, float32
identity = np.eye(5)                       # 5x5 identity matrix
full = np.full((3, 3), 7.0)                # 3x3 filled with 7.0
arange = np.arange(0, 10, 2)              # [0, 2, 4, 6, 8]
linspace = np.linspace(0, 1, 100)          # 100 points from 0 to 1

# Random
rng = np.random.default_rng(seed=42)
normal = rng.normal(0, 1, size=(1000,))    # Standard normal
uniform = rng.uniform(0, 1, size=(5, 5))   # Uniform [0, 1)
```

### Array Operations

```python
import numpy as np

a = np.array([1, 2, 3, 4, 5])
b = np.array([6, 7, 8, 9, 10])

# Element-wise operations (vectorized — no loops needed)
addition = a + b            # [7, 9, 11, 13, 15]
multiplication = a * b      # [6, 14, 24, 36, 50]
power = a ** 2              # [1, 4, 9, 16, 25]

# Aggregation
total = a.sum()             # 15
mean = a.mean()             # 3.0
std = a.std()               # 1.414...
max_val = a.max()           # 5

# Axis-based operations (2D)
matrix = np.array([[1, 2, 3], [4, 5, 6]])
row_sums = matrix.sum(axis=1)    # [6, 15]
col_sums = matrix.sum(axis=0)    # [5, 7, 9]
```

### Broadcasting

```python
import numpy as np

# Broadcasting eliminates loops when operating on different-shaped arrays
matrix = np.array([[1, 2, 3], [4, 5, 6]])  # shape: (2, 3)
vector = np.array([10, 20, 30])              # shape: (3,)

# vector is "broadcast" across rows automatically
result = matrix + vector
# [[11, 22, 33],
#  [14, 25, 36]]

# Scalar broadcasting
scaled = matrix * 2       # [[2, 4, 6], [8, 10, 12]]

# Adding a column to a 2D array
col_vector = np.array([[1], [2]])  # shape: (2, 1)
result = matrix + col_vector       # shape: (2, 3)
# [[2, 3, 4],
#  [6, 7, 8]]
```

### Indexing and Slicing

```python
import numpy as np

arr = np.array([10, 20, 30, 40, 50, 60])

# Basic indexing
print(arr[0])      # 10
print(arr[-1])     # 60

# Slicing
print(arr[1:4])    # [20, 30, 40]
print(arr[::2])    # [10, 30, 50]

# 2D indexing
matrix = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
print(matrix[1, 2])       # 6 (row 1, col 2)
print(matrix[0:2, 1:3])   # [[2, 3], [5, 6]]

# Boolean indexing (filtering)
data = np.array([15, 25, 35, 45, 55])
mask = data > 30           # [False, False, True, True, True]
filtered = data[mask]       # [35, 45, 55]

# Fancy indexing (integer arrays)
indices = np.array([0, 2, 4])
print(data[indices])        # [15, 35, 55]
```

### Linear Algebra

```python
import numpy as np

A = np.array([[1, 2], [3, 4]])
B = np.array([[5, 6], [7, 8]])

# Matrix multiplication
product = A @ B              # or np.dot(A, B)
# [[19, 22],
#  [43, 50]]

# Transpose
print(A.T)                  # [[1, 3], [2, 4]]

# Determinant and inverse
det = np.linalg.det(A)      # -2.0
inv = np.linalg.inv(A)      # [[-2, 1], [1.5, -0.5]]

# Solving linear equations: Ax = b
b = np.array([5, 11])
x = np.linalg.solve(A, b)   # [1, 2]

# Eigenvalues and eigenvectors
eigenvalues, eigenvectors = np.linalg.eig(A)
```

### Structured Arrays and View vs Copy

```python
import numpy as np

# Structured arrays (like database rows)
dtype = [('name', 'U10'), ('age', 'i4'), ('salary', 'f8')]
employees = np.array([('Alice', 30, 75000), ('Bob', 25, 65000)], dtype=dtype)
print(employees['salary'])  # [75000, 65000]

# View vs Copy — CRITICAL distinction
arr = np.array([1, 2, 3, 4, 5])

# View: shares memory, changes propagate
view = arr[1:4]            # Creates a VIEW
view[0] = 999
print(arr[1])              # 999 — original modified!

# Copy: independent data
copy = arr[1:4].copy()     # Creates a COPY
copy[0] = 888
print(arr[1])              # 999 — original unchanged

# Check if arrays share memory
print(np.shares_memory(arr, view))   # True
print(np.shares_memory(arr, copy))   # False
```

## Production Checklist

- [ ] **Specify `dtype` explicitly** — prevents unexpected type coercion and reduces memory
- [ ] **Use `default_rng()`** — new random API with proper seeding and independence
- [ ] **Avoid Python loops on arrays** — use vectorized operations or `np.vectorize`
- [ ] **Check `shares_memory()` when debugging** — unexpected mutations often come from views
- [ ] **Use `np.save()`/`np.load()` for persistence** — binary format is fast and compact
- [ ] **Handle `NaN` explicitly** — use `np.nanmean()`, `np.nanstd()` instead of `np.mean()`
- [ ] **Validate array shapes before operations** — broadcast errors are cryptic; check early
- [ ] **Pin NumPy version in production** — ABI changes between minor versions can break code

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **Lists** | Uses Python lists for everything. Nested loops. |
| 2 | **Arrays** | Uses `np.array()`. Basic operations like `sum()`, `mean()`. |
| 3 | **Vectorized** | Replaces loops with broadcasting. Uses boolean indexing. |
| 4 | **Advanced** | Linear algebra, FFT, structured arrays, memory-efficient dtypes. |
| 5 | **Expert** | Custom ufuncs, C extensions, GPU arrays (CuPy), understanding memory layouts. |

## Common Myths

### Myth 1: "NumPy is just faster Python lists"
**Reality:** NumPy isn't just faster lists — it's a fundamentally different abstraction. Arrays are homogeneous, contiguous memory blocks with fixed types. Lists are heterogeneous, pointer-based collections. The performance difference comes from eliminating per-element Python overhead and enabling cache-friendly computation.

### Myth 2: "Broadcasting is just syntactic sugar for loops"
**Reality:** Broadcasting creates actual memory-efficient operations. It doesn't copy data — it computes strides on-the-fly to simulate expanded shapes. A `(n, 1) + (1, m)` operation produces `(n, m)` without allocating the intermediate `(n, m)` array.

### Myth 3: "NumPy arrays are always faster than lists"
**Reality:** For very small arrays (1-10 elements), Python lists can be faster due to NumPy's function call overhead. For string-heavy or object-typed operations, lists often win. NumPy shines for numeric operations on arrays of 100+ elements.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Create array | `np.array([...])` | Convert list to array |
| Zeros/Ones | `np.zeros((r, c))` | Initialize arrays |
| Vectorized ops | `a + b`, `a * b` | Element-wise operations |
| Aggregation | `.sum()`, `.mean()`, `.std()` | Statistical summaries |
| Boolean indexing | `arr[arr > 0]` | Filter by condition |
| Broadcasting | `matrix + vector` | Shape-compatible operations |
| Linear algebra | `np.linalg.solve()` | Solve equations |
| View vs copy | `arr[1:4]` vs `.copy()` | Memory sharing control |
| Random numbers | `np.random.default_rng()` | Reproducible randomness |
| Reshape | `.reshape(r, c)` | Change array dimensions |

## Related Topics

- [20-libraries-pandas](../20-libraries-pandas/) - Built on NumPy for tabular data
- [03-advanced](../03-advanced/) - Generators and iterators for large datasets
- [15-performance](../15-performance/) - Memory and CPU optimization
- [06-type-hints](../06-type-hints/) - Typed NumPy arrays with NDArray

---

> **Remember:** NumPy is the lingua franca of scientific Python. Learn its broadcasting rules, understand views vs copies, and your data processing code will be fast and correct.
