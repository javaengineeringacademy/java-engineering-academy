# 02 - ClassLoader Deep Dive

## Introduction

The ClassLoader subsystem is the foundation of Java's dynamic class loading mechanism. It reads `.class` files from disk, network, or generated at runtime, and converts them into `java.lang.Class` objects in the method area. Understanding classloaders is essential for debugging `ClassNotFoundException`, implementing plugin architectures, and managing long-running applications.

## Classloading Phases

The class loading process follows these phases:

```
Loading → Linking → Initialization
           │
           ├─ Verification (bytecode validation)
           ├─ Preparation (allocate static field memory)
           └─ Resolve (replace symbolic refs)
```

### 1. Loading Phase
- Find the `.class` bytes (from file, network, byte array, etc.)
- Define the class: Convert bytes to `java.lang.Class` instance
- Resolve: Link to parent classloader's classes

### 2. Linking Phase

#### Verification
- Magic number: 0xCAFEBABE
- Version compatibility
- Constant pool validity
- Stack map frame verification
- Bytecode integrity

#### Preparation
- Allocate memory for static fields
- Assign default values (0, null, false)
- Assign constant values (static final fields)

#### Resolution
- Replace symbolic references with direct references
- Class references → Class object
- Field references → memory offset
- Method references → entry point

### 3. Initialization Phase
- Execute `<clinit>()` static initializers
- Run static blocks in order

## ClassLoader Hierarchy

```
Bootstrap ClassLoader (C/C++, null reference)
├── Loads: rt.jar, modules (java.base, java.xml, etc.)
├── Source: $JAVA_HOME/lib
└── Purpose: Core Java classes

Platform ClassLoader (Java, non-null)
├── Loads: java.* (non-core), javax.* modules
├── Source: $JAVA_HOME/lib/ext or module path
└── Purpose: Extension/platform classes

Application ClassLoader (Java, non-null)
├── Loads: Classpath classes (your code)
├── Source: -classpath/-cp
└── Purpose: Application classes
```

## Parent Delegation Model

```
Application ClassLoader
    │
    ▼
Platform ClassLoader
    │
    ▼
Bootstrap ClassLoader
    │
    ▼
null (native code)
```

**Process:**
1. Request goes to Application ClassLoader
2. Application delegates to Platform ClassLoader
3. Platform delegates to Bootstrap ClassLoader
4. Bootstrap attempts to load; if not found, delegates back
5. Each level tries to load; if not found, child tries

**Why parent-first?**
- Prevents loading core classes from untrusted sources
- Ensures single definition of core classes
- Avoids class conflicts between classloaders

## Custom Classloaders

### Custom ClassLoader Template

```java
public class CustomClassLoader extends ClassLoader {
    
    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String path = name.replace('.', '/') + ".class";
            byte[] bytes = loadClassBytes(path);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }
    
    private byte[] loadClassBytes(String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }
}
```

### Custom ClassLoader Use Cases

- Plugin architectures (OSGi, Spring Boot DevTools)
- Hot deployment/reloading
- Class isolation (web containers)
- Encrypted class files
- Loading from database/network

## ClassLoader Leaks

### Common Leak Sources

1. **ThreadLocal values not removed**
   - ThreadLocal.set() holds reference to classloader
   - Fix: Use InheritableThreadLocal or remove in finally

2. **JDBC drivers not deregistered**
   - DriverManager.registerDriver() holds static reference
   - Fix: Call DriverManager.deregisterDriver() in close()

3. **JNDI bindings not unbound**
   - InitialContext.bind() holds reference
   - Fix: Call unbind() in cleanup

4. **Static fields holding classloader references**
   - Static collections prevent GC
   - Use WeakReference for caches

5. **RMI/Remote objects not unexported**
   - UnicastRemoteObject.exportObject() leaks
   - Fix: UnicastRemoteObject.unexportObject()

### ClassLoader Leak Detection

```bash
# Monitor loaded classes
jcmd <pid> VM.classloader_stats

# ClassLoader monitoring
ManagementFactory.getClassLoadingMXBean().getLoadedClassCount()
ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount()
ManagementFactory.getClassLoadingMXBean().getUnloadedClassCount()
```

## Class Identity

A class is uniquely identified by:
- **Fully Qualified Name**: `java.lang.String`
- **Defining ClassLoader**: Two classloaders can load same class name independently

```java
Class<?> c1 = classLoader1.loadClass("com.example.MyClass");
Class<?> c2 = classLoader2.loadClass("com.example.MyClass");
c1 != c2  // Different class objects
```

## Class Unloading

- Classes can be unloaded when their defining classloader is garbage collected
- Bootstrap and Platform classloaders are never collected
- Application classloader: classes unload on JVM shutdown or if classloader is nulled
- Custom classloaders: unload when no references remain

## Best Practices

1. **Always Set Parent**: `super(parent)` in custom classloader constructors
2. **Cache Loaded Classes**: Use `findLoadedClass()` before loading from source
3. **Implement close()**: Clean up resources when classloader is no longer needed
4. **Use ProtectionDomain**: Set code source and permissions for loaded classes
5. **Monitor ClassLoader Count**: Track for potential leaks in long-running apps

## Interview Questions

1. **What is the parent delegation model?** - Request goes up the hierarchy before child tries to load
2. **What is the difference between ClassNotFoundException and NoClassDefFoundError?** - CNF thrown during load, NCDFD thrown when class was found but can't be loaded
3. **How many classloaders are there in a standard JVM?** - Three: Bootstrap, Platform, Application
4. **What is the Thread Context ClassLoader?** - A classloader associated with each thread, used to break parent delegation
5. **Can a class be unloaded from the JVM?** - Yes, when its defining classloader is garbage collected

## Debugging Tips

```bash
# Verbose class loading
java -verbose:class MyApp

# Trace class loading
java -verbose:class -XX:+TraceClassLoading MyApp

# ClassLoader monitoring
jcmd <pid> VM.classloader_stats
```

## References

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations

## References

- [Java ClassLoader Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java/lang/ClassLoader.html)
- "Inside the Java Virtual Machine" by Bill Venners
- "Optimizing Java" by Benjamin J. Evans

## Production Checklist

### ✅ Before using ClassLoader in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## Common Myths

### ❌ Myth 1: Classes are loaded when imported
**Reality:** Loaded on first use. Classes are loaded lazily when first referenced.

### ❌ Myth 2: ClassLoader is parent-first
**Reality:** Can be child-first. Custom classloaders can override parent delegation.

### ❌ Myth 3: Classes are loaded once
**Reality:** Can be unloaded. Classes can be garbage collected when their classloader is collected.
