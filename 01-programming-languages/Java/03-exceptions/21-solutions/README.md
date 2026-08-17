# Solutions: Java Exception Handling Practice Questions

This directory contains complete solutions for all 10 practice questions. Review your own implementation before reading these. Compare your approach; there is often more than one correct solution.

## Solutions

### Solution 1: Basic Try-Catch
- **File**: `Solution01_BasicTryCatch.java`
- **Technique**: Try-catch with return value
- **Key Point**: Catch `ArithmeticException` and return sentinel value

### Solution 2: Multiple Catch
- **File**: `Solution02_MultipleCatch.java`
- **Technique**: Multiple catch blocks
- **Key Point**: Order catch blocks from most specific to most general

### Solution 3: Multi-Catch with Throw
- **File**: `Solution03_MultiCatchWithThrow.java`
- **Technique**: Multi-catch syntax
- **Key Point**: Combine multiple exception types in one catch block

### Solution 4: Finally Cleanup
- **File**: `Solution04_FinallyCleanup.java`
- **Technique**: Finally block
- **Key Point**: Finally always executes, even after return

### Solution 5: Try-with-Resources
- **File**: `Solution05_TWRResources.java`
- **Technique**: AutoCloseable
- **Key Point**: Resources are closed automatically in reverse order

### Solution 6: Exception Chaining
- **File**: `Solution06_ExceptionChaining.java`
- **Technique**: Exception wrapping
- **Key Point**: Preserve cause chain with `(message, cause)` constructor

### Solution 7: Custom Checked Exception
- **File**: `Solution07_CustomCheckedException.java`
- **Technique**: Custom checked exception
- **Key Point**: Extend `Exception` for checked behavior

### Solution 8: Custom Unchecked Exception
- **File**: `Solution08_CustomUncheckedException.java`
- **Technique**: Custom unchecked exception
- **Key Point**: Extend `RuntimeException` for unchecked behavior

### Solution 9: RuntimeException Recovery
- **File**: `Solution09_RuntimeExceptionRecovery.java`
- **Technique**: Exception recovery
- **Key Point**: Catch specific exceptions and provide fallback behavior

### Solution 10: Throwable Hierarchy
- **File**: `Solution10_ThrowableHierarchy.java`
- **Technique**: Type checking with `instanceof`
- **Key Point**: Navigate the Throwable hierarchy with instanceof

## Instructions

1. Attempt each question before viewing its solution
2. Compare your implementation with the reference solution
3. Note differences in approach and style
4. Run each solution to verify behavior matches expectations
5. Focus on understanding the patterns, not just copying code

## Running

```bash
# Compile a solution
javac -d out src/academy/javaengineering/exceptions/solutions/Solution01_BasicTryCatch.java

# Run the solution
java -cp out academy.javaengineering.exceptions.solutions.Solution01_BasicTryCatch
```

## Key Takeaways

- Always catch specific exceptions before general ones
- Use multi-catch when handling multiple exception types the same way
- Always clean up resources in finally or try-with-resources
- Wrap low-level exceptions in domain-specific exceptions
- Preserve the cause chain for debugging
- Use checked exceptions for recoverable conditions
- Use unchecked exceptions for programming errors
