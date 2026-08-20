# Hamcrest - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           Heap Memory               │
│  - Matcher instances (stateless)    │
│  - Description builder              │
│  - Actual value reference           │
├─────────────────────────────────────┤
│          Stack Memory               │
│  - assertThat() frame               │
│  - matcher.matches() frame          │
│  - describeTo() frame               │
│  - describeMismatch() frame         │
└─────────────────────────────────────┘
```

## Memory Considerations

- Matchers are stateless and reusable
- Description objects are temporary
- No persistent state between assertions
- Matchers can be static constants

## Best Practices

1. Define matchers as static constants
2. Avoid mutable state in matchers
3. Use TypeSafeMatcher for type safety
4. Keep matcher logic simple
