"""
NumPy Array Basics
Demonstrates core NumPy concepts: creation, operations, indexing, and broadcasting
"""

import numpy as np

# ============================================
# Array Creation
# ============================================

def array_creation_examples():
    print("=== Array Creation ===")

    # From Python lists
    arr_1d = np.array([1, 2, 3, 4, 5])
    arr_2d = np.array([[1, 2, 3], [4, 5, 6]])
    print(f"1D array: {arr_1d}")
    print(f"2D array:\n{arr_2d}")

    # Built-in constructors
    zeros = np.zeros((2, 3))
    ones = np.ones((2, 3), dtype=np.float32)
    eye = np.eye(4)
    print(f"Zeros:\n{zeros}")
    print(f"Ones:\n{ones}")
    print(f"Identity:\n{eye}")

    # Sequences
    arange = np.arange(0, 10, 2)
    linspace = np.linspace(0, 1, 5)
    print(f"arange: {arange}")
    print(f"linspace: {linspace}")

# ============================================
# Array Operations
# ============================================

def array_operations():
    print("\n=== Array Operations ===")

    a = np.array([1, 2, 3, 4, 5])
    b = np.array([10, 20, 30, 40, 50])

    # Element-wise operations
    print(f"a + b = {a + b}")
    print(f"a * b = {a * b}")
    print(f"a ** 2 = {a ** 2}")

    # Aggregation
    print(f"Sum: {a.sum()}")
    print(f"Mean: {a.mean()}")
    print(f"Std: {a.std():.4f}")
    print(f"Min: {a.min()}, Max: {a.max()}")

    # Axis-based operations on 2D
    matrix = np.array([[1, 2, 3], [4, 5, 6]])
    print(f"Row sums: {matrix.sum(axis=1)}")
    print(f"Column sums: {matrix.sum(axis=0)}")

# ============================================
# Broadcasting
# ============================================

def broadcasting_examples():
    print("\n=== Broadcasting ===")

    matrix = np.array([[1, 2, 3], [4, 5, 6]])
    vector = np.array([10, 20, 30])

    # Vector broadcast across rows
    result = matrix + vector
    print(f"Matrix + Vector:\n{result}")

    # Scalar broadcasting
    print(f"Matrix * 2:\n{matrix * 2}")

    # Column vector broadcasting
    col = np.array([[1], [2]])
    print(f"Matrix + Column:\n{matrix + col}")

# ============================================
# Indexing and Slicing
# ============================================

def indexing_examples():
    print("\n=== Indexing and Slicing ===")

    arr = np.array([10, 20, 30, 40, 50, 60])

    # Basic indexing
    print(f"First element: {arr[0]}")
    print(f"Last element: {arr[-1]}")

    # Slicing
    print(f"Slice [1:4]: {arr[1:4]}")
    print(f"Every other: {arr[::2]}")

    # 2D indexing
    matrix = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
    print(f"Element [1,2]: {matrix[1, 2]}")
    print(f"Submatrix:\n{matrix[0:2, 1:3]}")

    # Boolean indexing
    data = np.array([15, 25, 35, 45, 55])
    mask = data > 30
    print(f"Filtered (>30): {data[mask]}")

# ============================================
# View vs Copy
# ============================================

def view_vs_copy():
    print("\n=== View vs Copy ===")

    arr = np.array([1, 2, 3, 4, 5])

    # View shares memory
    view = arr[1:4]
    view[0] = 999
    print(f"After modifying view, original: {arr}")  # [1, 999, 3, 4, 5]

    # Copy is independent
    arr2 = np.array([10, 20, 30, 40, 50])
    copy = arr2[1:4].copy()
    copy[0] = 888
    print(f"After modifying copy, original: {arr2}")  # [10, 20, 30, 40, 50]

    # Check memory sharing
    print(f"View shares memory: {np.shares_memory(arr, view)}")
    print(f"Copy shares memory: {np.shares_memory(arr2, copy)}")

# ============================================
# Linear Algebra
# ============================================

def linalg_examples():
    print("\n=== Linear Algebra ===")

    A = np.array([[1, 2], [3, 4]])
    B = np.array([[5, 6], [7, 8]])

    # Matrix multiplication
    print(f"A @ B:\n{A @ B}")

    # Determinant and inverse
    print(f"det(A): {np.linalg.det(A):.1f}")
    print(f"inv(A):\n{np.linalg.inv(A)}")

    # Solving Ax = b
    b = np.array([5, 11])
    x = np.linalg.solve(A, b)
    print(f"Solution to Ax=b: {x}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    array_creation_examples()
    array_operations()
    broadcasting_examples()
    indexing_examples()
    view_vs_copy()
    linalg_examples()

    print("\nAll examples completed!")
