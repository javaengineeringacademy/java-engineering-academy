# var Type Inference - Decision Guide

## Use var When

### Type is Obvious
```java
var name = "John";  // String is obvious
var count = 42;     // int is obvious
var list = List.of(1, 2, 3);  // List<Integer> is obvious
```

### Complex Generic Types
```java
// Before
Map<String, List<Map<String, Integer>>> complexMap = new HashMap<>();

// After
var complexMap = new HashMap<String, List<Map<String, Integer>>>();
```

### Lambda Expressions
```java
// Before
Function<String, Integer> lengthFunc = s -> s.length();

// After
var lengthFunc = (Function<String, Integer>) s -> s.length();
```

### For-Each Loops
```java
// Before
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    // ...
}

// After
for (var entry : map.entrySet()) {
    // ...
}
```

## Don't Use var When

### Type is Not Obvious
```java
var result = compute();  // What type is result?
```

### Public API Signatures
Prefer explicit types for public methods.

### When Clarity is Important
```java
var x = getSomeValue();  // Bad - unclear
int x = getSomeValue();  // Good - clear
```

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| var | Concise, readable | Can be unclear |
| Explicit type | Clear | Verbose |
| Diamond operator | Limited | Only for constructors |

## Best Practices

1. **Use var for obvious types** - When type is clear from context
2. **Use var for complex types** - When explicit type would be verbose
3. **Avoid var for unclear types** - When type is not obvious
4. **Don't use var for fields** - Only for local variables
