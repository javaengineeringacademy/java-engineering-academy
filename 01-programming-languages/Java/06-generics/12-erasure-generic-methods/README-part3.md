# Erasure of Generic Methods - Part 3: Advanced Topics

## 1. Varargs and Erasure

```java
// Safe varargs
@SafeVarargs
static <T> List<T> ofList(T... items) {
    return Arrays.asList(items);
}
```

## 2. Type Witness

```java
// Explicit type witness
String result = <String>identity("hello");

// Type inference (preferred)
String result = identity("hello");
```

## 3. Method Overloading with Erasure

```java
// PROBLEM: Same erasure
class Example {
    void process(List<String> list) { }
    void process(List<Integer> list) { } // ERROR: same erasure
}

// SOLUTION: Different names
class Example {
    void processStrings(List<String> list) { }
    void processIntegers(List<Integer> list) { }
}
```

## 4. Common Pitfalls

### Type Erasure Gotchas
- Cannot create `new T()`
- Cannot use `T.class`
- Cannot use `instanceof T`
- Cannot return `T` from static method

### Workarounds
```java
// Pass Class<T> for type info
static <T> T create(Class<T> clazz) {
    return clazz.newInstance();
}

// Use TypeToken for complex types
abstract class TypeToken<T> {
    Type type = getClass().getGenericSuperclass();
}
```
