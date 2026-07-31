# equals() and hashCode()

## Contract

1. **Reflexive**: `x.equals(x)` → `true`
2. **Symmetric**: `x.equals(y)` ↔ `y.equals(x)`
3. **Transitive**: `x.equals(y)` && `y.equals(z)` → `x.equals(z)`
4. **Consistent**: Multiple calls return same result
5. **Null**: `x.equals(null)` → `false`

## hashCode Contract
If `a.equals(b)` then `a.hashCode() == b.hashCode()`

## Implementation Template

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyClass that = (MyClass) o;
    return primitive == that.primitive && 
           Objects.equals(reference, that.reference);
}

@Override
public int hashCode() {
    return Objects.hash(primitive, reference);
}
```

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| `==` instead of `.equals()` | Use `.equals()` |
| Override `equals` only | Override both |
| Mutable fields in hashCode | Use immutable fields only |
| Not checking null/class | Add null and class checks |