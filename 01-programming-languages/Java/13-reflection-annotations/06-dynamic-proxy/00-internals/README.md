# Internals: Dynamic Proxy

## How JDK Dynamic Proxy Works

### Proxy Class Generation

When you call `Proxy.newProxyInstance()`:

1. JVM generates a new class that implements the specified interfaces
2. The generated class has a field for the `InvocationHandler`
3. Every method from the interfaces is implemented to call `handler.invoke()`
4. The class is loaded by the specified ClassLoader

```
Interface A, Interface B
        |
Proxy.newProxyInstance(loader, [A, B], handler)
        |
Generated class (A$B$Proxy0) implements A, B {
    InvocationHandler h;
    
    Object methodA(args) { return h.invoke(this, methodA, args); }
    Object methodB(args) { return h.invoke(this, methodB, args); }
}
```

### InvocationHandler Flow

```
proxy.method(args)
    ↓
Generated proxy class method
    ↓
handler.invoke(proxy, method, args)
    ↓
Your custom logic
    ↓
method.invoke(target, args) if needed
    ↓
Return result
```

### Method Resolution

The proxy class resolves methods from:
1. All specified interfaces (in order)
2. Object methods (toString, equals, hashCode) if not overridden

### Memory Layout of Proxy

Each proxy instance contains:
- Object header: 16 bytes
- InvocationHandler reference: 8 bytes
- Interface method tables: variable

The generated proxy class contains:
- Method stubs for each interface method
- InvocationHandler field
- Static method dispatch table

## CGLIB Proxy

CGLIB creates a subclass of the target class:
1. Generates a new class that extends the target
2. Overrides non-final methods
3. Intercepts calls via a MethodInterceptor
4. Can proxy concrete classes without interfaces

```
TargetClass
    |
CGLIB generates TargetClass$$EnhancerByCGLIB
    |
    extends TargetClass
    implements MethodInterceptor
```
