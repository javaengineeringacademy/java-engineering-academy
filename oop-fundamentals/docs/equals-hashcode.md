# equals() and hashCode()

## Contract
1. **Reflexive**: `x.equals(x)` → `true`
2. **Symmetric**: `x.equals(y)` ↔ `y.equals(x)`
3. **Transitive**: `x.equals(y)` && `y.equals(z)` → `x.equals(z)`
4. **Consistent**: Multiple calls return same result
5. **Null**: `x.equals(null)` → `false`

## Implementation Template
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyClass that = (MyClass) o;
    return primitiveField == that.primitiveField &&
           Objects.equals(referenceField, that.referenceField);
}

@Override
public int hashCode() {
    return Objects.hash(primitiveField, referenceField);
}
```

## Common Mistakes
- Using `==` instead of `.equals()` for objects
- Overriding `equals` but not `hashCode`
- Using mutable fields in `hashCode`
- Not checking `null` or class type