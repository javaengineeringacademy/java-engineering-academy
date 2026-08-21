# Internals: Constructor Access

## How Constructor.newInstance() Works

### Constructor Resolution

When you call `getDeclaredConstructor(paramTypes)`:
1. JVM searches the constructor table in class metadata
2. Matches by parameter types (exact match required)
3. Returns a `Constructor` mirror object
4. Throws `NoSuchMethodException` if not found

### Instantiation Flow

```
constructor.newInstance(args)
    ↓
setAccessible check → Skip if true
    ↓
Parameter type verification
    ↓
Autoboxing of primitive arguments
    ↓
Allocate object memory (uninitialized)
    ↓
Run constructor bytecode
    ↓
Return initialized object
    ↓
InvocationTargetException if constructor threw exception
```

### Object Allocation

The JVM allocates memory for the object before running the constructor:
1. Calculate object size from field layout
2. Allocate in TLAB (Thread-Local Allocation Buffer) if small
3. Initialize all fields to default values (0, null, false)
4. Run constructor body

### Exception Wrapping

Constructor exceptions are wrapped in `InvocationTargetException`:
- `InstantiationException` — Class is abstract, interface, or array
- `IllegalAccessException` — No access to constructor
- `InvocationTargetException` — Constructor threw an exception

### Inner Class Construction

Inner class constructors have an implicit first parameter for the enclosing instance:

```java
// Source: class Outer { class Inner { Inner(int x) {} } }
// Bytecode constructor: Inner(Outer, int)
```

When reflecting, you must pass the outer instance as the first argument.
