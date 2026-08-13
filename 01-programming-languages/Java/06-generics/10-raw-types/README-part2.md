# Raw Types - Part 2: Deep Dive

## 1. What Are Raw Types?

Raw types are parameterized types used without their type arguments.

```java
// Parameterized (correct)
List<String> list = new ArrayList<>();

// Raw type (unsafe)
List rawList = new ArrayList();
```

## 2. Why Raw Types Exist

- Backward compatibility with pre-generics Java code
- Allows gradual migration to generics
- Legacy API integration

## 3. Raw Type Conversion

### Parameterized to Raw
```java
List<String> typed = new ArrayList<>();
List raw = typed; // Allowed (unchecked warning)
```

### Raw to Parameterized
```java
List raw = new ArrayList();
List<String> typed = raw; // Allowed (unchecked warning)
```

## 4. Unchecked Warnings

```java
@SuppressWarnings("unchecked")
void processRaw(List list) {
    String s = (String) list.get(0); // Cast needed
}
```

## 5. Best Practices

- Avoid raw types in new code
- Use `List<?>` instead of `List` if type unknown
- Add `@SuppressWarnings` for legacy code
- Migrate gradually to parameterized types
