# Class Loading in Java

## Class Loading Phases

| Phase | Action | Key Detail |
|-------|--------|------------|
| Loading | Find bytecode, create Class object | Via classloader hierarchy |
| Linking: Verify | Check bytecode format and integrity | Prevents malicious code |
| Linking: Prepare | Allocate memory for static fields | Set to zero values |
| Linking: Resolve | Replace symbolic refs with direct refs | Lazy resolution |
| Initialization | Execute static initializers (clinit) | Thread-safe, runs once |

## Classloader Delegation Model

```
Bootstrap ClassLoader (null)
  └─ loads: java.lang.*, java.util.*, etc.

Platform/Extension ClassLoader
  └─ loads: JAVA_HOME/lib/ext, javax.*

Application ClassLoader
  └─ loads: classpath entries

Custom ClassLoader
  └─ loads: user-defined sources
```

**Parent-first delegation**: Each loader asks parent to load first. Only if parent fails does the child attempt loading.

### Why Delegation Matters
- Prevents loading core classes from untrusted sources
- Ensures single definition of core classes
- Enables classloader isolation for plugins

## ClassNotFoundException vs NoClassDefFoundError

| Exception | Phase | Cause | Type |
|-----------|-------|-------|------|
| ClassNotFoundException | Loading | Class not found on classpath | Checked |
| NoClassDefFoundError | Linking | Class found but dependency missing | Error (unchecked) |

### ClassNotFoundException
- Class.forName("com.missing.Class")
- ClassLoader.loadClass("com.missing.Class")
- Fix: check classpath, verify jar contents

### NoClassDefFoundError
- Class found but failed to link
- Missing dependent class or failed static init
- Fix: add missing dependency, check transitive deps

## Custom ClassLoader

```java
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = loadFromCustomSource(name);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
```

### Use Cases
- Hot deployment: reload classes without restart
- Plugin systems: isolated class loading
- Loading from database or network
- Encrypted bytecode (license protection)
- OSGi module isolation

## Common Issues

### Issue 1: ClassCastException with Same Class
Different classloaders load same class. Two `Class` objects are not equal.
**Fix**: Use same classloader or shared parent.

### Issue 2: LinkageError
Class loaded by multiple classloaders creates conflict.
**Fix**: Ensure single classloader path for each class.

### Issue 3: Metaspace Growth
Custom classloaders not unloaded, classes accumulate.
**Fix**: Ensure classloader has no strong references after use.

### Issue 4: ClassNotFoundException in WAR
Classes in WEB-INF/classes not visible to shared libraries.
**Fix**: Use thread context classloader properly.

## Tools

```bash
# Verbose class loading
-XX:+TraceClassLoading -XX:+TraceClassUnloading

# JVM flag to log
-Xlog:class+load=info

# List loaded classes
jcmd <pid> VM.classloader_stats

# Find which classloader loaded a class
System.out.println(MyClass.class.getClassLoader());
```

## Overview

Class loading is the JVM process of finding, verifying, and initializing `.class` bytecode files into runtime `Class` objects. The classloader subsystem uses a delegation model (parent-first) where each loader asks its parent to load before attempting itself. This ensures core JDK classes are never replaced by user code. Understanding class loading is essential for plugin systems, hot deployment, debugging `ClassNotFoundException`, and application server classloader isolation.

## Why This Concept Exists

Class loading exists because Java programs are compiled to platform-independent bytecode, not native code. The JVM loads classes on demand (lazy loading), verifying each class's bytecode integrity before execution. The delegation model prevents security vulnerabilities (untrusted code replacing `java.lang.String`) and ensures type consistency (one `Class` object per fully-qualified name per classloader). Custom classloaders enable plugin architectures, hot deployment, and loading classes from non-standard sources (databases, networks).

## Internal Working

### Class Loading Phases in Detail

```java
// Phase 1: LOADING
// ClassLoader.findClass() reads bytecode from source
// Creates java.lang.Class object in Metaspace

// Phase 2: LINKING

// 2a: VERIFICATION
// BytecodeVerifier checks:
// - Magic number (0xCAFEBABE)
// - Version compatibility
// - Constant pool validity
// - Stack integrity (no underflow/overflow)
// - Type safety (no invalid casts)

// 2b: PREPARATION
// Allocates memory for static fields
// Sets all static fields to zero values
// Does NOT execute static initializers yet

// 2c: RESOLUTION
// Replaces symbolic references with direct references
// lazy — done when first referenced

// Phase 3: INITIALIZATION
// Executes <clinit>() static initializer
// Thread-safe — JVM acquires lock
// Runs exactly once per classloader
```

### Parent-First Delegation Model

```java
// ClassLoader delegation chain
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        // 1. Check if already loaded
        Class<?> loaded = findLoadedClass(name);
        if (loaded != null) return loaded;

        // 2. Delegate to parent (parent-first)
        try {
            return getParent().loadClass(name);
        } catch (ClassNotFoundException e) {
            // 3. Parent failed — try self
            return findClass(name);
        }
    }
}

// Bootstrap ClassLoader (null reference)
// Loads: java.lang.*, java.util.*, etc.
// Written in: C++ (not Java)

// Platform ClassLoader (Java 9+)
// Loads: javax.*, jdk.*, etc.

// Application ClassLoader
// Loads: classpath entries
// Reference: ClassLoader.getSystemClassLoader()
```

### Thread Context ClassLoader

```java
// Problem: Parent-first breaks for SPI (Service Provider Interface)
// Solution: Thread context classloader

// Set context classloader
Thread.currentThread().setContextClassLoader(
    new URLClassLoader(urls, getClass().getClassLoader())
);

// Use in SPI code
public class SPILoader {
    public static <T> List<T> load(Class<T> type) {
        ServiceLoader<T> loader = ServiceLoader.load(type);
        // ServiceLoader uses thread context classloader
        // This breaks parent-first delegation
        return StreamSupport.stream(loader.spliterator(), false)
            .collect(Collectors.toList());
    }
}
```

## Examples

### Custom ClassLoader for Hot Deployment

```java
public class HotDeployClassLoader extends ClassLoader {
    private final Map<String, byte[]> classBytes = new ConcurrentHashMap<>();
    private final Path classDir;

    public HotDeployClassLoader(Path classDir, ClassLoader parent) {
        super(parent);
        this.classDir = classDir;
    }

    public void updateClass(String name, byte[] bytes) {
        classBytes.put(name, bytes);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = classBytes.get(name);
        if (bytes == null) {
            // Load from file system
            Path classFile = classDir.resolve(name.replace('.', '/') + ".class");
            bytes = Files.readAllBytes(classFile);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    // Force reload by removing from cache
    public void reload(String name) throws ClassNotFoundException {
        classBytes.remove(name);
        findLoadedClass(name); // Clear from JVM cache
        // Next loadClass() will reload
    }
}
```

### Debugging ClassNotFoundException

```java
// Diagnostic classloader hierarchy
public class ClassLoaderDiagnostic {
    public static void diagnose(String className) {
        System.out.println("=== ClassLoader Diagnostic ===");
        System.out.println("Class: " + className);

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        while (cl != null) {
            System.out.println("Loader: " + cl.getClass().getName());
            try {
                Class<?> clazz = cl.loadClass(className);
                System.out.println("  Found in: " + cl);
                System.out.println("  Resource: " + cl.getResource(
                    className.replace('.', '/') + ".class"));
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("  Not found");
            }
            cl = cl.getParent();
        }
        System.out.println("NOT LOADED by any classloader");
    }
}
```

### Metaspace Monitoring

```java
// Monitor Metaspace usage
public class MetaspaceMonitor {
    public static void printUsage() {
        ManagementFactory.getMemoryPoolMXBeans().stream()
            .filter(bean -> bean.getName().contains("Metaspace"))
            .forEach(bean -> {
                MemoryUsage usage = bean.getUsage();
                System.out.printf("Metaspace: used=%dMB, max=%dMB%n",
                    usage.getUsed() / (1024 * 1024),
                    usage.getMax() / (1024 * 1024));
            });
    }
}

// JVM flags for Metaspace
// -XX:MaxMetaspaceSize=256m
// -XX:MetaspaceSize=64m
// -XX:MaxMetaspaceExpansion=4m
```

## Performance

### Class Loading Overhead

| Operation | Time | When |
|-----------|------|------|
| findClass (read bytecode) | ~100μs | First load |
| defineClass (create Class) | ~50μs | First load |
| resolveClass (link) | ~200μs | First use |
| initializeClass (clinit) | ~10-1000μs | First use |
| findLoadedClass (cached) | ~0.1μs | Subsequent |

### Metaspace Impact

| Scenario | Metaspace Growth | Impact |
|----------|-----------------|--------|
| Normal application | 50-200MB | Stable |
| Classloader leak | Unlimited | OOM |
| Dynamic proxy generation | 10-50MB | Moderate |
| Hot deployment | 20-100MB per reload | High |

### Delegation Overhead

| Model | Time (1000 lookups) | Security |
|-------|---------------------|----------|
| Parent-first | ~15ms | High |
| Child-first | ~10ms | Low |
| Thread context | ~12ms | Medium |

## Pitfalls

### 1. ClassCastException with Multiple ClassLoaders

```java
// BAD: Different classloaders load same class
ClassLoader cl1 = new URLClassLoader(urls1);
ClassLoader cl2 = new URLClassLoader(urls2);
Class<?> c1 = cl1.loadClass("com.example.MyClass");
Class<?> c2 = cl2.loadClass("com.example.MyClass");
// c1 != c2 (different Class objects)
// Instance of c1 cannot be cast to c2

// GOOD: Use shared classloader or parent
```

### 2. Classloader Leak in Application Servers

```java
// BAD: Holding reference to classloader
private static final ClassLoader LEAKED_LOADER = 
    Thread.currentThread().getContextClassLoader();
// This prevents the classloader from being GC'd

// GOOD: Release classloader reference
public void undeploy() {
    // Clear all references to classes loaded by this classloader
    this.classLoader = null;
    // Force GC to unload classes
    System.gc();
}
```

### 3. ClassNotFoundException in WAR Deployment

```java
// BAD: Class in WEB-INF/classes not visible to shared libraries
// Tomcat classloader hierarchy:
// Bootstrap → System → Common → Webapp → WEB-INF/classes

// GOOD: Use Thread context classloader
Thread.currentThread().setContextClassLoader(
    getClass().getClassLoader()
);
```

### 4. NoClassDefFoundError vs ClassNotFoundException

```java
// ClassNotFoundException: class not found on classpath
// Fix: add missing JAR to classpath

// NoClassDefFoundError: class found but dependency missing
// Example: My.class exists but Helper.class (used by My) is missing
// Fix: add missing dependency, check transitive deps

// Diagnostic:
try {
    Class.forName("com.example.MyClass");
} catch (ClassNotFoundException e) {
    System.out.println("Class not found: " + e.getMessage());
} catch (NoClassDefFoundError e) {
    System.out.println("Dependency missing: " + e.getMessage());
}
```

### 5. Metaspace OutOfMemoryError

```java
// BAD: Dynamic class generation without unloading
while (true) {
    ClassLoader cl = new URLClassLoader(urls);
    Class<?> clazz = cl.loadClass("DynamicClass");
    // Classes accumulate in Metaspace
}

// GOOD: Ensure classloader is garbage collected
while (true) {
    ClassLoader cl = new URLClassLoader(urls);
    Class<?> clazz = cl.loadClass("DynamicClass");
    // Use class
    cl = null; // Allow GC to unload classes
}
```

## References

- [Java Virtual Machine Specification: Loading](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-5.html)
- [Java Class Loading](https://www.oreilly.com/library/view/java-class-loading/9781565924477/)
- [OpenJDK: ClassLoader.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/ClassLoader.java)
- *Inside the JVM* by Bill Venners
- [Classloader](https://www.cs.cornell.edu/courses/cs6120/2019fa/blog/classloaders/)
- [OSGi Specification](https://www.osgi.org/)
