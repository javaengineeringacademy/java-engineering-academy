# Object Class Methods

## Key Methods to Override

### `toString()`
```java
@Override
public String toString() {
    return "Person{name='%s', age=%d}".formatted(name, age);
}
```

### `equals(Object obj)` & `hashCode()`
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

### `clone()`
```java
@Override
protected Object clone() throws CloneNotSupportedException {
    return super.clone();  // Shallow copy
}
```

## Other Methods
- `finalize()`: Deprecated (Java 9+), use try-with-resources/Cleaner
- `getClass()`: Returns runtime class
- `notify()`, `notifyAll()`, `wait()`: Thread synchronization