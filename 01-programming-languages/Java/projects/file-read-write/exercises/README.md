# File Read/Write Exercises

## Exercise 1: Add XML Processing
Extend the project to support XML file processing.

**Requirements:**
- Create an `XmlProcessor.java` class in `src/`
- Use Java's built-in DOM or SAX parser
- Read XML files and convert to `List<Map<String, Object>>`
- Write data structures to XML format
- Add XML validation against a schema (XSD)

**Hints:**
- Use `javax.xml.parsers.DocumentBuilder` for DOM parsing
- Consider StAX for streaming large XML files
- Add error handling for malformed XML

---

## Exercise 2: Add Database Export
Create functionality to export processed data to a database.

**Requirements:**
- Add a `DatabaseExporter.java` class
- Use JDBC to connect to SQLite or H2 database
- Create tables dynamically from CSV/JSON column headers
- Insert data rows into appropriate tables
- Handle data type mapping (string to SQL types)

**Hints:**
- Use `PreparedStatement` for parameterized queries
- Batch inserts for performance
- Add transaction support

---

## Exercise 3: Add Parallel Processing
Implement parallel file processing for batch operations.

**Requirements:**
- Modify `FileProcessor.processBatch()` to use `ExecutorService`
- Add configurable thread pool size
- Implement progress callback for monitoring
- Add timeout handling for long-running operations
- Ensure thread-safe stats collection

**Hints:**
- Use `CompletableFuture` for async processing
- Consider `ForkJoinPool` for recursive file scanning
- Use `ConcurrentHashMap` for thread-safe stats
