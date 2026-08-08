# Reflection & Annotations — Reference Cheat Sheet

## Getting Class Objects

```java
Class<?> c1 = String.class;                    // .class literal
Class<?> c2 = "hello".getClass();              // from instance
Class<?> c3 = Class.forName("java.lang.String"); // from string
Class<?> c4 = String.class.getClassLoader().loadClass("java.lang.String");
```

## Class Metadata

```java
clazz.getName()              // "java.lang.String"
clazz.getSimpleName()        // "String"
clazz.getPackage().getName() // "java.lang"
clazz.getModifiers()         // int bitmask
clazz.isInterface()          // boolean
clazz.isArray()              // boolean
clazz.isPrimitive()          // boolean
clazz.getSuperclass()        // Class<?>
clazz.getInterfaces()        // Class<?>[]
```

## Field Access

```java
Field[] fields = clazz.getDeclaredFields();     // All fields (any access)
Field f = clazz.getDeclaredField("name");       // By name
f.setAccessible(true);                          // Bypass private access
Object val = f.get(obj);                        // Read
f.set(obj, value);                              // Write
f.getInt(obj);                                  // Read int directly
f.setInt(obj, 42);                              // Write int directly
f.getType();                                    // Class<?> of field type
f.getModifiers();                               // int bitmask
```

## Method Invocation

```java
Method m = clazz.getDeclaredMethod("name", int.class, String.class);
m.setAccessible(true);
Object result = m.invoke(obj, arg1, arg2);      // Call method
m.getReturnType();                              // Return type Class<?>
m.getParameterTypes();                          // Class<?>[] of params
m.getExceptionTypes();                          // Class<?>[] of exceptions
m.getModifiers();                               // int bitmask
m.isAnnotationPresent(MyAnnotation.class);      // Check annotation
```

## Constructor Access

```java
Constructor<?> ctor = clazz.getDeclaredConstructor(String.class, int.class);
ctor.setAccessible(true);
Object obj = ctor.newInstance("hello", 42);     // Create instance
ctor.getParameterTypes();                       // Class<?>[] of params
ctor.getModifiers();                            // int bitmask
```

## Modifier Checks

```java
Modifier.isPublic(mods)       // public
Modifier.isPrivate(mods)      // private
Modifier.isProtected(mods)    // protected
Modifier.isStatic(mods)       // static
Modifier.isFinal(mods)        // final
Modifier.isAbstract(mods)     // abstract
Modifier.isSynchronized(mods) // synchronized
Modifier.isTransient(mods)    // transient
Modifier.isVolatile(mods)     // volatile
Modifier.toString(mods)       // "private static final"
```

## Dynamic Proxy

```java
Object proxy = Proxy.newProxyInstance(
    target.getClass().getClassLoader(),
    target.getClass().getInterfaces(),
    (p, method, args) -> {
        // Before
        Object result = method.invoke(target, args);
        // After
        return result;
    }
);
```

## Annotation Access

```java
// Check presence
clazz.isAnnotationPresent(MyAnnotation.class);
field.isAnnotationPresent(MyAnnotation.class);
method.isAnnotationPresent(MyAnnotation.class);

// Get annotation
MyAnnotation ann = clazz.getAnnotation(MyAnnotation.class);
ann.value();  // annotation element value

// Repeatable annotations
MyAnnotation[] anns = clazz.getAnnotationsByType(MyAnnotation.class);

// All annotations
Annotation[] all = clazz.getAnnotations();
```

## Custom Annotation Template

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)  // or FIELD, METHOD, etc.
@Documented
@Inherited
public @interface MyAnnotation {
    String value();
    int priority() default 5;
    String[] tags() default {};
}
```

## Common Exceptions

| Exception | When | Fix |
|-----------|------|-----|
| `ClassNotFoundException` | Wrong class name in forName | Verify FQN |
| `NoSuchFieldException` | Field doesn't exist | Check name, use getDeclaredField |
| `NoSuchMethodException` | Method/constructor not found | Check name + param types |
| `IllegalAccessException` | No access to member | Call setAccessible(true) |
| `InstantiationException` | Cannot create instance | Abstract/interface/no default ctor |
| `InvocationTargetException` | Target method threw exception | Unwrap with getTargetException() |
| `InaccessibleObjectException` | Java 9+ module blocks access | Add --add-opens JVM flag |

## Performance Tips

1. Cache Class, Method, Field objects
2. Call setAccessible(true) ONCE outside hot loops
3. Use MethodHandle for near-direct-call performance
4. Prefer compile-time processing over runtime reflection
5. Avoid reflection in tight loops

## JVM Flags for Java 9+ Module Access

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.lang.reflect=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
```
