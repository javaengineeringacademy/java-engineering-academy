# Introduction to Object-Oriented Programming

Object-Oriented Programming (OOP) is a programming approach that organizes software design around objects rather than functions and logic. An object is a data field that has unique attributes and behavior. OOP focuses on binding data and the methods that operate on that data into a single unit, and restricting direct access to some of the object's components. This approach models real-world entities as objects, making complex systems more manageable, reusable, and easier to understand.

## Learning Objectives
By the end of this topic, you will be able to:
- Understand the fundamental concepts of Object-Oriented Programming
- Differentiate between procedural and object-oriented programming approachs
- Identify the four pillars of OOP: Encapsulation, Inheritance, Polymorphism, and Abstraction
- Recognize the benefits of using OOP in software development

## Prerequisites
- Basic understanding of Java syntax and programming fundamentals
- Familiarity with variables, data types, and control structures
- Knowledge of functions/methods

## Why This Concept Exists

### The Problem
In procedural programming, code is organized as a sequence of instructions. As programs grow larger, this approach leads to several issues:
- **Code duplication**: Similar logic is repeated across multiple functions
- **Tight coupling**: Changes in one part of the code affect other parts
- **Difficulty in maintenance**: Large codebases become hard to understand and modify
- **Lack of reusability**: Code cannot be easily reused in different parts of an application

### The Solution
OOP addresses these problems by:
- **Organizing code into objects**: Each object encapsulates data and behavior
- **Promoting modularity**: Objects are self-contained units
- **Enabling inheritance**: New classes can reuse and extend existing classes
- **Providing polymorphism**: Objects can be treated through common interfaces
- **Enforcing encapsulation**: Internal state is protected from external interference

### Real-World Analogy
Think of a car. A car has attributes (color, model, speed) and behaviors (accelerate, brake, steer). In OOP, we model this as a `Car` object with properties and methods. Multiple car objects can be created from the same blueprint (class), each with its own state but sharing the same behaviors.

## Internal Working

### JVM Perspective
When Java code is compiled, the JVM loads classes into memory. Each class definition becomes a blueprint for creating objects. The JVM manages object creation, memory allocation, and garbage collection. When you create an object using `new`, the JVM:
1. Allocates memory on the heap for the object
2. Initializes the object's state (instance variables)
3. Returns a reference to the object

### Memory Representation
```java
// When we write:
Car myCar = new Car("Toyota", "Camry");

// JVM creates:
// Stack: myCar (reference) -> points to Heap
// Heap: Car object with:
//   - make: "Toyota"
//   - model: "Camry"
//   - speed: 0
```

## Syntax

```java
// Basic class definition
public class ClassName {
    // Instance variables (attributes)
    private String attribute;
    
    // Constructor
    public ClassName(String value) {
        this.attribute = value;
    }
    
    // Method (behavior)
    public String getAttribute() {
        return attribute;
    }
}

// Creating an object
ClassName object = new ClassName("value");
```

## Easy Examples

### Example 1: First Java Class
**Problem Statement:**
Create a simple class to represent a person with basic attributes and methods.

**Implementation:**
```java
package academy.javaengineering.oop.introduction;

public class Person {
    // Instance variables
    private String name;
    private int age;
    
    // Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Method to display information
    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
    
    // Main method to test the class
    public static void main(String[] args) {
        // Creating objects
        Person person1 = new Person("Alice", 25);
        Person person2 = new Person("Bob", 30);
        
        // Calling methods
        person1.displayInfo();
        person2.displayInfo();
    }
}
```

```
**Expected Output:**
```
Name: Alice, Age: 25
Name: Bob, Age: 30
```

**Best Practices:**
- Use meaningful class names that represent real-world entities
- Keep classes focused on a single responsibility
- Use private access modifiers for instance variables

### Example 2: Using Static Members
**Problem Statement:**
Demonstrate the difference between instance and static members.

**Implementation:**
```java
package academy.javaengineering.oop.introduction;

public class Counter {
    // Instance variable
    private int count;
    
    // Static variable (shared across all instances)
    private static int totalCount = 0;
    
    // Constructor
    public Counter() {
        this.count = 0;
        totalCount++;
    }
    
    // Instance method
    public void increment() {
        count++;
    }
    
    // Static method
    public static int getTotalCount() {
        return totalCount;
    }
    
    public static void main(String[] args) {
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = new Counter();
        
        c1.increment();
        c1.increment();
        c2.increment();
        
        System.out.println("Total counters created: " + Counter.getTotalCount());
        System.out.println("c1 count: " + c1.count);
        System.out.println("c2 count: " + c2.count);
        System.out.println("c3 count: " + c3.count);
    }
}
```

**Expected Output:**
```
Total counters created: 3
c1 count: 2
c2 count: 1
c3 count: 0
```

**Best Practices:**
- Use static members for data that should be shared across all instances
- Avoid excessive use of static variables as they can lead to tight coupling
- Prefer instance variables for object-specific data

## Medium Examples

### Example 1: Bank Account System
**Problem Statement:**
Design a simple bank account system with deposit, withdrawal, and balance checking functionalities.
**Requirements:**
- Account should have account number, holder name, and balance
- Support deposit and withdrawal operations
- Prevent negative balances
- Display account information

**Implementation:**
```java
package academy.javaengineering.oop.introduction;

public class BankAccount {
    private String accountNumber;
    private String holderName;
    private double balance;
    
    public BankAccount(String accountNumber, String holderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialBalance;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
        } else if (amount > balance) {
            System.out.println("Insufficient funds");
        } else {
            System.out.println("Invalid withdrawal amount");
        }
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void displayAccountInfo() {
        System.out.println("Account: " + accountNumber);
        System.out.println("Holder: " + holderName);
        System.out.println("Balance: $" + balance);
    }
    
    public static void main(String[] args) {
        BankAccount account = new BankAccount("123456789", "John Doe", 1000.00);
        
        account.displayAccountInfo();
        System.out.println();
        
        account.deposit(500.00);
        account.withdraw(200.00);
        account.withdraw(2000.00); // Insufficient funds
        
        System.out.println();
        System.out.println("Final Balance: $" + account.getBalance());
    }
}
```

**Code Walkthrough:**
1. We define a `BankAccount` class with private instance variables for encapsulation
2. The constructor initializes the account with account number, holder name, and initial balance
3. The `deposit` method validates the amount and adds it to the balance
4. The `withdraw` method checks for sufficient funds before withdrawal
5. The `main` method demonstrates creating an account and performing operations

**Expected Output:**
```
Account: 123456789
Holder: John Doe
Balance: $1000.0

Deposited: $500.0
Withdrawn: $200.0
Insufficient funds

Final Balance: $1300.0
```

**Alternative Solution:**
```java
// Using enum for transaction types
public enum TransactionType {
    DEPOSIT, WITHDRAWAL, BALANCE_CHECK
}

// Enhanced BankAccount with transaction logging
public class EnhancedBankAccount extends BankAccount {
    private List<String> transactionHistory;
    
    public EnhancedBankAccount(String accountNumber, String holderName, double initialBalance) {
        super(accountNumber, holderName, initialBalance);
        this.transactionHistory = new ArrayList<>();
    }
    
    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        transactionHistory.add("Deposit: $" + amount);
    }
    
    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        transactionHistory.add("Withdrawal: $" + amount);
    }
    
    public void printTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory) {
            System.out.println("- " + transaction);
        }
    }
}
```

## Hard Examples

### Example 1: Library Management System
**Problem Statement:**
Design a comprehensive library management system that handles books, members, and borrowing operations.
**Requirements:**
- Support multiple book types (fiction, non-fiction, reference)
- Member management with different membership types
- Borrowing and returning functionality
- Overdue fine calculation
- Search functionality

**Architecture:**
```
library-system/
├── models/
│   ├── Book.java
│   ├── FictionBook.java
│   ├── NonFictionBook.java
│   ├── ReferenceBook.java
│   ├── Member.java
│   └── BorrowingRecord.java
├── services/
│   ├── LibraryService.java
│   ├── BookService.java
│   └── MemberService.java
├── exceptions/
│   ├── BookNotAvailableException.java
│   ├── MemberNotFoundException.java
│   └── OverdueException.java
└── Main.java
```

**Implementation:**
```java
package academy.javaengineering.oop.introduction;

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Book classes
public abstract class Book {
    private String isbn;
    private String title;
    private String author;
    private boolean available;
    
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = true;
    }
    
    // Getters and setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public abstract String getBookType();
    
    @Override
    public String toString() {
        return getBookType() + " - " + title + " by " + author + 
               " (ISBN: " + isbn + ") - " + (available ? "Available" : "Borrowed");
    }
}

public class FictionBook extends Book {
    private String genre;
    
    public FictionBook(String isbn, String title, String author, String genre) {
        super(isbn, title, author);
        this.genre = genre;
    }
    
    @Override

---

## Continue Reading

- Part 2
- Part 3
