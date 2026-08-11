# References — Try-Catch Exception Handling

---

## Official Documentation

- **Oracle — The try Statement**
  https://docs.oracle.com/javase/tutorial/essential/exceptions/try.html

- **Oracle — Catching and Handling Exceptions**
  https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html

- **Oracle — Multi-catch (Java 7)**
  https://docs.oracle.com/javase/7/docs/technotes/guides/language/catch-multiple.html

- **Java Language Specification — The try Statement**
  https://docs.oracle.com/javase/specs/jls/se17/html/jls-14.html#jls-14.20

- **Java SE API — Throwable Class**
  https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html

---

## Version History

| JDK Version | Feature | JEP/JSR |
|-------------|---------|---------|
| JDK 1.0 | try-catch-finally introduced | JSR 1 |
| JDK 5 | Enhanced for-loop (relevant for iteration patterns) | JSR 166 |
| JDK 7 | Multi-catch (`catch (A | B e)`) | JLS 14.20 |
| JDK 7 | try-with-resources | JLS 14.20.2 |
| JDK 7 | More precise rethrow | JLS 11.2.3 |
| JDK 14 | Switch expressions (arrow syntax with exceptions) | JEP 361 |
| JDK 15 | Text blocks (better exception messages) | JEP 378 |
| JDK 16 | Records (usable as exception carriers) | JEP 395 |
| JDK 17 | Sealed classes (relevant for exception hierarchies) | JEP 409 |

---

## Books

- **"Effective Java" (3rd Edition)** — Joshua Bloch
  Item 69: Use exceptions only for exceptional conditions
  Item 70: Use checked exceptions for recoverable conditions, runtime exceptions for programming errors
  Item 71: Avoid unnecessary use of checked exceptions
  Item 72: Favor the use of standard exceptions
  Item 73: Throw exceptions appropriate to the abstraction
  Item 74: Document all exceptions thrown by each method
  Item 75: Include failure-capture information in detail messages

- **"Java Concurrency in Practice"** — Brian Goetz
  Exception handling patterns in concurrent code

- **"Java: The Complete Reference" (12th Edition)** — Herbert Schildt
  Chapter 11: Exception Handling

- **"Core Java Volume I — Fundamentals" (12th Edition)** — Cay Horstmann
  Chapter 7: Exceptions, Assertions, and Logging

---

## Articles and Tutorials

- **Baeldung — Java Exceptions**
  https://www.baeldung.com/java-exceptions

- **Baeldung — Multi-catch in Java**
  https://www.baeldung.com/java-multi-catch

- **Baeldung — Java Try With Resources**
  https://www.baeldung.com/java-try-with-resources

- **Baeldung — Exception Handling in Lambda Expressions**
  https://www.baeldung.com/java-lambda-exception-handling

- **Oracle — Exceptions (Full Trail)**
  https://docs.oracle.com/javase/tutorial/essential/exceptions/

---

## Related Topics in This Module

- 01 — Exception Hierarchy (Introduction to Throwable, Exception, Error)
- 02 — Checked vs Unchecked Exceptions
- 04 — finally Block and try-with-resources
- 05 — Custom Exceptions
- 06 — Exception Best Practices

---

## Common Exception Types Reference

| Exception | Type | When It Occurs |
|-----------|------|----------------|
| NullPointerException | Runtime | Accessing method/field on null |
| ArrayIndexOutOfBoundsException | Runtime | Invalid array index |
| NumberFormatException | Runtime | Invalid string-to-number conversion |
| ArithmeticException | Runtime | Illegal arithmetic (division by zero) |
| ClassCastException | Runtime | Invalid type cast |
| IllegalArgumentException | Runtime | Invalid method argument |
| IllegalStateException | Runtime | Method called at wrong time |
| UnsupportedOperationException | Runtime | Operation not supported |
| IOException | Checked | I/O operation failure |
| FileNotFoundException | Checked | File not found (subclass of IOException) |
| SQLException | Checked | Database operation failure |
| ClassNotFoundException | Checked | Class not found at runtime |
| InterruptedException | Checked | Thread interrupted during wait |

---

## Cross-Module References

- [I/O Module](../06-io/) — Exception handling in file and stream operations
- [NIO Module](../07-nio/) — Asynchronous exception handling with Channels
- [JDBC Module](../10-jdbc/) — SQLException handling in database operations
- [Concurrency Module](../12-concurrency/) — Thread exception handling and interruption
- [Logging Module](../14-logging/) — Exception logging patterns and frameworks
