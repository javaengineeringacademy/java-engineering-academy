# Decision: Class Introspection

## When to Use Class Introspection

**Use when:**
- Building frameworks that need to discover class metadata dynamically
- Creating DI containers that scan for annotations on classes
- Implementing serialization frameworks that map class structure to formats
- Building testing tools that discover test methods and fields
- Writing code generators that read class structure

**Avoid when:**
- You know the exact type at compile time — use direct access
- You only need to check `instanceof` — use `isAssignableFrom()` or direct casting
- Performance is critical — caching metadata is essential

## Decision Matrix

| Need | Method | Performance |
|------|--------|-------------|
| Runtime type of instance | `obj.getClass()` | Fast |
| Type known at compile time | `Type.class` | Fastest |
| Type from config string | `Class.forName()` | Slow (first call) |
| Check if A is subtype of B | `B.class.isAssignableFrom(A.class)` | Fast |
| Get class name for logging | `clazz.getName()` | Fast |
| Navigate class hierarchy | `getSuperclass()` / `getInterfaces()` | Fast |

## Common Pitfalls

1. **Assuming `getName()` returns simple name** — Use `getSimpleName()` for display
2. **Forgetting `Class.forName()` throws checked exceptions** — Always handle `ClassNotFoundException`
3. **Confusing `getInterfaces()` (direct) with full hierarchy** — Use `getGenericInterfaces()` for generic info
4. **Ignoring Java 9+ module restrictions** — May need `--add-opens` flags
