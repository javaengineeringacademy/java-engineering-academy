# Erasure of Generic Types - Part 2: Deep Dive

## 1. Type Erasure Process

### Step-by-Step
```java
// Source
class Container<T> {
    T value;
    T getValue() { return value; }
}

// After Erasure
class Container {
    Object value;
    Object getValue() { return value; }
}
```

### Bounded Type Erasure
```java
// Source
class Container<T extends Number> {
    T value;
}

// After Erasure
class Container {
    Number value;  // T replaced with bound
}
```

## 2. Bridge Methods

```java
// Source
class Child extends Parent<String> {
    @Override
    public String get() { return "hello"; }
}

// After Erasure
class Child extends Parent {
    public String get() { return "hello"; }
    // Compiler adds:
    public Object get() { return get(); } // Bridge method
}
```

## 3. Cast Insertion

```java
// Source
String s = container.getValue();

// After Erasure
String s = (String) container.getValue(); // Cast inserted
```

## 4. Runtime Type Checking

```java
// Cannot do
obj instanceof Container<String> // ERROR

// Can do
obj instanceof Container<?> // OK
obj instanceof Container // OK (raw type)
```
