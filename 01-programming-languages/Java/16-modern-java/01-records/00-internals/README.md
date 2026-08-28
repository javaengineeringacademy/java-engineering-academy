# Records Internals

## Bytecode Generation

When you declare a record, the compiler generates:

### 1. Final Class
```java
// Your record:
record Point(int x, int y) {}

// Compiler generates (conceptually):
public final class Point extends Record {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int x() { return x; }
    public int y() { return y; }
    
    @Override
    public boolean equals(Object o) { ... }
    
    @Override
    public int hashCode() { ... }
    
    @Override
    public String toString() { ... }
}
```

### 2. Generated Methods

| Method | Implementation |
|--------|----------------|
| `equals()` | Compares all components using `Objects.equals()` |
| `hashCode()` | Uses `Objects.hash()` with all components |
| `toString()` | Returns `RecordName[comp1=val1, comp2=val2]` |
| `accessor` | Returns the corresponding field value |

### 3. Constructor

The canonical constructor is a plain constructor (no assignments needed in compact form).

## Memory Layout

Records follow the same memory layout as regular classes:
- Object header (12-16 bytes)
- Instance fields in declaration order
- Padding for alignment

## Serialization

Records implement `Serializable` automatically if they implement the interface. The serialization mechanism uses the canonical constructor.

## Reflection

Records can be introspected using:
- `Record.class.getComponentNames()` (Java 16+)
- `record.getClass().getRecordComponents()` (Java 16+)
