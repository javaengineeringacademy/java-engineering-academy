# 07 — Custom Annotations

## Why Custom Annotations Matter

Annotations let you attach metadata to code without changing its behavior. The behavior comes from processors that read the annotations. This separation of metadata from logic is powerful — it is how Spring knows which beans to inject, which HTTP endpoint to map, and which methods to test.

Custom annotations let you create your own domain-specific metadata, turning repetitive boilerplate into clean, declarative code.

---

## Defining an Annotation — @interface

```java
public @interface MyAnnotation {
    String value();
    int priority() default 5;
    String[] tags() default {};
    Class<?> type() default Object.class;
}
```

### Element Types Allowed

| Type | Example | Notes |
|------|---------|-------|
| Primitive | `int count()` | int, long, double, etc. |
| String | `String name()` | |
| Class | `Class<?> type()` | |
| Enum | `Status status()` | Must be nested or imported |
| Annotation | `Meta meta()` | Annotation within annotation |
| Array | `String[] values()` | Single-element arrays auto-wrap |

### Restrictions

- No constructors
- No instance fields (only annotation elements)
- Cannot extend other annotations
- Elements cannot have parameters
- Return types are limited to the table above

---

## Using Annotations

```java
@MyAnnotation(value = "test", priority = 10, tags = {"a", "b"})
public class MyClass {
    // ...
}

// Single-element shorthand (when element is named "value")
@MyAnnotation("test")
public class SimpleClass {
    // ...
}
```

---

## Retention Policies — @Retention

The @Retention meta-annotation determines where the annotation is available:

```java
@Retention(RetentionPolicy.SOURCE)  // Only in source code
@Retention(RetentionPolicy.CLASS)   // In .class file but NOT at runtime (default)
@Retention(RetentionPolicy.RUNTIME) // Available at runtime via reflection
```

| Policy | Source | Class File | Runtime | Use Case |
|--------|--------|-----------|---------|----------|
| SOURCE | Yes | No | No | @Override, @SuppressWarnings |
| CLASS | Yes | Yes | No | Bytecode manipulation |
| RUNTIME | Yes | Yes | Yes | Frameworks, reflection |

### When to Use Each

```java
// SOURCE: Compile-time only
@Retention(RetentionPolicy.SOURCE)
public @interface Override { }

// CLASS: Post-compilation processing
@Retention(RetentionPolicy.CLASS)
public @interface GenerateBytecode { }

// RUNTIME: Framework processing via reflection
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired { }
```

---

## Target — @Target

The @Target meta-annotation specifies where the annotation can be applied:

```java
@Target(ElementType.TYPE)        // Class, interface, enum
@Target(ElementType.METHOD)      // Method
@Target(ElementType.FIELD)       // Field
@Target(ElementType.CONSTRUCTOR) // Constructor
@Target(ElementType.PARAMETER)   // Method/constructor parameter
@Target(ElementType.LOCAL_VARIABLE) // Local variable
@Target(ElementType.PACKAGE)     // Package
```

### Multiple Targets

```java
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RestController { }
```

---

## Meta-Annotations

Annotations can be applied to other annotations:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface MyAnnotation {
    String value();
}
```

### Common Meta-Annotations

| Meta-Annotation | Purpose |
|----------------|---------|
| @Retention | When annotation is available |
| @Target | Where annotation can be applied |
| @Documented | Include in Javadoc |
| @Inherited | Subclasses inherit annotation |
| @Repeatable | Annotation can be applied multiple times |

### @Inherited Deep Dive

```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Inheritable { }

@Inheritable
public class Parent { }

public class Child extends Parent { }

// With @Inherited:
Child.class.isAnnotationPresent(Inheritable.class); // true
```

**Note:** @Inherited only works with class-level annotations, not method or field annotations.

### @Repeatable Deep Dive

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Roles.class)
public @interface Role {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Roles {
    Role[] value();
}

// Usage
@Role("admin")
@Role("editor")
public class AdminUser { }

// Reading repeatable annotations
Role[] roles = AdminUser.class.getAnnotationsByType(Role.class);
```

---

## Reading Annotations at Runtime

```java
@Entity(table = "users")
public class User {
    @Column(name = "username", nullable = false)
    private String name;
    
    @Column(name = "email")
    private String email;
}

Class<?> clazz = User.class;
boolean isEntity = clazz.isAnnotationPresent(Entity.class);
Entity entity = clazz.getAnnotation(Entity.class);
String tableName = entity.table();

for (Field field : clazz.getDeclaredFields()) {
    Column column = field.getAnnotation(Column.class);
    if (column != null) {
        System.out.println(field.getName() + " -> " + column.name());
    }
}
```

---

## Complete Example: Annotation-Driven Configuration

```java
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface ConfigValue {
    String key();
    String defaultValue() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface AppConfig {
    String prefix() default "";
}

class ConfigLoader {
    private final Properties properties;
    
    ConfigLoader(Properties props) { this.properties = props; }
    
    public <T> T load(Class<T> clazz) throws Exception {
        AppConfig appConfig = clazz.getAnnotation(AppConfig.class);
        String prefix = appConfig != null ? appConfig.prefix() : "";
        
        T instance = clazz.getDeclaredConstructor().newInstance();
        
        for (Field field : clazz.getDeclaredFields()) {
            ConfigValue cv = field.getAnnotation(ConfigValue.class);
            if (cv == null) continue;
            
            String key = prefix + cv.key();
            String value = properties.getProperty(key, cv.defaultValue());
            
            field.setAccessible(true);
            
            if (field.getType() == String.class) {
                field.set(instance, value);
            } else if (field.getType() == int.class) {
                field.setInt(instance, Integer.parseInt(value));
            } else if (field.getType() == boolean.class) {
                field.setBoolean(instance, Boolean.parseBoolean(value));
            }
        }
        
        return instance;
    }
}

@AppConfig(prefix = "app.database.")
class DatabaseConfig {
    @ConfigValue(key = "host")
    private String host;
    
    @ConfigValue(key = "port", defaultValue = "5432")
    private int port;
    
    @ConfigValue(key = "ssl", defaultValue = "true")
    private boolean ssl;
}
```

---

## Production Incident: Annotation Retention Mismatch

**Incident:** A team created a @Version annotation with @Retention(RetentionPolicy.CLASS) (the default) and tried to read it at runtime via reflection. The annotation was always null.

**Root cause:** CLASS retention means the annotation is in the .class file but not available in the runtime class metadata.

**Fix:** Change to @Retention(RetentionPolicy.RUNTIME).

**Lesson:** Always specify @Retention(RetentionPolicy.RUNTIME) if you need to read annotations via reflection.

---

## Code Review Checklist

- [ ] Is @Retention explicitly specified (not relying on default CLASS)?
- [ ] Is @Target specified to restrict where the annotation can be applied?
- [ ] Are default values provided for optional elements?
- [ ] Is @Inherited used intentionally (only for class-level annotations)?
- [ ] Is @Repeatable used with a container annotation?
- [ ] Are annotation elements limited to the allowed types?
- [ ] Is @Documented included for public API annotations?

---

## Security Considerations

| Risk | Description | Mitigation |
|------|-------------|-----------|
| Information leakage | Annotations may expose secrets | Do not put secrets in annotations |
| Configuration injection | User-controlled annotation values | Validate annotation values |
| Module restrictions | Java 9+ may block annotation access | Use --add-opens if needed |

---

## Debugging Tips

1. Print `annotation.toString()` — Shows annotation name and values
2. Use `annotation.annotationType()` — Gets the annotation Class object
3. Check `isAnnotationPresent()` before `getAnnotation()` — Avoids NPE
4. Use `getAnnotationsByType()` for repeatable annotations — Returns array
5. Verify retention policy — Runtime reflection only works with RUNTIME retention

---

## Interview Questions

1. What are the three retention policies and when do you use each?
2. Can annotations have constructors?
3. What does @Inherited do?
4. What element types are allowed in annotations?
5. How do you read annotations at runtime?

---

## Summary

| Concept | Key Point |
|---------|-----------|
| @interface | Defines a new annotation type |
| @Retention | When annotation is available |
| @Target | Where annotation can be applied |
| @Inherited | Subclasses inherit class-level annotations |
| @Repeatable | Annotation can appear multiple times |
| Reading | Use reflection: getAnnotation(), isAnnotationPresent() |

---

*Next: [08 — Annotation Processing](../08-annotation-processing/README.md)*
