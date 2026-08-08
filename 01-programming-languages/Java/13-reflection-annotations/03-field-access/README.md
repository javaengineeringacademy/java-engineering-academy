# 03 — Field Access

## Why Field Access Matters

Field access is one of reflection's most powerful — and most dangerous — capabilities. It lets you read and write *any* field on an object, including private fields that the class never intended to expose. This capability is essential for frameworks (Spring's dependency injection, JPA's field mapping) but must be used carefully.

---

## Getting Fields

### `getDeclaredFields()` — This Class Only

```java
class User {
    public String name;
    private int age;
    private static final String TYPE = "human";
    transient private String session;
}

Field[] fields = User.class.getDeclaredFields();
for (Field f : fields) {
    System.out.printf("%s %s %s%n",
        Modifier.toString(f.getModifiers()),
        f.getType().getSimpleName(),
        f.getName());
}
// Output:
// public String name
// private int age
// private static final String TYPE
// private transient String session
```

### `getFields()` — Public Fields Only (Including Inherited)

```java
// Only returns public fields from this class AND all superclasses/interfaces
Field[] publicFields = User.class.getFields();
```

### Getting a Specific Field

```java
// By exact name — throws NoSuchFieldException if not found
Field nameField = User.class.getDeclaredField("name");
Field ageField = User.class.getDeclaredField("age");
```

---

## Reading Field Values

```java
User user = new User();
user.name = "Alice";
// age is private, can't access directly

// Get the field object
Field ageField = User.class.getDeclaredField("age");

// Must call setAccessible(true) for private fields
ageField.setAccessible(true);

// Read the value
int age = ageField.getInt(user);        // For int fields
Object nameObj = nameField.get(user);   // For reference types
String name = (String) nameObj;         // Cast to correct type

// Generic get — returns Object for reference types, autoboxed for primitives
Object value = ageField.get(user);      // Returns Integer
```

### Type-Specific Getters

| Method | Return Type | Use For |
|--------|------------|---------|
| `get(obj)` | `Object` | Reference types |
| `getInt(obj)` | `int` | `int` fields |
| `getLong(obj)` | `long` | `long` fields |
| `getDouble(obj)` | `double` | `double` fields |
| `getFloat(obj)` | `float` | `float` fields |
| `getBoolean(obj)` | `boolean` | `boolean` fields |
| `getChar(obj)` | `char` | `char` fields |
| `getShort(obj)` | `short` | `short` fields |
| `getByte(obj)` | `byte` | `byte` fields |

---

## Writing Field Values

```java
User user = new User();

Field nameField = User.class.getDeclaredField("name");
nameField.setAccessible(true);
nameField.set(user, "Bob");  // user.name = "Bob"

Field ageField = User.class.getDeclaredField("age");
ageField.setAccessible(true);
ageField.setInt(user, 25);   // user.age = 25

// Generic set — autoboxes primitives
ageField.set(user, 30);      // Autoboxes int to Integer
```

### Type-Specific Setters

| Method | Use For |
|--------|---------|
| `set(obj, value)` | Reference types |
| `setInt(obj, value)` | `int` fields |
| `setLong(obj, value)` | `long` fields |
| `setDouble(obj, value)` | `double` fields |
| `setFloat(obj, value)` | `float` fields |
| `setBoolean(obj, value)` | `boolean` fields |
| `setChar(obj, value)` | `char` fields |

---

## Static Fields

Static fields don't belong to an instance — pass `null` as the object:

```java
Field typeField = User.class.getDeclaredField("TYPE");
typeField.setAccessible(true);

// Read static field
String type = (String) typeField.get(null);

// Write static field
typeField.set(null, "robot");
```

---

## Final Fields

Java allows writing to final fields via reflection, but the behavior is implementation-dependent:

```java
class Config {
    public static final String DEFAULT_NAME = "unknown";
}

Field field = Config.class.getDeclaredField("DEFAULT_NAME");
field.setAccessible(true);

// This works in most JVMs but is NOT guaranteed by the JLS
field.set(null, "custom");

// After modification, the field may still show the old value in some contexts
// due to constant folding by the compiler
```

**Warning:** Modifying final static fields is unreliable. The compiler may inline the original value as a constant, making the change invisible in some code paths.

---

## Field Metadata

```java
Field field = User.class.getDeclaredField("age");

// Name
String name = field.getName(); // "age"

// Type
Class<?> type = field.getType(); // int.class
String typeName = type.getSimpleName(); // "int"

// Modifiers
int mods = field.getModifiers();
boolean isPrivate = Modifier.isPrivate(mods);  // true
boolean isStatic = Modifier.isStatic(mods);    // false
boolean isFinal = Modifier.isFinal(mods);      // false
boolean isTransient = Modifier.isTransient(mods); // false
boolean isVolatile = Modifier.isVolatile(mods); // false

// Declaring class (where the field is declared)
Class<?> declaringClass = field.getDeclaringClass(); // User.class

// Generic type (for parameterized fields like List<String>)
Field listField = MyClass.class.getDeclaredField("items");
Type genericType = listField.getGenericType();
if (genericType instanceof ParameterizedType) {
    ParameterizedType pt = (ParameterizedType) genericType;
    Type[] typeArgs = pt.getActualTypeArguments();
    System.out.println("Element type: " + typeArgs[0]); // String
}
```

---

## Annotations on Fields

```java
public class User {
    @JsonProperty("user_name")
    @Column(name = "username")
    @NotNull
    private String name;
    
    @JsonProperty("user_age")
    @Column(name = "age")
    @Min(0) @Max(150)
    private int age;
}

Field nameField = User.class.getDeclaredField("name");

// Check annotation presence
boolean hasJsonProperty = nameField.isAnnotationPresent(JsonProperty.class);
boolean hasNotNull = nameField.isAnnotationPresent(NotNull.class);

// Get annotation values
JsonProperty jsonProp = nameField.getAnnotation(JsonProperty.class);
String jsonName = jsonProp.value(); // "user_name"

Column column = nameField.getAnnotation(Column.class);
String columnName = column.name(); // "username"
```

---

## Accessibility Deep Dive

### `setAccessible(true)` — What It Actually Does

```java
Field field = clazz.getDeclaredField("secret");
// Before setAccessible:
// field.get(obj) → throws IllegalAccessException

field.setAccessible(true);
// After setAccessible:
// field.get(obj) → works (bypasses access checks)
```

`setAccessible(true)` does NOT make the field public. It tells the JVM to suppress the access check for this reflective operation. The field is still private in all other respects.

### When You Need `setAccessible(true)`

| Field Modifier | Need setAccessible? |
|---------------|-------------------|
| `public` | No |
| `protected` | No (same package or subclass) |
| `package-private` | No (same package) |
| `private` | Yes — always |
| `final` | Yes — and modifying may be unreliable |

### Security Implications

```java
// This breaks the class's encapsulation guarantee
Field passwordField = User.class.getDeclaredField("password");
passwordField.setAccessible(true);
String password = (String) passwordField.get(user);

// The User class expected password to be inaccessible
// Reflection bypasses this entirely
```

**Best practice:** Only use `setAccessible(true)` when you have a legitimate reason (framework, testing, serialization) and document why.

---

## Working with Arrays via Reflection

```java
// Create an array dynamically
Class<?> componentType = Class.forName("java.lang.String");
Object array = Array.newInstance(componentType, 5);

// Set elements
Array.set(array, 0, "hello");
Array.set(array, 1, "world");

// Get elements
String first = (String) Array.get(array, 0);

// Get array length
int length = Array.getLength(array);

// Get component type
Class<?> compType = array.getClass().getComponentType(); // String.class
```

---

## Complete Example: Field Copier

```java
import java.lang.reflect.*;

public class FieldCopier {

    /**
     * Copies all field values from source to target, matching by name.
     * Both classes must have fields with the same names and compatible types.
     */
    public static void copyFields(Object source, Object target) 
            throws IllegalAccessException {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        
        for (Field sourceField : sourceClass.getDeclaredFields()) {
            // Skip static fields
            if (Modifier.isStatic(sourceField.getModifiers())) continue;
            
            try {
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                
                // Check type compatibility
                if (!sourceField.getType().equals(targetField.getType())) {
                    System.out.println("Type mismatch for field: " + sourceField.getName());
                    continue;
                }
                
                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                
                Object value = sourceField.get(source);
                targetField.set(target, value);
                
            } catch (NoSuchFieldException e) {
                // Target doesn't have this field — skip silently
            }
        }
    }
}
```

---

## Production Incident: Field Access in Hot Path

**Incident:** A data processing pipeline used reflection to read fields from POJOs in a loop processing 1 million records/second. The `setAccessible(true)` call was inside the loop.

**Root cause:** Each `setAccessible(true)` call triggers a security check. In a hot loop, this overhead is catastrophic.

**Fix:**
```java
// Before: setAccessible inside loop (slow)
for (Object obj : records) {
    for (Field f : fields) {
        f.setAccessible(true); // Security check every iteration
        Object value = f.get(obj);
    }
}

// After: setAccessible outside loop (fast)
for (Field f : fields) {
    f.setAccessible(true); // Security check once
}
for (Object obj : records) {
    for (Field f : fields) {
        Object value = f.get(obj); // No security check
    }
}
```

**Result:** 8x performance improvement.

---

## Code Review Checklist

- [ ] Is `setAccessible(true)` justified, or can a getter/setter be used instead?
- [ ] Are field accesses cached (not looked up per invocation)?
- [ ] Is `setAccessible(true)` called outside hot loops?
- [ ] Are final field modifications avoided (unreliable behavior)?
- [ ] Are static fields accessed with `null` as the object?
- [ ] Is type safety maintained (using typed getters like `getInt()` vs generic `get()`)?
- [ ] Are generic field types inspected when needed (`getGenericType()`)?

---

## Security Considerations

| Risk | Description | Mitigation |
|------|-------------|-----------|
| Encapsulation bypass | Reading private fields | Limit `setAccessible` to trusted code |
| Final field mutation | Changing constants | Avoid; use configuration instead |
| Sensitive data exposure | Reading passwords/secrets | Don't expose via reflection |
| Module system violations | Accessing JDK internals | Use `--add-opens` explicitly |

---

## Debugging Tips

1. **Use `field.toString()`** — Shows full field signature with modifiers
2. **Print `field.getModifiers()` decoded** — `Modifier.toString(mods)` gives human-readable output
3. **Check `field.getDeclaringClass()`** — Confirms where the field is actually declared
4. **Use `getDeclaredFields()` not `getFields()`** — `getFields()` only returns public fields
5. **Verify type compatibility** — Use `field.getType()` before casting values

---

## Interview Questions

1. **What's the difference between `getFields()` and `getDeclaredFields()`?**
   - `getFields()`: Public fields from this class AND inherited public fields
   - `getDeclaredFields()`: All fields declared in this class (any access modifier), no inherited

2. **Why do you need `setAccessible(true)` for private fields?**
   - It suppresses the JVM's access control check, allowing reflective access to private members

3. **Can you modify a final field via reflection?**
   - Technically yes in most JVMs, but it's unreliable due to compiler constant folding

4. **How do you access a static field via reflection?**
   - Pass `null` as the instance: `field.get(null)` for reading, `field.set(null, value)` for writing

5. **What happens if you try to read a field of the wrong type?**
   - `IllegalArgumentException` if using type-specific getters, `ClassCastException` if using `get()` and casting wrong

---

## Summary

| Operation | Method | Notes |
|-----------|--------|-------|
| Get all fields | `getDeclaredFields()` | All access levels, this class only |
| Get public fields | `getFields()` | Including inherited |
| Get specific field | `getDeclaredField(name)` | Throws `NoSuchFieldException` |
| Read field | `field.get(obj)` | May need `setAccessible(true)` |
| Write field | `field.set(obj, value)` | May need `setAccessible(true)` |
| Read primitive | `field.getInt(obj)` etc. | Auto-unboxing |
| Write primitive | `field.setInt(obj, val)` etc. | Auto-boxing |
| Static field | `field.get(null)` | No instance needed |
| Field type | `field.getType()` | Returns `Class<?>` |
| Field modifiers | `field.getModifiers()` | Decode with `Modifier` |

---

*Next: [04 — Method Invocation](../04-method-invocation/README.md)*
