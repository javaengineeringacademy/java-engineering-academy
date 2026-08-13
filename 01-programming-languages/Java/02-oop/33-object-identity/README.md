# Object Identity

## Learning Objectives
By the end of this topic, you will be able to:
- Understand what object identity means in Java
- Differentiate between object identity, equality, and reference comparison
- Use `==` vs `equals()` correctly
- Implement proper identity-based logic

## Prerequisites
- Basic understanding of objects and references
- Knowledge of `equals()` and `hashCode()` methods

## Why This Concept Exists

### The Problem
When comparing objects, developers often confuse identity (same object in memory) with equality (same content/values). This leads to bugs in collections, caching, and comparison logic.

### The Solution
Object identity provides a way to determine if two references point to the exact same object in memory, separate from whether their contents are equal.

## Internal Working

### JVM Perspective
- Each object has a unique identity in memory
- The `==` operator compares references (memory addresses)
- The `equals()` method compares content/values
- `System.identityHashCode()` returns the default hash code based on memory address

### Memory Representation
```java
String s1 = new String("hello");
String s2 = new String("hello");
String s3 = s1;

// s1 == s2: false (different objects)
// s1 == s3: true (same object)
// s1.equals(s2): true (same content)
// s1.equals(s3): true (same content)
```

## Syntax

```java
// Reference comparison (identity)
if (obj1 == obj2) {
    // Same object in memory
}

// Content comparison (equality)
if (obj1.equals(obj2)) {
    // Same content/values
}

// Identity hash code
int hash = System.identityHashCode(obj);
```

## Easy Examples

### Example 1: Reference vs Content Comparison
```java
public class IdentityDemo {
    public static void main(String[] args) {
        String s1 = new String("hello");
        String s2 = new String("hello");
        String s3 = s1;
        
        System.out.println("s1 == s2: " + (s1 == s2));      // false
        System.out.println("s1 == s3: " + (s1 == s3));      // true
        System.out.println("s1.equals(s2): " + s1.equals(s2)); // true
    }
}
```

## Medium Examples

### Example 1: Identity in Collections
```java
import java.util.*;

public class IdentityInCollections {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        String s1 = new String("hello");
        String s2 = new String("hello");
        
        set.add(s1);
        set.add(s2);
        
        System.out.println("Set size: " + set.size()); // 1 (equals-based)
    }
}
```

## Hard Examples

### Example 1: Custom Identity Logic
```java
public class Entity {
    private final long id;
    
    public Entity(long id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Identity check first
        if (obj == null || getClass() != obj.getClass()) return false;
        Entity other = (Entity) obj;
        return id == other.id;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
```

## Best Practices
1. Use `==` for identity comparison (same object)
2. Use `equals()` for content comparison
3. Always override `equals()` and `hashCode()` together
4. Consider identity-based logic for caching and interning

## Common Pitfalls
- Forgetting that `==` compares references, not content
- Not overriding `equals()` in custom classes
- Mixing identity and equality logic

## Related Topics
- 14-object-class
- 25-immutable-objects
- 34-value-objects
