# Objects

An object in Java is an instance of a class. It is a runtime entity that combines state (data) and behavior (methods) into a single unit. Objects are created from class definitions and represent specific entities in the program. When you create an object, memory is allocated on the heap, and the object's fields are initialized with values. Objects can interact with each other through method calls, and they can be passed as parameters, returned from methods, and stored in collections.

## Learning Objectives
By the end of this topic, you will be able to:
- Understand the lifecycle of objects from creation to garbage collection
- Create and manipulate objects using constructors and methods
- Differentiate between object references and object values
- Apply object-oriented principles to solve real-world problems

## Prerequisites
- [Classes](../02-classes/)
- Understanding of Java syntax and basic programming concepts
- Familiarity with variables and data types

## Why This Concept Exists

### The Problem
Without objects, programs consisted of procedural code with data and functions separated. This approach had limitations:
- **No real-world modeling**: Programs couldn't easily represent real-world entities
- **State management complexity**: Tracking state across functions was error-prone
- **Code reuse limitations**: Similar structures couldn't be easily reused

### The Solution
Objects solve these problems by:
- **Modeling real-world entities**: Each object represents a specific thing with properties and behaviors
- **Encapsulating state**: Objects maintain their own state through instance variables
- **Enabling interaction**: Objects can communicate through method calls
- **Supporting dynamic behavior**: Objects can change state and respond to messages

### Real-World Analogy
Think of a class as a cookie cutter and objects as the cookies. The cookie cutter (class) defines the shape and size, but each cookie (object) can have different decorations (state). You can make many cookies from the same cutter, each unique but sharing the same basic shape.

## Internal Working

### JVM Perspective
When an object is created:
1. **Memory Allocation**: JVM allocates memory on the heap for the object
2. **Constructor Execution**: The constructor initializes the object's state
3. **Reference Assignment**: A reference to the object is assigned to a variable
4. **Object Usage**: The object can be used through its reference
5. **Garbage Collection**: When no references exist, the object becomes eligible for GC

### Memory Representation
```java
public class Point {
    int x;
    int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

// Object creation
Point p1 = new Point(10, 20);
Point p2 = new Point(30, 40);
Point p3 = p1; // p3 references same object as p1

// Memory layout:
// Stack:
//   p1 -> reference to Point object at 0x1000
//   p2 -> reference to Point object at 0x1008
//   p3 -> reference to Point object at 0x1000 (same as p1)
// Heap:
//   0x1000: Point object {x: 10, y: 20}
//   0x1008: Point object {x: 30, y: 40}
```

## Syntax

```java
// Creating objects using constructors
ClassName objectName = new ClassName();

// Creating objects with parameters
ClassName objectName = new ClassName(parameter1, parameter2);

// Accessing object members
objectName.fieldName;        // Accessing field
objectName.methodName();     // Calling method

// Passing objects as parameters
public void methodName(ClassName parameter) {
    // Use parameter
}

// Returning objects from methods
public ClassName methodName() {
    return new ClassName();
}

// Array of objects
ClassName[] objects = new ClassName[5];
objects[0] = new ClassName();
```

## Easy Examples

### Example 1: Creating and Using Point Objects
**Problem Statement:**
Create Point objects to represent coordinates and perform operations on them.

**Implementation:**
```java
package academy.javaengineering.oop.objects;

public class Point {
    private int x;
    private int y;
    
    // Default constructor
    public Point() {
        this.x = 0;
        this.y = 0;
    }
    
    // Parameterized constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Method to calculate distance to another point
    public double distanceTo(Point other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    // Method to move the point
    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }
    
    // Method to display point coordinates
    public void display() {
        System.out.println("(" + x + ", " + y + ")");
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    
    public static void main(String[] args) {
        // Create Point objects
        Point origin = new Point();
        Point p1 = new Point(3, 4);
        Point p2 = new Point(6, 8);
        
        System.out.println("Origin:");
        origin.display();
        
        System.out.println("Point 1:");
        p1.display();
        
        System.out.println("Point 2:");
        p2.display();
        
        // Calculate distances
        System.out.println("\nDistance from origin to p1: " + 
                          String.format("%.2f", origin.distanceTo(p1)));
        System.out.println("Distance from p1 to p2: " + 
                          String.format("%.2f", p1.distanceTo(p2)));
        
        // Move a point
        System.out.println("\nMoving p1 by (2, 2)...");
        p1.move(2, 2);
        System.out.println("New p1 location:");
        p1.display();
        
        // Objects as parameters
        System.out.println("\nDistance from origin to moved p1: " + 
                          String.format("%.2f", origin.distanceTo(p1)));
    }
}
```

**Expected Output:**
```
Origin:
(0, 0)

Point 1:
(3, 4)

Point 2:
(6, 8)

Distance from origin to p1: 5.00
Distance from p1 to p2: 5.00

Moving p1 by (2, 2)...
New p1 location:
(5, 6)

Distance from origin to moved p1: 7.81
```

**Best Practices:**
- Use meaningful method names that describe the operation
- Provide multiple constructors for flexibility
- Use getters to control access to private fields

### Example 2: Object References and Equality
**Problem Statement:**
Demonstrate the difference between object references and object equality.

**Implementation:**
```java
package academy.javaengineering.oop.objects;

public class Student {
    private String name;
    private int id;
    
    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return id == student.id && name.equals(student.name);
    }
    
    @Override
    public String toString() {
        return "Student{name='" + name + "', id=" + id + "}";
    }
    
    public static void main(String[] args) {
        // Object references
        Student student1 = new Student("Alice", 1001);
        Student student2 = new Student("Alice", 1001);
        Student student3 = student1; // student3 references same object as student1
        
        System.out.println("student1: " + student1);
        System.out.println("student2: " + student2);
        System.out.println("student3: " + student3);
        
        // Reference comparison (==)
        System.out.println("\nReference comparison:");
        System.out.println("student1 == student2: " + (student1 == student2)); // false
        System.out.println("student1 == student3: " + (student1 == student3)); // true
        
        // Value comparison (equals)
        System.out.println("\nValue comparison:");
        System.out.println("student1.equals(student2): " + student1.equals(student2)); // true
        System.out.println("student1.equals(student3): " + student1.equals(student3)); // true
        
        // Modifying through reference
        System.out.println("\nModifying through reference:");
        student3.name = "Bob"; // Changes the same object
        System.out.println("After modifying student3:");
        System.out.println("student1: " + student1); // Also changed
        System.out.println("student3: " + student3);
    }
}
```

**Expected Output:**
```
student1: Student{name='Alice', id=1001}
student2: Student{name='Alice', id=1001}
student3: Student{name='Alice', id=1001}

Reference comparison:
student1 == student2: false
student1 == student3: true

Value comparison:
student1.equals(student2): true
student1.equals(student3): true

Modifying through reference:
After modifying student3:
student1: Student{name='Bob', id=1001}
student3: Student{name='Bob', id=1001}
```

**Best Practices:**
- Use `==` to compare object references (same object)
- Override `equals()` to compare object values
- Always override `hashCode()` when overriding `equals()`
- Understand that multiple references can point to the same object

## Medium Examples

### Example 1: Object Composition
**Problem Statement:**
Create a Computer class that contains components (CPU, RAM, Storage) as objects.
**Requirements:**
- Each component is a separate object with its own properties
- Computer should be able to display its configuration
- Components should be replaceable

**Implementation:**
```java
package academy.javaengineering.oop.objects;

// Component classes
class CPU {
    private String brand;
    private String model;
    private int cores;
    private double clockSpeed; // in GHz
    
    public CPU(String brand, String model, int cores, double clockSpeed) {
        this.brand = brand;
        this.model = model;
        this.cores = cores;
        this.clockSpeed = clockSpeed;
    }
    
    public void display() {
        System.out.printf("CPU: %s %s (%d cores, %.1f GHz)%n", 
                         brand, model, cores, clockSpeed);
    }
    
    // Getters and setters
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getCores() { return cores; }
    public double getClockSpeed() { return clockSpeed; }
}

class RAM {
    private int capacity; // in GB
    private String type; // DDR4, DDR5
    private int speed; // in MHz
    
    public RAM(int capacity, String type, int speed) {
        this.capacity = capacity;
        this.type = type;
        this.speed = speed;
    }
    
    public void display() {
        System.out.printf("RAM: %dGB %s %dMHz%n", capacity, type, speed);
    }
    
    // Getters
    public int getCapacity() { return capacity; }
    public String getType() { return type; }
    public int getSpeed() { return speed; }
}

class Storage {
    private int capacity; // in GB
    private String type; // SSD, HDD
    private int readSpeed; // in MB/s
    
    public Storage(int capacity, String type, int readSpeed) {
        this.capacity = capacity;
        this.type = type;
        this.readSpeed = readSpeed;
    }
    
    public void display() {
        System.out.printf("Storage: %dGB %s (%d MB/s read)%n", 
                         capacity, type, readSpeed);
    }
    
    // Getters
    public int getCapacity() { return capacity; }
    public String getType() { return type; }
    public int getReadSpeed() { return readSpeed; }
}

// Computer class with object composition
public class Computer {
    private String name;
    private CPU cpu;
    private RAM ram;
    private Storage storage;
    
    public Computer(String name, CPU cpu, RAM ram, Storage storage) {
        this.name = name;
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
    }
    
    public void displayConfiguration() {
        System.out.println("=== " + name + " Configuration ===");
        cpu.display();
        ram.display();
        storage.display();
        System.out.println();
    }
    
    public double calculatePerformanceScore() {
        // Simple performance calculation
        double cpuScore = cpu.getCores() * cpu.getClockSpeed() * 10;
        double ramScore = ram.getCapacity() * (ram.getSpeed() / 1000.0);
        double storageScore = storage.getReadSpeed() / 10.0;
        
        return cpuScore + ramScore + storageScore;
    }
    
    // Method to upgrade components
    public void upgradeCPU(CPU newCPU) {
        System.out.println("Upgrading CPU from " + cpu.getModel() + " to " + newCPU.getModel());
        this.cpu = newCPU;
    }
    
    public void upgradeRAM(RAM newRAM) {
        System.out.println("Upgrading RAM from " + ram.getCapacity() + "GB to " + 
                          newRAM.getCapacity() + "GB");
        this.ram = newRAM;
    }
    
    // Getters
    public String getName() { return name; }
    public CPU getCpu() { return cpu; }
    public RAM getRam() { return ram; }
    public Storage getStorage() { return storage; }
    
    public static void main(String[] args) {
        // Create components
        CPU cpu1 = new CPU("Intel", "Core i7-12700K", 12, 3.6);
        RAM ram1 = new RAM(32, "DDR5", 4800);
        Storage storage1 = new Storage(1000, "NVMe SSD", 3500);
        
        CPU cpu2 = new CPU("AMD", "Ryzen 9 5900X", 12, 3.7);
        RAM ram2 = new RAM(64, "DDR4", 3600);
        Storage storage2 = new Storage(2000, "NVMe SSD", 5000);
        
        // Create computers
        Computer gamingPC = new Computer("Gaming PC", cpu1, ram1, storage1);
        Computer workstation = new Computer("Workstation", cpu2, ram2, storage2);
        
        // Display configurations
        gamingPC.displayConfiguration();
        workstation.displayConfiguration();
        
        // Calculate performance
        System.out.println("Gaming PC Performance Score: " + 
                          String.format("%.2f", gamingPC.calculatePerformanceScore()));
        System.out.println("Workstation Performance Score: " + 
                          String.format("%.2f", workstation.calculatePerformanceScore()));
        
        // Upgrade components
        System.out.println("\nUpgrading Gaming PC...");
        RAM newRAM = new RAM(64, "DDR5", 5200);
        gamingPC.upgradeRAM(newRAM);
        
        System.out.println("\nUpdated Gaming PC Configuration:");
        gamingPC.displayConfiguration();
        System.out.println("New Performance Score: " + 
                          String.format("%.2f", gamingPC.calculatePerformanceScore()));
    }
}
```

**Code Walkthrough:**
1. We create separate classes for each component (CPU, RAM, Storage)
2. The Computer class uses composition to contain these components
3. Each component encapsulates its own data and behavior
4. The Computer class provides methods to manipulate its components
5. We can replace components without affecting the Computer class structure

**Expected Output:**
```
=== Gaming PC Configuration ===
CPU: Intel Core i7-12700K (12 cores, 3.6 GHz)
RAM: 32GB DDR5 4800MHz
Storage: 1000GB NVMe SSD (3500 MB/s read)

=== Workstation Configuration ===
CPU: AMD Ryzen 9 5900X (12 cores, 3.7 GHz)
RAM: 64GB DDR4 3600MHz
Storage: 2000GB NVMe SSD (5000 MB/s read)

Gaming PC Performance Score: 868.00
Workstation Performance Score: 1002.80

Upgrading Gaming PC...
Upgrading RAM from 32GB to 64GB

Updated Gaming PC Configuration:
=== Gaming PC Configuration ===
CPU: Intel Core i7-12700K (12 cores, 3.6 GHz)
RAM: 64GB DDR5 5200MHz
Storage: 1000GB NVMe SSD (3500 MB/s read)

New Performance Score: 1108.00
```

**Alternative Solution:**
```java
// Using interfaces for more flexible composition
interface Component {
    String getDetails();
    double getPerformanceScore();
}

class CPU implements Component {
    @Override
    public String getDetails() {
        return String.format("CPU: %s %s", brand, model);
    }
    
    @Override
    public double getPerformanceScore() {
        return cores * clockSpeed * 10;
    }
}

// Computer can now work with any Component implementation
public class FlexibleComputer {
    private List<Component> components;
    
    public void addComponent(Component component) {
        components.add(component);
    }
    
    public double getTotalPerformance() {
        return components.stream()
            .mapToDouble(Component::getPerformanceScore)
            .sum();
    }
}
```

## Hard Examples

### Example 1: Object Graph and Serialization
**Problem Statement:**
Design a system that manages a graph of objects with relationships and supports serialization/deserialization.
**Requirements:**
- Model objects with relationships (many-to-many, one-to-many)
- Detect and handle circular references
- Serialize object graphs to JSON and deserialize them
- Maintain object identity during serialization

**Architecture:**
```
object-graph/
├── models/
│   ├── Entity.java
│   ├── Person.java
│   ├── Company.java
│   └── Relationship.java
├── serialization/
│   ├── ObjectGraphSerializer.java
│   ├── JsonSerializer.java
│   └── ReferenceTracker.java
└── Main.java
```

**Implementation:**
```java
package academy.javaengineering.oop.objects;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// Base entity class
abstract class Entity {
    protected final String id;
    protected String name;
    
    protected Entity(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entity entity = (Entity) obj;
        return id.equals(entity.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

// Person class
class Person extends Entity {
    private int age;
    private String email;
    private List<Company> companies; // Person can work for multiple companies
    private Person manager; // One-to-many relationship
    
    public Person(String id, String name, int age, String email) {
        super(id, name);
        this.age = age;
        this.email = email;
        this.companies = new ArrayList<>();
    }
    
    public void addCompany(Company company) {
        if (!companies.contains(company)) {
            companies.add(company);
            company.addEmployee(this); // Maintain bidirectional relationship
        }
    }
    
    public void setManager(Person manager) {
        this.manager = manager;
    }
    
    // Getters
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public List<Company> getCompanies() { return Collections.unmodifiableList(companies); }
    public Person getManager() { return manager; }
    
    @Override
    public String toString() {
        return String.format("Person{id='%s', name='%s', age=%d}", id, name, age);
    }
}

// Company class
class Company extends Entity {
    private String industry;
    private int foundedYear;
    private List<Person> employees; // One-to-many relationship
    private Map<String, Department> departments;
    
    public Company(String id, String name, String industry, int foundedYear) {
        super(id, name);
        this.industry = industry;
        this.foundedYear = foundedYear;
        this.employees = new ArrayList<>();
        this.departments = new HashMap<>();
    }
    
    public void addEmployee(Person person) {
        if (!employees.contains(person)) {
            employees.add(person);
        }
    }
    
    public void addDepartment(String name, Department department) {
        departments.put(name, department);
    }
    
    // Getters
    public String getIndustry() { return industry; }
    public int getFoundedYear() { return foundedYear; }
    public List<Person> getEmployees() { return Collections.unmodifiableList(employees); }
    public Map<String, Department> getDepartments() { 
        return Collections.unmodifiableMap(departments); 
    }
    
    @Override
    public String toString() {
        return String.format("Company{id='%s', name='%s', industry='%s'}", 
                           id, name, industry);
    }
}

// Department class
class Department {
    private String name;
    private Person head;
    private List<Person> members;
    
    public Department(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }
    
    public void setHead(Person head) {
        this.head = head;
    }
    
    public void addMember(Person person) {
        if (!members.contains(person)) {
            members.add(person);
        }
    }
    
    // Getters
    public String getName() { return name; }
    public Person getHead() { return head; }
    public List<Person> getMembers() { return Collections.unmodifiableList(members); }
}

// Relationship class to track connections
class Relationship {
    public enum Type {
        WORKS_FOR,
        MANAGES,
        MEMBER_OF,
        REPORTS_TO
    }
    
    private Entity from;
    private Entity to;
    private Type type;
    
    public Relationship(Entity from, Entity to, Type type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }
    
    // Getters
    public Entity getFrom() { return from; }
    public Entity getTo() { return to; }
    public Type getType() { return type; }
    
    @Override
    public String toString() {
        return String.format("%s --[%s]--> %s", from.getName(), type, to.getName());
    }
}

// Reference tracker to handle circular references during serialization
class ReferenceTracker {
    private final Map<Object, Integer> objectToId;
    private final Map<Integer, Object> idToObject;
    private final AtomicInteger nextId;
    
    public ReferenceTracker() {
        this.objectToId = new IdentityHashMap<>();
        this.idToObject = new HashMap<>();
        this.nextId = new AtomicInteger(1);
    }
    
    public Integer getOrCreateId(Object obj) {
        return objectToId.computeIfAbsent(obj, k -> {
            int id = nextId.getAndIncrement();
            idToObject.put(id, k);
            return id;
        });
    }
    
    public boolean hasBeenSeen(Object obj) {
        return objectToId.containsKey(obj);
    }
    
    public Object getObjectById(int id) {
        return idToObject.get(id);
    }
}

// Object graph serializer
class ObjectGraphSerializer {
    private final ReferenceTracker tracker;
    private final StringBuilder json;
    
    public ObjectGraphSerializer() {
        this.tracker = new ReferenceTracker();
        this.json = new StringBuilder();
    }
    
    public String serialize(Object root) {
        json.setLength(0); // Reset
        json.append("{");
        serializeObject(root, 0);
        json.append("}");
        return json.toString();
    }
    
    private void serializeObject(Object obj, int indent) {
        if (obj == null) {
            json.append("null");
            return;
        }
        
        // Check for circular reference
        if (tracker.hasBeenSeen(obj)) {
            json.append("{\"$ref\":").append(tracker.getOrCreateId(obj)).append("}");
            return;
        }
        
        Integer id = tracker.getOrCreateId(obj);
        json.append("{\"$id\":").append(id).append(",");
        
        if (obj instanceof Person) {
            serializePerson((Person) obj, indent);
        } else if (obj instanceof Company) {
            serializeCompany((Company) obj, indent);
        } else if (obj instanceof Department) {
            serializeDepartment((Department) obj, indent);
        }
        
        json.append("}");
    }
    
    private void serializePerson(Person person, int indent) {
        json.append("\"type\":\"Person\",");
        json.append("\"id\":\"").append(person.getId()).append("\",");
        json.append("\"name\":\"").append(person.getName()).append("\",");
        json.append("\"age\":").append(person.getAge()).append(",");
        json.append("\"email\":\"").append(person.getEmail()).append("\",");
        
        // Serialize manager (with circular reference handling)
        json.append("\"manager\":");
        serializeObject(person.getManager(), indent + 1);
        json.append(",");
        
        // Serialize companies
        json.append("\"companies\":[");
        List<Company> companies = person.getCompanies();
        for (int i = 0; i < companies.size(); i++) {
            if (i > 0) json.append(",");
            serializeObject(companies.get(i), indent + 1);
        }
        json.append("]");
    }
    
    private void serializeCompany(Company company, int indent) {
        json.append("\"type\":\"Company\",");
        json.append("\"id\":\"").append(company.getId()).append("\",");
        json.append("\"name\":\"").append(company.getName()).append("\",");
        json.append("\"industry\":\"").append(company.getIndustry()).append("\",");
        json.append("\"foundedYear\":").append(company.getFoundedYear()).append(",");
        
        // Serialize employees
        json.append("\"employees\":[");
        List<Person> employees = company.getEmployees();
        for (int i = 0; i < employees.size(); i++) {
            if (i > 0) json.append(",");
            serializeObject(employees.get(i), indent + 1);
        }
        json.append("]");
    }
    
    private void serializeDepartment(Department department, int indent) {
        json.append("\"type\":\"Department\",");
        json.append("\"name\":\"").append(department.getName()).append("\",");
        
        json.append("\"head\":");
        serializeObject(department.getHead(), indent + 1);
        json.append(",");
        
        // Serialize members
        json.append("\"members\":[");
        List<Person> members = department.getMembers();
        for (int i = 0; i < members.size(); i++) {
            if (i > 0) json.append(",");
            serializeObject(members.get(i), indent + 1);
        }
        json.append("]");
    }
}

// Main class
public class ObjectGraphDemo {
    public static void main(String[] args) {
        // Create entities
        Person alice = new Person("P001", "Alice Smith", 30, "alice@company.com");
        Person bob = new Person("P002", "Bob Johnson", 35, "bob@company.com");
        Person charlie = new Person("P003", "Charlie Brown", 28, "charlie@company.com");
        
        Company techCorp = new Company("C001", "TechCorp", "Technology", 2000);
        Company innovationInc = new Company("C002", "Innovation Inc", "Consulting", 2010);
        
        // Create relationships
        alice.addCompany(techCorp);
        bob.addCompany(techCorp);
        charlie.addCompany(innovationInc);
        
        alice.setManager(bob); // Bob manages Alice
        
        // Create department
        Department engineering = new Department("Engineering");
        engineering.setHead(bob);
        engineering.addMember(alice);
        engineering.addMember(bob);
        
        techCorp.addDepartment("Engineering", engineering);
        
        // Display relationships
        System.out.println("=== Object Graph ===");
        System.out.println("People:");
        System.out.println("  " + alice);
        System.out.println("  " + bob);
        System.out.println("  " + charlie);
        
        System.out.println("\nCompanies:");
        System.out.println("  " + techCorp);
        System.out.println("  " + innovationInc);
        
        System.out.println("\nRelationships:");
        for (Person employee : techCorp.getEmployees()) {
            System.out.println("  " + employee.getName() + " works for " + techCorp.getName());
        }
        
        System.out.println("\nManager relationships:");
        for (Person employee : techCorp.getEmployees()) {
            if (employee.getManager() != null) {
                System.out.println("  " + employee.getName() + " reports to " + 
                                  employee.getManager().getName());
            }
        }
        
        // Serialize object graph
        System.out.println("\n=== Serialized Object Graph ===");
        ObjectGraphSerializer serializer = new ObjectGraphSerializer();
        String json = serializer.serialize(techCorp);
        System.out.println(json.substring(0, Math.min(500, json.length())) + "...");
        
        // Analyze object graph
        System.out.println("\n=== Object Graph Analysis ===");
        analyzeObjectGraph(techCorp);
    }
    
    private static void analyzeObjectGraph(Company company) {
        System.out.println("Company: " + company.getName());
        System.out.println("Industry: " + company.getIndustry());
        System.out.println("Founded: " + company.getFoundedYear());
        System.out.println("Employees: " + company.getEmployees().size());
        System.out.println("Departments: " + company.getDepartments().size());
        
        int totalRelationships = 0;
        for (Person employee : company.getEmployees()) {
            totalRelationships += employee.getCompanies().size();
            if (employee.getManager() != null) {
                totalRelationships++;
            }
        }
        System.out.println("Total relationships: " + totalRelationships);
    }
}
```

**Unit Tests:**
```java
package academy.javaengineering.oop.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ObjectGraphTest {
    private Person alice;
    private Person bob;
    private Company techCorp;
    
    @BeforeEach
    void setUp() {
        alice = new Person("P001", "Alice", 30, "alice@test.com");
        bob = new Person("P002", "Bob", 35, "bob@test.com");
        techCorp = new Company("C001", "TechCorp", "Tech", 2000);
    }
    
    @Test
    void testBidirectionalRelationship() {
        alice.addCompany(techCorp);
        
        assertTrue(alice.getCompanies().contains(techCorp));
        assertTrue(techCorp.getEmployees().contains(alice));
    }
    
    @Test
    void testCircularReferenceDetection() {
        ReferenceTracker tracker = new ReferenceTracker();
        
        tracker.getOrCreateId(alice);
        tracker.getOrCreateId(bob);
        alice.setManager(bob);
        
        assertTrue(tracker.hasBeenSeen(alice));
        assertTrue(tracker.hasBeenSeen(bob));
    }
    
    @Test
    void testSerializationWithCircularReference() {
        alice.setManager(bob);
        bob.addCompany(techCorp);
        alice.addCompany(techCorp);
        
        ObjectGraphSerializer serializer = new ObjectGraphSerializer();
        String json = serializer.serialize(techCorp);
        
        assertNotNull(json);
        assertTrue(json.contains("\"$id\""));
        assertTrue(json.contains("\"type\":\"Person\""));
        assertTrue(json.contains("\"type\":\"Company\""));
    }
    
    @Test
    void testDepartmentRelationships() {
        Department dept = new Department("Engineering");
        dept.setHead(bob);
        dept.addMember(alice);
        dept.addMember(bob);
        
        assertEquals("Engineering", dept.getName());
        assertEquals(bob, dept.getHead());
        assertTrue(dept.getMembers().contains(alice));
        assertTrue(dept.getMembers().contains(bob));
    }
}
```

**Execution Flow:**
1. Create entity objects (Person, Company, Department)
2. Establish relationships between objects
3. ReferenceTracker maintains object identity during serialization
4. ObjectGraphSerializer traverses the object graph
5. Circular references are detected and replaced with $ref pointers
6. JSON output represents the complete object graph with relationships

**Complexity:**
- Time: O(n) for serialization where n is number of objects
- Space: O(n) for storing object references and relationships

**Best Practices:**
- Use identity-based equality for object graphs
- Implement proper circular reference detection
- Use immutable objects where possible
- Separate serialization logic from business objects
- Consider performance implications of deep object graphs

## Exercises

### Easy
1. **Student Records**: Create Student objects with grades and calculate GPA. Create multiple students and compare their GPAs.

2. **Bank Account**: Design a BankAccount class and create multiple accounts. Transfer money between accounts.

3. **Product Catalog**: Create Product objects with different categories. Search for products by category and price range.

### Medium
1. **Library System**: Create Book, Member, and Loan objects. Manage book borrowing and returning with due dates.

2. **Vehicle Fleet**: Design Car, Truck, and Motorcycle objects. Track fuel consumption and maintenance schedules.

3. **Social Network**: Create User and Post objects. Implement friend relationships and news feed generation.

### Hard
1. **E-commerce Platform**: Design a complete object model with Product, Cart, Order, Payment, and Shipping objects. Handle complex relationships and state transitions.

2. **Airport System**: Create Flight, Plane, Passenger, and Gate objects. Manage boarding, delays, and gate assignments with real-time updates.

3. **Game Engine**: Design a game object model with Entity, Component, and System objects. Implement game logic with object hierarchies and component composition.

## Interview Questions

### Easy
1. **Q:** What is an object in Java?
   **A:** An object is an instance of a class. It is a runtime entity that combines state (data in fields) and behavior (methods). Objects are created on the heap memory and are accessed through references.

2. **Q:** How do you create an object in Java?
   **A:** Objects are created using the `new` keyword followed by a constructor call:
   ```java
   ClassName object = new ClassName();
   ClassName object = new ClassName(parameter1, parameter2);
   ```

3. **Q:** What is the difference between `==` and `.equals()`?
   **A:** `==` compares object references (whether they point to the same memory location). `.equals()` compares object values (the content of the objects). You should override `equals()` to compare object content.

### Medium
1. **Q:** Explain object lifecycle in Java.
   **A:** Object lifecycle:
   1. **Creation**: Object is created with `new` keyword, memory allocated on heap
   2. **Initialization**: Constructor initializes the object's state
   3. **Usage**: Object is used through its reference
   4. **Dereferencing**: Reference is set to null or goes out of scope
   5. **Garbage Collection**: JVM reclaims memory when no references exist

2. **Q:** What are the different ways to create objects in Java?
   **A:** Several ways:
   1. Using `new` keyword: `new ClassName()`
   2. Using factory methods: `ClassName.create()`
   3. Using clone method: `object.clone()`
   4. Using deserialization: `ObjectInputStream.readObject()`
   5. Using reflection: `ClassName.newInstance()`

3. **Q:** How does Java handle object memory management?
   **A:** Java uses automatic garbage collection. Objects are allocated on the heap. The JVM tracks references to objects. When an object has no more references, it becomes eligible for garbage collection. The garbage collector automatically reclaims memory, preventing memory leaks.

### Hard
1. **Q:** Explain the concept of object graphs and their challenges.
   **A:** An object graph is a network of objects connected by references. Challenges include:
   - **Circular references**: Objects can reference each other, causing infinite loops during traversal
   - **Serialization complexity**: Converting object graphs to bytes while maintaining relationships
   - **Memory leaks**: Objects in graphs can prevent garbage collection of other objects
   - **Performance**: Traversing large object graphs can be expensive
   
   Solutions include using identity maps, reference tracking, and proper serialization frameworks.

2. **Q:** What are the different types of object relationships?
   **A:** Object relationships:
   - **Association**: General relationship between objects
   - **Aggregation**: "Has-a" relationship where child can exist independently
   - **Composition**: Strong "has-a" where child cannot exist without parent
   - **Inheritance**: "Is-a" relationship (class subclass)
   - **Dependency**: One object depends on another for functionality

## Common Pitfalls

### Pitfall 1: Memory Leaks from Unused References
**Mistake:**
```java
// Bad: Holding unnecessary references
public class Cache {
    private Map<String, Object> cache = new HashMap<>();
    
    public void addToCache(String key, Object value) {
        cache.put(key, value);
    }
    
    // Cache grows indefinitely, causing memory leak
}

// Usage
Cache cache = new Cache();
for (int i = 0; i < 1000000; i++) {
    cache.addToCache("key" + i, new Object()); // Memory grows unbounded
}
```

**Correct:**
```java
// Good: Use bounded cache with eviction policy
public class BoundedCache {
    private final Map<String, Object> cache;
    private final int maxSize;
    
    public BoundedCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<String, Object>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                return size() > maxSize;
            }
        };
    }
    
    public void addToCache(String key, Object value) {
        cache.put(key, value);
    }
}
```

**Why:** Holding references to objects that are no longer needed prevents garbage collection, leading to memory leaks. Use bounded collections and remove references when objects are no longer needed.

### Pitfall 2: Not Implementing equals() and hashCode()
**Mistake:**
```java
// Bad: Using default Object equals() and hashCode()
public class Employee {
    private String id;
    private String name;
    
    // No equals() or hashCode() override
}

// Usage fails
Set<Employee> employees = new HashSet<>();
employees.add(new Employee("E001", "Alice"));
employees.add(new Employee("E001", "Alice")); // Added as separate object!

System.out.println(employees.size()); // 2, not 1 as expected
```

**Correct:**
```java
// Good: Proper equals() and hashCode() implementation
public class Employee {
    private String id;
    private String name;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return Objects.equals(id, employee.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

// Usage works correctly
Set<Employee> employees = new HashSet<>();
employees.add(new Employee("E001", "Alice"));
employees.add(new Employee("E001", "Alice")); // Same employee

System.out.println(employees.size()); // 1, as expected
```

**Why:** Collections like HashSet and HashMap rely on equals() and hashCode() to determine object equality and uniqueness. Without proper implementations, objects are compared by reference, not value.

### Pitfall 3: Creating Too Many Objects
**Mistake:**
```java
// Bad: Creating unnecessary objects in loops
public class StringProcessor {
    public void processStrings(List<String> strings) {
        for (String s : strings) {
            String processed = new String(s.toUpperCase()); // Unnecessary object
            System.out.println(processed);
        }
    }
}

// Bad: Using == for String comparison
public class StringComparer {
    public void compareStrings(String s1, String s2) {
        if (s1 == s2) { // Reference comparison
            System.out.println("Same reference");
        }
    }
}
```

**Correct:**
```java
// Good: Reuse objects and use .equals()
public class StringProcessor {
    public void processStrings(List<String> strings) {
        for (String s : strings) {
            String processed = s.toUpperCase(); // Reuse String method
            System.out.println(processed);
        }
    }
}

public class StringComparer {
    public void compareStrings(String s1, String s2) {
        if (s1.equals(s2)) { // Value comparison
            System.out.println("Same content");
        }
    }
}
```

**Why:** Creating unnecessary objects wastes memory and increases garbage collection overhead. Strings in Java are interned, so use .equals() for comparison, not ==.

## Best Practices
1. **Override equals() and hashCode()**: When objects need to be compared by value
2. **Use immutable objects**: When object state shouldn't change after creation
3. **Implement toString()**: For meaningful object representation in logs and debugging
4. **Manage object references**: Avoid holding references longer than necessary
5. **Use appropriate collections**: Choose collections based on object access patterns

## Real World Usage

### Spring Framework
Spring manages objects (beans) through IoC container:
- **Bean Lifecycle**: Objects are created, initialized, and destroyed by the container
- **Dependency Injection**: Objects receive their dependencies through constructor or setter injection
- **Scope**: Objects can be singleton, prototype, request, session, or application scoped

### Hibernate
Hibernate maps Java objects to database tables:
- **Entity Objects**: Represent database records as Java objects
- **Proxy Objects**: Lazy loading uses proxy objects to defer database access
- **Session Management**: Objects are managed within Hibernate sessions

### JDK Source Code
The JDK uses objects extensively:
- **Collection Objects**: ArrayList, HashMap, etc. are all objects
- **Wrapper Objects**: Integer, Double, etc. wrap primitive values
- **Thread Objects**: Threads are objects that execute concurrently

### Enterprise Applications
Production systems use objects for:
- **Domain Models**: Business entities as objects
- **Data Transfer Objects**: Objects that carry data between processes
- **Value Objects**: Immutable objects representing values
- **Service Objects**: Objects that provide business functionality

## References
- [Official Java Documentation](https://docs.oracle.com/en/java/)
- Effective Java by Joshua Bloch
- Head First Design Patterns by Eric Freeman
- Clean Code by Robert C. Martin

## Summary
- Objects are instances of classes that combine state and behavior
- Objects are created on the heap and accessed through references
- Object lifecycle includes creation, initialization, usage, and garbage collection
- Proper equals() and hashCode() implementations are essential for collections
- Object composition enables flexible and reusable designs
- Memory management is handled automatically by Java's garbage collector

---
**Next Topic:** [Constructors](../04-constructors/)
**Previous Topic:** [Classes](../02-classes/)