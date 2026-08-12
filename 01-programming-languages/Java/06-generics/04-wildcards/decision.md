# Decision Guide: Wildcards

## Key Engineering Decisions

### Decision 1: Wildcards vs Type Parameters

```
Do you need to reference the type inside the method body?
│
├─ YES (need to create, store, or return values of type T)
│  → Use type parameter: <T>
│
└─ NO (just reading or passing through)
   → Use wildcard: <?>, <? extends T>, <? super T>
```

| Scenario | Use | Example |
|----------|-----|---------|
| Method returns values of type T | `<T>` | `T max(List<T> list)` |
| Method adds values to collection | `<T>` | `void add(List<T> list, T item)` |
| Method only reads from collection | `<? extends T>` | `double sum(List<? extends Number> list)` |
| Method only writes to collection | `<? super T>` | `void addNumbers(List<? super Integer> list)` |
| Method just iterates | `<?>` | `void print(List<?> list)` |

### Decision 2: ? extends vs ? super

```
Is the collection a SOURCE (reading from it)?
│
├─ YES → Use <? extends T> (Producer Extends)
│
└─ NO → Is the collection a SINK (writing to it)?
   │
   ├─ YES → Use <? super T> (Consumer Super)
   │
   └─ NO → Use <?> or <T>
```

| Role | Syntax | Real-World Example |
|------|--------|-------------------|
| Producer | `<? extends T>` | `Collections.copy(dest, src)` — src produces items |
| Consumer | `<? super T>` | `Collections.copy(dest, src)` — dest consumes items |
| Both | `<T>` | `Arrays.sort(array)` — reads and writes |

### Decision 3: Wildcards in Parameters vs Return Types

```
Should the method return a wildcard type?
│
├─ YES → Almost never. Use a type parameter instead.
│
└─ NO → Should the parameter use wildcards?
   │
   ├─ YES → If method only reads or only writes
   │
   └─ NO → Use type parameter if method does both
```

**Rule:** Never use wildcards in return types. Wildcards in return types force callers to deal with unknown types.

### Decision 4: PECS (Producer Extends, Consumer Super)

```
What does the collection do in the method?
│
├─ PROVIDES data (Producer) → Use <? extends T>
│
├─ ACCEPTS data (Consumer) → Use <? super T>
│
└─ BOTH → Use <T> (type parameter)
```

**Examples:**

```java
// Producer: reading from list
public static double sum(List<? extends Number> list) { ... }

// Consumer: writing to list
public static void addNumbers(List<? super Integer> list, int n) { ... }

// Both: reading and writing
public static <T> void swap(List<T> list, int i, int j) { ... }
```

## Comparison Matrix

| Factor | `<?>` | `<? extends T>` | `<? super T>` | `<T>` |
|--------|-------|-----------------|---------------|-------|
| Read | Objects | T | Objects | T |
| Write | Nothing | Nothing | T or subtypes | T |
| Flexibility | High | Medium | Medium | Low |
| Use case | Iterate | Source | Sink | Full access |

## When to Use Each

### Use `<?>` when:
- Method doesn't care about the type
- Just iterating or printing
- Method is truly type-agnostic

### Use `<? extends T>` when:
- Method reads from collection
- Method returns values from collection
- You need a specific upper bound for reading

### Use `<? super T>` when:
- Method writes to collection
- Method accepts values for storage
- You need flexibility for what types can be stored

### Use `<T>` when:
- Method both reads and writes
- Method needs to reference the type
- Method creates new instances of the type

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| `List<?>` when you add items | Can't add anything (except null) | Use `<T>` or `? super T` |
| `List<? extends Number>` and add Integer | Can't add to `? extends` | Use `List<Number>` or `? super Number` |
| Wildcard in return type | Callers lose type safety | Use type parameter `<T>` |
| Using `?` when you need to reference type | Can't use `?` as type | Use `<T>` to capture |

## Code Review Checklist

- [ ] Wildcards used correctly (PECS principle)
- [ ] No wildcards in return types
- [ ] `? super T` used for consumers (methods that add elements)
- [ ] `? extends T` used for producers (methods that read elements)
- [ ] `<T>` used when method both reads and writes
