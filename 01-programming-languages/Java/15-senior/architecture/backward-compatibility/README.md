# Backward Compatibility

Maintaining compatibility across versions.

## Binary Compatibility

### Definition
Code compiled against one version works with another without recompilation.

### Binary Compatible Changes
- Adding new classes, methods, fields
- Adding new exceptions to throws clause
- Adding type parameters
- Removing `final` from class/method

### Binary Incompatible Changes
- Removing classes, methods, fields
- Changing method signatures
- Changing field types
- Adding abstract methods to class
- Changing superclass/interface

### Checking Binary Compatibility
```bash
# Using japicmp
java -jar japicmp.jar \
  --old-jar old.jar \
  --new-jar new.jar

# Using Revapi
revapi.sh --old old.jar --new new.jar
```

## Source Compatibility

### Definition
Source code compiles against both versions.

### Source Compatible Changes
- Adding new methods with default implementations
- Adding new classes
- Adding new interfaces
- Adding `final` to class

### Source Incompatible Changes
- Removing methods
- Changing method signatures
- Adding abstract methods
- Changing field types

### Example
```java
// Version 1.0
public class UserService {
    public User findById(String id) { }
}

// Version 1.1 (Source compatible)
public class UserService {
    public User findById(String id) { }
    
    // New method - source compatible
    public Optional<User> findByIdOptional(String id) { }
}

// Version 2.0 (Source incompatible)
public class UserService {
    // findById removed - breaks source compatibility
}
```

## Behavioral Compatibility

### Definition
Same input produces same output.

### Behavioral Compatible Changes
- Adding new functionality
- Optimizing performance
- Fixing bugs (intentional changes)

### Behavioral Incompatible Changes
- Changing return values
- Changing exception types
- Changing side effects
- Changing timing/ordering

### Example
```java
// Version 1.0
public int calculate(int a, int b) {
    return a + b;  // Returns sum
}

// Version 2.0 (Behavioral change)
public int calculate(int a, int b) {
    return a * b;  // Now multiplies - behavioral change!
}
```

## Deprecation Strategy

### @Deprecated Annotation
```java
// Deprecate method
@Deprecated(since = "1.2", forRemoval = true)
public void oldMethod() {
    // Old implementation
}

// Deprecate class
@Deprecated(since = "1.5")
public class OldClass {
    // Still works but should not be used
}
```

### Deprecation Policy
1. **@Deprecated**: Mark as deprecated
2. **@since**: Version when deprecated
3. **forRemoval**: Whether removed in future
4. **Javadoc**: Explain replacement
5. **Timeline**: Provide migration period

### Deprecation Phases
1. **Phase 1**: Mark as @Deprecated
2. **Phase 2**: Log warnings
3. **Phase 3**: Remove (after deprecation period)

### Example Deprecation
```java
/**
 * @deprecated Use {@link #newMethod()} instead.
 *             This method will be removed in version 2.0.
 * @since 1.0
 * @see #newMethod()
 */
@Deprecated(since = "1.2", forRemoval = true)
public void oldMethod() {
    // ...
}
```

## Migration Guides

### Migration Checklist
1. **Identify Changes**: List all API changes
2. **Document Changes**: Create migration guide
3. **Provide Helpers**: Add compatibility utilities
4. **Test Thoroughly**: Verify backward compatibility
5. **Communicate**: Inform users of changes

### Migration Helper Pattern
```java
// Compatibility layer
public class CompatibilityHelper {
    /**
     * @deprecated Use {@link #newApi()} instead.
     */
    @Deprecated
    public static void legacyApi() {
        newApi();
    }
    
    public static void newApi() {
        // New implementation
    }
}
```

### Version Strategy
```
Version 1.0: Initial release
Version 1.1: Add new features
Version 1.2: Deprecate old API
Version 1.3: Add migration helpers
Version 2.0: Remove deprecated API
```

## Best Practices

### API Design
```java
// Good: Extensible
public interface UserService {
    User findById(String id);
    
    // Default method for extensibility
    default Optional<User> findByIdOptional(String id) {
        return Optional.ofNullable(findById(id));
    }
}

// Bad: Not extensible
public class UserService {
    public User findById(String id) { }
    // Adding new method breaks compatibility
}
```

### Interface Evolution
```java
// Version 1.0
public interface Repository {
    Object findById(String id);
}

// Version 2.0 (Compatible)
public interface Repository {
    Object findById(String id);
    
    // Default method - no break
    default boolean exists(String id) {
        return findById(id) != null;
    }
}
```

### Version Management
```java
// Semantic versioning
// MAJOR.MINOR.PATCH
// MAJOR: Incompatible changes
// MINOR: Compatible additions
// PATCH: Bug fixes

// Maven
<version>1.2.3</version>

// Gradle
version = '1.2.3'
```

### Testing Compatibility
```java
// Binary compatibility test
@Test
void testBinaryCompatibility() {
    // Load old version
    Class<?> oldClass = classLoader.loadClass("com.v1.MyClass");
    
    // Verify methods exist
    Method method = oldClass.getMethod("myMethod", String.class);
    assertNotNull(method);
}

// Source compatibility test
// Compile with both versions
// mvn compile -Dold.version=1.0
// mvn compile -Dnew.version=1.1
```

## Common Pitfalls

### Breaking Changes
1. **Removing methods**: Always deprecate first
2. **Changing signatures**: Use overloads
3. **Changing return types**: Add new methods
4. **Changing exceptions**: Add new exception types

### Safe Changes
1. **Adding methods**: With default implementation
2. **Adding classes**: New functionality
3. **Adding interfaces**: New contracts
4. **Adding parameters**: With default values

### Migration Tips
1. **Provide clear upgrade path**
2. **Write migration guide**
3. **Add compatibility layer**
4. **Test thoroughly**
5. **Communicate changes**

## Interview Questions

1. **What is the difference between binary, source, and behavioral compatibility?**
   Binary compatibility means code compiled against one version works with another without recompilation. Source compatibility means source code compiles against both versions. Behavioral compatibility means same input produces same output. Binary is strongest (no recompile), behavioral is weakest (same behavior, not just compilation).

2. **When should you use default methods on interfaces?**
   Use default methods when adding new methods to interfaces without breaking existing implementations. They allow interface evolution while maintaining backward compatibility. However, they can cause the diamond problem when multiple interfaces provide conflicting defaults. Prefer overloading with default implementations over adding abstract methods.

3. **How do you test backward compatibility between library versions?**
   Use japicmp or Revapi to compare binary compatibility between JARs. Write cross-version tests: compile code against old version, run against new version. Use API compatibility checkers in CI/CD. Test with old consumer code against new library code to catch breaking changes.

4. **What is the @Deprecated annotation lifecycle?**
   Phase 1: Mark with @Deprecated and forRemoval=false. Phase 2: Log warnings when deprecated API is used. Phase 3: Update forRemoval=true. Phase 4: Remove after deprecation period (typically 2 major versions). Always provide a migration path and document the replacement in Javadoc.

5. **How does semantic versioning help with backward compatibility?**
   Semantic versioning (MAJOR.MINOR.PATCH) communicates compatibility guarantees: MAJOR = breaking changes, MINOR = backward-compatible additions, PATCH = backward-compatible fixes. This allows consumers to upgrade safely within the same MAJOR version, knowing no breaking changes exist.

## Pitfalls

**Breaking changes in library releases:**
```java
// BAD: Removing method in minor version
// v1.0
public class UserService {
    public User findById(String id) { ... }
}

// v1.1 (BREAKING: removed method)
public class UserService {
    // findById removed — consumer code breaks
}

// GOOD: Deprecate first, remove in next major
// v1.0
public class UserService {
    public User findById(String id) { ... }
}

// v1.1
@Deprecated(since = "1.1", forRemoval = false)
public User findById(String id) { ... }

// v2.0
// findById removed (after deprecation period)
```

**Changing method signatures:**
```java
// BAD: Adding required parameter breaks consumers
// v1.0
public Order createOrder(String productId, int quantity) { ... }

// v2.0 (BREAKING: changed signature)
public Order createOrder(String productId, int quantity, String currency) {
    // All existing calls break
}

// GOOD: Add overload with default behavior
// v2.0
public Order createOrder(String productId, int quantity) {
    return createOrder(productId, quantity, "USD"); // Default currency
}

public Order createOrder(String productId, int quantity, String currency) {
    // New implementation
}
```

**Not providing migration helpers:**
```java
// BAD: Just deprecate and move on
@Deprecated
public void legacyApi() { ... }

// GOOD: Provide migration helper with clear instructions
/**
 * @deprecated Use {@link #newApi(Request)} instead.
 *             Migration guide: https://wiki.example.com/migration
 *             This method will be removed in v3.0.
 * @since 1.2
 */
@Deprecated(since = "1.2", forRemoval = true)
public Result legacyApi() {
    logger.warn("legacyApi() is deprecated. Migrate to newApi()");
    return newApi(Request.fromLegacy());
}
```

## Performance

**Backward Compatibility Overhead:**
- Default methods: Zero runtime overhead (compiled like regular methods)
- Bridge methods: ~1-2ns per invocation (synthetic methods for covariant return types)
- Reflection-based compatibility layers: 50-100ns overhead (use sparingly)
- API versioning (header-based): 0.1-1ms overhead per request (HTTP header parsing)

**japicmp Benchmark Results:**
```
Comparing two JARs (1000 classes, 10000 methods):
- Binary compatibility check: 2.3 seconds
- Source compatibility check: 4.1 seconds
- Full API report generation: 8.5 seconds
- Memory usage: 256MB max

CI/CD integration: Add 5-10 seconds to build pipeline
```

## Internal Working

**Binary Compatibility Detection:**
1. japicmp/Revapi loads old and new JAR files
2. Parses class files using ASM bytecode library
3. Compares method signatures, field types, class hierarchies
4. Detects removed classes/methods/fields
5. Detects changed method signatures
6. Detects added abstract methods
7. Generates compatibility report with violations

**Default Method Compilation:**
```java
// Interface with default method
public interface Repository {
    Object findById(String id);
    
    default boolean exists(String id) {
        return findById(id) != null;
    }
}

// Compiled bytecode includes default method in interface
// Implementing class doesn't need to override exists()
// JVM resolves default method at link time
```

## Why This Concept Exists

Backward compatibility is critical because:

1. **Ecosystem stability**: Libraries with millions of users can't break compatibility in minor versions
2. **Deployment safety**: Applications depend on dozens of libraries; incompatible upgrades cascade failures
3. **Developer productivity**: Breaking changes force consumers to modify code, increasing maintenance burden
4. **Enterprise requirements**: Regulated industries require stable APIs with guaranteed support periods
5. **Open-source trust**: Users adopt libraries they trust won't break their code unexpectedly

The deprecation lifecycle exists because abrupt removal breaks consumers. A gradual deprecation period (typically 2 major versions) gives consumers time to migrate while maintaining trust.

## Overview

Backward compatibility ensures code compiled against one version works with another without recompilation or behavioral changes. It encompasses binary compatibility (same bytecode), source compatibility (same source), and behavioral compatibility (same output). This document covers compatibility types, deprecation strategies, migration guides, and best practices for Java library authors and API designers.

## References

- Oracle Java Tutorials — Binary Compatibility: https://docs.oracle.com/javase/tutorial/information/glossary.html
- japicmp GitHub: https://github.com/siom79/japicmp
- Revapi API Checker: https://revapi.org/
- Semantic Versioning Specification: https://semver.org/
- "Effective Java" by Joshua Bloch — Item 26: Favor using standard interfaces
- Java Language Specification — Binary Compatibility: https://docs.oracle.com/javase/specs/jls/se21/html/jls-13.html
