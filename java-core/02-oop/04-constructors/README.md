# Constructors

A constructor in Java is a special block of code that is called when an object is created. It initializes the object's state and allocates memory for its fields. Constructors have the same name as the class and do not have a return type, not even `void`. They are used to set initial values for object attributes and perform any setup needed when an object is instantiated.

## Learning Objectives
By the end of this topic, you will be able to:
- Understand the purpose and types of constructors in Java
- Implement default, parameterized, and copy constructors
- Apply constructor chaining and the `this()` and `super()` calls
- Follow best practices for constructor design

## Prerequisites
- [Objects](../03-objects/)
- Understanding of Java classes and objects
- Basic knowledge of methods and parameters

## Why This Concept Exists

### The Problem
Without constructors, objects would be created with default values (null for objects, 0 for numbers, false for booleans). This leads to:
- **Uninitialized state**: Objects may be in invalid states after creation
- **Redundant initialization code**: Similar initialization code repeated across methods
- **No validation**: No way to ensure objects are properly configured when created

### The Solution
Constructors solve these problems by:
- **Guaranteeing initialization**: Objects are properly initialized when created
- **Providing multiple ways to create objects**: Different constructors for different initialization needs
- **Enforcing invariants**: Validation logic ensures objects are in valid states
- **Reducing code duplication**: Common initialization logic is centralized

### Real-World Analogy
Think of a constructor as a factory assembly line. When a product (object) is manufactured (created), the assembly line (constructor) ensures all components are properly installed and the product is ready for use. Different assembly lines (constructors) can create different versions of the same product.

## Internal Working

### JVM Perspective
When a constructor is called:
1. **Memory Allocation**: JVM allocates memory for the object on the heap
2. **Reference Creation**: A reference to the object is created on the stack
3. **Constructor Execution**: The constructor body is executed
4. **Field Initialization**: Instance variables are initialized
5. **Object Ready**: The object is now ready for use

### Memory Representation
```java
public class Student {
    String name;
    int age;
    
    // Default constructor
    public Student() {
        this.name = "Unknown";
        this.age = 0;
    }
    
    // Parameterized constructor
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

// Object creation
Student s1 = new Student();           // Calls default constructor
Student s2 = new Student("Alice", 20); // Calls parameterized constructor

// Memory layout:
// Stack:
//   s1 -> reference to Student object at 0x1000
//   s2 -> reference to Student object at 0x1008
// Heap:
//   0x1000: Student {name: "Unknown", age: 0}
//   0x1008: Student {name: "Alice", age: 20}
```

## Syntax

```java
// Default constructor (no parameters)
public ClassName() {
    // Initialization code
}

// Parameterized constructor
public ClassName(parameter1, parameter2, ...) {
    this.field1 = parameter1;
    this.field2 = parameter2;
}

// Copy constructor
public ClassName(ClassName other) {
    this.field1 = other.field1;
    this.field2 = other.field2;
}

// Constructor with validation
public ClassName(parameter) {
    if (parameter == null) {
        throw new IllegalArgumentException("Parameter cannot be null");
    }
    this.field = parameter;
}

// Constructor chaining with this()
public ClassName() {
    this("default", 0); // Calls parameterized constructor
}

// Constructor chaining with super()
public SubClass() {
    super(); // Calls parent constructor
}
```

## Easy Examples

### Example 1: Basic Constructors
**Problem Statement:**
Create a Student class with default and parameterized constructors.

**Implementation:**
```java
package academy.javaengineering.oop.constructors;

public class Student {
    private String name;
    private int age;
    private String major;
    
    // Default constructor
    public Student() {
        this.name = "Unknown";
        this.age = 18;
        this.major = "Undeclared";
        System.out.println("Default constructor called");
    }
    
    // Parameterized constructor with two parameters
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        this.major = "Undeclared";
        System.out.println("Two-parameter constructor called for " + name);
    }
    
    // Parameterized constructor with three parameters
    public Student(String name, int age, String major) {
        this.name = name;
        this.age = age;
        this.major = major;
        System.out.println("Three-parameter constructor called for " + name);
    }
    
    // Display method
    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age + ", Major: " + major);
    }
    
    public static void main(String[] args) {
        System.out.println("Creating students...");
        System.out.println();
        
        // Create objects using different constructors
        Student student1 = new Student();
        Student student2 = new Student("Alice", 20);
        Student student3 = new Student("Bob", 22, "Computer Science");
        
        System.out.println();
        
        // Display student information
        student1.displayInfo();
        student2.displayInfo();
        student3.displayInfo();
    }
}
```

**Expected Output:**
```
Creating students...

Default constructor called
Two-parameter constructor called for Alice
Three-parameter constructor called for Bob

Name: Unknown, Age: 18, Major: Undeclared
Name: Alice, Age: 20, Major: Undeclared
Name: Bob, Age: 22, Major: Computer Science
```

**Best Practices:**
- Provide a default constructor if no other constructors are defined
- Use parameterized constructors to enforce required values
- Use `this` keyword to distinguish between parameters and fields

### Example 2: Constructor with Validation
**Problem Statement:**
Create a BankAccount class with constructor validation to ensure valid initial state.

**Implementation:**
```java
package academy.javaengineering.oop.constructors;

public class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;
    private boolean isActive;
    
    // Constructor with validation
    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        // Validate account number
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        
        // Validate owner name
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be null or empty");
        }
        
        // Validate initial balance
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.isActive = true;
        
        System.out.println("Account created for " + ownerName + 
                          " with balance $" + balance);
    }
    
    // Convenience constructor with zero balance
    public BankAccount(String accountNumber, String ownerName) {
        this(accountNumber, ownerName, 0.0); // Calls main constructor
    }
    
    public void displayAccount() {
        System.out.println("Account: " + accountNumber);
        System.out.println("Owner: " + ownerName);
        System.out.printf("Balance: $%.2f%n", balance);
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
    }
    
    public static void main(String[] args) {
        try {
            // Valid accounts
            BankAccount account1 = new BankAccount("123456789", "Alice Smith", 1000.00);
            BankAccount account2 = new BankAccount("987654321", "Bob Johnson"); // Zero balance
            
            System.out.println();
            account1.displayAccount();
            System.out.println();
            account2.displayAccount();
            
            System.out.println();
            
            // Invalid accounts (will throw exceptions)
            BankAccount invalid1 = new BankAccount("", "Charlie Brown", 500); // Empty account number
            BankAccount invalid2 = new BankAccount("111222333", "", 500); // Empty owner name
            BankAccount invalid3 = new BankAccount("444555666", "David White", -100); // Negative balance
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Expected Output:**
```
Account created for Alice Smith with balance $1000.0
Account created for Bob Johnson with balance $0.0

Account: 123456789
Owner: Alice Smith
Balance: $1000.00
Status: Active

Account: 987654321
Owner: Bob Johnson
Balance: $0.00
Status: Active

Error: Account number cannot be null or empty
```

**Best Practices:**
- Validate all parameters in constructors
- Throw descriptive exceptions for invalid parameters
- Use constructor chaining to reduce code duplication
- Ensure objects are in valid states after construction

## Medium Examples

### Example 1: Copy Constructor and Object Cloning
**Problem Statement:**
Implement a copy constructor for a Person class and compare it with the clone method.
**Requirements:**
- Implement a copy constructor that creates a deep copy
- Compare copy constructor with clone method
- Handle mutable objects within the class

**Implementation:**
```java
package academy.javaengineering.oop.constructors;

import java.util.ArrayList;
import java.util.List;

public class Person implements Cloneable {
    private String name;
    private int age;
    private List<String> hobbies;
    private Address address;
    
    // Regular constructor
    public Person(String name, int age, List<String> hobbies, Address address) {
        this.name = name;
        this.age = age;
        this.hobbies = new ArrayList<>(hobbies); // Create new list
        this.address = new Address(address); // Create new address
    }
    
    // Copy constructor (deep copy)
    public Person(Person other) {
        this.name = other.name;
        this.age = other.age;
        this.hobbies = new ArrayList<>(other.hobbies); // Deep copy list
        this.address = new Address(other.address); // Deep copy address
    }
    
    // Clone method (deep copy)
    @Override
    public Person clone() {
        try {
            Person cloned = (Person) super.clone();
            cloned.hobbies = new ArrayList<>(this.hobbies); // Deep copy list
            cloned.address = new Address(this.address); // Deep copy address
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Display person info
    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
        System.out.println("Hobbies: " + hobbies);
        System.out.println("Address: " + address);
    }
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public List<String> getHobbies() { return hobbies; }
    public Address getAddress() { return address; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setHobbies(List<String> hobbies) { 
        this.hobbies = new ArrayList<>(hobbies); 
    }
    public void setAddress(Address address) { 
        this.address = new Address(address); 
    }
    
    public static void main(String[] args) {
        // Create original person
        Address originalAddress = new Address("123 Main St", "Springfield", "IL");
        List<String> originalHobbies = new ArrayList<>();
        originalHobbies.add("Reading");
        originalHobbies.add("Swimming");
        
        Person original = new Person("Alice", 30, originalHobbies, originalAddress);
        
        System.out.println("=== Original Person ===");
        original.displayInfo();
        System.out.println();
        
        // Create copy using copy constructor
        Person copyConstructor = new Person(original);
        
        System.out.println("=== Copy Constructor ===");
        copyConstructor.displayInfo();
        System.out.println();
        
        // Create copy using clone method
        Person cloneMethod = original.clone();
        
        System.out.println("=== Clone Method ===");
        cloneMethod.displayInfo();
        System.out.println();
        
        // Modify the copy constructor copy
        System.out.println("=== Modifying Copy Constructor Copy ===");
        copyConstructor.setName("Bob");
        copyConstructor.getHobbies().add("Cooking");
        copyConstructor.getAddress().setCity("Shelbyville");
        
        System.out.println("Modified copy:");
        copyConstructor.displayInfo();
        System.out.println();
        
        System.out.println("Original (should be unchanged):");
        original.displayInfo();
        System.out.println();
        
        // Verify deep copy
        System.out.println("=== Deep Copy Verification ===");
        System.out.println("Original hobbies == Copy hobbies: " + 
                          (original.getHobbies() == copyConstructor.getHobbies()));
        System.out.println("Original address == Copy address: " + 
                          (original.getAddress() == copyConstructor.getAddress()));
    }
}

// Address class to demonstrate deep copy
class Address {
    private String street;
    private String city;
    private String state;
    
    public Address(String street, String city, String state) {
        this.street = street;
        this.city = city;
        this.state = state;
    }
    
    // Copy constructor
    public Address(Address other) {
        this.street = other.street;
        this.city = other.city;
        this.state = other.state;
    }
    
    // Getters and setters
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    
    @Override
    public String toString() {
        return street + ", " + city + ", " + state;
    }
}
```

**Code Walkthrough:**
1. We create a `Person` class with mutable fields (List, Address)
2. The copy constructor creates a deep copy by copying each field
3. The `clone()` method also creates a deep copy
4. We demonstrate that modifications to the copy don't affect the original
5. We verify that the copy is independent (different objects in memory)

**Expected Output:**
```
=== Original Person ===
Name: Alice, Age: 30
Hobbies: [Reading, Swimming]
Address: 123 Main St, Springfield, IL

=== Copy Constructor ===
Name: Alice, Age: 30
Hobbies: [Reading, Swimming]
Address: 123 Main St, Springfield, IL

=== Clone Method ===
Name: Alice, Age: 30
Hobbies: [Reading, Swimming]
Address: 123 Main St, Springfield, IL

=== Modifying Copy Constructor Copy ===
Modified copy:
Name: Bob, Age: 30
Hobbies: [Reading, Swimming, Cooking]
Address: 123 Main St, Shelbyville, IL

Original (should be unchanged):
Name: Alice, Age: 30
Hobbies: [Reading, Swimming]
Address: 123 Main St, Springfield, IL

=== Deep Copy Verification ===
Original hobbies == Copy hobbies: false
Original address == Copy address: false
```

**Alternative Solution:**
```java
// Using static factory method instead of copy constructor
public class PersonFactory {
    public static Person createCopy(Person original) {
        return new Person(original);
    }
    
    public static Person createImmutable(String name, int age, 
                                        List<String> hobbies, Address address) {
        return new Person(name, age, 
                         Collections.unmodifiableList(hobbies), 
                         address);
    }
}

// Usage
Person immutable = PersonFactory.createImmutable("Alice", 30, 
    Arrays.asList("Reading", "Swimming"), 
    new Address("123 Main St", "Springfield", "IL"));
```

## Hard Examples

### Example 1: Builder Pattern for Complex Object Construction
**Problem Statement:**
Design a Computer class with many optional parameters and implement the Builder pattern for flexible object creation.
**Requirements:**
- Support many optional configuration parameters
- Provide fluent interface for building objects
- Ensure thread-safe construction
- Validate configuration during build

**Architecture:**
```
computer-builder/
├── Computer.java
├── ComputerBuilder.java
├── ComputerConfigurator.java
└── Main.java
```

**Implementation:**
```java
package academy.javaengineering.oop.constructors;

import java.util.*;

// Computer class with many parameters
public class Computer {
    // Required parameters
    private final String model;
    private final String serialNumber;
    
    // Optional parameters with defaults
    private final CPU cpu;
    private final RAM ram;
    private final Storage storage;
    private final GPU gpu;
    private final PowerSupply powerSupply;
    private final CoolingSystem coolingSystem;
    private final List<Peripheral> peripherals;
    private final Map<String, String> biosSettings;
    private final boolean isCustomBuild;
    
    // Private constructor - only accessible through Builder
    private Computer(ComputerBuilder builder) {
        this.model = builder.model;
        this.serialNumber = builder.serialNumber;
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.gpu = builder.gpu;
        this.powerSupply = builder.powerSupply;
        this.coolingSystem = builder.coolingSystem;
        this.peripherals = Collections.unmodifiableList(builder.peripherals);
        this.biosSettings = Collections.unmodifiableMap(builder.biosSettings);
        this.isCustomBuild = builder.isCustomBuild;
    }
    
    // Getters
    public String getModel() { return model; }
    public String getSerialNumber() { return serialNumber; }
    public CPU getCpu() { return cpu; }
    public RAM getRam() { return ram; }
    public Storage getStorage() { return storage; }
    public GPU getGpu() { return gpu; }
    public PowerSupply getPowerSupply() { return powerSupply; }
    public CoolingSystem getCoolingSystem() { return coolingSystem; }
    public List<Peripheral> getPeripherals() { return peripherals; }
    public Map<String, String> getBiosSettings() { return biosSettings; }
    public boolean isCustomBuild() { return isCustomBuild; }
    
    // Display computer configuration
    public void displayConfiguration() {
        System.out.println("=== Computer Configuration ===");
        System.out.println("Model: " + model);
        System.out.println("Serial: " + serialNumber);
        System.out.println("Custom Build: " + isCustomBuild);
        System.out.println();
        System.out.println("Components:");
        System.out.println("  CPU: " + cpu);
        System.out.println("  RAM: " + ram);
        System.out.println("  Storage: " + storage);
        System.out.println("  GPU: " + gpu);
        System.out.println("  Power Supply: " + powerSupply);
        System.out.println("  Cooling: " + coolingSystem);
        
        if (!peripherals.isEmpty()) {
            System.out.println("  Peripherals: " + peripherals.size());
            for (Peripheral p : peripherals) {
                System.out.println("    - " + p);
            }
        }
        
        if (!biosSettings.isEmpty()) {
            System.out.println("  BIOS Settings:");
            for (Map.Entry<String, String> entry : biosSettings.entrySet()) {
                System.out.println("    " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }
    
    // Builder class
    public static class ComputerBuilder {
        // Required parameters
        private final String model;
        private final String serialNumber;
        
        // Optional parameters with defaults
        private CPU cpu = new CPU("Intel", "Core i5", 4, 3.0);
        private RAM ram = new RAM(16, "DDR4", 2666);
        private Storage storage = new Storage(512, "SSD");
        private GPU gpu = new GPU("Integrated", 0);
        private PowerSupply powerSupply = new PowerSupply(500, "80+ Bronze");
        private CoolingSystem coolingSystem = new CoolingSystem("Air", 120);
        private List<Peripheral> peripherals = new ArrayList<>();
        private Map<String, String> biosSettings = new HashMap<>();
        private boolean isCustomBuild = false;
        
        // Constructor with required parameters
        public ComputerBuilder(String model, String serialNumber) {
            this.model = model;
            this.serialNumber = serialNumber;
        }
        
        // Fluent methods for setting optional parameters
        public ComputerBuilder cpu(CPU cpu) {
            this.cpu = cpu;
            return this;
        }
        
        public ComputerBuilder ram(RAM ram) {
            this.ram = ram;
            return this;
        }
        
        public ComputerBuilder storage(Storage storage) {
            this.storage = storage;
            return this;
        }
        
        public ComputerBuilder gpu(GPU gpu) {
            this.gpu = gpu;
            return this;
        }
        
        public ComputerBuilder powerSupply(PowerSupply powerSupply) {
            this.powerSupply = powerSupply;
            return this;
        }
        
        public ComputerBuilder coolingSystem(CoolingSystem coolingSystem) {
            this.coolingSystem = coolingSystem;
            return this;
        }
        
        public ComputerBuilder addPeripheral(Peripheral peripheral) {
            this.peripherals.add(peripheral);
            return this;
        }
        
        public ComputerBuilder biosSetting(String key, String value) {
            this.biosSettings.put(key, value);
            return this;
        }
        
        public ComputerBuilder customBuild(boolean isCustom) {
            this.isCustomBuild = isCustom;
            return this;
        }
        
        // Validation method
        private void validate() {
            if (model == null || model.trim().isEmpty()) {
                throw new IllegalStateException("Model cannot be null or empty");
            }
            if (serialNumber == null || serialNumber.trim().isEmpty()) {
                throw new IllegalStateException("Serial number cannot be null or empty");
            }
            
            // Validate power supply is sufficient
            int totalPower = cpu.getTdp() + gpu.getTdp();
            if (totalPower > powerSupply.getWattage()) {
                throw new IllegalStateException(
                    "Insufficient power: " + totalPower + "W required, " + 
                    powerSupply.getWattage() + "W available");
            }
            
            // Validate RAM compatibility
            if (ram.getSpeed() > cpu.getMaxMemorySpeed()) {
                throw new IllegalStateException(
                    "RAM speed " + ram.getSpeed() + "MHz exceeds CPU maximum " + 
                    cpu.getMaxMemorySpeed() + "MHz");
            }
        }
        
        // Build method
        public Computer build() {
            validate();
            return new Computer(this);
        }
        
        // Reset builder
        public ComputerBuilder reset() {
            this.cpu = new CPU("Intel", "Core i5", 4, 3.0);
            this.ram = new RAM(16, "DDR4", 2666);
            this.storage = new Storage(512, "SSD");
            this.gpu = new GPU("Integrated", 0);
            this.powerSupply = new PowerSupply(500, "80+ Bronze");
            this.coolingSystem = new CoolingSystem("Air", 120);
            this.peripherals.clear();
            this.biosSettings.clear();
            this.isCustomBuild = false;
            return this;
        }
    }
    
    public static void main(String[] args) {
        try {
            // Build a gaming computer
            Computer gamingPC = new ComputerComputerBuilder("Gaming Pro", "SN-001")
                .cpu(new CPU("AMD", "Ryzen 7 5800X", 8, 3.8))
                .ram(new RAM(32, "DDR4", 3600))
                .storage(new Storage(1000, "NVMe SSD"))
                .gpu(new GPU("NVIDIA RTX 3070", 220))
                .powerSupply(new PowerSupply(750, "80+ Gold"))
                .coolingSystem(new CoolingSystem("Liquid", 240))
                .addPeripheral(new Peripheral("Mechanical Keyboard", "USB"))
                .addPeripheral(new Peripheral("Gaming Mouse", "USB"))
                .biosSetting("XMP", "Enabled")
                .biosSetting("Fan Curve", "Performance")
                .customBuild(true)
                .build();
            
            // Build a office computer
            Computer officePC = new ComputerBuilder("Office Basic", "SN-002")
                .build(); // Use all defaults
            
            // Display configurations
            gamingPC.displayConfiguration();
            System.out.println();
            officePC.displayConfiguration();
            
            // Test validation (will fail)
            Computer invalidPC = new ComputerBuilder("Invalid", "SN-003")
                .cpu(new CPU("Intel", "Core i9", 16, 3.5))
                .gpu(new GPU("NVIDIA RTX 3090", 350))
                .powerSupply(new PowerSupply(500, "80+ Bronze")) // Insufficient power
                .build();
            
        } catch (IllegalStateException e) {
            System.out.println("Build error: " + e.getMessage());
        }
    }
}

// Supporting component classes
class CPU {
    private String brand;
    private String model;
    private int cores;
    private double clockSpeed;
    private int tdp;
    private int maxMemorySpeed;
    
    public CPU(String brand, String model, int cores, double clockSpeed) {
        this.brand = brand;
        this.model = model;
        this.cores = cores;
        this.clockSpeed = clockSpeed;
        this.tdp = 105; // Default TDP
        this.maxMemorySpeed = 3200; // Default max memory speed
    }
    
    public int getTdp() { return tdp; }
    public int getMaxMemorySpeed() { return maxMemorySpeed; }
    
    @Override
    public String toString() {
        return brand + " " + model + " (" + cores + " cores, " + clockSpeed + " GHz)";
    }
}

class RAM {
    private int capacity;
    private String type;
    private int speed;
    
    public RAM(int capacity, String type, int speed) {
        this.capacity = capacity;
        this.type = type;
        this.speed = speed;
    }
    
    public int getSpeed() { return speed; }
    
    @Override
    public String toString() {
        return capacity + "GB " + type + " " + speed + "MHz";
    }
}

class Storage {
    private int capacity;
    private String type;
    
    public Storage(int capacity, String type) {
        this.capacity = capacity;
        this.type = type;
    }
    
    @Override
    public String toString() {
        return capacity + "GB " + type;
    }
}

class GPU {
    private String model;
    private int tdp;
    
    public GPU(String model, int tdp) {
        this.model = model;
        this.tdp = tdp;
    }
    
    public int getTdp() { return tdp; }
    
    @Override
    public String toString() {
        return model;
    }
}

class PowerSupply {
    private int wattage;
    private String efficiency;
    
    public PowerSupply(int wattage, String efficiency) {
        this.wattage = wattage;
        this.efficiency = efficiency;
    }
    
    public int getWattage() { return wattage; }
    
    @Override
    public String toString() {
        return wattage + "W " + efficiency;
    }
}

class CoolingSystem {
    private String type;
    private int size;
    
    public CoolingSystem(String type, int size) {
        this.type = type;
        this.size = size;
    }
    
    @Override
    public String toString() {
        return type + " " + size + "mm";
    }
}

class Peripheral {
    private String name;
    private String connection;
    
    public Peripheral(String name, String connection) {
        this.name = name;
        this.connection = connection;
    }
    
    @Override
    public String toString() {
        return name + " (" + connection + ")";
    }
}
```

**Unit Tests:**
```java
package academy.javaengineering.oop.constructors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComputerBuilderTest {
    
    @Test
    void testBuildWithDefaults() {
        Computer computer = new Computer.ComputerBuilder("Test", "SN-001")
            .build();
        
        assertNotNull(computer);
        assertEquals("Test", computer.getModel());
        assertEquals("SN-001", computer.getSerialNumber());
        assertFalse(computer.isCustomBuild());
    }
    
    @Test
    void testBuildWithCustomComponents() {
        CPU cpu = new CPU("AMD", "Ryzen 9", 12, 3.7);
        RAM ram = new RAM(64, "DDR4", 3600);
        
        Computer computer = new Computer.ComputerBuilder("Custom", "SN-002")
            .cpu(cpu)
            .ram(ram)
            .customBuild(true)
            .build();
        
        assertEquals(cpu, computer.getCpu());
        assertEquals(ram, computer.getRam());
        assertTrue(computer.isCustomBuild());
    }
    
    @Test
    void testInsufficientPower() {
        assertThrows(IllegalStateException.class, () -> {
            new Computer.ComputerBuilder("Invalid", "SN-003")
                .cpu(new CPU("Intel", "Core i9", 16, 3.5))
                .gpu(new GPU("RTX 3090", 350))
                .powerSupply(new PowerSupply(500, "80+ Bronze"))
                .build();
        });
    }
    
    @Test
    void testBuilderReset() {
        Computer.ComputerBuilder builder = new Computer.ComputerBuilder("Test", "SN-001")
            .customBuild(true);
        
        builder.reset();
        Computer computer = builder.build();
        
        assertFalse(computer.isCustomBuild());
    }
}
```

**Execution Flow:**
1. Builder is initialized with required parameters
2. Optional parameters are set using fluent methods
3. Validation checks ensure configuration is valid
4. Computer object is created with validated parameters
5. Immutable collections prevent modification after creation

**Complexity:**
- Time: O(1) for building, O(n) for validation where n is number of components
- Space: O(1) for builder, O(n) for computer object

**Best Practices:**
- Use builder pattern for objects with many optional parameters
- Make builder methods return `this` for fluent interface
- Validate parameters during build, not in setter methods
- Make the constructed object immutable
- Provide sensible defaults for optional parameters

## Exercises

### Easy
1. **Rectangle**: Create a Rectangle class with default and parameterized constructors. Include a copy constructor.

2. **Book**: Design a Book class with constructors for different initialization scenarios (with/without page count).

3. **Employee**: Create an Employee class with constructors that validate input parameters.

### Medium
1. **Vehicle**: Design a Vehicle class hierarchy with constructors for Car, Truck, and Motorcycle. Use constructor chaining.

2. **Database Connection**: Create a DatabaseConnection class with constructors for different connection types (file, network, memory).

3. **Configuration**: Design a Configuration class with a builder pattern for flexible configuration creation.

### Hard
1. **Game Character**: Create a game character system with complex constructors for different character classes (warrior, mage, archer) with various equipment and skill sets.

2. **Network Protocol**: Design a network protocol message class with constructors for different message types and optional fields.

3. **Document Processor**: Create a document processor with constructors for different document formats and processing options.

## Interview Questions

### Easy
1. **Q:** What is a constructor in Java?
   **A:** A constructor is a special block of code that is called when an object is created. It has the same name as the class and no return type. It initializes the object's state and allocates memory for its fields.

2. **Q:** What is the difference between a constructor and a method?
   **A:** Key differences:
   - Constructors have the same name as the class, methods have descriptive names
   - Constructors have no return type, methods have return types
   - Constructors are called when an object is created, methods are called on existing objects
   - Constructors cannot be static, final, abstract, or synchronized

3. **Q:** What is a default constructor?
   **A:** A default constructor is a no-argument constructor that Java automatically provides if no other constructor is defined. It initializes fields to default values (null for objects, 0 for numbers, false for booleans).

### Medium
1. **Q:** Explain constructor chaining in Java.
   **A:** Constructor chaining is the process of calling one constructor from another within the same class or from a parent class. It's done using:
   - `this()` - to call another constructor in the same class
   - `super()` - to call the parent class constructor
   
   Example:
   ```java
   public class Student {
       public Student() {
           this("Unknown", 0); // Calls parameterized constructor
       }
       
       public Student(String name, int age) {
           this.name = name;
           this.age = age;
       }
   }
   ```

2. **Q:** What is the purpose of the `this` keyword in constructors?
   **A:** The `this` keyword in constructors serves two purposes:
   1. To distinguish between parameters and instance variables when they have the same name
   2. To call another constructor in the same class using `this()`
   
   Example:
   ```java
   public Person(String name) {
       this.name = name; // Distinguishes field from parameter
   }
   
   public Person() {
       this("Unknown"); // Calls other constructor
   }
   ```

3. **Q:** Can a constructor be private? What are the implications?
   **A:** Yes, a constructor can be private. This is used in:
   - Singleton pattern: to prevent direct instantiation
   - Factory methods: to control object creation
   - Utility classes: to prevent instantiation
   
   Example:
   ```java
   public class Singleton {
       private static Singleton instance;
       private Singleton() {} // Private constructor
       
       public static Singleton getInstance() {
           if (instance == null) {
               instance = new Singleton();
           }
           return instance;
       }
   }
   ```

### Hard
1. **Q:** Explain the Builder pattern and when to use it.
   **A:** The Builder pattern separates the construction of a complex object from its representation. It's useful when:
   - An object has many optional parameters
   - You want to avoid telescoping constructors
   - You need to create different representations of the same object
   - Construction involves multiple steps
   
   Implementation involves:
   - A static inner Builder class
   - Fluent interface methods that return `this`
   - A `build()` method that creates the final object
   - Validation in the `build()` method

2. **Q:** How do you implement a copy constructor for a class with mutable fields?
   **A:** For mutable fields, you must create deep copies:
   ```java
   public Person(Person other) {
       this.name = other.name; // Immutable, shallow copy is fine
       this.age = other.age;   // Immutable, shallow copy is fine
       this.hobbies = new ArrayList<>(other.hobbies); // Deep copy list
       this.address = new Address(other.address); // Deep copy address
   }
   ```
   You can also use:
   - `Collections.unmodifiableList()` for unmodifiable collections
   - `clone()` method for objects that implement `Cloneable`
   - Static factory methods for controlled copy creation

3. **Q:** What are the implications of constructor overloading?
   **A:** Constructor overloading provides multiple ways to create objects, but has implications:
   - **Pros**: Flexibility in object creation, better readability, support for different initialization scenarios
   - **Cons**: Can lead to confusion if too many constructors, maintenance overhead
   - **Best practices**: 
     - Use constructor chaining to reduce duplication
     - Follow the principle of least astonishment
     - Document each constructor's purpose
     - Consider using builder pattern for many parameters

## Common Pitfalls

### Pitfall 1: Forgetting to Initialize Fields
**Mistake:**
```java
// Bad: Not initializing all fields in constructor
public class Person {
    private String name;
    private int age;
    private List<String> hobbies;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        // Forgot to initialize hobbies!
    }
    
    public void addHobby(String hobby) {
        hobbies.add(hobby); // NullPointerException!
    }
}
```

**Correct:**
```java
// Good: Initialize all fields in constructor
public class Person {
    private String name;
    private int age;
    private List<String> hobbies;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.hobbies = new ArrayList<>(); // Initialize empty list
    }
    
    public void addHobby(String hobby) {
        hobbies.add(hobby); // Works correctly
    }
}
```

**Why:** Uninitialized fields have default values (null for objects), leading to NullPointerException when accessed. Always initialize all fields in constructors.

### Pitfall 2: Using `this` Incorrectly
**Mistake:**
```java
// Bad: Incorrect use of this() in constructor chaining
public class Person {
    private String name;
    
    public Person() {
        name = "Unknown";
        this("Alice"); // Wrong! this() must be first statement
    }
    
    public Person(String name) {
        this.name = name;
    }
}
```

**Correct:**
```java
// Good: this() as first statement
public class Person {
    private String name;
    
    public Person() {
        this("Alice"); // Calls parameterized constructor
    }
    
    public Person(String name) {
        this.name = name;
    }
}
```

**Why:** `this()` and `super()` must be the first statement in a constructor. This ensures proper initialization order and prevents undefined behavior.

### Pitfall 3: Creating Mutable Objects in Constructor
**Mistake:**
```java
// Bad: Exposing mutable internal state
public class ShoppingCart {
    private List<Item> items;
    
    public ShoppingCart(List<Item> items) {
        this.items = items; // Stores reference to external list!
    }
    
    public void addItem(Item item) {
        items.add(item); // Modifies external list
    }
}

// Usage
List<Item> itemList = new ArrayList<>();
ShoppingCart cart = new ShoppingCart(itemList);
cart.addItem(new Item("Apple")); // Also modifies itemList!
```

**Correct:**
```java
// Good: Create defensive copies
public class ShoppingCart {
    private final List<Item> items;
    
    public ShoppingCart(List<Item> items) {
        this.items = new ArrayList<>(items); // Defensive copy
    }
    
    public void addItem(Item item) {
        items.add(item); // Only modifies internal list
    }
    
    public List<Item> getItems() {
        return Collections.unmodifiableList(items); // Return unmodifiable view
    }
}

// Usage
List<Item> itemList = new ArrayList<>();
ShoppingCart cart = new ShoppingCart(itemList);
cart.addItem(new Item("Apple")); // Only modifies cart's internal list
```

**Why:** Storing references to external mutable objects breaks encapsulation and can lead to unexpected behavior. Always create defensive copies of mutable parameters.

## Best Practices
1. **Initialize all fields**: Ensure every field is initialized in the constructor
2. **Use constructor chaining**: Call `this()` to avoid code duplication
3. **Validate parameters**: Check for invalid parameters and throw descriptive exceptions
4. **Create defensive copies**: For mutable parameters, create copies to protect internal state
5. **Keep constructors simple**: Avoid complex logic in constructors; use factory methods if needed

## Real World Usage

### Spring Framework
Spring uses constructors for dependency injection:
- **Constructor Injection**: Dependencies are provided through constructors
- **@Autowired**: Can be used on constructors for automatic injection
- **Immutable Beans**: Spring supports constructor injection for immutable beans

### Hibernate
Hibernate uses constructors for entity instantiation:
- **Default Constructor**: Required for Hibernate to create proxy objects
- **Parameterized Constructors**: Used with `@ConstructorProperties` annotation
- **Immutable Entities**: Supported through constructor injection

### JDK Source Code
The JDK uses constructors extensively:
- **Collection Constructors**: ArrayList, HashMap have multiple constructors
- **Wrapper Classes**: Integer, Double have constructors for value creation
- **Exception Classes**: All exceptions have constructors for error messages

### Enterprise Applications
Production systems use constructors for:
- **Dependency Injection**: Providing dependencies through constructors
- **Immutable Objects**: Creating thread-safe objects
- **Factory Methods**: Controlling object creation
- **Validation**: Ensuring objects are in valid states

## References
- [Official Java Documentation](https://docs.oracle.com/en/java/)
- Effective Java by Joshua Bloch
- Head First Design Patterns by Eric Freeman
- Clean Code by Robert C. Martin

## Summary
- Constructors initialize objects when they are created
- Java provides a default no-argument constructor if none is defined
- Constructor chaining with `this()` reduces code duplication
- Copy constructors create deep copies of objects
- Builder pattern is useful for objects with many optional parameters
- Always validate parameters and create defensive copies for mutable objects

---
**Next Topic:** [Methods](../05-methods/)
**Previous Topic:** [Objects](../03-objects/)