# Project Structure Memory Model

## Class Loading Memory

When Java loads classes, it allocates memory for:
- Class metadata (method tables, field layouts)
- Constant pool
- Static fields
- Method bytecode

### Class Loading Phases

```
1. Loading: Read .class file into memory
2. Linking: Verify, prepare, resolve
3. Initialization: Execute static initializers
```

### Module Memory

```java
// Module system adds metadata overhead
module com.company.project {
    requires java.sql;
    exports com.company.api;
}

// Memory: Module descriptor + requires/exports tables
```

### Package-Private Memory

```java
package com.company.project;

class PackagePrivate {
    int field;  // Accessible within package
}

// No additional memory overhead for package-private access
```

### Inner Class Memory

```java
public class Outer {
    private int outerField;
    
    class Inner {
        void access() {
            // Can access outerField
            // Creates implicit reference to Outer instance
        }
    }
}

// Inner class memory:
// - Reference to Outer instance (8 bytes)
// - Method tables
// - Field accessors
```

### Static vs Instance Memory

```java
public class MyClass {
    static int staticField;    // Stored in Method Area
    int instanceField;         // Stored in Heap (per instance)
}

// Static: One copy per class
// Instance: One copy per object
```

### Annotation Processing Memory

```java
@Retention(RUNTIME)
@Target(TYPE)
public @interface MyAnnotation {
    String value();
}

// Annotation metadata stored in class file
// Processed at compile time or runtime via reflection
```

### Resource Loading Memory

```java
// Resources loaded from classpath
InputStream is = getClass().getResourceAsStream("/config.properties");

// Memory: Resource data loaded into heap
// Cached by ClassLoader for repeated access
```
