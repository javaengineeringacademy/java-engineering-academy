# Advanced Mockito - Memory

## Spy Memory Model

```
┌─────────────────────────────────┐
│         Spy Proxy               │
├─────────────────────────────────┤
│  ┌───────────────────────────┐  │
│  │    Real Object State      │  │
│  │  - All fields             │  │
│  │  - All method logic       │  │
│  └───────────────────────────┘  │
├─────────────────────────────────┤
│  ┌───────────────────────────┐  │
│  │    Stub Overlay           │  │
│  │  - Stubbed method mappings│  │
│  │  - Invocation record      │  │
│  └───────────────────────────┘  │
├─────────────────────────────────┤
│  ┌───────────────────────────┐  │
│  │    Method Interceptor     │  │
│  │  - Stub lookup            │  │
│  │  - Real method delegation │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

## Memory Considerations

### Spy Overhead
- ByteBuddy proxy class loaded into metaspace
- Interceptor state per stubbed method
- Invocation record per mock method call

### Answer Lambda Captures
- Lambda captures are kept in memory
- Avoid capturing large objects
- Consider using method references for heavy logic

### Cleanup
- Spies are eligible for GC after test
- Extension-managed spies cleaned up via store
- Static spy references cause memory leaks
