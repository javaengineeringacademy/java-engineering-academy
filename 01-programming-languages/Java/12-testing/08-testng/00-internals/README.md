# TestNG - Internals

## Test Execution Engine

1. **XML Parsing**: Reads suite configuration
2. **Class Discovery**: Finds annotated test classes
3. **Method Sorting**: Orders by dependencies/priorities
4. **Thread Pool Creation**: For parallel execution
5. **Test Execution**: Runs methods respecting constraints
6. **Result Collection**: Gathers pass/fail/skip
7. **Report Generation**: HTML/XML output

## Dependency Resolution

```
Test A (no deps) → Run first
Test B (depends on A) → Run after A
Test C (depends on A, B) → Run after both
```

Dependencies create a DAG (Directed Acyclic Graph). Cycles cause failure.

## Data Provider Flow

```
@DataProvider returns Object[][]
    ↓
For each row:
    - Extract parameters
    - Invoke test method
    - Collect result
    ↓
Aggregate all results
```

## Parallel Execution Model

- **methods**: Each method in separate thread
- **tests**: Each <test> tag in separate thread
- **classes**: Each class in separate thread
- **instances**: Each instance in separate thread
