# JUnit 5 Advanced - Memory

## Parameterized Test Memory

```
┌─────────────────────────────────────────────┐
│           Parameterized Test Memory         │
├─────────────────────────────────────────────┤
│  Argument Cache                             │
│  - Pre-generated argument sets              │
│  - Streamed lazily (method source)          │
│  - Materialized for CSV/file sources        │
├─────────────────────────────────────────────┤
│  Test Instance Pool                         │
│  - New instance per parameter set           │
│  - Includes injected parameters             │
│  - GC eligible after test completes         │
├─────────────────────────────────────────────┤
│  Result Accumulator                         │
│  - Pass/fail per parameter set              │
│  - Aggregated into single report entry      │
└─────────────────────────────────────────────┘
```

## Dynamic Test Memory

- Test descriptors created during discovery
- Test bodies are lambdas (captured variables)
- Execution creates temporary test instances
- No persistent state between dynamic tests

## Extension Store Memory

- Store is keyed by Namespace
- GLOBAL namespace persists across test classes
- Class/method namespaces cleaned up after tests
- AutoCloseable resources closed on cleanup

## Memory Optimization

1. Use lazy streams for large parameter sets
2. Avoid capturing large objects in lambdas
3. Close extension store resources
4. Limit parallel test execution on low-memory systems
5. Use @BeforeAll for expensive shared setup
