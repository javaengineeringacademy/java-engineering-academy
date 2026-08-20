# Unit Testing - Memory

## Memory Model

```
┌──────────────────────────────────────┐
│           Method Area                │
│  - Test class bytecode               │
│  - Production class bytecode         │
│  - Mock class (ByteBuddy generated)  │
├──────────────────────────────────────┤
│           Heap Memory                │
│  - Test instance (new per test)      │
│  - Mock object                       │
│  - Class under test                  │
│  - Test data fixtures                │
├──────────────────────────────────────┤
│          Stack Memory                │
│  - Test method frame                 │
│  - Setup/teardown frames             │
│  - Assertion frames                  │
└──────────────────────────────────────┘
```

## Memory Considerations

### Test Instance
- New instance created per test method
- Eligible for GC after test completes
- Static fields persist across all tests

### Mock Overhead
- Each mock creates a proxy class in metaspace
- Invocation interceptor state in heap
- Argument matcher state per verification

### Memory Leaks
- Static collections that grow
- Unclosed resources in @BeforeAll
- Thread pools not shut down
- File handles not released

## Optimization Tips

1. Use @BeforeEach to reset state
2. Close resources in @AfterEach
3. Avoid static mutable fields
4. Use try-with-resources
5. Limit test parallelism
