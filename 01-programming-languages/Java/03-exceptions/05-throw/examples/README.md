# Practice Exercises: The throw Keyword

## Exercise 1: Basic throw
Write a method `divide(int a, int b)` that throws `ArithmeticException` with message `"Division by zero"` when `b` is 0. Otherwise, return `a / b`.

**Expected behavior:**
- `divide(10, 2)` → returns 5
- `divide(10, 0)` → throws `ArithmeticException("Division by zero")`

---

## Exercise 2: Parameter Validation
Write a method `setEmail(String email)` that throws:
- `NullPointerException` if `email` is null
- `IllegalArgumentException` if `email` does not contain `@`

---

## Exercise 3: Exception Chaining
Write a method `readConfig(String path)` that:
1. Calls `readFile(path)` (assume it throws `IOException`)
2. Catches `IOException` and wraps it in a `ConfigException` (custom unchecked exception)
3. The `ConfigException` must preserve the original cause

---

## Exercise 4: Rethrow After Logging
Write a method `processPayment(double amount)` that:
1. Throws `PaymentException` (custom checked exception) if `amount <= 0`
2. If processing fails with any other exception, logs the error and rethrows the original exception

---

## Exercise 5: Multiple Validation Rules
Write a method `createProduct(String name, double price, int stock)` that throws:
- `NullPointerException` if `name` is null
- `IllegalArgumentException` if `name` is empty
- `IllegalArgumentException` if `price` is negative
- `IllegalArgumentException` if `stock` is negative

Each exception message should describe the specific problem.

---

## Exercise 6: Defensive Builder
Write a `Response` class with a `Builder` that validates in `build()`:
- `statusCode` (int) is required — throw `IllegalStateException` if missing
- `body` (String) is required — throw `IllegalStateException` if missing
- `statusCode` must be between 100 and 599 — throw `IllegalArgumentException` if invalid

---

## Exercise 7: Rethrow as Different Type
Write a method `parseInteger(String s)` that:
1. Catches `NumberFormatException`
2. Throws `ParseException` (from `java.text`) with the original message

---

## Exercise 8: Exception Hierarchy
Create a domain exception hierarchy:
- `ServiceException` (unchecked, base)
- `ValidationException extends ServiceException`
- `NotFoundException extends ServiceException`

Write a method `findById(int id)` that throws `NotFoundException` when `id` is negative, and `ValidationException` when `id` is 0.

---

## Exercise 9: Throw in Lambda
Write a method that takes a `List<String>` and a `Predicate<String>`, filters the list, and throws `IllegalArgumentException` if the resulting list is empty after filtering.

---

## Exercise 10: Multi-Cause Exception
Write a method that attempts two operations. If both fail, create an exception that includes both causes. If only one fails, throw that exception. If neither fails, return normally.

---

## Instructions
- Each exercise should be implemented in a single Java file
- Use proper Javadoc comments
- Test each method with valid and invalid inputs
- Verify that exception messages are descriptive
