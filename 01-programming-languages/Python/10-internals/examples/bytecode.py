"""
Python Bytecode Internals
Demonstrates bytecode inspection and understanding
"""

import dis
import sys
import types

# ============================================
# Basic Bytecode Inspection
# ============================================

def simple_function(x: int, y: int) -> int:
    """A simple function to inspect."""
    result = x + y
    return result

def inspect_bytecode() -> None:
    """Inspect bytecode of a function."""
    print("=== Bytecode for simple_function ===")
    dis.dis(simple_function)
    
    # Get code object
    code = simple_function.__code__
    print(f"\nCode object attributes:")
    print(f"  co_argcount: {code.co_argcount}")
    print(f"  co_varnames: {code.co_varnames}")
    print(f"  co_consts: {code.co_consts}")
    print(f"  co_names: {code.co_names}")

# ============================================
# Bytecode Instructions
# ============================================

def demonstrate_instructions() -> None:
    """Show different bytecode instructions."""
    def example():
        x = 1
        y = 2
        z = x + y
        if z > 2:
            return True
        return False
    
    print("\n=== Bytecode Instructions ===")
    dis.dis(example)

# ============================================
# Code Object Properties
# ============================================

def explore_code_object() -> None:
    """Explore code object properties."""
    def nested():
        """Nested function."""
        x = 10
        return x * 2
    
    code = nested.__code__
    
    print("\n=== Code Object Properties ===")
    print(f"  Filename: {code.co_filename}")
    print(f"  Function name: {code.co_name}")
    print(f"  First line number: {code.co_firstlineno}")
    print(f"  Stack size: {code.co_stacksize}")
    print(f"  Flags: {code.co_flags}")
    print(f"  Constants: {code.co_consts}")
    print(f"  Names: {code.co_names}")
    print(f"  Variable names: {code.co_varnames}")

# ============================================
# Comparing Bytecode
# ============================================

def compare_approaches() -> None:
    """Compare bytecode of different approaches."""
    def using_loop():
        result = []
        for i in range(10):
            result.append(i * 2)
        return result
    
    def using_comprehension():
        return [i * 2 for i in range(10)]
    
    def using_map():
        return list(map(lambda x: x * 2, range(10)))
    
    print("\n=== Comparing Approaches ===")
    print("\nLoop approach:")
    dis.dis(using_loop)
    
    print("\nComprehension approach:")
    dis.dis(using_comprehension)
    
    print("\nMap approach:")
    dis.dis(using_map)

# ============================================
# Bytecode Optimization
# ============================================

def demonstrate_optimization() -> None:
    """Show Python's bytecode optimizations."""
    def constant_folding():
        # Python optimizes constant expressions
        return 2 + 3  # May be folded to 5
    
    def string_concatenation():
        # String concatenation optimization
        return "Hello" + " " + "World"
    
    print("\n=== Bytecode Optimization ===")
    print("Constant folding:")
    dis.dis(constant_folding)
    
    print("\nString concatenation:")
    dis.dis(string_concatenation)

# ============================================
# Bytecode Compilation
# ============================================

def compile_example() -> None:
    """Demonstrate bytecode compilation."""
    source_code = """
def factorial(n):
    if n <= 1:
        return 1
    return n * factorial(n - 1)
"""
    
    print("\n=== Compiling Source Code ===")
    code = compile(source_code, "<string>", "exec")
    print(f"Compiled code object: {code}")
    print(f"Type: {type(code)}")
    
    # Execute the compiled code
    namespace = {}
    exec(code, namespace)
    factorial = namespace['factorial']
    print(f"5! = {factorial(5)}")
    print(f"10! = {factorial(10)}")

# ============================================
# Bytecode Analysis
# ============================================

def analyze_bytecode() -> None:
    """Analyze bytecode for specific patterns."""
    def analyze_function(func):
        code = func.__code__
        instructions = list(dis.get_instructions(code))
        
        print(f"\nFunction: {func.__name__}")
        print(f"  Total instructions: {len(instructions)}")
        print(f"  Stack size: {code.co_stacksize}")
        
        # Count instruction types
        op_counts = {}
        for instr in instructions:
            opname = instr.opname
            op_counts[opname] = op_counts.get(opname, 0) + 1
        
        print("  Instruction counts:")
        for op, count in sorted(op_counts.items(), key=lambda x: -x[1])[:5]:
            print(f"    {op}: {count}")
    
    def simple():
        x = 1
        y = 2
        return x + y
    
    def complex_logic():
        result = []
        for i in range(10):
            if i % 2 == 0:
                result.append(i ** 2)
        return result
    
    analyze_function(simple)
    analyze_function(complex_logic)

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    inspect_bytecode()
    demonstrate_instructions()
    explore_code_object()
    compare_approaches()
    demonstrate_optimization()
    compile_example()
    analyze_bytecode()
