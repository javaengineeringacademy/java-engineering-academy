# Raw Types - Part 3: Advanced Topics

## 1. Raw Types in APIs

```java
// Legacy API
public void process(List list) { ... }

// Modern API
public void process(List<?> list) { ... }
```

## 2. Type Safety Comparison

| Aspect | Raw Type | Parameterized |
|--------|----------|---------------|
| Compile-time check | No | Yes |
| Runtime check | No | No (erased) |
| Casts required | Yes | Compiler inserts |
| Type safety | Unsafe | Safe |

## 3. Migration Strategy

### Step 1: Identify Raw Types
```bash
grep -r "List " src/
```

### Step 2: Add Type Parameters
```java
// Before
List list = new ArrayList();

// After
List<Object> list = new ArrayList<>();
```

### Step 3: Verify No Unchecked Warnings
```bash
javac -Xlint:unchecked *.java
```

## 4. Common Patterns

### Unknown Type
```java
// Bad
List list;

// Good
List<?> list;
List<Object> list;
```

### Legacy Integration
```java
// Wrapper for raw API
<T> List<T> wrapRaw(List raw) {
    return (List<T>) raw;
}
```
