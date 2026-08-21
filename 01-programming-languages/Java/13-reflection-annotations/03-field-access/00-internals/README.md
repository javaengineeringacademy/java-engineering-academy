# Internals: Field Access

## How Field Access Works in the JVM

### Field Resolution

When you call `getDeclaredField("name")`, the JVM:
1. Searches the class's field table (from bytecode)
2. Matches by name and type descriptor
3. Returns a `Field` mirror object

### Access Control Check

When you call `field.get(obj)`:
1. JVM checks if the caller has access to the field
2. Access level is determined by `field.getModifiers()`
3. If denied, throws `IllegalAccessException`
4. If `setAccessible(true)` was called, the check is skipped

### Type Boxing/Unboxing

When reading/writing primitive fields via reflection:
- `field.getInt(obj)` reads and unboxes directly
- `field.get(obj)` autoboxes to `Integer`
- `field.set(obj, value)` unboxes the value

### Field Offset Optimization

The JVM uses field offsets (memory positions within the object) for direct access. Reflective access must:
1. Look up the field descriptor
2. Calculate the offset
3. Perform the access check
4. Read/write at the calculated offset

This is significantly slower than direct field access where the offset is known at compile time.

### Java 9+ Module Restrictions

The module system adds another layer:
- ` field.setAccessible(true)` may throw `InaccessibleObjectException`
- Must use `--add-opens module/package=target` JVM flag
- Or use `MethodHandles.Lookup` with appropriate access

```
field.setAccessible(true)
    ↓
Module opens package? ──YES──→ Access granted
    ↓
NO
    ↓
--add-opens used? ──YES──→ Access granted
    ↓
NO
    ↓
InaccessibleObjectException
```
