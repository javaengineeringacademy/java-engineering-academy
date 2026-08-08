# 02 — Class Introspection

## Why Class Introspection Matters

Every reflective operation starts with a `Class` object. Before you can access fields, invoke methods, or create instances, you need to *find* the class and *understand* its structure. Class introspection is the foundation — if you get this wrong, everything downstream fails.

This topic covers all the ways to obtain `Class` objects, query their metadata, and navigate the class hierarchy.

---

## Obtaining Class Objects — Deep Dive

### Method 1: `.class` Literal

```java
// Compile-time known type
Class<String> stringClass = String.class;
Class<int[]> intArrayClass = int[].class;
Class<Void> voidClass = Void.class;

// For interfaces
Class<Runnable> runnableClass = Runnable.class;

// For primitive types
Class<int> intClass = int.class;
Class<double> doubleClass = double.class;
```

**Advantages:**
- Type-safe — compiler verifies the class exists
- No exceptions thrown
- Fastest — no name lookup required

**When to use:** You know the exact type at compile time.

### Method 2: `getClass()` on an Instance

```java
String s = "hello";
Class<?> clazz = s.getClass(); // returns java.lang.String

Integer num = 42;
Class<?> numClass = num.getClass(); // returns java.lang.Integer

// Works with polymorphism
Object obj = getSomeObject();
Class<?> runtimeClass = obj.getClass(); // actual runtime type
```

**Advantages:**
- Gets the *runtime* type, not the declared type
- Useful for polymorphic code

**When to use:** You have an instance and need its actual runtime type.

### Method 3: `Class.forName()`

```java
// Fully-qualified class name as a string
Class<?> clazz = Class.forName("java.lang.String");

// From configuration or user input
String className = config.getProperty("service.class");
Class<?> serviceClass = Class.forName(className);

// With classloader
Class<?> clazz = Class.forName("com.example.MyClass", true, 
    Thread.currentThread().getContextClassLoader());
```

**Advantages:**
- Fully dynamic — class name can come from anywhere
- Essential for plugin architectures

**When to use:** The class name is not known until runtime (config files, user input, databases).

**Disadvantages:**
- Throws `ClassNotFoundException`
- No compile-time type checking
- String-based — typos become runtime errors

### Method 4: `getClassLoader().loadClass()`

```java
ClassLoader loader = Thread.currentThread().getContextClassLoader();
Class<?> clazz = loader.loadClass("com.example.MyClass");
```

**When to use:** When you need control over which classloader loads the class.

---

## Class Metadata Methods

Once you have a `Class` object, you can query its metadata:

### Name and Package

```java
Class<?> clazz = java.util.ArrayList.class;

// Full name with package
String fullName = clazz.getName();        // "java.util.ArrayList"

// Simple name (without package)
String simpleName = clazz.getSimpleName(); // "ArrayList"

// Canonical name (for arrays and inner classes)
String canonicalName = clazz.getCanonicalName(); // "java.util.ArrayList"

// Package
Package pkg = clazz.getPackage();
String packageName = pkg.getName(); // "java.util"

// Module (Java 9+)
Module module = clazz.getModule();
String moduleName = module.getName(); // "java.base"
```

### Type Information

```java
Class<?> clazz = ArrayList.class;

boolean isArray = clazz.isArray();           // false
boolean isPrimitive = clazz.isPrimitive();   // false
boolean isInterface = clazz.isInterface();   // false
boolean isEnum = clazz.isEnum();             // false
boolean isAnnotation = clazz.isAnnotation(); // false
boolean isSynthetic = clazz.isSynthetic();   // false
boolean isAnonymousClass = clazz.isAnonymousClass(); // false
boolean isLocalClass = clazz.isLocalClass(); // false
boolean isMemberClass = clazz.isMemberClass(); // false

// For arrays
Class<?> arrClass = int[].class;
Class<?> componentType = arrClass.getComponentType(); // int.class
```

### Modifiers

```java
Class<?> clazz = java.lang.String.class;

int mods = clazz.getModifiers();
boolean isPublic = Modifier.isPublic(mods);     // true
boolean isFinal = Modifier.isFinal(mods);       // true (String is final)
boolean isAbstract = Modifier.isAbstract(mods); // false
boolean isStatic = Modifier.isStatic(mods);     // false (top-level class)

// For fields and methods
Field field = clazz.getDeclaredField("hash");
int fieldMods = field.getModifiers();
System.out.println(Modifier.toString(fieldMods)); // "private transient final"
```

---

## Class Hierarchy Navigation

### Superclass Chain

```java
Class<?> clazz = javax.swing.JButton.class;

// Walk up the hierarchy
Class<?> current = clazz;
while (current != null) {
    System.out.println(current.getSimpleName());
    current = current.getSuperclass();
}
// Output: JButton → JComponent → Container → Component → Object → null
```

### Interfaces

```java
Class<?> clazz = ArrayList.class;

// All implemented interfaces
Class<?>[] interfaces = clazz.getInterfaces();
for (Class<?> iface : interfaces) {
    System.out.println(iface.getSimpleName());
}
// Output: List, RandomAccess, Cloneable, Serializable

// All public interfaces (including inherited)
// Note: getInterfaces() only returns direct interfaces
// For full hierarchy, use getGenericInterfaces()
Type[] genericInterfaces = clazz.getGenericInterfaces();
```

### Checking Relationships

```java
Class<?> parent = Animal.class;
Class<?> child = Dog.class;

// Is Dog a subclass of Animal?
boolean isAssignable = parent.isAssignableFrom(child); // true

// Is ArrayList a List?
boolean isList = List.class.isAssignableFrom(ArrayList.class); // true

// Is String an Object?
boolean isObject = Object.class.isAssignableFrom(String.class); // true
```

---

## Type Parameters and Generics

Generics are erased at runtime, but reflection can still access generic type information through the bytecode:

```java
// Generic type info is preserved in field/method signatures
Field field = MyClass.class.getDeclaredField("items");
Type genericType = field.getGenericType();

if (genericType instanceof ParameterizedType) {
    ParameterizedType pt = (ParameterizedType) genericType;
    Type[] typeArgs = pt.getActualTypeArguments();
    for (Type typeArg : typeArgs) {
        System.out.println("Type argument: " + typeArg);
    }
}

// For method parameters
Method method = MyClass.class.getDeclaredMethod("process", List.class);
Type[] paramTypes = method.getGenericParameterTypes();
```

### Superclass with Generics

```java
// Useful for discovering type parameters in generic superclasses
public class StringRepository extends BaseRepository<String> {
    // ...
}

Class<?> clazz = StringRepository.class;
Type genericSuper = clazz.getGenericSuperclass();

if (genericSuper instanceof ParameterizedType) {
    ParameterizedType pt = (ParameterizedType) genericSuper;
    Type[] typeArgs = pt.getActualTypeArguments();
    System.out.println("Repository type: " + typeArgs[0]); // String
}
```

---

## Inner Classes and Nested Types

```java
public class Outer {
    private int x;
    
    public class Inner {
        public void doSomething() { }
    }
    
    public static class StaticNested {
        public void doSomething() { }
    }
    
    private void privateMethod() { }
}

// Get all declared classes (inner, nested, local, anonymous)
Class<?>[] innerClasses = Outer.class.getDeclaredClasses();
for (Class<?> inner : innerClasses) {
    System.out.println(inner.getSimpleName() + " : " + 
        Modifier.toString(inner.getModifiers()));
}
// Output: Inner : public, StaticNested : public static

// Get the enclosing class
Class<?> innerClass = Outer.Inner.class;
Class<?> enclosing = innerClass.getEnclosingClass(); // Outer.class
```

---

## Annotations on Classes

```java
@Entity
@Table(name = "users")
public class User {
    // ...
}

Class<?> clazz = User.class;

// Check if annotation is present
boolean hasEntity = clazz.isAnnotationPresent(Entity.class); // true
boolean hasTable = clazz.isAnnotationPresent(Table.class);    // true

// Get annotation instance
Table table = clazz.getAnnotation(Table.class);
String tableName = table.name(); // "users"

// Get all annotations
Annotation[] annotations = clazz.getAnnotations();
for (Annotation ann : annotations) {
    System.out.println(ann.annotationType().getSimpleName());
}
```

---

## Complete Example: Class Inspector

```java
import java.lang.reflect.*;
import java.util.*;

public class ClassInspector {

    public static void inspect(Class<?> clazz) {
        System.out.println("=== Class: " + clazz.getName() + " ===");
        System.out.println("Simple Name: " + clazz.getSimpleName());
        System.out.println("Package: " + clazz.getPackage().getName());
        System.out.println("Modifiers: " + Modifier.toString(clazz.getModifiers()));
        System.out.println("Superclass: " + 
            (clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "none"));
        
        // Interfaces
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.println("\nInterfaces:");
            for (Class<?> iface : interfaces) {
                System.out.println("  - " + iface.getName());
            }
        }
        
        // Fields
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            System.out.println("\nFields:");
            for (Field f : fields) {
                System.out.printf("  %s %s %s%n",
                    Modifier.toString(f.getModifiers()),
                    f.getType().getSimpleName(),
                    f.getName());
            }
        }
        
        // Methods
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            System.out.println("\nMethods:");
            for (Method m : methods) {
                System.out.printf("  %s %s %s(%s)%n",
                    Modifier.toString(m.getModifiers()),
                    m.getReturnType().getSimpleName(),
                    m.getName(),
                    Arrays.toString(m.getParameterTypes()));
            }
        }
        
        // Constructors
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length > 0) {
            System.out.println("\nConstructors:");
            for (Constructor<?> c : constructors) {
                System.out.printf("  %s %s(%s)%n",
                    Modifier.toString(c.getModifiers()),
                    clazz.getSimpleName(),
                    Arrays.toString(c.getParameterTypes()));
            }
        }
    }
}
```

---

## Production Incident: ClassNotFoundException in Production

**Incident:** A SaaS application loaded user-defined classes dynamically. After a deployment, customers reported `ClassNotFoundException` for classes that existed in the previous version.

**Root cause:** The classloader was cached from the old deployment. When the application was redeployed without restarting the JVM, the old classloader still held references to the old classes, and `Class.forName()` resolved to the old classloader.

**Fix:** Implement a classloader leak detection mechanism. On each deployment, explicitly null out references to old classloaders and trigger garbage collection.

**Lesson:** Classloader management is critical for long-running applications that hot-reload classes.

---

## Code Review Checklist

- [ ] Is `Class.forName()` used with validated input?
- [ ] Are class names fully-qualified and not hardcoded strings?
- [ ] Is the classloader specified when dynamic loading is needed?
- [ ] Are generic type parameters accessible where needed?
- [ ] Is the class hierarchy navigated correctly (checking superclass chain)?
- [ ] Are inner class relationships understood (enclosing class vs nested)?

---

## Security Considerations

```java
// DANGEROUS: Loading arbitrary class names
String userInput = request.getParameter("className");
Class<?> clazz = Class.forName(userInput); // Could load anything!

// SAFER: Whitelist approach
Set<String> allowed = Set.of("com.example.User", "com.example.Order");
if (allowed.contains(userInput)) {
    Class<?> clazz = Class.forName(userInput);
} else {
    throw new SecurityException("Class not allowed: " + userInput);
}
```

**Best Practices:**
1. Validate class names against a whitelist
2. Use `Thread.currentThread().getContextClassLoader()` instead of `ClassLoader.getSystemClassLoader()`
3. Never load classes from untrusted sources
4. Consider Java module system restrictions (Java 9+)

---

## Debugging Tips

1. **Print `Class.toString()`** — Shows class name and hashcode
2. **Use `Modifier.toString(modifiers)`** — Human-readable modifier strings
3. **Check `clazz.getSuperclass()` != null** — Distinguish `Object` from other classes
4. **Use `isAssignableFrom()`** — More reliable than `instanceof` for type checking
5. **Inspect generic types** — Use `getGenericSuperclass()` and `getGenericInterfaces()`

---

## Interview Questions

1. **What's the difference between `getName()`, `getSimpleName()`, and `getCanonicalName()`?**
   - `getName()`: Fully-qualified with package, internal format for arrays/proxies
   - `getSimpleName()`: Just the class name, no package
   - `getCanonicalName()`: Human-readable, null for local/anonymous classes

2. **When would you use `Class.forName()` vs `.class`?**
   - `Class.forName()`: When class name is dynamic (from config, user input)
   - `.class`: When you know the exact type at compile time

3. **How do you get the class of a primitive type?**
   - `int.class`, `boolean.class`, etc. — these return the `Class` object for the primitive

4. **What's the difference between `getInterfaces()` and `getGenericInterfaces()`?**
   - `getInterfaces()`: Returns only direct interfaces (Class objects)
   - `getGenericInterfaces()`: Returns Type objects, preserving generic info

5. **How does `isAssignableFrom()` differ from `instanceof`?**
   - `isAssignableFrom()` is called on the parent: `Parent.class.isAssignableFrom(child)`
   - `instanceof` is called on the instance: `child instanceof Parent`

---

## Summary

| Method | Returns | When to Use |
|--------|---------|-------------|
| `String.class` | `Class<String>` | Compile-time known type |
| `obj.getClass()` | `Class<?>` | Runtime type of an instance |
| `Class.forName(name)` | `Class<?>` | Dynamic class name |
| `getClassLoader().loadClass(name)` | `Class<?>` | Custom classloader needed |
| `clazz.getName()` | Full qualified name | Logging, serialization |
| `clazz.getSimpleName()` | Simple name | Display, error messages |
| `clazz.getModifiers()` | int (bitmask) | Access control checks |
| `clazz.getSuperclass()` | `Class<?>` | Hierarchy navigation |
| `clazz.getInterfaces()` | `Class<?>[]` | Interface discovery |

---

*Next: [03 — Field Access](../03-field-access/README.md)*
