# Module 59: Migration Guides

## Overview
Guides for migrating Java applications between versions, frameworks, and architectures. Covers Java version upgrades, Spring Boot migrations, and modernization strategies.

## Learning Objectives
- Plan Java version upgrades
- Migrate between frameworks
- Modernize legacy applications
- Handle breaking changes
- Test migrations thoroughly

## Prerequisites
- Java development experience
- Framework knowledge
- Migration planning

## Why This Concept Exists
Legacy applications need:
- Security updates
- Performance improvements
- New features
- Support lifecycle

Migration provides:
- Updated technology
- Improved maintainability
- Better performance
- Security compliance

## Problem Statement
How do you safely migrate Java applications to newer versions or frameworks?

## Migration Guides

### Java Version Migration

| From | To | Key Changes |
|------|-----|-------------|
| Java 8 | Java 11 | Modules, HTTP Client, ZGC |
| Java 11 | Java 17 | Records, Pattern Matching, Sealed Classes |
| Java 17 | Java 21 | Virtual Threads, Pattern Matching, Record Patterns |

### Java 8 to 11

| Feature | Change |
|---------|--------|
| Modules | Module system introduced |
| HTTP Client | New HttpClient API |
| String | New methods (isBlank, strip, etc.) |
| Optional | New methods |
| Process API | Improved |
| ZGC | Experimental |

### Java 11 to 17

| Feature | Change |
|---------|--------|
| Records | Immutable data classes |
| Pattern Matching | instanceof with pattern |
| Sealed Classes | Restricted inheritance |
| Text Blocks | Multi-line strings |
| Switch Expressions | Enhanced switch |
| Helpful NullPointerExceptions | Better error messages |

### Java 17 to 21

| Feature | Change |
|---------|--------|
| Virtual Threads | Lightweight threads |
| Pattern Matching | Enhanced switch |
| Record Patterns | Destructuring records |
| Sequenced Collections | Unified collection API |
| String Templates | Template strings |

### Spring Boot Migration

| From | To | Key Changes |
|------|-----|-------------|
| 2.x | 3.x | Jakarta EE, Java 17+, Native |

### Spring Boot 2 to 3

| Change | Impact |
|--------|--------|
| Java baseline | Java 17+ required |
| Jakarta EE | javax.* to jakarta.* |
| Properties | Property renames |
| Auto-configuration | New registration |
| GraalVM | Native support |

## Enterprise Example

```java
// Migration from Java 8 to 17

// Before (Java 8)
public class UserDTO {
    private final Long id;
    private final String name;
    
    public UserDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && 
               Objects.equals(name, userDTO.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

// After (Java 17) - Record
public record UserDTO(Long id, String name) {}

// Pattern matching with instanceof
public String process(Object obj) {
    if (obj instanceof UserDTO user) {
        return "User: " + user.name();
    }
    return "Unknown";
}

// Text blocks
String json = """
        {
            "name": "%s",
            "id": %d
        }
        """.formatted(user.name(), user.id());
```

## Performance Considerations
- Test thoroughly after migration
- Benchmark before and after
- Monitor for regressions
- Use feature flags

## Best Practices
1. Plan migration carefully
2. Test incrementally
3. Update dependencies
4. Monitor after migration
5. Document changes

## Interview Questions

### Q1: What are the key changes in Java 17?
**Answer:** Records, pattern matching, sealed classes, text blocks.

### Q2: What is the difference between javax and jakarta?
**Answer:** Jakarta EE is the successor to Java EE, namespace changed.

### Q3: How do you migrate from Spring Boot 2 to 3?
**Answer:** Update Java to 17+, change javax to jakarta, update properties.

### Q4: What are virtual threads?
**Answer:** Lightweight threads managed by the JVM, not the OS.

### Q5: What is GraalVM native image?
**Answer:** Compile Java to native executable for faster startup.

## Summary
Migration requires careful planning and testing. Follow guides and test thoroughly.

## References
- Java Migration Guide
- Spring Boot Migration Guide
- Jakarta EE Documentation
