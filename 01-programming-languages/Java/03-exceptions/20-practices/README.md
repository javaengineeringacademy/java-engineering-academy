# Practice Questions: Java Exception Handling

This directory contains 10 practice questions covering Java exception handling concepts. Complete each question by filling in the TODO sections.

## Questions

### Question 1: Basic Try-Catch
- **File**: `Question01_BasicTryCatch.java`
- **Concept**: Basic try-catch with ArithmeticException
- **Task**: Implement `safeDivide(int a, int b)` that returns `a / b` or `-1` on failure

### Question 2: Multiple Catch
- **File**: `Question02_MultipleCatch.java`
- **Concept**: Multiple catch blocks
- **Task**: Handle both `NumberFormatException` and `ArithmeticException` separately

### Question 3: Multi-Catch with Throw
- **File**: `Question03_MultiCatchWithThrow.java`
- **Concept**: Multi-catch syntax and throwing custom exceptions
- **Task**: Use multi-catch to handle exceptions and throw a custom exception

### Question 4: Finally Cleanup
- **File**: `Question04_FinallyCleanup.java`
- **Concept**: Finally block for guaranteed cleanup
- **Task**: Ensure cleanup code runs regardless of exceptions

### Question 5: Try-with-Resources
- **File**: `Question05_TWRResources.java`
- **Concept**: AutoCloseable resources
- **Task**: Implement a custom `AutoCloseable` resource

### Question 6: Exception Chaining
- **File**: `Question06_ExceptionChaining.java`
- **Concept**: Wrapping exceptions with cause chain
- **Task**: Wrap low-level exceptions in domain exceptions

### Question 7: Custom Checked Exception
- **File**: `Question07_CustomCheckedException.java`
- **Concept**: Creating custom checked exceptions
- **Task**: Create and throw a custom checked exception

### Question 8: Custom Unchecked Exception
- **File**: `Question08_CustomUncheckedException.java`
- **Concept**: Creating custom unchecked exceptions
- **Task**: Create and throw a custom unchecked exception

### Question 9: RuntimeException Recovery
- **File**: `Question09_RuntimeExceptionRecovery.java`
- **Concept**: Recovering from RuntimeException
- **Task**: Catch and recover from runtime exceptions gracefully

### Question 10: Throwable Hierarchy
- **File**: `Question10_ThrowableHierarchy.java`
- **Concept**: Understanding the Throwable type hierarchy
- **Task**: Classify exceptions by their position in the hierarchy

## Instructions

1. Work through questions in order
2. Each question is a complete Java file with a `main` method
3. Fill in the TODO sections to complete each exercise
4. Compile and run to verify your implementation
5. Compare your solutions with the reference solutions in `21-solutions/`
6. Do not look at solutions until you have attempted each question

## Running

```bash
# Compile a question
javac -d out src/academy/javaengineering/exceptions/questions/Question01_BasicTryCatch.java

# Run the question
java -cp out academy.javaengineering.exceptions.questions.Question01_BasicTryCatch
```
