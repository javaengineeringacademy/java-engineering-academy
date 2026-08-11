# Custom Exceptions Quiz

## Questions

### 1. Custom Exception Base Class
What is the recommended base class for a custom exception that represents a programming error?

A) `Exception`
B) `Error`
C) `RuntimeException`
D) `Throwable`

### 2. Naming Convention
Which naming convention is correct for custom exceptions?

A) `PaymentDeclinedError`
B) `PaymentDeclined`
C) `PaymentDeclinedException`
D) `PaymentExceptionDeclined`

### 3. Constructor Requirement
Which constructors should a custom exception always provide?

A) Message-only and cause-only
B) Message, message+cause, and cause-only
C) Message-only
D) Message+cause only

### 4. Cause Preservation
What happens if you don't pass the original exception to the `super` constructor?

A) The exception is not thrown
B) The original cause is lost
C) The JVM adds it automatically
D) It's logged automatically

### 5. When to Create
Which scenario BEST warrants a custom exception?

A) A null parameter is passed
B) A user account is not found
C) An array index is out of bounds
D) An arithmetic overflow occurs

### 6. serialVersionUID
When is `serialVersionUID` required on a custom exception?

A) Always, for all exceptions
B) Only for checked exceptions
C) Only for exceptions used in RMI
D) Never required

### 7. Error Codes
What is the primary benefit of including error codes in custom exceptions?

A) Better performance
B) More compact stack traces
C) Programmatic error handling and monitoring
D) Smaller class files

### 8. Hierarchy Depth
For a large enterprise application, what is the recommended maximum hierarchy depth?

A) 1 level (flat)
B) 2 levels
C) 3 levels
D) 5 levels

### 9. Exception Factory
Why use an exception factory (static methods) instead of constructors directly?

A) Factories are faster
B) Factories hide constructor complexity and improve readability
C) Constructors are private
D) Factories reduce memory usage

### 10. Flow Control
What is wrong with using exceptions for normal flow control?

A) It's not allowed by the compiler
B) It reduces performance and readability
C) It causes memory leaks
D) It prevents serialization

---

## Answers

1. **C** — `RuntimeException` for programming errors; `Exception` for recoverable errors.
2. **C** — Always suffix with `Exception`.
3. **B** — Message, message+cause, and cause-only covers all standard cases.
4. **B** — The original cause is lost, making debugging harder.
5. **B** — Domain-specific errors benefit from custom exceptions; standard exceptions work for the others.
6. **A** — Best practice is to include it for all serializable exceptions.
7. **C** — Error codes enable programmatic handling and log monitoring.
8. **C** — 3 levels maximum keeps hierarchies manageable.
9. **B** — Factories improve readability and can provide default values.
10. **B** — It hurts performance and makes code harder to understand.
