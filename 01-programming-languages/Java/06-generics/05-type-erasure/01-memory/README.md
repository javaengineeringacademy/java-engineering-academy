# Type Erasure Memory Layout

Understanding how generics affect memory and runtime behavior.

## Memory Layout

### Before Erasure (Compile Time)
```
Box<String> box = new Box<>("Hello");
Box<Integer> intBox = new Box<>(42);
```

### After Erasure (Runtime)
```
Box box = new Box("Hello");
Box intBox = new Box(42);
```

## What Gets Erased

1. **Type Parameters** - T becomes Object or bound
2. **Type Arguments** - `<String>` is removed
3. **Cast Operations** - Added automatically where needed
4. **Bridge Methods** - Generated for polymorphism

## What Remains

1. **Raw Type** - The class name without type parameters
2. **Bounded Type** - The bound replaces the type parameter
3. **Generic Signatures** - In class file for reflection
4. **Type Annotations** - If retention policy is runtime

## Memory Implications

- No additional memory for type parameters
- Same bytecode for all parameterized types
- Casting instructions added by compiler
- Bridge methods add slight overhead

## Type Information at Runtime

```java
// These are the same at runtime:
Box<String>.class == Box<Integer>.class  // true

// But type info is available via reflection:
TypeVariable<?>[] params = Box.class.getTypeParameters();
```

## Key Points

1. Generics are purely compile-time
2. No performance overhead from generics
3. Type safety is enforced by compiler
4. Raw types should be avoided
5. Use bounded wildcards for flexibility
