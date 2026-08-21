# Decision: Field Access

## When to Use Field Access via Reflection

**Use when:**
- Frameworks need to inject values into private fields (Spring DI, JPA column mapping)
- Serialization libraries must read field values without getters
- Test utilities need to access private state for verification
- Configuration frameworks map properties to fields via annotations
- ORM frameworks map database columns to fields

**Avoid when:**
- Public getters/setters exist — use them instead
- The class provides a proper API for field access
- Performance is critical and fields are accessed in hot paths
- You need to bypass security checks without justification

## Decision Matrix

| Field Access Need | Approach | Notes |
|-------------------|----------|-------|
| Public field | `field.get(obj)` | No setAccessible needed |
| Private field | `setAccessible(true)` + `field.get(obj)` | Bypasses access control |
| Static field | `field.get(null)` | Pass null as instance |
| Final field | `setAccessible(true)` + `field.set()` | Unreliable, avoid |
| Field metadata | `field.getType()`, `field.getModifiers()` | No access issues |

## Performance Considerations

```java
// GOOD: Cache field and call setAccessible once
Field field = clazz.getDeclaredField("name");
field.setAccessible(true);

// BAD: Looking up field every time
for (Object obj : objects) {
    Field f = clazz.getDeclaredField("name"); // Lookup overhead
    f.setAccessible(true); // Security check overhead
    f.get(obj);
}
```
