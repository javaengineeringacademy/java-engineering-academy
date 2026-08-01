# Serialization - Easy Exercises

## Exercise 1: Serialize and Deserialize a Student Object

### Problem Statement
Implement serialization and deserialization for a `Student` class.

### Requirements
1. Create a `Student` class implementing `Serializable`
2. Fields: `rollNumber` (int), `name` (String), `gpa` (double), `isActive` (boolean)
3. Implement methods to save (serialize) and load (deserialize) the object
4. Handle serialization exceptions properly

### Starter Code
```java
import java.io.*;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rollNumber;
    private String name;
    private double gpa;
    private boolean isActive;

    public Student(int rollNumber, String name, double gpa, boolean isActive) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.gpa = gpa;
        this.isActive = isActive;
    }

    // TODO: Implement saveToFile(String filename) method

    // TODO: Implement loadFromFile(String filename) method

    // Getters and setters

    @Override
    public String toString() {
        return "Student{" +
                "rollNumber=" + rollNumber +
                ", name='" + name + '\'' +
                ", gpa=" + gpa +
                ", isActive=" + isActive +
                '}';
    }
}
```

### Expected Behavior
```java
Student original = new Student(1, "Alice", 3.8, true);
System.out.println("Original: " + original);

// Serialize to file
original.saveToFile("student.dat");
System.out.println("Saved to file");

// Deserialize from file
Student loaded = Student.loadFromFile("student.dat");
System.out.println("Loaded: " + loaded);

// Verify equality
System.out.println("Roll numbers match: " + (original.getRollNumber() == loaded.getRollNumber()));
System.out.println("Names match: " + original.getName().equals(loaded.getName()));
```

### Hints
- Use `ObjectOutputStream` for serialization
- Use `ObjectInputStream` for deserialization
- Use try-with-resources to automatically close streams
- Wrap exceptions in meaningful error messages

### Evaluation Criteria
- [ ] Student class implements Serializable
- [ ] `serialVersionUID` is defined
- [ ] `saveToFile()` correctly serializes the object
- [ ] `loadFromFile()` correctly deserializes the object
- [ ] Exceptions are handled properly
- [ ] Loaded object has same values as original

---

## Exercise 2: Use Transient to Exclude Fields

### Problem Statement
Understand how to use the `transient` keyword to exclude specific fields from serialization.

### Requirements
1. Create an `Employee` class with sensitive and non-sensitive fields
2. Mark sensitive fields as `transient`
3. Demonstrate that transient fields are not serialized
4. Verify behavior after deserialization

### Starter Code
```java
import java.io.*;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private double salary;
    private transient String password;        // Should not be serialized
    private transient String secretKey;       // Should not be serialized
    private String department;                // Should be serialized

    public Employee(int id, String name, double salary, String password, String secretKey, String department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.password = password;
        this.secretKey = secretKey;
        this.department = department;
    }

    // TODO: Implement serialization method

    // TODO: Implement deserialization method

    // Getters for all fields

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", password='" + password + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
```

### Expected Behavior
```java
Employee original = new Employee(1, "Alice", 75000, "secret123", "key456", "Engineering");
System.out.println("Original: " + original);

// Serialize
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("employee.dat"));
oos.writeObject(original);
oos.close();

// Deserialize
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("employee.dat"));
Employee loaded = (Employee) ois.readObject();
ois.close();

System.out.println("Loaded: " + loaded);

// Non-transient fields should be preserved
System.out.println("Name preserved: " + original.getName().equals(loaded.getName()));
System.out.println("Department preserved: " + original.getDepartment().equals(loaded.getDepartment()));

// Transient fields should be null
System.out.println("Password is null: " + (loaded.getPassword() == null));
System.out.println("Secret key is null: " + (loaded.getSecretKey() == null));
```

### Hints
- Transient fields default to `null` for objects, `0` for primitives
- You can verify by printing the object before and after serialization
- Check the file size to confirm less data is written

### Evaluation Criteria
- [ ] Sensitive fields are marked as `transient`
- [ ] Transient fields are `null` after deserialization
- [ ] Non-transient fields preserve their values
- [ ] Code demonstrates understanding of transient keyword

---

## Exercise 3: Serializable vs Externalizable

### Problem Statement
Compare the `Serializable` interface with `Externalizable` interface.

### Requirements
1. Create two versions of the same class:
   - `ProductSerializable` implementing `Serializable`
   - `ProductExternalizable` implementing `Externalizable`
2. Implement custom serialization logic for `Externalizable` version
3. Compare the behavior and performance

### Starter Code
```java
import java.io.*;

// Version 1: Using Serializable
public class ProductSerializable implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private double price;

    public ProductSerializable(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters and setters

    @Override
    public String toString() {
        return "ProductSerializable{id=" + id + ", name='" + name + "', price=" + price + '}';
    }
}

// Version 2: Using Externalizable
public class ProductExternalizable implements Externalizable {
    private int id;
    private String name;
    private double price;

    // Required no-arg constructor
    public ProductExternalizable() {
    }

    public ProductExternalizable(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // TODO: Implement writeExternal() method

    // TODO: Implement readExternal() method

    // Getters and setters

    @Override
    public String toString() {
        return "ProductExternalizable{id=" + id + ", name='" + name + "', price=" + price + '}';
    }
}
```

### Expected Behavior
```java
// Test Serializable version
ProductSerializable p1 = new ProductSerializable(1, "Laptop", 999.99);
// Serialize and deserialize...

// Test Externalizable version
ProductExternalizable p2 = new ProductExternalizable(1, "Laptop", 999.99);
// Serialize and deserialize...

// Compare:
// 1. File sizes
// 2. Execution time
// 3. Control over serialization process
```

### Hints
- `Externalizable` requires a public no-arg constructor
- `writeExternal()` and `readExternal()` give you full control
- You can choose which fields to write and in what order
- `Externalizable` is generally faster but more complex

### Evaluation Criteria
- [ ] Both classes are implemented correctly
- [ ] `Externalizable` version has public no-arg constructor
- [ ] `writeExternal()` and `readExternal()` are implemented
- [ ] Comparison analysis is documented
- [ ] Understanding of trade-offs is demonstrated
