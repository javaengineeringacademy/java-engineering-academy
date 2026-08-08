"""
Module 10 - Python Internals: Bytecode Solutions
Complete solutions with explanations
"""

import dis
import sys
from types import CodeType


# =============================================================================
# Exercise 1: Basic Bytecode Inspection - SOLUTION
# =============================================================================

def exercise_1_basic_bytecode():
    """
    Use the dis module to display the bytecode of the following function.
    Identify the LOAD_CONST, STORE_NAME, and RETURN_VALUE instructions.
    """
    def sample_function(x, y):
        result = x + y
        if result > 10:
            return "large"
        return "small"
    
    # Get the bytecode using dis
    bytecode = list(dis.get_instructions(sample_function))
    
    # Count the total number of bytecode instructions
    instruction_count = len(bytecode)
    
    # Optional: Print the disassembly for visualization
    print("\nBytecode for sample_function:")
    dis.dis(sample_function)
    print(f"\nTotal instructions: {instruction_count}")
    
    return instruction_count


# =============================================================================
# Exercise 2: Bytecode Analysis - SOLUTION
# =============================================================================

def exercise_2_bytecode_analysis():
    """
    Analyze the bytecode of a function with a loop.
    Identify the JUMP instructions and understand control flow.
    """
    def loop_function(n):
        total = 0
        for i in range(n):
            if i % 2 == 0:
                total += i
        return total
    
    # Get structured instruction info
    instructions = list(dis.get_instructions(loop_function))
    
    # Find all jump instructions
    jump_instructions = [
        inst.opname for inst in instructions 
        if 'JUMP' in inst.opname
    ]
    
    print("\nJump instructions found:")
    for inst in jump_instructions:
        print(f"  - {inst}")
    
    return jump_instructions


# =============================================================================
# Exercise 3: Code Object Attributes - SOLUTION
# =============================================================================

def exercise_3_code_object_attributes():
    """
    Explore the attributes of a code object.
    """
    def complex_function(a, b, c=10):
        x = a + b
        y = x * c
        z = [i for i in range(y)]
        return z
    
    # Get the code object
    code = complex_function.__code__
    
    # Extract key attributes
    attributes = {
        'co_argcount': code.co_argcount,      # Number of positional arguments
        'co_nlocals': code.co_nlocals,        # Number of local variables
        'co_stacksize': code.co_stacksize,    # Stack size required
        'co_flags': code.co_flags,            # Function flags
        'co_filename': code.co_filename,      # Source file
        'co_name': code.co_name,              # Function name
    }
    
    print("\nCode object attributes:")
    for key, value in attributes.items():
        print(f"  {key}: {value}")
    
    return attributes


# =============================================================================
# Exercise 4: Bytecode Comparison - SOLUTION
# =============================================================================

def exercise_4_bytecode_comparison():
    """
    Compare the bytecode efficiency of two equivalent functions.
    """
    def list_comprehension():
        return [x**2 for x in range(100)]
    
    def loop_version():
        result = []
        for x in range(100):
            result.append(x**2)
        return result
    
    # Get bytecode instruction counts
    comp_instructions = list(dis.get_instructions(list_comprehension))
    loop_instructions = list(dis.get_instructions(loop_version))
    
    comp_count = len(comp_instructions)
    loop_count = len(loop_instructions)
    
    result = {
        'comprehension_count': comp_count,
        'loop_count': loop_count,
        'more_efficient': 'comprehension' if comp_count < loop_count else 'loop',
        'difference': abs(comp_count - loop_count)
    }
    
    print(f"\nList comprehension: {comp_count} instructions")
    print(f"For loop: {loop_count} instructions")
    print(f"More efficient: {result['more_efficient']}")
    
    return result


# =============================================================================
# Exercise 5: Bytecode Optimization - SOLUTION
# =============================================================================

def exercise_5_bytecode_optimization():
    """
    Write a function that takes source code as a string and returns
    the bytecode optimization report.
    """
    source_code = """
x = 1
y = 2
z = x + y
print(z)
"""
    
    # Compile the source string to a code object
    code = compile(source_code, '<string>', 'exec')
    
    # Get bytecode instructions
    instructions = list(dis.get_instructions(code))
    
    # Analyze for common patterns
    report = {
        'total_instructions': len(instructions),
        'load_const_count': sum(1 for i in instructions if i.opname == 'LOAD_CONST'),
        'repeated_constants': False,
        'dead_code_detected': False,
    }
    
    # Check for repeated LOAD_CONST with same value
    constants = [i.argval for i in instructions if i.opname == 'LOAD_CONST']
    if len(constants) != len(set(constants)):
        report['repeated_constants'] = True
    
    print("\nBytecode Optimization Report:")
    print(f"  Total instructions: {report['total_instructions']}")
    print(f"  LOAD_CONST count: {report['load_const_count']}")
    print(f"  Repeated constants: {report['repeated_constants']}")
    
    return report


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 10 - Bytecode Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Bytecode Inspection")
    result = exercise_1_basic_bytecode()
    print(f"  Result: {result} instructions")
    assert isinstance(result, int), "Should return an integer"
    assert result > 0, "Should have positive instruction count"
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Bytecode Analysis")
    result = exercise_2_bytecode_analysis()
    print(f"  Jump instructions: {result}")
    assert isinstance(result, list), "Should return a list"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Code Object Attributes")
    result = exercise_3_code_object_attributes()
    print(f"  Attributes: {result}")
    assert isinstance(result, dict), "Should return a dictionary"
    assert 'co_argcount' in result, "Missing co_argcount"
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Bytecode Comparison")
    result = exercise_4_bytecode_comparison()
    print(f"  Comparison: {result}")
    assert isinstance(result, dict), "Should return a dictionary"
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Bytecode Optimization")
    result = exercise_5_bytecode_optimization()
    print(f"  Report: {result}")
    assert isinstance(result, dict), "Should return a dictionary"
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
