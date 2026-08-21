# Decision: Constructor Access

## When to Use Constructor Access

**Use when:**
- Building dependency injection containers (Spring, Guice)
- Implementing factory patterns with dynamic instantiation
- Creating serialization frameworks that reconstruct objects
- Building plugin systems that load unknown classes
- Writing test utilities that create instances of test classes

**Avoid when:**
- You can use `new` directly — it's faster and safer
- The class has a static factory method — prefer it
- Performance is critical in hot paths

## Decision Matrix

| Need | Approach | Notes |
|------|----------|-------|
| Create with no-arg constructor | `clazz.getDeclaredConstructor().newInstance()` | Safest |
| Create with parameters | `clazz.getDeclaredConstructor(types).newInstance(args)` | Match types exactly |
| Create inner class | Pass enclosing instance as first arg | Required for non-static inner |
| Create via private constructor | `setAccessible(true)` + `newInstance()` | Bypasses access control |
| Create array | `Array.newInstance(type, length)` | For dynamic arrays |
