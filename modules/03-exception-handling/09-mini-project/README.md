# Mini Project: Library Management System

## Introduction

Build a complete Library Management System that demonstrates all exception handling concepts learned in this module.

## Learning Objectives

- Apply all exception handling concepts in a complete project
- Design a comprehensive exception hierarchy
- Implement proper error handling across layers
- Create a user-friendly error reporting system

## Prerequisites

- All previous topics in this module
- Understanding of OOP principles
- Basic file I/O operations

## Project Description

Create a Library Management System that manages books, members, and borrowing operations with robust exception handling.

### Requirements

1. **Exception Hierarchy**
   ```
   LibraryException (base)
   ├── BookException
   │   ├── BookNotFoundException
   │   ├── BookNotAvailableException
   │   └── InvalidBookException
   ├── MemberException
   │   ├── MemberNotFoundException
   │   ├── MemberSuspendedException
   │   └── InvalidMemberException
   ├── BorrowException
   │   ├── BorrowLimitExceededException
   │   ├── OverdueBookException
   │   └── BookAlreadyReturnedException
   └── SystemException
       ├── DatabaseException
       └── ConfigurationException
   ```

2. **Core Classes**
   ```java
   public class Book {
       private String isbn;
       private String title;
       private String author;
       private boolean available;

       // Validate book data on creation
       public Book(String isbn, String title, String author) throws InvalidBookException {
           if (isbn == null || isbn.isEmpty()) {
               throw new InvalidBookException("ISBN cannot be empty");
           }
           // ... validation
       }
   }

   public class Member {
       private String memberId;
       private String name;
       private List<BorrowRecord> borrowHistory;
       private boolean suspended;

       public void borrowBook(Book book) throws BorrowException {
           if (suspended) {
               throw new MemberSuspendedException(memberId);
           }
           if (!book.isAvailable()) {
               throw new BookNotAvailableException(book.getIsbn());
           }
           // ... borrowing logic
       }
   }

   public class Library {
       private Map<String, Book> books;
       private Map<String, Member> members;

       public void addBook(Book book) throws SystemException {
           try {
               // Database operation
               books.put(book.getIsbn(), book);
               saveToDatabase(book);
           } catch (DatabaseException e) {
               throw new SystemException("Failed to add book", e);
           }
       }
   }
   ```

3. **Global Exception Handler**
   ```java
   public class LibraryExceptionHandler {
       public ErrorResponse handleException(LibraryException e) {
           // Log exception
           logger.error("Library error: {}", e.getMessage(), e);

           // Return user-friendly response
           return new ErrorResponse(
               e.getErrorCode(),
               e.getMessage(),
               e.getTimestamp()
           );
       }
   }
   ```

## Implementation Steps

1. Create the exception hierarchy
2. Implement the Book class with validation
3. Implement the Member class with borrowing logic
4. Create the Library class with CRUD operations
5. Add file-based persistence with exception handling
6. Create the exception handler
7. Build the main application with user interface
8. Add comprehensive error handling and logging

## Exercises

1. Complete the exception hierarchy with all required exceptions
2. Implement the Book class with proper validation
3. Create the Member class with borrowing rules
4. Build the Library class with file persistence
5. Implement the global exception handler
6. Add logging throughout the application

## Interview Questions

- How would you modify this to work with a real database?
- What additional exceptions might you need for a production system?
- How would you implement undo functionality for failed operations?

## Common Pitfalls

- Not validating data at boundaries
- Losing exception context when wrapping
- Not cleaning up resources on failure
- Making error messages too technical for end users

## Best Practices

1. Validate all inputs at class boundaries
2. Use exception chaining to preserve context
3. Log exceptions with sufficient detail
4. Provide user-friendly error messages
5. Clean up resources in finally blocks
6. Test all exception paths

## Real World Applications

This project structure applies to:
- Library management systems
- Inventory management systems
- Any CRUD application with complex business rules

## References

- [Java Design Patterns](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [Exception Handling Patterns](https://www.oracle.com/technetwork/articles/java/except-137252.html)

## Summary

You have completed the Exception Handling module by building a complete Library Management System. This project demonstrates proper exception hierarchy design, layered exception handling, and user-friendly error reporting. Apply these patterns in your future projects.
