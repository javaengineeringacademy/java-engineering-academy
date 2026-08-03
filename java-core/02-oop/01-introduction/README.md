# Introduction to Object-Oriented Programming

Object-Oriented Programming (OOP) is a programming paradigm that organizes software design around objects rather than functions and logic. An object is a data field that has unique attributes and behavior. OOP focuses on binding data and the methods that operate on that data into a single unit, and restricting direct access to some of the object's components. This approach models real-world entities as objects, making complex systems more manageable, reusable, and easier to understand.

## Learning Objectives
By the end of this topic, you will be able to:
- Understand the fundamental concepts of Object-Oriented Programming
- Differentiate between procedural and object-oriented programming paradigms
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
    public String getBookType() {
        return "Fiction (" + genre + ")";
    }
}

public class NonFictionBook extends Book {
    private String subject;
    
    public NonFictionBook(String isbn, String title, String author, String subject) {
        super(isbn, title, author);
        this.subject = subject;
    }
    
    @Override
    public String getBookType() {
        return "Non-Fiction (" + subject + ")";
    }
}

public class ReferenceBook extends Book {
    private String edition;
    
    public ReferenceBook(String isbn, String title, String author, String edition) {
        super(isbn, title, author);
        this.edition = edition;
    }
    
    @Override
    public String getBookType() {
        return "Reference (Edition: " + edition + ")";
    }
}

// Member classes
public class Member {
    private String memberId;
    private String name;
    private String email;
    private MembershipType membershipType;
    private List<BorrowingRecord> borrowingHistory;
    
    public enum MembershipType {
        BASIC(2, 14),    // 2 books, 14 days
        PREMIUM(5, 30),  // 5 books, 30 days
        VIP(10, 60);     // 10 books, 60 days
        
        private final int maxBooks;
        private final int loanDays;
        
        MembershipType(int maxBooks, int loanDays) {
            this.maxBooks = maxBooks;
            this.loanDays = loanDays;
        }
        
        public int getMaxBooks() { return maxBooks; }
        public int getLoanDays() { return loanDays; }
    }
    
    public Member(String memberId, String name, String email, MembershipType membershipType) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipType = membershipType;
        this.borrowingHistory = new ArrayList<>();
    }
    
    // Getters and methods
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public MembershipType getMembershipType() { return membershipType; }
    public List<BorrowingRecord> getBorrowingHistory() { return borrowingHistory; }
    
    public int getCurrentBorrowedCount() {
        return (int) borrowingHistory.stream()
            .filter(record -> record.getReturnDate() == null)
            .count();
    }
    
    public boolean canBorrow() {
        return getCurrentBorrowedCount() < membershipType.getMaxBooks();
    }
}

// BorrowingRecord
public class BorrowingRecord {
    private Book book;
    private Member member;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    
    public BorrowingRecord(Book book, Member member, int loanDays) {
        this.book = book;
        this.member = member;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(loanDays);
        this.returnDate = null;
    }
    
    public double calculateFine() {
        if (returnDate == null) {
            LocalDate today = LocalDate.now();
            if (today.isAfter(dueDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
                return daysOverdue * 1.0; // $1 per day
            }
        }
        return 0.0;
    }
    
    // Getters
    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    
    public void returnBook() {
        this.returnDate = LocalDate.now();
        book.setAvailable(true);
    }
    
    @Override
    public String toString() {
        return book.getTitle() + " borrowed on " + borrowDate + 
               ", due on " + dueDate + 
               (returnDate != null ? ", returned on " + returnDate : " (not returned)");
    }
}

// Library Service
public class LibraryService {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private List<BorrowingRecord> allRecords;
    
    public LibraryService() {
        this.books = new HashMap<>();
        this.members = new HashMap<>();
        this.allRecords = new ArrayList<>();
    }
    
    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }
    
    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
    }
    
    public BorrowingRecord borrowBook(String isbn, String memberId) throws Exception {
        Book book = books.get(isbn);
        Member member = members.get(memberId);
        
        if (book == null) {
            throw new Exception("Book not found with ISBN: " + isbn);
        }
        
        if (member == null) {
            throw new Exception("Member not found with ID: " + memberId);
        }
        
        if (!book.isAvailable()) {
            throw new Exception("Book is not available for borrowing");
        }
        
        if (!member.canBorrow()) {
            throw new Exception("Member has reached maximum borrowing limit");
        }
        
        int loanDays = member.getMembershipType().getLoanDays();
        BorrowingRecord record = new BorrowingRecord(book, member, loanDays);
        
        book.setAvailable(false);
        member.getBorrowingHistory().add(record);
        allRecords.add(record);
        
        return record;
    }
    
    public void returnBook(String isbn, String memberId) throws Exception {
        for (BorrowingRecord record : allRecords) {
            if (record.getBook().getIsbn().equals(isbn) && 
                record.getMember().getMemberId().equals(memberId) &&
                record.getReturnDate() == null) {
                
                double fine = record.calculateFine();
                record.returnBook();
                
                if (fine > 0) {
                    System.out.println("Fine charged: $" + fine);
                }
                return;
            }
        }
        throw new Exception("No active borrowing record found");
    }
    
    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(lowerQuery) ||
                book.getAuthor().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        }
        return results;
    }
    
    public void displayAllBooks() {
        System.out.println("=== Library Books ===");
        for (Book book : books.values()) {
            System.out.println(book);
        }
    }
}

// Main class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        LibraryService library = new LibraryService();
        
        // Add books
        library.addBook(new FictionBook("978-0-13-468599-1", "The Great Gatsby", "F. Scott Fitzgerald", "Classic"));
        library.addBook(new FictionBook("978-0-06-112008-4", "To Kill a Mockingbird", "Harper Lee", "Classic"));
        library.addBook(new NonFictionBook("978-0-13-468599-2", "Sapiens", "Yuval Noah Harari", "History"));
        library.addBook(new ReferenceBook("978-0-13-468599-3", "Java Programming", "John Doe", "4th Edition"));
        
        // Register members
        library.registerMember(new Member("M001", "Alice Smith", "alice@email.com", Member.MembershipType.PREMIUM));
        library.registerMember(new Member("M002", "Bob Johnson", "bob@email.com", Member.MembershipType.BASIC));
        
        // Display all books
        library.displayAllBooks();
        System.out.println();
        
        // Borrow books
        try {
            System.out.println("Alice borrows 'The Great Gatsby':");
            BorrowingRecord record1 = library.borrowBook("978-0-13-468599-1", "M001");
            System.out.println(record1);
            
            System.out.println("\nAlice borrows 'To Kill a Mockingbird':");
            BorrowingRecord record2 = library.borrowBook("978-0-06-112008-4", "M001");
            System.out.println(record2);
            
            System.out.println("\nBob borrows 'Sapiens':");
            BorrowingRecord record3 = library.borrowBook("978-0-13-468599-2", "M002");
            System.out.println(record3);
            
            // Search books
            System.out.println("\nSearching for 'Java':");
            List<Book> results = library.searchBooks("Java");
            for (Book book : results) {
                System.out.println(book);
            }
            
            // Return book
            System.out.println("\nAlice returns 'The Great Gatsby':");
            library.returnBook("978-0-13-468599-1", "M001");
            
            // Display updated book status
            System.out.println("\nUpdated book status:");
            library.displayAllBooks();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

**Unit Tests:**
```java
package academy.javaengineering.oop.introduction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {
    private LibraryService library;
    
    @BeforeEach
    void setUp() {
        library = new LibraryService();
        library.addBook(new FictionBook("978-0-13-468599-1", "Test Book", "Test Author", "Test"));
        library.registerMember(new Member("M001", "Test Member", "test@email.com", Member.MembershipType.BASIC));
    }
    
    @Test
    void testBorrowBook() throws Exception {
        BorrowingRecord record = library.borrowBook("978-0-13-468599-1", "M001");
        assertNotNull(record);
        assertFalse(record.getBook().isAvailable());
    }
    
    @Test
    void testBorrowUnavailableBook() {
        assertThrows(Exception.class, () -> {
            library.borrowBook("978-0-13-468599-1", "M001"); // First borrow
            library.borrowBook("978-0-13-468599-1", "M001"); // Second borrow should fail
        });
    }
    
    @Test
    void testReturnBook() throws Exception {
        library.borrowBook("978-0-13-468599-1", "M001");
        library.returnBook("978-0-13-468599-1", "M001");
        
        // Verify book is available again
        assertTrue(library.searchBooks("Test Book").get(0).isAvailable());
    }
    
    @Test
    void testSearchBooks() {
        List<Book> results = library.searchBooks("Test");
        assertEquals(1, results.size());
        assertEquals("Test Book", results.get(0).getTitle());
    }
}
```

**Execution Flow:**
1. Library service is initialized with empty collections
2. Books are added to the library system
3. Members are registered with different membership types
4. Borrowing operation checks book availability and member limits
5. Borrowing records are created with due dates based on membership type
6. Fine calculation occurs when books are returned late
7. Search functionality filters books by title or author

**Complexity:**
- Time: O(1) for borrow/return operations, O(n) for search
- Space: O(n) where n is the number of books and members

**Best Practices:**
- Use inheritance to model different types of books
- Implement proper exception handling for invalid operations
- Use enums for fixed sets of values (membership types)
- Separate business logic from data models
- Use composition for complex relationships

## Exercises

### Easy
1. **Student Class**: Create a `Student` class with fields for name, ID, and GPA. Add methods to calculate if the student is on the honor roll (GPA >= 3.5).

2. **Rectangle Calculator**: Design a `Rectangle` class with width and height. Implement methods to calculate area, perimeter, and determine if it's a square.

3. **Temperature Converter**: Create a `Temperature` class that stores temperature in Celsius. Add methods to convert to Fahrenheit and Kelvin.

### Medium
1. **Bank Account System**: Extend the bank account example to support multiple account types (savings, checking) with different interest rates and fee structures.

2. **Student Management System**: Create a system to manage students, courses, and enrollments. Support adding/removing courses and calculating GPA.

3. **Inventory Management**: Design an inventory system for a store with products, categories, and stock management.

### Hard
1. **Airport Simulation**: Model an airport with planes, gates, flights, and passengers. Handle boarding, delays, and gate assignments.

2. **E-commerce Platform**: Design a complete e-commerce system with products, carts, orders, and payment processing.

3. **Social Network**: Create a simplified social network with users, posts, comments, and friend relationships.

## Interview Questions

### Easy
1. **Q:** What is Object-Oriented Programming?
   **A:** OOP is a programming paradigm that organizes code around objects, which are instances of classes. It focuses on binding data and methods that operate on that data into single units, promoting encapsulation, inheritance, polymorphism, and abstraction.

2. **Q:** What are the four pillars of OOP?
   **A:** The four pillars are:
   - Encapsulation: Hiding internal state and requiring interaction through methods
   - Inheritance: Creating new classes from existing ones
   - Polymorphism: Treating objects of different types through a common interface
   - Abstraction: Hiding complex implementation details behind simpler interfaces

3. **Q:** What is the difference between a class and an object?
   **A:** A class is a blueprint or template that defines the structure and behavior of objects. An object is a specific instance of a class with its own state and can perform the behaviors defined in the class.

### Medium
1. **Q:** Explain the concept of encapsulation with an example.
   **A:** Encapsulation is the bundling of data with methods that operate on that data, restricting direct access to an object's components. Example:
   ```java
   public class BankAccount {
       private double balance; // Hidden from outside
       
       public void deposit(double amount) { // Controlled access
           if (amount > 0) balance += amount;
       }
       
       public double getBalance() { // Controlled access
           return balance;
       }
   }
   ```

2. **Q:** How does Java achieve memory management for objects?
   **A:** Java uses automatic garbage collection. Objects are allocated on the heap when created with `new`. The JVM tracks references to objects. When an object has no more references, it becomes eligible for garbage collection. The garbage collector automatically reclaims memory, preventing memory leaks.

3. **Q:** What is the difference between instance variables and static variables?
   **A:** Instance variables are unique to each object instance. Static variables belong to the class itself and are shared across all instances. Instance variables are stored in heap memory with the object, while static variables are stored in the method area.

### Hard
1. **Q:** Design a class hierarchy for a zoo management system. What design principles would you apply?
   **A:** A possible hierarchy:
   ```
   Animal (abstract)
   ├── Mammal
   │   ├── Lion
   │   ├── Elephant
   │   └── Monkey
   ├── Bird
   │   ├── Eagle
   │   └── Penguin
   └── Reptile
       ├── Snake
       └── Lizard
   ```
   Design principles:
   - Single Responsibility: Each class handles one type of animal
   - Open/Closed: New animal types can be added without modifying existing code
   - Liskov Substitution: Any animal subclass can be used where Animal is expected
   - Interface Segregation: Specific interfaces for flying, swimming abilities
   - Dependency Inversion: High-level modules depend on abstractions (Animal interface)

2. **Q:** Explain the SOLID principles with real-world examples.
   **A:** 
   - Single Responsibility: A `User` class handles user data, not email sending
   - Open/Closed: Payment system accepts new payment methods without modifying existing code
   - Liskov Substitution: Any `Shape` subclass (Circle, Rectangle) can be used interchangeably
   - Interface Segregation: Separate `Readable`, `Writable`, `Flushable` interfaces instead of one large `IOInterface`
   - Dependency Inversion: `OrderProcessor` depends on `PaymentService` interface, not concrete implementation

## Common Pitfalls

### Pitfall 1: Creating God Classes
**Mistake:**
```java
// Bad: Class with too many responsibilities
public class UserManager {
    private List<User> users;
    private EmailService emailService;
    private DatabaseManager database;
    private ReportGenerator reportGenerator;
    private AuditLogger auditLogger;
    
    // Methods for user management, email, database, reports, auditing...
}
```

**Correct:**
```java
// Good: Separate concerns
public class UserService {
    private UserRepository repository;
    private UserValidator validator;
    
    public void createUser(User user) {
        validator.validate(user);
        repository.save(user);
    }
}

public class NotificationService {
    private EmailService emailService;
    
    public void sendWelcomeEmail(User user) {
        // Send email logic
    }
}
```

**Why:** Single Responsibility Principle states that a class should have only one reason to change. Splitting responsibilities makes code more maintainable and testable.

### Pitfall 2: Exposing Internal State
**Mistake:**
```java
// Bad: Exposing internal collection
public class ShoppingCart {
    public List<Item> items; // Public field!
    
    public void addItem(Item item) {
        items.add(item);
    }
}

// External code can modify the list directly
cart.items.clear(); // Bypasses business logic
```

**Correct:**
```java
// Good: Encapsulate internal state
public class ShoppingCart {
    private List<Item> items;
    
    public void addItem(Item item) {
        if (item != null && !items.contains(item)) {
            items.add(item);
        }
    }
    
    public List<Item> getItems() {
        return Collections.unmodifiableList(items); // Return unmodifiable view
    }
    
    public void removeItem(Item item) {
        items.remove(item);
    }
}
```

**Why:** Encapsulation protects object integrity by controlling how internal state is accessed and modified. This prevents invalid states and maintains invariants.

### Pitfall 3: Ignoring Object Lifecycle
**Mistake:**
```java
// Bad: Creating objects without considering lifecycle
public class Connection {
    private Socket socket;
    
    public Connection(String host, int port) {
        socket = new Socket(host, port);
        // No cleanup mechanism
    }
    
    // Socket never closed, leads to resource leak
}
```

**Correct:**
```java
// Good: Proper resource management
public class Connection implements AutoCloseable {
    private Socket socket;
    
    public Connection(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }
    
    @Override
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}

// Usage with try-with-resources
try (Connection conn = new Connection("localhost", 8080)) {
    // Use connection
} // Automatically closed
```

**Why:** Objects that hold resources (files, network connections, database connections) must implement proper cleanup to prevent resource leaks. Using `AutoCloseable` and try-with-resources ensures deterministic cleanup.

## Best Practices
1. **Follow SOLID Principles**: Design classes with single responsibility, open for extension, and closed for modification
2. **Use Meaningful Names**: Class names should be nouns (e.g., `Customer`, `Order`), method names should be verbs (e.g., `calculateTotal`, `sendEmail`)
3. **Prefer Composition Over Inheritance**: Use object composition to achieve code reuse rather than deep inheritance hierarchies
4. **Encapsulate Internal State**: Make fields private and provide controlled access through getters/setters
5. **Design for Change**: Anticipate future modifications by using interfaces, abstract classes, and loose coupling

## Real World Usage

### Spring Framework
Spring uses OOP extensively:
- **Dependency Injection**: Creates objects (beans) and manages their lifecycle
- **AOP (Aspect-Oriented Programming)**: Uses proxies to add cross-cutting concerns
- **MVC Pattern**: Controllers, services, and repositories are separate classes with distinct responsibilities

### Hibernate
Hibernate leverages OOP for ORM:
- **Entity Classes**: Java objects map to database tables
- **Inheritance Mapping**: Supports table-per-hierarchy, table-per-subclass strategies
- **Lazy Loading**: Uses proxy objects to load data on demand

### JDK Source Code
The JDK itself demonstrates OOP principles:
- **Collection Framework**: Interface-based design (`List`, `Set`, `Map`)
- **IO Streams**: Decorator pattern for adding functionality to streams
- **GUI Libraries**: Swing/JavaFX use inheritance and composition for UI components

### Enterprise Applications
Production systems use OOP for:
- **Microservices Architecture**: Each service is a modular, reusable component
- **Domain-Driven Design**: Business logic modeled as domain objects
- **Design Patterns**: Factory, Strategy, Observer patterns for flexible architectures

## References
- [Official Java Documentation](https://docs.oracle.com/en/java/)
- Effective Java by Joshua Bloch
- Head First Design Patterns by Eric Freeman
- [Introduction to Java Programming by Daniel Liang](https://www.cs.uic.edu/~liang/java/book/)

## Summary
- OOP organizes code around objects rather than functions
- The four pillars are Encapsulation, Inheritance, Polymorphism, and Abstraction
- Classes are blueprints, objects are instances of classes
- OOP promotes code reuse, modularity, and maintainability
- Proper design leads to more scalable and testable applications
- Java implements OOP through classes, interfaces, and inheritance

---
**Next Topic:** [Classes](../02-classes/)
**Previous Topic:** [Module Introduction](../../)