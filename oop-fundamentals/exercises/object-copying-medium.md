# Object Copying - Medium Exercises

## Exercise 1: Deep Clone for Department Class

### Problem Statement
Implement a deep clone for a `Department` class that contains a list of `Employee` objects.

### Requirements
1. Create an `Employee` class with fields: `id` (int), `name` (String), `salary` (double)
2. Create a `Department` class with fields: `name` (String), `employees` (List<Employee>)
3. Implement `Cloneable` interface for both classes
4. Create a deep clone that copies all nested objects

### Starter Code
```java
import java.util.ArrayList;
import java.util.List;

public class Employee implements Cloneable {
    private int id;
    private String name;
    private double salary;

    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    // TODO: Implement clone()

    // Getters and setters
}

public class Department implements Cloneable {
    private String name;
    private List<Employee> employees;

    public Department(String name, List<Employee> employees) {
        this.name = name;
        this.employees = new ArrayList<>(employees);
    }

    // TODO: Implement deep clone()

    // Getters
}
```

### Expected Behavior
```java
List<Employee> empList = new ArrayList<>();
empList.add(new Employee(1, "Alice", 50000));
empList.add(new Employee(2, "Bob", 60000));

Department original = new Department("Engineering", empList);
Department clone = original.clone();

// Modifying clone's employee list doesn't affect original
clone.getEmployees().get(0).setName("Charlie");
System.out.println(original.getEmployees().get(0).getName()); // Should print "Alice"
```

### Hints
- Clone each employee individually when cloning the department
- Use a loop to create new Employee objects in the cloned list
- Don't forget to clone the list structure itself

### Evaluation Criteria
- [ ] Employee class implements Cloneable
- [ ] Department class implements Cloneable
- [ ] Deep clone creates new list with new employee objects
- [ ] Modifying cloned employees doesn't affect originals

---

## Exercise 2: Builder-Based Copy Pattern

### Problem Statement
Create a builder pattern for copying objects with the ability to modify specific fields during copy.

### Requirements
1. Create a `Product` class with fields: `id` (String), `name` (String), `price` (double), `quantity` (int)
2. Create a `ProductBuilder` inner class
3. Implement a `copy()` method that returns a builder pre-filled with current object's values
4. Allow overriding specific fields during copy

### Starter Code
```java
public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;

    public Product(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // TODO: Implement copy() method returning ProductBuilder

    // Getters

    // Inner class ProductBuilder
    public static class ProductBuilder {
        // TODO: Implement builder with pre-filled values

        // TODO: Implement build() method returning Product
    }
}
```

### Expected Behavior
```java
Product original = new Product("P001", "Laptop", 999.99, 10);

// Copy with modification
Product modifiedCopy = original.copy()
    .price(899.99)
    .quantity(5)
    .build();

System.out.println(original.getPrice());  // Should print 999.99
System.out.println(modifiedCopy.getPrice()); // Should print 899.99
System.out.println(modifiedCopy.getName());  // Should print "Laptop" (unchanged)
```

### Hints
- The `copy()` method should create a builder with all current values
- Builder methods should return `this` for method chaining
- Handle null values appropriately

### Evaluation Criteria
- [ ] `copy()` method returns a pre-filled builder
- [ ] Builder allows modifying individual fields
- [ ] `build()` creates a new Product object
- [ ] Original object remains unchanged
- [ ] Method chaining works correctly

---

## Exercise 3: Deep Copy with Different Nested Object Types

### Problem Statement
Implement deep copy for a complex object containing various nested types including arrays, lists, and custom objects.

### Requirements
1. Create an `Address` class (Street, City, ZipCode)
2. Create a `ContactInfo` class containing `Address`, phone number, and email
3. Create a `Person` class containing `ContactInfo`, hobbies (String array), and friends (List<Person>)
4. Implement deep copy that handles all nested types correctly

### Starter Code
```java
import java.util.ArrayList;
import java.util.List;

public class Address {
    private String street;
    private String city;
    private String zipCode;

    // Constructor, getters, clone method
}

public class ContactInfo {
    private Address address;
    private String phone;
    private String email;

    // Constructor, getters, clone method
}

public class Person {
    private String name;
    private ContactInfo contactInfo;
    private String[] hobbies;
    private List<Person> friends;

    // Constructor, getters, clone method
}
```

### Expected Behavior
```java
Address addr = new Address("123 Main St", "Springfield", "62701");
ContactInfo contact = new ContactInfo(addr, "555-0123", "alice@email.com");
String[] hobbies = {"Reading", "Hiking"};
List<Person> friends = new ArrayList<>();

Person original = new Person("Alice", contact, hobbies, friends);
Person clone = original.clone();

// Modify clone's nested objects
clone.getContactInfo().getAddress().setCity("Chicago");
clone.getHobbies()[0] = "Swimming";

// Original should be unchanged
System.out.println(original.getContactInfo().getAddress().getCity()); // Should print "Springfield"
System.out.println(original.getHobbies()[0]); // Should print "Reading"
```

### Hints
- Clone each nested object individually
- For arrays, use `Arrays.copyOf()` or loop to copy elements
- For lists, create new list and clone each element
- Handle null values to avoid NullPointerException

### Evaluation Criteria
- [ ] Address class has working clone method
- [ ] ContactInfo class has working clone method
- [ ] Person class deep clones all nested objects
- [ ] Array is deep copied (new array with copied elements)
- [ ] List is deep copied (new list with cloned elements)
- [ ] Null values are handled gracefully
