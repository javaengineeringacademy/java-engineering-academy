# TestNG - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           Method Area               │
│  - TestNG framework classes         │
│  - Test class bytecode              │
│  - XML configuration data           │
├─────────────────────────────────────┤
│           Heap Memory               │
│  - Test instances (per method)      │
│  - Data provider results            │
│  - Shared test state                │
│  - Thread-local storage             │
├─────────────────────────────────────┤
│         Thread Pool                 │
│  - Worker threads for parallel      │
│  - Each thread has own stack        │
│  - Shared heap for all threads      │
└─────────────────────────────────────┘
```

## Thread Safety

- Static fields shared across threads
- Use ThreadLocal for thread-specific data
- synchronized blocks for shared mutable state
- ConcurrentHashMap for concurrent access

## Memory Considerations

1. Data providers load all data into memory
2. Large datasets may cause heap pressure
3. Thread pool consumes additional memory
4. Test instances created per method (default)
5. Factory instances persist for entire suite
