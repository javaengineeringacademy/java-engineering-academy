# Mutation Testing Theory

## Overview
Mutation testing evaluates test quality by introducing small changes (mutants) to source code.

## Core Concepts
- **Mutant**: Modified version of the program
- **Killed**: Test detects the change (GOOD)
- **Survived**: Test does not detect (BAD)
- **Equivalent**: No test can distinguish (IGNORE)

## Mutation Operators

| Type | Operator | Example |
|------|----------|---------|
| Value | Return value | `return x` -> `return 0` |
| Conditional | Boundary | `x > 0` -> `x >= 0` |
| Conditional | Negation | `x > 0` -> `!(x > 0)` |
| Statement | Void removal | `foo()` -> removed |

## Theoretical Framework
- **Strong Mutation**: Program reaches mutation point + different output
- **Weak Mutation**: Reaches mutation point + different state
- **Selective Mutation**: Subset of operators to reduce cost

## Comparison with Coverage

| Aspect | Code Coverage | Mutation Testing |
|--------|--------------|------------------|
| Measures | Code execution | Test effectiveness |
| Strength | Weak | Strong |
| Cost | Low | Medium-High |
