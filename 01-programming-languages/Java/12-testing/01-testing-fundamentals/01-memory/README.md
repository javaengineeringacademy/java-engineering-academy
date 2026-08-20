# Testing Fundamentals - Memory

## JVM Memory During Test Execution

```
┌─────────────────────────────────────────┐
│              Method Area                │
│  - Test class bytecode                  │
│  - Production class bytecode            │
│  - Framework classes (JUnit, Mockito)   │
├─────────────────────────────────────────┤
│              Heap Memory                │
│  - Test instance objects                │
│  - Production objects under test        │
│  - Mock objects (ByteBuddy proxies)     │
│  - Test data and fixtures               │
├─────────────────────────────────────────┤
│             Stack Memory                │
│  - Test method frames                   │
│  - Assertion call frames                │
│  - Exception handling frames            │
└─────────────────────────────────────────┘
```

## Memory Considerations

### Test Instance Allocation
- JUnit 5 creates a **new instance per test** to ensure isolation
- Each instance is eligible for GC after test completion
- Static fields persist across all tests in a class

### Mock Object Memory
- Mocks use ByteBuddy proxies (subclass generation)
- Each mock adds overhead: proxy class + interceptor state
- `@Mock` annotations are initialized via `MockitoAnnotations.openMocks()`

### Memory Leak Risks
- Static collections that grow across tests
- Unclosed resources in `@BeforeAll`/`@AfterAll`
- Thread pools not properly shut down
- File handles not released in teardown

## Best Practices for Memory

1. Use `@BeforeEach` to reset mutable state
2. Close resources in `@AfterEach` or use try-with-resources
3. Avoid static mutable fields in test classes
4. Use `@AfterAll` for expensive one-time cleanup
5. Limit test parallelism based on available heap
