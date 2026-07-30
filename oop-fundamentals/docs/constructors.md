# Constructors

## Types of Constructors
1. **No-arg Constructor**: `public ClassName() { ... }`
2. **Parameterized Constructor**: `public ClassName(Type param) { ... }`
3. **Copy Constructor**: `public ClassName(ClassName other) { ... }`
4. **Builder Pattern**: For complex objects with many optional fields

## Constructor Rules
- Same name as class
- No return type (not even void)
- Can be overloaded
- If no constructor defined → compiler provides default no-arg

## Constructor Chaining
```java
public class Person {
    private String name;
    private int age;
    private String address;

    // Primary constructor
    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Chaining to primary
    public Person(String name, int age) {
        this(name, age, "Unknown");
    }

    public Person(String name) {
        this(name, 0);
    }
}
```

## Copy Constructor
```java
public Person(Person other) {
    this.name = other.name;
    this.age = other.age;
    this.address = other.address;
}
```

## Best Practices
- Validate parameters in constructors
- Use `final` for immutable fields
- Prefer constructor over setters for required fields
- Keep constructors simple; delegate complex logic to factory methods