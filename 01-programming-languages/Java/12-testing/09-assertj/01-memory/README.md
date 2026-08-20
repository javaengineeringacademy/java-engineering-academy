# AssertJ - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           Heap Memory               │
│  - AbstractAssert instance          │
│  - Actual value reference           │
│  - Error message builder            │
│  - Chained assertion state          │
├─────────────────────────────────────┤
│          Stack Memory               │
│  - assertThat() frame               │
│  - Each chaining method frame       │
│  - Error message construction       │
└─────────────────────────────────────┘
```

## Memory Considerations

- AssertJ assertions are short-lived
- No persistent state between assertions
- Error messages built only on failure
- Object references, not copies

## Performance

- Assertions are inline methods
- No reflection overhead
- Minimal object creation
- Fast execution
