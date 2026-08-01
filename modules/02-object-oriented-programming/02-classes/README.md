# Classes

A class in Java is a blueprint or template for creating objects. It defines the properties (fields) and behaviors (methods) that objects of that type will have. Classes are fundamental building blocks of object-oriented programming, allowing developers to model real-world entities and concepts in code. When you create a class, you define what an object of that type will look like, but the class itself is not an object—it's the template from which objects are created.

## Learning Objectives
By the end of this topic, you will be able to:
- Understand the structure and components of a Java class
- Define classes with appropriate fields, constructors, and methods
- Differentiate between instance and static members
- Apply access modifiers to control visibility of class members

## Prerequisites
- [Introduction to OOP](../01-introduction/)
- Basic Java syntax and programming concepts
- Understanding of variables and data types

## Why This Concept Exists

### The Problem
Without classes, programmers had to manage data and functions separately. This led to:
- **No data grouping**: Related data and functions were scattered throughout code
- **Code duplication**: Similar structures were repeated without reuse
- **Lack of structure**: Programs became difficult to organize and maintain

### The Solution
Classes provide a structured way to:
- **Bundle data and behavior**: Group related fields and methods together
- **Create reusable templates**: Define once, instantiate multiple times
- **Enforce encapsulation**: Control access to internal state
- **Enable inheritance**: Create specialized versions of existing classes

### Real-World Analogy
Think of a class as an architectural blueprint for a house. The blueprint defines:
- The number and size of rooms (fields)
- How rooms connect (methods)
- The overall structure (class definition)

You can build many houses from the same blueprint, each with different furniture and decorations (different object instances), but they all follow the same structural design.

## Internal Working

### JVM Perspective
When the JVM encounters a class definition:
1. **Class Loading**: The classloader reads the .class file and loads it into memory
2. **Verification**: The bytecode verifier ensures the class file is valid
3. **Preparation**: JVM allocates memory for static variables and initializes them
4. **Resolution**: Symbolic references are replaced with direct references
5. **Initialization**: Static initializers and static variable assignments are executed

### Memory Representation
```java
public class Student {
    String name;      // Instance variable
    int age;         // Instance variable
    static int count; // Static variable (shared)
}

// When we create objects:
Student s1 = new Student();
Student s2 = new Student();

// Memory layout:
// Method Area: Student.class loaded
// Heap: 
//   - s1 object: {name: null, age: 0}
//   - s2 object: {name: null, age: 0}
//   - Student.count (static): 0
// Stack:
//   - s1 reference -> points to s1 object
//   - s2 reference -> points to s2 object
```

## Syntax

```java
// Basic class definition
public class ClassName {
    // Instance variables (fields)
    private dataType fieldName;
    
    // Static variables (class variables)
    private static dataType staticFieldName;
    
    // Constructor
    public ClassName(parameters) {
        // Initialization code
    }
    
    // Instance method
    public void instanceMethod() {
        // Method body
    }
    
    // Static method
    public static void staticMethod() {
        // Method body
    }
    
    // Getter
    public dataType getFieldName() {
        return fieldName;
    }
    
    // Setter
    public void setFieldName(dataType value) {
        this.fieldName = value;
    }
}
```

## Easy Examples

### Example 1: Basic Car Class
**Problem Statement:**
Create a class to represent a car with basic properties and behaviors.

**Implementation:**
```java
package academy.javaengineering.oop.classes;

public class Car {
    // Instance variables
    private String make;
    private String model;
    private int year;
    private double speed;
    
    // Constructor
    public Car(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.speed = 0.0;
    }
    
    // Instance methods
    public void accelerate(double amount) {
        if (amount > 0) {
            speed += amount;
            System.out.println("Accelerating. Current speed: " + speed + " mph");
        }
    }
    
    public void brake(double amount) {
        if (amount > 0 && speed >= amount) {
            speed -= amount;
            System.out.println("Braking. Current speed: " + speed + " mph");
        } else if (speed < amount) {
            speed = 0;
            System.out.println("Car stopped.");
        }
    }
    
    public void displayInfo() {
        System.out.println(year + " " + make + " " + model + " - Speed: " + speed + " mph");
    }
    
    // Getters
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getSpeed() { return speed; }
    
    // Main method to test
    public static void main(String[] args) {
        Car myCar = new Car("Toyota", "Camry", 2023);
        Car yourCar = new Car("Honda", "Civic", 2022);
        
        myCar.displayInfo();
        myCar.accelerate(60);
        myCar.brake(20);
        myCar.displayInfo();
        
        System.out.println();
        yourCar.displayInfo();
    }
}
```

**Expected Output:**
```
2023 Toyota Camry - Speed: 0.0 mph
Accelerating. Current speed: 60.0 mph
Braking. Current speed: 40.0 mph
2023 Toyota Camry - Speed: 40.0 mph

2022 Honda Civic - Speed: 0.0 mph
```

**Best Practices:**
- Use descriptive names for classes (nouns) and methods (verbs)
- Initialize all instance variables in constructors
- Provide getters for private fields that need to be read

### Example 2: Student Class with Static Members
**Problem Statement:**
Create a Student class that tracks the total number of students enrolled.

**Implementation:**
```java
package academy.javaengineering.oop.classes;

public class Student {
    // Instance variables
    private String name;
    private int id;
    private double gpa;
    
    // Static variable to track total students
    private static int totalStudents = 0;
    private static int nextId = 1000;
    
    // Constructor
    public Student(String name, double gpa) {
        this.name = name;
        this.id = nextId++;
        this.gpa = gpa;
        totalStudents++;
    }
    
    // Instance method
    public boolean isHonors() {
        return gpa >= 3.5;
    }
    
    public void displayInfo() {
        System.out.println("ID: " + id + ", Name: " + name + 
                          ", GPA: " + gpa + 
                          (isHonors() ? " (Honors)" : ""));
    }
    
    // Static method
    public static int getTotalStudents() {
        return totalStudents;
    }
    
    public static void main(String[] args) {
        Student student1 = new Student("Alice", 3.8);
        Student student2 = new Student("Bob", 3.2);
        Student student3 = new Student("Charlie", 3.9);
        
        student1.displayInfo();
        student2.displayInfo();
        student3.displayInfo();
        
        System.out.println("\nTotal students enrolled: " + Student.getTotalStudents());
    }
}
```

**Expected Output:**
```
ID: 1000, Name: Alice, GPA: 3.8 (Honors)
ID: 1001, Name: Bob, GPA: 3.2
ID: 1002, Name: Charlie, GPA: 3.9 (Honors)

Total students enrolled: 3
```

**Best Practices:**
- Use static members for data shared across all instances
- Use static methods for utility functions that don't need object state
- Initialize static variables appropriately

## Medium Examples

### Example 1: Bank Account with Validation
**Problem Statement:**
Create a BankAccount class with proper validation, transaction history, and interest calculation.
**Requirements:**
- Validate account holder name and initial balance
- Support deposits and withdrawals with validation
- Track transaction history
- Calculate monthly interest based on account type

**Implementation:**
```java
package academy.javaengineering.oop.classes;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankAccount {
    // Instance variables
    private String accountNumber;
    private String holderName;
    private double balance;
    private AccountType accountType;
    private List<String> transactionHistory;
    private LocalDateTime createdDate;
    
    // Static variables
    private static int accountCounter = 0;
    private static final double SAVINGS_INTEREST_RATE = 0.02;
    private static final double CHECKING_INTEREST_RATE = 0.005;
    
    // Enum for account types
    public enum AccountType {
        SAVINGS, CHECKING
    }
    
    // Constructor
    public BankAccount(String holderName, double initialBalance, AccountType accountType) {
        // Validation
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name cannot be null or empty");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        
        this.holderName = holderName;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.accountNumber = generateAccountNumber();
        this.transactionHistory = new ArrayList<>();
        this.createdDate = LocalDateTime.now();
        
        // Record initial deposit if balance > 0
        if (initialBalance > 0) {
            addTransaction("Initial deposit: $" + initialBalance);
        }
    }
    
    // Private helper method
    private String generateAccountNumber() {
        accountCounter++;
        return String.format("ACC-%06d", accountCounter);
    }
    
    private void addTransaction(String description) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        transactionHistory.add(timestamp + " - " + description);
    }
    
    // Public methods
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        balance += amount;
        addTransaction("Deposit: $" + String.format("%.2f", amount));
        System.out.println("Successfully deposited $" + amount);
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds. Current balance: $" + balance);
        }
        
        balance -= amount;
        addTransaction("Withdrawal: $" + String.format("%.2f", amount));
        System.out.println("Successfully withdrew $" + amount);
    }
    
    public double calculateInterest() {
        double rate = (accountType == AccountType.SAVINGS) ? 
                     SAVINGS_INTEREST_RATE : CHECKING_INTEREST_RATE;
        return balance * rate;
    }
    
    public void applyInterest() {
        double interest = calculateInterest();
        balance += interest;
        addTransaction("Interest applied: $" + String.format("%.2f", interest));
        System.out.println("Interest applied: $" + String.format("%.2f", interest));
    }
    
    // Display methods
    public void displayAccountInfo() {
        System.out.println("=== Account Information ===");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Holder: " + holderName);
        System.out.println("Type: " + accountType);
        System.out.printf("Balance: $%.2f%n", balance);
        System.out.println("Created: " + createdDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    public void displayTransactionHistory() {
        System.out.println("=== Transaction History ===");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public AccountType getAccountType() { return accountType; }
    
    public static void main(String[] args) {
        try {
            // Create accounts
            BankAccount savings = new BankAccount("Alice Smith", 5000.00, BankAccount.AccountType.SAVINGS);
            BankAccount checking = new BankAccount("Bob Johnson", 1500.00, BankAccount.AccountType.CHECKING);
            
            // Perform operations
            savings.displayAccountInfo();
            System.out.println();
            
            savings.deposit(1000.00);
            savings.withdraw(500.00);
            savings.applyInterest();
            System.out.println();
            
            savings.displayAccountInfo();
            System.out.println();
            savings.displayTransactionHistory();
            
            System.out.println();
            
            // Test validation
            checking.withdraw(2000.00); // Should fail
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Code Walkthrough:**
1. We define a `BankAccount` class with private instance variables for encapsulation
2. An `AccountType` enum restricts account types to SAVINGS or CHECKING
3. The constructor validates inputs and initializes the account
4. Instance methods perform operations and maintain transaction history
5. Static variables and methods manage class-level data
6. Helper methods keep the code clean and maintainable

**Expected Output:**
```
=== Account Information ===
Account Number: ACC-000001
Holder: Alice Smith
Type: SAVINGS
Balance: $5000.00
Created: 2024-01-15 10:30:45

Successfully deposited $1000.0
Successfully withdrew $500.0
Interest applied: $110.00

=== Account Information ===
Account Number: ACC-000001
Holder: Alice Smith
Type: SAVINGS
Balance: $5610.00
Created: 2024-01-15 10:30:45

=== Transaction History ===
2024-01-15 10:30:45 - Initial deposit: $5000.0
2024-01-15 10:30:45 - Deposit: $1000.00
2024-01-15 10:30:45 - Withdrawal: $500.00
2024-01-15 10:30:45 - Interest applied: $110.00

Error: Insufficient funds. Current balance: $1500.0
```

**Alternative Solution:**
```java
// Using Builder pattern for complex object creation
public class BankAccountBuilder {
    private String holderName;
    private double initialBalance = 0.0;
    private BankAccount.AccountType accountType = BankAccount.AccountType.CHECKING;
    
    public BankAccountBuilder setHolderName(String holderName) {
        this.holderName = holderName;
        return this;
    }
    
    public BankAccountBuilder setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
        return this;
    }
    
    public BankAccountBuilder setAccountType(BankAccount.AccountType accountType) {
        this.accountType = accountType;
        return this;
    }
    
    public BankAccount build() {
        return new BankAccount(holderName, initialBalance, accountType);
    }
}

// Usage:
BankAccount account = new BankAccountBuilder()
    .setHolderName("Charlie Brown")
    .setInitialBalance(2500.00)
    .setAccountType(BankAccount.AccountType.SAVINGS)
    .build();
```

## Hard Examples

### Example 1: University Course Management System
**Problem Statement:**
Design a comprehensive course management system for a university with courses, students, professors, and enrollments.
**Requirements:**
- Support different course types (lecture, lab, seminar)
- Manage professor assignments and student enrollments
- Handle prerequisites and grading
- Generate reports and statistics

**Architecture:**
```
course-management/
├── models/
│   ├── Course.java
│   ├── LectureCourse.java
│   ├── LabCourse.java
│   ├── SeminarCourse.java
│   ├── Student.java
│   ├── Professor.java
│   └── Enrollment.java
├── services/
│   ├── CourseService.java
│   ├── EnrollmentService.java
│   └── GradingService.java
├── exceptions/
│   ├── PrerequisiteException.java
│   ├── EnrollmentException.java
│   └── CapacityException.java
└── Main.java
```

**Implementation:**
```java
package academy.javaengineering.oop.classes;

import java.util.*;
import java.time.LocalDateTime;

// Abstract Course class
public abstract class Course {
    private String courseCode;
    private String title;
    private String description;
    private int credits;
    private int maxCapacity;
    private Professor professor;
    private List<String> prerequisites;
    private Map<String, Double> gradingCriteria;
    
    public Course(String courseCode, String title, String description, 
                  int credits, int maxCapacity) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.credits = credits;
        this.maxCapacity = maxCapacity;
        this.prerequisites = new ArrayList<>();
        this.gradingCriteria = new LinkedHashMap<>();
        initializeGradingCriteria();
    }
    
    protected abstract void initializeGradingCriteria();
    
    public abstract String getCourseType();
    
    public boolean hasPrerequisitesMet(List<String> completedCourses) {
        return completedCourses.containsAll(prerequisites);
    }
    
    // Getters and setters
    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public int getMaxCapacity() { return maxCapacity; }
    public Professor getProfessor() { return professor; }
    
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
    
    public void addPrerequisite(String courseCode) {
        prerequisites.add(courseCode);
    }
    
    public Map<String, Double> getGradingCriteria() {
        return Collections.unmodifiableMap(gradingCriteria);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s) [%d credits] - %s",
            courseCode, title, getCourseType(), credits,
            professor != null ? professor.getName() : "TBA");
    }
}

// Lecture Course
public class LectureCourse extends Course {
    private boolean hasFinalExam;
    
    public LectureCourse(String courseCode, String title, String description,
                        int credits, int maxCapacity, boolean hasFinalExam) {
        super(courseCode, title, description, credits, maxCapacity);
        this.hasFinalExam = hasFinalExam;
    }
    
    @Override
    protected void initializeGradingCriteria() {
        getGradingCriteria().put("Midterm", 0.30);
        getGradingCriteria().put("Assignments", 0.30);
        getGradingCriteria().put("Participation", 0.10);
        if (hasFinalExam) {
            getGradingCriteria().put("Final Exam", 0.30);
        } else {
            getGradingCriteria().put("Project", 0.30);
        }
    }
    
    @Override
    public String getCourseType() {
        return "Lecture";
    }
    
    public boolean hasFinalExam() { return hasFinalExam; }
}

// Lab Course
public class LabCourse extends Course {
    private int labHours;
    private String labRoom;
    
    public LabCourse(String courseCode, String title, String description,
                    int credits, int maxCapacity, int labHours, String labRoom) {
        super(courseCode, title, description, credits, maxCapacity);
        this.labHours = labHours;
        this.labRoom = labRoom;
    }
    
    @Override
    protected void initializeGradingCriteria() {
        getGradingCriteria().put("Lab Reports", 0.40);
        getGradingCriteria().put("Lab Participation", 0.20);
        getGradingCriteria().put("Lab Final", 0.40);
    }
    
    @Override
    public String getCourseType() {
        return "Lab";
    }
    
    public int getLabHours() { return labHours; }
    public String getLabRoom() { return labRoom; }
}

// Professor class
public class Professor {
    private String professorId;
    private String name;
    private String department;
    private List<Course> courses;
    
    public Professor(String professorId, String name, String department) {
        this.professorId = professorId;
        this.name = name;
        this.department = department;
        this.courses = new ArrayList<>();
    }
    
    public void assignCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.setProfessor(this);
        }
    }
    
    public String getProfessorId() { return professorId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public List<Course> getCourses() { return Collections.unmodifiableList(courses); }
    
    @Override
    public String toString() {
        return String.format("Prof. %s (%s)", name, department);
    }
}

// Student class
public class Student {
    private String studentId;
    private String name;
    private String email;
    private int enrollmentYear;
    private List<String> completedCourses;
    private Map<String, Double> courseGrades;
    
    public Student(String studentId, String name, String email, int enrollmentYear) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.enrollmentYear = enrollmentYear;
        this.completedCourses = new ArrayList<>();
        this.courseGrades = new HashMap<>();
    }
    
    public boolean canEnrollIn(Course course) {
        return course.hasPrerequisitesMet(completedCourses);
    }
    
    public void completeCourse(String courseCode, double grade) {
        completedCourses.add(courseCode);
        courseGrades.put(courseCode, grade);
    }
    
    public double calculateGPA() {
        if (courseGrades.isEmpty()) return 0.0;
        
        double totalPoints = 0.0;
        for (double grade : courseGrades.values()) {
            totalPoints += gradeToPoints(grade);
        }
        return totalPoints / courseGrades.size();
    }
    
    private double gradeToPoints(double grade) {
        if (grade >= 90) return 4.0;
        if (grade >= 80) return 3.0;
        if (grade >= 70) return 2.0;
        if (grade >= 60) return 1.0;
        return 0.0;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public List<String> getCompletedCourses() { return completedCourses; }
    public Map<String, Double> getCourseGrades() { return courseGrades; }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - GPA: %.2f", name, studentId, calculateGPA());
    }
}

// Enrollment class
public class Enrollment {
    private Student student;
    private Course course;
    private LocalDateTime enrollmentDate;
    private boolean active;
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
        this.active = true;
    }
    
    public void drop() {
        this.active = false;
    }
    
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public boolean isActive() { return active; }
    
    @Override
    public String toString() {
        return String.format("%s enrolled in %s on %s",
            student.getName(), course.getCourseCode(),
            enrollmentDate.toLocalDate());
    }
}

// Course Service
public class CourseService {
    private Map<String, Course> courses;
    private Map<String, Professor> professors;
    private Map<String, Student> students;
    private List<Enrollment> enrollments;
    
    public CourseService() {
        this.courses = new HashMap<>();
        this.professors = new HashMap<>();
        this.students = new HashMap<>();
        this.enrollments = new ArrayList<>();
    }
    
    public void addCourse(Course course) {
        courses.put(course.getCourseCode(), course);
    }
    
    public void addProfessor(Professor professor) {
        professors.put(professor.getProfessorId(), professor);
    }
    
    public void addStudent(Student student) {
        students.put(student.getStudentId(), student);
    }
    
    public Enrollment enrollStudent(String studentId, String courseCode) throws Exception {
        Student student = students.get(studentId);
        Course course = courses.get(courseCode);
        
        if (student == null) {
            throw new Exception("Student not found: " + studentId);
        }
        
        if (course == null) {
            throw new Exception("Course not found: " + courseCode);
        }
        
        if (!student.canEnrollIn(course)) {
            throw new Exception("Student does not meet prerequisites for " + courseCode);
        }
        
        // Check capacity
        long activeEnrollments = enrollments.stream()
            .filter(e -> e.getCourse().equals(course) && e.isActive())
            .count();
        
        if (activeEnrollments >= course.getMaxCapacity()) {
            throw new Exception("Course " + courseCode + " is at full capacity");
        }
        
        // Check if already enrolled
        boolean alreadyEnrolled = enrollments.stream()
            .anyMatch(e -> e.getStudent().equals(student) && 
                          e.getCourse().equals(course) && 
                          e.isActive());
        
        if (alreadyEnrolled) {
            throw new Exception("Student is already enrolled in " + courseCode);
        }
        
        Enrollment enrollment = new Enrollment(student, course);
        enrollments.add(enrollment);
        return enrollment;
    }
    
    public void dropCourse(String studentId, String courseCode) throws Exception {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getStudentId().equals(studentId) &&
                enrollment.getCourse().getCourseCode().equals(courseCode) &&
                enrollment.isActive()) {
                enrollment.drop();
                return;
            }
        }
        throw new Exception("No active enrollment found");
    }
    
    public List<Enrollment> getStudentEnrollments(String studentId) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getStudentId().equals(studentId) &&
                enrollment.isActive()) {
                result.add(enrollment);
            }
        }
        return result;
    }
    
    public void displayCourseStatistics() {
        System.out.println("=== Course Statistics ===");
        for (Course course : courses.values()) {
            long enrolled = enrollments.stream()
                .filter(e -> e.getCourse().equals(course) && e.isActive())
                .count();
            System.out.printf("%s: %d/%d enrolled%n",
                course.getCourseCode(), enrolled, course.getMaxCapacity());
        }
    }
}

// Main class
public class UniversitySystem {
    public static void main(String[] args) {
        CourseService service = new CourseService();
        
        // Create professors
        Professor profSmith = new Professor("P001", "John Smith", "Computer Science");
        Professor profJohnson = new Professor("P002", "Emily Johnson", "Mathematics");
        
        service.addProfessor(profSmith);
        service.addProfessor(profJohnson);
        
        // Create courses
        Course cs101 = new LectureCourse("CS101", "Intro to Programming", 
            "Basic programming concepts", 3, 30, true);
        Course cs201 = new LabCourse("CS201", "Programming Lab", 
            "Hands-on programming", 2, 20, 3, "Lab 101");
        Course math101 = new LectureCourse("MATH101", "Calculus I", 
            "Introduction to calculus", 4, 40, true);
        
        // Set prerequisites
        cs201.addPrerequisite("CS101");
        
        // Assign professors
        profSmith.assignCourse(cs101);
        profSmith.assignCourse(cs201);
        profJohnson.assignCourse(math101);
        
        // Add courses to service
        service.addCourse(cs101);
        service.addCourse(cs201);
        service.addCourse(math101);
        
        // Create students
        Student alice = new Student("S001", "Alice", "alice@university.edu", 2023);
        Student bob = new Student("S002", "Bob", "bob@university.edu", 2023);
        
        service.addStudent(alice);
        service.addStudent(bob);
        
        // Enroll students
        try {
            System.out.println("=== Enrollment Results ===");
            Enrollment e1 = service.enrollStudent("S001", "CS101");
            System.out.println("✓ " + e1);
            
            Enrollment e2 = service.enrollStudent("S001", "MATH101");
            System.out.println("✓ " + e2);
            
            Enrollment e3 = service.enrollStudent("S002", "CS101");
            System.out.println("✓ " + e3);
            
            // Try to enroll in CS201 without prerequisite
            Enrollment e4 = service.enrollStudent("S002", "CS201");
            System.out.println("✓ " + e4);
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        
        System.out.println();
        
        // Display statistics
        service.displayCourseStatistics();
        
        System.out.println();
        
        // Display student enrollments
        System.out.println("=== Alice's Enrollments ===");
        for (Enrollment enrollment : service.getStudentEnrollments("S001")) {
            System.out.println(enrollment);
        }
    }
}
```

**Unit Tests:**
```java
package academy.javaengineering.oop.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {
    private CourseService service;
    private Student student;
    private Course course;
    
    @BeforeEach
    void setUp() {
        service = new CourseService();
        student = new Student("S001", "Test Student", "test@email.com", 2023);
        course = new LectureCourse("CS101", "Test Course", "Description", 3, 30, true);
        
        service.addStudent(student);
        service.addCourse(course);
    }
    
    @Test
    void testSuccessfulEnrollment() throws Exception {
        Enrollment enrollment = service.enrollStudent("S001", "CS101");
        assertNotNull(enrollment);
        assertTrue(enrollment.isActive());
    }
    
    @Test
    void testDuplicateEnrollment() {
        assertThrows(Exception.class, () -> {
            service.enrollStudent("S001", "CS101");
            service.enrollStudent("S001", "CS101"); // Should fail
        });
    }
    
    @Test
    void testPrerequisiteCheck() {
        Course advancedCourse = new LectureCourse("CS201", "Advanced", "Desc", 3, 30, true);
        advancedCourse.addPrerequisite("CS101");
        service.addCourse(advancedCourse);
        
        assertThrows(Exception.class, () -> {
            service.enrollStudent("S001", "CS201"); // Should fail - no prerequisite
        });
    }
    
    @Test
    void testCourseCapacity() {
        Course smallCourse = new LectureCourse("SMALL", "Small", "Desc", 3, 1, true);
        service.addCourse(smallCourse);
        
        Student student2 = new Student("S002", "Student2", "s2@email.com", 2023);
        service.addStudent(student2);
        
        assertThrows(Exception.class, () -> {
            service.enrollStudent("S001", "SMALL");
            service.enrollStudent("S002", "SMALL"); // Should fail - capacity
        });
    }
    
    @Test
    void testDropCourse() throws Exception {
        service.enrollStudent("S001", "CS101");
        service.dropCourse("S001", "CS101");
        
        List<Enrollment> enrollments = service.getStudentEnrollments("S001");
        assertTrue(enrollments.isEmpty());
    }
}
```

**Execution Flow:**
1. Course service is initialized with empty collections
2. Professors are created and assigned to courses
3. Courses are created with different types and prerequisites
4. Students are registered in the system
5. Enrollment process validates prerequisites and capacity
6. Enrollments are tracked and can be dropped
7. Statistics and reports are generated

**Complexity:**
- Time: O(1) for add operations, O(n) for search and enrollment checks
- Space: O(n) where n is the number of students, courses, and enrollments

**Best Practices:**
- Use abstract classes for common behavior with specialized implementations
- Implement proper validation at all levels
- Use enums for fixed sets of values
- Separate concerns into different classes and services
- Use immutable objects where possible

## Exercises

### Easy
1. **Library Book**: Create a `Book` class with title, author, ISBN, and availability status. Methods to check out and return books.

2. **Rectangle**: Design a `Rectangle` class with width and height. Include methods for area, perimeter, and diagonal calculation.

3. **Employee**: Create an `Employee` class with name, ID, position, and salary. Methods to give raises and display information.

### Medium
1. **Banking System**: Design a complete banking system with different account types (savings, checking, business), transaction history, and interest calculation.

2. **Vehicle Fleet**: Create a vehicle fleet management system with different vehicle types, maintenance tracking, and fuel efficiency calculations.

3. **Inventory System**: Design a retail inventory system with products, categories, suppliers, and stock management.

### Hard
1. **Hospital Management**: Create a hospital management system with patients, doctors, appointments, medical records, and billing.

2. **E-commerce Platform**: Design a complete e-commerce system with products, shopping carts, orders, payments, and shipping.

3. **Airline Reservation**: Create an airline reservation system with flights, seats, passengers, and booking management.

## Interview Questions

### Easy
1. **Q:** What is a class in Java?
   **A:** A class is a blueprint or template for creating objects. It defines the properties (fields) and behaviors (methods) that objects of that type will have. A class is not an object itself but the template from which objects are created.

2. **Q:** What is the difference between a class and an object?
   **A:** A class is a definition or blueprint, while an object is an instance of a class. A class defines what properties and methods an object will have, while an object is a specific instance with actual values for those properties.

3. **Q:** What are instance variables?
   **A:** Instance variables are variables declared in a class that are unique to each object instance. Each object has its own copy of instance variables, which store the object's state.

### Medium
1. **Q:** Explain the difference between instance and static members.
   **A:** Instance members (variables and methods) belong to each object instance. Each object has its own copy of instance variables. Static members belong to the class itself and are shared across all instances. Static variables have one copy for all objects, and static methods can be called without creating an object.

2. **Q:** What is the purpose of access modifiers in Java?
   **A:** Access modifiers control the visibility and accessibility of classes, methods, and variables. They are:
   - `public`: Accessible from anywhere
   - `protected`: Accessible within the same package and subclasses
   - `default` (no modifier): Accessible only within the same package
   - `private`: Accessible only within the same class
   
   They enable encapsulation by hiding internal implementation details.

3. **Q:** How do you create a copy of an object?
   **A:** There are several ways:
   1. Copy constructor: `public ClassName(ClassName other) { ... }`
   2. Clone method: Implement `Cloneable` interface and override `clone()`
   3. Copy fields manually: Create new object and copy each field
   4. Use serialization/deserialization for deep copies
   
   The best approach depends on whether you need a shallow or deep copy.

### Hard
1. **Q:** Explain the Singleton pattern and its implementation.
   **A:** The Singleton pattern ensures a class has only one instance and provides a global point of access to it. Implementation:
   ```java
   public class Singleton {
       private static Singleton instance;
       private Singleton() {} // Private constructor
       
       public static synchronized Singleton getInstance() {
           if (instance == null) {
               instance = new Singleton();
           }
           return instance;
       }
   }
   ```
   Use cases: Database connections, configuration managers, logging services. Consider double-checked locking for thread safety.

2. **Q:** What is the difference between shallow and deep copy?
   **A:** **Shallow copy** copies the object's fields but not the objects referenced by those fields. If a field is a reference, both the original and copy point to the same object. **Deep copy** recursively copies all objects referenced by the fields. Changes to one copy don't affect the other.
   
   Example:
   ```java
   // Shallow copy
   public Person shallowCopy() {
       return new Person(this.name, this.address); // Same Address object
   
   // Deep copy
   public Person deepCopy() {
       return new Person(this.name, new Address(this.address)); // New Address object
   }
   ```

3. **Q:** How does Java handle memory management for objects?
   **A:** Java uses automatic garbage collection. Objects are allocated on the heap when created with `new`. The JVM tracks references to objects. When an object has no more references, it becomes eligible for garbage collection. The garbage collector automatically reclaims memory, preventing memory leaks. You can suggest garbage collection with `System.gc()`, but it's not guaranteed. Use `finalize()` method (deprecated) or `AutoCloseable` interface for cleanup.

## Common Pitfalls

### Pitfall 1: Creating Mutable Static State
**Mistake:**
```java
// Bad: Mutable static state shared across all instances
public class User {
    private static List<String> allUsers = new ArrayList<>();
    private String name;
    
    public User(String name) {
        this.name = name;
        allUsers.add(name); // Shared state
    }
    
    public static void removeAllUsers() {
        allUsers.clear(); // Affects all instances
    }
}
```

**Correct:**
```java
// Good: Immutable or properly managed static state
public class User {
    private static final List<String> allUsers = new CopyOnWriteArrayList<>();
    private final String name; // Immutable
    
    public User(String name) {
        this.name = name;
        allUsers.add(name);
    }
    
    public static List<String> getAllUsers() {
        return Collections.unmodifiableList(allUsers); // Return unmodifiable view
    }
    
    // No remove method - static state is managed carefully
}
```

**Why:** Mutable static state can lead to unpredictable behavior, especially in multi-threaded environments. It makes testing difficult and can cause memory leaks.

### Pitfall 2: Not Using Encapsulation
**Mistake:**
```java
// Bad: Exposing internal state
public class Account {
    public double balance; // Public field!
    public String owner;
    
    public void withdraw(double amount) {
        balance -= amount; // No validation
    }
}

// External code can corrupt state
Account account = new Account();
account.balance = -1000; // Invalid state
account.owner = null;    // Invalid state
```

**Correct:**
```java
// Good: Proper encapsulation
public class Account {
    private double balance;
    private String owner;
    
    public Account(String owner, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.owner = owner;
        this.balance = initialBalance;
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getOwner() {
        return owner;
    }
}
```

**Why:** Encapsulation protects object integrity by controlling how internal state is accessed and modified. It prevents invalid states and maintains invariants.

### Pitfall 3: Ignoring Object Equality
**Mistake:**
```java
// Bad: Using == for object comparison
public class Person {
    String name;
    int age;
    
    public boolean equals(Person other) {
        return this.name == other.name && this.age == other.age;
    }
}

// Usage fails
Person p1 = new Person("Alice", 25);
Person p2 = new Person("Alice", 25);
System.out.println(p1.equals(p2)); // false - different objects
```

**Correct:**
```java
// Good: Proper equals implementation
public class Person {
    private final String name;
    private final int age;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

// Usage works
Person p1 = new Person("Alice", 25);
Person p2 = new Person("Alice", 25);
System.out.println(p1.equals(p2)); // true
```

**Why:** In Java, `==` compares object references, not values. To compare object content, you must override `equals()` and `hashCode()` methods.

## Best Practices
1. **Follow Naming Conventions**: Use PascalCase for class names, camelCase for methods and variables
2. **Keep Classes Focused**: Each class should have a single responsibility (Single Responsibility Principle)
3. **Use Appropriate Access Modifiers**: Make fields private and provide controlled access through methods
4. **Initialize Properly**: Always initialize instance variables through constructors or initializers
5. **Override toString()**: Provide meaningful string representation for debugging and logging

## Real World Usage

### Spring Framework
Spring uses classes extensively:
- **Bean Classes**: Spring beans are Java classes managed by the IoC container
- **Component Classes**: `@Component`, `@Service`, `@Repository` are class annotations
- **Configuration Classes**: `@Configuration` classes define bean factories

### Hibernate
Hibernate relies on classes for ORM:
- **Entity Classes**: Java classes map to database tables
- **POJO Classes**: Plain Old Java Objects represent database records
- **Component Classes**: Reusable parts of entity classes

### JDK Source Code
The JDK itself uses classes throughout:
- **Collection Classes**: `ArrayList`, `HashMap`, `LinkedList` are all classes
- **IO Classes**: `File`, `InputStream`, `OutputStream` represent IO operations
- **GUI Classes**: Swing and JavaFX use class hierarchies for UI components

### Enterprise Applications
Production systems use classes for:
- **Domain Models**: Business entities are represented as classes
- **Service Layers**: Business logic is encapsulated in service classes
- **Data Transfer Objects**: Classes that carry data between processes
- **Value Objects**: Immutable classes representing values

## References
- [Official Java Documentation](https://docs.oracle.com/en/java/)
- Effective Java by Joshua Bloch
- Head First Design Patterns by Eric Freeman
- Clean Code by Robert C. Martin

## Summary
- Classes are blueprints for creating objects
- They contain fields (state) and methods (behavior)
- Access modifiers control visibility and encapsulation
- Static members belong to the class, not instances
- Proper class design leads to maintainable and reusable code
- Classes enable the four pillars of OOP: encapsulation, inheritance, polymorphism, and abstraction

---
**Next Topic:** [Objects](../03-objects/)
**Previous Topic:** [Introduction to OOP](../01-introduction/)