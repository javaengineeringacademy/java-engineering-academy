"""
Module 10: Python Internals - Bytecode Analysis Exercises
=========================================================
Practice analyzing and understanding Python bytecode.
"""

import dis
import sys
import types

# =============================================================================
# Exercise 1: Simple Bytecode Viewer (★☆☆☆☆)
# =============================================================================
# TODO: Create function that returns bytecode instructions as list

def get_bytecode(func):
    """Return list of (opname, arg) tuples from function bytecode."""
    # TODO: Use dis.get_instructions to extract bytecode
    pass

# Test Cases
def test_bytecode_viewer():
    def sample_func():
        x = 10
        y = 20
        return x + y
    
    result = get_bytecode(sample_func)
    assert isinstance(result, list)
    assert len(result) > 0
    assert all(isinstance(item, tuple) for item in result)
    assert any(item[0] == 'LOAD_CONST' for item in result)
    print(f"✓ Exercise 1 passed: extracted {len(result)} bytecode instructions")

# =============================================================================
# Exercise 2: Instruction Counter (★★☆☆☆)
# =============================================================================
# TODO: Count different types of operations in bytecode

def count_operations(func):
    """Return dict mapping operation name to count."""
    # TODO: Iterate bytecode and count each operation type
    pass

# Test Cases
def test_op_counter():
    def sample_func():
        x = []
        for i in range(10):
            x.append(i)
        return sum(x)
    
    result = count_operations(sample_func)
    assert "LOAD_CONST" in result
    assert "FOR_ITER" in result
    assert result["LOAD_CONST"] >= 3
    print(f"✓ Exercise 2 passed: counted {len(result)} operation types")

# =============================================================================
# Exercise 3: Complexity Analyzer (★★★☆☆)
# =============================================================================
# TODO: Analyze function complexity from bytecode

def estimate_complexity(func):
    """Estimate cyclomatic complexity from bytecode.
    
    Returns dict with:
    - branches: count of jump instructions
    - loops: count of loop constructs
    - total_complexity: branches + loops + 1
    """
    # TODO: Identify branch and loop instructions
    pass

# Test Cases
def test_complexity():
    def simple_func():
        return 42
    
    def complex_func():
        result = 0
        for i in range(10):
            if i % 2 == 0:
                result += i
            else:
                result -= i
        return result
    
    simple = estimate_complexity(simple_func)
    complex_ = estimate_complexity(complex_func)
    
    assert simple["total_complexity"] == 1
    assert complex_["total_complexity"] > simple["total_complexity"]
    print(f"✓ Exercise 3 passed: simple={simple['total_complexity']}, complex={complex_['total_complexity']}")

# =============================================================================
# Exercise 4: Constant Extractor (★★★☆☆)
# =============================================================================
# TODO: Extract all constants from function bytecode

def extract_constants(func):
    """Return list of all constants used in function."""
    # TODO: Find all LOAD_CONST instructions and extract values
    pass

# Test Cases
def test_constant_extractor():
    def sample_func():
        x = "hello"
        y = 3.14
        z = [1, 2, 3]
        return (x, y, z, None, True)
    
    result = extract_constants(sample_func)
    assert "hello" in result
    assert 3.14 in result
    assert (1, 2, 3) in result  # Lists become tuples in bytecode
    assert None in result
    assert True in result
    print(f"✓ Exercise 4 passed: extracted {len(result)} constants")

# =============================================================================
# Exercise 5: Stack Depth Analyzer (★★★★★)
# =============================================================================
# TODO: Analyze maximum stack depth needed by function

def max_stack_depth(func):
    """Calculate maximum stack depth needed to execute function.
    
    Track stack operations: PUSH/POP, CALL, etc.
    """
    # TODO: Simulate stack operations to find max depth
    pass

# Test Cases
def test_stack_depth():
    def simple():
        return 1
    
    def nested():
        return max(1, 2, 3)
    
    def complex_expr():
        return (1 + 2) * (3 + 4) / (5 + 6)
    
    simple_depth = max_stack_depth(simple)
    nested_depth = max_stack_depth(nested)
    complex_depth = max_stack_depth(complex_expr)
    
    assert simple_depth >= 1
    assert nested_depth >= simple_depth
    print(f"✓ Exercise 5 passed: simple={simple_depth}, nested={nested_depth}, complex={complex_depth}")

if __name__ == "__main__":
    print("Running Bytecode Analysis Exercises...")
    print("=" * 50)
    test_bytecode_viewer()
    test_op_counter()
    test_complexity()
    test_constant_extractor()
    test_stack_depth()
    print("=" * 50)
    print("All tests passed!")
