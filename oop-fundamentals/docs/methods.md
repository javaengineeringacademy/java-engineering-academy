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
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public int add(int... numbers) { return Arrays.stream(numbers).sum(); }
}
```

**Rules:**
- Different parameter list (number, type, or order)
- Return type can differ
- Access modifier can differ

## Method Overriding

Subclass provides specific implementation:

```java
class Animal {
    public void makeSound() { System.out.println("Animal sound"); }
}

class Dog extends Animal {
    @Override
    public void makeSound() { System.out.println("Woof!"); }
}
```

**Rules:**
- Same signature (name + parameters)
- Return type: covariant (same or subclass)
- Access modifier: same or less restrictive
- Cannot override: `final`, `static`, `private`, `final` methods

## Varargs

```java
public void printAll(String... items) {
    for (String item : items) System.out.println(item);
}

// Calls
printAll("a", "b", "c");
printAll(new String[]{"a", "b"});
```

**Rules:**
- Only one varargs per method
- Must be last parameter
- Treated as array inside method

## Method References (Java 8+)

```java
// Static method reference
Function<String, Integer> parser = Integer::parseInt;

// Instance method reference
Consumer<String> printer = System.out::println;

// Constructor reference
Supplier<ArrayList<String>> listMaker = ArrayList::new;

// Instance method of arbitrary object
Comparator<String> cmp = String::compareToIgnoreCase;
```

## Pass-by-Value

Java is **always** pass-by-value:
- Primitives: copy of value
- Objects: copy of reference

```java
void modify(int x) { x = 20; }           // Primitive: unchanged
void modify(StringBuilder sb) { sb.append("!"); } // Object: modified!
```

## Best Practices

- Use `@Override` annotation
- Keep methods small (single responsibility)
- Prefer primitives over wrappers for performance
- Use `@Nullable`/`@NonNull` annotations
- Document exceptions with `@throws`