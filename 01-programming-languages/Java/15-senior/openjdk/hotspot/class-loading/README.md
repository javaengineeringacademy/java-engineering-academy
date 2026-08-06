# Class Loading in HotSpot

Class loading is the process of loading `.class` files into the JVM, verifying bytecode, preparing class data, and initializing static fields. HotSpot implements the class loading mechanism defined in the JVM specification.

## Class Loading Lifecycle

```
Loading → Linking (Verification → Preparation → Resolution) → Initialization
```

### 1. Loading

The class loader reads the `.class` file and creates a `java.lang.Class` object in the JVM.

**Sources for loading:**
- File system (`.class` files from the build)
- JAR/ZIP archives
- Network (applets, RMI)
- Generated at runtime (proxy classes, lambda)

**What happens:**
- Find the `.class` bytes via the class loader
- Parse the binary data into internal representation
- Create a `Klass` object in Metaspace
- Create the mirror (`java.lang.Class`) in the Java heap

### 2. Linking

#### Verification

Bytecode verification ensures the `.class` file is structurally correct and type-safe:

- **Format verification**: Magic number, version, constant pool structure
- **Semantic verification**: Stack type consistency, access control, branch targets
- **Type verification**: Ensures type compatibility in assignments and calls

```bash
# Disable verification (dangerous, for development only)
-XX:-UseSplitVerifier
-XX:-FailOverToOldVerifier
```

#### Preparation

The JVM allocates memory for static fields and assigns default values:

```java
class Example {
    static int x = 10;    // Prepared: x = 0 (default)
    static String name;   // Prepared: name = null (default)
}
```

Static final constants are pre-computed during preparation.

#### Resolution

Symbolic references in the constant pool are replaced with direct references:

- Class references → `Klass` pointers
- Field references → memory offsets
- Method references → entry points

Resolution may happen lazily (on first use) or eagerly, depending on the implementation.

### 3. Initialization

Static initializers and static field assignments are executed:

```java
class Example {
    static int x = 10;        // Initialized: x = 10
    static String name = "hi"; // Initialized: name = "hi"
    static { System.out.println("Class loaded!"); }
}
```

Initialization is thread-safe — the JVM uses locking to ensure a class is initialized only once.

## Class Loader Hierarchy

```
Bootstrap Class Loader (null)
    ↓
Platform Class Loader (java.*)
    ↓
Application Class Loader (class.path)
    ↓
Custom Class Loaders (user-defined)
```

### Bootstrap Class Loader

- Loads core Java classes (`java.lang.String`, `java.util.ArrayList`)
- Written in native code (C++), not a Java object
- Loads from `jmods` files and the `lib/` directory
- `Class.forName()` returns `null` for the bootstrap loader

### Platform Class Loader (Extension Class Loader)

- Loads classes from the `java.*` modules (Java 9+)
- Replaced the old extension class loader
- Loads `java.sql`, `java.xml`, `java.logging`, etc.

### Application Class Loader (System Class Loader)

- Loads classes from the application classpath (`-cp`, `-classpath`)
- The default loader for application code
- `ClassLoader.getSystemClassLoader()` returns this loader

### Custom Class Loaders

You can create your own class loader by extending `java.lang.ClassLoader`:

```java
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = loadClassBytes(name);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
```

Common use cases:
- **Hot reloading**: Load new versions of classes without restart
- **Isolation**: Separate class spaces for plugins
- **Network loading**: Download classes from remote servers
- **Transformation**: Modify bytecode before loading (instrumentation)

## Delegation Model

Class loaders use a parent-first delegation model:

```java
protected Class<?> loadClass(String name, boolean resolve) {
    // 1. Check if already loaded
    Class<?> c = findLoadedClass(name);
    if (c == null) {
        // 2. Delegate to parent
        try {
            c = parent.loadClass(name, false);
        } catch (ClassNotFoundException e) {
            // 3. If parent can't find, load it ourselves
            c = findClass(name);
        }
    }
    return c;
}
```

This ensures core classes are always loaded by the bootstrap loader, preventing classes like `java.lang.String` from being replaced by untrusted code.

## Class Loading in HotSpot Source

### Key Classes

| Class | Purpose |
|-------|---------|
| `SystemDictionary` | Central class loading registry |
| `ClassLoaderData` | Metadata for a class loader |
| `Klass` | Internal representation of a Java class |
| `InstanceKlass` | Class with instance fields |
| `ObjArrayKlass` | Array class |
| `Symbol` | Interned string (class names, descriptors) |

### Loading Flow in HotSpot

```
1. Java calls Class.forName() or ClassLoader.loadClass()
2. JVM enters VM (via JNI transition)
3. SystemDictionary.resolve_or_null() is called
4. ClassLoaderData is found or created
5. find_class_or_null() checks if already loaded
6. If not, load_class() invokes the Java class loader
7. Java loader calls findClass() → defineClass()
8. defineClass() enters VM → SystemDictionary.define_instance_class()
9. Class is linked (verified, prepared, resolved)
10. Class is initialized
```

### Key Source Files

| File | Purpose |
|------|---------|
| `src/hotspot/share/classfile/systemDictionary.cpp` | Core class loading logic |
| `src/hotspot/share/classfile/classLoader.cpp` | Class file loading |
| `src/hotspot/share/classfile/classLoaderData.cpp` | ClassLoader data management |
| `src/hotspot/share/oops/instanceKlass.hpp` | InstanceKlass definition |
| `src/hotspot/share/oops/klass.hpp` | Base Klass definition |

## CDS (Class Data Sharing)

CDS pre-loads and archiving classes at build time or during `-Xshare:dump`:

```bash
# Create CDS archive
-XX:SharedArchiveFile=shared.jsa -Xshare:dump

# Use CDS archive
-XX:SharedArchiveFile=shared.jsa -Xshare:on
```

Benefits:
- Faster startup (classes already verified and linked)
- Reduced memory footprint (shared across processes)
- Better AOT compatibility
