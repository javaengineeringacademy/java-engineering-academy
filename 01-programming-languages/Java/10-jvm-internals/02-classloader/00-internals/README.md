# 02. ClassLoader Internals Deep Dive

## ClassLoader Architecture

The JVM uses a hierarchical classloading system with three built-in classloaders and support for custom classloaders.

### Built-in ClassLoaders

```
Bootstrap ClassLoader (native, C/C++)
├── Loads: java.base module (java.lang.*, java.util.*, etc.)
├── Source: $JAVA_HOME/lib (or jrt:/ filesystem in Java 9+)
├── Java reference: null (no Java representation)
└── Purpose: Core JDK classes that form the foundation

Platform ClassLoader (Java, java.lang.ClassLoader)
├── Loads: java.xml, java.sql, java.logging, java.desktop, etc.
├── Source: Module path ($JAVA_HOME/jmods/)
├── Java reference: ClassLoader.getPlatformClassLoader()
└── Purpose: Non-core platform modules

Application ClassLoader (Java, java.lang.ClassLoader)
├── Loads: Application classes from classpath
├── Source: -classpath / -cp / CLASSPATH environment variable
├── Java reference: ClassLoader.getSystemClassLoader()
└── Purpose: User application classes
```

### Parent Delegation Model Internals

The delegation model is implemented in `java.lang.ClassLoader.loadClass()`:

```java
// Simplified loadClass() logic
protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    // 1. Check if already loaded
    Class<?> c = findLoadedClass(name);
    if (c == null) {
        try {
            // 2. Delegate to parent
            if (parent != null) {
                c = parent.loadClass(name, false);
            } else {
                c = findBootstrapClassOrNull(name);
            }
        } catch (ClassNotFoundException e) {
            // Parent could not find the class
        }
        if (c == null) {
            // 3. Find locally (child-first fallback)
            c = findClass(name);
        }
    }
    if (resolve) {
        resolveClass(c);
    }
    return c;
}
```

### Why Parent-First?

1. **Security**: Prevents untrusted code from replacing core Java classes like `java.lang.String`
2. **Consistency**: Ensures all code uses the same core class definitions
3. **Caching**: Classes loaded once by a parent are shared across all children
4. **Avoidance of duplication**: Prevents loading the same class multiple times

### Custom ClassLoader Internals

When you extend `ClassLoader`, you control how classes are found and defined:

```
CustomClassLoader.loadClass(name)
    │
    ├── findLoadedClass(name) → Already loaded? Return it
    │
    ├── parent.loadClass(name) → Delegate to parent
    │
    └── findClass(name) → Load from your source
            │
            └── defineClass(name, bytes) → Create Class object
```

### Key Internal Methods

| Method | Purpose |
|--------|---------|
| `loadClass(String)` | Entry point, implements delegation |
| `findClass(String)` | Override to load from custom source |
| `defineClass(String, byte[])` | Convert bytes to Class object |
| `findLoadedClass(String)` | Check cache of already-loaded classes |
| `resolveClass(Class<?>)` | Link a class (verification, preparation, resolution) |
| `getResource(String)` | Find resource associated with the classloader |

### Class Loading Phases Internals

```
Phase 1: LOADING
├── findClass() locates the .class bytes
├── defineClass() converts bytes to java.lang.Class
└── Class is now in the "loaded" state

Phase 2: LINKING
├── VERIFICATION
│   ├── Check magic number (0xCAFEBABE)
│   ├── Verify version compatibility
│   ├── Validate constant pool
│   └── Verify bytecode instructions (type safety, stack map frames)
├── PREPARATION
│   ├── Allocate memory for static fields
│   ├── Set default values (0, null, false)
│   └── Assign constant values (static final primitives)
└── RESOLUTION
    ├── Replace symbolic references with direct references
    ├── Class references → Class objects
    ├── Field references → memory offsets
    └── Method references → entry points

Phase 3: INITIALIZATION
├── Execute <clinit>() methods
├── Run static blocks in textual order
└── Assign static variable values from source code
```

### Thread Context ClassLoader

The Thread Context ClassLoader (TCCL) breaks parent delegation:

```
Normal delegation:  Bootstrap → Platform → Application → Custom
TCCL override:      Application → Custom (bypasses parent)
```

Used by:
- **JDBC**: DriverManager (bootstrap) needs to find drivers (application)
- **JNDI**: Bootstrap classes need to load implementations from application
- **Spring**: Parent-first for app code, child-first for library isolation
- **Application servers**: Each web app has its own classloader

### ClassLoader Leak Internals

A classloader leak occurs when a classloader cannot be garbage collected because strong references still exist:

```
Leak Sources:
├── ThreadLocal values (thread holds reference)
├── Static fields in loaded classes (class holds reference)
├── JDBC Driver registrations (DriverManager static list)
├── JNDI bindings (naming service holds reference)
├── RMI remote objects (registry holds reference)
└── Custom caches (application holds reference)

Detection:
├── jcmd <pid> VM.classloader_stats
├── ManagementFactory.getClassLoadingMXBean()
└── Heap dump analysis (Dominator Tree)
```
