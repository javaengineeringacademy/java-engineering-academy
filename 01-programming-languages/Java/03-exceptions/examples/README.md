# Java Exception Handling Examples

This directory contains 10 progressive examples demonstrating Java exception handling concepts, from basic try-catch to production-grade error handling patterns.

## Examples List

### 1. **BasicTryCatchExample.java**
- **Concept**: Basic try-catch with ArithmeticException
- **Demonstrates**: Fundamental try-catch syntax, catching specific exceptions, basic error handling
- **Key Learning**: How to handle exceptions and prevent program crashes

### 2. **MultipleCatchExample.java**
- **Concept**: Multiple catch blocks with different exception types
- **Demonstrates**: Catching different exception types, multi-catch syntax (Java 7+), exception hierarchy consideration
- **Key Learning**: Proper catch block ordering and specific exception handling

### 3. **MultiCatchWithThrowExample.java**
- **Concept**: Multi-catch + throw custom exception
- **Demonstrates**: Combining multi-catch syntax with throwing custom exceptions, converting runtime to checked exceptions
- **Key Learning**: Creating custom exceptions and exception translation

### 4. **FinallyCleanupExample.java**
- **Concept**: try-catch-finally with file cleanup
- **Demonstrates**: Finally block for resource cleanup, handling multiple resources, ensuring cleanup occurs
- **Key Learning**: Proper resource management with finally blocks

### 5. **TWRwithExceptionTranslationExample.java**
- **Concept**: Try-with-resources + exception translation
- **Demonstrates**: AutoCloseable resources, TWR syntax, exception translation pattern
- **Key Learning**: Modern resource management and exception abstraction

### 6. **ExceptionChainingExample.java**
- **Concept**: Wrapping exceptions with cause chain
- **Demonstrates**: Exception chaining, maintaining cause chains, walking exception hierarchy
- **Key Learning**: Preserving exception context while providing meaningful abstractions

### 7. **RuntimeExceptionRecoveryExample.java**
- **Concept**: Catching and recovering from RuntimeException
- **Demonstrates**: Null check recovery, default values, retry with fallback, graceful degradation
- **Key Learning**: Recovering from unexpected runtime failures

### 8. **CustomExceptionHierarchyExample.java**
- **Concept**: Custom exception hierarchy with checked/unchecked exceptions
- **Demonstrates**: Designing exception hierarchies, checked vs unchecked exceptions, domain-specific exceptions
- **Key Learning**: Creating proper exception hierarchies for different use cases

### 9. **ProductionRetryExample.java**
- **Concept**: Retry pattern with exponential backoff
- **Demonstrates**: Simple retry, exponential backoff, production-grade retry with configuration
- **Key Learning**: Robust retry mechanisms for transient failures

### 10. **GlobalExceptionHandlerExample.java**
- **Concept**: Thread.UncaughtExceptionHandler + logging
- **Demonstrates**: Thread exception handling, logging patterns, production monitoring
- **Key Learning**: Global exception handling and logging in multi-threaded applications

## Running the Examples

Each example is a complete, runnable Java program with a `main` method. To run any example:

```bash
# Compile the example
javac examples/BasicTryCatchExample.java

# Run the example
java academy.javaengineering.exceptions.examples.BasicTryCatchExample
```

## Learning Path

The examples are designed to be followed in order:
1. Start with **BasicTryCatchExample** for fundamentals
2. Progress through **MultipleCatchExample** and **MultiCatchWithThrowExample**
3. Learn resource management with **FinallyCleanupExample** and **TWRwithExceptionTranslationExample**
4. Understand exception chaining with **ExceptionChainingExample**
5. Master recovery patterns with **RuntimeExceptionRecoveryExample**
6. Design custom exceptions with **CustomExceptionHierarchyExample**
7. Implement production patterns with **ProductionRetryExample**
8. Complete with **GlobalExceptionHandlerExample** for real-world applications

## Key Concepts Covered

- ✅ Basic try-catch syntax
- ✅ Multiple catch blocks and multi-catch
- ✅ Custom exception creation
- ✅ Finally block for cleanup
- ✅ Try-with-resources (TWR)
- ✅ Exception chaining and cause preservation
- ✅ Runtime exception recovery
- ✅ Exception hierarchy design
- ✅ Retry patterns with backoff
- ✅ Global exception handling and logging

## Package Structure

All examples are in the `academy.javaengineering.exceptions.examples` package for consistency with the broader exception handling module.