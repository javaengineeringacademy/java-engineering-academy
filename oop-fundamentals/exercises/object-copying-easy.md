# Object Copying - Easy Exercises

## Exercise 1: Copy Constructor for Person Class

### Problem Statement
Create a `Person` class with a copy constructor that creates a deep copy of the object.

### Requirements
1. Create a `Person` class with fields: `name` (String), `age` (int), `email` (String)
2. Implement a default constructor and a parameterized constructor
3. Implement a copy constructor that creates a deep copy
4. Add getters and setters
5. Override `toString()` method

### Starter Code
```java
public class Person {
    private String name;
    private int age;
    private String email;

    // Default constructor
    public Person() {
    }

    // Parameterized constructor
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // TODO: Implement copy constructor

    // Getters and setters

    // Override toString()
}
```

### Expected Behavior
```java
Person original = new Person("Alice", 30, "alice@email.com");
Person copy = new Person(original);

// Modifying copy should not affect original
copy.setName("Bob");
System.out.println(original.getName()); // Should print "Alice"
System.out.println(copy.getName());     // Should print "Bob"
```

### Hints
- Use `this()` to call the parameterized constructor
- Remember that Strings are immutable in Java, so direct assignment is safe

### Evaluation Criteria
- [ ] Copy constructor correctly creates a new object
- [ ] Modifying the copy doesn't affect the original
- [ ] All fields are properly copied
- [ ] Code follows Java naming conventions

---

## Exercise 2: Shallow Clone for Address Class

### Problem Statement
Implement the `Cloneable` interface and create a shallow clone method for an `Address` class.

### Requirements
1. Create an `Address` class with fields: `street` (String), `city` (String), `coordinates` (double array)
2. Implement `Cloneable` interface
3. Override `clone()` method
4. Create a shallow clone that copies primitive and immutable fields

### Starter Code
```java
public class Address implements Cloneable {
    private String street;
    private String city;
    private double[] coordinates; // [latitude, longitude]

    public Address(String street, String city, double latitude, double longitude) {
        this.street = street;
        this.city = city;
        this.coordinates = new double[]{latitude, longitude};
    }

    // TODO: Override clone() method

    // Getters

    // Override toString()
}
```

### Expected Behavior
```java
Address original = new Address("123 Main St", "Springfield", 39.7817, -89.6501);
Address clone = original.clone();

// Primitive and immutable fields are independent
clone.setStreet("456 Oak Ave");
System.out.println(original.getStreet()); // Should print "123 Main St"

// But coordinates array is shared (shallow clone)
clone.getCoordinates()[0] = 40.0;
System.out.println(original.getCoordinates()[0]); // Should print 40.0 (shared!)
```

### Hints
- Call `super.clone()` to create the clone
- Handle `CloneNotSupportedException` with try-catch or declare it
- Remember: arrays are objects, so shallow clone shares the reference

### Evaluation Criteria
- [ ] Class implements `Cloneable` interface
- [ ] `clone()` method properly overrides `Object.clone()`
- [ ] Primitive and String fields are independent
- [ ] Array reference is shared (shallow copy behavior)

---

## Exercise 3: Reference Copy vs Object Copy

### Problem Statement
Understand the difference between creating a reference copy and an object copy.

### Requirements
1. Create a `Car` class with fields: `brand` (String), `model` (String), `year` (int)
2. Create a method `createReferenceCopy()` that returns a reference to the same object
3. Create a method `createObjectCopy()` that returns a new independent object
4. Demonstrate the difference in behavior

### Starter Code
```java
public class Car {
    private String brand;
    private String model;
    private int year;

    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // TODO: Implement createReferenceCopy()

    // TODO: Implement createObjectCopy() using copy constructor

    // Getters and setters

    // Override toString()
}
```

### Expected Behavior
```java
Car original = new Car("Toyota", "Camry", 2023);

Car refCopy = original.createReferenceCopy();
Car objCopy = original.createObjectCopy();

// Reference copy points to same object
refCopy.setBrand("Honda");
System.out.println(original.getBrand()); // Should print "Honda" (changed!)

// Object copy is independent
objCopy.setBrand("Ford");
System.out.println(original.getBrand()); // Should print "Honda" (unchanged)
```

### Hints
- Reference copy simply returns `this`
- Object copy creates a new instance using the copy constructor
- Both methods should return `Car` type

### Evaluation Criteria
- [ ] `createReferenceCopy()` returns `this`
- [ ] `createObjectCopy()` returns a new object
- [ ] Reference copy changes affect original
- [ ] Object copy changes don't affect original
