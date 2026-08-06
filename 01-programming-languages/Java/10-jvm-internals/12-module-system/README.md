# 12. Java Platform Module System (JPMS)

## Introduction

The Java Platform Module System (JPMS), introduced in Java 9 (Project Jigsaw), provides a reliable configuration and strong encapsulation mechanism. It replaces the classpath with a module path and enables better performance, security, and maintainability.

## Module System Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Module System                            │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  module-info.java                                   │   │
│  │  - Module declaration                               │   │
│  │  - Dependencies (requires)                          │   │
│  │  - Exports (exports)                                │   │
│  │  - Opens (opens)                                    │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Module Path                                        │   │
│  │  - Replaces classpath                               │   │
│  │  - Named modules                                    │   │
│  │  - Automatic modules                                │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Module Declarations

### Basic Module Structure

```java
module com.example.myapp {
    // Dependencies
    requires java.sql;
    requires java.logging;
    
    // Exports
    exports com.example.api;
    exports com.example.model to com.example.client;
    
    // Opens (for reflection)
    opens com.example.impl to com.example.testing;
    
    // Services
    uses com.example.spi.MyService;
    provides com.example.spi.MyService with com.example.impl.MyServiceImpl;
}
```

### Declaration Elements

| Element | Description |
|---------|-------------|
| `requires` | Declares dependency on another module |
| `requires transitive` | Dependency exposed to dependents |
| `exports` | Makes package public to other modules |
| `exports...to` | Restricts exports to specific modules |
| `opens` | Opens package for reflection |
| `opens...to` | Opens to specific modules only |
| `uses` | Declares service consumption |
| `provides...with` | Declares service provider |

## Module Path vs Classpath

### Classpath (Legacy)

```bash
java -cp lib/*.jar com.example.Main
```

- Flat namespace (all classes visible)
- No encapsulation
- Fragile (missing classes at runtime)
- JAR hell

### Module Path (Modern)

```bash
java --module-path mods -m com.example/com.example.Main
java -p mods/... -m module/class
```

- Named modules with encapsulation
- Reliable configuration
- Better performance
- Startup optimization

## Strong Encapsulation

### Without Module System

- All public classes accessible via reflection
- Internal APIs can be accessed
- Frameworks rely on deep JDK internals

### With Module System

- Only exported packages are accessible
- Reflection restricted to opened packages
- Internal APIs hidden by default
- Clean API boundaries

### Encapsulation Rules

1. **Unexported package**: No compile-time access
2. **Unopened package**: No reflection access
3. **Module boundary**: Strong encapsulation
4. **Reflection requires 'opens' directive**

## Reliable Configuration

### Classpath Problems

- `ClassNotFoundException` at runtime
- `NoNameFoundError` for missing dependencies
- Version conflicts between JARs
- Split packages (same package in multiple JARs)

### Module System Solutions

- All dependencies declared explicitly
- Configuration verified at startup
- No split packages allowed
- Missing modules caught immediately

### Module Resolution Process

1. Module system resolves all dependencies
2. Checks for split packages
3. Verifies all required modules are present
4. Fails fast if configuration is invalid

## Migration from Classpath

### Step-by-Step Migration

1. **Add module-info.java** to each JAR
2. **Declare requires** for dependencies
3. **Export public API** packages
4. **Open packages** needed for reflection
5. **Test with --module-path** before switching

### Common Migration Challenges

- Frameworks using deep reflection need `opens`
- Libraries using internal APIs need refactoring
- Testing frameworks need `opens` for mocking

## Key JDK Modules

| Module | Description |
|--------|-------------|
| `java.base` | Core classes (String, Object, etc.) |
| `java.sql` | JDBC API |
| `java.logging` | Logging API |
| `java.management` | JMX API |
| `java.desktop` | AWT/Swing |
| `java.xml` | XML processing |

## Best Practices

1. **Start with module-info.java**: Add to new projects
2. **Export only public API**: Use packages wisely
3. **Open for reflection**: When frameworks require it
4. **Use requires transitive**: For transitive dependencies
5. **Test thoroughly**: With module path enabled

## Interview Questions

1. **What is JPMS?** - Java Platform Module System (Project Jigsaw)
2. **What is the difference between requires and requires transitive?** - transitive exposes dependency to dependents
3. **What is the difference between exports and opens?** - exports is compile-time, opens is runtime (reflection)
4. **How do you migrate from classpath?** - Add module-info.java, declare dependencies, test

## References

- [Java Module System (Jigsaw)](https://openjdk.java.net/projects/jigsaw/)
- [JPMS Documentation](https://docs.oracle.com/en/java/javase/17/language/)
- "Java 9 Modularity" by Sander Mak
