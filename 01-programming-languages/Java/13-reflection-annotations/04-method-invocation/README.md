# 04 — Method Invocation

## Why Method Invocation Matters

Method invocation via reflection is the heart of dynamic behavior in Java. It lets you call any method on any object at runtime, regardless of what the compiler knows. This is how Spring calls your `@PostConstruct` methods, how JUnit invokes your `@Test` methods, and how serialization frameworks invoke your getters and setters.

---

## Getting Methods

### `getDeclaredMethods()` — This Class Only

```java
class Calculator {
    public int add(int a, int b) { return a + b; }
    private int multiply(int a, int b) { return a * b; }
    protected void validate(int n) { }
    static int parse(String s) { return Integer.parseInt(s); }
}

Method[] methods = Calculator.class.getDeclaredMethods();
for (Method m : methods) {
    System.out.printf("%s %s %s(%s)%n",
        Modifier.toString(m.getModifiers()),
        m.getReturnType().getSimpleName(),
        m.getName(),
        Arrays.toString(m.getParameterTypes()));
}
```

### `getMethods()` — Public Methods Only (Including Inherited)

```java
// Returns all public methods from this class, superclasses, and interfaces
Method[] publicMethods = Calculator.class.getMethods();
```

### Getting a Specific Method

```java
// By name and parameter types
Method addMethod = Calculator.class.getDeclaredMethod("add", int.class, int.class);

// No-arg method
Method noArg = MyClass.class.getDeclaredMethod("doSomething");

// Varargs method
Method varargs = MyClass.class.getDeclaredMethod("process", String[].class);
```

---

## Invoking Methods

### Instance Methods

```java
Calculator calc = new Calculator();

Method addMethod = Calculator.class.getDeclaredMethod("add", int.class, int.class);

// Invoke: first arg is the instance, remaining args are method parameters
int result = (int) addMethod.invoke(calc, 3, 5); // 8

// For methods that return void
Method validateMethod = Calculator.class.getDeclaredMethod("validate", int.class);
validateMethod.setAccessible(true);
validateMethod.invoke(calc, 42); // Returns null for void methods
```

### Static Methods

```java
Method parseMethod = Calculator.class.getDeclaredMethod("parse", String.class);

// First arg is null for static methods
int parsed = (int) parseMethod.invoke(null, "123"); // 123
```

### Private Methods

```java
Method multiplyMethod = Calculator.class.getDeclaredMethod("multiply", int.class, int.class);
multiplyMethod.setAccessible(true);

int product = (int) multiplyMethod.invoke(calc, 3, 5); // 15
```

---

## Method Metadata

```java
Method method = Calculator.class.getDeclaredMethod("add", int.class, int.class);

// Name
String name = method.getName(); // "add"

// Return type
Class<?> returnType = method.getReturnType(); // int.class
String returnTypeName = returnType.getSimpleName(); // "int"

// Parameter types
Class<?>[] paramTypes = method.getParameterTypes(); // [int.class, int.class]
int paramCount = method.getParameterCount(); // 2

// Parameter names (Java 8+ with -parameters flag)
Parameter[] params = method.getParameters();
for (Parameter p : params) {
    System.out.println(p.getName() + " : " + p.getType().getSimpleName());
}

// Modifiers
int mods = method.getModifiers();
boolean isPublic = Modifier.isPublic(mods);
boolean isStatic = Modifier.isStatic(mods);
boolean isAbstract = Modifier.isAbstract(mods);
boolean isSynchronized = Modifier.isSynchronized(mods);

// Declaring class
Class<?> declaringClass = method.getDeclaringClass();

// Exception types
Class<?>[] exceptions = method.getExceptionTypes();
```

---

## Generic Method Types

```java
public class Repository<T> {
    public T findById(long id) { return null; }
    public <E extends T> List<E> findByExample(E example) { return null; }
}

Method findById = Repository.class.getDeclaredMethod("findById", long.class);
Type genericReturn = findById.getGenericReturnType();
if (genericReturn instanceof TypeVariable) {
    TypeVariable<?> tv = (TypeVariable<?>) genericReturn;
    System.out.println("Type var name: " + tv.getName()); // "T"
    Type[] bounds = tv.getBounds();
}

Method findByExample = Repository.class.getDeclaredMethod("findByExample", Object.class);
Type genericReturn2 = findByExample.getGenericReturnType();
// Returns ParameterizedType: List<E>
```

---

## Annotations on Methods

```java
public class UserService {
    @Transactional
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUsers() { return null; }
    
    @PostConstruct
    public void init() { }
    
    @Deprecated
    public void oldMethod() { }
}

Method getUsers = UserService.class.getDeclaredMethod("getUsers");

// Check annotation presence
boolean isTransactional = getUsers.isAnnotationPresent(Transactional.class);
boolean isRequestMapping = getUsers.isAnnotationPresent(RequestMapping.class);

// Get annotation values
RequestMapping mapping = getUsers.getAnnotation(RequestMapping.class);
String[] value = mapping.value(); // ["/users"]
RequestMethod[] method = mapping.method(); // [GET]
```

---

## Handling Exceptions

Method invocation wraps checked exceptions in `InvocationTargetException`:

```java
public class RiskyService {
    public void riskyMethod() throws IOException {
        throw new IOException("File not found");
    }
}

Method method = RiskyService.class.getDeclaredMethod("riskyMethod");
RiskyService service = new RiskyService();

try {
    method.invoke(service);
} catch (InvocationTargetException e) {
    // The actual exception thrown by the method
    Throwable cause = e.getTargetException();
    
    if (cause instanceof IOException) {
        IOException ioEx = (IOException) cause;
        System.out.println("IO error: " + ioEx.getMessage());
    }
} catch (IllegalAccessException e) {
    // Cannot access the method
    e.printStackTrace();
}
```

### Exception Handling Best Practices

```java
public static Object invokeMethod(Object obj, Method method, Object... args) 
        throws Exception {
    try {
        return method.invoke(obj, args);
    } catch (InvocationTargetException e) {
        // Unwrap the actual exception
        Throwable targetException = e.getTargetException();
        
        // Re-throw as the actual exception type
        if (targetException instanceof RuntimeException) {
            throw (RuntimeException) targetException;
        } else if (targetException instanceof Exception) {
            throw (Exception) targetException;
        } else {
            throw new RuntimeException("Unexpected throwable", targetException);
        }
    }
}
```

---

## Varargs Methods

```java
public class StringJoiner {
    public String join(String delimiter, String... items) {
        return String.join(delimiter, items);
    }
}

Method joinMethod = StringJoiner.class.getDeclaredMethod("join", 
    String.class, String[].class);

StringJoiner joiner = new StringJoiner();

// Method 1: Pass array directly
String result1 = (String) joinMethod.invoke(joiner, ",", new String[]{"a", "b", "c"});

// Method 2: Pass varargs (Java automatically wraps in array)
// Note: Some JVMs require explicit array wrapping for reflective calls
String result2 = (String) joinMethod.invoke(joiner, ",", "a", "b", "c");
```

---

## Method Overloading Resolution

```java
public class Overloaded {
    public void process(int x) { }
    public void process(String x) { }
    public void process(int x, String y) { }
}

// Must specify exact parameter types to disambiguate
Method m1 = Overloaded.class.getDeclaredMethod("process", int.class);
Method m2 = Overloaded.class.getDeclaredMethod("process", String.class);
Method m3 = Overloaded.class.getDeclaredMethod("process", int.class, String.class);
```

---

## Complete Example: Method Dispatcher

```java
import java.lang.reflect.*;
import java.util.*;

public class MethodDispatcher {

    private final Object target;
    private final Map<String, Method> methodCache;

    public MethodDispatcher(Object target) {
        this.target = target;
        this.methodCache = new HashMap<>();
        cacheMethods();
    }

    private void cacheMethods() {
        for (Method method : target.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            methodCache.put(method.getName(), method);
        }
    }

    public Object dispatch(String methodName, Object... args) 
            throws Exception {
        Method method = methodCache.get(methodName);
        if (method == null) {
            throw new NoSuchMethodException("No method: " + methodName);
        }

        // Verify argument count
        if (args.length != method.getParameterCount()) {
            throw new IllegalArgumentException(
                "Expected " + method.getParameterCount() + " args, got " + args.length);
        }

        // Type check arguments
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && !paramTypes[i].isInstance(args[i])) {
                throw new ClassCastException(
                    "Arg " + i + " expected " + paramTypes[i].getSimpleName());
            }
        }

        return method.invoke(target, args);
    }

    public List<String> getAvailableMethods() {
        return new ArrayList<>(methodCache.keySet());
    }
}
```

---

## Production Incident: InvocationTargetException Swallowed

**Incident:** A microservice framework used reflection to call user-defined handlers. When a handler threw an exception, the framework caught `InvocationTargetException` but logged only the wrapper message, not the root cause. Users saw "null" in error logs.

**Root cause:** The framework logged `e.getMessage()` on `InvocationTargetException`, which delegates to the wrapper message (often null), instead of `e.getTargetException().getMessage()`.

**Fix:** Always unwrap `InvocationTargetException` to get the actual exception:

```java
} catch (InvocationTargetException e) {
    Throwable actual = e.getTargetException();
    logger.error("Handler failed: " + actual.getMessage(), actual);
}
```

**Lesson:** `InvocationTargetException` is a wrapper. Always extract the target exception.

---

## Performance Considerations

```java
// BAD: Lookup method every time
for (int i = 0; i < 1000000; i++) {
    Method m = clazz.getDeclaredMethod("process", String.class);
    m.invoke(obj, "data");
}

// GOOD: Cache method
Method m = clazz.getDeclaredMethod("process", String.class);
m.setAccessible(true);
for (int i = 0; i < 1000000; i++) {
    m.invoke(obj, "data");
}

// BETTER: Use MethodHandle (Java 7+) for near-direct-call performance
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle handle = lookup.findVirtual(clazz, "process", 
    MethodType.methodType(void.class, String.class));
for (int i = 0; i < 1000000; i++) {
    handle.invoke(obj, "data");
}
```

---

## Code Review Checklist

- [ ] Is the method cached (not looked up per invocation)?
- [ ] Is `setAccessible(true)` called before the hot loop, not inside it?
- [ ] Is `InvocationTargetException` properly unwrapped?
- [ ] Are parameter types specified exactly (for overloaded methods)?
- [ ] Is varargs handling correct (explicit array wrapping)?
- [ ] Are void method invocations handled (return null)?
- [ ] Is the method's declaring class understood (inherited vs own)?

---

## Security Considerations

| Risk | Description | Mitigation |
|------|-------------|-----------|
| Arbitrary code execution | Calling any method on any object | Restrict method invocation to trusted code |
| Bypassing access control | Invoking private methods | Document why `setAccessible` is needed |
| Deserialization attacks | Invoking `Runtime.exec()` via reflection | Whitelist allowed classes/methods |

---

## Debugging Tips

1. **Use `method.toString()`** — Shows full method signature
2. **Print `Arrays.toString(method.getParameterTypes())`** — Verify parameter types match
3. **Check `method.getReturnType()`** — Know what to expect from `invoke()`
4. **Unwrap `InvocationTargetException`** — The real exception is in `getTargetException()`
5. **Verify argument count** — `method.getParameterCount()` vs actual args

---

## Interview Questions

1. **What does `method.invoke(obj, args)` return for a void method?**
   - `null`

2. **How do you handle exceptions thrown by a reflectively invoked method?**
   - Catch `InvocationTargetException` and unwrap with `getTargetException()`

3. **What's the difference between `getMethods()` and `getDeclaredMethods()`?**
   - `getMethods()`: Public methods including inherited
   - `getDeclaredMethods()`: All methods declared in this class (any access)

4. **How do you invoke a static method via reflection?**
   - Pass `null` as the first argument: `method.invoke(null, args)`

5. **How do you resolve overloaded methods?**
   - Specify exact parameter types: `getDeclaredMethod("name", int.class, String.class)`

---

## Summary

| Operation | Method | Notes |
|-----------|--------|-------|
| Get all methods | `getDeclaredMethods()` | All access levels |
| Get public methods | `getMethods()` | Including inherited |
| Get specific method | `getDeclaredMethod(name, types...)` | Exact parameter types |
| Invoke instance | `method.invoke(obj, args...)` | First arg is instance |
| Invoke static | `method.invoke(null, args...)` | First arg is null |
| Invoke private | `method.setAccessible(true)` | Then invoke |
| Return type | `method.getReturnType()` | `void.class` for void |
| Parameter types | `method.getParameterTypes()` | Array of `Class<?>` |
| Handle exceptions | Catch `InvocationTargetException` | Unwrap with `getTargetException()` |

---

*Next: [05 — Constructor Access](../05-constructor-access/README.md)*
