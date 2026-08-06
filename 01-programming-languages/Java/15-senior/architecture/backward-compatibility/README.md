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
