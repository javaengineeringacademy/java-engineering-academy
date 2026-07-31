# Object Class Methods

## Key Methods to Override

### toString()
```java
@Override
public String toString() {
    return "Person{name='%s', age=%d}".formatted(name, age);
}
```

### equals(Object obj) & hashCode()
**Contract:** If `a.equals(b)` then `a.hashCode() == b.hashCode()`

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Person person = (Person) obj;
    return age == person.age && Objects.equals(name, person.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### clone()
```java
@Override
protected Person clone() throws CloneNotSupportedException {
    return (Person) super.clone();  // Shallow copy
}
```

## Other Methods

### finalize() (Deprecated)
```java
@Deprecated(since = "9", forRemoval = true)
@Override
protected void finalize() throws Throwable {
    try {
        // Cleanup
    } finally {
        super.finalize();
    }
}
```

**Use try-with-resources or Cleaner instead.**

### getClass()
Returns runtime class of object.

### notify(), notifyAll(), wait()
Thread synchronization methods.

## Best Practices

1. **Always override both** `equals` and `hashCode`
2. **Use `Objects.hash()`** for hashCode
3. **Use `Objects.equals()`** for null-safe comparison
4. **Make `toString()` informative**
5. **Avoid `finalize()`** - use try-with-resources/Cleaner