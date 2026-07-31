# Instance Members

## Instance Variables (Fields)
Non-static fields that belong to each object instance.

```java
public class Person {
    // Instance variables
    private String name;
    private int age;
    private Address address;
}
```

## Instance Methods
Methods that operate on instance state.

```java
public class Person {
    private String name;

    public void greet() {  // Instance method
        System.out.println("Hello, I'm " + name);
    }
}
```

## Instance Initializer Block
Code that runs before constructor, for each instance.

```java
public class Person {
    private final String id;
    private String name;

    // Instance initializer block
    {
        this.id = UUID.randomUUID().toString();
        System.out.println("Initializing Person...");
    }

    public Person(String name) {
        this.name = name;
    }
}
```

## Instance vs Static

| Aspect | Instance | Static |
|--------|----------|--------|
| Memory | Per object | Single copy |
| Access | `obj.field` | `Class.field` |
| this reference | Available | Not available |
| Override | Yes | No (hidden) |

## Instance Initializer Block Example

```java
public class Person {
    private final String id;
    private String name;

    // Instance initializer block
    {
        this.id = UUID.randomUUID().toString();
        System.out.println("Initializing Person...");
    }

    public Person(String name) {
        this.name = name;
    }
}
```

## Object Memory Layout

```
Stack (Thread)          Heap (Shared)
┌─────────────┐         ┌─────────────────────┐
│ reference: obj │────────▶ Object: Person     │
└─────────────┘         │ - name: "Alice"     │
                        │ - age: 30           │
                        └─────────────────────┘
```

## Related Topics
← [Constructors](constructors.md) | → [Static Members](static-members.md)

## References
- [Java Language Specification - Instance Members](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.3)