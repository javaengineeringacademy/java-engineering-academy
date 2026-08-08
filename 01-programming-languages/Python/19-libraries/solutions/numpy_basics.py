"""
Module 19 - Libraries: NumPy Basics Solutions
Complete solutions with explanations
"""

import numpy as np


# =============================================================================
# Exercise 1: Array Creation - SOLUTION
# =============================================================================

def exercise_1_array_creation():
    """
    Create NumPy arrays in different ways.
    """
    # 1. Create array from list
    arr_from_list = np.array([1, 2, 3, 4, 5])
    
    # 2. Create array with specific values
    zeros = np.zeros((3, 3))
    ones = np.ones((2, 4))
    full = np.full((2, 3), 7)
    
    # 3. Create array with ranges
    arange = np.arange(0, 10, 2)
    linspace = np.linspace(0, 1, 5)
    
    # 4. Random arrays
    random_normal = np.random.randn(3, 3)
    random_int = np.random.randint(0, 10, (3, 3))
    
    return {
        'from_list': arr_from_list,
        'zeros': zeros,
        'ones': ones,
        'arange': arange,
        'linspace': linspace,
        'random_shape': random_int.shape,
    }


# =============================================================================
# Exercise 2: Array Operations - SOLUTION
# =============================================================================

def exercise_2_array_operations():
    """
    Perform basic array operations.
    """
    a = np.array([1, 2, 3, 4, 5])
    b = np.array([10, 20, 30, 40, 50])
    
    # 1. Element-wise operations
    addition = a + b
    multiplication = a * b
    power = a ** 2
    
    # 2. Aggregation operations
    sum_val = np.sum(a)
    mean_val = np.mean(a)
    max_val = np.max(a)
    min_val = np.min(a)
    
    # 3. Broadcasting
    matrix = np.array([[1, 2, 3], [4, 5, 6]])
    scaled = matrix * 2  # Broadcast scalar
    
    return {
        'addition': addition,
        'multiplication': multiplication,
        'power': power,
        'sum': sum_val,
        'mean': mean_val,
        'max': max_val,
        'min': min_val,
        'broadcast_scaled': scaled,
    }


# =============================================================================
# Exercise 3: Indexing and Slicing - SOLUTION
# =============================================================================

def exercise_3_indexing_slicing():
    """
    Index and slice arrays.
    """
    arr = np.array([10, 20, 30, 40, 50, 60, 70, 80, 90])
    
    # 1. Basic indexing
    first = arr[0]
    last = arr[-1]
    
    # 2. Slicing
    slice1 = arr[2:5]  # [30, 40, 50]
    slice2 = arr[::2]  # Every other element
    slice3 = arr[::-1]  # Reversed
    
    # 3. Boolean indexing
    arr2 = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    evens = arr2[arr2 % 2 == 0]
    
    # 4. Fancy indexing
    indices = [0, 2, 4]
    selected = arr2[indices]
    
    # 5. 2D indexing
    matrix = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
    element = matrix[1, 2]
    row = matrix[0, :]
    col = matrix[:, 1]
    
    return {
        'first': first,
        'last': last,
        'slice': slice1,
        'evens': evens,
        'selected': selected,
        'matrix_element': element,
        'matrix_row': row,
        'matrix_col': col,
    }


# =============================================================================
# Exercise 4: Reshaping - SOLUTION
# =============================================================================

def exercise_4_reshaping():
    """
    Reshape and manipulate arrays.
    """
    arr = np.arange(12)
    
    # 1. Reshape
    reshaped_3x4 = arr.reshape(3, 4)
    reshaped_4x3 = arr.reshape(4, 3)
    reshaped_2x6 = arr.reshape(2, 6)
    
    # 2. Transpose
    transposed = reshaped_3x4.T
    
    # 3. Flatten and ravel
    flattened = reshaped_3x4.flatten()
    raveled = reshaped_3x4.ravel()
    
    # 4. Resize
    resized = np.resize(arr, (3, 3))
    
    # 5. Concatenate
    a = np.array([[1, 2], [3, 4]])
    b = np.array([[5, 6], [7, 8]])
    vstack = np.vstack((a, b))
    hstack = np.hstack((a, b))
    
    return {
        'original_shape': arr.shape,
        'reshaped_3x4': reshaped_3x4.shape,
        'transposed': transposed.shape,
        'flattened': flattened,
        'vstack': vstack,
        'hstack': hstack,
    }


# =============================================================================
# Exercise 5: Linear Algebra - SOLUTION
# =============================================================================

def exercise_5_linear_algebra():
    """
    Perform linear algebra operations.
    """
    A = np.array([[1, 2], [3, 4]])
    B = np.array([[5, 6], [7, 8]])
    
    # 1. Matrix multiplication
    product = A @ B  # or np.dot(A, B)
    
    # 2. Determinant
    det = np.linalg.det(A)
    
    # 3. Eigenvalues and eigenvectors
    eigenvalues, eigenvectors = np.linalg.eig(A)
    
    # 4. Inverse
    inverse = np.linalg.inv(A)
    
    # 5. Solve linear system
    # Ax = b
    b = np.array([5, 11])
    x = np.linalg.solve(A, b)
    
    return {
        'product': product,
        'determinant': round(det, 2),
        'eigenvalues': eigenvalues,
        'inverse': inverse,
        'solution': x,
    }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 19 - NumPy Basics Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Array Creation")
    result = exercise_1_array_creation()
    assert result['from_list'].shape == (5,)
    assert result['zeros'].shape == (3, 3)
    assert result['arange'].tolist() == [0, 2, 4, 6, 8]
    print(f"  Arrays created successfully")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Array Operations")
    result = exercise_2_array_operations()
    assert result['addition'].tolist() == [11, 22, 33, 44, 55]
    assert result['sum'] == 15
    assert result['mean'] == 3.0
    print(f"  Operations completed")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Indexing and Slicing")
    result = exercise_3_indexing_slicing()
    assert result['first'] == 10
    assert result['last'] == 90
    assert result['slice'].tolist() == [30, 40, 50]
    print(f"  Indexing works correctly")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Reshaping")
    result = exercise_4_reshaping()
    assert result['reshaped_3x4'] == (3, 4)
    assert result['transposed'] == (4, 3)
    print(f"  Reshaping works correctly")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Linear Algebra")
    result = exercise_5_linear_algebra()
    assert result['product'].shape == (2, 2)
    assert abs(result['determinant'] - (-2)) < 0.01
    print(f"  Linear algebra works correctly")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
