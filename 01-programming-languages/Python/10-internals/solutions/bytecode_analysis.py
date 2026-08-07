"""
Module 10: Python Internals - Bytecode Analysis Solutions
Practice analyzing and understanding Python bytecode.
"""

import dis
import sys
import types


def get_bytecode(func):
    """Return list of (opname, arg) tuples from function bytecode."""
    instructions = dis.get_instructions(func)
    return [(instr.opname, instr.arg) for instr in instructions]


def count_operations(func):
    """Return dict mapping operation name to count."""
    instructions = dis.get_instructions(func)
    counts = {}
    for instr in instructions:
        counts[instr.opname] = counts.get(instr.opname, 0) + 1
    return counts


def estimate_complexity(func):
    """Estimate cyclomatic complexity from bytecode."""
    instructions = dis.get_instructions(func)

    branch_instructions = {
        'JUMP_IF_FALSE', 'JUMP_IF_TRUE', 'JUMP_IF_FALSE_OR_POP',
        'JUMP_IF_TRUE_OR_POP', 'POP_JUMP_IF_FALSE', 'POP_JUMP_IF_TRUE'
    }
    loop_instructions = {'FOR_ITER', 'SETUP_LOOP'}

    branches = 0
    loops = 0

    for instr in instructions:
        if instr.opname in branch_instructions:
            branches += 1
        if instr.opname in loop_instructions:
            loops += 1

    return {
        "branches": branches,
        "loops": loops,
        "total_complexity": branches + loops + 1
    }


def extract_constants(func):
    """Return list of all constants used in function."""
    instructions = dis.get_instructions(func)
    constants = []
    for instr in instructions:
        if instr.opname == 'LOAD_CONST' and instr.argval is not None:
            constants.append(instr.argval)
    return constants


def max_stack_depth(func):
    """Calculate maximum stack depth needed to execute function."""
    instructions = list(dis.get_instructions(func))
    current_depth = 0
    max_depth = 0

    push_instructions = {
        'LOAD_CONST', 'LOAD_NAME', 'LOAD_GLOBAL', 'LOAD_FAST',
        'LOAD_ATTR', 'LOAD_METHOD', 'CALL_FUNCTION', 'CALL_METHOD',
        'BUILD_LIST', 'BUILD_TUPLE', 'BUILD_DICT', 'BUILD_SET',
        'BINARY_ADD', 'BINARY_SUBTRACT', 'BINARY_MULTIPLY',
        'BINARY_TRUE_DIVIDE', 'BINARY_MODULO', 'BINARY_POWER',
        'BINARY_AND', 'BINARY_OR', 'BINARY_XOR',
        'UNARY_POSITIVE', 'UNARY_NEGATIVE', 'UNARY_NOT',
        'FORMAT_VALUE', 'BUILD_STRING'
    }

    pop_instructions = {
        'STORE_NAME', 'STORE_GLOBAL', 'STORE_FAST', 'STORE_ATTR',
        'POP_TOP', 'RETURN_VALUE', 'JUMP_FORWARD',
        'POP_JUMP_IF_FALSE', 'POP_JUMP_IF_TRUE',
        'BINARY_SUBSCR', 'DELETE_NAME', 'DELETE_GLOBAL'
    }

    for instr in instructions:
        if instr.opname in push_instructions:
            current_depth += 1
        elif instr.opname in pop_instructions:
            current_depth = max(0, current_depth - 1)

        max_depth = max(max_depth, current_depth)

    return max(1, max_depth)


if __name__ == "__main__":
    print("Testing Bytecode Analysis Solutions...")

    # Test bytecode viewer
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

    # Test op counter
    def sample_func2():
        x = []
        for i in range(10):
            x.append(i)
        return sum(x)

    result = count_operations(sample_func2)
    assert "LOAD_CONST" in result
    assert "FOR_ITER" in result
    assert result["LOAD_CONST"] >= 3
    print(f"✓ Exercise 2 passed: counted {len(result)} operation types")

    # Test complexity
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

    # Test constant extractor
    def sample_func3():
        x = "hello"
        y = 3.14
        z = [1, 2, 3]
        return (x, y, z, None, True)

    result = extract_constants(sample_func3)
    assert "hello" in result
    assert 3.14 in result
    assert (1, 2, 3) in result
    assert True in result
    print(f"✓ Exercise 4 passed: extracted {len(result)} constants")

    # Test stack depth
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

    print("All Bytecode Analysis solutions passed!")
