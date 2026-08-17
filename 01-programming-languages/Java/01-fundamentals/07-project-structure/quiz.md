# Project Structure - Quiz

## Questions

### Q1: What is the standard Maven project layout?
- A) `src/main/java` for sources, `src/test/java` for tests
- B) `java/src` for all code
- C) Root directory for all files
- D) No standard layout

### Q2: What file defines a Maven project?
- A) build.gradle
- B) pom.xml
- C) Makefile
- D) package.json

### Q3: What is a package in Java?
- A) A compiled class
- B) A namespace for organizing classes
- C) A library
- D) A method

### Q4: What does `com.example.myapp` represent?
- A) A file path
- B) A package name (reversed domain convention)
- C) A class name
- D) A variable

### Q5: What is `module-info.java`?
- A) A configuration file
- B) Module declaration for Java 9+ module system
- C) A test file
- D) A build script

### Q6: What is the purpose of `src/test/java`?
- A) Source code
- B) Test source code
- C) Resources
- D) Documentation

### Q7: What does `pom.xml` define?
- A) Project dependencies and build configuration
- B) Java code
- C) Test cases
- D) Documentation

### Q8: What is a JAR file?
- A) Java Archive - packaged classes and resources
- B) Java Array Resource
- C) Java Application Runner
- D) Java Archive Record

### Q9: What is the `resources` directory for?
- A) Source code
- B) Non-code files (properties, XML, images)
- C) Compiled classes
- D) Dependencies

### Q10: What is the standard test framework for Maven?
- A) JUnit
- B) TestNG
- C) JUnit (with Surefire plugin)
- D) None

## Answers

1. **A** - Maven follows `src/main/java` and `src/test/java` convention
2. **B** - `pom.xml` (Project Object Model) defines the Maven project
3. **B** - Package is a namespace for grouping related classes
4. **B** - Reverse domain name is Java's package naming convention
5. **B** - `module-info.java` declares module exports and dependencies (Java 9+)
6. **B** - Test sources go in `src/test/java`
7. **A** - pom.xml defines dependencies, plugins, and build settings
8. **A** - JAR bundles compiled classes and resources
9. **B** - Resources like properties, XML, and images go here
10. **C** - JUnit with Maven Surefire plugin for test execution
