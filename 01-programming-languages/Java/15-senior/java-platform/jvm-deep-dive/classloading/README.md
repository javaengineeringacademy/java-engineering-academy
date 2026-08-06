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
