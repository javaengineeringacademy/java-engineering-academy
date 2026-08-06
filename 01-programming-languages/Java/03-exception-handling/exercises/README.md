# Exception Handling Exercises

Practice exception handling in Java through hands-on exercises.

## Exercise 1: Basic Try-Catch

**Problem Statement:**
Write a program that reads an integer from the user and divides it by another integer. Handle the case where the user enters a non-integer value and the case where division by zero occurs.

**Expected Behavior:**
- If the user enters a valid integer for both values, the division result is printed.
- If the first input is not an integer, a helpful error message is displayed.
- If the divisor is zero, a division-by-zero message is displayed.
- The program should not crash in any scenario.

**Hints:**
- Use `Scanner.nextInt()` inside a `try` block.
- Catch `InputMismatchException` for non-integer input.
- Catch `ArithmeticException` for division by zero.

---

## Exercise 2: Finally Block

**Problem Statement:**
Write a method that opens a simulated file (using a string resource), reads from it, and ensures cleanup happens regardless of success or failure. Use the `finally` block to simulate closing the resource.

**Expected Behavior:**
- The method attempts to read from the resource.
- Whether an exception occurs or not, the `finally` block prints a "Resource closed" message.
- If reading fails, the exception is caught and a message is printed before cleanup.

**Hints:**
- Declare a resource variable before the `try` block.
- Set the resource to `null` or print a cleanup message in `finally`.
- Verify the cleanup message appears in both success and failure paths.

---

## Exercise 3: Custom Exceptions

**Problem Statement:**
Create two custom exceptions: `InsufficientFundsException` and `NegativeAmountException`. Write a `BankAccount` class with a `withdraw` method that throws the appropriate custom exception based on the situation.

**Expected Behavior:**
- Withdrawing more than the balance throws `InsufficientFundsException` with the deficit amount.
- Withdrawing a negative amount throws `NegativeAmountException`.
- Valid withdrawals decrease the balance and return the new balance.
- Custom exceptions should extend `Exception` and carry meaningful data.

**Hints:**
- Create a class hierarchy: `BankException extends Exception`.
- Add fields to each custom exception to store context (amount, balance).
- Override `getMessage()` to provide descriptive error messages.

---

## Exercise 4: Exception Propagation

**Problem Statement:**
Write a chain of three methods: `methodA` calls `methodB`, which calls `methodC`. `methodC` throws an exception. Demonstrate both the default propagation behavior and how to catch the exception at different levels.

**Expected Behavior:**
- By default, the exception propagates up the call stack from `methodC` to `methodA`.
- When caught in `methodB`, the exception is handled and does not reach `methodA`.
- When re-thrown with `throw`, the exception continues propagating.
- Stack trace shows the full call chain.

**Hints:**
- Use `try-catch` in each method to observe where exceptions are caught.
- Use `throw` to re-throw exceptions after partial handling.
- Call `printStackTrace()` to inspect the propagation path.

---

## Exercise 5: Resource Cleanup with Try-With-Resources

**Problem Statement:**
Simulate a `Closeable` resource class. Use try-with-resources to ensure automatic cleanup. Compare this approach to manual try-finally cleanup.

**Expected Behavior:**
- The resource's `close()` method is called automatically when the try block exits.
- If both the try block and `close()` throw exceptions, the suppressed exception is attached.
- The `isClosed()` method returns true after the try block.
- Demonstrate that try-with-resources is cleaner than manual try-finally.

**Hints:**
- Implement `Closeable` or `AutoCloseable` on your resource class.
- Throw an exception inside the try block to verify cleanup still occurs.
- Use `getSuppressedExceptions()` to inspect suppressed exceptions.

---

## Exercise 6: Multi-Catch Block

**Problem Statement:**
Write a method that parses a configuration string containing key-value pairs. Handle multiple possible exceptions using a single multi-catch block, then refactor to handle each exception type separately.

**Expected Behavior:**
- `NumberFormatException` is caught for invalid numeric values.
- `ArrayIndexOutOfBoundsException` is caught for malformed key-value pairs.
- `NullPointerException` is caught for null input.
- First version uses `catch (ExceptionA | ExceptionB e)` syntax.
- Second version catches each type individually for specific recovery.

**Hints:**
- Use the pipe `|` operator to combine exception types in one catch block.
- In the multi-catch block, the exception variable is implicitly `final`.
- In separate catch blocks, perform type-specific recovery logic.

---

## Exercise 7: Exception Hierarchy Design

**Problem Statement:**
Design an exception hierarchy for an e-commerce order processing system. Create at least five exception types in a meaningful hierarchy, with each level adding specific context.

**Expected Behavior:**
- A base `OrderException` extends `Exception`.
- Domain-specific exceptions like `PaymentException`, `InventoryException`, `ShippingException` extend `OrderException`.
- Each exception carries relevant fields (orderId, timestamp, details).
- A catch block for `OrderException` catches all subtypes.
- More specific catch blocks can be placed before general ones.

**Hints:**
- Use composition to attach common fields like `orderId` and `timestamp`.
- Add an enum or string field for error codes.
- Override `toString()` for debugging convenience.

---

## Exercise 8: Exception-Driven Control Flow

**Problem Statement:**
Implement a retry mechanism that uses exceptions to control flow. Write a method that attempts an operation up to N times, catching exceptions on each attempt, and returning successfully when the operation succeeds.

**Expected Behavior:**
- The method retries the operation up to the maximum number of attempts.
- Each failed attempt logs the attempt number and error message.
- If all attempts fail, a custom exception is thrown with the list of all errors.
- If an attempt succeeds, the result is returned immediately without further retries.

**Hints:**
- Use a `for` loop with a `try-catch` inside.
- Store each exception in a list for the final failure report.
- Throw a composite exception if all retries are exhausted.
