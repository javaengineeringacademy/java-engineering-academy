# Constructors

Constructors initialize objects when they are created. They have the same name as the class and no return type.

## Constructor Types

### 1. Default Constructor
```java
class Person {
    Person() {
        System.out.println("Default constructor");
    }
}
```

### 2. Parameterized Constructor
```java
class Person {
    String name;
    int age;
    
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

### 3. Copy Constructor
```java
class Person {
    String name;
    
    Person(Person other) {
        this.name = other.name;
    }
}
```

## Constructor Overloading

Constructor overloading means having multiple constructors with **different parameter lists**.

```java
class Student {
    String name;
    int age;
    String school;
    
    // No-args constructor
    Student() {
        this("Unknown", 0, "Unknown");
    }
    
    // One-arg constructor
    Student(String name) {
        this(name, 18, "Unknown");
    }
    
    // Two-args constructor
    Student(String name, int age) {
        this(name, age, "Unknown");
    }
    
    // Full constructor
    Student(String name, int age, String school) {
        this.name = name;
        this.age = age;
        this.school = school;
    }
}
```

**Usage:**
```java
Student s1 = new Student();           // Calls no-args
Student s2 = new Student("Alice");    // Calls one-arg
Student s3 = new Student("Bob", 20);  // Calls two-args
```

## Constructor Overriding - NOT POSSIBLE

**Important:** Constructors **CANNOT be overridden** in Java!

### Why?
1. Constructors are **NOT inherited** by child classes
2. Overriding requires inheritance
3. Child class must define its own constructors

### Example:
```java
class Parent {
    Parent() {
        System.out.println("Parent constructor");
    }
}

class Child extends Parent {
    // Child MUST define its own constructor
    Child() {
        super(); // Calls parent constructor
        System.out.println("Child constructor");
    }
}
```

### What Child Can Do:
```java
class Child extends Parent {
    Child() {
        super(); // Call parent constructor (required)
        // Initialize child-specific fields
    }
}
```

## Constructor Chaining

### Using this() - Same Class
```java
class Person {
    Person() {
        this("Unknown", 0); // Calls other constructor
    }
    
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

### Using super() - Parent Class
```java
class Child extends Parent {
    Child() {
        super(); // Must be first statement
        // Initialize child
    }
}
```

## Key Points

| Concept | Description |
|---------|-------------|
| Constructor Overloading | ✅ Possible - multiple constructors with different params |
| Constructor Overriding | ❌ Not possible - constructors not inherited |
| this() | Calls another constructor in same class |
| super() | Calls parent constructor |
| private constructor | Prevents instantiation |

## Best Practices

1. Use constructor overloading for flexibility
2. Chain constructors using this() to avoid duplication
3. Always call super() if parent has no no-args constructor
4. Use copy constructor for deep copies
5. Make classes immutable with final fields
