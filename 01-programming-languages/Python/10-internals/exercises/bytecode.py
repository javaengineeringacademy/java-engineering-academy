"""
Module 10 - Python Internals: Bytecode Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Understanding Python bytecode, dis module, and compilation
"""

import dis
import sys
from types import CodeType


# =============================================================================
# Exercise 1: Basic Bytecode Inspection (⭐⭐⭐)
# =============================================================================

def exercise_1_basic_bytecode():
    """
    Use the dis module to display the bytecode of the following function.
    Identify the LOAD_CONST, STORE_NAME, and RETURN_VALUE instructions.
    
    TODO:
    1. Use dis.dis() to display the bytecode
    2. Count the total number of bytecode instructions
    3. Return the count
    """
    def sample_function(x, y):
        result = x + y
        if result > 10:
            return "large"
        return "small"
    
    # TODO: Use dis.dis() to show bytecode
    # TODO: Count instructions and return the count
    pass


# =============================================================================
# Exercise 2: Bytecode Analysis (⭐⭐⭐⭐)
# =============================================================================

def exercise_2_bytecode_analysis():
    """
    Analyze the bytecode of a function with a loop.
    Identify the JUMP instructions and understand control flow.
    
    TODO:
    1. Get the code object using compile() or __code__
    2. Use dis.get_instructions() to get structured instruction info
    3. Find all jump instructions (JUMP_ABSOLUTE, POP_JUMP_IF_FALSE, etc.)
    4. Return a list of jump instruction names
    """
    def loop_function(n):
        total = 0
        for i in range(n):
            if i % 2 == 0:
                total += i
        return total
    
    # TODO: Analyze bytecode for jump instructions
    pass


# =============================================================================
# Exercise 3: Code Object Attributes (⭐⭐⭐)
# =============================================================================

def exercise_3_code_object_attributes():
    """
    Explore the attributes of a code object.
    
    TODO:
    1. Get the __code__ attribute of the function
    2. Extract: co_argcount, co_nlocals, co_stacksize, co_flags
    3. Return these values as a dictionary
    """
    def complex_function(a, b, c=10):
        x = a + b
        y = x * c
        z = [i for i in range(y)]
        return z
    
    # TODO: Extract code object attributes
    pass


# =============================================================================
# Exercise 4: Bytecode Comparison (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_bytecode_comparison():
    """
    Compare the bytecode efficiency of two equivalent functions.
    
    TODO:
    1. Get bytecode instructions for both functions
    2. Count the number of instructions in each
    3. Return a dict with counts and which is more efficient
    """
    def list_comprehension():
        return [x**2 for x in range(100)]
    
    def loop_version():
        result = []
        for x in range(100):
            result.append(x**2)
        return result
    
    # TODO: Compare bytecode instruction counts
    pass


# =============================================================================
# Exercise 5: Bytecode Optimization (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_bytecode_optimization():
    """
    Write a function that takes source code as a string and returns
    the bytecode optimization report.
    
    TODO:
    1. Compile the source string to a code object
    2. Analyze for common patterns:
       - LOAD followed by POP (dead code)
       - Repeated LOAD_CONST
    3. Return a report dict with findings
    """
    source_code = """
x = 1
y = 2
z = x + y
print(z)
"""
    
    # TODO: Compile and analyze source code
    pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 10 - Bytecode Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Bytecode Inspection")
    try:
        result = exercise_1_basic_bytecode()
        print(f"  Result: {result} instructions")
        assert isinstance(result, int), "Should return an integer"
        assert result > 0, "Should have positive instruction count"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Bytecode Analysis")
    try:
        result = exercise_2_bytecode_analysis()
        print(f"  Jump instructions: {result}")
        assert isinstance(result, list), "Should return a list"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Code Object Attributes")
    try:
        result = exercise_3_code_object_attributes()
        print(f"  Attributes: {result}")
        assert isinstance(result, dict), "Should return a dictionary"
        assert 'co_argcount' in result, "Missing co_argcount"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Bytecode Comparison")
    try:
        result = exercise_4_bytecode_comparison()
        print(f"  Comparison: {result}")
        assert isinstance(result, dict), "Should return a dictionary"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Bytecode Optimization")
    try:
        result = exercise_5_bytecode_optimization()
        print(f"  Report: {result}")
        assert isinstance(result, dict), "Should return a dictionary"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
