# Association

## Introduction

Association is a fundamental relationship between two or more classes in object-oriented programming that establishes a connection between objects, defining how they interact with each other and what roles they play in the relationship. Unlike composition and aggregation which represent specific types of associations, association is the broadest term that encompasses any relationship between objects, including both structural and behavioral connections. Association can be unidirectional (one class knows about another) or bidirectional (both classes know about each other), and can have different multiplicities (one-to-one, one-to-many, many-to-many). Understanding associations is crucial for designing object-oriented systems because they define how objects collaborate to fulfill system requirements, and the strength and direction of associations significantly impact code maintainability, flexibility, and testability.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the concept of association and its different types (unidirectional, bidirectional)
- [ ] Identify association multiplicities (one-to-one, one-to-many, many-to-many)
- [ ] Implement associations between classes using object references and navigation methods
- [ ] Distinguish association from composition, aggregation, and dependency relationships

## Prerequisites

- [02-classes](../02-classes/README.md) - Class structure and object creation
- [09-inheritance](../09-inheritance/README.md) - Understanding class relationships
- [03-objects](../03-objects/README.md) - Object references and memory allocation
- [12-interfaces](../12-interfaces/README.md) - Interface contracts and polymorphism

## Why This Concept Exists

### The Problem

In real-world systems, objects rarely exist in isolation. They need to interact with other objects to fulfill their responsibilities. Without a clear understanding of associations, you might:

1. **Create tight coupling**: Objects might depend too heavily on each other's internal implementations
2. **Miss relationships**: Important connections between objects might be overlooked
3. **Design confusing APIs**: Navigation between objects might be unclear or inconsistent
4. **Break encapsulation**: Internal details might be exposed unnecessarily

### The Solution

Association provides a formal way to:

- Define how objects are connected and can navigate to each other
- Specify the multiplicity of relationships (how many objects are involved)
- Control the direction of dependency (unidirectional vs bidirectional)
- Document the roles objects play in relationships

### Real-World Analogy

Think of associations as **social relationships**. A person can be associated with:
- A spouse (one-to-one, bidirectional)
- Children (one-to-many, bidirectional)
- Employers (many-to-one, unidirectional from employee perspective)
- Friends (many-to-many, bidirectional)

Each relationship has different characteristics: some are mandatory, some are optional, some have specific roles, and some have multiplicity constraints.

## Internal Working

### JVM Perspective

Associations are implemented through object references in Java:

1. **Object References**: Associations are stored as instance variables that reference other objects
2. **Navigation**: Methods are provided to traverse associations between objects
3. **Garbage Collection**: Referenced objects are kept alive; unreferenced objects are collected
4. **Memory Management**: Each association consumes memory for the reference (typically 4-8 bytes)

### Memory Representation

```
Association Examples in Memory:

Unidirectional Association:
┌─────────────────────┐      ┌─────────────────────┐
│      Student        │      │      Course         │
├─────────────────────┤      ├─────────────────────┤
│ name: String        │      │ name: String        │
│ course → ──────────────────→│ instructor: String  │
└─────────────────────┘      └─────────────────────┘

Bidirectional Association:
┌─────────────────────┐      ┌─────────────────────┐
│      Teacher        │←─────────│      Student       │
├─────────────────────┤      ├─────────────────────┤
│ name: String        │      │ name: String        │
│ students → ──────────────────→│ teacher: Teacher   │
└─────────────────────┘      └─────────────────────┘
```

### Association Types

```
Association Relationships:

1. Unidirectional Association:
   Class A ────────→ Class B
   (A knows about B, B doesn't know about A)

2. Bidirectional Association:
   Class A ←───────→ Class B
   (Both classes know about each other)

3. Multiplicity:
   1:1  - One-to-One
   1:N  - One-to-Many
   N:1  - Many-to-One
   N:M  - Many-to-Many
```

## Syntax

### Unidirectional Association

```java
class Order {
    private Customer customer; // Order knows about Customer
    private List<OrderItem> items;

    public Order(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public Customer getCustomer() {
        return customer;
    }
}

class Customer {
    private String name;
    // Customer doesn't know about Order
}
```

### Bidirectional Association

```java
class Department {
    private String name;
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee emp) {
        employees.add(emp);
        emp.setDepartment(this); // Maintain both sides
    }
}

class Employee {
    private String name;
    private Department department;

    public void setDepartment(Department dept) {
        this.department = dept;
    }
}
```

### Association with Multiplicity

```java
class University {
    private List<Department> departments; // One-to-Many
}

class Department {
    private University university; // Many-to-One
    private List<Course> courses; // One-to-Many
}

class Course {
    private Department department; // Many-to-One
    private List<Student> students; // Many-to-Many
}

class Student {
    private List<Course> enrolledCourses; // Many-to-Many
}
```

## Easy Examples

### Example 1: Library System

**Problem Statement**: Design a library system that demonstrates unidirectional and bidirectional associations between Books, Authors, Members, and Loans.

**Implementation**:

```java
package academy.javaengineering.oop.association;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Author {
    private String name;
    private String biography;
    private List<Book> books; // One author can write many books

    public Author(String name, String biography) {
        this.name = name;
        this.biography = biography;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public String getName() { return name; }
    public String getBiography() { return biography; }
    public List<Book> getBooks() { return new ArrayList<>(books); }
}

class Book {
    private String title;
    private String isbn;
    private Author author; // Many books can have one author
    private List<Loan> loans; // One book can have many loans

    public Book(String title, String isbn, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.loans = new ArrayList<>();
        author.addBook(this); // Maintain bidirectional association
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public boolean isAvailable() {
        return loans.isEmpty() || loans.stream().allMatch(Loan::isReturned);
    }

    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public Author getAuthor() { return author; }
}

class Member {
    private String memberId;
    private String name;
    private String email;
    private List<Loan> loans; // One member can have many loans

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.loans = new ArrayList<>();
    }

    public Loan borrowBook(Book book) {
        if (!book.isAvailable()) {
            System.out.println("Book is not available: " + book.getTitle());
            return null;
        }

        Loan loan = new Loan(this, book);
        loans.add(loan);
        book.addLoan(loan);
        System.out.println(name + " borrowed: " + book.getTitle());
        return loan;
    }

    public void returnBook(Loan loan) {
        if (loan.isReturned()) {
            System.out.println("Book already returned");
            return;
        }

        loan.returnBook();
        System.out.println(name + " returned: " + loan.getBook().getTitle());
    }

    public List<Loan> getLoans() { return new ArrayList<>(loans); }
    public String getName() { return name; }
    public String getMemberId() { return memberId; }
}

class Loan {
    private Member member; // Loan is associated with one member
    private Book book; // Loan is associated with one book
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(Member member, Book book) {
        this.member = member;
        this.book = book;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(14);
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue() {
        return !isReturned() && LocalDate.now().isAfter(dueDate);
    }

    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
}

class Library {
    private String name;
    private List<Book> books;
    private List<Member> members;

    public Library(String name) {
        this.name = name;
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void registerMember(Member member) {
        members.add(member);
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public void printStatus() {
        System.out.println("=== " + name + " Status ===");
        System.out.println("Books: " + books.size());
        System.out.println("Members: " + members.size());

        long availableBooks = books.stream().filter(Book::isAvailable).count();
        System.out.println("Available books: " + availableBooks);
        System.out.println("Loaned books: " + (books.size() - availableBooks));
    }
}

public class LibraryDemo {
    public static void main(String[] args) {
        // Create authors
        Author author1 = new Author("George Orwell", "English novelist");
        Author author2 = new Author("Jane Austen", "English novelist");

        // Create books (association with authors)
        Book book1 = new Book("1984", "978-0451524935", author1);
        Book book2 = new Book("Animal Farm", "978-0451526342", author1);
        Book book3 = new Book("Pride and Prejudice", "978-0141439518", author2);

        // Create library and add books
        Library library = new Library("City Library");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        // Create members
        Member member1 = new Member("M001", "Alice Johnson", "alice@email.com");
        Member member2 = new Member("M002", "Bob Smith", "bob@email.com");

        library.registerMember(member1);
        library.registerMember(member2);

        // Borrow books (creates loan associations)
        System.out.println("=== Borrowing Books ===");
        Loan loan1 = member1.borrowBook(book1);
        Loan loan2 = member1.borrowBook(book3);
        Loan loan3 = member2.borrowBook(book2);

        System.out.println("\n=== Trying to borrow unavailable book ===");
        member2.borrowBook(book1); // Should fail

        // Return books
        System.out.println("\n=== Returning Books ===");
        member1.returnBook(loan1);

        // Check availability
        System.out.println("\n=== Book Availability ===");
        System.out.println(book1.getTitle() + ": " + (book1.isAvailable() ? "Available" : "On Loan"));
        System.out.println(book2.getTitle() + ": " + (book2.isAvailable() ? "Available" : "On Loan"));
        System.out.println(book3.getTitle() + ": " + (book3.isAvailable() ? "Available" : "On Loan"));

        // Print library status
        System.out.println();
        library.printStatus();
    }
}
```

**Expected Output**:
```
=== Borrowing Books ===
Alice Johnson borrowed: 1984
Alice Johnson borrowed: Pride and Prejudice
Bob Smith borrowed: Animal Farm

=== Trying to borrow unavailable book ===
Book is not available: 1984

=== Returning Books ===
Alice Johnson returned: 1984

=== Book Availability ===
1984: Available
Animal Farm: On Loan
Pride and Prejudice: On Loan

=== City Library Status ===
Books: 3
Members: 2
Available books: 1
Loaned books: 2
```

**Best Practices**:
- Keep associations simple and clear
- Provide navigation methods for traversing associations
- Maintain both sides of bidirectional associations consistently
- Use interfaces for association types to enable flexibility

### Example 2: School Management System

**Problem Statement**: Design a school system that demonstrates different association multiplicities between Schools, Departments, Teachers, Students, and Courses.

**Implementation**:

```java
package academy.javaengineering.oop.association;

import java.util.ArrayList;
import java.util.List;

class School {
    private String name;
    private List<Department> departments; // One-to-many
    private List<Student> students; // One-to-many

    public School(String name) {
        this.name = name;
        this.departments = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void addDepartment(Department dept) {
        departments.add(dept);
        dept.setSchool(this);
    }

    public void enrollStudent(Student student) {
        students.add(student);
        student.setSchool(this);
    }

    public void printInfo() {
        System.out.println("School: " + name);
        System.out.println("Departments: " + departments.size());
        System.out.println("Students: " + students.size());
    }

    public String getName() { return name; }
    public List<Department> getDepartments() { return new ArrayList<>(departments); }
}

class Department {
    private String name;
    private School school; // Many-to-one
    private List<Teacher> teachers; // One-to-many
    private List<Course> courses; // One-to-many

    public Department(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.setDepartment(this);
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.setDepartment(this);
    }

    public String getName() { return name; }
    public School getSchool() { return school; }
    public List<Teacher> getTeachers() { return new ArrayList<>(teachers); }
}

class Teacher {
    private String id;
    private String name;
    private Department department; // Many-to-one
    private List<Course> courses; // One-to-many

    public Teacher(String id, String name) {
        this.id = id;
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public void setDepartment(Department dept) {
        this.department = dept;
    }

    public void assignCourse(Course course) {
        courses.add(course);
        course.setTeacher(this);
    }

    public String getName() { return name; }
    public Department getDepartment() { return department; }
    public List<Course> getCourses() { return new ArrayList<>(courses); }
}

class Course {
    private String code;
    private String name;
    private Department department; // Many-to-one
    private Teacher teacher; // Many-to-one
    private List<Student> enrolledStudents; // Many-to-many

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
        this.enrolledStudents = new ArrayList<>();
    }

    public void setDepartment(Department dept) {
        this.department = dept;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            student.enrollInCourse(this);
        }
    }

    public void printRoster() {
        System.out.println("Course: " + code + " - " + name);
        System.out.println("Teacher: " + (teacher != null ? teacher.getName() : "TBD"));
        System.out.println("Students enrolled: " + enrolledStudents.size());
    }

    public String getCode() { return code; }
    public String getName() { return name; }
}

class Student {
    private String id;
    private String name;
    private School school; // Many-to-one
    private List<Course> courses; // Many-to-many

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void enrollInCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.enrollStudent(this);
        }
    }

    public String getName() { return name; }
    public School getSchool() { return school; }
    public List<Course> getCourses() { return new ArrayList<>(courses); }
}

public class SchoolDemo {
    public static void main(String[] args) {
        // Create school
        School school = new School("Tech University");

        // Create departments
        Department csDept = new Department("Computer Science");
        Department mathDept = new Department("Mathematics");
        school.addDepartment(csDept);
        school.addDepartment(mathDept);

        // Create teachers
        Teacher teacher1 = new Teacher("T001", "Dr. Smith");
        Teacher teacher2 = new Teacher("T002", "Prof. Johnson");
        csDept.addTeacher(teacher1);
        mathDept.addTeacher(teacher2);

        // Create courses
        Course course1 = new Course("CS101", "Introduction to Programming");
        Course course2 = new Course("CS201", "Data Structures");
        Course course3 = new Course("MATH101", "Calculus I");
        csDept.addCourse(course1);
        csDept.addCourse(course2);
        mathDept.addCourse(course3);

        // Assign teachers to courses
        teacher1.assignCourse(course1);
        teacher1.assignCourse(course2);
        teacher2.assignCourse(course3);

        // Create and enroll students
        Student student1 = new Student("S001", "Alice");
        Student student2 = new Student("S002", "Bob");
        Student student3 = new Student("S003", "Charlie");

        school.enrollStudent(student1);
        school.enrollStudent(student2);
        school.enrollStudent(student3);

        // Enroll students in courses (many-to-many)
        course1.enrollStudent(student1);
        course1.enrollStudent(student2);
        course2.enrollStudent(student1);
        course3.enrollStudent(student2);
        course3.enrollStudent(student3);

        // Print information
        school.printInfo();
        System.out.println();

        course1.printRoster();
        System.out.println();
        course2.printRoster();
        System.out.println();
        course3.printRoster();
    }
}
```

**Expected Output**:
```
School: Tech University
Departments: 2
Students: 3

Course: CS101 - Introduction to Programming
Teacher: Dr. Smith
Students enrolled: 2

Course: CS201 - Data Structures
Teacher: Dr. Smith
Students enrolled: 1

Course: MATH101 - Calculus I
Teacher: Prof. Johnson
Students enrolled: 2
```

**Best Practices**:
- Use appropriate multiplicity for associations
- Provide clear navigation methods
- Maintain consistency in bidirectional associations
- Document association constraints and rules

## Medium Examples

### Example 1: Hospital Management System

**Problem Statement**: Design a hospital management system with complex associations between Patients, Doctors, Appointments, Departments, and Medical Records.

**Requirements**:

- Support multiple association types and multiplicities
- Handle bidirectional associations properly
- Implement association navigation and querying
- Support role-based associations

**Implementation**:

```java
package academy.javaengineering.oop.association;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class Hospital {
    private String name;
    private List<Department> departments;
    private List<Doctor> doctors;

    public Hospital(String name) {
        this.name = name;
        this.departments = new ArrayList<>();
        this.doctors = new ArrayList<>();
    }

    public void addDepartment(Department dept) {
        departments.add(dept);
        dept.setHospital(this);
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        doctor.setHospital(this);
    }

    public void printInfo() {
        System.out.println("Hospital: " + name);
        System.out.println("Departments: " + departments.size());
        System.out.println("Doctors: " + doctors.size());
    }

    public String getName() { return name; }
}

class Department {
    private String name;
    private Hospital hospital;
    private List<Doctor> doctors;

    public Department(String name) {
        this.name = name;
        this.doctors = new ArrayList<>();
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        doctor.setDepartment(this);
    }

    public String getName() { return name; }
    public List<Doctor> getDoctors() { return new ArrayList<>(doctors); }
}

class Doctor {
    private String id;
    private String name;
    private String specialization;
    private Hospital hospital;
    private Department department;
    private List<Appointment> appointments;

    public Doctor(String id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.appointments = new ArrayList<>();
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Appointment scheduleAppointment(Patient patient, LocalDateTime dateTime) {
        Appointment appointment = new Appointment(this, patient, dateTime);
        appointments.add(appointment);
        patient.addAppointment(appointment);
        return appointment;
    }

    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public Department getDepartment() { return department; }
    public List<Appointment> getAppointments() { return new ArrayList<>(appointments); }
}

class Patient {
    private String id;
    private String name;
    private int age;
    private List<Appointment> appointments;
    private List<MedicalRecord> medicalRecords;

    public Patient(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.appointments = new ArrayList<>();
        this.medicalRecords = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment) {
        if (!appointments.contains(appointment)) {
            appointments.add(appointment);
        }
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public List<Appointment> getAppointments() { return new ArrayList<>(appointments); }
    public List<MedicalRecord> getMedicalRecords() { return new ArrayList<>(medicalRecords); }
}

class Appointment {
    private Doctor doctor;
    private Patient patient;
    private LocalDateTime dateTime;
    private String status;
    private String notes;

    public Appointment(Doctor doctor, Patient patient, LocalDateTime dateTime) {
        this.doctor = doctor;
        this.patient = patient;
        this.dateTime = dateTime;
        this.status = "SCHEDULED";
    }

    public void complete(String notes) {
        this.status = "COMPLETED";
        this.notes = notes;

        // Create medical record
        MedicalRecord record = new MedicalRecord(this, notes);
        patient.addMedicalRecord(record);
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public Doctor getDoctor() { return doctor; }
    public Patient getPatient() { return patient; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getStatus() { return status; }
}

class MedicalRecord {
    private Appointment appointment;
    private String diagnosis;
    private LocalDateTime createdDate;

    public MedicalRecord(Appointment appointment, String diagnosis) {
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.createdDate = LocalDateTime.now();
    }

    public String getDiagnosis() { return diagnosis; }
    public Appointment getAppointment() { return appointment; }
}

public class HospitalDemo {
    public static void main(String[] args) {
        // Create hospital
        Hospital hospital = new Hospital("City General Hospital");

        // Create departments
        Department cardiology = new Department("Cardiology");
        Department neurology = new Department("Neurology");
        hospital.addDepartment(cardiology);
        hospital.addDepartment(neurology);

        // Create doctors
        Doctor drSmith = new Doctor("D001", "Dr. Smith", "Cardiologist");
        Doctor drJohnson = new Doctor("D002", "Dr. Johnson", "Neurologist");
        hospital.addDoctor(drSmith);
        hospital.addDoctor(drJohnson);
        cardiology.addDoctor(drSmith);
        neurology.addDoctor(drJohnson);

        // Create patients
        Patient patient1 = new Patient("P001", "Alice", 45);
        Patient patient2 = new Patient("P002", "Bob", 62);

        // Schedule appointments
        System.out.println("=== Scheduling Appointments ===");
        Appointment appt1 = drSmith.scheduleAppointment(patient1, LocalDateTime.now().plusDays(1));
        Appointment appt2 = drJohnson.scheduleAppointment(patient2, LocalDateTime.now().plusDays(2));

        System.out.println("Scheduled appointment for " + patient1.getName() + " with " + drSmith.getName());
        System.out.println("Scheduled appointment for " + patient2.getName() + " with " + drJohnson.getName());

        // Complete appointments
        System.out.println("\n=== Completing Appointments ===");
        appt1.complete("Routine checkup, patient healthy");
        appt2.complete("Migraine, prescribed medication");

        // Print information
        hospital.printInfo();
        System.out.println();

        System.out.println("Patient " + patient1.getName() + " medical records:");
        for (MedicalRecord record : patient1.getMedicalRecords()) {
            System.out.println("  - " + record.getDiagnosis());
        }

        System.out.println("\nPatient " + patient2.getName() + " medical records:");
        for (MedicalRecord record : patient2.getMedicalRecords()) {
            System.out.println("  - " + record.getDiagnosis());
        }
    }
}
```

**Expected Output**:
```
=== Scheduling Appointments ===
Scheduled appointment for Alice with Dr. Smith
Scheduled appointment for Bob with Dr. Johnson

=== Completing Appointments ===
Hospital: City General Hospital
Departments: 2
Doctors: 2

Patient Alice medical records:
  - Routine checkup, patient healthy

Patient Bob medical records:
  - Migraine, prescribed medication
```

**Code Walkthrough**:

1. **Hospital Structure**: Hospital has departments and doctors (one-to-many)
2. **Department Organization**: Departments contain doctors (one-to-many)
3. **Doctor-Patient Relationship**: Doctors schedule appointments with patients
4. **Appointment Creation**: Appointments link doctors and patients
5. **Medical Records**: Created when appointments are completed

**Alternative Solution**:

```java
// Using Association Classes for complex relationships
class Enrollment {
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String grade;

    // Association class that captures additional data about the relationship
}
```

## Hard Examples

### Example 1: E-commerce Order System

**Problem Statement**: Design an e-commerce order system with complex associations between Customers, Products, Orders, OrderItems, Payments, and Shipments, supporting multiple association types and business rules.

**Requirements**:

- Support complex association multiplicities
- Handle association lifecycle management
- Implement association querying and navigation
- Support role-based associations

**Architecture**:

```
E-commerce Order System
├── Customer
│   ├── Orders (1:N)
│   └── Addresses (1:N)
├── Order
│   ├── OrderItems (1:N)
│   ├── Payment (1:1)
│   └── Shipment (1:1)
├── Product
│   ├── OrderItems (1:N)
│   └── Inventory (1:1)
└── Category
    └── Products (1:N)
```

**Implementation**:

```java
package academy.javaengineering.oop.association;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Customer {
    private String id;
    private String name;
    private String email;
    private List<Address> addresses;
    private List<Order> orders;

    public Customer(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.addresses = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public Order createOrder() {
        Order order = new Order(this);
        orders.add(order);
        return order;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Address> getAddresses() { return new ArrayList<>(addresses); }
    public List<Order> getOrders() { return new ArrayList<>(orders); }
}

class Address {
    private String id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private Customer customer;
    private AddressType type;

    public enum AddressType {
        BILLING, SHIPPING, BOTH
    }

    public Address(String street, String city, String state, String zipCode, AddressType type) {
        this.id = UUID.randomUUID().toString();
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.type = type;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFullAddress() {
        return street + ", " + city + ", " + state + " " + zipCode;
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public AddressType getType() { return type; }
}

class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private Category category;
    private Inventory inventory;
    private List<OrderItem> orderItems;

    public Product(String name, String description, double price) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderItems = new ArrayList<>();
    }

    public void setCategory(Category category) {
        this.category = category;
        category.addProduct(this);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        inventory.setProduct(this);
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }

    public boolean isInStock() {
        return inventory != null && inventory.getQuantity() > 0;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }
}

class Category {
    private String id;
    private String name;
    private List<Product> products;

    public Category(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
        }
    }

    public String getName() { return name; }
    public List<Product> getProducts() { return new ArrayList<>(products); }
}

class Inventory {
    private String id;
    private int quantity;
    private int reservedQuantity;
    private Product product;

    public Inventory(int quantity) {
        this.id = UUID.randomUUID().toString();
        this.quantity = quantity;
        this.reservedQuantity = 0;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean reserve(int amount) {
        if (quantity - reservedQuantity >= amount) {
            reservedQuantity += amount;
            return true;
        }
        return false;
    }

    public void release(int amount) {
        reservedQuantity = Math.max(0, reservedQuantity - amount);
    }

    public void confirmShipment(int amount) {
        quantity -= amount;
        reservedQuantity -= amount;
    }

    public int getQuantity() { return quantity; }
    public int getAvailableQuantity() { return quantity - reservedQuantity; }
}

class Order {
    private String id;
    private Customer customer;
    private List<OrderItem> items;
    private Address shippingAddress;
    private Address billingAddress;
    private Payment payment;
    private Shipment shipment;
    private OrderStatus status;
    private LocalDateTime orderDate;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    public Order(Customer customer) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    public OrderItem addItem(Product product, int quantity) {
        // Check if product already in order
        for (OrderItem item : items) {
            if (item.getProduct().equals(product)) {
                item.setQuantity(item.getQuantity() + quantity);
                return item;
            }
        }

        OrderItem item = new OrderItem(this, product, quantity);
        items.add(item);
        product.addOrderItem(item);
        return item;
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public boolean setShippingAddress(Address address) {
        if (address.getType() == Address.AddressType.SHIPPING ||
            address.getType() == Address.AddressType.BOTH) {
            this.shippingAddress = address;
            return true;
        }
        return false;
    }

    public boolean setBillingAddress(Address address) {
        if (address.getType() == Address.AddressType.BILLING ||
            address.getType() == Address.AddressType.BOTH) {
            this.billingAddress = address;
            return true;
        }
        return false;
    }

    public void processPayment(Payment payment) {
        if (payment.isSuccessful()) {
            this.payment = payment;
            this.status = OrderStatus.CONFIRMED;
            // Reserve inventory
            for (OrderItem item : items) {
                item.getProduct().getInventory().reserve(item.getQuantity());
            }
        }
    }

    public void ship(Shipment shipment) {
        this.shipment = shipment;
        this.status = OrderStatus.SHIPPED;
        // Confirm inventory reduction
        for (OrderItem item : items) {
            item.getProduct().getInventory().confirmShipment(item.getQuantity());
        }
    }

    public void deliver() {
        this.status = OrderStatus.DELIVERED;
    }

    public double getTotal() {
        return items.stream()
            .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
            .sum();
    }

    public void printOrderSummary() {
        System.out.println("=== Order " + id + " ===");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Status: " + status);
        System.out.println("Items:");
        for (OrderItem item : items) {
            System.out.printf("  %s x%d = $%.2f%n",
                item.getProduct().getName(),
                item.getQuantity(),
                item.getProduct().getPrice() * item.getQuantity());
        }
        System.out.printf("Total: $%.2f%n", getTotal());
        if (shippingAddress != null) {
            System.out.println("Shipping to: " + shippingAddress.getFullAddress());
        }
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public OrderStatus getStatus() { return status; }
}

class OrderItem {
    private Order order;
    private Product product;
    private int quantity;

    public OrderItem(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() { return order; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
}

class Payment {
    private String id;
    private double amount;
    private PaymentMethod method;
    private boolean successful;
    private LocalDateTime paymentDate;

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER
    }

    public Payment(double amount, PaymentMethod method) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.method = method;
        this.paymentDate = LocalDateTime.now();
        this.successful = true; // Simplified
    }

    public boolean isSuccessful() { return successful; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
}

class Shipment {
    private String id;
    private String trackingNumber;
    private String carrier;
    private ShipmentStatus status;
    private LocalDateTime shipDate;
    private LocalDateTime estimatedDelivery;

    public enum ShipmentStatus {
        PREPARING, SHIPPED, IN_TRANSIT, DELIVERED
    }

    public Shipment(String trackingNumber, String carrier) {
        this.id = UUID.randomUUID().toString();
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.status = ShipmentStatus.PREPARING;
        this.shipDate = LocalDateTime.now();
        this.estimatedDelivery = shipDate.plusDays(5);
    }

    public void updateStatus(ShipmentStatus status) {
        this.status = status;
    }

    public String getTrackingNumber() { return trackingNumber; }
    public String getCarrier() { return carrier; }
    public ShipmentStatus getStatus() { return status; }
}

public class EcommerceDemo {
    public static void main(String[] args) {
        // Create customer
        Customer customer = new Customer("Alice Johnson", "alice@email.com");

        // Add addresses
        Address shippingAddr = new Address("123 Main St", "New York", "NY", "10001",
            Address.AddressType.SHIPPING);
        Address billingAddr = new Address("456 Oak Ave", "New York", "NY", "10002",
            Address.AddressType.BILLING);
        customer.addAddress(shippingAddr);
        customer.addAddress(billingAddr);

        // Create products
        Category electronics = new Category("Electronics");
        Product laptop = new Product("Laptop", "High-performance laptop", 999.99);
        Product mouse = new Product("Mouse", "Wireless mouse", 29.99);
        laptop.setCategory(electronics);
        mouse.setCategory(electronics);

        // Set inventory
        laptop.setInventory(new Inventory(10));
        mouse.setInventory(new Inventory(50));

        // Create order
        Order order = customer.createOrder();
        order.setShippingAddress(shippingAddr);
        order.setBillingAddress(billingAddr);

        // Add items to order
        order.addItem(laptop, 1);
        order.addItem(mouse, 2);

        // Print order summary
        order.printOrderSummary();

        // Process payment
        System.out.println("\n=== Processing Payment ===");
        Payment payment = new Payment(order.getTotal(), Payment.PaymentMethod.CREDIT_CARD);
        order.processPayment(payment);
        System.out.println("Payment processed: $" + payment.getAmount());

        // Ship order
        System.out.println("\n=== Shipping Order ===");
        Shipment shipment = new Shipment("TRACK123456", "FedEx");
        order.ship(shipment);
        System.out.println("Order shipped with tracking: " + shipment.getTrackingNumber());

        // Update status
        System.out.println("\n=== Order Status ===");
        order.printOrderSummary();
    }
}
```

**Execution Flow**:

1. **Customer Setup**: Customer is created with addresses
2. **Product Setup**: Products are created with categories and inventory
3. **Order Creation**: Order is created and items are added
4. **Address Assignment**: Shipping and billing addresses are set
5. **Payment Processing**: Payment is processed and inventory is reserved
6. **Shipment**: Order is shipped and inventory is confirmed

**Unit Tests**:

```java
public class EcommerceTest {
    public static void main(String[] args) {
        System.out.println("=== Running E-commerce Tests ===\n");

        testCustomerCreation();
        testOrderCreation();
        testInventoryManagement();
        testPaymentProcessing();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testCustomerCreation() {
        System.out.println("Test 1: Customer Creation");
        Customer customer = new Customer("Test User", "test@email.com");
        assert customer.getName().equals("Test User") : "Name incorrect";
        assert customer.getEmail().equals("test@email.com") : "Email incorrect";

        System.out.println("  PASS: Customer creation test passed\n");
    }

    private static void testOrderCreation() {
        System.out.println("Test 2: Order Creation");
        Customer customer = new Customer("Test", "test@test.com");
        Order order = customer.createOrder();

        assert order.getCustomer() == customer : "Order customer incorrect";
        assert order.getStatus() == Order.OrderStatus.PENDING : "Order status incorrect";

        System.out.println("  PASS: Order creation test passed\n");
    }

    private static void testInventoryManagement() {
        System.out.println("Test 3: Inventory Management");
        Inventory inventory = new Inventory(10);

        assert inventory.reserve(5) : "Should reserve inventory";
        assert inventory.getAvailableQuantity() == 5 : "Available quantity incorrect";

        assert !inventory.reserve(10) : "Should not reserve more than available";

        inventory.confirmShipment(5);
        assert inventory.getQuantity() == 5 : "Quantity should be reduced";

        System.out.println("  PASS: Inventory management test passed\n");
    }

    private static void testPaymentProcessing() {
        System.out.println("Test 4: Payment Processing");
        Payment payment = new Payment(100.0, Payment.PaymentMethod.CREDIT_CARD);

        assert payment.isSuccessful() : "Payment should be successful";
        assert payment.getAmount() == 100.0 : "Amount incorrect";

        System.out.println("  PASS: Payment processing test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(n) for order processing where n is number of items
- **Space Complexity**: O(n) for storing order items and related objects

**Best Practices**:

- Use association classes for complex relationships with additional data
- Implement proper lifecycle management for associations
- Use bidirectional associations only when necessary
- Document association constraints and business rules
- Consider performance implications of deep association chains

## Exercises

### Easy

1. **Blog System**: Create a blog system with associations between Users, Posts, Comments, and Tags.

2. **Contact List**: Design a contact list with associations between Contacts, Groups, and Addresses.

3. **Movie Database**: Create a movie database with associations between Movies, Actors, Directors, and Genres.

### Medium

1. **Inventory System**: Design an inventory system with associations between Products, Categories, Suppliers, and Orders.

2. **Reservation System**: Create a reservation system with associations between Customers, Rooms, Reservations, and Payments.

3. **Social Network**: Design a social network with associations between Users, Posts, Comments, Likes, and Friendships.

### Hard

1. **Healthcare System**: Create a comprehensive healthcare system with complex associations between Patients, Doctors, Appointments, Medical Records, and Prescriptions.

2. **Banking System**: Design a banking system with associations between Customers, Accounts, Transactions, and Loans.

3. **Flight Booking**: Create a flight booking system with associations between Passengers, Flights, Reservations, and Payments.

## Interview Questions

### Easy

1. **What is association in object-oriented programming?**
   Association is a relationship between two or more classes that defines how objects interact with each other. It can be unidirectional or bidirectional, and has different multiplicities (one-to-one, one-to-many, many-to-many).

2. **What is the difference between association and aggregation?**
   Association is a general relationship between objects. Aggregation is a specific type of association where one object "has" another, but the contained object can exist independently. Aggregation represents a "has-a" relationship with loose coupling.

3. **What is a bidirectional association?**
   A bidirectional association is a relationship where both classes know about each other. Each class has a reference to the other, allowing navigation in both directions. Both sides must be maintained consistently.

### Medium

1. **How do you implement bidirectional associations in Java?**
   Implement bidirectional associations by providing setter methods on both classes and maintaining both references when the association is established. Use helper methods to ensure consistency.

2. **What are the implications of deep association chains?**
   Deep association chains can lead to performance issues (excessive object loading), tight coupling, and complex navigation code. Consider using lazy loading, fetch strategies, or flattening the association structure.

3. **How do association multiplicities affect implementation?**
   Multiplicities determine the data structures used (single references vs collections), the validation rules (required vs optional), and the lifecycle management (cascade operations).

### Hard

1. **How do you handle circular associations?**
   Circular associations can cause infinite recursion during toString() or equals() methods. Break the cycle by using @JsonIgnore in JPA, implementing lazy loading, or redesigning the association to be unidirectional.

2. **How do associations impact database design?**
   Associations map to database relationships (foreign keys, join tables). One-to-one uses a foreign key, one-to-many uses a foreign key on the many side, many-to-many uses a join table. Association mapping affects query performance and data integrity.

## Common Pitfalls

### 1. Forgetting to Maintain Both Sides of Bidirectional Association

**Wrong**:
```java
class Order {
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        // Forgot to add this order to customer's orders
    }
}

class Customer {
    private List<Order> orders = new ArrayList<>();
}
```

**Right**:
```java
class Order {
    private Customer customer;

    public void setCustomer(Customer customer) {
        if (this.customer != null) {
            this.customer.getOrders().remove(this);
        }
        this.customer = customer;
        if (customer != null) {
            customer.getOrders().add(this);
        }
    }
}

class Customer {
    private List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() { return orders; }
}
```

### 2. Creating Circular References in toString()

**Wrong**:
```java
class Parent {
    private List<Child> children;

    @Override
    public String toString() {
        return "Parent{children=" + children + "}"; // Calls Child.toString()
    }
}

class Child {
    private Parent parent;

    @Override
    public String toString() {
        return "Child{parent=" + parent + "}"; // Calls Parent.toString() - infinite loop!
    }
}
```

**Right**:
```java
class Parent {
    private List<Child> children;

    @Override
    public String toString() {
        return "Parent{children=" + children.size() + " children}"; // Don't include full object
    }
}

class Child {
    private Parent parent;

    @Override
    public String toString() {
        return "Child{parent=" + (parent != null ? parent.getName() : "null") + "}";
    }
}
```

### 3. Not Handling Association Lifecycle Properly

**Wrong**:
```java
class Order {
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        // Forgot to clean up item's reference to order
    }
}
```

**Right**:
```java
class Order {
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this); // Maintain both sides
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null); // Clean up both sides
    }
}
```

## Best Practices

1. **Keep associations simple**: Prefer unidirectional associations unless bidirectional navigation is required. Bidirectional associations add complexity and maintenance overhead.

2. **Maintain consistency**: When modifying bidirectional associations, always update both sides to maintain consistency. Use helper methods to ensure atomicity.

3. **Document multiplicities**: Clearly document the multiplicity and constraints of associations. This helps other developers understand the intended usage.

4. **Consider performance**: Association traversal can be expensive. Use lazy loading, fetch strategies, or denormalization when performance is critical.

5. **Use interfaces for flexibility**: Depend on interfaces rather than concrete classes for association types. This allows implementations to be swapped without affecting the association.

## Real World Usage

### How Spring Uses This

Spring Framework uses associations for:

- **Bean Dependencies**: Spring beans have associations through dependency injection
- **Application Context**: Hierarchical associations between parent and child contexts
- **Event Publishing**: Associations between event publishers and subscribers

### How Hibernate Uses This

Hibernate ORM manages associations through:

- **Entity Relationships**: @OneToMany, @ManyToOne, @ManyToMany annotations
- **Fetch Strategies**: Eager vs lazy loading of associations
- **Cascade Operations**: Propagating operations through associations

### How JDK Uses This

The Java Development Kit uses associations in:

- **Collections**: Collections contain references to elements
- **I/O Streams**: Streams are composed of other streams (buffering, filtering)
- **Thread Groups**: Threads are associated with thread groups

### Enterprise Usage

In enterprise applications, associations are used for:

- **Domain Models**: Business entities have associations representing business relationships
- **Service Dependencies**: Services depend on other services through associations
- **Configuration**: Configuration objects have associations to property sources

## References

1. **UML Distilled** by Martin Fowler - Association relationships
2. **Effective Java** by Joshua Bloch - Item 25: Prefer lists to arrays
3. **Hibernate in Action** - Entity association mappings
4. **Domain-Driven Design** by Eric Evans - Aggregate roots and associations
5. **Clean Architecture** by Robert C. Martin - Component relationships

## Summary

- Association defines relationships between objects in object-oriented systems
- Associations can be unidirectional or bidirectional
- Multiplicities define how many objects are involved (1:1, 1:N, N:M)
- Maintain consistency in bidirectional associations
- Consider performance implications of deep association chains
- Use interfaces for association types to enable flexibility

**Next Steps**: [21-aggregation](../21-aggregation/README.md)
