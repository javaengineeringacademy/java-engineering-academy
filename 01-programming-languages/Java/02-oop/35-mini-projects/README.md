# Mini Projects

## Overview

Apply all OOP concepts through 14 progressive projects organized by difficulty level. Each project reinforces specific OOP principles and provides hands-on experience with real-world scenarios. Projects are designed to be completed incrementally — start with the simplest version and add complexity as you master each concept.

## Difficulty Levels

### Beginner (1-2 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 1 | [Student Management](#project-1-student-management) | Classes, objects, encapsulation, methods | 1 week |
| 2 | [Library Management](#project-2-library-management) | Inheritance, polymorphism, composition | 1 week |

### Intermediate (2-3 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 3 | [Employee Management](#project-3-employee-management) | Abstract classes, interfaces, SOLID | 2 weeks |
| 4 | [Bank Management](#project-4-bank-management) | All OOP concepts, design patterns | 2 weeks |
| 5 | [Hospital Management](#project-5-hospital-management) | Complex relationships, state management | 3 weeks |

### Advanced (3-4 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 6 | [Inventory Management](#project-6-inventory-management) | Collections, generics, file I/O | 3 weeks |
| 7 | [Parking System](#project-7-parking-system) | Design patterns, concurrency basics | 3 weeks |
| 8 | [Food Delivery](#project-8-food-delivery) | API design, event handling | 4 weeks |
| 9 | [Movie Booking](#project-9-movie-booking) | Concurrent access, booking logic | 3 weeks |
| 10 | [Ride Sharing](#project-10-ride-sharing) | Matching algorithms, geolocation | 4 weeks |

### Enterprise (4-6 weeks each)

| # | Project | Concepts Reinforced | Est. Time |
|---|---------|---------------------|-----------|
| 11 | [Order Processing](#project-11-order-processing) | Full stack, persistence, transactions | 4 weeks |
| 12 | [Payment Gateway](#project-12-payment-gateway) | Security, API design, error handling | 5 weeks |
| 13 | [E-Commerce](#project-13-e-commerce) | Microservices, scalability | 6 weeks |
| 14 | [Trading Platform](#project-14-trading-platform) | Performance, concurrency, reliability | 6 weeks |

---

## Project 1: Student Management

### Description

A console-based application to manage student records — add, update, delete, search, and display students.

### Requirements

**Functional Requirements**:
- Add a new student (ID, name, age, grade, email)
- Update student information by ID
- Delete a student by ID
- Search students by name or grade
- Display all students sorted by name or grade
- Calculate class average grade
- Display honor roll students (GPA >= 3.5)

**Non-Functional Requirements**:
- Data validation (age 5-100, valid email format, grade A-F)
- Persistent storage using file I/O
- Clean console output with formatting
- Graceful error handling

### Architecture

```
┌──────────────────────────────────┐
│         Main (entry point)       │
├──────────────────────────────────┤
│       StudentManager             │
│  - addStudent(Student)           │
│  - updateStudent(id, Student)    │
│  - deleteStudent(id)             │
│  - searchByName(name)            │
│  - searchByGrade(grade)          │
│  - getAllStudents()              │
│  - getClassAverage()             │
│  - getHonorRoll()               │
├──────────────────────────────────┤
│         Student (model)          │
│  - id, name, age, grade, email   │
│  - getGPA()                      │
│  - isHonorRoll()                 │
├──────────────────────────────────┤
│       StudentRepository          │
│  - save(List<Student>)           │
│  - load() → List<Student>        │
└──────────────────────────────────┘
```

### Learning Outcomes

- Understand classes and objects (creating `Student` instances)
- Practice encapsulation (private fields with getters/setters)
- Apply method design (single responsibility methods)
- Implement file I/O for persistence
- Practice input validation and error handling

### Implementation Checklist

- [ ] Create `Student` class with fields, constructor, getters, setters
- [ ] Create `StudentManager` class with CRUD operations
- [ ] Create `StudentRepository` for file persistence
- [ ] Create `Main` class with console menu
- [ ] Implement data validation
- [ ] Add search functionality
- [ ] Add sorting capabilities
- [ ] Add statistics (average, honor roll)
- [ ] Write unit tests
- [ ] Add error handling for file operations

---

## Project 2: Library Management

### Description

A library management system supporting books, members, and borrow/return operations with different member types.

### Requirements

**Functional Requirements**:
- Add/update/remove books (title, author, ISBN, genre, copies)
- Register members (Regular, Premium, Student)
- Borrow books (different borrowing limits per member type)
- Return books with fine calculation for overdue
- Search books by title, author, or genre
- View borrowing history
- Reserve books

**Non-Functional Requirements**:
- Polymorphic behavior for different member types
- Fine calculation based on days overdue
- Book availability tracking
- Member borrowing limits

### Architecture

```
┌─────────────────────────────────────────────┐
│              LibrarySystem                   │
├─────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │   Book   │  │  Member  │  │ Transaction│ │
│  └────┬─────┘  └────┬─────┘  └──────────┘  │
│       │             │                       │
│  ┌────▼─────┐  ┌────▼─────────────────┐    │
│  │  Genre   │  │ AbstractMember        │    │
│  │ (enum)   │  │ ├─ RegularMember      │    │
│  └──────────┘  │ ├─ PremiumMember      │    │
│                │ └─ StudentMember      │    │
│                └─────────────────────────┘  │
├─────────────────────────────────────────────┤
│         BookRepository                      │
│         MemberRepository                    │
│         TransactionRepository               │
└─────────────────────────────────────────────┘
```

### Learning Outcomes

- Apply inheritance and polymorphism (different member types)
- Use abstract classes and interfaces
- Implement the Strategy pattern for fine calculation
- Practice composition (Library HAS-A list of Books)
- Understand method overriding

### Implementation Checklist

- [ ] Create `Book` class with ISBN validation
- [ ] Create abstract `Member` class with `Borrowable` interface
- [ ] Implement `RegularMember`, `PremiumMember`, `StudentMember`
- [ ] Create `Transaction` class (borrow date, due date, return date)
- [ ] Implement `FineCalculator` with Strategy pattern
- [ ] Create `Library` class orchestrating all operations
- [ ] Add book search and filtering
- [ ] Implement reservation system
- [ ] Write unit tests for borrowing rules
- [ ] Add file persistence

---

## Project 3: Employee Management

### Description

An employee management system with different departments, roles, and payroll calculations.

### Requirements

**Functional Requirements**:
- Add employees with different roles (Developer, Manager, Director, Intern)
- Department management (Engineering, HR, Marketing, Finance)
- Payroll calculation (base salary + bonuses - deductions)
- Performance review tracking
- Report generation (department-wise, role-wise)
- Employee hierarchy display

**Non-Functional Requirements**:
- Abstract classes for employee hierarchy
- Interfaces for payroll and review strategies
- SOLID principle compliance
- File-based persistence

### Architecture

```
┌────────────────────────────────────────────────┐
│              EmployeeManagement                 │
├────────────────────────────────────────────────┤
│  ┌──────────────┐      ┌──────────────┐       │
│  │  Department  │      │    Role      │       │
│  │  (enum)      │      │  (enum)      │       │
│  └──────────────┘      └──────────────┘       │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │        AbstractEmployee              │     │
│  │  ├─ Developer                        │     │
│  │  ├─ Manager                          │     │
│  │  ├─ Director                         │     │
│  │  └─ Intern                           │     │
│  └──────────────────────────────────────┘     │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │     PayrollStrategy (interface)      │     │
│  │  ├─ StandardPayroll                  │     │
│  │  ├─ OvertimePayroll                  │     │
│  │  └─ CommissionPayroll                │     │
│  └──────────────────────────────────────┘     │
├────────────────────────────────────────────────┤
│         EmployeeRepository                     │
│         PayrollService                         │
│         ReportGenerator                        │
└────────────────────────────────────────────────┘
```

### Learning Outcomes

- Design abstract class hierarchies
- Implement interfaces for behavior variation
- Apply SOLID principles
- Use the Strategy pattern for payroll
- Practice the Template Method for reports

---

## Project 4: Bank Management

### Description

A banking system supporting multiple account types, transactions, and financial operations.

### Requirements

**Functional Requirements**:
- Account types: Savings, Current, Fixed Deposit
- Deposit, withdrawal, transfer between accounts
- Interest calculation (different for each account type)
- Transaction history with search
- Account statements (monthly/yearly)
- Overdraft protection for Current accounts

**Non-Functional Requirements**:
- Thread-safe transaction processing
- Transaction integrity (ACID-like)
- Account locking for concurrent access
- Audit trail for all operations

### Architecture

```
┌───────────────────────────────────────────────┐
│              BankingSystem                     │
├───────────────────────────────────────────────┤
│  ┌─────────────────────────────────────┐     │
│  │       AbstractAccount               │     │
│  │  ├─ SavingsAccount                  │     │
│  │  ├─ CurrentAccount                  │     │
│  │  └─ FixedDepositAccount             │     │
│  └─────────────────────────────────────┘     │
│                                                │
│  ┌─────────────────────────────────────┐     │
│  │      Transaction (record)           │     │
│  │  ├─ DepositTransaction              │     │
│  │  ├─ WithdrawalTransaction           │     │
│  │  └─ TransferTransaction             │     │
│  └─────────────────────────────────────┘     │
│                                                │
│  ┌─────────────────────────────────────┐     │
│  │     InterestStrategy (interface)    │     │
│  │  ├─ SimpleInterest                  │     │
│  │  ├─ CompoundInterest                │     │
│  │  └─ FixedDepositInterest            │     │
│  └─────────────────────────────────────┘     │
├───────────────────────────────────────────────┤
│         AccountRepository                     │
│         TransactionService                    │
│         StatementGenerator                    │
└───────────────────────────────────────────────┘
```

### Learning Outcomes

- Implement complex inheritance hierarchies
- Use the Strategy pattern for interest calculation
- Apply the Observer pattern for transaction notifications
- Practice thread safety with synchronized blocks
- Understand the Template Method pattern

---

## Project 5: Hospital Management

### Description

A hospital management system managing patients, doctors, appointments, and medical records.

### Requirements

**Functional Requirements**:
- Patient registration and medical history
- Doctor profiles with specializations
- Appointment scheduling (conflict detection)
- Ward and bed management
- Prescription and medication tracking
- Billing and insurance processing

**Non-Functional Requirements**:
- Complex entity relationships
- State management for appointments (Scheduled, InProgress, Completed, Cancelled)
- Role-based access (Admin, Doctor, Nurse, Receptionist)
- Data validation for medical records

### Architecture

```
┌─────────────────────────────────────────────────┐
│             HospitalManagement                   │
├─────────────────────────────────────────────────┤
│  ┌────────┐  ┌────────┐  ┌──────────────────┐  │
│  │ Patient│  │ Doctor │  │ Appointment      │  │
│  └────┬───┘  └────┬───┘  │ (state machine)  │  │
│       │           │       └──────────────────┘  │
│  ┌────▼───────────▼────┐  ┌──────────────────┐  │
│  │ MedicalRecord       │  │ Ward/Bed         │  │
│  │ (composed of)       │  │ Management       │  │
│  │ - Prescriptions     │  └──────────────────┘  │
│  │ - Lab Results       │                        │
│  │ - Vitals            │  ┌──────────────────┐  │
│  └─────────────────────┘  │ BillingService   │  │
│                            └──────────────────┘  │
├─────────────────────────────────────────────────┤
│     PatientRepository  DoctorRepository         │
│     AppointmentService BillingService           │
└─────────────────────────────────────────────────┘
```

### Learning Outcomes

- Model complex real-world relationships
- Implement state machines for appointment lifecycle
- Use composition for medical records
- Practice role-based design
- Handle complex validation rules

---

**[Continue to Part 2: Advanced Projects (6-10) →](README-part2.md)**