# var Type Inference References

## Official Documentation

- [JEP 286: Local-Variable Type Inference](https://openjdk.org/jeps/286)
- [Java Language Specification - Local Variable Type Inference](https://docs.oracle.com/javase/specs/jls/se17/html/jls-14.html#jls-14.4)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Type Inference | Compiler determines the type |
| Initializer | Required for var declaration |
| Local Variable | var is only for local variables |
| Effectively Final | var variables are effectively final |

## Code Examples

### Basic var
```java
var x = 10;          // int
var name = "Hello";  // String
var list = List.of(1, 2, 3);  // List<Integer>
```

### Complex Types
```java
var map = new HashMap<String, List<Integer>>();
var stream = list.stream().filter(i -> i > 2);
```

### For-Each Loops
```java
for (var entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

### Lambda Expressions
```java
var lengthFunc = (Function<String, Integer>) s -> s.length();
```

## Common Patterns

1. **Obvious types:** `var name = "John";`
2. **Complex generics:** `var map = new HashMap<String, List<Integer>>();`
3. **For-each:** `for (var item : list)`
4. **Lambda:** `var func = (Function<String, Integer>) s -> s.length();`
