# Methods

## Method Signature
```java
[modifiers] ReturnType methodName(ParameterType paramName) [throws Exception] {
    // body
}
```

## Method Types
| Type | Keyword | Use Case |
|------|---------|----------|
| Instance | (none) | Operates on object state |
| Static | `static` | Utility, no instance needed |
| Final | `final` | Cannot be overridden |
| Abstract | `abstract` | No body, must be implemented |
| Synchronized | `synchronized` | Thread-safe |

## Method Overloading
Same name, different parameter list (number, type, or order):
```java
public void print(String s) { ... }
public void print(int i) { ... }
public void print(String s, int count) { ... }
```

## Method Overriding
Subclass provides specific implementation:
```java
@Override
public String toString() { ... }
```

**Rules:**
- Same signature (name + parameters)
- Return type must be covariant
- Access modifier cannot be more restrictive
- Cannot override `final`, `static`, or `private` methods

## Varargs
```java
public void printAll(String... items) {
    for (String item : items) System.out.println(item);
}
// Call: printAll("a", "b", "c") or printAll(new String[]{"a","b"})
```

## Pass-by-Value
Java is ALWAYS pass-by-value:
- Primitives: copy of value
- References: copy of reference (both point to same object)