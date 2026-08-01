# Aggregation

## Introduction

Aggregation is a specific type of association in object-oriented programming that represents a "has-a" relationship between objects where the contained object can exist independently of the container, creating a weak ownership relationship that allows shared instances and independent lifecycle management. Unlike composition where the contained object is tightly coupled to the container and cannot exist without it, aggregation maintains loose coupling between objects, enabling greater flexibility and reusability. Aggregation is often referred to as "weak association" or "shared aggregation" because multiple container objects can reference the same contained object, and the contained object's lifecycle is not managed by the container. This relationship is commonly found in real-world scenarios where objects logically belong together but have independent lifecycles, such as a library having books (books exist independently), a department having employees (employees can transfer), or a car having tires (tires can be replaced).

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the concept of aggregation and how it differs from composition and association
- [ ] Implement aggregation relationships where contained objects have independent lifecycles
- [ ] Recognize when to use aggregation versus composition based on ownership and lifecycle requirements
- [ ] Apply aggregation to create flexible, loosely coupled designs with shared instances

## Prerequisites

- [19-composition](../19-composition/README.md) - Understanding composition and "has-a" relationships
- [20-association](../20-association/README.md) - General association concepts and multiplicities
- [02-classes](../02-classes/README.md) - Class structure and object creation
- [03-objects](../03-objects/README.md) - Object references and memory management

## Why This Concept Exists

### The Problem

Without aggregation, you face several design challenges:

1. **Tight coupling**: Objects are too dependent on each other's lifecycles
2. **Limited reusability**: Objects cannot be shared or reused across different contexts
3. **Resource waste**: Duplicate objects are created when they could be shared
4. **Complex cleanup**: Lifecycle management becomes difficult when objects are tightly coupled

```java
// Without aggregation - tight coupling
class Team {
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player); // Player's lifecycle tied to Team
    }

    // When Team is destroyed, Players are also destroyed
    // Even though Players could exist independently
}
```

### The Solution

Aggregation solves these problems by:

- Allowing contained objects to exist independently of the container
- Enabling shared instances across multiple containers
- Providing loose coupling between related objects
- Supporting flexible lifecycle management

### Real-World Analogy

Think of aggregation as a **library and books**. A library "has" books, but:
- Books existed before the library and can exist after it closes
- The same book can belong to multiple libraries (inter-library loans)
- When the library closes, the books still exist and can be moved elsewhere
- Books have their own lifecycle (published, borrowed, returned, damaged)

This is different from composition where a car "has" an engine - if the car is destroyed, the engine is typically destroyed with it.

## Internal Working

### JVM Perspective

Aggregation is implemented through object references with loose lifecycle coupling:

1. **Object References**: Container holds references to contained objects
2. **Independent Lifecycle**: Contained objects are not created or destroyed by the container
3. **Shared References**: Multiple containers can reference the same object
4. **Garbage Collection**: Objects are collected only when no references exist

### Memory Representation

```
Aggregation in Memory:

Department Object:
┌─────────────────────────────┐
│ Fields:                     │
│ ├── name: "Engineering"     │
│ └── employees → ──────────────────────┐
└─────────────────────────────┘         │
                                        ▼
Employee Objects:              ┌─────────────────────┐
                              │ Employee Object      │
                              │ ├── id: "E001"       │
                              │ ├── name: "Alice"    │
                              │ └── department → ──────────┐
                              └─────────────────────┘      │
                                                           │
                              ┌─────────────────────┐      │
                              │ Employee Object      │      │
                              │ ├── id: "E002"       │      │
                              │ ├── name: "Bob"      │      │
                              │ └── department → ──────────┘
                              └─────────────────────┘

Note: Employee objects can exist independently
      Multiple departments could reference the same employee
```

### Aggregation vs Composition

```
Aggregation (Weak "has-a"):
┌─────────────────┐      ┌─────────────────┐
│    Library      │      │      Book       │
├─────────────────┤      ├─────────────────┤
│ books → ─────────────→ │ title: "..."    │
│                   │      │ author: "..."   │
└─────────────────┘      └─────────────────┘
                          ↑
                          │ Can exist independently
                          │ Can be shared with other libraries

Composition (Strong "has-a"):
┌─────────────────┐      ┌─────────────────┐
│      Car        │      │     Engine      │
├─────────────────┤      ├─────────────────┤
│ engine → ─────────────→│ type: "V8"     │
│                   │      │ horsepower: 400 │
└─────────────────┘      └─────────────────┘
                          ↑
                          │ Cannot exist without Car
                          │ Lifecycle tied to Car
```

## Syntax

### Basic Aggregation

```java
class Library {
    private String name;
    private List<Book> books; // Aggregation: Library has Books

    public Library(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book); // Book's lifecycle is independent
    }

    public void removeBook(Book book) {
        books.remove(book); // Book continues to exist
    }
}

class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
    // Book can exist without Library
}
```

### Aggregation with Shared Instances

```java
class Department {
    private String name;
    private List<Employee> employees;

    public Department(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.addDepartment(this); // Bidirectional aggregation
    }
}

class Employee {
    private String name;
    private List<Department> departments; // Can belong to multiple departments

    public Employee(String name) {
        this.name = name;
        this.departments = new ArrayList<>();
    }

    public void addDepartment(Department dept) {
        if (!departments.contains(dept)) {
            departments.add(dept);
        }
    }
}
```

### Aggregation with Lifecycle Management

```java
class Team {
    private String name;
    private List<Player> players;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
        // Don't manage player's lifecycle
    }

    public void removePlayer(Player player) {
        players.remove(player);
        // Player continues to exist independently
    }
}

class Player {
    private String name;
    private Team team; // Optional reference back to team

    public Player(String name) {
        this.name = name;
    }

    public void setTeam(Team team) {
        this.team = team; // Weak reference
    }

    public void leaveTeam() {
        this.team = null; // Can leave team without being destroyed
    }
}
```

## Easy Examples

### Example 1: Library Management System

**Problem Statement**: Design a library management system that demonstrates aggregation between Libraries, Books, Authors, and Members, where each entity has an independent lifecycle.

**Implementation**:

```java
package academy.javaengineering.oop.aggregation;

import java.util.ArrayList;
import java.util.List;

class Author {
    private String name;
    private String biography;
    private List<Book> books; // Author can exist without Library

    public Author(String name, String biography) {
        this.name = name;
        this.biography = biography;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        }
    }

    public String getName() { return name; }
    public String getBiography() { return biography; }
    public List<Book> getBooks() { return new ArrayList<>(books); }
}

class Book {
    private String title;
    private String isbn;
    private Author author; // Book has an Author (aggregation)
    private int publicationYear;

    public Book(String title, String isbn, Author author, int publicationYear) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publicationYear = publicationYear;
        author.addBook(this); // Maintain association
    }

    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public Author getAuthor() { return author; }
    public int getPublicationYear() { return publicationYear; }
}

class Member {
    private String memberId;
    private String name;
    private String email;
    private List<Book> borrowedBooks; // Member can exist without Library

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return new ArrayList<>(borrowedBooks); }
    public String getMemberId() { return memberId; }
}

class Library {
    private String name;
    private String address;
    private List<Book> books; // Aggregation: Library has Books
    private List<Member> members; // Aggregation: Library has Members

    public Library(String name, String address) {
        this.name = name;
        this.address = address;
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Added to library: " + book.getTitle());
    }

    public void removeBook(Book book) {
        books.remove(book);
        System.out.println("Removed from library: " + book.getTitle());
        // Book continues to exist independently
    }

    public void registerMember(Member member) {
        members.add(member);
        System.out.println("Registered member: " + member.getName());
    }

    public void lendBook(Book book, Member member) {
        if (books.contains(book)) {
            member.borrowBook(book);
            System.out.println(member.getName() + " borrowed: " + book.getTitle());
        }
    }

    public void receiveReturn(Book book, Member member) {
        member.returnBook(book);
        System.out.println(member.getName() + " returned: " + book.getTitle());
    }

    public void printCatalog() {
        System.out.println("\n=== " + name + " Catalog ===");
        for (Book book : books) {
            System.out.println("- " + book.getTitle() + " by " + book.getAuthor().getName());
        }
    }

    public void printMembers() {
        System.out.println("\n=== Members ===");
        for (Member member : members) {
            System.out.println("- " + member.getName() + " (" + member.getMemberId() + ")");
        }
    }

    public String getName() { return name; }
    public int getBookCount() { return books.size(); }
    public int getMemberCount() { return members.size(); }
}

public class LibraryAggregationDemo {
    public static void main(String[] args) {
        // Create authors (independent of library)
        Author orwell = new Author("George Orwell", "English novelist");
        Author austen = new Author("Jane Austen", "English novelist");

        // Create books (independent of library)
        Book book1 = new Book("1984", "978-0451524935", orwell, 1949);
        Book book2 = new Book("Animal Farm", "978-0451526342", orwell, 1945);
        Book book3 = new Book("Pride and Prejudice", "978-0141439518", austen, 1813);

        // Create library and add books (aggregation)
        Library library = new Library("City Public Library", "123 Main St");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        // Create members (independent of library)
        Member alice = new Member("M001", "Alice Johnson", "alice@email.com");
        Member bob = new Member("M002", "Bob Smith", "bob@email.com");

        library.registerMember(alice);
        library.registerMember(bob);

        // Lend books
        System.out.println("\n=== Lending Books ===");
        library.lendBook(book1, alice);
        library.lendBook(book3, bob);

        // Print information
        library.printCatalog();
        library.printMembers();

        // Demonstrate independent lifecycle
        System.out.println("\n=== Independent Lifecycle Demo ===");
        System.out.println("Author " + orwell.getName() + " has " + orwell.getBooks().size() + " books");
        System.out.println("Book '" + book1.getTitle() + "' exists independently of library");

        // Remove book from library (book still exists)
        System.out.println("\n=== Removing Book from Library ===");
        library.removeBook(book2);
        System.out.println("Book '" + book2.getTitle() + "' still exists: " + book2.getTitle());
        System.out.println("Library now has " + library.getBookCount() + " books");
    }
}
```

**Expected Output**:
```
Added to library: 1984
Added to library: Animal Farm
Added to library: Pride and Prejudice
Registered member: Alice Johnson
Registered member: Bob Smith

=== Lending Books ===
Alice Johnson borrowed: 1984
Bob Smith borrowed: Pride and Prejudice

=== City Public Library Catalog ===
- 1984 by George Orwell
- Animal Farm by George Orwell
- Pride and Prejudice by Jane Austen

=== Members ===
- Alice Johnson (M001)
- Bob Smith (M002)

=== Independent Lifecycle Demo ===
Author George Orwell has 2 books
Book '1984' exists independently of library

=== Removing Book from Library ===
Removed from library: Animal Farm
Book 'Animal Farm' still exists: Animal Farm
Library now has 2 books
```

**Best Practices**:
- Use aggregation when contained objects can exist independently
- Maintain references only when necessary for navigation
- Avoid managing lifecycle of aggregated objects
- Document the relationship type clearly

### Example 2: Department-Employee System

**Problem Statement**: Design a department-employee system where employees can belong to multiple departments, demonstrating aggregation with shared instances and independent lifecycles.

**Implementation**:

```java
package academy.javaengineering.oop.aggregation;

import java.util.ArrayList;
import java.util.List;

class Department {
    private String name;
    private String code;
    private List<Employee> employees; // Aggregation: Department has Employees

    public Department(String name, String code) {
        this.name = name;
        this.code = code;
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
            employee.addDepartment(this); // Maintain bidirectional aggregation
            System.out.println(employee.getName() + " added to " + name);
        }
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.removeDepartment(this);
        System.out.println(employee.getName() + " removed from " + name);
        // Employee continues to exist and may belong to other departments
    }

    public void printEmployees() {
        System.out.println("\nDepartment: " + name + " (" + code + ")");
        for (Employee emp : employees) {
            System.out.println("- " + emp.getName() + " (" + emp.getPosition() + ")");
        }
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public int getEmployeeCount() { return employees.size(); }
}

class Employee {
    private String id;
    private String name;
    private String position;
    private List<Department> departments; // Can belong to multiple departments

    public Employee(String id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.departments = new ArrayList<>();
    }

    public void addDepartment(Department department) {
        if (!departments.contains(department)) {
            departments.add(department);
        }
    }

    public void removeDepartment(Department department) {
        departments.remove(department);
        // Employee may still belong to other departments
    }

    public void transferTo(Department newDepartment, Department oldDepartment) {
        System.out.println("\nTransferring " + name + " from " +
            oldDepartment.getName() + " to " + newDepartment.getName());
        oldDepartment.removeEmployee(this);
        newDepartment.addEmployee(this);
    }

    public void printDepartments() {
        System.out.println("\nEmployee: " + name + " (" + position + ")");
        System.out.println("Departments:");
        for (Department dept : departments) {
            System.out.println("- " + dept.getName());
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public List<Department> getDepartments() { return new ArrayList<>(departments); }
    public boolean belongsToDepartment(Department dept) {
        return departments.contains(dept);
    }
}

public class DepartmentEmployeeDemo {
    public static void main(String[] args) {
        // Create departments
        Department engineering = new Department("Engineering", "ENG");
        Department marketing = new Department("Marketing", "MKT");
        Department hr = new Department("Human Resources", "HR");

        // Create employees (independent of departments)
        Employee alice = new Employee("E001", "Alice Johnson", "Software Engineer");
        Employee bob = new Employee("E002", "Bob Smith", "Marketing Manager");
        Employee charlie = new Employee("E003", "Charlie Brown", "HR Specialist");

        // Add employees to departments (aggregation)
        System.out.println("=== Initial Assignment ===");
        engineering.addEmployee(alice);
        marketing.addEmployee(bob);
        hr.addEmployee(charlie);

        // Alice works in multiple departments
        engineering.addEmployee(alice);
        marketing.addEmployee(alice); // Alice works in both Engineering and Marketing

        // Print department rosters
        engineering.printEmployees();
        marketing.printEmployees();
        hr.printEmployees();

        // Print employee department assignments
        alice.printDepartments();
        bob.printDepartments();

        // Demonstrate transfer
        alice.transferTo(hr, engineering);

        // Final state
        System.out.println("\n=== Final State ===");
        engineering.printEmployees();
        hr.printEmployees();
        alice.printDepartments();
    }
}
```

**Expected Output**:
```
=== Initial Assignment ===
Alice Johnson added to Engineering
Bob Smith added to Marketing
Charlie Brown added to Human Resources
Alice Johnson added to Marketing

Department: Engineering (ENG)
- Alice Johnson (Software Engineer)

Department: Marketing (MKT)
- Bob Smith (Marketing Manager)
- Alice Johnson (Software Engineer)

Department: Human Resources (HR)
- Charlie Brown (HR Specialist)

Employee: Alice Johnson (Software Engineer)
Departments:
- Engineering
- Marketing

Employee: Bob Smith (Marketing Manager)
Departments:
- Marketing

Transferring Alice Johnson from Engineering to Human Resources
Alice Johnson removed from Engineering
Alice Johnson added to Human Resources

=== Final State ===

Department: Engineering (ENG)

Department: Human Resources (HR)
- Charlie Brown (HR Specialist)
- Alice Johnson (Software Engineer)

Employee: Alice Johnson (Software Engineer)
Departments:
- Marketing
- Human Resources
```

**Best Practices**:
- Use aggregation when objects can belong to multiple containers
- Maintain consistency in bidirectional relationships
- Allow objects to be transferred between containers without destruction
- Document the rules for object sharing and transfer

## Medium Examples

### Example 1: Course Management System

**Problem Statement**: Design a course management system that demonstrates aggregation with shared instances where courses, instructors, and students can exist independently and be reused across different contexts.

**Requirements**:

- Support multiple instructors per course
- Allow students to enroll in multiple courses
- Handle course sections with shared instructors
- Support independent lifecycle for all entities

**Implementation**:

```java
package academy.javaengineering.oop.aggregation;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

class Instructor {
    private String id;
    private String name;
    private String email;
    private String specialization;
    private List<Course> courses; // Can teach multiple courses

    public Instructor(String id, String name, String email, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.courses = new ArrayList<>();
    }

    public void assignCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.addInstructor(this);
        }
    }

    public void unassignCourse(Course course) {
        courses.remove(course);
        course.removeInstructor(this);
    }

    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public List<Course> getCourses() { return new ArrayList<>(courses); }
}

class Student {
    private String id;
    private String name;
    private String email;
    private List<Course> enrolledCourses; // Can enroll in multiple courses
    private List<Course> completedCourses;

    public Student(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrolledCourses = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
    }

    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            course.enrollStudent(this);
            System.out.println(name + " enrolled in " + course.getName());
        }
    }

    public void dropCourse(Course course) {
        enrolledCourses.remove(course);
        course.dropStudent(this);
        System.out.println(name + " dropped " + course.getName());
    }

    public void completeCourse(Course course, String grade) {
        enrolledCourses.remove(course);
        completedCourses.add(course);
        System.out.println(name + " completed " + course.getName() + " with grade: " + grade);
    }

    public String getName() { return name; }
    public List<Course> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }
    public int getEnrolledCount() { return enrolledCourses.size(); }
}

class CourseSection {
    private String sectionId;
    private DayOfWeek scheduleDay;
    private String timeSlot;
    private String room;
    private int capacity;
    private List<Student> enrolledStudents;

    public CourseSection(String sectionId, DayOfWeek day, String timeSlot, String room, int capacity) {
        this.sectionId = sectionId;
        this.scheduleDay = day;
        this.timeSlot = timeSlot;
        this.room = room;
        this.capacity = capacity;
        this.enrolledStudents = new ArrayList<>();
    }

    public boolean enrollStudent(Student student) {
        if (enrolledStudents.size() < capacity) {
            enrolledStudents.add(student);
            return true;
        }
        return false;
    }

    public void dropStudent(Student student) {
        enrolledStudents.remove(student);
    }

    public boolean isFull() {
        return enrolledStudents.size() >= capacity;
    }

    public String getSectionId() { return sectionId; }
    public DayOfWeek getScheduleDay() { return scheduleDay; }
    public String getTimeSlot() { return timeSlot; }
    public String getRoom() { return room; }
    public int getEnrolledCount() { return enrolledStudents.size(); }
    public int getCapacity() { return capacity; }
}

class Course {
    private String code;
    private String name;
    private String description;
    private int credits;
    private List<Instructor> instructors; // Can have multiple instructors
    private List<CourseSection> sections;
    private List<Student> enrolledStudents;
    private List<Student> waitlistedStudents;

    public Course(String code, String name, String description, int credits) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.instructors = new ArrayList<>();
        this.sections = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.waitlistedStudents = new ArrayList<>();
    }

    public void addInstructor(Instructor instructor) {
        if (!instructors.contains(instructor)) {
            instructors.add(instructor);
        }
    }

    public void removeInstructor(Instructor instructor) {
        instructors.remove(instructor);
    }

    public void addSection(CourseSection section) {
        sections.add(section);
    }

    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            // Try to enroll in a section
            for (CourseSection section : sections) {
                if (!section.isFull()) {
                    section.enrollStudent(student);
                    enrolledStudents.add(student);
                    return;
                }
            }
            // If no section available, add to waitlist
            waitlistedStudents.add(student);
            System.out.println("Added to waitlist: " + student.getName());
        }
    }

    public void dropStudent(Student student) {
        enrolledStudents.remove(student);
        for (CourseSection section : sections) {
            section.dropStudent(student);
        }
        // Check waitlist
        if (!waitlistedStudents.isEmpty()) {
            Student nextStudent = waitlistedStudents.remove(0);
            enrollStudent(nextStudent);
        }
    }

    public void printInfo() {
        System.out.println("\n=== Course: " + code + " - " + name + " ===");
        System.out.println("Credits: " + credits);
        System.out.println("Description: " + description);
        System.out.println("Instructors:");
        for (Instructor instructor : instructors) {
            System.out.println("- " + instructor.getName() + " (" + instructor.getSpecialization() + ")");
        }
        System.out.println("Enrolled Students: " + enrolledStudents.size());
        System.out.println("Waitlisted Students: " + waitlistedStudents.size());
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public List<Instructor> getInstructors() { return new ArrayList<>(instructors); }
    public int getEnrolledCount() { return enrolledStudents.size(); }
}

class University {
    private String name;
    private List<Course> courses;
    private List<Instructor> instructors;
    private List<Student> students;

    public University(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addInstructor(Instructor instructor) {
        instructors.add(instructor);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printStatistics() {
        System.out.println("\n=== University Statistics ===");
        System.out.println("Name: " + name);
        System.out.println("Courses: " + courses.size());
        System.out.println("Instructors: " + instructors.size());
        System.out.println("Students: " + students.size());

        int totalEnrollments = courses.stream()
            .mapToInt(Course::getEnrolledCount)
            .sum();
        System.out.println("Total Enrollments: " + totalEnrollments);
    }

    public String getName() { return name; }
}

public class CourseManagementDemo {
    public static void main(String[] args) {
        // Create university
        University university = new University("Tech University");

        // Create instructors (independent of courses)
        Instructor drSmith = new Instructor("I001", "Dr. Smith", "smith@uni.edu", "Computer Science");
        Instructor profJohnson = new Instructor("I002", "Prof. Johnson", "johnson@uni.edu", "Mathematics");
        Instructor drLee = new Instructor("I003", "Dr. Lee", "lee@uni.edu", "Computer Science");

        university.addInstructor(drSmith);
        university.addInstructor(profJohnson);
        university.addInstructor(drLee);

        // Create courses (aggregation with instructors)
        Course cs101 = new Course("CS101", "Intro to Programming", "Basic programming concepts", 3);
        Course cs201 = new Course("CS201", "Data Structures", "Advanced data structures", 4);
        Course math101 = new Course("MATH101", "Calculus I", "Differential calculus", 4);

        cs101.addInstructor(drSmith);
        cs201.addInstructor(drLee);
        math101.addInstructor(profJohnson);

        university.addCourse(cs101);
        university.addCourse(cs201);
        university.addCourse(math101);

        // Create sections
        CourseSection cs101Section1 = new CourseSection("CS101-001", DayOfWeek.MONDAY, "10:00-11:30", "Room 101", 30);
        CourseSection cs101Section2 = new CourseSection("CS101-002", DayOfWeek.WEDNESDAY, "14:00-15:30", "Room 102", 30);
        cs101.addSection(cs101Section1);
        cs101.addSection(cs101Section2);

        // Create students (independent of courses)
        Student alice = new Student("S001", "Alice Johnson", "alice@uni.edu");
        Student bob = new Student("S002", "Bob Smith", "bob@uni.edu");
        Student charlie = new Student("S003", "Charlie Brown", "charlie@uni.edu");

        university.addStudent(alice);
        university.addStudent(bob);
        university.addStudent(charlie);

        // Enroll students in courses
        System.out.println("=== Enrolling Students ===");
        alice.enrollInCourse(cs101);
        alice.enrollInCourse(cs201);
        bob.enrollInCourse(cs101);
        bob.enrollInCourse(math101);
        charlie.enrollInCourse(cs101);
        charlie.enrollInCourse(cs201);

        // Print course information
        cs101.printInfo();
        cs201.printInfo();

        // Print university statistics
        university.printStatistics();

        // Demonstrate independent lifecycle
        System.out.println("\n=== Independent Lifecycle Demo ===");
        System.out.println("Instructor " + drSmith.getName() + " teaches " +
            drSmith.getCourses().size() + " courses");
        System.out.println("Student " + alice.getName() + " is enrolled in " +
            alice.getEnrolledCount() + " courses");
    }
}
```

**Expected Output**:
```
=== Enrolling Students ===
Alice Johnson enrolled in Intro to Programming
Alice Johnson enrolled in Data Structures
Bob Smith enrolled in Intro to Programming
Bob Smith enrolled in Calculus I
Charlie Brown enrolled in Intro to Programming
Charlie Brown enrolled in Data Structures

=== Course: CS101 - Intro to Programming ===
Credits: 3
Description: Basic programming concepts
Instructors:
- Dr. Smith (Computer Science)
Enrolled Students: 3
Waitlisted Students: 0

=== Course: CS201 - Data Structures ===
Credits: 4
Description: Advanced data structures
Instructors:
- Dr. Lee (Computer Science)
Enrolled Students: 2
Waitlisted Students: 0

=== University Statistics ===
Name: Tech University
Courses: 3
Instructors: 3
Students: 3
Total Enrollments: 7

=== Independent Lifecycle Demo ===
Instructor Dr. Smith teaches 1 courses
Student Alice Johnson is enrolled in 2 courses
```

**Code Walkthrough**:

1. **Instructor Independence**: Instructors exist independently and can teach multiple courses
2. **Student Independence**: Students exist independently and can enroll in multiple courses
3. **Course Aggregation**: Courses aggregate instructors and students without managing their lifecycle
4. **Section Management**: Course sections manage enrollment capacity independently

**Alternative Solution**:

```java
// Using interfaces for flexible aggregation
interface Enrollable {
    void enroll(Course course);
    void drop(Course course);
}

interface Teachable {
    void assignCourse(Course course);
    void unassignCourse(Course course);
}

// This allows different types of entities to participate in aggregation
```

## Hard Examples

### Example 1: Project Management System

**Problem Statement**: Design a project management system with complex aggregation relationships between Projects, Teams, Members, Tasks, and Resources, supporting resource sharing across projects and independent lifecycle management.

**Requirements**:

- Support resource sharing across multiple projects
- Handle team membership across projects
- Manage task dependencies and assignments
- Support independent lifecycle for all entities

**Architecture**:

```
Project Management System
├── Project
│   ├── Teams (Aggregation)
│   ├── Tasks (Composition)
│   └── Resources (Aggregation)
├── Team
│   ├── Members (Aggregation)
│   └── Projects (Aggregation)
├── Member
│   ├── Teams (Aggregation)
│   └── Tasks (Assignment)
└── Resource
    └── Projects (Aggregation)
```

**Implementation**:

```java
package academy.javaengineering.oop.aggregation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Resource {
    private String id;
    private String name;
    private ResourceType type;
    private double costPerHour;
    private List<Project> assignedProjects; // Can be assigned to multiple projects

    public enum ResourceType {
        HUMAN, EQUIPMENT, SOFTWARE, FACILITY
    }

    public Resource(String id, String name, ResourceType type, double costPerHour) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.costPerHour = costPerHour;
        this.assignedProjects = new ArrayList<>();
    }

    public void assignToProject(Project project) {
        if (!assignedProjects.contains(project)) {
            assignedProjects.add(project);
            project.addResource(this);
        }
    }

    public void unassignFromProject(Project project) {
        assignedProjects.remove(project);
        project.removeResource(this);
    }

    public double calculateCost(int hours) {
        return costPerHour * hours;
    }

    public String getName() { return name; }
    public ResourceType getType() { return type; }
    public double getCostPerHour() { return costPerHour; }
    public List<Project> getAssignedProjects() { return new ArrayList<>(assignedProjects); }
    public boolean isAvailable() { return assignedProjects.size() < 3; } // Max 3 projects
}

class Task {
    private String id;
    private String name;
    private String description;
    private TaskStatus status;
    private int estimatedHours;
    private int actualHours;
    private Member assignedMember;
    private List<Task> dependencies;
    private Project project; // Composition: Task belongs to Project

    public enum TaskStatus {
        TODO, IN_PROGRESS, REVIEW, COMPLETED, BLOCKED
    }

    public Task(String id, String name, String description, int estimatedHours) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.estimatedHours = estimatedHours;
        this.status = TaskStatus.TODO;
        this.dependencies = new ArrayList<>();
    }

    public void assignToMember(Member member) {
        this.assignedMember = member;
        member.assignTask(this);
    }

    public void addDependency(Task task) {
        if (!dependencies.contains(task)) {
            dependencies.add(task);
        }
    }

    public boolean canStart() {
        for (Task dependency : dependencies) {
            if (dependency.getStatus() != TaskStatus.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    public void start() {
        if (canStart()) {
            this.status = TaskStatus.IN_PROGRESS;
        } else {
            this.status = TaskStatus.BLOCKED;
        }
    }

    public void complete(int actualHours) {
        this.actualHours = actualHours;
        this.status = TaskStatus.COMPLETED;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public TaskStatus getStatus() { return status; }
    public int getEstimatedHours() { return estimatedHours; }
    public int getActualHours() { return actualHours; }
    public Member getAssignedMember() { return assignedMember; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}

class Member {
    private String id;
    private String name;
    private String role;
    private double hourlyRate;
    private List<Team> teams; // Aggregation: Member can be in multiple teams
    private List<Task> assignedTasks; // Aggregation: Member can have multiple tasks

    public Member(String id, String name, String role, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.hourlyRate = hourlyRate;
        this.teams = new ArrayList<>();
        this.assignedTasks = new ArrayList<>();
    }

    public void joinTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
            team.addMember(this);
        }
    }

    public void leaveTeam(Team team) {
        teams.remove(team);
        team.removeMember(this);
    }

    public void assignTask(Task task) {
        if (!assignedTasks.contains(task)) {
            assignedTasks.add(task);
        }
    }

    public void unassignTask(Task task) {
        assignedTasks.remove(task);
    }

    public int calculateTotalHours() {
        return assignedTasks.stream()
            .mapToInt(Task::getActualHours)
            .sum();
    }

    public String getName() { return name; }
    public String getRole() { return role; }
    public double getHourlyRate() { return hourlyRate; }
    public List<Team> getTeams() { return new ArrayList<>(teams); }
    public List<Task> getAssignedTasks() { return new ArrayList<>(assignedTasks); }
}

class Team {
    private String id;
    private String name;
    private List<Member> members; // Aggregation: Team has Members
    private List<Project> projects; // Aggregation: Team works on Projects

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void addMember(Member member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    public void assignToProject(Project project) {
        if (!projects.contains(project)) {
            projects.add(project);
            project.addTeam(this);
        }
    }

    public void removeFromProject(Project project) {
        projects.remove(project);
        project.removeTeam(this);
    }

    public String getName() { return name; }
    public List<Member> getMembers() { return new ArrayList<>(members); }
    public List<Project> getProjects() { return new ArrayList<>(projects); }
    public int getMemberCount() { return members.size(); }
}

class Project {
    private String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private List<Team> teams; // Aggregation: Project has Teams
    private List<Task> tasks; // Composition: Project owns Tasks
    private List<Resource> resources; // Aggregation: Project uses Resources

    public enum ProjectStatus {
        PLANNING, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    }

    public Project(String id, String name, String description, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ProjectStatus.PLANNING;
        this.teams = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.resources = new ArrayList<>();
    }

    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public Task createTask(String name, String description, int estimatedHours) {
        Task task = new Task("T" + (tasks.size() + 1), name, description, estimatedHours);
        task.setProject(this);
        tasks.add(task);
        return task;
    }

    public void addResource(Resource resource) {
        if (!resources.contains(resource)) {
            resources.add(resource);
        }
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    public void start() {
        this.status = ProjectStatus.IN_PROGRESS;
    }

    public void complete() {
        this.status = ProjectStatus.COMPLETED;
    }

    public void printStatus() {
        System.out.println("\n=== Project: " + name + " ===");
        System.out.println("Status: " + status);
        System.out.println("Teams: " + teams.size());
        System.out.println("Tasks: " + tasks.size());
        System.out.println("Resources: " + resources.size());

        long completedTasks = tasks.stream()
            .filter(t -> t.getStatus() == Task.TaskStatus.COMPLETED)
            .count();
        System.out.println("Completed Tasks: " + completedTasks + "/" + tasks.size());
    }

    public String getName() { return name; }
    public ProjectStatus getStatus() { return status; }
    public List<Team> getTeams() { return new ArrayList<>(teams); }
    public List<Task> getTasks() { return new ArrayList<>(tasks); }
    public List<Resource> getResources() { return new ArrayList<>(resources); }
}

public class ProjectManagementDemo {
    public static void main(String[] args) {
        // Create resources (independent, can be shared)
        Resource seniorDev = new Resource("R001", "Senior Developer", Resource.ResourceType.HUMAN, 150.0);
        Resource juniorDev = new Resource("R002", "Junior Developer", Resource.ResourceType.HUMAN, 80.0);
        Resource designer = new Resource("R003", "UI Designer", Resource.ResourceType.HUMAN, 120.0);

        // Create members (independent)
        Member alice = new Member("M001", "Alice Johnson", "Senior Developer", 150.0);
        Member bob = new Member("M002", "Bob Smith", "Junior Developer", 80.0);
        Member charlie = new Member("M003", "Charlie Brown", "Designer", 120.0);

        // Create teams (aggregation with members)
        Team devTeam = new Team("T001", "Development Team");
        Team designTeam = new Team("T002", "Design Team");

        devTeam.addMember(alice);
        devTeam.addMember(bob);
        designTeam.addMember(charlie);

        // Members can be in multiple teams
        alice.joinTeam(designTeam); // Alice is in both teams

        // Create projects (aggregation with teams and resources)
        Project website = new Project("P001", "Website Redesign",
            "Redesign company website", LocalDate.now(), LocalDate.now().plusMonths(3));
        Project mobileApp = new Project("P002", "Mobile App",
            "Develop mobile application", LocalDate.now(), LocalDate.now().plusMonths(6));

        website.addTeam(devTeam);
        website.addTeam(designTeam);
        website.addResource(seniorDev);
        website.addResource(designer);

        mobileApp.addTeam(devTeam);
        mobileApp.addResource(seniorDev);
        mobileApp.addResource(juniorDev);

        // Create tasks (composition: tasks belong to projects)
        Task designTask = website.createTask("UI Design", "Design new UI", 40);
        Task devTask = website.createTask("Frontend Development", "Implement new UI", 80);
        Task testingTask = website.createTask("Testing", "Test website", 20);

        // Set task dependencies
        devTask.addDependency(designTask);
        testingTask.addDependency(devTask);

        // Assign tasks to members
        designTask.assignToMember(charlie);
        devTask.assignToMember(alice);
        testingTask.assignToMember(bob);

        // Start project
        website.start();

        // Simulate task progress
        System.out.println("=== Task Progress ===");
        designTask.start();
        System.out.println("Design task started");

        designTask.complete(35);
        System.out.println("Design task completed in " + designTask.getActualHours() + " hours");

        devTask.start();
        System.out.println("Development task started");

        devTask.complete(75);
        System.out.println("Development task completed in " + devTask.getActualHours() + " hours");

        testingTask.start();
        System.out.println("Testing task started");

        // Print project status
        website.printStatus();
        mobileApp.printStatus();

        // Demonstrate independent lifecycle
        System.out.println("\n=== Independent Lifecycle Demo ===");
        System.out.println("Resource " + seniorDev.getName() + " is assigned to " +
            seniorDev.getAssignedProjects().size() + " projects");
        System.out.println("Member " + alice.getName() + " is in " +
            alice.getTeams().size() + " teams");
        System.out.println("Member " + alice.getName() + " has " +
            alice.getAssignedTasks().size() + " tasks");

        // Resource can be unassigned
        seniorDev.unassignFromProject(mobileApp);
        System.out.println("After unassigning: " + seniorDev.getName() + " is assigned to " +
            seniorDev.getAssignedProjects().size() + " projects");
    }
}
```

**Execution Flow**:

1. **Resource Creation**: Resources are created independently and can be shared
2. **Member Creation**: Members are created and assigned to teams
3. **Team Formation**: Teams are formed with member aggregation
4. **Project Setup**: Projects aggregate teams and resources
5. **Task Creation**: Tasks are created as composition within projects
6. **Task Assignment**: Tasks are assigned to members (aggregation)
7. **Progress Tracking**: Tasks are tracked through their lifecycle

**Unit Tests**:

```java
public class ProjectManagementTest {
    public static void main(String[] args) {
        System.out.println("=== Running Project Management Tests ===\n");

        testResourceSharing();
        testTeamMembership();
        taskDependencies();
        testProjectLifecycle();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testResourceSharing() {
        System.out.println("Test 1: Resource Sharing");
        Resource resource = new Resource("R001", "Developer", Resource.ResourceType.HUMAN, 100.0);
        Project project1 = new Project("P001", "Project 1", "Desc", LocalDate.now(), LocalDate.now().plusMonths(1));
        Project project2 = new Project("P002", "Project 2", "Desc", LocalDate.now(), LocalDate.now().plusMonths(1));

        resource.assignToProject(project1);
        resource.assignToProject(project2);

        assert resource.getAssignedProjects().size() == 2 : "Resource should be in 2 projects";
        assert project1.getResources().contains(resource) : "Project 1 should have resource";
        assert project2.getResources().contains(resource) : "Project 2 should have resource";

        System.out.println("  PASS: Resource sharing test passed\n");
    }

    private static void testTeamMembership() {
        System.out.println("Test 2: Team Membership");
        Member member = new Member("M001", "Alice", "Developer", 100.0);
        Team team1 = new Team("T001", "Team 1");
        Team team2 = new Team("T002", "Team 2");

        member.joinTeam(team1);
        member.joinTeam(team2);

        assert member.getTeams().size() == 2 : "Member should be in 2 teams";
        assert team1.getMembers().contains(member) : "Team 1 should have member";

        System.out.println("  PASS: Team membership test passed\n");
    }

    private static void taskDependencies() {
        System.out.println("Test 3: Task Dependencies");
        Task task1 = new Task("T001", "Task 1", "Desc", 10);
        Task task2 = new Task("T002", "Task 2", "Desc", 20);

        task2.addDependency(task1);

        assert !task2.canStart() : "Task 2 should not start before task 1 completes";

        task1.complete(10);
        assert task2.canStart() : "Task 2 should start after task 1 completes";

        System.out.println("  PASS: Task dependencies test passed\n");
    }

    private static void testProjectLifecycle() {
        System.out.println("Test 4: Project Lifecycle");
        Project project = new Project("P001", "Test Project", "Desc",
            LocalDate.now(), LocalDate.now().plusMonths(1));

        assert project.getStatus() == Project.ProjectStatus.PLANNING : "Should be in planning";

        project.start();
        assert project.getStatus() == Project.ProjectStatus.IN_PROGRESS : "Should be in progress";

        project.complete();
        assert project.getStatus() == Project.ProjectStatus.COMPLETED : "Should be completed";

        System.out.println("  PASS: Project lifecycle test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(n) for project operations where n is number of tasks/teams
- **Space Complexity**: O(n + m) where n is tasks and m is team/resource associations

**Best Practices**:

- Use aggregation for shared resources that can be assigned to multiple containers
- Maintain independence of aggregated objects' lifecycles
- Document the rules for resource sharing and assignment
- Consider performance implications of deep aggregation chains

## Exercises

### Easy

1. **Music Library**: Create a music library system where songs, artists, and albums exist independently and can be aggregated into playlists.

2. **Shopping Cart**: Design a shopping cart where products exist independently and can be added to multiple carts.

3. **Classroom**: Create a classroom system where teachers, students, and courses exist independently.

### Medium

1. **Conference System**: Design a conference system with speakers, sessions, and attendees that can be aggregated into different conferences.

2. **Inventory System**: Create an inventory system where products can be shared across multiple warehouses.

3. **Social Network**: Design a social network where users, posts, and groups have independent lifecycles.

### Hard

1. **Enterprise Resource Planning**: Create an ERP system with complex resource sharing across departments, projects, and tasks.

2. **Healthcare System**: Design a healthcare system where doctors, patients, and resources are shared across multiple hospitals.

3. **Airport Management**: Create an airport system where aircraft, gates, and staff are shared across multiple airlines.

## Interview Questions

### Easy

1. **What is aggregation in object-oriented programming?**
   Aggregation is a "has-a" relationship where the contained object can exist independently of the container. It represents weak ownership where objects have independent lifecycles and can be shared across multiple containers.

2. **How does aggregation differ from composition?**
   In aggregation, contained objects have independent lifecycles and can exist without the container. In composition, contained objects are tightly coupled to the container and are destroyed when the container is destroyed.

3. **When should you use aggregation?**
   Use aggregation when objects logically belong together but have independent lifecycles, when objects can be shared across multiple containers, or when you need loose coupling between related objects.

### Medium

1. **What are the benefits of aggregation?**
   Aggregation provides loose coupling, independent lifecycles, resource sharing, better reusability, and more flexible designs. It allows objects to be composed from reusable components without tight dependencies.

2. **How do you handle bidirectional aggregation?**
   Maintain both sides of the relationship consistently. Use helper methods to ensure atomicity. Document the relationship clearly. Consider the performance implications of bidirectional navigation.

3. **What are the implications of shared instances in aggregation?**
   Shared instances can lead to unintended side effects, require careful synchronization in concurrent environments, and may complicate lifecycle management. Consider thread safety and ownership semantics.

### Hard

1. **How do you prevent memory leaks in aggregation?**
   Use weak references when appropriate, implement proper cleanup methods, avoid circular references, and rely on garbage collection. Monitor object retention and implement dispose patterns for resource-heavy objects.

2. **How does aggregation affect system design and architecture?**
   Aggregation influences coupling, testability, and maintainability. It enables flexible compositions but requires careful design to avoid tight coupling. Consider the impact on domain models and persistence strategies.

## Common Pitfalls

### 1. Managing Lifecycle of Aggregated Objects

**Wrong**:
```java
class Library {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void destroy() {
        books.clear(); // This doesn't destroy the books!
        // Books still exist and may be referenced elsewhere
    }
}
```

**Right**:
```java
class Library {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
        // Book continues to exist independently
        // Don't try to destroy it here
    }

    public void dispose() {
        books.clear(); // Just clear references
        // Books are garbage collected when no longer referenced
    }
}
```

### 2. Not Handling Shared Instances Properly

**Wrong**:
```java
class Project {
    private List<Resource> resources = new ArrayList<>();

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void complete() {
        for (Resource resource : resources) {
            resource.release(); // This releases the resource for ALL projects!
        }
    }
}
```

**Right**:
```java
class Project {
    private List<Resource> resources = new ArrayList<>();

    public void addResource(Resource resource) {
        resources.add(resource);
        resource.assignToProject(this);
    }

    public void complete() {
        for (Resource resource : resources) {
            resource.unassignFromProject(this); // Only release from this project
        }
        resources.clear();
    }
}
```

### 3. Creating Circular References

**Wrong**:
```java
class Department {
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }
}

class Employee {
    private Department department;

    public void setDepartment(Department dept) {
        this.department = dept;
        dept.addEmployee(this); // Creates circular reference!
    }
}
```

**Right**:
```java
class Department {
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
            employee.setDepartment(this);
        }
    }
}

class Employee {
    private Department department;

    public void setDepartment(Department dept) {
        if (this.department != null) {
            this.department.getEmployees().remove(this);
        }
        this.department = dept;
        // Don't call dept.addEmployee() here
    }
}
```

## Best Practices

1. **Use aggregation for independent lifecycles**: When contained objects can exist without the container, use aggregation. This provides flexibility and reusability.

2. **Avoid managing lifecycle**: Don't try to destroy or clean up aggregated objects. Let them be garbage collected when no longer referenced.

3. **Handle shared instances carefully**: When objects are shared across multiple containers, be careful about state changes that might affect other containers.

4. **Document relationship semantics**: Clearly document whether relationships are aggregation, composition, or association, and the rules for object sharing.

5. **Consider thread safety**: If aggregated objects are shared across threads, ensure proper synchronization and thread-safe access patterns.

## Real World Usage

### How Spring Uses This

Spring Framework uses aggregation for:

- **Bean Dependencies**: Beans aggregate other beans without managing their lifecycle
- **ApplicationContext**: Aggregates bean definitions and configuration
- **Event Publishing**: Events aggregate listeners without tight coupling

### How Hibernate Uses This

Hibernate ORM uses aggregation for:

- **Entity Relationships**: Many-to-many relationships use aggregation
- **Shared References**: Multiple entities can reference the same object
- **Lazy Loading**: Aggregated objects can be loaded on demand

### How JDK Uses This

The Java Development Kit uses aggregation in:

- **Collections**: Collections aggregate elements without owning them
- **Thread Groups**: Thread groups aggregate threads without lifecycle management
- **Class Loaders**: Class loaders aggregate classes without ownership

### Enterprise Usage

In enterprise applications, aggregation is used for:

- **Resource Sharing**: Resources are shared across projects and departments
- **Team Composition**: Team members can belong to multiple teams
- **Project Collaboration**: Projects aggregate teams and resources without ownership

## References

1. **UML Distilled** by Martin Fowler - Aggregation vs Composition
2. **Effective Java** by Joshua Bloch - Item 25: Prefer lists to arrays
3. **Domain-Driven Design** by Eric Evans - Aggregate roots
4. **Clean Architecture** by Robert C. Martin - Component relationships
5. **Design Patterns** - Composite and Decorator patterns

## Summary

- Aggregation is a "has-a" relationship with independent lifecycles
- Contained objects can exist without the container and can be shared
- Aggregation provides loose coupling and better reusability
- Don't manage lifecycle of aggregated objects
- Handle shared instances carefully to avoid unintended side effects
- Use aggregation when objects logically belong together but have independent lifecycles

**Next Steps**: [22-dependency](../22-dependency/README.md)
