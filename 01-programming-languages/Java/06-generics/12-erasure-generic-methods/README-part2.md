# Erasure of Generic Methods - Part 2: Deep Dive

## 1. Method Erasure Rules

### Unbounded Method
```java
// Source
static <T> T identity(T value) { return value; }

// After Erasure
static Object identity(Object value) { return value; }
```

### Bounded Method
```java
// Source
static <T extends Comparable<T>> T max(T a, T b) { ... }

// After Erasure
static Comparable max(Comparable a, Comparable b) { ... }
```

## 2. Multiple Type Parameters

```java
// Source
static <T, R> R convert(T input, Function<T, R> mapper) { ... }

// After Erasure
static Object convert(Object input, Function mapper) { return mapper.apply(input); }
```

## 3. Method Signature Erasure

| Source | Erased Signature |
|--------|------------------|
| `<T> T get()` | `Object get()` |
| `<T extends X> T get()` | `X get()` |
| `<T> void set(T)` | `void set(Object)` |
| `<T> T process(List<T>)` | `Object process(List)` |

## 4. Bridge Methods for Overriding

```java
class Parent {
    <T> T get() { return null; }
}

class Child extends Parent {
    @Override
    String get() { return "hello"; }
}

// Compiler adds bridge method
class Child extends Parent {
    String get() { return "hello"; }
    Object get() { return get(); } // Bridge
}
```
