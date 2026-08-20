# Mutation Testing - Internals

## PIT Architecture

```
Source Code + Bytecode
    ↓
Mutation Engine
    ↓
Generate Mutants
    ↓
For Each Mutant:
    ├── Instrument Bytecode
    ├── Run Tests
    ├── Compare Results
    └── Classify (Killed/Survived)
    ↓
Generate Report
```

## Mutation Generation

PIT applies mutations at bytecode level:

1. **Conditionals**: Change comparison operators
2. **Returns**: Modify return values
3. **Math**: Change arithmetic operators
4. **Void**: Remove void method calls
5. **Increments**: Change ++ to --

## Test Execution

For each mutant:
1. Create mutated class
2. Run all tests against mutant
3. Compare test results with original
4. If any test fails: mutant killed
5. If all tests pass: mutant survived

## Reporting

Reports include:
- Mutation score percentage
- List of survived mutants
- Coverage information
- HTML/XML output
