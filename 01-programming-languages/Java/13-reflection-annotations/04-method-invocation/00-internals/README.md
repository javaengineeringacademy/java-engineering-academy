# Internals: Method Invocation

## How Method.invoke() Works

### Method Resolution

When you call `getDeclaredMethod("name", paramTypes)`:
1. JVM searches the method table in class metadata
2. Matches by name AND parameter types (exact match)
3. Returns a `Method` mirror object
4. Throws `NoSuchMethodException` if not found

### Invocation Flow

```
method.invoke(obj, args)
    ↓
setAccessible check → Skip if true
    ↓
Parameter type verification
    ↓
Autoboxing of primitive arguments
    ↓
JNI call to JVM method invocation
    ↓
Return value (autobox if primitive)
    ↓
InvocationTargetException if method threw exception
```

### Why Method.invoke() is Slow

1. **JNI overhead** — Every call crosses the Java/native boundary
2. **Type checking** — Parameter types verified at every call
3. **Boxing/unboxing** — Primitives wrapped in objects
4. **Security checks** — Access control enforced
5. **No JIT inlining** — HotSpot cannot optimize the call site

### MethodHandle: The Fast Alternative

```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle handle = lookup.findVirtual(String.class, "length",
    MethodType.methodType(int.class));
int len = (int) handle.invokeExact("hello"); // Near-direct-call speed
```

MethodHandles are designed for JIT optimization:
- The call site can be monomorphic (single target)
- The JIT can inline the call
- No JNI overhead

### Exception Wrapping

When a reflectively invoked method throws an exception:
1. The JVM wraps it in `InvocationTargetException`
2. The original exception is accessible via `getTargetException()`
3. `IllegalAccessException` is for access control issues
4. `IllegalArgumentException` is for parameter type mismatches
