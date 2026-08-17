# Project Structure Internals

## Java Package System

### Package Loading

When Java loads a class, it searches the classpath in order:
1. Bootstrap classpath (JDK classes)
2. Extension classpath (lib/ext)
3. Application classpath (your code)

```java
package com.company.project;

// Java looks for:
// com/company/project/MyClass.class
```

### Class Path Resolution

```java
import com.company.project.util.Helper;

// Java searches classpath for:
// com/company/project/util/Helper.class

// Classpath order matters:
// -J-Xbootclasspath:  Prepend to bootstrap
// -Djava.class.path=  Set classpath
// CLASSPATH env var   Fallback
```

### Package Access Control

```java
package com.company.project;

public class MyClass {
    private int privateField;      // Only this class
    int packageField;              // Same package only
    protected int protectedField;  // Subclasses + same package
    public int publicField;        // Anywhere
}
```

### Module System (JPMS)

```java
// module-info.java
module com.company.project {
    requires java.sql;           // Depend on java.sql module
    requires com.other.module;   // Depend on external module
    
    exports com.company.api;     // Public API
    opens com.company.internal;  // Reflective access
}
```

### Inner Class File Naming

```java
public class Outer {
    class Inner { }
    static class StaticInner { }
}

// Generates:
// Outer.class
// Outer$Inner.class
// Outer$StaticInner.class
```

### Package-Private Access

```java
// Same package, no import needed
package com.company.project;

class PackagePrivate { }  // Accessible within package
```

### Annotation Processing

```java
package com.company.project;

// Annotation processors run during compilation
// They generate source files or resources

@Generated("com.company.CodeGenerator")
public class GeneratedClass { }
```

### Resource Loading

```java
// Loading resources from classpath
InputStream is = getClass().getResourceAsStream("/config.properties");
URL url = getClass().getResource("template.html");

// Resource path resolution:
// 1. Class's package directory
// 2. Classpath root
// 3. Module path
```
